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
package uk.ac.ebi.eva.commons.mongodb.converters;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.VariantSourceEntry;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBObjectToVariantSourceEntryConverter implements Converter<DBObject, VariantSourceEntry> {

    public final static String FILEID_FIELD = "fid";
    public final static String STUDYID_FIELD = "sid";
    public final static String ALTERNATES_FIELD = "alts";
    public final static String ATTRIBUTES_FIELD = "attrs";
    public final static String FORMAT_FIELD = "fm";
    public final static String SAMPLES_FIELD = "samp";
    static final char CHARACTER_TO_REPLACE_DOTS = (char) 163; // <-- £

    private DBObjectToSamplesConverter samplesConverter;

    /**
     * Create a converter between VariantSourceEntry and DBObject entities when 
     * there is no need to provide a list of samples or statistics.
     */
    public DBObjectToVariantSourceEntryConverter() {
        this.samplesConverter = null;
    }


    /**
     * Create a converter from VariantSourceEntry to DBObject entities. A
     * samples converter and a statistics converter may be provided in case those
     * should be processed during the conversion.
     * @param samplesConverter The object used to convert the samples. If null, won't convert
     *
     */
    public DBObjectToVariantSourceEntryConverter(DBObjectToSamplesConverter samplesConverter) {
        this.samplesConverter = samplesConverter;
    }

    @Override
    public VariantSourceEntry convert(DBObject object) {
        String fileId = (String) object.get(FILEID_FIELD);
        String studyId = (String) object.get(STUDYID_FIELD);
        VariantSourceEntry file = new VariantSourceEntry(fileId, studyId);
        
        // Alternate alleles
        if (object.containsField(ALTERNATES_FIELD)) {
            List list = (List) object.get(ALTERNATES_FIELD);
            String[] alternatives = new String[list.size()];
            int i = 0;
            for (Object o : list) {
                alternatives[i] = o.toString();
                i++;
            }
            file.setSecondaryAlternates(alternatives);
        }
        
        // Attributes
        if (object.containsField(ATTRIBUTES_FIELD)) {
            BasicDBObject attributes = (BasicDBObject) object.get(ATTRIBUTES_FIELD);
            for (Map.Entry<String, Object> o : attributes.entrySet()) {
                file.addAttribute(o.getKey().replace(CHARACTER_TO_REPLACE_DOTS, '.'), o.getValue().toString());
            }
            
            // Unzip the "src" field, if available
            if (((DBObject) object.get(ATTRIBUTES_FIELD)).containsField("src")) {
                byte[] o = (byte[]) ((DBObject) object.get(ATTRIBUTES_FIELD)).get("src");
                try {
                    file.addAttribute("src", org.opencb.commons.utils.StringUtils.gunzip(o));
                } catch (IOException ex) {
                    Logger.getLogger(
                            uk.ac.ebi.eva.commons.mongodb.converters.DBObjectToVariantSourceEntryConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        if (object.containsField(FORMAT_FIELD)) {
            file.setFormat((String) object.get(FORMAT_FIELD));
        }
        
        // Samples
        if (samplesConverter != null && object.containsField(SAMPLES_FIELD)) {
            VariantSourceEntry fileWithSamplesData = samplesConverter.convert(object);
            
            // Add the samples to the Java object, combining the data structures
            // with the samples' names and the genotypes
            for (Map.Entry<String, Map<String, String>> sampleData : fileWithSamplesData.getSamplesData().entrySet()) {
                file.addSampleData(sampleData.getKey(), sampleData.getValue());
            }
        }
        
        return file;
    }
}
