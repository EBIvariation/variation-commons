/*
 * Copyright 2019 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.core.models.IDefaultLocusRangeMetadata;

/**
 * Mapped class for annotations metadata collection in mongo
 */
@Document(collection = "#{mongoCollectionsDefaultLocusRangeMetadata}")
public class DefaultLocusRangeMetadataMongo implements IDefaultLocusRangeMetadata {

    private static final String CHROMOSOME_FIELD = "chr";

    private static final String START_FIELD = "start";

    private static final String END_FIELD = "end";

    @Id
    private String id;

    @Field(CHROMOSOME_FIELD)
    private String chromosome;

    @Field(START_FIELD)
    private long start;

    @Field(END_FIELD)
    private long end;

    DefaultLocusRangeMetadataMongo() {
        // Empty document constructor for spring-data
    }

    public DefaultLocusRangeMetadataMongo(String id, String chromosome, long start, long end) {
        this.id = id;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChromosome() {
        return chromosome;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "DefaultLocusRangeMetadataMongo{" +
                "chromosome='" + chromosome + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultLocusRangeMetadataMongo that = (DefaultLocusRangeMetadataMongo) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (start != that.start) return false;
        if (end != that.end) return false;
        return chromosome.equals(that.chromosome);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (chromosome != null ? chromosome.hashCode() : 0);
        result = 31 * result + (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (end ^ (end >>> 32));
        return result;
    }
}
