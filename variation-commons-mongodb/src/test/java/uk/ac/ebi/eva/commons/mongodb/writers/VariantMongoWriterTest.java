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

import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.BasicDBList;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ebi.eva.commons.core.models.VariantStatistics;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.ALTERNATE_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.CHROMOSOME_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.END_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.FILES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.IDS_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.REFERENCE_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.START_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.STATISTICS_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.ALTERNATES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.ATTRIBUTES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.FILEID_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.FORMAT_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.SAMPLES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.STUDYID_FIELD;

/**
 * Testing {@link VariantMongoWriter}
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
public class VariantMongoWriterTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test");

    private final String collectionName = "variants";

    @Autowired
    private MongoOperations mongoOperations;

    @Before
    public void setUp() throws Exception {
        mongoOperations.dropCollection(collectionName);
    }

    @After
    public void tearDown() throws Exception {
        mongoOperations.dropCollection(collectionName);
    }

    @Test
    public void noVariantsNothingShouldBeWritten() throws UnknownHostException {
        DBCollection dbCollection = mongoOperations.getCollection(collectionName);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, false);
        variantMongoWriter.doWrite(emptyList());

        assertEquals(0, dbCollection.count());
    }

    @Test
    public void variantsShouldBeWrittenIntoMongoDb() throws Exception {
        Variant variant1 = new Variant("1", 1, 2, "A", "T");
        Variant variant2 = new Variant("2", 3, 4, "C", "G");

        DBCollection dbCollection = mongoOperations.getCollection(collectionName);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, false);
        variantMongoWriter.write(Collections.singletonList(variant1));
        variantMongoWriter.write(Collections.singletonList(variant2));

        assertEquals(2, dbCollection.count());
    }

    @Test
    public void indexesShouldBeCreatedInBackground() throws UnknownHostException {
        DBCollection dbCollection = mongoOperations.getCollection(collectionName);

        new VariantMongoWriter(collectionName, mongoOperations, false, false);

        List<DBObject> indexInfo = dbCollection.getIndexInfo();

        Set<String> createdIndexes = indexInfo.stream().map(index -> index.get("name").toString())
                                              .collect(Collectors.toSet());
        Set<String> expectedIndexes = new HashSet<>();
        expectedIndexes.addAll(Arrays.asList("annot.xrefs_1", "files.sid_1_files.fid_1", "chr_1_start_1_end_1",
                                             "annot.so_1", "_id_", "ids_1"));
        assertEquals(expectedIndexes, createdIndexes);

        indexInfo.stream().filter(index -> !("_id_".equals(index.get("name").toString())))
                 .forEach(index -> assertEquals("true", index.get(VariantMongoWriter.BACKGROUND_INDEX).toString()));

    }

    @Test
    public void writeTwiceSameVariantShouldUpdate() throws Exception {
        Variant variant1 = new Variant("1", 1, 2, "A", "T");
        variant1.addSourceEntry(new VariantSourceEntry("test_file", "test_study_id"));

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, false);
        variantMongoWriter.write(Collections.singletonList(variant1));

        variantMongoWriter.write(Collections.singletonList(variant1));
        DBCollection dbCollection = mongoOperations.getCollection(collectionName);
        assertEquals(1, dbCollection.count());
        final DBObject storedVariant = dbCollection.findOne();
        assertEquals(1, ((BasicDBList) storedVariant.get(FILES_FIELD)).size());
    }

    @Test
    public void allFieldsOfVariantShouldBeStored() throws Exception {
        final String chromosome = "12";
        final long start = 3;
        final long end = 4;
        final String reference = "A";
        final String alternate = "T";
        final String fileId = "fileId";
        final String studyId = "studyId";
        Variant variant = buildVariant(chromosome, start, end, reference, alternate, fileId, studyId);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        DBCollection dbCollection = mongoOperations.getCollection(collectionName);
        assertEquals(1, dbCollection.count());
        final DBObject storedVariant = dbCollection.findOne();
        final BasicDBList variantSources = (BasicDBList) storedVariant.get(FILES_FIELD);
        assertNotNull(variantSources);
        assertFalse(variantSources.isEmpty());
        assertEquals(fileId, ((DBObject) variantSources.get(0)).get(FILEID_FIELD));
        assertEquals(studyId, ((DBObject) variantSources.get(0)).get(STUDYID_FIELD));
        assertEquals(String.format("%s_%s_%s_%s", chromosome, start, reference, alternate), storedVariant.get("_id"));
        assertEquals(chromosome, storedVariant.get(CHROMOSOME_FIELD));
        assertEquals(start, storedVariant.get(START_FIELD));
        assertEquals(end, storedVariant.get(END_FIELD));
        assertEquals(reference, storedVariant.get(REFERENCE_FIELD));
        assertEquals(alternate, storedVariant.get(ALTERNATE_FIELD));
    }

    @Test
    public void includeStatsTrueShouldIncludeStatistics() throws Exception {
        Variant variant = buildVariant("12", 3, 4, "A", "T", "fileId", "studyId");

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, true, false);
        variantMongoWriter.write(Collections.singletonList(variant));

        DBCollection dbCollection = mongoOperations.getCollection(collectionName);
        assertEquals(1, dbCollection.count());
        final DBObject storedVariant = dbCollection.findOne();
        assertNotNull(storedVariant.get(STATISTICS_FIELD));
    }

    @Test
    public void includeStatsFalseShouldNotIncludeStatistics() throws Exception {
        Variant variant = buildVariant("12", 3, 4, "A", "T", "fileId", "studyId");

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        DBCollection dbCollection = mongoOperations.getCollection(collectionName);
        assertEquals(1, dbCollection.count());
        final DBObject storedVariant = dbCollection.findOne();
        assertNull(storedVariant.get(STATISTICS_FIELD));
    }

    @Test
    public void idsIfPresentShouldBeWrittenIntoTheVariant() throws Exception {
        Variant variant = buildVariant("12", 3, 4, "A", "T", "fileId", "studyId");
        variant.setIds(new HashSet<>(Arrays.asList("a", "b", "c")));

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        DBCollection dbCollection = mongoOperations.getCollection(collectionName);
        assertEquals(1, dbCollection.count());
        final DBObject storedVariant = dbCollection.findOne();
        assertNotNull(storedVariant.get(IDS_FIELD));
    }

    @Test
    public void idsIfNotPresentShouldNotBeWrittenIntoTheVariant() throws Exception {
        Variant variant = buildVariant("12", 3, 4, "A", "T", "fileId", "studyId");

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        DBCollection dbCollection = mongoOperations.getCollection(collectionName);
        assertEquals(1, dbCollection.count());
        final DBObject storedVariant = dbCollection.findOne();
        assertNull(storedVariant.get(IDS_FIELD));
    }

    @Test
    public void sourceEntryShouldBeWritten() throws Exception {
        final String chromosome = "12";
        final long start = 3;
        final long end = 4;
        final String reference = "A";
        final String alternate = "T";
        final String fileId = "fileId";
        final String studyId = "studyId";
        Variant variant = buildVariantWithSourceEntry(chromosome, start, end, reference, alternate, fileId, studyId);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        DBCollection dbCollection = mongoOperations.getCollection(collectionName);
        assertEquals(1, dbCollection.count());
        final DBObject storedVariant = dbCollection.findOne();
        final BasicDBList variantSources = (BasicDBList) storedVariant.get(FILES_FIELD);
        assertNotNull(variantSources);
        assertFalse(variantSources.isEmpty());
        DBObject sourceEntry = (DBObject) variantSources.get(0);
        assertEquals(fileId, sourceEntry.get(FILEID_FIELD));
        assertEquals(studyId, sourceEntry.get(STUDYID_FIELD));
        assertNotNull(sourceEntry.get(ALTERNATES_FIELD));
        assertNotNull(sourceEntry.get(ATTRIBUTES_FIELD));
        assertNotNull(sourceEntry.get(FORMAT_FIELD));
        assertNotNull(sourceEntry.get(SAMPLES_FIELD));
    }

    @Test
    public void sourceEntryShouldBeAdded() throws Exception {
        Variant variant = buildVariantWithSourceEntry("1", 1, 2, "A", "T", "fileId", "previousStudy");
        Variant newVariant = buildVariantWithSourceEntry("1", 1, 2, "A", "T", "fileId", "dbsnpStudy");

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(collectionName, mongoOperations, false, false);
        variantMongoWriter.write(Collections.singletonList(variant));

        variantMongoWriter.write(Collections.singletonList(newVariant));
        DBCollection dbCollection = mongoOperations.getCollection(collectionName);
        assertEquals(1, dbCollection.count());
        final DBObject storedVariant = dbCollection.findOne();
        assertEquals(2, ((BasicDBList) storedVariant.get(FILES_FIELD)).size());
    }

    private Variant buildVariant(String chromosome, long start, long end, String reference, String alternate,
                                 String fileId, String studyId) {
        return buildVariant(chromosome, start, end, reference, alternate, new VariantSourceEntry(fileId, studyId));
    }

    private Variant buildVariantWithSourceEntry(String chromosome, long start, long end, String reference, String alternate,
                                                String fileId, String studyId) {
        String[] secondaryAlternates = new String[]{alternate + "A"};
        String format = "GT:AD:DP:GQ:PL:PP";
        Map<String, String> attributes = new HashMap<>();
        attributes.put("FILTER", "PASS");
        List<Map<String, String>> samplesData = new ArrayList<>();
        HashMap<String, String> firstSampleData = new HashMap<>();
        firstSampleData.put("GT", "0/0");
        HashMap<String, String> secondSampleData = new HashMap<>();
        secondSampleData.put("GT", "0/1");
        samplesData.add(firstSampleData);
        samplesData.add(secondSampleData);
        VariantSourceEntry variantSourceEntry = new VariantSourceEntry(fileId, studyId, secondaryAlternates,
                                                                       format, null, attributes, samplesData);
        return buildVariant(chromosome, start, end, reference, alternate, variantSourceEntry);
    }

    private Variant buildVariant(String chromosome, long start, long end, String reference, String alternate,
                                 VariantSourceEntry variantSourceEntry) {
        Variant variant = new Variant(chromosome, start, end, reference, alternate);
        variantSourceEntry.setCohortStats("cohortStats", new VariantStatistics(reference, alternate, VariantType.SNV));
        variant.addSourceEntry(variantSourceEntry);

        return variant;
    }

}
