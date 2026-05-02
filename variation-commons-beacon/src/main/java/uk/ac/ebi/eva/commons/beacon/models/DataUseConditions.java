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

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * Data use conditions ruling this dataset.
 */
@Schema(description = "Data use conditions ruling this dataset.")
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class DataUseConditions   {
  @JsonProperty("consentCodeDataUse")
  private ConsentCodeDataUse consentCodeDataUse = null;

  @JsonProperty("adamDataUse")
  private AdamDataUse adamDataUse = null;

  public DataUseConditions consentCodeDataUse(ConsentCodeDataUse consentCodeDataUse) {
    this.consentCodeDataUse = consentCodeDataUse;
    return this;
  }

  /**
   * Get consentCodeDataUse
   * @return consentCodeDataUse
  **/
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description ="")
  @NotNull

  @Valid
  public ConsentCodeDataUse getConsentCodeDataUse() {
    return consentCodeDataUse;
  }

  public void setConsentCodeDataUse(ConsentCodeDataUse consentCodeDataUse) {
    this.consentCodeDataUse = consentCodeDataUse;
  }

  public DataUseConditions adamDataUse(AdamDataUse adamDataUse) {
    this.adamDataUse = adamDataUse;
    return this;
  }

  /**
   * Get adamDataUse
   * @return adamDataUse
  **/
  @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description ="")
  @NotNull

  @Valid
  public AdamDataUse getAdamDataUse() {
    return adamDataUse;
  }

  public void setAdamDataUse(AdamDataUse adamDataUse) {
    this.adamDataUse = adamDataUse;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataUseConditions dataUseConditions = (DataUseConditions) o;
    return Objects.equals(this.consentCodeDataUse, dataUseConditions.consentCodeDataUse) &&
        Objects.equals(this.adamDataUse, dataUseConditions.adamDataUse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consentCodeDataUse, adamDataUse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataUseConditions {\n");
    
    sb.append("    consentCodeDataUse: ").append(toIndentedString(consentCodeDataUse)).append("\n");
    sb.append("    adamDataUse: ").append(toIndentedString(adamDataUse)).append("\n");
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
