/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.eva.commons.mongodb.writers;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.SampleMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.SamplePhenotypeMongo;
import uk.ac.ebi.eva.commons.mongodb.utils.MongoTestContainerHelper;
import uk.ac.ebi.eva.commons.mongodb.utils.MongoTestDataLoader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:eva.properties")
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class SampleMongoWriterTest extends MongoTestContainerHelper {
    @Value("${eva.mongo.collections.files}")
    private String fileCollection;

    @Value("${eva.mongo.collections.variants}")
    private String variantCollection;

    @Value("${eva.mongo.collections.annotations}")
    private String annotationCollection;

    @Value("${eva.mongo.collections.annotation-metadata}")
    private String annotationMetadataCollection;

    @Value("${eva.mongo.collections.features}")
    private String featureCollection;

    @Value("${eva.mongo.collections.samples}")
    private String samplesCollection;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    private MongoOperations mongoOperations;

    private MongoCollection dbCollection;

    private SampleMongoWriter sampleMongoWriter;

    @BeforeEach
    public void setUp() {
        dbCollection = mongoOperations.getCollection(samplesCollection);
        sampleMongoWriter = new SampleMongoWriter(mongoOperations);

        mongoTemplate.getDb().drop();
        new MongoTestDataLoader(mongoTemplate, resourceLoader).load("/test-data/files.json", fileCollection);
        new MongoTestDataLoader(mongoTemplate, resourceLoader).load("/test-data/variants.json", variantCollection);
        new MongoTestDataLoader(mongoTemplate, resourceLoader).load("/test-data/annotations.json", annotationCollection);
        new MongoTestDataLoader(mongoTemplate, resourceLoader).load("/test-data/annotation_metadata.json", annotationMetadataCollection);
        new MongoTestDataLoader(mongoTemplate, resourceLoader).load("/test-data/features.json", featureCollection);
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.getDb().drop();
    }

    @Test
    public void noSamplesNothingShouldBeWritten() throws Exception {
        sampleMongoWriter.write(Chunk.of());

        assertEquals(0, dbCollection.countDocuments());
    }

    @Test
    public void variantsShouldBeWrittenIntoMongoDb() throws Exception {
        SamplePhenotypeMongo phenotype1 = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1));

        SamplePhenotypeMongo phenotype2 = new SamplePhenotypeMongo("category2", "value2");
        SampleMongo sample2 = new SampleMongo("id2", "V", "father2", "mother2", buildPhenotypeSet(phenotype2));

        sampleMongoWriter.write(Chunk.of(sample1, sample2));

        assertEquals(2, dbCollection.countDocuments());
    }

    @Test
    public void sameSampleWrittenTwiceShouldBeStoredJustOnce() throws Exception {
        SamplePhenotypeMongo phenotype1 = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1));

        sampleMongoWriter.write(Chunk.of(sample1, sample1));

        assertEquals(1, dbCollection.countDocuments());
    }


    @Test
    public void identicalSamplesShouldBeStoredJustOnce() throws Exception {
        SamplePhenotypeMongo phenotype1 = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1));

        SamplePhenotypeMongo phenotype1b = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1b = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1b));

        sampleMongoWriter.write(Chunk.of(sample1, sample1b));
        assertEquals(1, dbCollection.countDocuments());
    }

    @Test
    public void addTwoSamplesAndRemoveOne() throws Exception {
        SamplePhenotypeMongo phenotype1 = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1));

        SamplePhenotypeMongo phenotype2 = new SamplePhenotypeMongo("category2", "value2");
        SampleMongo sample2 = new SampleMongo("id2", "V", "father2", "mother2", buildPhenotypeSet(phenotype2));

        sampleMongoWriter.write(Chunk.of(sample1, sample2));
        sampleMongoWriter.setDelete(true);
        sampleMongoWriter.write(Chunk.of(sample2));

        assertEquals(1, dbCollection.countDocuments());
        assertEquals("id1", ((Document) dbCollection.find().first()).get("_id"));
    }

    private Set<SamplePhenotypeMongo> buildPhenotypeSet(SamplePhenotypeMongo... phenotypes) {
        return new HashSet<>(Arrays.asList(phenotypes));
    }
}
