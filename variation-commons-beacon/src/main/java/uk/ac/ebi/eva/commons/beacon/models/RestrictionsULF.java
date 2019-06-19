package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets RestrictionsULF
 */
public enum RestrictionsULF {
  UNRESTRICTED("UNRESTRICTED"),
    LIMITED("LIMITED"),
    UNRESTRICTED_OBLIGATORY("UNRESTRICTED_OBLIGATORY"),
    LIMITED_OBLIGATORY("LIMITED_OBLIGATORY"),
    FORBIDDEN("FORBIDDEN");

  private String value;

  RestrictionsULF(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static RestrictionsULF fromValue(String text) {
    for (RestrictionsULF b : RestrictionsULF.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
