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

package uk.ac.ebi.eva.commons.mongodb.services;


import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.projections.VariantStudySummary;
import uk.ac.ebi.eva.commons.mongodb.test.rule.FixSpringMongoDbRule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:eva.properties")
@UsingDataSet(locations = {"/test-data/files.json"})
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class VariantStudySummaryServiceTest {

    protected static Logger logger = LoggerFactory.getLogger(VariantStudySummaryServiceTest.class);

    private static final String FIRST_STUDY_NAME = "firstStudyName";
    private static final String SECOND_STUDY_NAME = "secondStudyName";

    private static final String FIRST_STUDY_ID = "firstStudyId";
    private static final String SECOND_STUDY_ID = "secondStudyId";

    private static final String TEST_DB = "test-db";

    private static final int EXPECTED_UNIQUE_STUDIES_COUNT = 18;

    private static final long FIRST_PAGE_NUMBER = 0;
    private static final long SECOND_PAGE_NUMBER = 1;
    private static final long PAGE_SIZE = 10;
    private static final int FIRST_PAGE_EXPECTED_UNIQUE_STUDIES_COUNT = 10;
    private static final int SECOND_PAGE_EXPECTED_UNIQUE_STUDIES_COUNT = 8;

    private static final int EXPECTED_FILE_COUNT_FROM_FIRST_STUDY_ID = 1;
    private static final int EXPECTED_FILE_COUNT_FROM_SECOND_STUDY_ID = 2;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private VariantStudySummaryService service;

    @Rule
    public MongoDbRule mongoDbRule = new FixSpringMongoDbRule(
            MongoDbConfigurationBuilder.mongoDb().databaseName(TEST_DB).build());

    @Test
    public void testFindsByNameOrIdProvidingName() {
        assertFindBySecondNameOrId(SECOND_STUDY_NAME);
    }

    @Test
    public void testFindsByNameOrIdProvidingId() {
        assertFindBySecondNameOrId(SECOND_STUDY_ID);
    }

    private void assertFindBySecondNameOrId(String studyNameOrId) {
        VariantStudySummary study = service.findByStudyNameOrStudyId(studyNameOrId);
        assertNotNull(study);
        assertEquals(SECOND_STUDY_ID, study.getStudyId());
        assertEquals(SECOND_STUDY_NAME, study.getStudyName());
        assertCorrectCount(EXPECTED_FILE_COUNT_FROM_SECOND_STUDY_ID, study);
    }

    @Test
    public void testDoesntFindNonPresentStudies() throws Exception {
        VariantStudySummary study = service.findByStudyNameOrStudyId("wrongStudyId");
        assertNull(study);
    }

    @Test
    public void testListStudies() {
        List<VariantStudySummary> uniqueStudies = service.findAll();
        assertEquals(EXPECTED_UNIQUE_STUDIES_COUNT, uniqueStudies.size());
    }

    @Test
    public void testListsStudiesWithPagination() {
        assertEquals(FIRST_PAGE_EXPECTED_UNIQUE_STUDIES_COUNT, service.findAll(FIRST_PAGE_NUMBER, PAGE_SIZE).size());
        assertEquals(SECOND_PAGE_EXPECTED_UNIQUE_STUDIES_COUNT, service.findAll(SECOND_PAGE_NUMBER, PAGE_SIZE).size());
    }

    @Test
    public void testCountStudies() {
        assertEquals(EXPECTED_UNIQUE_STUDIES_COUNT, service.countAll());
    }

    @Test
    public void testListStudiesByFromDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<VariantStudySummary> allStudies = service.findByFromDate(dateFormat.parse("2015-12-31"));
        assertEquals(EXPECTED_UNIQUE_STUDIES_COUNT, allStudies.size());

        List<VariantStudySummary> studiesRightBeforeLastDate = service.findByFromDate(dateFormat.parse("2018-04-22"));
        assertEquals(1, studiesRightBeforeLastDate.size());

        List<VariantStudySummary> studiestOnLastDate = service.findByFromDate(dateFormat.parse("2018-04-23"));
        assertEquals(1, studiestOnLastDate.size());

        List<VariantStudySummary> studiesRightAfterLastDate = service.findByFromDate(dateFormat.parse("2018-04-24"));
        assertEquals(0, studiesRightAfterLastDate.size());

        int nextYear = LocalDate.now().getYear()+1;
        List<VariantStudySummary> futureStudies = service.findByFromDate(dateFormat.parse(nextYear + "-01-01"));
        assertEquals(0, futureStudies.size());
    }

    private void assertCorrectCount(int expectedFileCount, VariantStudySummary study) {
        int buggedFongoCount = 0;
        if (study.getFilesCount() == buggedFongoCount) {
            logger.warn("Although the expected files count is different from the actual one ({} != {}) " +
                    "this is a known limitation of Fongo, in a real mongo it works, " +
                    "see https://github.com/fakemongo/fongo/issues/258", expectedFileCount, study.getFilesCount());
        } else {
            assertEquals(expectedFileCount, study.getFilesCount());
        }

    }
}


