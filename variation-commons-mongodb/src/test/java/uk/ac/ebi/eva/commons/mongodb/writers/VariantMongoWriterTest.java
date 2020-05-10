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

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ebi.eva.commons.core.models.VariantStatistics;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.test.rule.FixSpringMongoDbRule;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.ALTERNATE_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.CHROMOSOME_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.DBSNP_IDS_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.END_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.FILES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.IDS_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.MAIN_ID_FIELD;
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
@TestPropertySource("classpath:eva.properties")
@UsingDataSet(locations = {
        "/test-data/annotation_metadata.json",
        "/test-data/annotations.json",
        "/test-data/features.json",
        "/test-data/files.json",
        "/test-data/variants.json"})
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class VariantMongoWriterTest {

    private static final String TEST_DB = "test-db";

    private static final String MAIN_ID = "b";

    private static final HashSet<String> IDS = new HashSet<>(Arrays.asList("a", MAIN_ID, "c"));

    private static final HashSet<String> DBSNP_IDS = new HashSet<>(Arrays.asList("d", MAIN_ID, "e"));

    private static final String COLLECTION_NAME = "variants";

    //Required by nosql-unit
    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = new FixSpringMongoDbRule(
            MongoDbConfigurationBuilder.mongoDb().databaseName(TEST_DB).build());

    @Autowired
    private MongoOperations mongoOperations;

    @Before
    public void setUp() throws Exception {
        mongoOperations.dropCollection(COLLECTION_NAME);
    }

    @After
    public void tearDown() throws Exception {
        mongoOperations.dropCollection(COLLECTION_NAME);
    }

    @Test
    public void noVariantsNothingShouldBeWritten() throws UnknownHostException {
        MongoCollection dbCollection = mongoOperations.getCollection(COLLECTION_NAME);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, false);
        variantMongoWriter.doWrite(emptyList());

        assertEquals(0, dbCollection.count());
    }

    @Test
    public void variantsShouldBeWrittenIntoMongoDb() throws Exception {
        Variant variant1 = new Variant("1", 1, 2, "A", "T");
        Variant variant2 = new Variant("2", 3, 4, "C", "G");

        MongoCollection dbCollection = mongoOperations.getCollection(COLLECTION_NAME);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, false);
        variantMongoWriter.write(Collections.singletonList(variant1));
        variantMongoWriter.write(Collections.singletonList(variant2));

        assertEquals(2, dbCollection.count());
    }

    @Test
    public void indexesShouldBeCreatedInBackground() throws UnknownHostException {
        MongoCollection dbCollection = mongoOperations.getCollection(COLLECTION_NAME);

        new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, false);

        ListIndexesIterable<Document> indexesInfo = dbCollection.listIndexes();

        Set<String> createdIndexes = StreamSupport.stream(
                indexesInfo.map(index -> index.get("name").toString()).spliterator(), false).collect(
                Collectors.toSet());
        Set<String> expectedIndexes = new HashSet<>();
        expectedIndexes.addAll(Arrays.asList("annot.xrefs_1", "files.sid_1_files.fid_1", "chr_1_start_1_end_1",
                                             "annot.so_1", "_id_", "ids_1"));
        assertEquals(expectedIndexes, createdIndexes);

        for(Document indexInfo: indexesInfo) {
            if (!("_id_".equals(indexInfo.get("name").toString()))) {
                assertEquals("true", indexInfo.get(VariantMongoWriter.BACKGROUND_INDEX).toString());
            }
        }
    }

    @Test
    public void writeTwiceSameVariantShouldUpdate() throws Exception {
        Variant variant1 = new Variant("1", 1, 2, "A", "T");
        variant1.addSourceEntry(new VariantSourceEntry("test_file", "test_study_id"));

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, false);
        variantMongoWriter.write(Collections.singletonList(variant1));

        variantMongoWriter.write(Collections.singletonList(variant1));
        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertEquals(1, ((List<Document>) storedVariant.get(FILES_FIELD)).size());
    }

    private Document assertThereIsOnlyOneDocumentAndReturnIt() {
        MongoCollection<Document> dbCollection = mongoOperations.getCollection(COLLECTION_NAME);
        assertEquals(1, dbCollection.count());
        return dbCollection.find().first();
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
        Variant variant = buildVariantWithStats(chromosome, start, end, reference, alternate, fileId, studyId);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        final List<Document> variantSources = (List<Document>) storedVariant.get(FILES_FIELD);
        assertNotNull(variantSources);
        assertFalse(variantSources.isEmpty());
        assertEquals(fileId, variantSources.get(0).get(FILEID_FIELD));
        assertEquals(studyId, variantSources.get(0).get(STUDYID_FIELD));
        assertEquals(String.format("%s_%s_%s_%s", chromosome, start, reference, alternate), storedVariant.get("_id"));
        assertEquals(chromosome, storedVariant.get(CHROMOSOME_FIELD));
        assertEquals(start, storedVariant.get(START_FIELD));
        assertEquals(end, storedVariant.get(END_FIELD));
        assertEquals(reference, storedVariant.get(REFERENCE_FIELD));
        assertEquals(alternate, storedVariant.get(ALTERNATE_FIELD));
    }

    @Test
    public void includeStatsTrueShouldIncludeStatistics() throws Exception {
        Variant variant = buildVariantWithStats("12", 3, 4, "A", "T", "fileId", "studyId");

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, true, false);
        variantMongoWriter.write(Collections.singletonList(variant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertNotNull(storedVariant.get(STATISTICS_FIELD));
    }

    @Test
    public void includeStatsFalseShouldNotIncludeStatistics() throws Exception {
        Variant variant = buildVariantWithStats("12", 3, 4, "A", "T", "fileId", "studyId");

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, false);
        variantMongoWriter.write(Collections.singletonList(variant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertNull(storedVariant.get(STATISTICS_FIELD));
    }

    @Test
    public void idsIfPresentShouldBeWrittenIntoTheVariant() throws Exception {
        Variant variant = buildVariantWithStats("12", 3, 4, "A", "T", "fileId", "studyId");
        variant.setIds(IDS);
        variant.setMainId(MAIN_ID);
        variant.setDbsnpIds(DBSNP_IDS);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertDocumentListEquals(IDS, (List<Document>) storedVariant.get(IDS_FIELD));
        assertDocumentListEquals(DBSNP_IDS, (List<Document>) storedVariant.get(DBSNP_IDS_FIELD));
        assertEquals(MAIN_ID, storedVariant.get(MAIN_ID_FIELD));
    }

    private void assertDocumentListEquals(Collection<String> expected, List<Document> actual) {
        for (String expectedElement : expected) {
            assertTrue(actual.contains(expectedElement));
        }
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void idsIfNotPresentShouldNotBeWrittenIntoTheVariant() throws Exception {
        Variant variant = buildVariantWithStats("12", 3, 4, "A", "T", "fileId", "studyId");

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertNull(storedVariant.get(IDS_FIELD));
        assertNull(storedVariant.get(MAIN_ID_FIELD));
        assertNull(storedVariant.get(DBSNP_IDS_FIELD));
    }

    @Test
    public void writeOnlySubmittedIds() throws Exception {
        Variant variant = buildVariantWithStats("12", 3, 4, "A", "T", "fileId", "studyId");
        variant.setIds(IDS);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertDocumentListEquals(IDS, (List<Document>) storedVariant.get(IDS_FIELD));
        assertNull(storedVariant.get(MAIN_ID_FIELD));
        assertNull(storedVariant.get(DBSNP_IDS_FIELD));
    }

    @Test
    public void writeOnlyDbsnpIds() throws Exception {
        Variant variant = buildVariantWithStats("12", 3, 4, "A", "T", "fileId", "studyId");
        variant.setMainId(MAIN_ID);
        variant.setDbsnpIds(DBSNP_IDS);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertNull(storedVariant.get(IDS_FIELD));
        assertDocumentListEquals(DBSNP_IDS, (List<Document>) storedVariant.get(DBSNP_IDS_FIELD));
        assertEquals(MAIN_ID, storedVariant.get(MAIN_ID_FIELD));
    }

    @Test
    public void updateWithDbsnpIds() throws Exception {
        Variant variant = buildVariantWithStats("12", 3, 4, "A", "T", "fileId", "studyId");
        variant.setIds(IDS);

        Variant updatedDbsnpVariant = buildVariantWithStats("12", 3, 4, "A", "T", "fileId", "studyId");
        updatedDbsnpVariant.setMainId(MAIN_ID);
        updatedDbsnpVariant.setDbsnpIds(DBSNP_IDS);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));
        variantMongoWriter.write(Collections.singletonList(updatedDbsnpVariant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertDocumentListEquals(IDS, (List<Document>) storedVariant.get(IDS_FIELD));
        assertDocumentListEquals(DBSNP_IDS, (List<Document>) storedVariant.get(DBSNP_IDS_FIELD));
        assertEquals(MAIN_ID, storedVariant.get(MAIN_ID_FIELD));
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
        Variant variant = buildVariantWithSampleData(chromosome, start, end, reference, alternate, fileId, studyId);

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, true);
        variantMongoWriter.write(Collections.singletonList(variant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        final List<Document> variantSources = (List<Document>) storedVariant.get(FILES_FIELD);
        assertNotNull(variantSources);
        assertFalse(variantSources.isEmpty());
        Document sourceEntry = (Document) variantSources.get(0);
        assertEquals(fileId, sourceEntry.get(FILEID_FIELD));
        assertEquals(studyId, sourceEntry.get(STUDYID_FIELD));
        assertNotNull(sourceEntry.get(ALTERNATES_FIELD));
        assertNotNull(sourceEntry.get(ATTRIBUTES_FIELD));
        assertNotNull(sourceEntry.get(FORMAT_FIELD));
        assertNotNull(sourceEntry.get(SAMPLES_FIELD));
    }

    @Test
    public void sourceEntryShouldBeAdded() throws Exception {
        Variant variant = buildVariantWithSampleData("1", 1, 2, "A", "T", "fileId", "previousStudy");
        Variant newVariant = buildVariantWithSampleData("1", 1, 2, "A", "T", "fileId", "dbsnpStudy");

        VariantMongoWriter variantMongoWriter = new VariantMongoWriter(COLLECTION_NAME, mongoOperations, false, false);
        variantMongoWriter.write(Collections.singletonList(variant));
        variantMongoWriter.write(Collections.singletonList(newVariant));

        Document storedVariant = assertThereIsOnlyOneDocumentAndReturnIt();
        assertEquals(2, ((List<Document>) storedVariant.get(FILES_FIELD)).size());
    }

    private Variant buildVariantWithStats(String chromosome, long start, long end, String reference, String alternate,
                                          String fileId, String studyId) {
        return buildVariantWithStats(chromosome, start, end, reference, alternate, new VariantSourceEntry(fileId, studyId));
    }

    private Variant buildVariantWithSampleData(String chromosome, long start, long end, String reference, String alternate,
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
        return buildVariantWithStats(chromosome, start, end, reference, alternate, variantSourceEntry);
    }

    private Variant buildVariantWithStats(String chromosome, long start, long end, String reference, String alternate,
                                          VariantSourceEntry variantSourceEntry) {
        Variant variant = new Variant(chromosome, start, end, reference, alternate);
        variantSourceEntry.setCohortStats("cohortStats", new VariantStatistics(reference, alternate, VariantType.SNV));
        variant.addSourceEntry(variantSourceEntry);

        return variant;
    }

}
