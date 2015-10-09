package embl.ebi.variation.commons.models.metadata;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collections;
import java.util.Set;

/**
 * Created by tom on 09/10/15.
 */
public class Taxon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int taxonId;
    private String commonName;
    private String scientificName;
    private String taxonomyCode;
    private String evaName;

    private Taxon supertaxon;
    private Set<Taxon> subtaxa;


    public int getTaxonId() {
        return taxonId;
    }

    public void setTaxonId(int taxonId) {
        this.taxonId = taxonId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getTaxonomyCode() {
        return taxonomyCode;
    }

    public void setTaxonomyCode(String taxonomyCode) {
        this.taxonomyCode = taxonomyCode;
    }

    public String getEvaName() {
        return evaName;
    }

    public void setEvaName(String evaName) {
        this.evaName = evaName;
    }

    public Taxon getSupertaxon() {
        return supertaxon;
    }

    public void setSupertaxon(Taxon supertaxon) {
        this.supertaxon = supertaxon;
    }

    public Set<Taxon> getSubtaxa() {
        return Collections.unmodifiableSet(subtaxa);
    }

    public void removeSubtaxon(Taxon subtaxon){
        subtaxa.remove(subtaxon);
    }

    public void addSubtaxon(Taxon subtaxon){
        subtaxa.add(subtaxon);
    }

    public void setCollaborators(Set<Taxon> subtaxa) {
        this.subtaxa.clear();
        for(Taxon subtaxon: subtaxa){
            addSubtaxon(subtaxon);
        }
    }
}
