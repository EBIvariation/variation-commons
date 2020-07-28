/*
 * Copyright 2018 EMBL - European Bioinformatics Institute
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

import java.util.regex.Pattern;

/**
 * Implementation of Variant type classification based on Sequence Ontology (SO) definitions.
 */
public class VariantClassifier {

    private static final Pattern alphaRegExPattern = Pattern.compile("[A-Z]+");

    private static final Pattern ACGTPattern = Pattern.compile("[ACGT]+");

    private static final Pattern tandemRepeatsPattern = Pattern.compile("^\\([ACGT]+\\)\\d+$");

    /**
     * Get the type of a given variant based on Sequence Ontology (SO) definitions.
     * See <a href="https://docs.google.com/spreadsheets/d/1YH8qDBDu7C6tqULJNCrGw8uBjdW3ZT5OjTkJzGNOZ4E/edit#gid=1433496764">documentation</a>.
     * @TODO: might need to be merged eventually into {@link uk.ac.ebi.eva.commons.core.models.AbstractVariant#getType()}
     *
     * @param reference Reference allele - expects normalized allele
     * @param alternate Alternate allele - expects normalized allele
     * @param subSnpClass SubSNP class (as defined in dbSNP variant data) - See <a href="https://www.ncbi.nlm.nih.gov/books/NBK21088/table/ch5.ch5_t3/?report=objectonly">definitions</a>
     * @return Type of the variant
     */
    public static VariantType getVariantClassification(String reference, String alternate, int subSnpClass) {
        reference = reference.trim().toUpperCase();
        alternate = alternate.trim().toUpperCase();

        if (reference.equals("NOVARIATION") || alternate.equals(reference)) {
                return VariantType.NO_SEQUENCE_ALTERATION;
        } else {
            if (subSnpClass == 4) {
                return VariantType.TANDEM_REPEAT;
            }
            if (subSnpClass == 5) {
                return VariantType.SEQUENCE_ALTERATION;
            }

            boolean isRefAlpha = alphaRegExPattern.matcher(reference).matches();
            boolean isAltAlpha = alphaRegExPattern.matcher(alternate).matches();

            if (isRefAlpha && isAltAlpha) {
                if (reference.length() == alternate.length()) {
                    return reference.length() == 1? VariantType.SNV : VariantType.MNV;
                }
                if (subSnpClass == 2) {
                    return VariantType.INDEL;
                }
            }
            if (isRefAlpha && alternate.isEmpty()) {
                return VariantType.DEL;
            }
            if (isAltAlpha && reference.isEmpty()) {
                return VariantType.INS;
            }
        }
        throw new IllegalArgumentException(String.format("Cannot determine the type of the Variant with Reference: %s, "
                                                                 + "Alternate: %s and SubSNP class: %d",
                                                         reference, alternate, subSnpClass));
    }

    /**
     * Get the type of a given variant without using the subSnpClass
     */
    public static VariantType getVariantClassification(String reference, String alternate) {
        reference = reference.trim().toUpperCase();
        alternate = alternate.trim().toUpperCase();

        if (reference.equals("NOVARIATION") || alternate.equals(reference)) {
            return VariantType.NO_SEQUENCE_ALTERATION;
        } else {
            if (isTandemRepeat(reference) || isTandemRepeat(alternate)) {
                return VariantType.INDEL;
            }

            boolean isRefAlpha = alphaRegExPattern.matcher(reference).matches();
            boolean isAltAlpha = alphaRegExPattern.matcher(alternate).matches();

            if(isNamedAllele(reference, isRefAlpha) || isNamedAllele(alternate, isAltAlpha)) {
                return VariantType.SEQUENCE_ALTERATION;
            }

            if (isRefAlpha && isAltAlpha) {
                if (reference.length() == alternate.length()) {
                    return reference.length() == 1? VariantType.SNV : VariantType.MNV;
                }
                if (!reference.isEmpty() && !alternate.isEmpty()) {
                    return VariantType.INDEL;
                }
            }
            if (isRefAlpha && alternate.isEmpty()) {
                return VariantType.DEL;
            }
            if (isAltAlpha && reference.isEmpty()) {
                return VariantType.INS;
            }
        }
        throw new IllegalArgumentException(String.format("Cannot determine the type of the Variant with Reference: %s, "
                        + "Alternate: %s", reference, alternate));
    }

    private static boolean isTandemRepeat(String allele) {
        return tandemRepeatsPattern.matcher(allele).matches();
    }

    private static boolean isNamedAllele(String allele, boolean isAlpha) {
        return (allele.startsWith("(") && allele.endsWith(")")) || (allele.startsWith("<") && allele.endsWith(">")) ||
                isAlpha && !ACGTPattern.matcher(allele).matches();
    }
}
