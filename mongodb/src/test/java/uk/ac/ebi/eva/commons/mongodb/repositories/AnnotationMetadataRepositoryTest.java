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

import uk.ac.ebi.eva.commons.core.models.mongodb.AnnotationMetadataMongo;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;

import java.util.List;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
@UsingDataSet(locations = {"/test-data/annotation_metadata.json"})
public class AnnotationMetadataRepositoryTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test-db");

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