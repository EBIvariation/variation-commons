package uk.ac.ebi.eva.commons.mongodb.services;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.ac.ebi.eva.commons.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.core.models.Aggregation;
import uk.ac.ebi.eva.commons.core.models.StudyType;
import uk.ac.ebi.eva.commons.core.models.VariantSource;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantGlobalStatsMongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
@UsingDataSet(locations = {"/test-data/files.json"})
public class VariantSourceServiceTest {

    private static final String TEST_DB = "test-db";

    @Autowired
    private VariantSourceService service;

    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb(TEST_DB);

    @Test
    public void testConvert() {
        List<VariantSourceMongo> variantSourceMongos = new ArrayList<>();

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
        assertNotEquals(0, variantSources.size());
    }

    @Test
    public void testFindByFileIdIn() {
        List<String> fileIds = Collections.singletonList("firstFileId");
        List<VariantSource> variantSources = service.findByFileIdIn(fileIds, new PageRequest(0, 10));
        assertNotEquals(0, variantSources.size());
    }

}
