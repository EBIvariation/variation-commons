package uk.ac.ebi.eva.commons.mongodb.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.AnnotationMetadataMongo;
import uk.ac.ebi.eva.commons.mongodb.utils.MongoTestContainerHelper;
import uk.ac.ebi.eva.commons.mongodb.utils.MongoTestDataLoader;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:eva.properties")
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class AnnotationMetadataRepositoryTest extends MongoTestContainerHelper {
    @Value("${eva.mongo.collections.annotation-metadata}")
    private String annotationMetadataCollection;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ResourceLoader resourceLoader;

    @BeforeEach
    void setUp() {
        mongoTemplate.getDb().drop();
        new MongoTestDataLoader(mongoTemplate, resourceLoader).load("/test-data/annotation_metadata.json", annotationMetadataCollection);
    }

    @AfterEach
    void cleanDb() {
        mongoTemplate.getDb().drop();
    }

    @Autowired
    private AnnotationMetadataRepository repository;

    @Test
    public void findOneByDefault() {
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