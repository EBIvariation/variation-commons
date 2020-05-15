package uk.ac.ebi.eva.commons.mongodb.repositories;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.AnnotationMetadataMongo;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.test.rule.FixSpringMongoDbRule;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:eva.properties")
@UsingDataSet(locations = {"/test-data/annotation_metadata.json"})
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class AnnotationMetadataRepositoryTest {

    private static final String TEST_DB = "test-db";

    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = new FixSpringMongoDbRule(
            MongoDbConfigurationBuilder.mongoDb().databaseName(TEST_DB).build());

    @Autowired
    private AnnotationMetadataRepository repository;

    @Test
    public void findOneByDefault() throws Exception {
        List<AnnotationMetadataMongo> annotationMetadataMongoList = repository.findByDefaultVersionTrue();
        assertEquals(1, annotationMetadataMongoList.size());
        AnnotationMetadataMongo annotationMetadataMongo = annotationMetadataMongoList.get(0);
        assertTrue(annotationMetadataMongo.isDefaultVersion());
    }

    @Test
    public void testFindByCacheVersionVepVersionExists() {
        assertEquals(1, repository.findByCacheVersionAndVepVersion("78", "78").size());
    }

    @Test
    public void testFindByCacheVersionVepVersionNotExists() {
        assertEquals(0, repository.findByCacheVersionAndVepVersion("76", "76").size());
    }

}