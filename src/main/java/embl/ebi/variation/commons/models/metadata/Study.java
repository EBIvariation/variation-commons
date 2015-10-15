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

import java.io.Serializable;
import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Study implements Serializable {

    private String studyAccession; // Bioproject ID?
    private String title;
    private Organisation centre; //TODO Make a set and connect an Organisation class (i.e. private Organisation centre)? Organisation class could be used for broker attribute too
    private Material material; // controlled vocabulary, use enum?
    private Scope scope; // controlled vocabulary, use enum?
    private String type; // e.g. umbrella, pop genomics BUT separate column for study_type (aggregate, control set, case control)

    private Set<FileGenerator> fileGenerators = new HashSet<FileGenerator>();

    private String alias;
    private String description;
    private Set<URI> uris = new HashSet<URI>();
//    private Taxon taxon; // When there is a taxon class
    private Set<Publication> publications = new HashSet<>(); // TODO   private Set<Publication> publications; if there will be separate publication class
    private Organisation broker; // if there is a separate broker/centre class

//    private Set<Study> associatedStudies = new HashSet<Study>(); // in submission template is described as: "Associated Project(s)	Accession OR Alias of all project(s) assoicated to this project (NCBI, ENA, EVA all share the same project accession space so no database distinction is necessary) (e.g. PRJEB4019)" but will a hierarchy of studies be needed instead?
    private Study parentStudy;
    private Set<Study> childStudies = new HashSet<>();


    public Study(String alias, String title, String description, Material material, Scope scope) {
        this.alias = alias;
        this.title = title;
        this.description = description;
        this.material = material;
        this.scope = scope;
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

    public Organisation getCentre() {
        return centre;
    }

    public void setCentre(Organisation centre) {
        this.centre = centre;
        centre.addStudy(this); // should the study be adding itself to the centre, or the other way around? which is responsible?
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
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
        fileGenerator.unsetStudy();
        fileGenerators.remove(fileGenerator);
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

    public void removePublication(Publication publication){
        publication.removeStudy(this);
        publications.remove(publication);
    }

    public void addPublication(Publication publication){
        publications.add(publication);
        publication.addStudy(this);
    }

    public void setPublications(Set<Publication> publications) {
        this.publications.clear();
        for(Publication publication: publications){
            addPublication(publication);
        }
    }

    public Organisation getBroker() {
        return broker;
    }

    public void setBroker(Organisation broker) {
        this.broker = broker;
    }

    public Study getParentStudy() {
        return parentStudy;
    }

    void setParentStudy(Study parentStudy) {
        this.parentStudy = parentStudy;
    }

    void unsetParentStudy(){
        this.parentStudy = null;
    }

    public Set<Study> getChildStudies() {
        return Collections.unmodifiableSet(childStudies);
    }

    public void addChildStudy(Study childStudy){
        childStudies.add(childStudy);
        childStudy.setParentStudy(this);
    }

    public void removeChildStudy(Study childStudy){
        childStudies.remove(childStudy);
        childStudy.unsetParentStudy();
    }

    public void setChildStudies(Set<Study> childStudies) {
        this.childStudies.clear();
        for(Study childStudy: childStudies){
            addChildStudy(childStudy);
            childStudy.setParentStudy(this);
        }
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        }else if (!(e instanceof Study)) {
            return false;
//        }else if (studyAccession != null){
//            return Objects.equals(((Study) e).getStudyAccession(), studyAccession); // remove this, per comment "Both studies should have the accession defined. Although ENA doesn't have a single criteria for study accessioning any more, so we could just stick to the other attributes that we know will always exist."?
        }else{
            return (
                    Objects.equals(((Study) e).getTitle(), title) &&
                            Objects.equals(((Study) e).getMaterial(), material) &&
                            Objects.equals(((Study) e).getScope(), scope) &&
                            Objects.equals(((Study) e).getDescription(), description) &&
                            Objects.equals(((Study) e).getAlias(), alias)
            );
        }
    }

    @Override
    public int hashCode() {
        if (studyAccession != null){
            return studyAccession.hashCode();
        }else{
            int result = 17;
            result = 31 * result + title.hashCode();
            result = 31 * result + material.hashCode();
            result = 31 * result + scope.hashCode();
            result = 31 * result + description.hashCode();
            result = 31 * result + alias.hashCode();

            return result;
        }
    }

    public enum Scope {
        SINGLE_ISOLATE ("single isolate"),
        MULTI_ISOLATE ("multi-isolate"),
        SINGLE_CELL ("single cell"),
        COMMUNITY ("community"),
        UNKNOWN ("unknown"),
        OTHER ("other");

        private final String name;

        private Scope(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }


    public enum Material {
        DNA ("DNA"),
        EXONIC_RNA ("exonic RNA"),
        TRANSCRIBED_RNA ("transcribed RNA"),
        UNKNOWN ("unknown"),
        OTHER ("other");

        private final String name;

        private Material(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }
}
