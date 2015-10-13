package embl.ebi.variation.commons.models.metadata;

import java.util.*;

/**
 * Created by tom on 12/10/15.
 */
public class Publication {

    private int pmid;
    private String database;
    private String title;
    private String journal; // should journal be separate class?
    private Calendar publicationDate;

    private List<String> contributors;
    private Set<Study> studies = new HashSet<>();


    public Publication(int pmid, String database, String title, String journal) {
        this.pmid = pmid;
        this.database = database;
        this.title = title;
        this.journal = journal;
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

    public Calendar getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Calendar publicationDate) {
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
            return (Objects.equals(((Publication) e).getPmid(), pmid));
        }
    }

    @Override
    public int hashCode() {
        int result = 17;
        return (31 * result + pmid);
    }
}
