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
import java.util.Set;

/**
 * Created by parce on 05/10/15.
 */
public class Experiment {
    private String alias;
    private String instrumentPlatform;
    private String instrumentModel;
    private String libraryModel;
    private String libraryLayout;
    private String libraryName;
    private String libraryStrategy;
    private String librarySource;
    private String librarySelection;
    private String pairedNominalLength;
    private String pairedNominalSdev;

    private Set<Run> runs;

    public Experiment(String alias, String instrumentPlatform, String instrumentModel, String libraryModel,
                      String libraryLayout, String libraryName, String libraryStrategy, String librarySource,
                      String librarySelection, String pairedNominalLength, String pairedNominalSdev) {
        this(alias, instrumentPlatform, instrumentModel, libraryModel, libraryLayout, libraryName, libraryStrategy,
                librarySource, librarySelection, pairedNominalLength, pairedNominalSdev, new HashSet<Run>());
    }

    public Experiment(String alias, String instrumentPlatform, String instrumentModel, String libraryModel,
                      String libraryLayout, String libraryName, String libraryStrategy, String librarySource,
                      String librarySelection, String pairedNominalLength, String pairedNominalSdev, Set<Run> runs) {
        this.alias = alias;
        this.instrumentPlatform = instrumentPlatform;
        this.instrumentModel = instrumentModel;
        this.libraryModel = libraryModel;
        this.libraryLayout = libraryLayout;
        this.libraryName = libraryName;
        this.libraryStrategy = libraryStrategy;
        this.librarySource = librarySource;
        this.librarySelection = librarySelection;
        this.pairedNominalLength = pairedNominalLength;
        this.pairedNominalSdev = pairedNominalSdev;
        this.runs = runs;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInstrumentPlatform() {
        return instrumentPlatform;
    }

    public void setInstrumentPlatform(String instrumentPlatform) {
        this.instrumentPlatform = instrumentPlatform;
    }

    public String getInstrumentModel() {
        return instrumentModel;
    }

    public void setInstrumentModel(String instrumentModel) {
        this.instrumentModel = instrumentModel;
    }

    public String getLibraryModel() {
        return libraryModel;
    }

    public void setLibraryModel(String libraryModel) {
        this.libraryModel = libraryModel;
    }

    public String getLibraryLayout() {
        return libraryLayout;
    }

    public void setLibraryLayout(String libraryLayout) {
        this.libraryLayout = libraryLayout;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getLibraryStrategy() {
        return libraryStrategy;
    }

    public void setLibraryStrategy(String libraryStrategy) {
        this.libraryStrategy = libraryStrategy;
    }

    public String getLibrarySource() {
        return librarySource;
    }

    public void setLibrarySource(String librarySource) {
        this.librarySource = librarySource;
    }

    public String getLibrarySelection() {
        return librarySelection;
    }

    public void setLibrarySelection(String librarySelection) {
        this.librarySelection = librarySelection;
    }

    public String getPairedNominalLength() {
        return pairedNominalLength;
    }

    public void setPairedNominalLength(String pairedNominalLength) {
        this.pairedNominalLength = pairedNominalLength;
    }

    public String getPairedNominalSdev() {
        return pairedNominalSdev;
    }

    public void setPairedNominalSdev(String pairedNominalSdev) {
        this.pairedNominalSdev = pairedNominalSdev;
    }

    public Set<Run> getRuns() {
        return runs;
    }

    public void setRuns(Set<Run> runs) {
        this.runs.clear();
        for (Run run : runs) {
            addRun(run);
        }
    }

    public void addRun(Run run) {
        runs.add(run);
        run.setExperiment(this);
    }

    // TODO: add removeRun method
}
