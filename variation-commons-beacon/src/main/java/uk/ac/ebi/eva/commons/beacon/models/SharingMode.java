package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets SharingMode
 */
public enum SharingMode {
  UNKNOWN("UNKNOWN"),
    DISCOVERY("DISCOVERY"),
    ACCESS("ACCESS"),
    DISCOVERY_AND_ACCESS("DISCOVERY_AND_ACCESS");

  private String value;

  SharingMode(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static SharingMode fromValue(String text) {
    for (SharingMode b : SharingMode.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
