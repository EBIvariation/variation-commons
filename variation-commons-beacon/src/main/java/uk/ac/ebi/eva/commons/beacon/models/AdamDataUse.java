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
 * AdamDataUse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class AdamDataUse   {
  @JsonProperty("header")
  private Header header = null;

  @JsonProperty("profile")
  private Profile profile = null;

  @JsonProperty("terms")
  private Terms terms = null;

  @JsonProperty("metaConditions")
  private MetaConditions metaConditions = null;

  public AdamDataUse header(Header header) {
    this.header = header;
    return this;
  }

  /**
   * Get header
   * @return header
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public Header getHeader() {
    return header;
  }

  public void setHeader(Header header) {
    this.header = header;
  }

  public AdamDataUse profile(Profile profile) {
    this.profile = profile;
    return this;
  }

  /**
   * Get profile
   * @return profile
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public Profile getProfile() {
    return profile;
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
  }

  public AdamDataUse terms(Terms terms) {
    this.terms = terms;
    return this;
  }

  /**
   * Get terms
   * @return terms
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public Terms getTerms() {
    return terms;
  }

  public void setTerms(Terms terms) {
    this.terms = terms;
  }

  public AdamDataUse metaConditions(MetaConditions metaConditions) {
    this.metaConditions = metaConditions;
    return this;
  }

  /**
   * Get metaConditions
   * @return metaConditions
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public MetaConditions getMetaConditions() {
    return metaConditions;
  }

  public void setMetaConditions(MetaConditions metaConditions) {
    this.metaConditions = metaConditions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdamDataUse adamDataUse = (AdamDataUse) o;
    return Objects.equals(this.header, adamDataUse.header) &&
        Objects.equals(this.profile, adamDataUse.profile) &&
        Objects.equals(this.terms, adamDataUse.terms) &&
        Objects.equals(this.metaConditions, adamDataUse.metaConditions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(header, profile, terms, metaConditions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdamDataUse {\n");
    
    sb.append("    header: ").append(toIndentedString(header)).append("\n");
    sb.append("    profile: ").append(toIndentedString(profile)).append("\n");
    sb.append("    terms: ").append(toIndentedString(terms)).append("\n");
    sb.append("    metaConditions: ").append(toIndentedString(metaConditions)).append("\n");
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
