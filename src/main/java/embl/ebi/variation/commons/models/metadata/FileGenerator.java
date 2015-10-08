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

    protected Set<File> files;
    protected Dataset dataset;
    protected Set<Study> studies;

    //TODO does FileGenerator need to have study in the constructor, otherwise it's possible to have a floating filegenerator, not attached to a study. e.g.:
//    protected FileGenerator(Study study){
//        this(study, null);
//    }
//
//    protected FileGenerator(Study study, Dataset dataset){
//        this(study, dataset, new HashSet<File>());
//    }
//
//    protected FileGenerator(Study study, Dataset dataset, Set<File> files) {
//        this.dataset = dataset;
//        setFiles(files);
//        studies = new HashSet<Study>();
//        addStudy(study);
//    }

    protected FileGenerator(Dataset dataset){
        this(dataset, new HashSet<File>());
    }

    protected FileGenerator(Dataset dataset, Set<File> files) {
        this.dataset = dataset;
        setFiles(files);
    }

    void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Set<File> getFiles() {
        return Collections.unmodifiableSet(files);
    }

    public void addFile(File file){
        this.files.add(file);
    }

    public void setFiles(Set<File> files) {
        this.files.clear();
        for (File f : files) {
            addFile(f);
        }
    }

    public Dataset getDataset() {
        return dataset;
    }

    public Set<Study> getStudies() {
        return Collections.unmodifiableSet(studies);
    }

    void addStudy(Study study){
        studies.add(study);
    }

    @Override
    public abstract boolean equals(Object e);

    @Override
    public abstract int hashCode();
}
