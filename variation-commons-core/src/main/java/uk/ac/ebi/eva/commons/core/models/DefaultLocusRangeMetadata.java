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
package uk.ac.ebi.eva.commons.core.models;

/**
 * Base implementation of {@link IDefaultLocusRangeMetadata}
 */
public class DefaultLocusRangeMetadata implements IDefaultLocusRangeMetadata {

    private String chromosome;

    private long start;

    private long end;

    public DefaultLocusRangeMetadata() {

    }

    public DefaultLocusRangeMetadata(IDefaultLocusRangeMetadata defaultLocusRangeMetadata) {
        this(defaultLocusRangeMetadata.getChromosome(), defaultLocusRangeMetadata.getStart(),
                defaultLocusRangeMetadata.getEnd());
    }

    public DefaultLocusRangeMetadata(String chromosome, long start, long end) {
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
    }

    @Override
    public String getChromosome() {
        return chromosome;
    }

    @Override
    public long getStart() {
        return start;
    }

    @Override
    public long getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "DefaultLocusRangeMetadata{" +
                "chromosome='" + chromosome + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultLocusRangeMetadata that = (DefaultLocusRangeMetadata) o;

        if (start != that.start) return false;
        if (end != that.end) return false;
        return chromosome.equals(that.chromosome);
    }

    @Override
    public int hashCode() {
        int result = chromosome.hashCode();
        result = 31 * result + (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (end ^ (end >>> 32));
        return result;
    }
}
