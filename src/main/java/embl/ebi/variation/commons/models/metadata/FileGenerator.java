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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by parce on 02/10/15.
 */
public abstract class FileGenerator {

    protected Set<File> files = new HashSet<File>();
    protected Dataset dataset;
    protected Study study;

    protected FileGenerator(Study study){
        this(study, null);
    }

    protected FileGenerator(Study study, Dataset dataset){
        this(study, dataset, null);
    }

    protected FileGenerator(Study study, Dataset dataset, Set<File> files) {
        this.study = study;
        if(this.dataset != null){
            this.dataset = dataset;
        }
        if(files != null){
            setFiles(files);
        }
    }

    void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Set<File> getFiles() {
        if(files != null){
            return Collections.unmodifiableSet(files);
        }else{
            return files;
        }
    }

    public void addFile(File file){
        this.files.add(file);
        file.addFileGenerator(this);
    }

    public void setFiles(Set<File> files) {
        this.files.clear();
        for(File file: files){
            addFile(file);
        }
    }

    public Dataset getDataset() {
        return dataset;
    }

    public Study getStudy() {
        return study;
    }

    void removeFromStudy(){
        study = null;
    }

    void setStudy (Study study){
        this.study = study;
    }

    // TODO: add removeFile method

    @Override
    public abstract boolean equals(Object e);

    @Override
    public abstract int hashCode();
}
