/*
 * Copyright 2015-2016 OpenCB
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.models.converters.data;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.annotation.VariantAnnotation;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantAnnotationConverter;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantSourceEntryConverter;
import org.springframework.core.convert.converter.Converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * Design policies:
 *  - IDS
 *      - The ids of a Variant will NOT be put in the DBObject. Using addToSet(ids) and
 *              setOnInsert(without ids) avoids overwriting ids.
 *      - In a DBObject, both an empty ids array or no ids property, converts to a Variant with an
 *              empty set of ids.
 */
public class DBObjectToVariantConverter implements Converter<DBObject, Variant> {

    public final static String CHROMOSOME_FIELD = "chr";
    public final static String START_FIELD = "start";
    public final static String END_FIELD = "end";
    public final static String LENGTH_FIELD = "len";
    public final static String REFERENCE_FIELD = "ref";
    public final static String ALTERNATE_FIELD = "alt";
//    public final static String ID_FIELD = "id";
    public final static String IDS_FIELD = "ids";
    
    public final static String HGVS_FIELD = "hgvs";
    public final static String TYPE_FIELD = "type";
    public final static String NAME_FIELD = "name";
    
    public final static String FILES_FIELD = "files";
    
    public final static String EFFECTS_FIELD = "effs";
    public final static String SOTERM_FIELD = "so";
    public final static String GENE_FIELD = "gene";
    public final static String ANNOTATION_FIELD = "annot";
    public final static String STATS_FIELD = "st";

    public final static Map<String, String> fieldsMap;

    static {
        fieldsMap = new HashMap<>();
        fieldsMap.put("chromosome", CHROMOSOME_FIELD);
        fieldsMap.put("start", START_FIELD);
        fieldsMap.put("end", END_FIELD);
        fieldsMap.put("length", LENGTH_FIELD);
        fieldsMap.put("reference", REFERENCE_FIELD);
        fieldsMap.put("alternative", ALTERNATE_FIELD);
//        fieldsMap.put("id", ID_FIELD);
        fieldsMap.put("ids", IDS_FIELD);
        fieldsMap.put("hgvs", HGVS_FIELD);
        fieldsMap.put("type", TYPE_FIELD);
//        fields.put("name", NAME_FIELD);
        fieldsMap.put("sourceEntries", FILES_FIELD);
        fieldsMap.put("sourceEntries.cohortStats", STATS_FIELD);
        fieldsMap.put("annotation", ANNOTATION_FIELD);
    }

    private DBObjectToVariantSourceEntryConverter variantSourceEntryConverter;
    private DBObjectToVariantAnnotationConverter variantAnnotationConverter;
    private DBObjectToVariantStatsConverter statsConverter;

    /**
     * Create a converter between Variant and DBObject entities when there is 
     * no need to convert the files the variant was read from.
     */
    public DBObjectToVariantConverter() {
        this(null, null);
    }

    /**
     * Create a converter between Variant and DBObject entities. A converter for 
     * the files the variant was read from can be provided in case those 
     * should be processed during the conversion.
     *
     * @param variantSourceEntryConverter The object used to convert the files
     * @param statsConverter
     */
    public DBObjectToVariantConverter(DBObjectToVariantSourceEntryConverter variantSourceEntryConverter, DBObjectToVariantStatsConverter statsConverter) {
        this.variantSourceEntryConverter = variantSourceEntryConverter;
        this.variantAnnotationConverter = new DBObjectToVariantAnnotationConverter();
        this.statsConverter = statsConverter;
    }

    @Override
    public Variant convert(DBObject object) {
        String chromosome = (String) object.get(CHROMOSOME_FIELD);
        int start = (int) object.get(START_FIELD);
        int end = (int) object.get(END_FIELD);
        String reference = (String) object.get(REFERENCE_FIELD);
        String alternate = (String) object.get(ALTERNATE_FIELD);
        Variant variant = new Variant(chromosome, start, end, reference, alternate);
        if (object.containsField(IDS_FIELD)) {
            Object ids = object.get(IDS_FIELD);
            variant.setIds(new HashSet<String>(((Collection<String>) ids)));
        } else {
            variant.setIds(new HashSet<String>());
        }

        // Transform HGVS: List of map entries -> Map of lists
        BasicDBList mongoHgvs = (BasicDBList) object.get(HGVS_FIELD);
        if (mongoHgvs != null) {
            for (Object o : mongoHgvs) {
                DBObject dbo = (DBObject) o;
                variant.addHgvs((String) dbo.get(TYPE_FIELD), (String) dbo.get(NAME_FIELD));
            }
        }
        
        // Files
        if (variantSourceEntryConverter != null) {
            BasicDBList mongoFiles = (BasicDBList) object.get(FILES_FIELD);
            if (mongoFiles != null) {
                for (Object o : mongoFiles) {
                    DBObject dbo = (DBObject) o;
                    variant.addSourceEntry(variantSourceEntryConverter.convertToDataModelType(dbo));
                }
            }
        }

        // Annotations
        DBObject mongoAnnotation = (DBObject) object.get(ANNOTATION_FIELD);
        if (mongoAnnotation != null) {
            VariantAnnotation annotation = variantAnnotationConverter.convertToDataModelType(mongoAnnotation);
            annotation.setChromosome(variant.getChromosome());
            annotation.setAlternativeAllele(variant.getAlternate());
            annotation.setReferenceAllele(variant.getReference());
            annotation.setStart(variant.getStart());
            variant.setAnnotation(annotation);
        }
        
        // Statistics
        if (statsConverter != null && object.containsField(STATS_FIELD)) {
            DBObject stats = (DBObject) object.get(STATS_FIELD);
            statsConverter.convertCohortsToDataModelType(stats, variant);
        }
        return variant;
    }


}
