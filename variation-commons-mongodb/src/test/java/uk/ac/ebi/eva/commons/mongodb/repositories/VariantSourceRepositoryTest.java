/*
 * Copyright 2016-2017 EMBL - European Bioinformatics Institute
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.ac.ebi.eva.commons.core.models.IVariantGlobalStats;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.test.TestDataLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:eva.properties")
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class VariantSourceRepositoryTest {

    protected static Logger logger = LoggerFactory.getLogger(VariantSourceRepositoryTest.class);

    private static final String FIRST_STUDY_ID = "firstStudyId";
    private static final String SECOND_STUDY_ID = "secondStudyId";

    private static final String FIRST_FILE_ID = "firstFileId";
    private static final String SECOND_FILE_ID = "secondFileId";

    @Autowired
    private VariantSourceRepository repository;

    @Autowired
    private TestDataLoader testDataLoader;

    @BeforeEach
    public void setUp() throws IOException {
        testDataLoader.load("/test-data/files.json");
    }

    @AfterEach
    public void tearDown() {
        testDataLoader.cleanupTestCollections();
    }

    @Test
    public void testFindAll() {
        List<VariantSourceMongo> variantSourceMongoList = repository.findAll();
        assertEquals(24, variantSourceMongoList.size());
    }

    @Test
    public void testFindByStudyIdOrStudyName() {
        List<VariantSourceMongo> variantSourceMongoList = repository.findByStudyIdOrStudyName(FIRST_STUDY_ID, FIRST_STUDY_ID);
        assertEquals(1, variantSourceMongoList.size());
        variantSourceMongoList = repository.findByStudyIdOrStudyName(SECOND_STUDY_ID, SECOND_STUDY_ID);
        assertEquals(2, variantSourceMongoList.size());
    }

    @Test
    public void testFindByStudyIdOrStudyNameTestNonExistent() {
        List<VariantSourceMongo> variantSourceMongoList = repository.findByStudyIdOrStudyName("notARealId", "notARealId");
        assertEquals(0, variantSourceMongoList.size());
    }

    @Test
    public void testFindByStudyId() {
        List<String> studyIds = new ArrayList<>();
        studyIds.add(SECOND_STUDY_ID);

        Pageable pageable = PageRequest.of(0, 1);
        List<VariantSourceMongo> variantSourceMongoList = repository.findByStudyIdIn(studyIds, pageable);
        assertEquals(1, variantSourceMongoList.size());

        pageable = PageRequest.of(0, 2);
        variantSourceMongoList = repository.findByStudyIdIn(studyIds, pageable);
        assertEquals(2, variantSourceMongoList.size());

        studyIds.add(FIRST_STUDY_ID);

        pageable = PageRequest.of(1, 2);
        variantSourceMongoList = repository.findByStudyIdIn(studyIds, pageable);
        assertEquals(1, variantSourceMongoList.size());

        pageable = PageRequest.of(2, 2);
        variantSourceMongoList = repository.findByStudyIdIn(studyIds, pageable);
        assertEquals(0, variantSourceMongoList.size());
    }

    @Test
    public void testCountByStudyIdIn() {
        List<String> studyIds = new ArrayList<>();
        studyIds.add(SECOND_STUDY_ID);

        long count = repository.countByStudyIdIn(studyIds);
        assertEquals(2, count);

        studyIds.add(FIRST_STUDY_ID);

        count = repository.countByStudyIdIn(studyIds);
        assertEquals(3, count);
    }

    @Test
    public void testFindByFileIdIn() {
        List<String> fileIds = new ArrayList<>();
        fileIds.add(FIRST_FILE_ID);

        Pageable pageable = PageRequest.of(0, 100);
        List<VariantSourceMongo> variantSourceMongoList = repository.findByFileIdIn(fileIds, pageable);
        assertEquals(1, variantSourceMongoList.size());

        for (VariantSourceMongo variantSourceMongo : variantSourceMongoList) {
            assertFalse(variantSourceMongo.getSamplesPosition().isEmpty());
            assertEquals(FIRST_FILE_ID, variantSourceMongo.getFileId());
        }
    }

    @Test
    public void testStatsNotZero() {
        List<String> fileIds = new ArrayList<>();
        fileIds.add(FIRST_FILE_ID);

        Pageable pageable = PageRequest.of(0, 100);
        List<VariantSourceMongo> variantSourceMongoList = repository.findByFileIdIn(fileIds, pageable);
        assertEquals(1, variantSourceMongoList.size());

        VariantSourceMongo variantSourceMongo = variantSourceMongoList.get(0);
        IVariantGlobalStats variantGlobalStatsMongo = variantSourceMongo.getStats();

        assertNotEquals(0, variantGlobalStatsMongo.getSamplesCount());
        assertNotEquals(0, variantGlobalStatsMongo.getVariantsCount());
        assertNotEquals(0, variantGlobalStatsMongo.getSnpsCount());
        assertNotEquals(0, variantGlobalStatsMongo.getIndelsCount());
        assertNotEquals(0, variantGlobalStatsMongo.getPassCount());
        assertNotEquals(0, variantGlobalStatsMongo.getTransitionsCount());
        assertNotEquals(0, variantGlobalStatsMongo.getTransversionsCount());
        assertNotEquals(0, variantGlobalStatsMongo.getMeanQuality());
    }

    @Test
    public void testCountByFileIdIn() {
        List<String> fileIds = new ArrayList<>();
        fileIds.add(SECOND_FILE_ID);

        long count = repository.countByFileIdIn(fileIds);
        assertEquals(1, count);

        fileIds.add(FIRST_FILE_ID);

        count = repository.countByFileIdIn(fileIds);
        assertEquals(2, count);
    }


}
