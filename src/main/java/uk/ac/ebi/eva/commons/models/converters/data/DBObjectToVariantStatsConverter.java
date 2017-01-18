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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.opencb.biodata.models.feature.Genotype;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.VariantSourceEntry;
import org.opencb.biodata.models.variant.stats.VariantStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.List;
import java.util.Map;

public class DBObjectToVariantStatsConverter implements Converter<DBObject, VariantStats> {

    public final static String COHORT_ID = "cid";
    public final static String STUDY_ID = "sid";
    public final static String FILE_ID = "fid";
    
    public final static String MAF_FIELD = "maf";
    public final static String MGF_FIELD = "mgf";
    public final static String MAFALLELE_FIELD = "mafAl";
    public final static String MGFGENOTYPE_FIELD = "mgfGt";
    public final static String MISSALLELE_FIELD = "missAl";
    public final static String MISSGENOTYPE_FIELD = "missGt";
    public final static String NUMGT_FIELD = "numGt";

    protected static Logger logger = LoggerFactory
            .getLogger(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantStatsConverter.class);

    @Override
    public VariantStats convert(DBObject object) {
        // Basic fields
        VariantStats stats = new VariantStats();
        stats.setMaf(((Double) object.get(MAF_FIELD)).floatValue());
        stats.setMgf(((Double) object.get(MGF_FIELD)).floatValue());
        stats.setMafAllele((String) object.get(MAFALLELE_FIELD));
        stats.setMgfGenotype((String) object.get(MGFGENOTYPE_FIELD));

        stats.setMissingAlleles((int) object.get(MISSALLELE_FIELD));
        stats.setMissingGenotypes((int) object.get(MISSGENOTYPE_FIELD));

        // Genotype counts
        BasicDBObject genotypes = (BasicDBObject) object.get(NUMGT_FIELD);
        for (Map.Entry<String, Object> o : genotypes.entrySet()) {
            String genotypeStr = o.getKey().replace("-1", ".");
            stats.addGenotype(new Genotype(genotypeStr), (int) o.getValue());
        }

        return stats;
    }

    /**
     * As in mongo, a variant is {studies:[],stats:[]} but the data model is {studies:[stats:[]]} this method doesn't 
     * return anything. Instead, the sourceEntries within the variant is filled.
     * @param cohortsStats List from mongo containing VariantStats.
     * @param variant contains allele info to fill the VariantStats, and it sourceEntries will be filled.
     */
    public void convertCohortsToDataModelType(DBObject cohortsStats, Variant variant) {
        if (cohortsStats instanceof List) {
            List<DBObject> cohortStatsList = ((List) cohortsStats);
            for (DBObject vs : cohortStatsList) {
                VariantStats variantStats = convert(vs);
                if (variant != null) {
                    variantStats.setRefAllele(variant.getReference());
                    variantStats.setAltAllele(variant.getAlternate());
                    variantStats.setVariantType(variant.getType());
                    String fid = (String) vs.get(FILE_ID);
                    String sid = (String) vs.get(STUDY_ID);
                    String cid = (String) vs.get(COHORT_ID);
                    VariantSourceEntry sourceEntry;
                    if (fid != null && sid != null && cid != null) {
                       sourceEntry = variant.getSourceEntry(fid, sid);
                        if (sourceEntry != null) {
                            Map<String, VariantStats> cohortStats = sourceEntry.getCohortStats();
                            cohortStats.put(cid, variantStats);
                        } else {
                            logger.warn("ignoring non present source entry fileId={}, studyId={}", fid, sid);
                        }
                    } else {
                        logger.error("invalid mongo document: all studyId={}, fileId={}, cohortId={} should be present.", sid, fid, cid);
                    }
                }
            }
        }
    }
}

