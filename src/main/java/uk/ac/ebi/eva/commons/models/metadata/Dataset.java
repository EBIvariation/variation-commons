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

package uk.ac.ebi.eva.commons.models.metadata;

import java.util.*;

/**
 * Created by parce on 05/10/15.
 * 
 * @TODO Come back to this class for further design
 */
public class Dataset {
    // EVA PRO (ega_ega_dataset table)
    private String centre;
    private boolean protect;
    private int version;
    private String md5;
    private boolean editable;

    private Set<FileGenerator> fileGenerators;

    public Dataset(String centre, boolean protect, int version, String md5, Boolean editable) {
        this(centre, protect, version, md5, editable, new HashSet<FileGenerator>());
    }

    public Dataset(String centre, boolean protect, int version, String md5, Boolean editable,
                   Set<FileGenerator> fileGenerators) {
        this.centre = centre;
        this.protect = protect;
        this.version = version;
        this.md5 = md5;
        this.editable = editable;
        this.fileGenerators = fileGenerators != null ? fileGenerators : new HashSet<FileGenerator>();
    }

    public String getCentre() {
        return centre;
    }

    public void setCentre(String centre) {
        this.centre = centre;
    }

    public boolean isProtect() {
        return protect;
    }

    public void setProtect(boolean protect) {
        this.protect = protect;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Set<FileGenerator> getFileGenerators() {
        return fileGenerators;
    }

    public void setFileGenerators(Set<FileGenerator> fileGenerators) {
        this.fileGenerators.clear();
        for (FileGenerator f : fileGenerators) {
            addFileGenerator(f);
        }
    }

    public void addFileGenerator(FileGenerator generator) {
        fileGenerators.add(generator);
        generator.setDataset(this);
    }

    // TODO add removeFileGenerator method
}
