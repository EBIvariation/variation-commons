package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Intepretation rule if multiple obligatory profiles are specified.
 */
public enum MultipleObligationsRule {
  ALL_OBLIGATIONS("MEET_ALL_OBLIGATIONS"),
    AT_LEAST_ONE_OBLIGATION("MEET_AT_LEAST_ONE_OBLIGATION");

  private String value;

  MultipleObligationsRule(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static MultipleObligationsRule fromValue(String text) {
    for (MultipleObligationsRule b : MultipleObligationsRule.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
