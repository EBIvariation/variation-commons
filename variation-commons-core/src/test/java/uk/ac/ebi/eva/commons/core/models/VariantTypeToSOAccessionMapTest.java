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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class VariantTypeToSOAccessionMapTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testInvalidVariantTypeLookup() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(
                String.format(VariantTypeToSOAccessionMap.INVALID_VARIANT_TYPE_EXCEPTION_MESSAGE,
                              VariantType.NO_ALTERNATE));
        VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.NO_ALTERNATE);
    }

    @Test
    public void testValidVariantTypeLookup() {
        assertEquals("SO:0001483", VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.SNV));
        assertEquals("SO:0000159", VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.DEL));
        assertEquals("SO:0000667", VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.INS));
        assertEquals("SO:1000032", VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.INDEL));
        assertEquals("SO:0000705", VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.TANDEM_REPEAT));
        assertEquals("SO:0001059",
                     VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.SEQUENCE_ALTERATION));
        assertEquals("SO:0002073",
                     VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.NO_SEQUENCE_ALTERATION));
        assertEquals("SO:0002007", VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.MNV));
        assertEquals("SO:0001537", VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.SV));
        assertEquals("SO:0001019", VariantTypeToSOAccessionMap.getSequenceOntologyAccession(VariantType.CNV));
    }

}
