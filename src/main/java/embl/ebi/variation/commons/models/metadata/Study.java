/*
 * Copyright 2015 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package embl.ebi.variation.commons.models.metadata;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Study")
public class Study implements Serializable {

    @Id
    @Column(name = "study_id", nullable = false)
    private int studyId;

    @ManyToOne(optional = false)
    @JoinColumn(name="submission_id", unique=true, nullable = false) // unsure if both of these attributes are needed if object has a Submission
    private Submission submission; // TODO submission class needed
    private int submissionId; // submission_id

    @Column(name = "stable_id", unique=true, nullable = false)
    private String stableId;
    @Column(name = "study_type", nullable = false)
    private String studyType;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "title")
    private String title;
    @Column(name = "abstract")
    private String abstract_;
    @Column(name = "description")
    private String description;
    @Column(name = "accession")
    private String accession;
    @Column(name = "url")
    private String url;
    @Column(name = "current")
    private int current;
    @Column(name = "released")
    private int released;
    @Column(name = "date")
    private LocalDateTime date;
    @ManyToOne // Unsure of how many parent studies a study can have
    @JoinColumn(name="parent_id")
    private Study parentStudy;
    @OneToMany(mappedBy = "parentStudy", cascade = CascadeType.ALL)
    private Set<Study> childStudies = new HashSet<Study>();

    Study() {
    }

    public Study(int studyId, Submission submission, int submissionId, String stableId, String studyType, String name) {
        this.studyId = studyId;
        this.submission = submission;
        this.submissionId = submissionId;
        this.stableId = stableId;
        this.studyType = studyType;
        this.name = name;
    }

    // TODO getter and setter for submission relation

    public int getStudyId() {
        return studyId;
    }

    public void setStudyId(int studyId) {
        this.studyId = studyId;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public String getStableId() {
        return stableId;
    }

    public void setStableId(String stableId) {
        this.stableId = stableId;
    }

    public String getStudyType() {
        return studyType;
    }

    public void setStudyType(String studyType) {
        this.studyType = studyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract_() {
        return abstract_;
    }

    public void setAbstract_(String abstract_) {
        this.abstract_ = abstract_;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
