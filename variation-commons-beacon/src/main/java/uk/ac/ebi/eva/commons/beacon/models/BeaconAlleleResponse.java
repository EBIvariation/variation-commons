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

import java.util.List;

public class BeaconAlleleResponse {

    private String beaconId;

    private String apiVersion;

    private Boolean exists;

    private BeaconAlleleRequest alleleRequest;

    private BeaconError error;

    private List<BeaconDatasetAlleleResponse> datasetAlleleResponses;

    public BeaconAlleleResponse() { }

    public BeaconAlleleResponse(String beaconId, String apiVersion, Boolean exists,
                                BeaconAlleleRequest alleleRequest, BeaconError error,
                                List<BeaconDatasetAlleleResponse> datasetAlleleResponses) {
        this.beaconId = beaconId;
        this.apiVersion = apiVersion;
        this.exists = exists;
        this.alleleRequest = alleleRequest;
        this.error = error;
        this.datasetAlleleResponses = datasetAlleleResponses;
    }

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Boolean getExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public BeaconAlleleRequest getAlleleRequest() {
        return alleleRequest;
    }

    public void setAlleleRequest(BeaconAlleleRequest alleleRequest) {
        this.alleleRequest = alleleRequest;
    }

    public BeaconError getError() {
        return error;
    }

    public void setError(BeaconError error) {
        this.error = error;
    }

    public List<BeaconDatasetAlleleResponse> getDatasetAlleleResponses() {
        return datasetAlleleResponses;
    }

    public void setDatasetAlleleResponses(List<BeaconDatasetAlleleResponse> datasetAlleleResponses) {
        this.datasetAlleleResponses = datasetAlleleResponses;
    }
}
