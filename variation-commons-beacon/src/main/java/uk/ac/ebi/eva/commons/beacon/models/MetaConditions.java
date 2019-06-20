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
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * MetaConditions
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class MetaConditions   {
  @JsonProperty("sharingMode")
  private SharingMode sharingMode = null;

  @JsonProperty("multipleObligationsRule")
  private MultipleObligationsRule multipleObligationsRule = null;

  @JsonProperty("noOtherConditions")
  private Boolean noOtherConditions = null;

  @JsonProperty("whichOtherConditions")
  @Valid
  private List<String> whichOtherConditions = null;

  @JsonProperty("sensitivePopulations")
  private Boolean sensitivePopulations = null;

  @JsonProperty("uniformConsent")
  private Boolean uniformConsent = null;

  public MetaConditions sharingMode(SharingMode sharingMode) {
    this.sharingMode = sharingMode;
    return this;
  }

  /**
   * Get sharingMode
   * @return sharingMode
  **/
  @ApiModelProperty(value = "")

  @Valid
  public SharingMode getSharingMode() {
    return sharingMode;
  }

  public void setSharingMode(SharingMode sharingMode) {
    this.sharingMode = sharingMode;
  }

  public MetaConditions multipleObligationsRule(MultipleObligationsRule multipleObligationsRule) {
    this.multipleObligationsRule = multipleObligationsRule;
    return this;
  }

  /**
   * Get multipleObligationsRule
   * @return multipleObligationsRule
  **/
  @ApiModelProperty(value = "")

  @Valid
  public MultipleObligationsRule getMultipleObligationsRule() {
    return multipleObligationsRule;
  }

  public void setMultipleObligationsRule(MultipleObligationsRule multipleObligationsRule) {
    this.multipleObligationsRule = multipleObligationsRule;
  }

  public MetaConditions noOtherConditions(Boolean noOtherConditions) {
    this.noOtherConditions = noOtherConditions;
    return this;
  }

  /**
   * There are no other use restrictions/limitations in force which are not herein specified.
   * @return noOtherConditions
  **/
  @ApiModelProperty(value = "There are no other use restrictions/limitations in force which are not herein specified.")

  public Boolean isNoOtherConditions() {
    return noOtherConditions;
  }

  public void setNoOtherConditions(Boolean noOtherConditions) {
    this.noOtherConditions = noOtherConditions;
  }

  public MetaConditions whichOtherConditions(List<String> whichOtherConditions) {
    this.whichOtherConditions = whichOtherConditions;
    return this;
  }

  public MetaConditions addWhichOtherConditionsItem(String whichOtherConditionsItem) {
    if (this.whichOtherConditions == null) {
      this.whichOtherConditions = new ArrayList<String>();
    }
    this.whichOtherConditions.add(whichOtherConditionsItem);
    return this;
  }

  /**
   * Other permissions/limitations may apply as specified.
   * @return whichOtherConditions
  **/
  @ApiModelProperty(value = "Other permissions/limitations may apply as specified.")

  public List<String> getWhichOtherConditions() {
    return whichOtherConditions;
  }

  public void setWhichOtherConditions(List<String> whichOtherConditions) {
    this.whichOtherConditions = whichOtherConditions;
  }

  public MetaConditions sensitivePopulations(Boolean sensitivePopulations) {
    this.sensitivePopulations = sensitivePopulations;
    return this;
  }

  /**
   * No special evaluation required for access requests involving sensitive/restricted populations.
   * @return sensitivePopulations
  **/
  @ApiModelProperty(value = "No special evaluation required for access requests involving sensitive/restricted populations.")

  public Boolean isSensitivePopulations() {
    return sensitivePopulations;
  }

  public void setSensitivePopulations(Boolean sensitivePopulations) {
    this.sensitivePopulations = sensitivePopulations;
  }

  public MetaConditions uniformConsent(Boolean uniformConsent) {
    this.uniformConsent = uniformConsent;
    return this;
  }

  /**
   * Identical consent permissions have been provided by all subjects.
   * @return uniformConsent
  **/
  @ApiModelProperty(value = "Identical consent permissions have been provided by all subjects.")

  public Boolean isUniformConsent() {
    return uniformConsent;
  }

  public void setUniformConsent(Boolean uniformConsent) {
    this.uniformConsent = uniformConsent;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaConditions metaConditions = (MetaConditions) o;
    return Objects.equals(this.sharingMode, metaConditions.sharingMode) &&
        Objects.equals(this.multipleObligationsRule, metaConditions.multipleObligationsRule) &&
        Objects.equals(this.noOtherConditions, metaConditions.noOtherConditions) &&
        Objects.equals(this.whichOtherConditions, metaConditions.whichOtherConditions) &&
        Objects.equals(this.sensitivePopulations, metaConditions.sensitivePopulations) &&
        Objects.equals(this.uniformConsent, metaConditions.uniformConsent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sharingMode, multipleObligationsRule, noOtherConditions, whichOtherConditions, sensitivePopulations, uniformConsent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetaConditions {\n");
    
    sb.append("    sharingMode: ").append(toIndentedString(sharingMode)).append("\n");
    sb.append("    multipleObligationsRule: ").append(toIndentedString(multipleObligationsRule)).append("\n");
    sb.append("    noOtherConditions: ").append(toIndentedString(noOtherConditions)).append("\n");
    sb.append("    whichOtherConditions: ").append(toIndentedString(whichOtherConditions)).append("\n");
    sb.append("    sensitivePopulations: ").append(toIndentedString(sensitivePopulations)).append("\n");
    sb.append("    uniformConsent: ").append(toIndentedString(uniformConsent)).append("\n");
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
