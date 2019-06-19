package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ClinicalDescription
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class ClinicalDescription   {
  @JsonProperty("type")
  private ClinicalProfile type = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("restriction")
  private RestrictionsULF restriction = null;

  public ClinicalDescription type(ClinicalProfile type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(value = "")

  @Valid
  public ClinicalProfile getType() {
    return type;
  }

  public void setType(ClinicalProfile type) {
    this.type = type;
  }

  public ClinicalDescription description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(value = "")

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ClinicalDescription restriction(RestrictionsULF restriction) {
    this.restriction = restriction;
    return this;
  }

  /**
   * Get restriction
   * @return restriction
  **/
  @ApiModelProperty(value = "")

  @Valid
  public RestrictionsULF getRestriction() {
    return restriction;
  }

  public void setRestriction(RestrictionsULF restriction) {
    this.restriction = restriction;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClinicalDescription clinicalDescription = (ClinicalDescription) o;
    return Objects.equals(this.type, clinicalDescription.type) &&
        Objects.equals(this.description, clinicalDescription.description) &&
        Objects.equals(this.restriction, clinicalDescription.restriction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description, restriction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClinicalDescription {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    restriction: ").append(toIndentedString(restriction)).append("\n");
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
