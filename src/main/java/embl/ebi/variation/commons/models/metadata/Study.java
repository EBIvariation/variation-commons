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
public class Study implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studyId;

    private String studyAccession; // Bioproject ID?

    private String title;
    private String alias;
    private String description;

    private String centre;
//    private Organisation centre; // could this be used to connect an Organisation class? Organisation class could be used for broker attribute too

//    private Taxonomy taxonomy; // When there is a taxonomy

    private StudyEnums.Scope scope; // controlled vocabulary, use enum?
    private StudyEnums.Material material; // controlled vocabulary, use enum?
//    private Set<Publication> publications; // if there will be separate publication class

//    private Set<Study> associatedStudies = new HashSet<Study>(); // in submission template is described as: "Associated Project(s)	Accession OR Alias of all project(s) assoicated to this project (NCBI, ENA, EVA all share the same project accession space so no database distinction is necessary) (e.g. PRJEB4019)" but will a hierarchy of studies be needed instead?
//    private Study parentStudy;
//    private Set<Study> childStudies = new HashSet<Study>(); // ... or use this hierarchical relationship?

//    private Set<Link> links = new HashSet<Link>(); // should this be given as a string or as relation to other "Link" class? Submission template gives as: "Link(s)	Links in the form DB:ID:LABEL (e.g. DGVA:esv1, DBSNP:rs149486)"
    private LocalDateTime holdDate;
//    private Set<Collaborator> collaborators = new HashSet<Collaborator>(); // link to collaborator class? collaborator could be part of multiple studies

//    private Strain strain; // part of submission template, but should they be strings or separate classes?
//    private Breed breed;

//    private Broker broker; // if there is a separate broker/organisation class

    private Set<String> urls = new HashSet<String>();
    private String type; // e.g. umbrella, pop genomics BUT separate column for study_type (aggregate, control set, case control)


    protected Study(){}

    public Study(String studyAccession, Long studyId, String centre, StudyEnums.Material material, StudyEnums.Scope scope, String type) {
        this.studyAccession = studyAccession;
        this.studyId = studyId;
        this.centre = centre;
        this.material = material;
        this.scope = scope;
        this.type = type;
    }


    public Long getStudyId() {
        return studyId;
    }

    public void setStudyId(Long studyId) {
        this.studyId = studyId;
    }

    public String getStudyAccession() {
        return studyAccession;
    }

    public void setStudyAccession(String studyAccession) {
        this.studyAccession = studyAccession;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public StudyEnums.Scope getScope() {
        return scope;
    }

    public void setScope(StudyEnums.Scope scope) {
        this.scope = scope;
    }

    public StudyEnums.Material getMaterial() {
        return material;
    }

    public void setMaterial(StudyEnums.Material material) {
        this.material = material;
    }

    public LocalDateTime getHoldDate() {
        return holdDate;
    }

    public void setHoldDate(LocalDateTime holdDate) {
        this.holdDate = holdDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
