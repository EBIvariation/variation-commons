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
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class VariantRepositoryFilterTest {
    @Test
    public void getCriteriaEq() throws Exception {
        VariantRepositoryFilter filter = new VariantRepositoryMafFilter("=0.1");
        assertEquals(Criteria.where(VariantRepositoryFilter.MAF_FIELD).is(0.1), filter.getCriteria());
    }

    @Test
    public void getCriteriaGt() throws Exception {
        VariantRepositoryFilter filter = new VariantRepositoryPolyphenFilter(">0.5");
        assertEquals(Criteria.where(VariantRepositoryFilter.POLYPHEN_FIELD).gt(0.5), filter.getCriteria());
    }

    @Test
    public void getCriteriaLt() throws Exception {
        VariantRepositoryFilter filter = new VariantRepositorySiftFilter("<0.9");
        assertEquals(Criteria.where(VariantRepositoryFilter.SIFT_FIELD).lt(0.9),
                filter.getCriteria());
    }

    @Test
    public void getCriteriaGte() throws Exception {
        VariantRepositoryFilter filter = new VariantRepositoryMafFilter(">=0.12");
        assertEquals(Criteria.where(VariantRepositoryFilter.MAF_FIELD).gte(0.12), filter.getCriteria());
    }

    @Test
    public void getCriteriaLte() throws Exception {
        VariantRepositoryFilter filter = new VariantRepositoryPolyphenFilter("<=0.856");
        assertEquals(Criteria.where(VariantRepositoryFilter.POLYPHEN_FIELD).lte(0.856), filter.getCriteria());
    }

    @Test
    public void getCriteriaIn() throws Exception {
        List<String> studies = new ArrayList<>();
        studies.add("PRJEB123");
        VariantRepositoryFilter filter = new VariantRepositoryStudyFilter(studies);
        Criteria expected = Criteria.where(VariantRepositoryFilter.STUDY_ID_FIELD).in(studies);
        Criteria test = filter.getCriteria();
        assertEquals(expected, test);
    }
}