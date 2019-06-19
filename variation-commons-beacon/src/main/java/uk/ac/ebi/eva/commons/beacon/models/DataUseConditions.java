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
 * Data use conditions ruling this dataset.
 */
@ApiModel(description = "Data use conditions ruling this dataset.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class DataUseConditions   {
  @JsonProperty("consentCodeDataUse")
  private ConsentCodeDataUse consentCodeDataUse = null;

  @JsonProperty("adamDataUse")
  private AdamDataUse adamDataUse = null;

  public DataUseConditions consentCodeDataUse(ConsentCodeDataUse consentCodeDataUse) {
    this.consentCodeDataUse = consentCodeDataUse;
    return this;
  }

  /**
   * Get consentCodeDataUse
   * @return consentCodeDataUse
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public ConsentCodeDataUse getConsentCodeDataUse() {
    return consentCodeDataUse;
  }

  public void setConsentCodeDataUse(ConsentCodeDataUse consentCodeDataUse) {
    this.consentCodeDataUse = consentCodeDataUse;
  }

  public DataUseConditions adamDataUse(AdamDataUse adamDataUse) {
    this.adamDataUse = adamDataUse;
    return this;
  }

  /**
   * Get adamDataUse
   * @return adamDataUse
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid
  public AdamDataUse getAdamDataUse() {
    return adamDataUse;
  }

  public void setAdamDataUse(AdamDataUse adamDataUse) {
    this.adamDataUse = adamDataUse;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataUseConditions dataUseConditions = (DataUseConditions) o;
    return Objects.equals(this.consentCodeDataUse, dataUseConditions.consentCodeDataUse) &&
        Objects.equals(this.adamDataUse, dataUseConditions.adamDataUse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consentCodeDataUse, adamDataUse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataUseConditions {\n");
    
    sb.append("    consentCodeDataUse: ").append(toIndentedString(consentCodeDataUse)).append("\n");
    sb.append("    adamDataUse: ").append(toIndentedString(adamDataUse)).append("\n");
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
