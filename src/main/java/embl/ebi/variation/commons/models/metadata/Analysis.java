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
    private String title;
    private String alias;
    private String description;
    // private String projectTitle; TODO: relationship to Project?
    // TODO: experiment type in experiment? there is no relation between analysis and experiment
    // private String experimentType; Choose 1 of the following "whole genome sequencing", "Exome sequencing"', "Genotyping by array", "Curation"
    private String reference; // Reference the analysis was performed against. GRC reference name or ENA accession accepted
    private String platform;
    private String software;
    private boolean imputation;
    private String centre;
    private Date date;
    private String links; // TODO: use a set? Create an externalResourceClass to represent BD:ID:LABEL?
    private String runs; // TODO: use a set? relation to run?

    public Analysis(Study study, String title, String alias, String description, String reference, String platform, String software,
                    boolean imputation, String centre, Date date, String links, String runs) {
        super(study);
        this.title = title;
        this.alias = alias;
        this.description = description;
        this.reference = reference;
        this.platform = platform;
        this.software = software;
        this.imputation = imputation;
        this.centre = centre;
        this.date = date;
        this.links = links;
        this.runs = runs;
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

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        }else if (!(e instanceof Analysis)) {
            return false;
        }else {
            return ((Analysis)e).getTitle() == title;
        }
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}
