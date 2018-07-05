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

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class VariantClassifierTest {

    @Test
    public void testGetVariantClassification() {
        assertEquals(VariantType.SNV, VariantClassifier.getVariantClassification("A", "C",
                                                                                 1));
        assertEquals(VariantType.MNV, VariantClassifier.getVariantClassification("AT", "CG",
                                                                                 1));
        assertEquals(VariantType.DEL, VariantClassifier.getVariantClassification("T", "",
                                                                                 1));
        assertEquals(VariantType.INS, VariantClassifier.getVariantClassification("", "G",
                                                                                 1));
        assertEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("A", "GTC",
                                                                                 2));
        assertNotEquals(VariantType.INDEL, VariantClassifier.getVariantClassification("A",
                                                                                      "(LARGEDEL)",
                                                                                      2));
    }

}
