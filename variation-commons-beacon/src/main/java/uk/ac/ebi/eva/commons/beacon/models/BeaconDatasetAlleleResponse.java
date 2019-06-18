/*
 * European Variation Archive (EVA) - Open-access database of all types of genetic
 * variation data from all species
 *
 * Copyright 2019 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.eva.commons.beacon.models;

import java.util.HashMap;

/**
 * Response containing information about an allele in a particular dataset, and made against a Beacon.
 */
public class BeaconDatasetAlleleResponse {

    private String datasetId;

    private boolean exists;

    private BeaconError error;

    private Float frequency;

    private Long variantCount;

    private Long callCount;

    private Long sampleCount;

    private String note;

    private String externalUrl;

    private HashMap<String,String> info;

    public BeaconDatasetAlleleResponse() { }

    public BeaconDatasetAlleleResponse(String datasetId, boolean exists) {
        this.datasetId = datasetId;
        this.exists = exists;
    }

    public BeaconDatasetAlleleResponse(String datasetId, boolean exists, BeaconError error, Float frequency,
                                       Long variantCount, Long callCount, Long sampleCount, String note,
                                       String externalUrl, HashMap<String, String> info) {
        this.datasetId = datasetId;
        this.exists = exists;
        this.error = error;
        this.frequency = frequency;
        this.variantCount = variantCount;
        this.callCount = callCount;
        this.sampleCount = sampleCount;
        this.note = note;
        this.externalUrl = externalUrl;
        this.info = info;
    }

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public BeaconError getError() {
        return error;
    }

    public void setError(BeaconError error) {
        this.error = error;
    }

    public Float getFrequency() {
        return frequency;
    }

    public void setFrequency(Float frequency) {
        this.frequency = frequency;
    }

    public Long getVariantCount() {
        return variantCount;
    }

    public void setVariantCount(Long variantCount) {
        this.variantCount = variantCount;
    }

    public Long getCallCount() {
        return callCount;
    }

    public void setCallCount(Long callCount) {
        this.callCount = callCount;
    }

    public Long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(Long sampleCount) {
        this.sampleCount = sampleCount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public HashMap<String, String> getInfo() {
        return info;
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }
}
