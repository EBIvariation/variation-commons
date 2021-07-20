/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.eva.commons.core.models;

import org.apache.commons.lang3.StringUtils;
import uk.ac.ebi.eva.commons.core.models.factories.exception.NonVariantException;

import static java.lang.Math.max;

/**
 * Class that stores a Variant coordinates and alleles, normalizing and left aligning them.
 * <p>
 * This task comprises 2 steps: removing the trailing bases that are
 * identical in both alleles, then the leading identical bases.
 * <p>
 * It is left aligned because the trailing bases are removed before the leading ones, implying a normalization where
 * the position is moved the least possible from its original location.
 */
public class VariantCoreFields {

    private final String chromosome;

    private long start;

    private long end;

    private String reference;

    private String alternate;

    public VariantCoreFields(String chromosome, long position, String reference, String alternate, boolean checkForContextBase) {
        if (reference.equals(alternate)) {
            throw new NonVariantException("One alternate allele is identical to the reference. Variant found as: "
                    + chromosome + ":" + position + ":" + reference + ">" + alternate);
        }

        this.chromosome = chromosome;
        String rightTrimmedReference = reference;
        String rightTrimmedAlternate = alternate;

        if (!checkForContextBase || (!isContextBasePresent(reference, alternate))) {
            // remove common trailing bases
            int numTrailingNucleotidesToRemove = getIndexOfLastDifferentNucleotide(reference, alternate);
            rightTrimmedReference = reference.substring(0, reference.length() - numTrailingNucleotidesToRemove);
            rightTrimmedAlternate = alternate.substring(0, alternate.length() - numTrailingNucleotidesToRemove);
        }

        // remove common leading bases
        int numLeadingNucleotidesToRemove = StringUtils.indexOfDifference(rightTrimmedReference, rightTrimmedAlternate);
        this.reference = rightTrimmedReference.substring(numLeadingNucleotidesToRemove);
        this.alternate = rightTrimmedAlternate.substring(numLeadingNucleotidesToRemove);

        // calculate start and end
        start = position + numLeadingNucleotidesToRemove;
        end = calculateEnd(position, rightTrimmedReference, rightTrimmedAlternate);
    }

    private boolean isContextBasePresent(String reference, String alternate) {
        if (alternate.length() == 1 && reference.length() > 1 && reference.startsWith(alternate)) {
            return true;
        } else if (reference.length() == 1 && alternate.length() > 1 && alternate.startsWith(reference)) {
            return true;
        } else {
            return false;
        }
    }

    private int getIndexOfLastDifferentNucleotide(String reference, String alternate) {
        String refReversed = StringUtils.reverse(reference);
        String altReversed = StringUtils.reverse(alternate);
        return StringUtils.indexOfDifference(refReversed, altReversed);
    }

    private long calculateEnd(long position, String reference, String alternate) {
        int length = max(reference.length(), alternate.length());
        return position + length - 1;
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

    public String getReference() {
        return reference;
    }

    public String getAlternate() {
        return alternate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VariantCoreFields that = (VariantCoreFields) o;

        if (start != that.start) return false;
        if (end != that.end) return false;
        if (chromosome != null ? !chromosome.equals(that.chromosome) : that.chromosome != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;
        return alternate != null ? alternate.equals(that.alternate) : that.alternate == null;
    }

    @Override
    public int hashCode() {
        int result = chromosome != null ? chromosome.hashCode() : 0;
        result = 31 * result + (int) (start ^ (start >>> 32));
        result = 31 * result + (int) (end ^ (end >>> 32));
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (alternate != null ? alternate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VariantCoreFields{" +
                "chromosome='" + chromosome + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", reference='" + reference + '\'' +
                ", alternate='" + alternate + '\'' +
                '}';
    }
}