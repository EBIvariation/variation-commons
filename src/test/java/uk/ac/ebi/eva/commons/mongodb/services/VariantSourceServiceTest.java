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
import uk.ac.ebi.eva.commons.core.models.VariantSource;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        List<VariantSource> variantSources = service.convert(variantSourceMongos);
        assertNotNull(variantSources);

        variantSources = service.convert(null);
        assertNull(variantSources);
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
