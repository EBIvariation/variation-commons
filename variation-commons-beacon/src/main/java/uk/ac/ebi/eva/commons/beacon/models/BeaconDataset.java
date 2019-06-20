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
 * BeaconDataset
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class BeaconDataset   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("assemblyId")
    private String assemblyId = null;

    @JsonProperty("createDateTime")
    private String createDateTime = null;

    @JsonProperty("updateDateTime")
    private String updateDateTime = null;

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("variantCount")
    private Long variantCount = null;

    @JsonProperty("callCount")
    private Long callCount = null;

    @JsonProperty("sampleCount")
    private Long sampleCount = null;

    @JsonProperty("externalUrl")
    private String externalUrl = null;

    @JsonProperty("info")
    @Valid
    private List<KeyValuePair> info = null;

    @JsonProperty("dataUseConditions")
    private DataUseConditions dataUseConditions = null;

    public BeaconDataset id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique identifier of the dataset.
     * @return id
     **/
    @ApiModelProperty(required = true, value = "Unique identifier of the dataset.")
    @NotNull

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BeaconDataset name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the dataset.
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Name of the dataset.")
    @NotNull

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BeaconDataset description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description of the dataset.
     * @return description
     **/
    @ApiModelProperty(value = "Description of the dataset.")

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BeaconDataset assemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
        return this;
    }

    /**
     * Assembly identifier (GRC notation, e.g. `GRCh37`).
     * @return assemblyId
     **/
    @ApiModelProperty(example = "GRCh38", required = true, value = "Assembly identifier (GRC notation, e.g. `GRCh37`).")
    @NotNull

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    public BeaconDataset createDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
        return this;
    }

    /**
     * The time the dataset was created (ISO 8601 format).
     * @return createDateTime
     **/
    @ApiModelProperty(example = "2012-07-29 or 2017-01-17T20:33:40Z", required = true, value = "The time the dataset was created (ISO 8601 format).")
    @NotNull

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public BeaconDataset updateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
        return this;
    }

    /**
     * The time the dataset was updated in (ISO 8601 format).
     * @return updateDateTime
     **/
    @ApiModelProperty(example = "2012-07-19 or 2017-01-17T20:33:40Z", required = true, value = "The time the dataset was updated in (ISO 8601 format).")
    @NotNull

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public BeaconDataset version(String version) {
        this.version = version;
        return this;
    }

    /**
     * Version of the dataset.
     * @return version
     **/
    @ApiModelProperty(value = "Version of the dataset.")

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BeaconDataset variantCount(Long variantCount) {
        this.variantCount = variantCount;
        return this;
    }

    /**
     * Total number of variants in the dataset.
     * minimum: 0
     * @return variantCount
     **/
    @ApiModelProperty(value = "Total number of variants in the dataset.")

    @Min(0L)  public Long getVariantCount() {
        return variantCount;
    }

    public void setVariantCount(Long variantCount) {
        this.variantCount = variantCount;
    }

    public BeaconDataset callCount(Long callCount) {
        this.callCount = callCount;
        return this;
    }

    /**
     * Total number of calls in the dataset.
     * minimum: 0
     * @return callCount
     **/
    @ApiModelProperty(value = "Total number of calls in the dataset.")

    @Min(0L)  public Long getCallCount() {
        return callCount;
    }

    public void setCallCount(Long callCount) {
        this.callCount = callCount;
    }

    public BeaconDataset sampleCount(Long sampleCount) {
        this.sampleCount = sampleCount;
        return this;
    }

    /**
     * Total number of samples in the dataset.
     * minimum: 0
     * @return sampleCount
     **/
    @ApiModelProperty(value = "Total number of samples in the dataset.")

    @Min(0L)  public Long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(Long sampleCount) {
        this.sampleCount = sampleCount;
    }

    public BeaconDataset externalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
        return this;
    }

    /**
     * URL to an external system providing more dataset information (RFC 3986 format).
     * @return externalUrl
     **/
    @ApiModelProperty(example = "http://example.org/wiki/Main_Page", value = "URL to an external system providing more dataset information (RFC 3986 format).")

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public BeaconDataset info(List<KeyValuePair> info) {
        this.info = info;
        return this;
    }

    public BeaconDataset addInfoItem(KeyValuePair infoItem) {
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

    public BeaconDataset dataUseConditions(DataUseConditions dataUseConditions) {
        this.dataUseConditions = dataUseConditions;
        return this;
    }

    /**
     * Get dataUseConditions
     * @return dataUseConditions
     **/
    @ApiModelProperty(value = "")

    @Valid
    public DataUseConditions getDataUseConditions() {
        return dataUseConditions;
    }

    public void setDataUseConditions(DataUseConditions dataUseConditions) {
        this.dataUseConditions = dataUseConditions;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BeaconDataset beaconDataset = (BeaconDataset) o;
        return Objects.equals(this.id, beaconDataset.id) &&
                Objects.equals(this.name, beaconDataset.name) &&
                Objects.equals(this.description, beaconDataset.description) &&
                Objects.equals(this.assemblyId, beaconDataset.assemblyId) &&
                Objects.equals(this.createDateTime, beaconDataset.createDateTime) &&
                Objects.equals(this.updateDateTime, beaconDataset.updateDateTime) &&
                Objects.equals(this.version, beaconDataset.version) &&
                Objects.equals(this.variantCount, beaconDataset.variantCount) &&
                Objects.equals(this.callCount, beaconDataset.callCount) &&
                Objects.equals(this.sampleCount, beaconDataset.sampleCount) &&
                Objects.equals(this.externalUrl, beaconDataset.externalUrl) &&
                Objects.equals(this.info, beaconDataset.info) &&
                Objects.equals(this.dataUseConditions, beaconDataset.dataUseConditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, assemblyId, createDateTime, updateDateTime, version, variantCount, callCount, sampleCount, externalUrl, info, dataUseConditions);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BeaconDataset {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    assemblyId: ").append(toIndentedString(assemblyId)).append("\n");
        sb.append("    createDateTime: ").append(toIndentedString(createDateTime)).append("\n");
        sb.append("    updateDateTime: ").append(toIndentedString(updateDateTime)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    variantCount: ").append(toIndentedString(variantCount)).append("\n");
        sb.append("    callCount: ").append(toIndentedString(callCount)).append("\n");
        sb.append("    sampleCount: ").append(toIndentedString(sampleCount)).append("\n");
        sb.append("    externalUrl: ").append(toIndentedString(externalUrl)).append("\n");
        sb.append("    info: ").append(toIndentedString(info)).append("\n");
        sb.append("    dataUseConditions: ").append(toIndentedString(dataUseConditions)).append("\n");
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
