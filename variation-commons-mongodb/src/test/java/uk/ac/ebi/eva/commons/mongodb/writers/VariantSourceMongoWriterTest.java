/*
 * Copyright 2015-2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.eva.commons.mongodb.writers;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import uk.ac.ebi.eva.commons.core.models.Aggregation;
import uk.ac.ebi.eva.commons.core.models.StudyType;

import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantGlobalStatsMongo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.concurrent.ThreadLocalRandom;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * {@link VariantSourceMongoWriter}
 * input: a VariantSourceMongo object
 * output: the VariantSourceMongo object gets written in mongo, with at least: fname, fid, sid, sname, samp, meta, stype,
 * date, aggregation. Stats are not there because those are written by the statistics job.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
public class VariantSourceMongoWriterTest {

    private static final String COLLECTION_FILES_NAME = "files";

    private static final String FILE_ID = "1";

    private static final String STUDY_ID = "1";

    private static final String STUDY_NAME = "small";

    private static final StudyType STUDY_TYPE = StudyType.COLLECTION;

    private static final Aggregation AGGREGATION = Aggregation.NONE;

    @Autowired
    private MongoOperations mongoOperations;

    @Before
    public void setUp() throws Exception {
        mongoOperations.dropCollection(COLLECTION_FILES_NAME);
    }

    @After
    public void tearDown() throws Exception {
        mongoOperations.dropCollection(COLLECTION_FILES_NAME);
    }

    @Test
    public void shouldWriteAllFieldsIntoMongoDb() throws Exception {
        DBCollection fileCollection = mongoOperations.getCollection (COLLECTION_FILES_NAME);
        VariantSourceMongoWriter filesWriter = new VariantSourceMongoWriter(
                mongoOperations, COLLECTION_FILES_NAME);

        VariantSourceMongo variantSource = getVariantSource();
        filesWriter.write(Collections.singletonList(variantSource));

        DBCursor cursor = fileCollection.find();
        int count = 0;

        while (cursor.hasNext()) {
            count++;
            DBObject next = cursor.next();
            assertNotNull(next.get(VariantSourceMongo.FILEID_FIELD));
            assertNotNull(next.get(VariantSourceMongo.FILENAME_FIELD));
            assertNotNull(next.get(VariantSourceMongo.STUDYID_FIELD));
            assertNotNull(next.get(VariantSourceMongo.STUDYNAME_FIELD));
            assertNotNull(next.get(VariantSourceMongo.STUDYTYPE_FIELD));
            assertNotNull(next.get(VariantSourceMongo.AGGREGATION_FIELD));
            assertNotNull(next.get(VariantSourceMongo.SAMPLES_FIELD));
            assertNotNull(next.get(VariantSourceMongo.DATE_FIELD));

            DBObject meta = (DBObject) next.get(VariantSourceMongo.METADATA_FIELD);
            assertNotNull(meta);
            assertNotNull(meta.get("ALT"));
            assertNotNull(meta.get("FILTER"));
            assertNotNull(meta.get("INFO"));
            assertNotNull(meta.get("FORMAT"));
        }
        assertEquals(1, count);
    }

    @Test
    public void shouldWriteSamplesWithDotsInName() throws Exception {
        DBCollection fileCollection = mongoOperations.getCollection(COLLECTION_FILES_NAME);

        VariantSourceMongoWriter filesWriter = new VariantSourceMongoWriter(
                mongoOperations, COLLECTION_FILES_NAME);

        VariantSourceMongo variantSource = getVariantSource();
        Map<String, Integer> samplesPosition = new HashMap<>();
        samplesPosition.put("EUnothing", 1);
        samplesPosition.put("NA.dot", 2);
        samplesPosition.put("JP-dash", 3);
        variantSource.setSamplesPosition(samplesPosition);

        filesWriter.write(Collections.singletonList(variantSource));

        DBCursor cursor = fileCollection.find();

        while (cursor.hasNext()) {
            DBObject next = cursor.next();
            DBObject samples = (DBObject) next.get(VariantSourceMongo.SAMPLES_FIELD);
            Set<String> keySet = samples.keySet();

            Set<String> expectedKeySet = new TreeSet<>(Arrays.asList("EUnothing", "NA£dot", "JP-dash"));
            assertEquals(expectedKeySet, keySet);
        }
    }

    @Test
    public void shouldCreateUniqueFileIndex() throws Exception {
        DBCollection fileCollection = mongoOperations.getCollection (COLLECTION_FILES_NAME);
        VariantSourceMongoWriter filesWriter = new VariantSourceMongoWriter( mongoOperations, COLLECTION_FILES_NAME);

        VariantSourceMongo variantSource = getVariantSource();
        filesWriter.write(Collections.singletonList(variantSource));

        List<DBObject> indexInfo = fileCollection.getIndexInfo();

        Set<String> createdIndexes = indexInfo.stream().map(index -> index.get("name").toString())
                .collect(Collectors.toSet());
        Set<String> expectedIndexes = new HashSet<>();
        expectedIndexes.addAll(Arrays.asList(VariantSourceMongoWriter.UNIQUE_FILE_INDEX_NAME, "_id_"));
        assertEquals(expectedIndexes, createdIndexes);

        DBObject uniqueIndex = indexInfo.stream().filter(
                index -> (VariantSourceMongoWriter.UNIQUE_FILE_INDEX_NAME.equals(index.get("name").toString())))
                .findFirst().get();
        assertNotNull(uniqueIndex);
        assertEquals("true", uniqueIndex.get(VariantSourceMongoWriter.UNIQUE_INDEX).toString());
        assertEquals("true", uniqueIndex.get(VariantSourceMongoWriter.BACKGROUND_INDEX).toString());
    }

    private VariantSourceMongo getVariantSource() throws Exception {
        Map<String, Integer> samplesPosition = new HashMap<>();
        samplesPosition.put("sample0", 0);
        samplesPosition.put("sample1", 1);
        samplesPosition.put("sample2", 2);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("fileformat", "VCFv4.1");
        metadata.put("source", "1000GenomesPhase3Pipeline");
        metadata.put("header", "##fileformat=VCFv4.1");
        metadata.put("ALT", "<ID=CNV:124,Description=\"Copy number allele: 124 copies\">");
        metadata.put("FILTER", "All filters passed");
        metadata.put("INFO", "INFO field");
        metadata.put("FORMAT", "FORMAT field");
        return new VariantSourceMongo(FILE_ID, "CHICKEN_SNPS_LAYER", STUDY_ID, STUDY_NAME, STUDY_TYPE,
                AGGREGATION, samplesPosition, metadata,
                new VariantGlobalStatsMongo(0,0,0, 0,
                        0, 0, 0, 0, 0));
    }
}
