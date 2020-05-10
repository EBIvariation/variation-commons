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

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.SampleMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.SamplePhenotypeMongo;
import uk.ac.ebi.eva.commons.mongodb.test.rule.FixSpringMongoDbRule;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@UsingDataSet(locations = {
        "/test-data/annotation_metadata.json",
        "/test-data/annotations.json",
        "/test-data/features.json",
        "/test-data/files.json",
        "/test-data/variants.json"})
@TestPropertySource("classpath:eva.properties")
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class SampleMongoWriterTest {

    private static final String TEST_DB = "test-db";

    @Autowired
    private MongoOperations mongoOperations;

    @Value("${eva.mongo.collections.samples}")
    private String samplesCollection;

    private MongoCollection dbCollection;

    private SampleMongoWriter sampleMongoWriter;

    //Required by nosql-unit
    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = new FixSpringMongoDbRule(
            MongoDbConfigurationBuilder.mongoDb().databaseName(TEST_DB).build());

    @Before
    public void setUp() {
        dbCollection = mongoOperations.getCollection(samplesCollection);
        sampleMongoWriter = new SampleMongoWriter(mongoOperations);
    }

    @After
    public void tearDown() {
        dbCollection.drop();
    }

    @Test
    public void noSamplesNothingShouldBeWritten() throws Exception {
        sampleMongoWriter.write(emptyList());

        assertEquals(0, dbCollection.count());
    }

    @Test
    public void variantsShouldBeWrittenIntoMongoDb() throws Exception {
        SamplePhenotypeMongo phenotype1 = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1));

        SamplePhenotypeMongo phenotype2 = new SamplePhenotypeMongo("category2", "value2");
        SampleMongo sample2 = new SampleMongo("id2", "V", "father2", "mother2", buildPhenotypeSet(phenotype2));

        sampleMongoWriter.write(Arrays.asList(sample1, sample2));

        assertEquals(2, dbCollection.count());
    }

    @Test
    public void sameSampleWrittenTwiceShouldBeStoredJustOnce() throws Exception {
        SamplePhenotypeMongo phenotype1 = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1));

        sampleMongoWriter.write(Arrays.asList(sample1, sample1));

        assertEquals(1, dbCollection.count());
    }


    @Test
    public void identicalSamplesShouldBeStoredJustOnce() throws Exception {
        SamplePhenotypeMongo phenotype1 = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1));

        SamplePhenotypeMongo phenotype1b = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1b = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1b));

        sampleMongoWriter.write(Arrays.asList(sample1, sample1b));
        assertEquals(1, dbCollection.count());
    }

    @Test
    public void addTwoSamplesAndRemoveOne() throws Exception {
        SamplePhenotypeMongo phenotype1 = new SamplePhenotypeMongo("category1", "value1");
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", buildPhenotypeSet(phenotype1));

        SamplePhenotypeMongo phenotype2 = new SamplePhenotypeMongo("category2", "value2");
        SampleMongo sample2 = new SampleMongo("id2", "V", "father2", "mother2", buildPhenotypeSet(phenotype2));

        sampleMongoWriter.write(Arrays.asList(sample1, sample2));
        sampleMongoWriter.setDelete(true);
        sampleMongoWriter.write(Collections.singletonList(sample2));

        assertEquals(1, dbCollection.count());
        assertEquals("id1", ((Document)dbCollection.find().first()).get("_id"));
    }

    private Set<SamplePhenotypeMongo> buildPhenotypeSet(SamplePhenotypeMongo... phenotypes) {
        return new HashSet<>(Arrays.asList(phenotypes));
    }
}
