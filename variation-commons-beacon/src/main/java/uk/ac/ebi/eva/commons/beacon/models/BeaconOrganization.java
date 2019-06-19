package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Organization owning the beacon.
 */
@ApiModel(description = "Organization owning the beacon.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class BeaconOrganization   {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("address")
    private String address = null;

    @JsonProperty("welcomeUrl")
    private String welcomeUrl = null;

    @JsonProperty("contactUrl")
    private String contactUrl = null;

    @JsonProperty("logoUrl")
    private String logoUrl = null;

    @JsonProperty("info")
    @Valid
    private List<KeyValuePair> info = null;

    public BeaconOrganization id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Unique identifier of the organization.
     * @return id
     **/
    @ApiModelProperty(required = true, value = "Unique identifier of the organization.")
    @NotNull

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BeaconOrganization name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Name of the organization.
     * @return name
     **/
    @ApiModelProperty(required = true, value = "Name of the organization.")
    @NotNull

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BeaconOrganization description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description of the organization.
     * @return description
     **/
    @ApiModelProperty(value = "Description of the organization.")

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BeaconOrganization address(String address) {
        this.address = address;
        return this;
    }

    /**
     * Address of the organization.
     * @return address
     **/
    @ApiModelProperty(value = "Address of the organization.")

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BeaconOrganization welcomeUrl(String welcomeUrl) {
        this.welcomeUrl = welcomeUrl;
        return this;
    }

    /**
     * URL of the website of the organization (RFC 3986 format).
     * @return welcomeUrl
     **/
    @ApiModelProperty(value = "URL of the website of the organization (RFC 3986 format).")

    public String getWelcomeUrl() {
        return welcomeUrl;
    }

    public void setWelcomeUrl(String welcomeUrl) {
        this.welcomeUrl = welcomeUrl;
    }

    public BeaconOrganization contactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
        return this;
    }

    /**
     * URL with the contact for the beacon operator/maintainer, e.g. link to a contact form (RFC 3986 format) or an email (RFC 2368 format).
     * @return contactUrl
     **/
    @ApiModelProperty(value = "URL with the contact for the beacon operator/maintainer, e.g. link to a contact form (RFC 3986 format) or an email (RFC 2368 format).")

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public BeaconOrganization logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    /**
     * URL to the logo (PNG/JPG format) of the organization (RFC 3986 format).
     * @return logoUrl
     **/
    @ApiModelProperty(value = "URL to the logo (PNG/JPG format) of the organization (RFC 3986 format).")

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BeaconOrganization info(List<KeyValuePair> info) {
        this.info = info;
        return this;
    }

    public BeaconOrganization addInfoItem(KeyValuePair infoItem) {
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
        BeaconOrganization beaconOrganization = (BeaconOrganization) o;
        return Objects.equals(this.id, beaconOrganization.id) &&
                Objects.equals(this.name, beaconOrganization.name) &&
                Objects.equals(this.description, beaconOrganization.description) &&
                Objects.equals(this.address, beaconOrganization.address) &&
                Objects.equals(this.welcomeUrl, beaconOrganization.welcomeUrl) &&
                Objects.equals(this.contactUrl, beaconOrganization.contactUrl) &&
                Objects.equals(this.logoUrl, beaconOrganization.logoUrl) &&
                Objects.equals(this.info, beaconOrganization.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, address, welcomeUrl, contactUrl, logoUrl, info);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BeaconOrganization {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    name: ").append(toIndentedString(name)).append("\n");
        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
        sb.append("    welcomeUrl: ").append(toIndentedString(welcomeUrl)).append("\n");
        sb.append("    contactUrl: ").append(toIndentedString(contactUrl)).append("\n");
        sb.append("    logoUrl: ").append(toIndentedString(logoUrl)).append("\n");
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
