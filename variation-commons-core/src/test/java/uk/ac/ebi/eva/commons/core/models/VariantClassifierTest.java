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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

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

    @Test(expected = IllegalArgumentException.class)
    public void testHyphensInReference() {
        VariantClassifier.getVariantClassification("-", "GG", 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHyphensInAlternate() {
        VariantClassifier.getVariantClassification("AA", "-", 2);
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
    public void testSTRsNoSnpClass() {
        assertEquals(VariantType.TANDEM_REPEAT, VariantClassifier.getVariantClassification("(A)5", "(A)7"));
        assertEquals(VariantType.TANDEM_REPEAT, VariantClassifier.getVariantClassification("(G)3", "(G)7"));
        assertEquals(VariantType.TANDEM_REPEAT, VariantClassifier.getVariantClassification("AAAAA", "AAAAAAA"));
        assertEquals(VariantType.TANDEM_REPEAT, VariantClassifier.getVariantClassification("CCCCC", "CCCCCCC"));
        assertEquals(VariantType.TANDEM_REPEAT, VariantClassifier.getVariantClassification("GGGGG", "GGGGGGG"));
        assertEquals(VariantType.TANDEM_REPEAT, VariantClassifier.getVariantClassification("TTTTT", "TTTTTTT"));
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

    @Test(expected = IllegalArgumentException.class)
    public void testHyphensInReferenceNoSnpClass() {
        VariantClassifier.getVariantClassification("-", "GG");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHyphensInAlternateNoSnpClass() {
        VariantClassifier.getVariantClassification("AA", "-");
    }
}
