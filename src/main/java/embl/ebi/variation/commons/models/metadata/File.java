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
public class File {
    private long id;
    private String name;
    private String type;
    private String md5;
    private Set<FileGenerator> fileGenerators;
    private Set<Sample> samples;

    public File(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void addSample(Sample sample) {
        internalAddSample(sample);
        sample.internalAddFile(this);
    }

    void internalAddSample(Sample sample) {
        if (getSamples() == null) {
            setSamples(new HashSet<Sample>());
        }
        getSamples().add(sample);
    }

    public void addFileGenerator(FileGenerator generator) {
        internalAddFileGenerator(generator);
        generator.internalAddFile(this);
    }

    void internalAddFileGenerator(FileGenerator fileGenerator) {
        if (getFileGenerators() == null) {
            setFileGenerators(new HashSet<FileGenerator>());
        }
        getFileGenerators().add(fileGenerator);
    }

    public Set<Sample> getSamples() {
        return samples;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }else if (!(object instanceof File)) {
            return false;
        }else {
            return ((File)object).getId() == getId();
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 22;
        int c = (int)(getId() ^(getId() >>>32));
        hashCode = 31 * hashCode + c;
        return hashCode;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Set<FileGenerator> getFileGenerators() {
        return fileGenerators;
    }

    public void setFileGenerators(Set<FileGenerator> fileGenerators) {
        this.fileGenerators = fileGenerators;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples = samples;
    }
}
