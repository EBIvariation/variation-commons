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

    private static Pattern alphaRegExPattern = Pattern.compile("[A-Z]+");
    /***
     * Get the type of a given variant based on Sequence Ontology (SO) definitions.
     * See https://docs.google.com/spreadsheets/d/1YH8qDBDu7C6tqULJNCrGw8uBjdW3ZT5OjTkJzGNOZ4E/edit#gid=1433496764
     * @TODO: might need to be merged eventually into {@link uk.ac.ebi.eva.commons.core.models.AbstractVariant#getType()}
     *
     * @param reference Reference allele - expects trimmed allele
     * @param alternate Alternate allele - expects trimmed allele
     * @param subSNPClass SubSNP class (as defined in dbSNP variant data)
     *                    - See https://www.ncbi.nlm.nih.gov/books/NBK21088/table/ch5.ch5_t3/?report=objectonly
     * @return Type of the variant
     */
    public static VariantType getVariantClassification(String reference, String alternate, int subSNPClass) {
        reference = reference.trim();
        alternate = alternate.trim();

        if (reference.equals("NOVARIATION")) {
            if (subSNPClass == 6) {
                return VariantType.NO_SEQUENCE_ALTERATION;
            }
        } else {
            if (subSNPClass == 4) {
                return VariantType.TANDEM_REPEAT;
            }
            if (subSNPClass == 5) {
                return VariantType.SEQUENCE_ALTERATION;
            }
            if (!alternate.equals(reference)) {
                boolean isRefAlpha = alphaRegExPattern.matcher(reference).matches();
                boolean isAltAlpha = alphaRegExPattern.matcher(alternate).matches();

                if (isRefAlpha && isAltAlpha) {
                    if (reference.length() == alternate.length()) {
                        return reference.length() == 1? VariantType.SNV: VariantType.MNV;
                    }
                    if (subSNPClass == 2) {
                        return VariantType.INDEL;
                    }
                }
                if (isRefAlpha && alternate.equals("")) {
                    return VariantType.DEL;
                }
                if (isAltAlpha && reference.equals("")) {
                    return VariantType.INS;
                }
            }
        }
        throw new IllegalArgumentException(String.format("Cannot determine the type of the Variant with Reference: %s, "
                                                                 + "Alternate: %s and SubSNP class: %d",
                                                         reference, alternate, subSNPClass));
    }
}
