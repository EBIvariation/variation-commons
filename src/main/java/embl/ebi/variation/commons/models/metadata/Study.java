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

import org.apache.commons.validator.routines.UrlValidator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.Collection;
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
    private String centre; // could this be used to connect an Organisation class (i.e. private Organisation centre)? Organisation class could be used for broker attribute too
    private StudyEnums.Material material; // controlled vocabulary, use enum?
    private StudyEnums.Scope scope; // controlled vocabulary, use enum?
    private String type; // e.g. umbrella, pop genomics BUT separate column for study_type (aggregate, control set, case control)

    private Set<FileGenerator> fileGenerators = new HashSet<FileGenerator>();

    private String alias;
    private String description;
    private Set<String> urls = new HashSet<String>();
//    private Taxon taxon; // When there is a taxon class
    private Set<String> publications = new HashSet<String>(); //    private Set<Publication> publications; if there will be separate publication class
    private Set<String> links = new HashSet<String>(); // should this be given as a string or as relation to other "Link" (i.e. Set<Link>) class? Submission template gives as: "Link(s)	Links in the form DB:ID:LABEL (e.g. DGVA:esv1, DBSNP:rs149486)"
    private Set<String> collaborators = new HashSet<String>(); // link to collaborator class? collaborator could be part of multiple studies  private Set<Collaborator>
    private LocalDateTime holdDate;
    private String strain; // part of submission template, but should they be strings or separate "Strain" and "Breed" classes?
    private String breed;
    private String broker; // if there is a separate broker/organisation class

//    private Set<Study> associatedStudies = new HashSet<Study>(); // in submission template is described as: "Associated Project(s)	Accession OR Alias of all project(s) assoicated to this project (NCBI, ENA, EVA all share the same project accession space so no database distinction is necessary) (e.g. PRJEB4019)" but will a hierarchy of studies be needed instead?
//    private Study parentStudy;
//    private Set<Study> childStudies = new HashSet<Study>(); // ... or use this hierarchical relationship?


    public Study(String studyAccession, String centre, StudyEnums.Material material, StudyEnums.Scope scope, String type) {
        this.setStudyAccession(studyAccession);
        this.centre = centre;
        this.material = material;
        this.scope = scope;
        this.type = type;
    }


//    public Long getStudyId() {
//        return studyId;
//    }
//
//    public void setStudyId(Long studyId) {
//        this.studyId = studyId;
//    }

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

    public Set<String> getUrls() {
        return Collections.unmodifiableSet(urls);
    }

    public void addUrl(String url){
//        UrlValidator urlValidator = new UrlValidator();
//        if(urlValidator.isValid(url)){
            urls.add(url);
//        }else{
            // TODO deal with invalid url
//        }
    }

    public void removeUrl(String url){
        urls.remove(url);
    }

    public void setUrls(Set<String> urls) {
        this.urls.clear();
        if(urls != null){
            for(String url: urls) {
                addUrl(url);
            }
        }
    }

    public Set<FileGenerator> getFileGenerators() {
        return Collections.unmodifiableSet(fileGenerators);
    }

    public void removeFileGenerator(FileGenerator fileGenerator){
        fileGenerators.remove(fileGenerator);
    }

    public void addFileGenerator(FileGenerator fileGenerator){
        fileGenerators.add(fileGenerator);
        fileGenerator.addStudy(this);
    }

    public void setFileGenerators(Set<FileGenerator> fileGenerators) {
        this.fileGenerators.clear();
        for(FileGenerator fileGenerator: fileGenerators){
            addFileGenerator(fileGenerator);
        }
    }

    public Set<String> getPublications() {
        return Collections.unmodifiableSet(publications);
    }

    public void removePublication(String publication){
        publications.remove(publication);
    }

    public void addPublication(String publicaton){
        publications.add(publicaton);
    }

    public void setPublications(Set<String> publications) {
        this.publications.clear();
        for(String publication: publications){
            addPublication(publication);
        }
    }

    public Set<String> getLinks() {
        return Collections.unmodifiableSet(links);
    }

    public void removeLink(String link){
        links.remove(link);
    }

    public void addLink(String link){
        links.add(link);
    }

    public void setLinks(Set<String> links) {
        this.links.clear();
        for(String link: links){
            addLink(link);
        }
    }

    public Set<String> getCollaborators() {
        return Collections.unmodifiableSet(collaborators);
    }

    public void removeCollaborator(String collaborator){
        collaborators.remove(collaborator);
    }

    public void addCollaborator(String collaborator){
        collaborators.add(collaborator);
    }

    public void setCollaborators(Set<String> collaborators) {
        this.collaborators.clear();
        for(String collaborator: collaborators){
            addCollaborator(collaborator);
        }
    }

    public String getStrain() {
        return strain;
    }

    public void setStrain(String strain) {
        this.strain = strain;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
    }
}
