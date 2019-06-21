/*
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
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * BeaconDatasetAlleleResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class BeaconDatasetAlleleResponse   {
    @JsonProperty("datasetId")
    private String datasetId = null;

    @JsonProperty("exists")
    private Boolean exists = null;

    @JsonProperty("error")
    private BeaconError error = null;

    @JsonProperty("frequency")
    private BigDecimal frequency = null;

    @JsonProperty("variantCount")
    private Long variantCount = null;

    @JsonProperty("callCount")
    private Long callCount = null;

    @JsonProperty("sampleCount")
    private Long sampleCount = null;

    @JsonProperty("note")
    private String note = null;

    @JsonProperty("externalUrl")
    private String externalUrl = null;

    @JsonProperty("info")
    @Valid
    private List<KeyValuePair> info = null;

    public BeaconDatasetAlleleResponse datasetId(String datasetId) {
        this.datasetId = datasetId;
        return this;
    }

    /**
     * not provided
     * @return datasetId
     **/
    @ApiModelProperty(required = true, value = "not provided")
    @NotNull

    public String getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(String datasetId) {
        this.datasetId = datasetId;
    }

    public BeaconDatasetAlleleResponse exists(Boolean exists) {
        this.exists = exists;
        return this;
    }

    /**
     * Indicator of whether the given allele was observed in the dataset. This should be non-null, unless there was an error, in which case `error` has to be non-null.
     * @return exists
     **/
    @ApiModelProperty(value = "Indicator of whether the given allele was observed in the dataset. This should be non-null, unless there was an error, in which case `error` has to be non-null.")

    public Boolean isExists() {
        return exists;
    }

    public void setExists(Boolean exists) {
        this.exists = exists;
    }

    public BeaconDatasetAlleleResponse error(BeaconError error) {
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

    public BeaconDatasetAlleleResponse frequency(BigDecimal frequency) {
        this.frequency = frequency;
        return this;
    }

    /**
     * Frequency of this allele in the dataset. Between 0 and 1, inclusive.
     * minimum: 0
     * maximum: 1
     * @return frequency
     **/
    @ApiModelProperty(value = "Frequency of this allele in the dataset. Between 0 and 1, inclusive.")

    @Valid
    @DecimalMin("0") @DecimalMax("1")   public BigDecimal getFrequency() {
        return frequency;
    }

    public void setFrequency(BigDecimal frequency) {
        this.frequency = frequency;
    }

    public BeaconDatasetAlleleResponse variantCount(Long variantCount) {
        this.variantCount = variantCount;
        return this;
    }

    /**
     * Number of variants matching the allele request in the dataset.
     * minimum: 0
     * @return variantCount
     **/
    @ApiModelProperty(value = "Number of variants matching the allele request in the dataset.")

    @Min(0L)  public Long getVariantCount() {
        return variantCount;
    }

    public void setVariantCount(Long variantCount) {
        this.variantCount = variantCount;
    }

    public BeaconDatasetAlleleResponse callCount(Long callCount) {
        this.callCount = callCount;
        return this;
    }

    /**
     * Number of calls matching the allele request in the dataset.
     * minimum: 0
     * @return callCount
     **/
    @ApiModelProperty(value = "Number of calls matching the allele request in the dataset.")

    @Min(0L)  public Long getCallCount() {
        return callCount;
    }

    public void setCallCount(Long callCount) {
        this.callCount = callCount;
    }

    public BeaconDatasetAlleleResponse sampleCount(Long sampleCount) {
        this.sampleCount = sampleCount;
        return this;
    }

    /**
     * Number of samples matching the allele request in the dataset
     * minimum: 0
     * @return sampleCount
     **/
    @ApiModelProperty(value = "Number of samples matching the allele request in the dataset")

    @Min(0L)  public Long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(Long sampleCount) {
        this.sampleCount = sampleCount;
    }

    public BeaconDatasetAlleleResponse note(String note) {
        this.note = note;
        return this;
    }

    /**
     * Additional note or description of the response.
     * @return note
     **/
    @ApiModelProperty(value = "Additional note or description of the response.")

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BeaconDatasetAlleleResponse externalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
        return this;
    }

    /**
     * URL to an external system, such as a secured beacon or a system providing more information about a given allele (RFC 3986 format).
     * @return externalUrl
     **/
    @ApiModelProperty(value = "URL to an external system, such as a secured beacon or a system providing more information about a given allele (RFC 3986 format).")

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public BeaconDatasetAlleleResponse info(List<KeyValuePair> info) {
        this.info = info;
        return this;
    }

    public BeaconDatasetAlleleResponse addInfoItem(KeyValuePair infoItem) {
        if (this.info == null) {
            this.info = new ArrayList<KeyValuePair>();
        }
        this.info.add(infoItem);
        return this;
    }

    /**
     * Additional structured metadata, key-value pairs.
     * @return info
     **/
    @ApiModelProperty(value = "Additional structured metadata, key-value pairs.")
    @Valid
    public List<KeyValuePair> getInfo() {
        return info;
    }

    public void setInfo(List<KeyValuePair> info) {
        this.info = info;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BeaconDatasetAlleleResponse beaconDatasetAlleleResponse = (BeaconDatasetAlleleResponse) o;
        return Objects.equals(this.datasetId, beaconDatasetAlleleResponse.datasetId) &&
                Objects.equals(this.exists, beaconDatasetAlleleResponse.exists) &&
                Objects.equals(this.error, beaconDatasetAlleleResponse.error) &&
                Objects.equals(this.frequency, beaconDatasetAlleleResponse.frequency) &&
                Objects.equals(this.variantCount, beaconDatasetAlleleResponse.variantCount) &&
                Objects.equals(this.callCount, beaconDatasetAlleleResponse.callCount) &&
                Objects.equals(this.sampleCount, beaconDatasetAlleleResponse.sampleCount) &&
                Objects.equals(this.note, beaconDatasetAlleleResponse.note) &&
                Objects.equals(this.externalUrl, beaconDatasetAlleleResponse.externalUrl) &&
                Objects.equals(this.info, beaconDatasetAlleleResponse.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datasetId, exists, error, frequency, variantCount, callCount, sampleCount, note, externalUrl, info);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BeaconDatasetAlleleResponse {\n");

        sb.append("    datasetId: ").append(toIndentedString(datasetId)).append("\n");
        sb.append("    exists: ").append(toIndentedString(exists)).append("\n");
        sb.append("    error: ").append(toIndentedString(error)).append("\n");
        sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
        sb.append("    variantCount: ").append(toIndentedString(variantCount)).append("\n");
        sb.append("    callCount: ").append(toIndentedString(callCount)).append("\n");
        sb.append("    sampleCount: ").append(toIndentedString(sampleCount)).append("\n");
        sb.append("    note: ").append(toIndentedString(note)).append("\n");
        sb.append("    externalUrl: ").append(toIndentedString(externalUrl)).append("\n");
        sb.append("    info: ").append(toIndentedString(info)).append("\n");
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
