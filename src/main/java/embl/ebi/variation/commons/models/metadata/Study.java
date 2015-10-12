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
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Study implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studyId;

    private String studyAccession; // Bioproject ID?
    private String title;
    private Centre centre; //TODO Make a set and connect an Organisation class (i.e. private Organisation centre)? Organisation class could be used for broker attribute too
    private StudyEnums.Material material; // controlled vocabulary, use enum?
    private StudyEnums.Scope scope; // controlled vocabulary, use enum?
    private String type; // e.g. umbrella, pop genomics BUT separate column for study_type (aggregate, control set, case control)

    private Set<FileGenerator> fileGenerators = new HashSet<FileGenerator>();

    private String alias;
    private String description;
    private Set<URI> uris = new HashSet<URI>();
//    private Taxon taxon; // When there is a taxon class
    private Set<Publication> publications = new HashSet<>(); // TODO   private Set<Publication> publications; if there will be separate publication class
    private String broker; // if there is a separate broker/organisation class

//    private Set<Study> associatedStudies = new HashSet<Study>(); // in submission template is described as: "Associated Project(s)	Accession OR Alias of all project(s) assoicated to this project (NCBI, ENA, EVA all share the same project accession space so no database distinction is necessary) (e.g. PRJEB4019)" but will a hierarchy of studies be needed instead?
//    private Study parentStudy;
//    private Set<Study> childStudies = new HashSet<Study>(); // ... or use this hierarchical relationship?


    public Study(String studyAccession, Centre centre, StudyEnums.Material material, StudyEnums.Scope scope, String type) {
        this.setStudyAccession(studyAccession);
        this.centre = centre;
        this.material = material;
        this.scope = scope;
        this.type = type;
    }


    public Long getStudyId() {
        return studyId;
    }

    public String getStudyAccession() {
        return studyAccession;
    }

    public void setStudyAccession(String studyAccession) {
        // starts with "PRJEA or PRJEB PRJNA\d+"
        if(studyAccession.matches("PRJ(E(A|B)|NA)\\d+")){
            this.studyAccession = studyAccession;
        }else{
            throw new IllegalArgumentException("Study accession must begin with a prefix from the following: (PRJEA, PRJEB, PRJNA), followed by multiple numerical digits.");
            // TODO openpojo tester throws exception with this
        }
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

    public Centre getCentre() {
        return centre;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
        centre.addStudy(this); // should the study be adding itself to the centre, or the other way around? which is responsible?
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<URI> getUris() {
        return Collections.unmodifiableSet(uris);
    }

    public void addUri(URI uri){
        uris.add(uri);
    }

    public void removeUrl(URI url){
        uris.remove(url);
    }

    public void setUris(Set<URI> uris) {
        this.uris.clear();
        if(uris != null){
            for(URI uri: uris) {
                addUri(uri);
            }
        }
    }

    public Set<FileGenerator> getFileGenerators() {
        return Collections.unmodifiableSet(fileGenerators);
    }

    public void removeFileGenerator(FileGenerator fileGenerator){
        fileGenerators.remove(fileGenerator);
        fileGenerator.removeFromStudy();
    }

    public void addFileGenerator(FileGenerator fileGenerator){
        fileGenerators.add(fileGenerator);
        fileGenerator.setStudy(this);
    }

    public void setFileGenerators(Set<FileGenerator> fileGenerators) {
        this.fileGenerators.clear();
        for(FileGenerator fileGenerator: fileGenerators){
            addFileGenerator(fileGenerator);
        }
    }

    public Set<Publication> getPublications() {
        return Collections.unmodifiableSet(publications);
    }

    public void removePublication(String publication){
        publications.remove(publication);
    }

    public void addPublication(Publication publicaton){
        publications.add(publicaton);
        publicaton.addStudy(this);
    }

    public void setPublications(Set<Publication> publications) {
        this.publications.clear();
        for(Publication publication: publications){
            addPublication(publication);
        }
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }
}
