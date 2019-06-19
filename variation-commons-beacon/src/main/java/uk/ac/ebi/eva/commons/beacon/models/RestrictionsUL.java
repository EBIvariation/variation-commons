package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets RestrictionsUL
 */
public enum RestrictionsUL {
  UNRESTRICTED("UNRESTRICTED"),
    LIMITED("LIMITED");

  private String value;

  RestrictionsUL(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RestrictionsUL fromValue(String text) {
    for (RestrictionsUL b : RestrictionsUL.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
