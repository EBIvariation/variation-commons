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
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Created by parce on 02/10/15.
 */
@Entity
@Inheritance
@DiscriminatorColumn(name = "type")
@Table(uniqueConstraints = {@UniqueConstraint(name = "alias_unique", columnNames = "alias")})
public abstract class FileGenerator extends AbstractPersistable<Long> {
    
    private static final long serialVersionUID = -5926609525556333330L;
    
    protected String alias;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name="file_generator_file",
            joinColumns = {@JoinColumn(name="file_generator_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name="file_id", referencedColumnName = "id")}
    )
    protected Set<File> files = new HashSet<>();
    @Transient protected Dataset dataset;

    @ManyToOne
    protected Study study;

    public FileGenerator() {
    }

    public FileGenerator(Long id) {
        this.setId(id);
    }
    
    protected FileGenerator(String alias) {
        this(alias, new HashSet<File>());
    }
    
    protected FileGenerator(String alias, Set<File> files) {
        setAlias(alias);
        setFiles(files);
    }

    @Column(name = "alias")
    public String getAlias() {
        return alias;
    }

    public final void setAlias(String alias) {
        Objects.requireNonNull(alias, "Alias not specified");
        this.alias = alias;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void addFile(File file) {
        this.files.add(file);
        file.addFileGenerator(this);
    }

    public final void setFiles(Set<File> files) {
        Objects.requireNonNull(files, "Files not specified");
        this.files.clear();
        for (File f : files) {
            addFile(f);
        }
    }

    public Dataset getDataset() {
        return dataset;
    }

    void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    void unsetDataset() {
        this.dataset = null;
    }

    public Study getStudy() {
        return study;
    }

    void setStudy(Study study) {
        this.study = study;
    }

    void unsetStudy() {
        this.study = null;
    }

    // TODO: add removeFile method
    @Override
    public abstract boolean equals(Object e);

    @Override
    public abstract int hashCode();
}
