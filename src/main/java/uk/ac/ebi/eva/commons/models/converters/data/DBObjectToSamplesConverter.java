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
import org.opencb.biodata.models.variant.VariantSourceEntry;
import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantSourceEntryConverter.FILEID_FIELD;
import static uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantSourceEntryConverter.SAMPLES_FIELD;
import static uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantSourceEntryConverter.STUDYID_FIELD;

public class DBObjectToSamplesConverter implements Converter<DBObject, VariantSourceEntry> {

    @Override
    public VariantSourceEntry convert(DBObject object) {
        BasicDBObject mongoGenotypes = (BasicDBObject) object.get(SAMPLES_FIELD);

        VariantSourceEntry fileWithSamples = new VariantSourceEntry(object.get(FILEID_FIELD).toString(), 
                object.get(STUDYID_FIELD).toString());

        // Add the samples to the file
        for (Map.Entry<String, Object> gtToIndexes: mongoGenotypes.entrySet()) {
            if (gtToIndexes.getKey().equals("def")) {
                fileWithSamples = addGenotypeToVariantSourceEntry(fileWithSamples, "def",
                                                                  (String) gtToIndexes.getValue());
            } else {
                String gtString = gtToIndexes.getKey();
                List<Integer> indexes = (List<Integer>) gtToIndexes.getValue();
                for (Integer index : indexes) {
                    fileWithSamples = addGenotypeToVariantSourceEntry(fileWithSamples, Integer.toString(index),
                                                                      gtString);
                }
            }
        }
        
        return fileWithSamples;
    }

    private VariantSourceEntry addGenotypeToVariantSourceEntry(VariantSourceEntry file, String index,
                                                               String gtString) {
        Map<String, String> sampleData = new HashMap<>(1);
        sampleData.put("GT", gtString);
        file.addSampleData(index, sampleData);
        return file;
    }
}
