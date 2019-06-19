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
 * Header
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class Header   {
  @JsonProperty("matrixName")
  private String matrixName = null;

  @JsonProperty("matrixVersion")
  private String matrixVersion = null;

  @JsonProperty("matrixReferences")
  @Valid
  private List<String> matrixReferences = null;

  @JsonProperty("matrixProfileCreateDate")
  private String matrixProfileCreateDate = null;

  @JsonProperty("matrixProfileUpdates")
  @Valid
  private List<ProfileUpdates> matrixProfileUpdates = null;

  @JsonProperty("resourceName")
  private String resourceName = null;

  @JsonProperty("resourceReferences")
  @Valid
  private List<String> resourceReferences = null;

  @JsonProperty("resourceDescription")
  private String resourceDescription = null;

  @JsonProperty("resourceDataLevel")
  private DataLevel resourceDataLevel = null;

  @JsonProperty("resourceContactNames")
  @Valid
  private List<Contact> resourceContactNames = null;

  @JsonProperty("resourceContactOrganisations")
  @Valid
  private List<String> resourceContactOrganisations = null;

  public Header matrixName(String matrixName) {
    this.matrixName = matrixName;
    return this;
  }

  /**
   * Get matrixName
   * @return matrixName
  **/
  @ApiModelProperty(value = "")

  public String getMatrixName() {
    return matrixName;
  }

  public void setMatrixName(String matrixName) {
    this.matrixName = matrixName;
  }

  public Header matrixVersion(String matrixVersion) {
    this.matrixVersion = matrixVersion;
    return this;
  }

  /**
   * Get matrixVersion
   * @return matrixVersion
  **/
  @ApiModelProperty(value = "")

  public String getMatrixVersion() {
    return matrixVersion;
  }

  public void setMatrixVersion(String matrixVersion) {
    this.matrixVersion = matrixVersion;
  }

  public Header matrixReferences(List<String> matrixReferences) {
    this.matrixReferences = matrixReferences;
    return this;
  }

  public Header addMatrixReferencesItem(String matrixReferencesItem) {
    if (this.matrixReferences == null) {
      this.matrixReferences = new ArrayList<String>();
    }
    this.matrixReferences.add(matrixReferencesItem);
    return this;
  }

  /**
   * Publications, URLs, DOIs for the resource.
   * @return matrixReferences
  **/
  @ApiModelProperty(value = "Publications, URLs, DOIs for the resource.")

  public List<String> getMatrixReferences() {
    return matrixReferences;
  }

  public void setMatrixReferences(List<String> matrixReferences) {
    this.matrixReferences = matrixReferences;
  }

  public Header matrixProfileCreateDate(String matrixProfileCreateDate) {
    this.matrixProfileCreateDate = matrixProfileCreateDate;
    return this;
  }

  /**
   * Get matrixProfileCreateDate
   * @return matrixProfileCreateDate
  **/
  @ApiModelProperty(example = "2017-01-17T20:33:40Z", value = "")

  public String getMatrixProfileCreateDate() {
    return matrixProfileCreateDate;
  }

  public void setMatrixProfileCreateDate(String matrixProfileCreateDate) {
    this.matrixProfileCreateDate = matrixProfileCreateDate;
  }

  public Header matrixProfileUpdates(List<ProfileUpdates> matrixProfileUpdates) {
    this.matrixProfileUpdates = matrixProfileUpdates;
    return this;
  }

  public Header addMatrixProfileUpdatesItem(ProfileUpdates matrixProfileUpdatesItem) {
    if (this.matrixProfileUpdates == null) {
      this.matrixProfileUpdates = new ArrayList<ProfileUpdates>();
    }
    this.matrixProfileUpdates.add(matrixProfileUpdatesItem);
    return this;
  }

  /**
   * Get matrixProfileUpdates
   * @return matrixProfileUpdates
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ProfileUpdates> getMatrixProfileUpdates() {
    return matrixProfileUpdates;
  }

  public void setMatrixProfileUpdates(List<ProfileUpdates> matrixProfileUpdates) {
    this.matrixProfileUpdates = matrixProfileUpdates;
  }

  public Header resourceName(String resourceName) {
    this.resourceName = resourceName;
    return this;
  }

  /**
   * Get resourceName
   * @return resourceName
  **/
  @ApiModelProperty(value = "")

  public String getResourceName() {
    return resourceName;
  }

  public void setResourceName(String resourceName) {
    this.resourceName = resourceName;
  }

  public Header resourceReferences(List<String> resourceReferences) {
    this.resourceReferences = resourceReferences;
    return this;
  }

  public Header addResourceReferencesItem(String resourceReferencesItem) {
    if (this.resourceReferences == null) {
      this.resourceReferences = new ArrayList<String>();
    }
    this.resourceReferences.add(resourceReferencesItem);
    return this;
  }

  /**
   * Publications, URLs, DOIs for the resource.
   * @return resourceReferences
  **/
  @ApiModelProperty(value = "Publications, URLs, DOIs for the resource.")

  public List<String> getResourceReferences() {
    return resourceReferences;
  }

  public void setResourceReferences(List<String> resourceReferences) {
    this.resourceReferences = resourceReferences;
  }

  public Header resourceDescription(String resourceDescription) {
    this.resourceDescription = resourceDescription;
    return this;
  }

  /**
   * Get resourceDescription
   * @return resourceDescription
  **/
  @ApiModelProperty(value = "")

  public String getResourceDescription() {
    return resourceDescription;
  }

  public void setResourceDescription(String resourceDescription) {
    this.resourceDescription = resourceDescription;
  }

  public Header resourceDataLevel(DataLevel resourceDataLevel) {
    this.resourceDataLevel = resourceDataLevel;
    return this;
  }

  /**
   * Get resourceDataLevel
   * @return resourceDataLevel
  **/
  @ApiModelProperty(value = "")

  @Valid
  public DataLevel getResourceDataLevel() {
    return resourceDataLevel;
  }

  public void setResourceDataLevel(DataLevel resourceDataLevel) {
    this.resourceDataLevel = resourceDataLevel;
  }

  public Header resourceContactNames(List<Contact> resourceContactNames) {
    this.resourceContactNames = resourceContactNames;
    return this;
  }

  public Header addResourceContactNamesItem(Contact resourceContactNamesItem) {
    if (this.resourceContactNames == null) {
      this.resourceContactNames = new ArrayList<Contact>();
    }
    this.resourceContactNames.add(resourceContactNamesItem);
    return this;
  }

  /**
   * Get resourceContactNames
   * @return resourceContactNames
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<Contact> getResourceContactNames() {
    return resourceContactNames;
  }

  public void setResourceContactNames(List<Contact> resourceContactNames) {
    this.resourceContactNames = resourceContactNames;
  }

  public Header resourceContactOrganisations(List<String> resourceContactOrganisations) {
    this.resourceContactOrganisations = resourceContactOrganisations;
    return this;
  }

  public Header addResourceContactOrganisationsItem(String resourceContactOrganisationsItem) {
    if (this.resourceContactOrganisations == null) {
      this.resourceContactOrganisations = new ArrayList<String>();
    }
    this.resourceContactOrganisations.add(resourceContactOrganisationsItem);
    return this;
  }

  /**
   * Get resourceContactOrganisations
   * @return resourceContactOrganisations
  **/
  @ApiModelProperty(value = "")

  public List<String> getResourceContactOrganisations() {
    return resourceContactOrganisations;
  }

  public void setResourceContactOrganisations(List<String> resourceContactOrganisations) {
    this.resourceContactOrganisations = resourceContactOrganisations;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Header header = (Header) o;
    return Objects.equals(this.matrixName, header.matrixName) &&
        Objects.equals(this.matrixVersion, header.matrixVersion) &&
        Objects.equals(this.matrixReferences, header.matrixReferences) &&
        Objects.equals(this.matrixProfileCreateDate, header.matrixProfileCreateDate) &&
        Objects.equals(this.matrixProfileUpdates, header.matrixProfileUpdates) &&
        Objects.equals(this.resourceName, header.resourceName) &&
        Objects.equals(this.resourceReferences, header.resourceReferences) &&
        Objects.equals(this.resourceDescription, header.resourceDescription) &&
        Objects.equals(this.resourceDataLevel, header.resourceDataLevel) &&
        Objects.equals(this.resourceContactNames, header.resourceContactNames) &&
        Objects.equals(this.resourceContactOrganisations, header.resourceContactOrganisations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(matrixName, matrixVersion, matrixReferences, matrixProfileCreateDate, matrixProfileUpdates, resourceName, resourceReferences, resourceDescription, resourceDataLevel, resourceContactNames, resourceContactOrganisations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Header {\n");
    
    sb.append("    matrixName: ").append(toIndentedString(matrixName)).append("\n");
    sb.append("    matrixVersion: ").append(toIndentedString(matrixVersion)).append("\n");
    sb.append("    matrixReferences: ").append(toIndentedString(matrixReferences)).append("\n");
    sb.append("    matrixProfileCreateDate: ").append(toIndentedString(matrixProfileCreateDate)).append("\n");
    sb.append("    matrixProfileUpdates: ").append(toIndentedString(matrixProfileUpdates)).append("\n");
    sb.append("    resourceName: ").append(toIndentedString(resourceName)).append("\n");
    sb.append("    resourceReferences: ").append(toIndentedString(resourceReferences)).append("\n");
    sb.append("    resourceDescription: ").append(toIndentedString(resourceDescription)).append("\n");
    sb.append("    resourceDataLevel: ").append(toIndentedString(resourceDataLevel)).append("\n");
    sb.append("    resourceContactNames: ").append(toIndentedString(resourceContactNames)).append("\n");
    sb.append("    resourceContactOrganisations: ").append(toIndentedString(resourceContactOrganisations)).append("\n");
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
