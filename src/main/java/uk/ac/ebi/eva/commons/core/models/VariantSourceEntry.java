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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Entry that associates a variant and a file in a variant archive. It contains
 * information related to samples, statistics and specifics of the file format.
 */
public class VariantSourceEntry extends AbstractVariantSourceEntry implements IVariantSourceEntry {

    /**
     * Genotypes and other sample-related information. The keys are the names
     * of the samples. The values are pairs (field name, field value), such as
     * (GT, A/C).
     */
    private final List<Map<String, String>> samplesData;

    public VariantSourceEntry(String fileId, String studyId) {
        this(fileId, studyId, null, null, null, null, null);
    }

    public VariantSourceEntry(VariantSourceEntryMongo variantSourceEntryMongo, int totalSamples) {
        this(
                variantSourceEntryMongo.getFileId(),
                variantSourceEntryMongo.getStudyId(),
                variantSourceEntryMongo.getSecondaryAlternates(),
                variantSourceEntryMongo.getFormat(),
                null,
                variantSourceEntryMongo.getAttributes(),
                variantSourceEntryMongo.deflateSamplesData(totalSamples)
        );
    }

    public VariantSourceEntry(String fileId, String studyId, String[] secondaryAlternates, String format,
                              Map<String, VariantStats> cohortStats, Map<String, String> attributes,
                              List<Map<String, String>> samplesData) {
        super(fileId, studyId, secondaryAlternates, format, cohortStats, attributes);
        this.samplesData = new ArrayList<>();
        if (samplesData != null) {
            this.samplesData.addAll(samplesData);
        }
    }

    public List<Map<String, String>> getSamplesData() {
        return samplesData;
    }

    public String getSampleData(int sampleIndex, String field) {
        return getSampleData(sampleIndex).get(field.toUpperCase());
    }

    public Map<String, String> getSampleData(int sampleIndex) {
        return samplesData.get(sampleIndex);
    }

    /**
     * Adds information about a new sample to associate to this VariantSourceEntry.
     *
     * @param sampleData Sample information to be added
     * @return The index where the sample was inserted
     */
    public int addSampleData(Map<String, String> sampleData) {
        this.samplesData.add(sampleData);
        return this.samplesData.size() - 1;
    }

}
