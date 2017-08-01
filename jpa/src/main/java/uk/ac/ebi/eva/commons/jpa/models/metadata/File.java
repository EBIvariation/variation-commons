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
package uk.ac.ebi.eva.commons.jpa.models.metadata;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @ManyToMany(mappedBy = "files")
    private Set<FileGenerator> fileGenerators;
    @Transient
    private Set<Sample> samples;

    public File() {
        this.name = null;
        this.type = File.Type.OTHER;
        this.md5 = null;
    }

    public File(Long id) {
        this.setId(id);
    }

    public File(String name, File.Type type, String md5) {
        this(name, type, md5, new HashSet<FileGenerator>(), new HashSet<Sample>());
    }

    public File(String name, File.Type type, String md5, Set<FileGenerator> fileGenerators, Set<Sample> samples) {
        setName(name);
        setType(type);
        setMd5(md5);
        setFileGenerators(fileGenerators);
        setSamples(samples);
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        Objects.requireNonNull(name, "Filename not specified");
        this.name = name;
    }

    public File.Type getType() {
        return type;
    }

    public final void setType(File.Type type) {
        Objects.requireNonNull(type, "Type not specified");
        this.type = type;
    }

    public String getMd5() {
        return md5;
    }

    public final void setMd5(String md5) {
        Objects.requireNonNull(md5, "MD5 not specified");
        this.md5 = md5;
    }

    public Set<FileGenerator> getFileGenerators() {
        return fileGenerators;
    }

    final void setFileGenerators(Set<FileGenerator> fileGenerators) {
        Objects.requireNonNull(fileGenerators, "File generators not specified");
        if (this.fileGenerators == null) { // Called from constructor
            this.fileGenerators = fileGenerators;
        } else {
            this.fileGenerators.clear();
            for (FileGenerator g : fileGenerators) {
                addFileGenerator(g);
            }
        }
    }

    void addFileGenerator(FileGenerator generator) {
        fileGenerators.add(generator);
    }

    public Set<Sample> getSamples() {
        return samples;
    }

    public final void setSamples(Set<Sample> samples) {
        Objects.requireNonNull(samples, "Samples not specified");
        if (this.samples == null) { // Called from constructor
            this.samples = samples;
        } else {
            this.samples.clear();
            for (Sample s : samples) {
                addSample(s);
            }
        }
    }

    public void addSample(Sample sample) {
        samples.add(sample);
        sample.addFile(this);
    }

    @Override
    public String toString() {
        return "File{" + "name=" + name + ", type=" + type + ", md5=" + md5 + '}';
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
