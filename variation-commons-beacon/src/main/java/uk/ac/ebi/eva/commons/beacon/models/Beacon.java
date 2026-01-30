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

import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

/**
 * Beacon
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class Beacon   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("apiVersion")
    private String apiVersion = null;

    @JsonProperty("organization")
    private BeaconOrganization organization = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("version")
    private String version = null;

    @JsonProperty("welcomeUrl")
    private String welcomeUrl = null;

    @JsonProperty("alternativeUrl")
    private String alternativeUrl = null;

    @JsonProperty("createDateTime")
    private String createDateTime = null;

    @JsonProperty("updateDateTime")
    private String updateDateTime = null;

    @JsonProperty("datasets")
    @Valid
    private List<BeaconDataset> datasets = new ArrayList<BeaconDataset>();

    @JsonProperty("sampleAlleleRequests")
    @Valid
    private List<BeaconAlleleRequest> sampleAlleleRequests = null;

    @JsonProperty("info")
    @Valid
    private List<KeyValuePair> info = null;

    public Beacon id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique identifier of the beacon. Use reverse domain name notation.
     * @return id
     **/
    @ApiModelProperty(example = "org.ga4gh.beacon", required = true, value = "Unique identifier of the beacon. Use reverse domain name notation.")
    @NotNull

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Beacon name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the beacon.
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Name of the beacon.")
    @NotNull

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Beacon apiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    /**
     * Version of the API provided by the beacon.
     * @return apiVersion
     **/
    @ApiModelProperty(example = "v0.3", required = true, value = "Version of the API provided by the beacon.")
    @NotNull

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Beacon organization(BeaconOrganization organization) {
        this.organization = organization;
        return this;
    }

    /**
     * Get organization
     * @return organization
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid
    public BeaconOrganization getOrganization() {
        return organization;
    }

    public void setOrganization(BeaconOrganization organization) {
        this.organization = organization;
    }

    public Beacon description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description of the beacon.
     * @return description
     **/
    @ApiModelProperty(value = "Description of the beacon.")

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Beacon version(String version) {
        this.version = version;
        return this;
    }

    /**
     * Version of the beacon.
     * @return version
     **/
    @ApiModelProperty(example = "v0.1", value = "Version of the beacon.")

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Beacon welcomeUrl(String welcomeUrl) {
        this.welcomeUrl = welcomeUrl;
        return this;
    }

    /**
     * URL to the welcome page for this beacon (RFC 3986 format).
     * @return welcomeUrl
     **/
    @ApiModelProperty(example = "http://example.org/wiki/Main_Page", value = "URL to the welcome page for this beacon (RFC 3986 format).")

    public String getWelcomeUrl() {
        return welcomeUrl;
    }

    public void setWelcomeUrl(String welcomeUrl) {
        this.welcomeUrl = welcomeUrl;
    }

    public Beacon alternativeUrl(String alternativeUrl) {
        this.alternativeUrl = alternativeUrl;
        return this;
    }

    /**
     * Alternative URL to the API, e.g. a restricted version of this beacon (RFC 3986 format).
     * @return alternativeUrl
     **/
    @ApiModelProperty(example = "http://example.org/wiki/Main_Page", value = "Alternative URL to the API, e.g. a restricted version of this beacon (RFC 3986 format).")

    public String getAlternativeUrl() {
        return alternativeUrl;
    }

    public void setAlternativeUrl(String alternativeUrl) {
        this.alternativeUrl = alternativeUrl;
    }

    public Beacon createDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
        return this;
    }

    /**
     * The time the beacon was created (ISO 8601 format).
     * @return createDateTime
     **/
    @ApiModelProperty(example = "2012-07-19 or 2017-01-17T20:33:40Z", value = "The time the beacon was created (ISO 8601 format).")

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Beacon updateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
        return this;
    }

    /**
     * The time the beacon was updated in (ISO 8601 format).
     * @return updateDateTime
     **/
    @ApiModelProperty(example = "2012-07-19 or 2017-01-17T20:33:40Z", value = "The time the beacon was updated in (ISO 8601 format).")

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public Beacon datasets(List<BeaconDataset> datasets) {
        this.datasets = datasets;
        return this;
    }

    public Beacon addDatasetsItem(BeaconDataset datasetsItem) {
        this.datasets.add(datasetsItem);
        return this;
    }

    /**
     * Dataset(s) served by the beacon.
     * @return datasets
     **/
    @ApiModelProperty(required = true, value = "Dataset(s) served by the beacon.")
    @NotNull
    @Valid
    @Size(min=1)   public List<BeaconDataset> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<BeaconDataset> datasets) {
        this.datasets = datasets;
    }

    public Beacon sampleAlleleRequests(List<BeaconAlleleRequest> sampleAlleleRequests) {
        this.sampleAlleleRequests = sampleAlleleRequests;
        return this;
    }

    public Beacon addSampleAlleleRequestsItem(BeaconAlleleRequest sampleAlleleRequestsItem) {
        if (this.sampleAlleleRequests == null) {
            this.sampleAlleleRequests = new ArrayList<BeaconAlleleRequest>();
        }
        this.sampleAlleleRequests.add(sampleAlleleRequestsItem);
        return this;
    }

    /**
     * Examples of interesting queries, e.g. a few queries demonstrating different responses.
     * @return sampleAlleleRequests
     **/
    @ApiModelProperty(value = "Examples of interesting queries, e.g. a few queries demonstrating different responses.")
    @Valid
    public List<BeaconAlleleRequest> getSampleAlleleRequests() {
        return sampleAlleleRequests;
    }

    public void setSampleAlleleRequests(List<BeaconAlleleRequest> sampleAlleleRequests) {
        this.sampleAlleleRequests = sampleAlleleRequests;
    }

    public Beacon info(List<KeyValuePair> info) {
        this.info = info;
        return this;
    }

    public Beacon addInfoItem(KeyValuePair infoItem) {
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
        Beacon beacon = (Beacon) o;
        return Objects.equals(this.id, beacon.id) &&
                Objects.equals(this.name, beacon.name) &&
                Objects.equals(this.apiVersion, beacon.apiVersion) &&
                Objects.equals(this.organization, beacon.organization) &&
                Objects.equals(this.description, beacon.description) &&
                Objects.equals(this.version, beacon.version) &&
                Objects.equals(this.welcomeUrl, beacon.welcomeUrl) &&
                Objects.equals(this.alternativeUrl, beacon.alternativeUrl) &&
                Objects.equals(this.createDateTime, beacon.createDateTime) &&
                Objects.equals(this.updateDateTime, beacon.updateDateTime) &&
                Objects.equals(this.datasets, beacon.datasets) &&
                Objects.equals(this.sampleAlleleRequests, beacon.sampleAlleleRequests) &&
                Objects.equals(this.info, beacon.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, apiVersion, organization, description, version, welcomeUrl, alternativeUrl, createDateTime, updateDateTime, datasets, sampleAlleleRequests, info);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Beacon {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    apiVersion: ").append(toIndentedString(apiVersion)).append("\n");
        sb.append("    organization: ").append(toIndentedString(organization)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    version: ").append(toIndentedString(version)).append("\n");
        sb.append("    welcomeUrl: ").append(toIndentedString(welcomeUrl)).append("\n");
        sb.append("    alternativeUrl: ").append(toIndentedString(alternativeUrl)).append("\n");
        sb.append("    createDateTime: ").append(toIndentedString(createDateTime)).append("\n");
        sb.append("    updateDateTime: ").append(toIndentedString(updateDateTime)).append("\n");
        sb.append("    datasets: ").append(toIndentedString(datasets)).append("\n");
        sb.append("    sampleAlleleRequests: ").append(toIndentedString(sampleAlleleRequests)).append("\n");
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
