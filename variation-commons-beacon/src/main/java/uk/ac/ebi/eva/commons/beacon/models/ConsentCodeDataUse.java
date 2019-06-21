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
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Data use of a resource based on consent codes.
 */
@ApiModel(description = "Data use of a resource based on consent codes.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class ConsentCodeDataUse   {
  @JsonProperty("primaryCategory")
  private ConsentCodeDataUseCondition primaryCategory = null;

  @JsonProperty("secondaryCategories")
  @Valid
  private List<ConsentCodeDataUseCondition> secondaryCategories = null;

  @JsonProperty("requirements")
  @Valid
  private List<ConsentCodeDataUseCondition> requirements = null;

  @JsonProperty("version")
  private String version = null;

  public ConsentCodeDataUse primaryCategory(ConsentCodeDataUseCondition primaryCategory) {
    this.primaryCategory = primaryCategory;
    return this;
  }

  /**
   * Get primaryCategory
   * @return primaryCategory
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public ConsentCodeDataUseCondition getPrimaryCategory() {
    return primaryCategory;
  }

  public void setPrimaryCategory(ConsentCodeDataUseCondition primaryCategory) {
    this.primaryCategory = primaryCategory;
  }

  public ConsentCodeDataUse secondaryCategories(List<ConsentCodeDataUseCondition> secondaryCategories) {
    this.secondaryCategories = secondaryCategories;
    return this;
  }

  public ConsentCodeDataUse addSecondaryCategoriesItem(ConsentCodeDataUseCondition secondaryCategoriesItem) {
    if (this.secondaryCategories == null) {
      this.secondaryCategories = new ArrayList<ConsentCodeDataUseCondition>();
    }
    this.secondaryCategories.add(secondaryCategoriesItem);
    return this;
  }

  /**
   * Get secondaryCategories
   * @return secondaryCategories
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ConsentCodeDataUseCondition> getSecondaryCategories() {
    return secondaryCategories;
  }

  public void setSecondaryCategories(List<ConsentCodeDataUseCondition> secondaryCategories) {
    this.secondaryCategories = secondaryCategories;
  }

  public ConsentCodeDataUse requirements(List<ConsentCodeDataUseCondition> requirements) {
    this.requirements = requirements;
    return this;
  }

  public ConsentCodeDataUse addRequirementsItem(ConsentCodeDataUseCondition requirementsItem) {
    if (this.requirements == null) {
      this.requirements = new ArrayList<ConsentCodeDataUseCondition>();
    }
    this.requirements.add(requirementsItem);
    return this;
  }

  /**
   * Get requirements
   * @return requirements
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ConsentCodeDataUseCondition> getRequirements() {
    return requirements;
  }

  public void setRequirements(List<ConsentCodeDataUseCondition> requirements) {
    this.requirements = requirements;
  }

  public ConsentCodeDataUse version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Version of the data use specification.
   * @return version
  **/
  @ApiModelProperty(example = "0.1", required = true, value = "Version of the data use specification.")
  @NotNull

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConsentCodeDataUse consentCodeDataUse = (ConsentCodeDataUse) o;
    return Objects.equals(this.primaryCategory, consentCodeDataUse.primaryCategory) &&
        Objects.equals(this.secondaryCategories, consentCodeDataUse.secondaryCategories) &&
        Objects.equals(this.requirements, consentCodeDataUse.requirements) &&
        Objects.equals(this.version, consentCodeDataUse.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(primaryCategory, secondaryCategories, requirements, version);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConsentCodeDataUse {\n");
    
    sb.append("    primaryCategory: ").append(toIndentedString(primaryCategory)).append("\n");
    sb.append("    secondaryCategories: ").append(toIndentedString(secondaryCategories)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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
