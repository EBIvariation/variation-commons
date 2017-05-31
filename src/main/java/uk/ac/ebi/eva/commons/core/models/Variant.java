/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Variant implements IVariant{

    public static final int SV_THRESHOLD = 50;

    /**
     * Chromosome where the genomic variation occurred.
     */
    private final String chromosome;

    /**
     * Position where the genomic variation starts.
     * <ul>
     * <li>SNVs have the same start and end position</li>
     * <li>Insertions start in the last present position: if the first nucleotide
     * is inserted in position 6, the start is position 5</li>
     * <li>Deletions start in the first previously present position: if the first
     * deleted nucleotide is in position 6, the start is position 6</li>
     * </ul>
     */
    private final int start;

    /**
     * Position where the genomic variation ends.
     * <ul>
     * <li>SNVs have the same start and end positions</li>
     * <li>Insertions end in the first present position: if the last nucleotide
     * is inserted in position 9, the end is position 10</li>
     * <li>Deletions ends in the last previously present position: if the last
     * deleted nucleotide is in position 9, the end is position 9</li>
     * </ul>
     */
    private final int end;

    /**
     * Reference allele.
     */
    private final String reference;

    /**
     * Alternate allele.
     */
    private final String alternate;

    /**
     * Set of identifiers used for this genomic variation.
     */
    private final Set<String> ids;

    /**
     * Unique identifier following the HGVS nomenclature.
     */
    private final Map<String, Set<String>> hgvs;

    /**
     * Information specific to each file the variant was read from, such as samples or statistics.
     */
    private final Map<String, IVariantSourceEntry> sourceEntries;

    public Variant(String chromosome, int start, int end, String reference, String alternate) {
        if (start > end && !(reference.equals("-"))) {
            throw new IllegalArgumentException("End position must be greater than the start position");
        }

        this.chromosome = (chromosome != null) ? chromosome : "";
        this.start = start;
        this.end = end;
        this.reference = (reference != null) ? reference : "";
        this.alternate = (alternate != null) ? alternate : "";

        this.ids = new HashSet<>();
        this.hgvs = new HashMap<>();
        if (getType() == VariantType.SNV) { // Generate HGVS code only for SNVs
            Set<String> hgvsCodes = new HashSet<>();
            hgvsCodes.add(chromosome + ":g." + start + reference + ">" + alternate);
            this.hgvs.put("genomic", hgvsCodes);
        }
        sourceEntries = new HashMap<>();
    }

    public int getLength() {
        return Math.max(this.reference.length(), this.alternate.length());
    }

    public VariantType getType() {
        if (reference.length() == alternate.length()) {
            if (getLength() > 1) {
                return VariantType.MNV;
            } else {
                return VariantType.SNV;
            }
        } else if (getLength() <= SV_THRESHOLD) {
            /*
            * 3 possibilities for being an INDEL:
            * - The value of the ALT field is <DEL> or <INS>
            * - The REF allele is not . but the ALT is
            * - The REF allele is . but the ALT is not
            * - The REF field length is different than the ALT field length
            */
            return VariantType.INDEL;
        } else {
            return VariantType.SV;
        }
    }

    public String getChromosome() {
        return chromosome;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getReference() {
        return reference;
    }

    public String getAlternate() {
        return alternate;
    }

    public void addId(String id) {
        this.ids.add(id);
    }

    public void setIds(Set<String> ids) {
        this.ids.clear();
        this.ids.addAll(ids);
    }

    public Set<String> getIds() {
        return Collections.unmodifiableSet(ids);
    }

    public boolean addHgvs(String type, String value) {
        Set<String> listByType = hgvs.get(type);
        if (listByType == null) {
            listByType = new HashSet<>();
        }
        return listByType.add(value);
    }

    public Map<String, Set<String>> getHgvs() {
        return Collections.unmodifiableMap(hgvs);
    }

    @Override
    public String toString() {
        return "VariantWithSamplesAndAnnotations{" +
                "chromosome='" + chromosome + '\'' +
                ", position=" + start + "-" + end +
                ", reference='" + reference + '\'' +
                ", alternate='" + alternate + '\'' +
                ", ids='" + ids + '\'' +
                '}';
    }

    public void addSourceEntry(IVariantSourceEntry sourceEntry) {
        this.sourceEntries.put(getSourceEntryIndex(sourceEntry.getStudyId(), sourceEntry.getFileId()), sourceEntry);
    }

    public void addSourceEntries(Collection<? extends IVariantSourceEntry> variantSourceEntries){
        this.sourceEntries.clear();
        variantSourceEntries.forEach(variantSourceEntry ->
                sourceEntries.put(getSourceEntryIndex(variantSourceEntry),variantSourceEntry));
    }

    private String getSourceEntryIndex(IVariantSourceEntry variantSourceEntries) {
        return getSourceEntryIndex(variantSourceEntries.getStudyId(), variantSourceEntries.getFileId());
    }

    private String getSourceEntryIndex(String studyId, String fileId) {
        return studyId + "_" + fileId;
    }

    public Collection<? extends IVariantSourceEntry> getSourceEntries() {
        return Collections.unmodifiableCollection(sourceEntries.values());
    }

    public IVariantSourceEntry getSourceEntry(String fileId, String studyId) {
        return sourceEntries.get(getSourceEntryIndex(studyId, fileId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Variant)) return false;

        Variant variant = (Variant) o;

        if (start != variant.start) return false;
        if (end != variant.end) return false;
        if (!chromosome.equals(variant.chromosome)) return false;
        if (!reference.equals(variant.reference)) return false;
        if (!alternate.equals(variant.alternate)) return false;
        if (!ids.equals(variant.ids)) return false;
        if (!hgvs.equals(variant.hgvs)) return false;
        return sourceEntries.equals(variant.sourceEntries);
    }

    @Override
    public int hashCode() {
        int result = chromosome.hashCode();
        result = 31 * result + start;
        result = 31 * result + end;
        result = 31 * result + reference.hashCode();
        result = 31 * result + alternate.hashCode();
        result = 31 * result + ids.hashCode();
        result = 31 * result + hgvs.hashCode();
        result = 31 * result + sourceEntries.hashCode();
        return result;
    }
}
