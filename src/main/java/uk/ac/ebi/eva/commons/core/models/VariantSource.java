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

import uk.ac.ebi.eva.commons.core.models.stats.VariantGlobalStats;

import java.util.Date;
import java.util.Map;

/**
 * Basic implementation of {@link IVariantSource}
 */
public class VariantSource implements IVariantSource {

    private String fileId;

    private String fileName;

    private String studyId;

    private String studyName;

    private StudyType type;

    private Aggregation aggregation;

    private Date date;

    private Map<String, Integer> samplesPosition;

    private Map<String, Object> metadata;

    private VariantGlobalStats stats;

    public VariantSource(IVariantSource variantSource) {
        this(
                variantSource.getFileId(),
                variantSource.getFileName(),
                variantSource.getStudyId(),
                variantSource.getStudyName(),
                variantSource.getType(),
                variantSource.getAggregation(),
                variantSource.getDate(),
                variantSource.getSamplesPosition(),
                variantSource.getMetadata(),
                new VariantGlobalStats(variantSource.getStats())
        );
    }

    public VariantSource(String fileId, String fileName, String studyId, String studyName, StudyType type,
                         Aggregation aggregation, Date date, Map<String, Integer> samplesPosition,
                         Map<String, Object> metadata, VariantGlobalStats stats) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.studyId = studyId;
        this.studyName = studyName;
        this.type = type;
        this.aggregation = aggregation;
        this.date = date;
        this.samplesPosition = samplesPosition;
        this.metadata = metadata;
        this.stats = stats;
    }

    @Override
    public String getFileId() {
        return fileId;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getStudyId() {
        return studyId;
    }

    @Override
    public String getStudyName() {
        return studyName;
    }

    @Override
    public StudyType getType() {
        return type;
    }

    @Override
    public Aggregation getAggregation() {
        return aggregation;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public Map<String, Integer> getSamplesPosition() {
        return samplesPosition;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    @Override
    public VariantGlobalStats getStats() {
        return stats;
    }
}
