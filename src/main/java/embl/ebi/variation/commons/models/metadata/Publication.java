package embl.ebi.variation.commons.models.metadata;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by tom on 12/10/15.
 */
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long publicationId;
    private int pmid;
    private String database;
    private String title;
    private String journal; // should journal be separate class?
    private LocalDateTime publicationDate;

    private List<String> contributors;
    private Set<Study> studies;


    public Publication(int pmid, String database, String title, String journal, LocalDateTime publicationDate, List<String> contributors) {
        this.pmid = pmid;
        this.database = database;
        this.title = title;
        this.journal = journal;
        this.publicationDate = publicationDate;
        this.contributors = contributors;
    }

    public int getPmid() {
        return pmid;
    }

    public void setPmid(int pmid) {
        this.pmid = pmid;
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

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public List<String> getContributors() {
        return Collections.unmodifiableList(contributors);
    }

    public void addContributor(String contributor){
        contributors.add(contributor);
    }

    public void setContributors(List<String> contributors) {
        this.contributors.clear();
        for(String contributor: contributors){
            addContributor(contributor);
        }
    }

    public Set<Study> getStudies() {
        return Collections.unmodifiableSet(studies);
    }

    void addStudy(Study study){
        studies.add(study);
    }
}
