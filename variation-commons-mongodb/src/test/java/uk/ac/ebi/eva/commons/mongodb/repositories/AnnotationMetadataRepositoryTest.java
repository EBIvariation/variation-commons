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
import uk.ac.ebi.eva.commons.mongodb.entities.AnnotationMetadataMongo;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.test.TestDataLoader;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:eva.properties")
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class AnnotationMetadataRepositoryTest {

    @Autowired
    private AnnotationMetadataRepository repository;

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
