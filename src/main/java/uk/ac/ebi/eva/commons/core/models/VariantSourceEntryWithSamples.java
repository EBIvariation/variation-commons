/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.core.models;

import uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantSourceEntryMongo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VariantSourceEntryWithSamples extends AbstractVariantSourceEntry implements IVariantSourceEntry {

    /**
     * Genotypes and other sample-related information. The keys are the names
     * of the samples. The values are pairs (field name, field value), such as
     * (GT, A/C).
     * <p>
     * This field needs to be a linked hash map to obtain ordered keys and values
     */
    private LinkedHashMap<String, Map<String, String>> samplesData;

    public VariantSourceEntryWithSamples(IVariantSourceEntry variantSourceEntry, List<String> sampleNames) {
        this(
                variantSourceEntry.getFileId(),
                variantSourceEntry.getStudyId(),
                variantSourceEntry.getSecondaryAlternates(),
                variantSourceEntry.getFormat(),
                variantSourceEntry.getCohortStats(),
                variantSourceEntry.getAttributes(),
                joinSamplesData(variantSourceEntry.getSamplesData(), sampleNames)
        );
    }

    public VariantSourceEntryWithSamples(VariantSourceEntryMongo variantSourceEntryMongo, List<String> sampleNames) {
        this(
                variantSourceEntryMongo.getFileId(),
                variantSourceEntryMongo.getStudyId(),
                variantSourceEntryMongo.getSecondaryAlternates(),
                variantSourceEntryMongo.getFormat(),
                null,
                variantSourceEntryMongo.getAttributes(),
                joinSamplesData(variantSourceEntryMongo.deflateSamplesData(sampleNames.size()), sampleNames)
        );
    }

    public VariantSourceEntryWithSamples(String fileId, String studyId, String[] secondaryAlternates, String format,
                                         Map<String, VariantStats> cohortStats, Map<String, String> attributes,
                                         LinkedHashMap<String, Map<String, String>> samplesData) {
        super(fileId, studyId, secondaryAlternates, format, cohortStats, attributes);
        this.samplesData = new LinkedHashMap<>();
        if (samplesData != null) {
            this.samplesData.putAll(samplesData);
        }
    }

    public List<Map<String, String>> getSamplesData() {
        return new ArrayList<>(samplesData.values());
    }

    public Map<String, Map<String, String>> getSamplesDataMap() {
        return samplesData;
    }

    private static LinkedHashMap<String, Map<String, String>> joinSamplesData(List<Map<String, String>> samplesData,
                                                                              List<String> samples) {
        LinkedHashMap<String, Map<String, String>> temp = new LinkedHashMap<>();
        int numberOfSamples = Math.min(samples.size(), samplesData.size());
        for (int i = 0; i < numberOfSamples; i++) {
            temp.put(samples.get(i), samplesData.get(i));
        }
        return temp;
    }
}
