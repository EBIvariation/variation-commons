package uk.ac.ebi.eva.commons.core.models.genotype;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

public class GenoTypeCountMapper {
    @Field("files.sid")
    @Id
    private String studyId;
    private List<String> mapper;
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getStudyId() {
        return studyId;
    }

    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }

    public List<String> getMapper() {
        return mapper;
    }

    public void setMapper(List<String> mapper) {
        this.mapper = mapper;
    }
}
