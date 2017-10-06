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

import static java.lang.Math.max;

public class VariantKeyFields {

    private final String chromosome;

    private int start;

    private int end;

    private String reference;

    private String alternate;

    public VariantKeyFields(String chromosome, int position, String reference, String alternate) {
        this.chromosome = chromosome;
        this.reference = reference;
        this.alternate = alternate;
        normalizeLeftAlign(position);
    }

    /**
     * Calculates the normalized start, end, reference and alternate of a variant where the
     * reference and the alternate are not identical.
     * <p>
     * This task comprises 2 steps: removing the trailing bases that are
     * identical in both alleles, then the leading identical bases.
     * <p>
     * It is left aligned because the traling bases are removed before the leading ones, implying a normalization where
     * the position is moved the least possible from its original location.
     * @param position Input starting position
     */
    private void normalizeLeftAlign(int position) {
        if (reference.equals(alternate)) {
            throw new IllegalArgumentException("One alternate allele is identical to the reference. Variant found as: "
                                                       + chromosome + ":" + position + ":" + reference + ">" + alternate);
        }

        // Remove the trailing bases
        String refReversed = StringUtils.reverse(reference);
        String altReversed = StringUtils.reverse(alternate);
        int indexOfDifference = StringUtils.indexOfDifference(refReversed, altReversed);
        reference = StringUtils.reverse(refReversed.substring(indexOfDifference));
        alternate = StringUtils.reverse(altReversed.substring(indexOfDifference));

        // Remove the leading bases
        indexOfDifference = StringUtils.indexOfDifference(reference, alternate);
        start = position + indexOfDifference;
        int length = max(reference.length(), alternate.length());
        end = position + length - 1;    // -1 because end is inclusive
        if (indexOfDifference > 0) {
            reference = reference.substring(indexOfDifference);
            alternate = alternate.substring(indexOfDifference);
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
}