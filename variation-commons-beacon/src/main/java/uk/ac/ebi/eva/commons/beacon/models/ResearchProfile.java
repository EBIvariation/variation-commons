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
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets ResearchProfile
 */
public enum ResearchProfile {
  OTHER("OTHER"),
    METHODS("METHODS"),
    CONTROL("CONTROL"),
    POPULATION("POPULATION"),
    ANCESTRY("ANCESTRY"),
    BIOMEDICAL("BIOMEDICAL"),
    FUNDAMENTAL("FUNDAMENTAL"),
    GENETIC("GENETIC"),
    DRUG("DRUG"),
    DISEASE("DISEASE"),
    GENDER("GENDER"),
    AGE("AGE");

  private String value;

  ResearchProfile(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ResearchProfile fromValue(String text) {
    for (ResearchProfile b : ResearchProfile.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
