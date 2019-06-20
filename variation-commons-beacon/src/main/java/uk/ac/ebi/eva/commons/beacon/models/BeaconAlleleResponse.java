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

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BeaconAlleleResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class BeaconAlleleResponse   {
    @JsonProperty("beaconId")
    private String beaconId = null;

    @JsonProperty("apiVersion")
    private String apiVersion = null;

    @JsonProperty("exists")
    private Boolean exists = null;

    @JsonProperty("alleleRequest")
    private BeaconAlleleRequest alleleRequest = null;

    @JsonProperty("datasetAlleleResponses")
    @Valid
    private List<BeaconDatasetAlleleResponse> datasetAlleleResponses = null;

    @JsonProperty("error")
    private BeaconError error = null;

    public BeaconAlleleResponse beaconId(String beaconId) {
        this.beaconId = beaconId;
        return this;
    }

    /**
     * Identifier of the beacon, as defined in `Beacon`.
     * @return beaconId
     **/
    @ApiModelProperty(required = true, value = "Identifier of the beacon, as defined in `Beacon`.")
    @NotNull

    public String getBeaconId() {
        return beaconId;
    }

    public void setBeaconId(String beaconId) {
        this.beaconId = beaconId;
    }

    public BeaconAlleleResponse apiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    /**
     * Version of the API. If specified, the value must match `apiVersion` in Beacon
     * @return apiVersion
     **/
    @ApiModelProperty(value = "Version of the API. If specified, the value must match `apiVersion` in Beacon")

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public BeaconAlleleResponse exists(Boolean exists) {
        this.exists = exists;
        return this;
    }

    /**
     * Indicator of whether the given allele was observed in any of the datasets queried. This should be non-null, unless there was an error, in which case `error` has to be non-null.
     * @return exists
     **/
    @ApiModelProperty(value = "Indicator of whether the given allele was observed in any of the datasets queried. This should be non-null, unless there was an error, in which case `error` has to be non-null.")

    public Boolean isExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public BeaconAlleleResponse alleleRequest(BeaconAlleleRequest alleleRequest) {
        this.alleleRequest = alleleRequest;
        return this;
    }

    /**
     * Get alleleRequest
     * @return alleleRequest
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BeaconAlleleRequest getAlleleRequest() {
        return alleleRequest;
    }

    public void setAlleleRequest(BeaconAlleleRequest alleleRequest) {
        this.alleleRequest = alleleRequest;
    }

    public BeaconAlleleResponse datasetAlleleResponses(List<BeaconDatasetAlleleResponse> datasetAlleleResponses) {
        this.datasetAlleleResponses = datasetAlleleResponses;
        return this;
    }

    public BeaconAlleleResponse addDatasetAlleleResponsesItem(BeaconDatasetAlleleResponse datasetAlleleResponsesItem) {
        if (this.datasetAlleleResponses == null) {
            this.datasetAlleleResponses = new ArrayList<BeaconDatasetAlleleResponse>();
        }
        this.datasetAlleleResponses.add(datasetAlleleResponsesItem);
        return this;
    }

    /**
     * Indicator of whether the given allele was  observed in individual datasets. This should be non-null if `includeDatasetResponses` in the corresponding `BeaconAlleleRequest` is true, and null otherwise.
     * @return datasetAlleleResponses
     **/
    @ApiModelProperty(value = "Indicator of whether the given allele was  observed in individual datasets. This should be non-null if `includeDatasetResponses` in the corresponding `BeaconAlleleRequest` is true, and null otherwise.")
    @Valid
    public List<BeaconDatasetAlleleResponse> getDatasetAlleleResponses() {
        return datasetAlleleResponses;
    }

    public void setDatasetAlleleResponses(List<BeaconDatasetAlleleResponse> datasetAlleleResponses) {
        this.datasetAlleleResponses = datasetAlleleResponses;
    }

    public BeaconAlleleResponse error(BeaconError error) {
        this.error = error;
        return this;
    }

    /**
     * Get error
     * @return error
     **/
    @ApiModelProperty(value = "")

    @Valid
    public BeaconError getError() {
        return error;
    }

    public void setError(BeaconError error) {
        this.error = error;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BeaconAlleleResponse beaconAlleleResponse = (BeaconAlleleResponse) o;
        return Objects.equals(this.beaconId, beaconAlleleResponse.beaconId) &&
                Objects.equals(this.apiVersion, beaconAlleleResponse.apiVersion) &&
                Objects.equals(this.exists, beaconAlleleResponse.exists) &&
                Objects.equals(this.alleleRequest, beaconAlleleResponse.alleleRequest) &&
                Objects.equals(this.datasetAlleleResponses, beaconAlleleResponse.datasetAlleleResponses) &&
                Objects.equals(this.error, beaconAlleleResponse.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beaconId, apiVersion, exists, alleleRequest, datasetAlleleResponses, error);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BeaconAlleleResponse {\n");

        sb.append("    beaconId: ").append(toIndentedString(beaconId)).append("\n");
        sb.append("    apiVersion: ").append(toIndentedString(apiVersion)).append("\n");
        sb.append("    exists: ").append(toIndentedString(exists)).append("\n");
        sb.append("    alleleRequest: ").append(toIndentedString(alleleRequest)).append("\n");
        sb.append("    datasetAlleleResponses: ").append(toIndentedString(datasetAlleleResponses)).append("\n");
        sb.append("    error: ").append(toIndentedString(error)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
