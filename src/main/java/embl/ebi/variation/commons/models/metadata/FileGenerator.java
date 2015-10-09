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
 * Created by parce on 02/10/15.
 */
public abstract class FileGenerator {

    protected Set<File> files;
    protected Dataset dataset;

    protected FileGenerator(){
        this(null, new HashSet<File>());
    }

    protected FileGenerator(Dataset dataset, Set<File> files) {
        this.dataset = dataset;
        this.files = files;
    }

    public Dataset getDataset() {
        return dataset;
    }

    void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files.clear();
        for (File f : files) {
            addFile(f);
        }
    }

    public void addFile(File file) {
        files.add(file);
        file.addFileGenerator(this);
    }

    // TODO: add removeFile method

    @Override
    public abstract boolean equals(Object e);

    @Override
    public abstract int hashCode();
}
