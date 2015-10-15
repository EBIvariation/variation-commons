package embl.ebi.variation.commons.models.metadata;

import java.util.*;

import com.gs.collections.api.bag.Bag;
import com.gs.collections.impl.list.mutable.FastList;

/**
 * Created by tom on 12/10/15.
 */
public class Publication {

    private String dbId;
    private String database;
    private String title;
    private String journal; // should journal be separate class?
    private String volume;
    private int startPage;
    private int endPage;
    private String doi;
    private String isbn;
    private Calendar publicationDate;
    private List<String> authors = new ArrayList<>();
    private Set<Study> studies = new HashSet<>();


    public Publication(String dbId, String database, String title, String journal, String volume, List<String> authors) {
        this.dbId = dbId;
        this.database = database;
        this.title = title;
        this.journal = journal;
        this.volume = volume;
        setAuthors(authors);
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
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
        return Collections.unmodifiableList(authors);
    }

    public void addAuthor(String author){
        authors.add(author);
    }

    public void setAuthors(List<String> authors) {
        this.authors.clear();
        for(String author: authors){
            addAuthor(author);
        }
    }

    public Set<Study> getStudies() {
        return Collections.unmodifiableSet(studies);
    }

    void removeStudy(Study study){
        studies.remove(study);
    }

    void addStudy(Study study){
        studies.add(study);
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        }else if (!(e instanceof Publication)) {
            return false;
        }else{
            Bag<String> eAuthorsBag = FastList.newList(((Publication) e).getAuthors()).toBag();
            Bag<String> thisAuthorsBag = FastList.newList(authors).toBag();

            return (
                    Objects.equals(((Publication) e).getTitle(), title) &&
                            Objects.equals(((Publication) e).getJournal(), journal) &&
                            Objects.equals(((Publication) e).getVolume(), volume) &&
                            Objects.equals(eAuthorsBag, thisAuthorsBag)
            );
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + title.hashCode();
        result = 31 * result + journal.hashCode();
        result = 31 * result + volume.hashCode();
        result = 31 * result + authors.hashCode();
        return result;
    }
}
