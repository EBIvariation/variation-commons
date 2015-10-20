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
import java.util.Objects;

/**
 * Created by parce on 02/10/15.
 * 
 * @TODO Experiment type: Choose 1 of the following "whole genome sequencing", "Exome sequencing", "Genotyping by array", "Curation"
 * @TODO Link with Dataset class
 * @TODO Link with Organisation classs?
 */
public class Analysis extends FileGenerator {

    private String title;
    private String description;
    private String centre;
    private String platform;
    private String software;
    private boolean imputation;
    private Date date;

    public Analysis(String alias, String title, String description) {
        this(alias, title, description, null, null, null, false, null);
    }
    
    public Analysis(String alias, String title, String description, String centre,
            String platform, String software, boolean imputation, Date date) {
        super(alias);
        this.setTitle(title);
        this.setDescription(description);
        this.centre = centre;
        this.platform = platform;
        this.software = software;
        this.imputation = imputation;
        this.date = date;
    }
    
    public String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        Objects.requireNonNull(title, "Title not specified");
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public final void setDescription(String description) {
        Objects.requireNonNull(description, "Description not specified");
        this.description = description;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }

    public boolean isImputation() {
        return imputation;
    }

    public void setImputation(boolean imputation) {
        this.imputation = imputation;
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
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
        } else if (!(e instanceof Analysis)) {
            return false;
        } else {
            return ((Analysis) e).getTitle().equals(title);
        }
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

}
