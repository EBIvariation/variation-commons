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
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * ProfileUpdates
 */
@Validated
@jakarta.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class ProfileUpdates   {
  @JsonProperty("date")
  private String date = null;

  @JsonProperty("description")
  private String description = null;

  public ProfileUpdates date(String date) {
    this.date = date;
    return this;
  }

  /**
   * Date the change was made to the profile.
   * @return date
  **/
  @Schema(example = "2012-07-19 or 2017-01-17T20:33:40Z", description = "Date the change was made to the profile.")

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public ProfileUpdates description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Text describing the profile change made.
   * @return description
  **/
  @Schema(description = "Text describing the profile change made.")

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfileUpdates profileUpdates = (ProfileUpdates) o;
    return Objects.equals(this.date, profileUpdates.date) &&
        Objects.equals(this.description, profileUpdates.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(date, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProfileUpdates {\n");
    
    sb.append("    date: ").append(toIndentedString(date)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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
