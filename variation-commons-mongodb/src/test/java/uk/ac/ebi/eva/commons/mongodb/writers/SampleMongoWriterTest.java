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

import com.mongodb.DBCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.CohortCatMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.SampleMongo;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
public class SampleMongoWriterTest {

    @Autowired
    private MongoOperations mongoOperations;

    @Value("${eva.mongo.collections.samples}")
    private String samplesCollection;

    private DBCollection dbCollection;

    private MongoItemWriter<SampleMongo> sampleMongoWriter;

    @Before
    public void setUp() {
        dbCollection = mongoOperations.getCollection(samplesCollection);
        sampleMongoWriter = new MongoItemWriter<>();
        sampleMongoWriter.setTemplate(mongoOperations);
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
        CohortCatMongo cohort1 = new CohortCatMongo("category1", "value1");
        Set<CohortCatMongo> cohortSet1 = new HashSet<>(Collections.singletonList(cohort1));
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", cohortSet1);

        CohortCatMongo cohort2 = new CohortCatMongo("category2", "value2");
        Set<CohortCatMongo> cohortSet2 = new HashSet<>(Collections.singletonList(cohort2));
        SampleMongo sample2 = new SampleMongo("id2", "V", "father2", "mother2", cohortSet2);

        sampleMongoWriter.write(Arrays.asList(sample1, sample2));

        assertEquals(2, dbCollection.count());
    }

    @Test
    public void sameSampleWrittenTwiceShouldBeStoredJustOnce() throws Exception {
        CohortCatMongo cohort1 = new CohortCatMongo("category1", "value1");
        Set<CohortCatMongo> cohortSet1 = new HashSet<>(Collections.singletonList(cohort1));
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", cohortSet1);

        sampleMongoWriter.write(Arrays.asList(sample1, sample1));

        assertEquals(1, dbCollection.count());
    }


    @Test
    public void identicalSamplesShouldBeStoredJustOnce() throws Exception {
        CohortCatMongo cohort1 = new CohortCatMongo("category1", "value1");
        Set<CohortCatMongo> cohortSet1 = new HashSet<>(Collections.singletonList(cohort1));
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", cohortSet1);

        CohortCatMongo cohort1b = new CohortCatMongo("category1", "value1");
        Set<CohortCatMongo> cohortSet1b = new HashSet<>(Collections.singletonList(cohort1b));
        SampleMongo sample1b = new SampleMongo("id1", "V", "father1", "mother1", cohortSet1b);

        sampleMongoWriter.write(Arrays.asList(sample1, sample1b));
        assertEquals(1, dbCollection.count());
    }

    @Test
    public void addTwoSamplesAndRemoveOne() throws Exception {
        CohortCatMongo cohort1 = new CohortCatMongo("category1", "value1");
        Set<CohortCatMongo> cohortSet1 = new HashSet<>(Collections.singletonList(cohort1));
        SampleMongo sample1 = new SampleMongo("id1", "V", "father1", "mother1", cohortSet1);

        CohortCatMongo cohort2 = new CohortCatMongo("category2", "value2");
        Set<CohortCatMongo> cohortSet2 = new HashSet<>(Collections.singletonList(cohort2));
        SampleMongo sample2 = new SampleMongo("id2", "V", "father2", "mother2", cohortSet2);

        sampleMongoWriter.write(Arrays.asList(sample1, sample2));
        sampleMongoWriter.setDelete(true);
        sampleMongoWriter.write(Collections.singletonList(sample2));

        assertEquals(1, dbCollection.count());
        assertEquals("id1", dbCollection.find().next().get("_id"));
    }
}
