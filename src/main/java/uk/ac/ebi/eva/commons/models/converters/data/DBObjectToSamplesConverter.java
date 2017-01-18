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

//    private boolean compressSamples;
//    private List<String> samples;
    private Map<String, Integer> sampleIds;
    private Map<Integer, String> idSamples;
    private boolean compressDefaultGenotype;

    /**
     * Create a converter from a Map of samples to DBObject entities.
     * 
     * @param compressDefaultGenotype Whether to compress samples default genotype or not
     */
    public DBObjectToSamplesConverter(boolean compressDefaultGenotype) {
        this.idSamples = null;
        this.sampleIds = null;
        this.compressDefaultGenotype = compressDefaultGenotype;
    }

    /**
     * Create a converter from DBObject to a Map of samples, providing the list 
     * of sample names.
     * 
     * @param samples The list of samples, if any
     */
    public DBObjectToSamplesConverter(List<String> samples) {
        this(true);
        setSamples(samples);
    }

    /**
     * Create a converter from DBObject to a Map of samples, providing a map
     * of sample names to the corresponding sample id.
     *
     * @param sampleIds Map of samples to sampleId
     */
    public DBObjectToSamplesConverter(boolean compressDefaultGenotype, Map<String, Integer> sampleIds) {
        this(compressDefaultGenotype);
        setSampleIds(sampleIds);
    }


    @Override
    public VariantSourceEntry convert(DBObject object) {
        
        if (sampleIds == null || sampleIds.isEmpty()) {
            return new VariantSourceEntry(object.get(FILEID_FIELD).toString(), object.get(STUDYID_FIELD).toString());
        }
        
        BasicDBObject mongoGenotypes = (BasicDBObject) object.get(SAMPLES_FIELD);
//        int numSamples = idSamples.size();
        
        // Temporary file, just to store the samples
        VariantSourceEntry fileWithSamples = new VariantSourceEntry(object.get(FILEID_FIELD).toString(), 
                object.get(STUDYID_FIELD).toString());

        // Add the samples to the file
        for (Map.Entry<String, Integer> entry : sampleIds.entrySet()) {
            Map<String, String> sampleData = new HashMap<>(1);  //size == 1, only will contain GT field
            fileWithSamples.addSampleData(entry.getKey(), sampleData);
        }

        // An array of genotypes is initialized with the most common one
        String mostCommonGtString = mongoGenotypes.getString("def");
        if(mostCommonGtString != null) {
            for (Map.Entry<String, Map<String, String>> entry : fileWithSamples.getSamplesData().entrySet()) {
                entry.getValue().put("GT", mostCommonGtString);
            }
        }

        // Loop through the non-most commmon genotypes, and set their value
        // in the position specified in the array, such as:
        // "0|1" : [ 41, 311, 342, 358, 881, 898, 903 ]
        // genotypes[41], genotypes[311], etc, will be set to "0|1"
        for (Map.Entry<String, Object> dbo : mongoGenotypes.entrySet()) {
            if (!dbo.getKey().equals("def")) {
                String genotype = dbo.getKey().replace("-1", ".");
                for (Integer sampleId : (List<Integer>) dbo.getValue()) {
                    if (idSamples.containsKey(sampleId)) {
                        fileWithSamples.getSamplesData().get(idSamples.get(sampleId)).put("GT", genotype);
                    }
                }
            }
        }


        
        return fileWithSamples;
    }

    public void setSamples(List<String> samples) {
        int i = 0;
        assert samples != null;
        int size = samples.size();
        sampleIds = new HashMap<>(size);
        idSamples = new HashMap<>(size);
        for (String sample : samples) {
            sampleIds.put(sample, i);
            idSamples.put(i, sample);
            i++;
        }
    }

    public void setSampleIds(Map<String, Integer> sampleIds) {
        this.sampleIds = new HashMap<String, Integer>(sampleIds);
        this.idSamples = new HashMap<>(this.sampleIds.size());
        for (Map.Entry<String, Integer> entry : this.sampleIds.entrySet()) {
            idSamples.put(entry.getValue(), entry.getKey());
        }
        assert sampleIds.size() == idSamples.size();
    }
}
