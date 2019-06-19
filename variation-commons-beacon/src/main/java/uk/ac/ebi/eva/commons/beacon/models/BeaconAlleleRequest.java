package uk.ac.ebi.eva.commons.beacon.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Allele request as interpreted by the beacon.
 */
@ApiModel(description = "Allele request as interpreted by the beacon.")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
public class BeaconAlleleRequest   {
    @JsonProperty("referenceName")
    private Chromosome referenceName = null;

    @JsonProperty("start")
    private Long start = null;

    @JsonProperty("end")
    private Integer end = null;

    @JsonProperty("startMin")
    private Integer startMin = null;

    @JsonProperty("startMax")
    private Integer startMax = null;

    @JsonProperty("endMin")
    private Integer endMin = null;

    @JsonProperty("endMax")
    private Integer endMax = null;

    @JsonProperty("referenceBases")
    private String referenceBases = null;

    @JsonProperty("alternateBases")
    private String alternateBases = null;

    @JsonProperty("variantType")
    private String variantType = null;

    @JsonProperty("assemblyId")
    private String assemblyId = null;

    @JsonProperty("datasetIds")
    @Valid
    private List<String> datasetIds = null;

    /**
     * Indicator of whether responses for individual datasets (datasetAlleleResponses) should be included in the response (BeaconAlleleResponse) to this request or not. If null (not specified), the default value of NONE is assumed.
     */
    public enum IncludeDatasetResponsesEnum {
        ALL("ALL"),

        HIT("HIT"),

        MISS("MISS"),

        NONE("NONE");

        private String value;

        IncludeDatasetResponsesEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static IncludeDatasetResponsesEnum fromValue(String text) {
            for (IncludeDatasetResponsesEnum b : IncludeDatasetResponsesEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }
    @JsonProperty("includeDatasetResponses")
    private IncludeDatasetResponsesEnum includeDatasetResponses = null;

    public BeaconAlleleRequest referenceName(Chromosome referenceName) {
        this.referenceName = referenceName;
        return this;
    }

    /**
     * Get referenceName
     * @return referenceName
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid
    public Chromosome getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(Chromosome referenceName) {
        this.referenceName = referenceName;
    }

    public BeaconAlleleRequest start(Long start) {
        this.start = start;
        return this;
    }

    /**
     * Precise start coordinate position, allele locus (0-based, inclusive). * start only:   - for single positions, e.g. the start of a specified sequence alteration where the size is given through the specified alternateBases   - typical use are queries for SNV and small InDels   - the use of \"start\" without an \"end\" parameter requires the use of \"referenceBases\" * start and end:   - special use case for exactly determined structural changes
     * minimum: 0
     * @return start
     **/
    @ApiModelProperty(value = "Precise start coordinate position, allele locus (0-based, inclusive). * start only:   - for single positions, e.g. the start of a specified sequence alteration where the size is given through the specified alternateBases   - typical use are queries for SNV and small InDels   - the use of \"start\" without an \"end\" parameter requires the use of \"referenceBases\" * start and end:   - special use case for exactly determined structural changes ")

    @Min(0L)  public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public BeaconAlleleRequest end(Integer end) {
        this.end = end;
        return this;
    }

    /**
     * Precise end coordinate (0-based, exclusive). See start.
     * @return end
     **/
    @ApiModelProperty(value = "Precise end coordinate (0-based, exclusive). See start.")

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public BeaconAlleleRequest startMin(Integer startMin) {
        this.startMin = startMin;
        return this;
    }

    /**
     * Minimum start coordinate * startMin + startMax + endMin + endMax   - for querying imprecise positions (e.g. identifying all structural variants starting anywhere between startMin <-> startMax, and ending anywhere between endMin <-> endMax)   - single or double sided precise matches can be achieved by setting startMin = startMax XOR endMin = endMax
     * @return startMin
     **/
    @ApiModelProperty(value = "Minimum start coordinate * startMin + startMax + endMin + endMax   - for querying imprecise positions (e.g. identifying all structural variants starting anywhere between startMin <-> startMax, and ending anywhere between endMin <-> endMax)   - single or double sided precise matches can be achieved by setting startMin = startMax XOR endMin = endMax ")

    public Integer getStartMin() {
        return startMin;
    }

    public void setStartMin(Integer startMin) {
        this.startMin = startMin;
    }

    public BeaconAlleleRequest startMax(Integer startMax) {
        this.startMax = startMax;
        return this;
    }

    /**
     * Maximum start coordinate. See startMin.
     * @return startMax
     **/
    @ApiModelProperty(value = "Maximum start coordinate. See startMin.")

    public Integer getStartMax() {
        return startMax;
    }

    public void setStartMax(Integer startMax) {
        this.startMax = startMax;
    }

    public BeaconAlleleRequest endMin(Integer endMin) {
        this.endMin = endMin;
        return this;
    }

    /**
     * Minimum end coordinate. See startMin.
     * @return endMin
     **/
    @ApiModelProperty(value = "Minimum end coordinate. See startMin.")

    public Integer getEndMin() {
        return endMin;
    }

    public void setEndMin(Integer endMin) {
        this.endMin = endMin;
    }

    public BeaconAlleleRequest endMax(Integer endMax) {
        this.endMax = endMax;
        return this;
    }

    /**
     * Maximum end coordinate. See startMin.
     * @return endMax
     **/
    @ApiModelProperty(value = "Maximum end coordinate. See startMin.")

    public Integer getEndMax() {
        return endMax;
    }

    public void setEndMax(Integer endMax) {
        this.endMax = endMax;
    }

    public BeaconAlleleRequest referenceBases(String referenceBases) {
        this.referenceBases = referenceBases;
        return this;
    }

    /**
     * Reference bases for this variant (starting from `start`). Accepted values: [ACGT]*   When querying for variants without specific base alterations (e.g. imprecise structural variants with separate variant_type as well as start_min & end_min ... parameters), the use of a single \"N\" value is required.
     * @return referenceBases
     **/
    @ApiModelProperty(required = true, value = "Reference bases for this variant (starting from `start`). Accepted values: [ACGT]*   When querying for variants without specific base alterations (e.g. imprecise structural variants with separate variant_type as well as start_min & end_min ... parameters), the use of a single \"N\" value is required. ")
    @NotNull

    @Pattern(regexp="^([ACGT]+|N)$")   public String getReferenceBases() {
        return referenceBases;
    }

    public void setReferenceBases(String referenceBases) {
        this.referenceBases = referenceBases;
    }

    public BeaconAlleleRequest alternateBases(String alternateBases) {
        this.alternateBases = alternateBases;
        return this;
    }

    /**
     * The bases that appear instead of the reference bases. Accepted values: [ACGT]* or N. Symbolic ALT alleles (DEL, INS, DUP, INV, CNV, DUP:TANDEM, DEL:ME, INS:ME) will be represented in `variantType`. Optional: either `alternateBases` or `variantType` is required.
     * @return alternateBases
     **/
    @ApiModelProperty(value = "The bases that appear instead of the reference bases. Accepted values: [ACGT]* or N. Symbolic ALT alleles (DEL, INS, DUP, INV, CNV, DUP:TANDEM, DEL:ME, INS:ME) will be represented in `variantType`. Optional: either `alternateBases` or `variantType` is required. ")

    @Pattern(regexp="^([ACGT]+|N)$")   public String getAlternateBases() {
        return alternateBases;
    }

    public void setAlternateBases(String alternateBases) {
        this.alternateBases = alternateBases;
    }

    public BeaconAlleleRequest variantType(String variantType) {
        this.variantType = variantType;
        return this;
    }

    /**
     * The `variantType` is used to denote e.g. structural variants. Examples: * DUP: duplication of sequence following `start`; not necessarily in situ * DEL: deletion of sequence following `start`  Optional: either `alternateBases` or `variantType` is required.
     * @return variantType
     **/
    @ApiModelProperty(value = "The `variantType` is used to denote e.g. structural variants. Examples: * DUP: duplication of sequence following `start`; not necessarily in situ * DEL: deletion of sequence following `start`  Optional: either `alternateBases` or `variantType` is required. ")

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public BeaconAlleleRequest assemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
        return this;
    }

    /**
     * Assembly identifier (GRC notation, e.g. `GRCh37`).
     * @return assemblyId
     **/
    @ApiModelProperty(example = "GRCh38", required = true, value = "Assembly identifier (GRC notation, e.g. `GRCh37`).")
    @NotNull

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    public BeaconAlleleRequest datasetIds(List<String> datasetIds) {
        this.datasetIds = datasetIds;
        return this;
    }

    public BeaconAlleleRequest addDatasetIdsItem(String datasetIdsItem) {
        if (this.datasetIds == null) {
            this.datasetIds = new ArrayList<String>();
        }
        this.datasetIds.add(datasetIdsItem);
        return this;
    }

    /**
     * Identifiers of datasets, as defined in `BeaconDataset`. If this field is null/not specified, all datasets should be queried.
     * @return datasetIds
     **/
    @ApiModelProperty(value = "Identifiers of datasets, as defined in `BeaconDataset`. If this field is null/not specified, all datasets should be queried.")

    public List<String> getDatasetIds() {
        return datasetIds;
    }

    public void setDatasetIds(List<String> datasetIds) {
        this.datasetIds = datasetIds;
    }

    public BeaconAlleleRequest includeDatasetResponses(IncludeDatasetResponsesEnum includeDatasetResponses) {
        this.includeDatasetResponses = includeDatasetResponses;
        return this;
    }

    /**
     * Indicator of whether responses for individual datasets (datasetAlleleResponses) should be included in the response (BeaconAlleleResponse) to this request or not. If null (not specified), the default value of NONE is assumed.
     * @return includeDatasetResponses
     **/
    @ApiModelProperty(value = "Indicator of whether responses for individual datasets (datasetAlleleResponses) should be included in the response (BeaconAlleleResponse) to this request or not. If null (not specified), the default value of NONE is assumed.")

    public IncludeDatasetResponsesEnum getIncludeDatasetResponses() {
        return includeDatasetResponses;
    }

    public void setIncludeDatasetResponses(IncludeDatasetResponsesEnum includeDatasetResponses) {
        this.includeDatasetResponses = includeDatasetResponses;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BeaconAlleleRequest beaconAlleleRequest = (BeaconAlleleRequest) o;
        return Objects.equals(this.referenceName, beaconAlleleRequest.referenceName) &&
                Objects.equals(this.start, beaconAlleleRequest.start) &&
                Objects.equals(this.end, beaconAlleleRequest.end) &&
                Objects.equals(this.startMin, beaconAlleleRequest.startMin) &&
                Objects.equals(this.startMax, beaconAlleleRequest.startMax) &&
                Objects.equals(this.endMin, beaconAlleleRequest.endMin) &&
                Objects.equals(this.endMax, beaconAlleleRequest.endMax) &&
                Objects.equals(this.referenceBases, beaconAlleleRequest.referenceBases) &&
                Objects.equals(this.alternateBases, beaconAlleleRequest.alternateBases) &&
                Objects.equals(this.variantType, beaconAlleleRequest.variantType) &&
                Objects.equals(this.assemblyId, beaconAlleleRequest.assemblyId) &&
                Objects.equals(this.datasetIds, beaconAlleleRequest.datasetIds) &&
                Objects.equals(this.includeDatasetResponses, beaconAlleleRequest.includeDatasetResponses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referenceName, start, end, startMin, startMax, endMin, endMax, referenceBases, alternateBases, variantType, assemblyId, datasetIds, includeDatasetResponses);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BeaconAlleleRequest {\n");

        sb.append("    referenceName: ").append(toIndentedString(referenceName)).append("\n");
        sb.append("    start: ").append(toIndentedString(start)).append("\n");
        sb.append("    end: ").append(toIndentedString(end)).append("\n");
        sb.append("    startMin: ").append(toIndentedString(startMin)).append("\n");
        sb.append("    startMax: ").append(toIndentedString(startMax)).append("\n");
        sb.append("    endMin: ").append(toIndentedString(endMin)).append("\n");
        sb.append("    endMax: ").append(toIndentedString(endMax)).append("\n");
        sb.append("    referenceBases: ").append(toIndentedString(referenceBases)).append("\n");
        sb.append("    alternateBases: ").append(toIndentedString(alternateBases)).append("\n");
        sb.append("    variantType: ").append(toIndentedString(variantType)).append("\n");
        sb.append("    assemblyId: ").append(toIndentedString(assemblyId)).append("\n");
        sb.append("    datasetIds: ").append(toIndentedString(datasetIds)).append("\n");
        sb.append("    includeDatasetResponses: ").append(toIndentedString(includeDatasetResponses)).append("\n");
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
