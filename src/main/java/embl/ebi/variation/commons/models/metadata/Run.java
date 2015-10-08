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
import java.util.HashSet;

/**
 * Created by parce on 05/10/15.
 */
public class Run extends FileGenerator {

    private String stableId;
    private String alias;
    private String eraVersion;
    private String released;
    private Date releasedTimestamp;
    private Date loadTimestamp;
    private Experiment experiment;

    public Run(String stableId, String alias, String eraVersion, String released, Date releasedTimestamp, Date loadTimestamp, Experiment experiment, String dataset) {
        super(new Dataset(), new HashSet<File>());
        this.stableId = stableId;
        this.alias = alias;
        this.eraVersion = eraVersion;
        this.released = released;
        this.releasedTimestamp = releasedTimestamp;
        this.loadTimestamp = loadTimestamp;
        this.experiment = experiment;
    }

    public String getStableId() {
        return stableId;
    }

    public void setStableId(String stableId) {
        this.stableId = stableId;
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

    void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        }else if (!(e instanceof Run)) {
            return false;
        }else {
            return ((Run)e).getStableId() == stableId;
        }
    }

    @Override
    public int hashCode() {
        return stableId.hashCode();
    }
}
