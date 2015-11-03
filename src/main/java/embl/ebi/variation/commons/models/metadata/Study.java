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

import org.springframework.data.jpa.domain.AbstractPersistable;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(indexes = {@Index(name = "study_unique", columnList = "title,material,scope,description,alias", unique = true)})
public class Study extends AbstractPersistable<Long>{

    private static final long serialVersionUid = 3947143813564096660L;

    private String title;
    private String alias;
    private String description;
    private Material material;
    private Scope scope;

    private String type; // e.g. umbrella, pop genomics BUT separate column for study_type (aggregate, control set, case control)

    private String studyAccession; // Bioproject ID?
    @Transient
    private Organisation centre;
    @Transient private Organisation broker;

    @Transient private Set<FileGenerator> fileGenerators;

//    @ManyToMany(targetEntity=URI.class)
    @Transient private Set<URI> uris;
//    @ManyToMany(targetEntity=Publication.class)
    @Transient private Set<Publication> publications;

//    @Transient
//    TODO giving this a manytoone relationship breaks StudyDatabaseTest.testUpdateDuplicate(). Makes it throw org.springframework.dao.DataIntegrityViolationException instead
    @ManyToOne (cascade = CascadeType.PERSIST)
    private Study parentStudy;

    @OneToMany(targetEntity=Study.class, mappedBy = "parentStudy", cascade = CascadeType.PERSIST)
    private Set<Study> childStudies;

    public Study(){
        this.title = null;
        this.alias = null;
        this.description = null;
        this.material = Material.OTHER;
        this.scope = Scope.OTHER;
    }

    public Study(Long id){
        this.setId(id);
    }

    public Study(String title, String alias, String description, Material material, Scope scope) {
        setTitle(title);
        setAlias(alias);
        setDescription(description);
        setMaterial(material);
        setScope(scope);
        fileGenerators = new HashSet<>();
        uris = new HashSet<>();
        publications = new HashSet<>();
        childStudies = new HashSet<>();
    }

    public String getStudyAccession() {
        return studyAccession;
    }

    public void setStudyAccession(String studyAccession) {
        // starts with "PRJEA or PRJEB PRJNA\d+"
        if (studyAccession.matches("PRJ(E(A|B)|NA)\\d+")) {
            this.studyAccession = studyAccession;
        } else {
            throw new IllegalArgumentException(
                    "Study accession must begin with a prefix from the following: (PRJEA, PRJEB, PRJNA), "
                    + "followed by multiple numerical digits.");
            // TODO openpojo tester throws exception with this
        }
    }

    public String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        Objects.requireNonNull(title, "Title not specified");
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public final void setAlias(String alias) {
        Objects.requireNonNull(alias, "Alias not specified");
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        Objects.requireNonNull(description, "Description not specified");
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

    public final void setScope(Scope scope) {
        Objects.requireNonNull(scope, "Scope not specified");
        this.scope = scope;
    }

    public Material getMaterial() {
        return material;
    }

    public final void setMaterial(Material material) {
        Objects.requireNonNull(material, "Material not specified");
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

    public void addUri(URI uri) {
        uris.add(uri);
    }

    public void removeUrl(URI url) {
        uris.remove(url);
    }

    public void setUris(Set<URI> uris) {
        this.uris.clear();
        if (uris != null) {
            for (URI uri : uris) {
                addUri(uri);
            }
        }
    }

    public Set<FileGenerator> getFileGenerators() {
        return Collections.unmodifiableSet(fileGenerators);
    }

    public void removeFileGenerator(FileGenerator fileGenerator) {
        fileGenerator.unsetStudy();
        fileGenerators.remove(fileGenerator);
    }

    public void addFileGenerator(FileGenerator fileGenerator) {
        fileGenerators.add(fileGenerator);
        fileGenerator.setStudy(this);
    }

    public void setFileGenerators(Set<FileGenerator> fileGenerators) {
        this.fileGenerators.clear();
        for (FileGenerator fileGenerator : fileGenerators) {
            addFileGenerator(fileGenerator);
        }
    }

    public Set<Publication> getPublications() {
        return Collections.unmodifiableSet(publications);
    }

    public void removePublication(Publication publication) {
        publication.removeStudy(this);
        publications.remove(publication);
    }

    public void addPublication(Publication publication) {
        publications.add(publication);
        publication.addStudy(this);
    }

    public void setPublications(Set<Publication> publications) {
        this.publications.clear();
        for (Publication publication : publications) {
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
        if(parentStudy.equals(this)){
            throw new RuntimeException("A study can't be its own parent study.");
        }
        this.parentStudy = parentStudy;
    }

    void unsetParentStudy() {
        this.parentStudy = null;
    }

    public Set<Study> getChildStudies() {
        return Collections.unmodifiableSet(childStudies);
    }

    public void addChildStudy(Study childStudy) {
        if(childStudy.equals(this)){
            throw new RuntimeException("A study can't be its own child study.");
        }
        childStudies.add(childStudy);
        childStudy.setParentStudy(this);
    }

    public void removeChildStudy(Study childStudy) {
        childStudies.remove(childStudy);
        childStudy.unsetParentStudy();
    }

    public void setChildStudies(Set<Study> childStudies) {
        this.childStudies.clear();
        for (Study childStudy : childStudies) {
            addChildStudy(childStudy);
            childStudy.setParentStudy(this);
        }
    }

    @Override
    public String toString(){
        return "Study{" + "title=" + title + ", material=" + material + ", scope=" + scope + ", description=" + description + ", alias=" + alias + '}';
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        } else if (!(e instanceof Study)) {
            return false;
        } else {
            return (Objects.equals(((Study) e).getTitle(), title)
                    && Objects.equals(((Study) e).getMaterial(), material)
                    && Objects.equals(((Study) e).getScope(), scope)
                    && Objects.equals(((Study) e).getDescription(), description)
                    && Objects.equals(((Study) e).getAlias(), alias));
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = (title != null) ? 31 * result + title.hashCode() : result;
        result = (material != null) ? 31 * result + material.hashCode() : result;
        result = (scope != null) ? 31 * result + scope.hashCode() : result;
        result = (description != null) ? 31 * result + description.hashCode() : result;
        result = (alias != null) ? 31 * result + alias.hashCode() : result;
        return result;
    }

    public enum Scope {

        SINGLE_ISOLATE("single isolate"),
        MULTI_ISOLATE("multi-isolate"),
        SINGLE_CELL("single cell"),
        COMMUNITY("community"),
        UNKNOWN("unknown"),
        OTHER("other");

        private final String name;

        private Scope(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public enum Material {

        DNA("DNA"),
        EXONIC_RNA("exonic RNA"),
        TRANSCRIBED_RNA("transcribed RNA"),
        UNKNOWN("unknown"),
        OTHER("other");

        private final String name;

        private Material(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
