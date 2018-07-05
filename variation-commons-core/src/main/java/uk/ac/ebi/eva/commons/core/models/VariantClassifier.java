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
    public static SortedMap<String, VariantClassifierResult> getVariantClassification(String chromosome, long position,
                                                                                      String reference, String alleles,
                                                                                      int subSNPClass) {
        String alphaRegEx = "[A-Z]+";
        String[] alternateAlleles = alleles.split("/");
        SortedMap<String, VariantClassifierResult> variantTypeMap = new TreeMap<>();

        reference = reference.trim();

        for (int allelesIdx = 0; allelesIdx < alternateAlleles.length; allelesIdx++) {
            alternateAlleles[allelesIdx] = alternateAlleles[allelesIdx].trim();
            String alternate = alternateAlleles[allelesIdx];
            if (alternate.equals("-")) {
                alternate = "";
            }
            if (reference.equals("-")) {
                reference = "";
            }
            if (!alternate.equals(reference)) {
                VariantCoreFields variantCoreFields = new VariantCoreFields(chromosome, position, reference, alternate);

                String alignedRef = variantCoreFields.getReference();
                String alignedAlt = variantCoreFields.getAlternate();
                String refAlt = alignedRef + "/" + alignedAlt;

                if (alignedRef.matches(alphaRegEx) && alignedAlt.matches(alphaRegEx)) {
                    if (alignedRef.length() == alignedAlt.length()) {
                        if (alignedRef.length() == 1) {
                            variantTypeMap.put(refAlt, new VariantClassifierResult(alignedRef, alignedAlt,
                                                                                       VariantType.SNV));
                        }
                        else {
                            variantTypeMap.put(refAlt, new VariantClassifierResult(alignedRef, alignedAlt,
                                                                                       VariantType.MNV));
                        }
                    }
                }
                else if (alignedRef.matches(alphaRegEx) && alignedAlt.equals("")) {
                    variantTypeMap.put(refAlt, new VariantClassifierResult(alignedRef, alignedAlt,
                                                                               VariantType.DEL));
                }
                else if (alignedAlt.matches(alphaRegEx) && alignedRef.equals("")) {
                    variantTypeMap.put(refAlt, new VariantClassifierResult(alignedRef, alignedAlt,
                                                                               VariantType.INS));
                }
            }
            //else if (alignedRef.matc)
        }

        return variantTypeMap;
    }
}
