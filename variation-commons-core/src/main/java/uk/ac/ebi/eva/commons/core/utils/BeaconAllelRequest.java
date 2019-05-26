package uk.ac.ebi.eva.commons.core.utils;

import java.util.List;

public class BeaconAllelRequest {
    String chr;
    Long start;
    String startMin;
    String startMax;
    Long end;
    String endMin;
    String endMax;
    String referenceBases;
    String alternateBases;
    String variantType;
    String assemblyId;
    List<String> studies;
    String includeDatasetResponses;

    public BeaconAllelRequest(String chr, Long start, String startMin, String startMax, Long end, String endMin, String endMax, String referenceBases, String alternateBases, String variantType, String assemblyId, List<String> studies, String includeDatasetResponses) {
        this.chr = chr;
        this.start = start;
        this.startMin = startMin;
        this.startMax = startMax;
        this.end = end;
        this.endMin = endMin;
        this.endMax = endMax;
        this.referenceBases = referenceBases;
        this.alternateBases = alternateBases;
        this.variantType = variantType;
        this.assemblyId = assemblyId;
        this.studies = studies;
        this.includeDatasetResponses = includeDatasetResponses;
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public String getStartMin() {
        return startMin;
    }

    public void setStartMin(String startMin) {
        this.startMin = startMin;
    }

    public String getStartMax() {
        return startMax;
    }

    public void setStartMax(String startMax) {
        this.startMax = startMax;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public String getEndMin() {
        return endMin;
    }

    public void setEndMin(String endMin) {
        this.endMin = endMin;
    }

    public String getEndMax() {
        return endMax;
    }

    public void setEndMax(String endMax) {
        this.endMax = endMax;
    }

    public String getReferenceBases() {
        return referenceBases;
    }

    public void setReferenceBases(String referenceBases) {
        this.referenceBases = referenceBases;
    }

    public String getAlternateBases() {
        return alternateBases;
    }

    public void setAlternateBases(String alternateBases) {
        this.alternateBases = alternateBases;
    }

    public String getVariantType() {
        return variantType;
    }

    public void setVariantType(String variantType) {
        this.variantType = variantType;
    }

    public String getAssemblyId() {
        return assemblyId;
    }

    public void setAssemblyId(String assemblyId) {
        this.assemblyId = assemblyId;
    }

    public List<String> getStudies() {
        return studies;
    }

    public void setStudies(List<String> studies) {
        this.studies = studies;
    }

    public String getIncludeDatasetResponses() {
        return includeDatasetResponses;
    }

    public void setIncludeDatasetResponses(String includeDatasetResponses) {
        this.includeDatasetResponses = includeDatasetResponses;
    }
}
