package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

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
