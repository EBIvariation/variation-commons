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
        SortedMap<String,VariantClassifierResult> actualResult = VariantClassifier.getVariantClassification(
                "chr1", 1000L, "A", "A/C", 1);
        assertEquals(actualResult.get("A/C").variantType, VariantType.SNV);

        actualResult = VariantClassifier.getVariantClassification("chr1", 1000L, "AT",
                                                            "AT/AG/CG/A/ATG/-", 1);
        assertEquals(actualResult.get("T/G").variantType, VariantType.SNV);
        assertEquals(actualResult.get("AT/CG").variantType, VariantType.MNV);
        assertEquals(actualResult.get("T/").variantType, VariantType.DEL);
        assertEquals(actualResult.get("/G").variantType, VariantType.INS);
        assertEquals(actualResult.get("AT/").variantType, VariantType.DEL);

        actualResult = VariantClassifier.getVariantClassification("chr1", 1000L, "-",
                                                                  " A /  AT", 1);
        assertEquals(actualResult.get("/A").variantType, VariantType.INS);
        assertEquals(actualResult.get("/AT").variantType, VariantType.INS);
    }

}
