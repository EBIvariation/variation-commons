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
package uk.ac.ebi.eva.commons.core.models.ws;

import uk.ac.ebi.eva.commons.core.models.AbstractVariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.IVariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.VariantStatistics;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VariantSourceEntryWithSampleNames extends AbstractVariantSourceEntry implements IVariantSourceEntry {

    /**
     * Genotypes and other sample-related information. The keys are the names
     * of the samples. The values are pairs (field name, field value), such as
     * (GT, A/C).
     * <p>
     * This field needs to be a linked hash map to obtain ordered keys and values
     */
    private LinkedHashMap<String, Map<String, String>> samplesData;

    public VariantSourceEntryWithSampleNames(IVariantSourceEntry variantSourceEntry, List<String> sampleNames) {
        this(
                variantSourceEntry.getFileId(),
                variantSourceEntry.getStudyId(),
                variantSourceEntry.getSecondaryAlternates(),
                variantSourceEntry.getFormat(),
                variantSourceEntry.getCohortStats(),
                variantSourceEntry.getAttributes(),
                joinSamplesDataWithSampleNames(variantSourceEntry.getSamplesData(), sampleNames)
        );
    }

    public VariantSourceEntryWithSampleNames(VariantSourceEntryMongo variantSourceEntryMongo, List<String> sampleNames) {
        this(
                variantSourceEntryMongo.getFileId(),
                variantSourceEntryMongo.getStudyId(),
                variantSourceEntryMongo.getSecondaryAlternates(),
                variantSourceEntryMongo.getFormat(),
                null,
                variantSourceEntryMongo.getAttributes(),
                joinSamplesDataWithSampleNames(variantSourceEntryMongo.deflateSamplesData(sampleNames.size()), sampleNames)
        );
    }

    public VariantSourceEntryWithSampleNames(String fileId, String studyId, String[] secondaryAlternates, String format,
                                             Map<String, VariantStatistics> cohortStats, Map<String, String> attributes,
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

    /**
     * Joins the list of sample data with their correspondent name from the sample list
     *
     * @param samplesData
     * @param samples
     * @return
     */
    private static LinkedHashMap<String, Map<String, String>> joinSamplesDataWithSampleNames(
            List<Map<String, String>> samplesData,
            List<String> samples) {
        LinkedHashMap<String, Map<String, String>> temp = new LinkedHashMap<>();
        int numberOfSamples = Math.min(samples.size(), samplesData.size());
        for (int i = 0; i < numberOfSamples; i++) {
            temp.put(samples.get(i), samplesData.get(i));
        }
        return temp;
    }
}
