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
 * Created by parce on 05/10/15.
 */
public class Run extends FileGenerator {
    private String stableId;
    private String experimentStableId; // TODO: we have an exeperiment relation, maybe we should delete this field
    private String alias;
    private String eraVersion;
    private String released;
    private Date releasedTimestamp;
    private Date loadTimestamp;
    private Experiment experiment;

    public Run(long id) {
        super(id);
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
        experiment.internalAddRun(this);
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        }else if (!(e instanceof Run)) {
            return false;
        }else {
            return ((Run)e).getId() == id;
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 23;
        int c = (int)(getId() ^(getId() >>>32));
        hashCode = 31 * hashCode + c;
        return hashCode;
    }

    public String getStableId() {
        return stableId;
    }

    public void setStableId(String stableId) {
        this.stableId = stableId;
    }

    public String getExperimentStableId() {
        return experimentStableId;
    }

    public void setExperimentStableId(String experimentStableId) {
        this.experimentStableId = experimentStableId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEraVersion() {
        return eraVersion;
    }

    public void setEraVersion(String eraVersion) {
        this.eraVersion = eraVersion;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public Date getReleasedTimestamp() {
        return releasedTimestamp;
    }

    public void setReleasedTimestamp(Date releasedTimestamp) {
        this.releasedTimestamp = releasedTimestamp;
    }

    public Date getLoadTimestamp() {
        return loadTimestamp;
    }

    public void setLoadTimestamp(Date loadTimestamp) {
        this.loadTimestamp = loadTimestamp;
    }

    public Experiment getExperiment() {
        return experiment;
    }
}
