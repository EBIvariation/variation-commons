package uk.ac.ebi.eva.commons.mongodb.services;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.ebi.eva.commons.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.core.models.Aggregation;
import uk.ac.ebi.eva.commons.core.models.StudyType;
import uk.ac.ebi.eva.commons.core.models.VariantSource;
import uk.ac.ebi.eva.commons.core.models.stats.VariantGlobalStats;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantGlobalStatsMongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
@UsingDataSet(locations = {"/test-data/files.json"})
public class VariantSourceServiceTest {

    private static final String FIRST_STUDY_ID = "firstStudyId";
    private static final String SECOND_STUDY_ID = "secondStudyId";

    private static final String FIRST_FILE_ID = "firstFileId";
    private static final String SECOND_FILE_ID = "secondFileId";

    private static final String TEST_DB = "test-db";

    @Autowired
    private VariantSourceService service;

    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb(TEST_DB);

    @Test
    public void testConvert() {
        String fileId = "fileId";
        String fileName = "fileName";
        String studyId = "studyId";
        String studyName = "studyName";
        StudyType type = StudyType.CONTROL;
        Aggregation aggregation = Aggregation.NONE;
        Date date = new Date(1);
        Map<String, Integer> samplesPosition = new HashMap<>();
        Map<String, Object> metadata = new HashMap<>();
        VariantGlobalStatsMongo stats = new VariantGlobalStatsMongo(1, 1, 1, 0, 0, 1, 1, 0, 1);

        VariantSourceMongo variantSourceMongo = new VariantSourceMongo(fileId, fileName, studyId, studyName, type,
                                                                       aggregation, samplesPosition, metadata, stats);
        VariantSource variantSource = new VariantSource(fileId, fileName, studyId, studyName, type, aggregation, date,
                                                        samplesPosition, metadata, stats);

        variantSourceMongo.setDate(variantSource.getDate());

        List<VariantSourceMongo> variantSourceMongos = new ArrayList<>();
        variantSourceMongos.add(variantSourceMongo);
        List<VariantSource> variantSources = service.convert(variantSourceMongos);

        assertEquals(variantSource, variantSources.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testConvertNull() {
        service.convert(null);
    }

    @Test
    public void testFindAll() {
        List<VariantSource> variantSources = service.findAll();
        assertEquals(24, variantSources.size());
    }

    @Test
    public void testFindByStudyIdOrStudyName() {
        List<VariantSource> variantSourceMongoList = service.findByStudyIdOrStudyName(FIRST_STUDY_ID, FIRST_STUDY_ID);
        assertEquals(1, variantSourceMongoList.size());
        assertEquals(FIRST_STUDY_ID, variantSourceMongoList.get(0).getStudyId());
        variantSourceMongoList = service.findByStudyIdOrStudyName(SECOND_STUDY_ID, SECOND_STUDY_ID);
        assertEquals(SECOND_STUDY_ID, variantSourceMongoList.get(0).getStudyId());
        assertEquals(2, variantSourceMongoList.size());
    }

    @Test
    public void testFindByStudyIdOrStudyNameTestNonExistent() {
        List<VariantSource> variantSourceMongoList = service.findByStudyIdOrStudyName("notARealId", "notARealId");
        assertEquals(0, variantSourceMongoList.size());
    }

    @Test
    public void testFindByStudyId() {
        List<String> studyIds = new ArrayList<>();
        studyIds.add(SECOND_STUDY_ID);

        Pageable pageable = new PageRequest(0, 1);
        List<VariantSource> variantSourceMongoList = service.findByStudyIdIn(studyIds, pageable);
        assertEquals(1, variantSourceMongoList.size());

        pageable = new PageRequest(0, 2);
        variantSourceMongoList = service.findByStudyIdIn(studyIds, pageable);
        assertEquals(2, variantSourceMongoList.size());

        studyIds.add(FIRST_STUDY_ID);

        pageable = new PageRequest(1, 2);
        variantSourceMongoList = service.findByStudyIdIn(studyIds, pageable);
        assertEquals(1, variantSourceMongoList.size());

        pageable = new PageRequest(2, 2);
        variantSourceMongoList = service.findByStudyIdIn(studyIds, pageable);
        assertEquals(0, variantSourceMongoList.size());
    }

    @Test
    public void testCountByStudyIdIn() {
        List<String> studyIds = new ArrayList<>();
        studyIds.add(SECOND_STUDY_ID);

        long count = service.countByStudyIdIn(studyIds);
        assertEquals(2, count);

        studyIds.add(FIRST_STUDY_ID);

        count = service.countByStudyIdIn(studyIds);
        assertEquals(3, count);
    }

    @Test
    public void testFindByFileIdIn() {
        List<String> fileIds = new ArrayList<>();
        fileIds.add(FIRST_FILE_ID);

        Pageable pageable = new PageRequest(0, 100);
        List<VariantSource> variantSourceMongoList = service.findByFileIdIn(fileIds, pageable);
        assertEquals(1, variantSourceMongoList.size());

        for (VariantSource variantSourceMongo : variantSourceMongoList) {
            assertFalse(variantSourceMongo.getSamplesPosition().isEmpty());
            assertEquals(FIRST_FILE_ID, variantSourceMongo.getFileId());
        }
    }

    @Test
    public void testStatsNotZero() {
        List<String> fileIds = new ArrayList<>();
        fileIds.add(FIRST_FILE_ID);

        Pageable pageable = new PageRequest(0, 100);
        List<VariantSource> variantSourceList = service.findByFileIdIn(fileIds, pageable);
        assertEquals(1, variantSourceList.size());

        VariantSource variantSource = variantSourceList.get(0);
        VariantGlobalStats variantGlobalStats = variantSource.getStats();

        assertNotEquals(0, variantGlobalStats.getSamplesCount());
        assertNotEquals(0, variantGlobalStats.getVariantsCount());
        assertNotEquals(0, variantGlobalStats.getSnpsCount());
        assertNotEquals(0, variantGlobalStats.getIndelsCount());
        assertNotEquals(0, variantGlobalStats.getPassCount());
        assertNotEquals(0, variantGlobalStats.getTransitionsCount());
        assertNotEquals(0, variantGlobalStats.getTransversionsCount());
        assertNotEquals(0, variantGlobalStats.getMeanQuality());
    }

    @Test
    public void testCountByFileIdIn() {
        List<String> fileIds = new ArrayList<>();
        fileIds.add(SECOND_FILE_ID);

        long count = service.countByFileIdIn(fileIds);
        assertEquals(1, count);

        fileIds.add(FIRST_FILE_ID);

        count = service.countByFileIdIn(fileIds);
        assertEquals(2, count);
    }

}
