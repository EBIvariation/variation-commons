/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.repositories;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.eva.commons.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.AnnotationMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
@UsingDataSet(locations = {
        "/test-data/variants.json",
        "/test-data/annotations.json"})
public class AnnotationMongoRepositoryTest {

    public static final String TEST_DB = "test-db";

    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb(TEST_DB);

    @Autowired
    private AnnotationRepository repository;

    @Autowired
    private VariantRepository variantRepository;

    @Test
    public void testFindOne() {
        AnnotationMongo annotation = repository.findOne("9", 10099, "A", "T", "78", "78");
        assertNotNull(annotation);
        assertEquals(7, annotation.getConsequenceTypes().size());
    }

    @Test
    public void testFindMultiple() {
        Set<AnnotationMongo> annotations = repository.findByIdIn(
                Arrays.asList("9_10099_A_T_78_78", "11_190010_G_A_78_78", "11_190013_C_T_78_78"));
        assertNotNull(annotations);
        assertEquals(3, annotations.size());
    }

    @Test
    public void testFindAnnotationsOfVariants() {
        List<VariantMongo> documents = new ArrayList<>();
        documents.add(variantRepository.findOne("9_10099_A_T"));
        documents.add(variantRepository.findOne("11_190020_G_A"));
        documents.add(variantRepository.findOne("11_190010_G_A"));
        Set<AnnotationMongo> annotations = repository.findAnnotationsOfVariants(documents);
        assertNotNull(annotations);
        assertEquals(3, annotations.size());
    }

    @Test
    public void testAndIndex() {
        List<VariantMongo> documents = new ArrayList<>();
        documents.add(variantRepository.findOne("9_10099_A_T"));
        documents.add(variantRepository.findOne("11_190020_G_A"));
        documents.add(variantRepository.findOne("11_190010_G_A"));
        Map<String, Set<AnnotationMongo>> annotations = repository.findAndIndexAnnotationsOfVariants(documents);
        assertNotNull(annotations);
        assertEquals(3, annotations.size());
        assertTrue(annotations.containsKey("9_10099_A_T"));
        assertTrue(annotations.containsKey("11_190020_G_A"));
        assertTrue(annotations.containsKey("11_190010_G_A"));
        assertEquals("9_10099_A_T_78_78", annotations.get("9_10099_A_T").iterator().next().getId());
        assertEquals("11_190020_G_A_78_78", annotations.get("11_190020_G_A").iterator().next().getId());
        assertEquals("11_190010_G_A_78_78", annotations.get("11_190010_G_A").iterator().next().getId());
    }

}
