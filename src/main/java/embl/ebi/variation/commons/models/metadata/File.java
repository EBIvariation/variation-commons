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
    private String name;
    private String type;
    private String md5;
    private Set<FileGenerator> fileGenerators;
    private Set<Sample> samples;

    // private String analysisTitle; // TODO: there is no relation between file or analysis in the new model?

    public File(String name, String type, String md5) {
        this(name, type, md5, new HashSet<FileGenerator>(), new HashSet<Sample>());
    }

    public File(String name, String type, String md5, Set<FileGenerator> fileGenerators, Set<Sample> samples) {
        this.name = name;
        this.type = type;
        this.md5 = md5;
        this.fileGenerators = fileGenerators != null ? fileGenerators : new HashSet<FileGenerator>();
        this.samples = samples != null ? samples : new HashSet<Sample>();
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

    void setFileGenerators(Set<FileGenerator> fileGenerators) {
        this.fileGenerators.clear();
        for (FileGenerator g: fileGenerators) {
            addFileGenerator(g);
        }
    }

    public Set<Sample> getSamples() {
        return samples;
    }

    public void setSamples(Set<Sample> samples) {
        this.samples.clear();
        for (Sample s : samples) {
            addSample(s);
        }
    }

    public void addSample(Sample sample) {
        samples.add(sample);
        sample.addFile(this);
    }

    void addFileGenerator(FileGenerator generator) {
        fileGenerators.add(generator);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }else if (!(object instanceof File)) {
            return false;
        }else {
            File otherFile = (File)object;
            return (otherFile.getName().equals(name )&& otherFile.getType().equals(type) && otherFile.getMd5().equals(md5));
        }
    }

    @Override
    public int hashCode() {
        int hashCode = 22;
        int c = name.hashCode();
        hashCode = 31 * hashCode + c;
        c = type.hashCode();
        hashCode = 31 * hashCode + c;
        c = md5.hashCode();
        hashCode = 31 * hashCode + c;
        return hashCode;
    }
}
