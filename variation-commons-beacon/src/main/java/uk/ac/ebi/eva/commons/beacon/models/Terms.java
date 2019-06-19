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
 * Terms
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class Terms   {
  @JsonProperty("noAuthorizationTerms")
  private Boolean noAuthorizationTerms = null;

  @JsonProperty("whichAuthorizationTerms")
  @Valid
  private List<String> whichAuthorizationTerms = null;

  @JsonProperty("noPublicationTerms")
  private Boolean noPublicationTerms = null;

  @JsonProperty("whichPublicationTerms")
  @Valid
  private List<String> whichPublicationTerms = null;

  @JsonProperty("noTimelineTerms")
  private Boolean noTimelineTerms = null;

  @JsonProperty("whichTimelineTerms")
  @Valid
  private List<String> whichTimelineTerms = null;

  @JsonProperty("noSecurityTerms")
  private Boolean noSecurityTerms = null;

  @JsonProperty("whichSecurityTerms")
  @Valid
  private List<String> whichSecurityTerms = null;

  @JsonProperty("noExpungingTerms")
  private Boolean noExpungingTerms = null;

  @JsonProperty("whichExpungingTerms")
  @Valid
  private List<String> whichExpungingTerms = null;

  @JsonProperty("noLinkingTerms")
  private Boolean noLinkingTerms = null;

  @JsonProperty("whichLinkingTerms")
  @Valid
  private List<String> whichLinkingTerms = null;

  @JsonProperty("noRecontactTerms")
  private Boolean noRecontactTerms = null;

  @JsonProperty("allowedRecontactTerms")
  @Valid
  private List<String> allowedRecontactTerms = null;

  @JsonProperty("compulsoryRecontactTerms")
  @Valid
  private List<String> compulsoryRecontactTerms = null;

  @JsonProperty("noIPClaimTerms")
  private Boolean noIPClaimTerms = null;

  @JsonProperty("whichIPClaimTerms")
  @Valid
  private List<String> whichIPClaimTerms = null;

  @JsonProperty("noReportingTerms")
  private Boolean noReportingTerms = null;

  @JsonProperty("whichReportingTerms")
  @Valid
  private List<String> whichReportingTerms = null;

  @JsonProperty("noCollaborationTerms")
  private Boolean noCollaborationTerms = null;

  @JsonProperty("whichCollaborationTerms")
  @Valid
  private List<String> whichCollaborationTerms = null;

  @JsonProperty("noPaymentTerms")
  private Boolean noPaymentTerms = null;

  @JsonProperty("whichPaymentTerms")
  @Valid
  private List<String> whichPaymentTerms = null;

  public Terms noAuthorizationTerms(Boolean noAuthorizationTerms) {
    this.noAuthorizationTerms = noAuthorizationTerms;
    return this;
  }

  /**
   * Get noAuthorizationTerms
   * @return noAuthorizationTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoAuthorizationTerms() {
    return noAuthorizationTerms;
  }

  public void setNoAuthorizationTerms(Boolean noAuthorizationTerms) {
    this.noAuthorizationTerms = noAuthorizationTerms;
  }

  public Terms whichAuthorizationTerms(List<String> whichAuthorizationTerms) {
    this.whichAuthorizationTerms = whichAuthorizationTerms;
    return this;
  }

  public Terms addWhichAuthorizationTermsItem(String whichAuthorizationTermsItem) {
    if (this.whichAuthorizationTerms == null) {
      this.whichAuthorizationTerms = new ArrayList<String>();
    }
    this.whichAuthorizationTerms.add(whichAuthorizationTermsItem);
    return this;
  }

  /**
   * Get whichAuthorizationTerms
   * @return whichAuthorizationTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichAuthorizationTerms() {
    return whichAuthorizationTerms;
  }

  public void setWhichAuthorizationTerms(List<String> whichAuthorizationTerms) {
    this.whichAuthorizationTerms = whichAuthorizationTerms;
  }

  public Terms noPublicationTerms(Boolean noPublicationTerms) {
    this.noPublicationTerms = noPublicationTerms;
    return this;
  }

  /**
   * Get noPublicationTerms
   * @return noPublicationTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoPublicationTerms() {
    return noPublicationTerms;
  }

  public void setNoPublicationTerms(Boolean noPublicationTerms) {
    this.noPublicationTerms = noPublicationTerms;
  }

  public Terms whichPublicationTerms(List<String> whichPublicationTerms) {
    this.whichPublicationTerms = whichPublicationTerms;
    return this;
  }

  public Terms addWhichPublicationTermsItem(String whichPublicationTermsItem) {
    if (this.whichPublicationTerms == null) {
      this.whichPublicationTerms = new ArrayList<String>();
    }
    this.whichPublicationTerms.add(whichPublicationTermsItem);
    return this;
  }

  /**
   * Get whichPublicationTerms
   * @return whichPublicationTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichPublicationTerms() {
    return whichPublicationTerms;
  }

  public void setWhichPublicationTerms(List<String> whichPublicationTerms) {
    this.whichPublicationTerms = whichPublicationTerms;
  }

  public Terms noTimelineTerms(Boolean noTimelineTerms) {
    this.noTimelineTerms = noTimelineTerms;
    return this;
  }

  /**
   * Get noTimelineTerms
   * @return noTimelineTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoTimelineTerms() {
    return noTimelineTerms;
  }

  public void setNoTimelineTerms(Boolean noTimelineTerms) {
    this.noTimelineTerms = noTimelineTerms;
  }

  public Terms whichTimelineTerms(List<String> whichTimelineTerms) {
    this.whichTimelineTerms = whichTimelineTerms;
    return this;
  }

  public Terms addWhichTimelineTermsItem(String whichTimelineTermsItem) {
    if (this.whichTimelineTerms == null) {
      this.whichTimelineTerms = new ArrayList<String>();
    }
    this.whichTimelineTerms.add(whichTimelineTermsItem);
    return this;
  }

  /**
   * Get whichTimelineTerms
   * @return whichTimelineTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichTimelineTerms() {
    return whichTimelineTerms;
  }

  public void setWhichTimelineTerms(List<String> whichTimelineTerms) {
    this.whichTimelineTerms = whichTimelineTerms;
  }

  public Terms noSecurityTerms(Boolean noSecurityTerms) {
    this.noSecurityTerms = noSecurityTerms;
    return this;
  }

  /**
   * Get noSecurityTerms
   * @return noSecurityTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoSecurityTerms() {
    return noSecurityTerms;
  }

  public void setNoSecurityTerms(Boolean noSecurityTerms) {
    this.noSecurityTerms = noSecurityTerms;
  }

  public Terms whichSecurityTerms(List<String> whichSecurityTerms) {
    this.whichSecurityTerms = whichSecurityTerms;
    return this;
  }

  public Terms addWhichSecurityTermsItem(String whichSecurityTermsItem) {
    if (this.whichSecurityTerms == null) {
      this.whichSecurityTerms = new ArrayList<String>();
    }
    this.whichSecurityTerms.add(whichSecurityTermsItem);
    return this;
  }

  /**
   * Get whichSecurityTerms
   * @return whichSecurityTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichSecurityTerms() {
    return whichSecurityTerms;
  }

  public void setWhichSecurityTerms(List<String> whichSecurityTerms) {
    this.whichSecurityTerms = whichSecurityTerms;
  }

  public Terms noExpungingTerms(Boolean noExpungingTerms) {
    this.noExpungingTerms = noExpungingTerms;
    return this;
  }

  /**
   * Get noExpungingTerms
   * @return noExpungingTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoExpungingTerms() {
    return noExpungingTerms;
  }

  public void setNoExpungingTerms(Boolean noExpungingTerms) {
    this.noExpungingTerms = noExpungingTerms;
  }

  public Terms whichExpungingTerms(List<String> whichExpungingTerms) {
    this.whichExpungingTerms = whichExpungingTerms;
    return this;
  }

  public Terms addWhichExpungingTermsItem(String whichExpungingTermsItem) {
    if (this.whichExpungingTerms == null) {
      this.whichExpungingTerms = new ArrayList<String>();
    }
    this.whichExpungingTerms.add(whichExpungingTermsItem);
    return this;
  }

  /**
   * Get whichExpungingTerms
   * @return whichExpungingTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichExpungingTerms() {
    return whichExpungingTerms;
  }

  public void setWhichExpungingTerms(List<String> whichExpungingTerms) {
    this.whichExpungingTerms = whichExpungingTerms;
  }

  public Terms noLinkingTerms(Boolean noLinkingTerms) {
    this.noLinkingTerms = noLinkingTerms;
    return this;
  }

  /**
   * Get noLinkingTerms
   * @return noLinkingTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoLinkingTerms() {
    return noLinkingTerms;
  }

  public void setNoLinkingTerms(Boolean noLinkingTerms) {
    this.noLinkingTerms = noLinkingTerms;
  }

  public Terms whichLinkingTerms(List<String> whichLinkingTerms) {
    this.whichLinkingTerms = whichLinkingTerms;
    return this;
  }

  public Terms addWhichLinkingTermsItem(String whichLinkingTermsItem) {
    if (this.whichLinkingTerms == null) {
      this.whichLinkingTerms = new ArrayList<String>();
    }
    this.whichLinkingTerms.add(whichLinkingTermsItem);
    return this;
  }

  /**
   * Get whichLinkingTerms
   * @return whichLinkingTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichLinkingTerms() {
    return whichLinkingTerms;
  }

  public void setWhichLinkingTerms(List<String> whichLinkingTerms) {
    this.whichLinkingTerms = whichLinkingTerms;
  }

  public Terms noRecontactTerms(Boolean noRecontactTerms) {
    this.noRecontactTerms = noRecontactTerms;
    return this;
  }

  /**
   * Get noRecontactTerms
   * @return noRecontactTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoRecontactTerms() {
    return noRecontactTerms;
  }

  public void setNoRecontactTerms(Boolean noRecontactTerms) {
    this.noRecontactTerms = noRecontactTerms;
  }

  public Terms allowedRecontactTerms(List<String> allowedRecontactTerms) {
    this.allowedRecontactTerms = allowedRecontactTerms;
    return this;
  }

  public Terms addAllowedRecontactTermsItem(String allowedRecontactTermsItem) {
    if (this.allowedRecontactTerms == null) {
      this.allowedRecontactTerms = new ArrayList<String>();
    }
    this.allowedRecontactTerms.add(allowedRecontactTermsItem);
    return this;
  }

  /**
   * Get allowedRecontactTerms
   * @return allowedRecontactTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getAllowedRecontactTerms() {
    return allowedRecontactTerms;
  }

  public void setAllowedRecontactTerms(List<String> allowedRecontactTerms) {
    this.allowedRecontactTerms = allowedRecontactTerms;
  }

  public Terms compulsoryRecontactTerms(List<String> compulsoryRecontactTerms) {
    this.compulsoryRecontactTerms = compulsoryRecontactTerms;
    return this;
  }

  public Terms addCompulsoryRecontactTermsItem(String compulsoryRecontactTermsItem) {
    if (this.compulsoryRecontactTerms == null) {
      this.compulsoryRecontactTerms = new ArrayList<String>();
    }
    this.compulsoryRecontactTerms.add(compulsoryRecontactTermsItem);
    return this;
  }

  /**
   * Get compulsoryRecontactTerms
   * @return compulsoryRecontactTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getCompulsoryRecontactTerms() {
    return compulsoryRecontactTerms;
  }

  public void setCompulsoryRecontactTerms(List<String> compulsoryRecontactTerms) {
    this.compulsoryRecontactTerms = compulsoryRecontactTerms;
  }

  public Terms noIPClaimTerms(Boolean noIPClaimTerms) {
    this.noIPClaimTerms = noIPClaimTerms;
    return this;
  }

  /**
   * Get noIPClaimTerms
   * @return noIPClaimTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoIPClaimTerms() {
    return noIPClaimTerms;
  }

  public void setNoIPClaimTerms(Boolean noIPClaimTerms) {
    this.noIPClaimTerms = noIPClaimTerms;
  }

  public Terms whichIPClaimTerms(List<String> whichIPClaimTerms) {
    this.whichIPClaimTerms = whichIPClaimTerms;
    return this;
  }

  public Terms addWhichIPClaimTermsItem(String whichIPClaimTermsItem) {
    if (this.whichIPClaimTerms == null) {
      this.whichIPClaimTerms = new ArrayList<String>();
    }
    this.whichIPClaimTerms.add(whichIPClaimTermsItem);
    return this;
  }

  /**
   * Get whichIPClaimTerms
   * @return whichIPClaimTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichIPClaimTerms() {
    return whichIPClaimTerms;
  }

  public void setWhichIPClaimTerms(List<String> whichIPClaimTerms) {
    this.whichIPClaimTerms = whichIPClaimTerms;
  }

  public Terms noReportingTerms(Boolean noReportingTerms) {
    this.noReportingTerms = noReportingTerms;
    return this;
  }

  /**
   * Get noReportingTerms
   * @return noReportingTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoReportingTerms() {
    return noReportingTerms;
  }

  public void setNoReportingTerms(Boolean noReportingTerms) {
    this.noReportingTerms = noReportingTerms;
  }

  public Terms whichReportingTerms(List<String> whichReportingTerms) {
    this.whichReportingTerms = whichReportingTerms;
    return this;
  }

  public Terms addWhichReportingTermsItem(String whichReportingTermsItem) {
    if (this.whichReportingTerms == null) {
      this.whichReportingTerms = new ArrayList<String>();
    }
    this.whichReportingTerms.add(whichReportingTermsItem);
    return this;
  }

  /**
   * Get whichReportingTerms
   * @return whichReportingTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichReportingTerms() {
    return whichReportingTerms;
  }

  public void setWhichReportingTerms(List<String> whichReportingTerms) {
    this.whichReportingTerms = whichReportingTerms;
  }

  public Terms noCollaborationTerms(Boolean noCollaborationTerms) {
    this.noCollaborationTerms = noCollaborationTerms;
    return this;
  }

  /**
   * Get noCollaborationTerms
   * @return noCollaborationTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoCollaborationTerms() {
    return noCollaborationTerms;
  }

  public void setNoCollaborationTerms(Boolean noCollaborationTerms) {
    this.noCollaborationTerms = noCollaborationTerms;
  }

  public Terms whichCollaborationTerms(List<String> whichCollaborationTerms) {
    this.whichCollaborationTerms = whichCollaborationTerms;
    return this;
  }

  public Terms addWhichCollaborationTermsItem(String whichCollaborationTermsItem) {
    if (this.whichCollaborationTerms == null) {
      this.whichCollaborationTerms = new ArrayList<String>();
    }
    this.whichCollaborationTerms.add(whichCollaborationTermsItem);
    return this;
  }

  /**
   * Get whichCollaborationTerms
   * @return whichCollaborationTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichCollaborationTerms() {
    return whichCollaborationTerms;
  }

  public void setWhichCollaborationTerms(List<String> whichCollaborationTerms) {
    this.whichCollaborationTerms = whichCollaborationTerms;
  }

  public Terms noPaymentTerms(Boolean noPaymentTerms) {
    this.noPaymentTerms = noPaymentTerms;
    return this;
  }

  /**
   * Get noPaymentTerms
   * @return noPaymentTerms
  **/
  @ApiModelProperty(value = "")

  public Boolean isNoPaymentTerms() {
    return noPaymentTerms;
  }

  public void setNoPaymentTerms(Boolean noPaymentTerms) {
    this.noPaymentTerms = noPaymentTerms;
  }

  public Terms whichPaymentTerms(List<String> whichPaymentTerms) {
    this.whichPaymentTerms = whichPaymentTerms;
    return this;
  }

  public Terms addWhichPaymentTermsItem(String whichPaymentTermsItem) {
    if (this.whichPaymentTerms == null) {
      this.whichPaymentTerms = new ArrayList<String>();
    }
    this.whichPaymentTerms.add(whichPaymentTermsItem);
    return this;
  }

  /**
   * Get whichPaymentTerms
   * @return whichPaymentTerms
  **/
  @ApiModelProperty(value = "")

  public List<String> getWhichPaymentTerms() {
    return whichPaymentTerms;
  }

  public void setWhichPaymentTerms(List<String> whichPaymentTerms) {
    this.whichPaymentTerms = whichPaymentTerms;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Terms terms = (Terms) o;
    return Objects.equals(this.noAuthorizationTerms, terms.noAuthorizationTerms) &&
        Objects.equals(this.whichAuthorizationTerms, terms.whichAuthorizationTerms) &&
        Objects.equals(this.noPublicationTerms, terms.noPublicationTerms) &&
        Objects.equals(this.whichPublicationTerms, terms.whichPublicationTerms) &&
        Objects.equals(this.noTimelineTerms, terms.noTimelineTerms) &&
        Objects.equals(this.whichTimelineTerms, terms.whichTimelineTerms) &&
        Objects.equals(this.noSecurityTerms, terms.noSecurityTerms) &&
        Objects.equals(this.whichSecurityTerms, terms.whichSecurityTerms) &&
        Objects.equals(this.noExpungingTerms, terms.noExpungingTerms) &&
        Objects.equals(this.whichExpungingTerms, terms.whichExpungingTerms) &&
        Objects.equals(this.noLinkingTerms, terms.noLinkingTerms) &&
        Objects.equals(this.whichLinkingTerms, terms.whichLinkingTerms) &&
        Objects.equals(this.noRecontactTerms, terms.noRecontactTerms) &&
        Objects.equals(this.allowedRecontactTerms, terms.allowedRecontactTerms) &&
        Objects.equals(this.compulsoryRecontactTerms, terms.compulsoryRecontactTerms) &&
        Objects.equals(this.noIPClaimTerms, terms.noIPClaimTerms) &&
        Objects.equals(this.whichIPClaimTerms, terms.whichIPClaimTerms) &&
        Objects.equals(this.noReportingTerms, terms.noReportingTerms) &&
        Objects.equals(this.whichReportingTerms, terms.whichReportingTerms) &&
        Objects.equals(this.noCollaborationTerms, terms.noCollaborationTerms) &&
        Objects.equals(this.whichCollaborationTerms, terms.whichCollaborationTerms) &&
        Objects.equals(this.noPaymentTerms, terms.noPaymentTerms) &&
        Objects.equals(this.whichPaymentTerms, terms.whichPaymentTerms);
  }

  @Override
  public int hashCode() {
    return Objects.hash(noAuthorizationTerms, whichAuthorizationTerms, noPublicationTerms, whichPublicationTerms, noTimelineTerms, whichTimelineTerms, noSecurityTerms, whichSecurityTerms, noExpungingTerms, whichExpungingTerms, noLinkingTerms, whichLinkingTerms, noRecontactTerms, allowedRecontactTerms, compulsoryRecontactTerms, noIPClaimTerms, whichIPClaimTerms, noReportingTerms, whichReportingTerms, noCollaborationTerms, whichCollaborationTerms, noPaymentTerms, whichPaymentTerms);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Terms {\n");
    
    sb.append("    noAuthorizationTerms: ").append(toIndentedString(noAuthorizationTerms)).append("\n");
    sb.append("    whichAuthorizationTerms: ").append(toIndentedString(whichAuthorizationTerms)).append("\n");
    sb.append("    noPublicationTerms: ").append(toIndentedString(noPublicationTerms)).append("\n");
    sb.append("    whichPublicationTerms: ").append(toIndentedString(whichPublicationTerms)).append("\n");
    sb.append("    noTimelineTerms: ").append(toIndentedString(noTimelineTerms)).append("\n");
    sb.append("    whichTimelineTerms: ").append(toIndentedString(whichTimelineTerms)).append("\n");
    sb.append("    noSecurityTerms: ").append(toIndentedString(noSecurityTerms)).append("\n");
    sb.append("    whichSecurityTerms: ").append(toIndentedString(whichSecurityTerms)).append("\n");
    sb.append("    noExpungingTerms: ").append(toIndentedString(noExpungingTerms)).append("\n");
    sb.append("    whichExpungingTerms: ").append(toIndentedString(whichExpungingTerms)).append("\n");
    sb.append("    noLinkingTerms: ").append(toIndentedString(noLinkingTerms)).append("\n");
    sb.append("    whichLinkingTerms: ").append(toIndentedString(whichLinkingTerms)).append("\n");
    sb.append("    noRecontactTerms: ").append(toIndentedString(noRecontactTerms)).append("\n");
    sb.append("    allowedRecontactTerms: ").append(toIndentedString(allowedRecontactTerms)).append("\n");
    sb.append("    compulsoryRecontactTerms: ").append(toIndentedString(compulsoryRecontactTerms)).append("\n");
    sb.append("    noIPClaimTerms: ").append(toIndentedString(noIPClaimTerms)).append("\n");
    sb.append("    whichIPClaimTerms: ").append(toIndentedString(whichIPClaimTerms)).append("\n");
    sb.append("    noReportingTerms: ").append(toIndentedString(noReportingTerms)).append("\n");
    sb.append("    whichReportingTerms: ").append(toIndentedString(whichReportingTerms)).append("\n");
    sb.append("    noCollaborationTerms: ").append(toIndentedString(noCollaborationTerms)).append("\n");
    sb.append("    whichCollaborationTerms: ").append(toIndentedString(whichCollaborationTerms)).append("\n");
    sb.append("    noPaymentTerms: ").append(toIndentedString(noPaymentTerms)).append("\n");
    sb.append("    whichPaymentTerms: ").append(toIndentedString(whichPaymentTerms)).append("\n");
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
