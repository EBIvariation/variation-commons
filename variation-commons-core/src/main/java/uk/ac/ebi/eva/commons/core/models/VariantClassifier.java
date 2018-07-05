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

import java.util.SortedMap;
import java.util.TreeMap;

public class VariantClassifier {
    public static VariantType getVariantClassification(String reference, String alternate, int subSNPClass) {
        String alphaRegEx = "[A-Z]+";

        reference = reference.trim();
        alternate = alternate.trim();

        if (!alternate.equals(reference)) {
            if (reference.matches(alphaRegEx) && alternate.matches(alphaRegEx)) {
                if (reference.length() == alternate.length()) {
                    if (reference.length() == 1) {
                        return VariantType.SNV;
                    }
                    else {
                        return VariantType.MNV;
                    }
                }
                if (alternate.length() > 0 && reference.length() > 0 &&
                        reference.charAt(0) != alternate.charAt(0) && alternate.length() > reference.length()) {
                    return VariantType.INDEL;
                }
            }
            else if (reference.matches(alphaRegEx) && alternate.equals("")) {
                return VariantType.DEL;
            }
            else if (alternate.matches(alphaRegEx) && reference.equals("")) {
                return VariantType.INS;
            }
        }

        return VariantType.UNKNOWN;
    }
}
