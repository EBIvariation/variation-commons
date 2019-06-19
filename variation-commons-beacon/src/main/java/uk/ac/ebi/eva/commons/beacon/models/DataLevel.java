package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets DataLevel
 */
public enum DataLevel {
  UNKNOWN("UNKNOWN"),
    DATABASE("DATABASE"),
    METADATA("METADATA"),
    SUMMARISED("SUMMARISED"),
    DATASET("DATASET"),
    RECORDSET("RECORDSET"),
    RECORD("RECORD"),
    RECORDFIELD("RECORDFIELD");

  private String value;

  DataLevel(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static DataLevel fromValue(String text) {
    for (DataLevel b : DataLevel.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
