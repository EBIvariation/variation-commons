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
package uk.ac.ebi.eva.commons.mongodb.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.ac.ebi.eva.commons.core.models.AnnotationMetadata;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.test.TestDataLoader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:eva.properties")
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class AnnotationMetadataServiceTest {

    @Autowired
    private AnnotationMetadataService service;

    @Autowired
    private TestDataLoader testDataLoader;

    @BeforeEach
    public void setUp() throws IOException {
        testDataLoader.load("/test-data/annotation_metadata.json");
    }

    @AfterEach
    public void tearDown() {
        testDataLoader.cleanupTestCollections();
    }

    @Test
    public void testFindAllByOrderByCacheVersionDescVepVersionDescSize() throws Exception {
        List<AnnotationMetadata> annotationMetadataMongoList = service.findAllByOrderByCacheVersionDescVepVersionDesc();
        assertEquals(5, annotationMetadataMongoList.size());
    }

    @Test
    public void testFindAllByOrderByCacheVersionDescVepVersionDescCacheVersionOrder() throws Exception {
        List<AnnotationMetadata> annotationMetadataList = service.findAllByOrderByCacheVersionDescVepVersionDesc();
        AnnotationMetadata prevAnnotationMetadata = annotationMetadataList.get(0);
        for (AnnotationMetadata curAnnotationMetadata :
                annotationMetadataList.subList(1, annotationMetadataList.size())) {
            assertTrue(curAnnotationMetadata.getCacheVersion()
                    .compareTo(prevAnnotationMetadata.getCacheVersion()) <= 0);
        }
    }

    @Test
    public void testFindAllByOrderByCacheVersionDescVepVersionDescVepVersionOrder() throws Exception {
        List<AnnotationMetadata> annotationMetadataList = service.findAllByOrderByCacheVersionDescVepVersionDesc();
        AnnotationMetadata prevAnnotationMetadata = annotationMetadataList.get(0);
        for (AnnotationMetadata curAnnotationMetadataMongo :
                annotationMetadataList.subList(1, annotationMetadataList.size())) {
            if (curAnnotationMetadataMongo.getCacheVersion().equals(prevAnnotationMetadata.getCacheVersion())) {
                assertTrue(curAnnotationMetadataMongo.getCacheVersion()
                        .compareTo(prevAnnotationMetadata.getCacheVersion()) <= 0);
            }
        }
    }

}
