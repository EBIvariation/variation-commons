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

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by parce on 02/10/15.
 */
@Entity
@Table(indexes = {@Index(name = "file_unique", columnList = "name,type,md5", unique = true)})
public class File extends AbstractPersistable<Long> {

    private static final long serialVersionUID = 4602079283068239196L;

    private String name;
    private File.Type type;
    private String md5;
    @Transient private Set<FileGenerator> fileGenerators;
    @Transient private Set<Sample> samples;

    public File() {
        this(null, File.Type.OTHER, null);
    }

    public File(Long id) {
        this.setId(id);
    }

    public File(String name, File.Type type, String md5) {
        this(name, type, md5, new HashSet<FileGenerator>(), new HashSet<Sample>());
    }

    public File(String name, File.Type type, String md5, Set<FileGenerator> fileGenerators, Set<Sample> samples) {
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

    public File.Type getType() {
        return type;
    }

    public void setType(File.Type type) {
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
        for (FileGenerator g : fileGenerators) {
            addFileGenerator(g);
        }
    }

    void addFileGenerator(FileGenerator generator) {
        fileGenerators.add(generator);
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

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (!(object instanceof File)) {
            return false;
        } else {
            File otherFile = (File) object;
            return otherFile.getName().equals(name) && otherFile.getType().equals(type) && otherFile.getMd5().equals(md5);
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

    public enum Type {

        VCF("vcf"),
        VCF_AGGREGATE("vcf_aggregate"),
        README("readme_file"),
        PHENOTYPE("phenotype_file"),
        CRAM("cram"),
        TABIX("tabix"),
        WIG("wig"),
        BED("bed"),
        GFF("gff"),
        FASTA("fasta"),
        OTHER("other");

        private final String name;

        private Type(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
