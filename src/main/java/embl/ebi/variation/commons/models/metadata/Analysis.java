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

import java.util.Date;

/**
 * Created by parce on 02/10/15.
 */
public class Analysis extends FileGenerator {
    private String accession;
    private String alias;
    private String title;
    private String centerName;
    private String description;
    private Date date;
    private String vcfReference;
    private String vcfReferenceAccession;
    private Boolean hiddenInEva;

    public Analysis(String accession, String alias, String title, String centerName, String description, Date date,
                    String vcfReference, String vcfReferenceAccession, Boolean hiddenInEva)
    {
        this.accession = accession;
        this.alias = alias;
        this.title = title;
        this.centerName = centerName;
        this.description = description;
        this.date = date;
        this.vcfReference = vcfReference;
        this.vcfReferenceAccession = vcfReferenceAccession;
        this.hiddenInEva = hiddenInEva;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getVcfReference() {
        return vcfReference;
    }

    public void setVcfReference(String vcfReference) {
        this.vcfReference = vcfReference;
    }

    public String getVcfReferenceAccession() {
        return vcfReferenceAccession;
    }

    public void setVcfReferenceAccession(String vcfReferenceAccession) {
        this.vcfReferenceAccession = vcfReferenceAccession;
    }

    public Boolean getHiddenInEva() {
        return hiddenInEva;
    }

    public void setHiddenInEva(Boolean hiddenInEva) {
        this.hiddenInEva = hiddenInEva;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        }else if (!(e instanceof Analysis)) {
            return false;
        }else {
            return ((Analysis)e).getAccession() == accession;
        }
    }

    @Override
    public int hashCode() {
        return accession.hashCode();
    }
}
