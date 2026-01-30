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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VariantClassifierTest {

    @Test
    public void testSNVs() {
        assertEquals(VariantType.SNV, VariantClassifier.getVariantClassification("A", "C", 1));
    }

    @Test
    public void testDeletions() {
        assertEquals(VariantType.DEL, VariantClassifier.getVariantClassification("TT", "", 2));
    }

    @Test
    public void testInsertions() {
        assertEquals(VariantType.INS, VariantClassifier.getVariantClassification("", "G", 2));
    }

    @Test
    public void testINDELs() {
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("A", "GTC", 2));
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("AA", "G", 2));
    }

    @Test
    public void testSTRs() {
        assertEquals(VariantType.TANDEM_REPEAT, VariantClassifier.getVariantClassification("(A)5", "(A)7", 4));
        assertEquals(VariantType.TANDEM_REPEAT, VariantClassifier.getVariantClassification("AAAAA", "AAAAAAA", 4));
    }

    @Test
    public void testSequenceAlterations() {
        assertEquals(VariantType.SEQUENCE_ALTERATION,
                     VariantClassifier.getVariantClassification("(ALI008)", "(LI090)", 5));
        assertEquals(VariantType.SEQUENCE_ALTERATION, VariantClassifier.getVariantClassification("ATCZ", "", 5));
        assertEquals(VariantType.SEQUENCE_ALTERATION,
                     VariantClassifier.getVariantClassification("(ALI008)", "(LI090)", 5));
    }

    @Test
    public void testNoSequenceAlterations() {
        assertEquals(VariantType.NO_SEQUENCE_ALTERATION,
                     VariantClassifier.getVariantClassification("NOVARIATION", "", 6));
        assertNotEquals(VariantType.NO_SEQUENCE_ALTERATION,
                        VariantClassifier.getVariantClassification("NOVAR", "", 6));
        assertEquals(VariantType.NO_SEQUENCE_ALTERATION,
                     VariantClassifier.getVariantClassification("", "", 6));
        assertEquals(VariantType.NO_SEQUENCE_ALTERATION,
                     VariantClassifier.getVariantClassification("NOVARIATION", "NOVARIATION", 6));
    }

    @Test
    public void testMNVs() {
        assertEquals(VariantType.MNV, VariantClassifier.getVariantClassification("AT", "CG", 8));
    }

    @Test
    public void testHyphensInReference() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariantClassifier.getVariantClassification("-", "GG", 2);
        });
    }

    @Test
    public void testHyphensInAlternate() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariantClassifier.getVariantClassification("AA", "-", 2);
        });
    }

    //Test getVariantClassification without subSNPClass
    @Test
    public void testSNVsNoSnpClass() {
        assertEquals(VariantType.SNV, VariantClassifier.getVariantClassification("A", "C"));
    }

    @Test
    public void testDeletionsNoSnpClass() {
        assertEquals(VariantType.DEL, VariantClassifier.getVariantClassification("TT", ""));
    }

    @Test
    public void testInsertionsNoSnpClass() {
        assertEquals(VariantType.INS, VariantClassifier.getVariantClassification("", "G"));
    }

    @Test
    public void testINDELsNoSnpClass() {
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("A", "GTC"));
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("AA", "G"));
    }

    @Test
    public void testSTRsAsIndelsNoSnpClass() {
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("(A)5", "(A)7"));
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("(G)3", "(G)7"));
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("AAAAA", "AAAAAAA"));
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("CCCCC", "CCCCCCC"));
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("TACTACTACTAC", "TACTAC"));
    }

    @Test
    public void testSequenceAlterationsNoSnpClass() {
        assertEquals(VariantType.SEQUENCE_ALTERATION,
                VariantClassifier.getVariantClassification("(ALI008)", "(LI090)"));
        assertEquals(VariantType.SEQUENCE_ALTERATION, VariantClassifier.getVariantClassification("ATCZ", ""));
        assertEquals(VariantType.SEQUENCE_ALTERATION,
                VariantClassifier.getVariantClassification("(ALI008)", "(LI090)"));
    }

    @Test
    public void testNoSequenceAlterationsNoSnpClass() {
        assertEquals(VariantType.NO_SEQUENCE_ALTERATION,
                VariantClassifier.getVariantClassification("NOVARIATION", ""));
        assertNotEquals(VariantType.NO_SEQUENCE_ALTERATION,
                VariantClassifier.getVariantClassification("NOVAR", ""));
        assertEquals(VariantType.NO_SEQUENCE_ALTERATION,
                VariantClassifier.getVariantClassification("", ""));
        assertEquals(VariantType.NO_SEQUENCE_ALTERATION,
                VariantClassifier.getVariantClassification("NOVARIATION", "NOVARIATION"));
    }

    @Test
    public void testMNVsNoSnpClass() {
        assertEquals(VariantType.MNV, VariantClassifier.getVariantClassification("AT", "CG"));
    }

    @Test
    public void testHyphensInReferenceNoSnpClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariantClassifier.getVariantClassification("-", "GG");
        });
    }

    @Test
    public void testHyphensInAlternateNoSnpClass() {
        assertThrows(IllegalArgumentException.class, () -> {
            VariantClassifier.getVariantClassification("AA", "-");
        });
    }
}
