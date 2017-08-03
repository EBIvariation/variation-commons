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

import java.util.HashSet;
import java.util.Set;

/**
 * Created by parce on 06/10/15.
 *
 * @TODO review how to connect with biosamples
 */
public class Sample {
    private String alias;
    private String accession;
    private String samplesetAccession;
    // Alias of the analysis performed on this sample. Comma separated list allowable for multiple analyses
    private String analysisAlias; // TODO: Set? Relation to analysis?
    private String description;
    private String gender;
    // Links to resources related to this sample/sampleset (publication(s), dataset(s), online database(s)). Format DB:ID:LABEL (label optional, a text label to dispaly for the link), or URL:LABEL (URL must start with "ftp:" or "http:" or "file:". Comma separated list allowed for multiple links
    private String links; // TODO: Set? Class to represent the links?
    private String attributes; // TODO: use a map? attribute class?
    private String phenotypes; // TODO: use a map? phenotype class?
    private String diseaseSite;
    private String strain;
    private String breed;
    private Set<File> files;

    public Sample(String alias, String accession, String samplesetAccession, String analysisAlias, String description,
                  String gender, String links, String attributes, String phenotypes, String diseaseSite, String strain,
                  String breed) {
        this(alias, accession, samplesetAccession, analysisAlias, description, gender, links, attributes, phenotypes, diseaseSite, strain, breed, new HashSet<File>());
    }

    public Sample(String alias, String accession, String samplesetAccession, String analysisAlias, String description,
                  String gender, String links, String attributes, String phenotypes, String diseaseSite, String strain,
                  String breed, Set<File> files) {
        this.alias = alias;
        this.accession = accession;
        this.samplesetAccession = samplesetAccession;
        this.analysisAlias = analysisAlias;
        this.description = description;
        this.gender = gender;
        this.links = links;
        this.attributes = attributes;
        this.phenotypes = phenotypes;
        this.diseaseSite = diseaseSite;
        this.strain = strain;
        this.breed = breed;
        this.files = files;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getSamplesetAccession() {
        return samplesetAccession;
    }

    public void setSamplesetAccession(String samplesetAccession) {
        this.samplesetAccession = samplesetAccession;
    }

    public String getAnalysisAlias() {
        return analysisAlias;
    }

    public void setAnalysisAlias(String analysisAlias) {
        this.analysisAlias = analysisAlias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLinks() {
        return links;
    }

    public void setLinks(String links) {
        this.links = links;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getPhenotypes() {
        return phenotypes;
    }

    public void setPhenotypes(String phenotypes) {
        this.phenotypes = phenotypes;
    }

    public String getDiseaseSite() {
        return diseaseSite;
    }

    public void setDiseaseSite(String diseaseSite) {
        this.diseaseSite = diseaseSite;
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

    public Set<File> getFiles() {
        return files;
    }

    void setFiles(Set<File> files) {
        this.files.clear();
        for (File f : files) {
            addFile(f);
        }
    }

    void addFile(File file) {
        files.add(file);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (!(object instanceof Sample)) {
            return false;
        } else {
            return ((Sample) object).getAccession() == accession;
        }
    }

    @Override
    public int hashCode() {
        return accession.hashCode();
    }
}
