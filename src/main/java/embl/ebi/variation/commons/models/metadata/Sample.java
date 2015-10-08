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

import java.util.HashSet;
import java.util.Set;

/**
 * Created by parce on 06/10/15.
 */
public class Sample {
    private long id;
    private String alias;
    private String accession;
    private String samplesetAccession;
    private String analysisAlias;
    private String description;
    private String gender;
    private String links;
    private String attributes; // TODO: use a map?
    private String phenotypes; // use a map?
    private String diseaseSite;
    private String strain;
    private String breed;
    private Set<File> files;

    public Sample(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void addFile(File file) {
        internalAddFile(file);
        file.internalAddSample(this);
    }

    void internalAddFile(File file) {
        if (getFiles() == null) {
            setFiles(new HashSet<File>());
        }
        getFiles().add(file);
    }

    public Set<File> getFiles() {
        return files;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }else if (!(object instanceof Sample)) {
            return false;
        }else {
            return ((Sample)object).getId() == getId();
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 22;
        int c = (int)(getId() ^(getId() >>>32));
        hashCode = 31 * hashCode + c;
        return hashCode;
    }

    public void setId(long id) {
        this.id = id;
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

    public void setFiles(Set<File> files) {
        this.files = files;
    }
}
