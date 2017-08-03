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
package uk.ac.ebi.eva.commons.jpa.models.metadata;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by tom on 12/10/15.
 */
@Entity
@Table(indexes = {@Index(name = "publication_unique", columnList = "title,journal,volume", unique = true)})
public class Publication extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 8055335219199952073L;

    private String title;
    private String journal; // should journal be separate class?
    private String volume;
    private int startPage;
    private int endPage;
    private String dbId;
    private String database;
    private String doi;
    private String isbn;
    private Calendar publicationDate;
    @ElementCollection
    private List<String> authors;
    @Transient
    private Set<Study> studies;


    public Publication(String title, String journal, String volume) {
        this(title, journal, volume, null, null, null);
    }

    public Publication(String title, String journal, String volume, List<String> authors, String database, String dbId) {
        setTitle(title);
        setJournal(journal);
        setVolume(volume);
        setAuthors(authors);
        this.dbId = dbId;
        this.database = database;
        this.studies = new HashSet<>();
    }

    public String getDbId() {
        return dbId;
    }

    public void setDbId(String dbId) {
        this.dbId = dbId;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        Objects.requireNonNull(title, "Title not specified");
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public final void setJournal(String journal) {
        Objects.requireNonNull(journal, "Journal not specified");
        this.journal = journal;
    }

    public String getVolume() {
        return volume;
    }

    public final void setVolume(String volume) {
        Objects.requireNonNull(volume, "Journal volume not specified");
        this.volume = volume;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Calendar getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Calendar publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getFirstAuthor() {
        return getAuthors().get(0);
    }

    public List<String> getAuthors() {
        if (authors != null) {
            return Collections.unmodifiableList(authors);
        }
        return null;
    }

    public void addAuthor(String author) {
        authors.add(author);
    }

    final void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public Set<Study> getStudies() {
        return Collections.unmodifiableSet(studies);
    }

    void removeStudy(Study study) {
        studies.remove(study);
    }

    void addStudy(Study study) {
        studies.add(study);
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        } else if (!(e instanceof Publication)) {
            return false;
        } else {
            return (
                    Objects.equals(((Publication) e).getTitle(), title) &&
                            Objects.equals(((Publication) e).getJournal(), journal) &&
                            Objects.equals(((Publication) e).getVolume(), volume)
            );
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + title.hashCode();
        result = 31 * result + journal.hashCode();
        result = 31 * result + volume.hashCode();
        return result;
    }
}
