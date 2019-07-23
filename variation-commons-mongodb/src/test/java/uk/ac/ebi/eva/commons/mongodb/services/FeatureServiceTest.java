/*
 * European Variation Archive (EVA) - Open-access database of all types of genetic
 * variation data from all species
 *
 * Copyright 2019 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.services;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.ebi.eva.commons.core.models.FeatureCoordinates;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;

import java.util.Arrays;
import java.util.List;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
@UsingDataSet(locations = {
        "/test-data/features.json"})
public class FeatureServiceTest {

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test-db");

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FeatureService featureService;

    private String GENE_ID_EXISTING1 = "ENSG00000223972";

    private String GENE_NAME_EXISITNG1 = "DDX11L1";

    private String GENE_ID_EXISTING2 = "ENST00000456328";

    private String GENE_NAME_EXISITNG2 = "DDX11L1-202";

    private String GENE_ID_NON_EXISTING = "NonExisitngGeneId";

    private String GENE_NAME_NON_EXISITNG = "NonExisitngGeneName";

    @Test
    public void testGeneIdorGeneName() {
        List<FeatureCoordinates> featureCoordinates = findByIdOrNameHelper(GENE_ID_EXISTING1,
                GENE_NAME_EXISITNG1);
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());

        featureCoordinates = findByIdOrNameHelper(GENE_ID_EXISTING1, null);
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());

        featureCoordinates = findByIdOrNameHelper(null, GENE_NAME_EXISITNG1);
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());
    }

    private List<FeatureCoordinates> findByIdOrNameHelper(String id, String name) {
        return featureService.findByIdOrName(id, name);
    }

    @Test
    public void testGeneIdorGeneNameNonExisting() {
        List<FeatureCoordinates> featureCoordinates = findByIdOrNameHelper(GENE_ID_NON_EXISTING,
                GENE_NAME_NON_EXISITNG);
        assertEquals(0, featureCoordinates.size());

        featureCoordinates = findByIdOrNameHelper(GENE_ID_NON_EXISTING, null);
        assertEquals(0, featureCoordinates.size());

        featureCoordinates = findByIdOrNameHelper(null, GENE_NAME_NON_EXISITNG);
        assertEquals(0, featureCoordinates.size());
    }

    @Test
    public void testGeneIdsorGeneNames() {
        List<FeatureCoordinates> featureCoordinates = findByIdsOrNamesHelper(Arrays.asList(GENE_ID_EXISTING1),
                Arrays.asList(GENE_NAME_EXISITNG1));
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());

        featureCoordinates = findByIdsOrNamesHelper(Arrays.asList(GENE_ID_EXISTING1), null);
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());

        featureCoordinates = findByIdsOrNamesHelper(null, Arrays.asList(GENE_NAME_EXISITNG1));
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());
    }

    private List<FeatureCoordinates> findByIdsOrNamesHelper(List<String> id, List<String> name) {
        return featureService.findAllByGeneIdsOrGeneNames(id, name);
    }

    @Test
    public void testGeneIdsOrGeneNamesForMultipleElements() {
        List<FeatureCoordinates> featureCoordinates = findByIdsOrNamesHelper(Arrays.asList(GENE_ID_EXISTING1,
                GENE_ID_EXISTING2), Arrays.asList(GENE_NAME_EXISITNG1, GENE_NAME_EXISITNG2));
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());
        assertEquals(GENE_ID_EXISTING2, featureCoordinates.get(1).getId());
        assertEquals(GENE_NAME_EXISITNG2, featureCoordinates.get(1).getName());

        featureCoordinates = findByIdsOrNamesHelper(Arrays.asList(GENE_ID_EXISTING1, GENE_ID_EXISTING2), null);
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());
        assertEquals(GENE_ID_EXISTING2, featureCoordinates.get(1).getId());
        assertEquals(GENE_NAME_EXISITNG2, featureCoordinates.get(1).getName());

        featureCoordinates = findByIdsOrNamesHelper(null, Arrays.asList(GENE_NAME_EXISITNG1, GENE_NAME_EXISITNG2));
        assertEquals(GENE_ID_EXISTING1, featureCoordinates.get(0).getId());
        assertEquals(GENE_NAME_EXISITNG1, featureCoordinates.get(0).getName());
        assertEquals(GENE_ID_EXISTING2, featureCoordinates.get(1).getId());
        assertEquals(GENE_NAME_EXISITNG2, featureCoordinates.get(1).getName());
    }

    @Test
    public void testGeneIdsOrGeneNamesNonExisting() {
        List<FeatureCoordinates> featureCoordinates = findByIdsOrNamesHelper(Arrays.asList(GENE_ID_NON_EXISTING),
                Arrays.asList(GENE_NAME_NON_EXISITNG));
        assertEquals(0, featureCoordinates.size());

        featureCoordinates = findByIdsOrNamesHelper(Arrays.asList(GENE_ID_NON_EXISTING), null);
        assertEquals(0, featureCoordinates.size());

        featureCoordinates = findByIdsOrNamesHelper(null, Arrays.asList(GENE_NAME_NON_EXISITNG));
        assertEquals(0, featureCoordinates.size());
    }
}
