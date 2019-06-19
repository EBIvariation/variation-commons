package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ProfileUpdates
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
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
  @ApiModelProperty(example = "2012-07-19 or 2017-01-17T20:33:40Z", value = "Date the change was made to the profile.")

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
  @ApiModelProperty(value = "Text describing the profile change made.")

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
