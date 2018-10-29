/*
 * Copyright 2018 EMBL - European Bioinformatics Institute
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

import java.util.EnumMap;
import java.util.stream.Collectors;

/**
 * Map variant types to their corresponding SO accessions
 * See <a href="https://docs.google.com/spreadsheets/d/1YH8qDBDu7C6tqULJNCrGw8uBjdW3ZT5OjTkJzGNOZ4E/edit#gid=1433496764">Sequence Ontology definitions</a>,
 * <a href="http://www.sequenceontology.org/browser/current_release/term/SO:0001537">Structural variant</a>
 * and <a href="http://www.sequenceontology.org/browser/current_release/term/SO:0001019">Copy number variation</a>.
 */
public class VariantTypeToSOAccessionMap {

    private static final EnumMap<VariantType, String> variantTypeToSOAccessionMap = new EnumMap<>(VariantType.class);

    static String INVALID_VARIANT_TYPE_EXCEPTION_MESSAGE = "Invalid Variant Type '%s'. Only the following " +
            "variant types have a valid SO accession: ";

    static {
        variantTypeToSOAccessionMap.put(VariantType.SNV, "SO:0001483");
        variantTypeToSOAccessionMap.put(VariantType.DEL, "SO:0000159");
        variantTypeToSOAccessionMap.put(VariantType.INS, "SO:0000667");
        variantTypeToSOAccessionMap.put(VariantType.INDEL, "SO:1000032");
        variantTypeToSOAccessionMap.put(VariantType.TANDEM_REPEAT, "SO:0000705");
        variantTypeToSOAccessionMap.put(VariantType.SEQUENCE_ALTERATION, "SO:0001059");
        variantTypeToSOAccessionMap.put(VariantType.NO_SEQUENCE_ALTERATION, "SO:0002073");
        variantTypeToSOAccessionMap.put(VariantType.MNV, "SO:0002007");
        variantTypeToSOAccessionMap.put(VariantType.SV, "SO:0001537");
        variantTypeToSOAccessionMap.put(VariantType.CNV, "SO:0001019");

        INVALID_VARIANT_TYPE_EXCEPTION_MESSAGE += variantTypeToSOAccessionMap.keySet().stream().map(Enum::toString)
                                                                             .collect(Collectors.joining(","));
    }

    public static String getSequenceOntologyAccession(VariantType variantType) {
        if (variantTypeToSOAccessionMap.containsKey(variantType)) {
            return variantTypeToSOAccessionMap.get(variantType);
        }

        throw new IllegalArgumentException(String.format(INVALID_VARIANT_TYPE_EXCEPTION_MESSAGE, variantType));
    }
}
