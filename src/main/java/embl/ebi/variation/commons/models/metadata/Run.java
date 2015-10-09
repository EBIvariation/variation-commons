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

    private String alias;
    private Experiment experiment;

    public Run(Study study, String alias) {
        super(study);
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
            return ((Run)e).getAlias() == alias;
        }
    }

    @Override
    public int hashCode() {
        return alias.hashCode();
    }
}
