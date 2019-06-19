package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets ClinicalProfile
 */
public enum ClinicalProfile {
  OTHER("OTHER"),
    DECISION_SUPPORT("DECISION_SUPPORT"),
    DISEASE("DISEASE");

  private String value;

  ClinicalProfile(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ClinicalProfile fromValue(String text) {
    for (ClinicalProfile b : ClinicalProfile.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
