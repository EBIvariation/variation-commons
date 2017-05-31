/*
 * European Variation Archive (EVA) - Open-access database of all types of genetic
 * variation data from all species
 *
 * Copyright 2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class VariantRepositoryDoubleFilterTest {

    @Test
    public void getValueFromRelation() throws Exception {
        assertEquals(new Double(0.5), VariantEntityRepositoryDoubleFilter.getValueFromRelation("=0.5"));
        assertEquals(new Double(0.12), VariantEntityRepositoryDoubleFilter.getValueFromRelation(">0.12"));
        assertEquals(new Double(0.134), VariantEntityRepositoryDoubleFilter.getValueFromRelation(">=0.134"));
        assertEquals(new Double(1.1), VariantEntityRepositoryDoubleFilter.getValueFromRelation("<1.1"));
        assertEquals(new Double(0.5), VariantEntityRepositoryDoubleFilter.getValueFromRelation("<=0.5"));
    }

    @Test
    public void getRelationalOperatorFromRelation() throws Exception {
        assertEquals(RelationalOperator.EQ,
                VariantEntityRepositoryDoubleFilter.getRelationalOperatorFromRelation("=0.5"));
        assertEquals(RelationalOperator.GT,
                VariantEntityRepositoryDoubleFilter.getRelationalOperatorFromRelation(">0.12"));
        assertEquals(RelationalOperator.GTE,
                VariantEntityRepositoryDoubleFilter.getRelationalOperatorFromRelation(">=0.134"));
        assertEquals(RelationalOperator.LT,
                VariantEntityRepositoryDoubleFilter.getRelationalOperatorFromRelation("<1.1"));
        assertEquals(RelationalOperator.LTE,
                VariantEntityRepositoryDoubleFilter.getRelationalOperatorFromRelation("<=0.5"));
    }

}
