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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.AnnotationMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.test.TestDataLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:eva.properties")
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class AnnotationMongoRepositoryTest {

    @Autowired
    private AnnotationRepository repository;

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private TestDataLoader testDataLoader;

    @BeforeEach
    public void setUp() throws IOException {
        testDataLoader.load(
                "/test-data/variants.json",
                "/test-data/annotations.json"
        );
    }

    @AfterEach
    public void tearDown() {
        testDataLoader.cleanupTestCollections();
    }

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
        documents.add(variantRepository.findById("9_10099_A_T").get());
        documents.add(variantRepository.findById("11_190020_G_A").get());
        documents.add(variantRepository.findById("11_190010_G_A").get());
        Set<AnnotationMongo> annotations = repository.findAnnotationsOfVariants(documents);
        assertNotNull(annotations);
        assertEquals(3, annotations.size());
    }

    @Test
    public void testAndIndex() {
        List<VariantMongo> documents = new ArrayList<>();
        documents.add(variantRepository.findById("9_10099_A_T").get());
        documents.add(variantRepository.findById("11_190020_G_A").get());
        documents.add(variantRepository.findById("11_190010_G_A").get());
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
