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
public class FileGenerator {
    protected long id;
    protected Set<File> files;
    protected Dataset dataset;

    protected FileGenerator(long id) {
        this.id = id;
    }

    public void addFile(File file) {
        internalAddFile(file);
        file.internalAddFileGenerator(this);
    }

    void internalAddFile(File file) {
        if (files == null) {
            files = new HashSet<File>();
        }
        files.add(file);
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
        dataset.internalAddFileGenerator(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    public Dataset getDataset() {
        return dataset;
    }
}
