/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb;

import com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import org.bson.Document;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.ws.VariantSourceEntryWithSampleNames;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.mongodb.test.rule.FixSpringMongoDbRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.ATTRIBUTES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.FILEID_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.FORMAT_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.SAMPLES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo.STUDYID_FIELD;

@RunWith(SpringRunner.class)
@TestPropertySource({"classpath:eva.properties"})
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class VariantSourceEntryConversionTest {

    private static final String TEST_DB = "test-db";

    public static final String FILE_ID = "f1";
    public static final String STUDY_ID = "s1";
    public static final String FORMAT = "GT";
    @Autowired
    private MongoOperations mongoOperations;

    //Required by nosql-unit
    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = new FixSpringMongoDbRule(
            MongoDbConfigurationBuilder.mongoDb().databaseName(TEST_DB).build());

    @Test
    public void testConvertVariantSourceEntryWithoutStatsToMongo() {
        VariantSourceEntryMongo variantSourceEntryMongo = new VariantSourceEntryMongo(createVariantSourceEntry());
        Document converted = (Document) mongoOperations.getConverter().convertToMongoType(variantSourceEntryMongo);
        Assert.assertEquals(FILE_ID, converted.get(FILEID_FIELD));
        Assert.assertEquals(STUDY_ID, converted.get(STUDYID_FIELD));
        Assert.assertEquals(FORMAT, converted.get(FORMAT_FIELD));
        Assert.assertEquals(3, ((Document) converted.get(ATTRIBUTES_FIELD)).size());
        Assert.assertEquals(3, ((Document) converted.get(SAMPLES_FIELD)).size());
    }

    private VariantSourceEntry createVariantSourceEntry() {
        VariantSourceEntry file = new VariantSourceEntry(FILE_ID, STUDY_ID);
        file.addAttribute("QUAL", "0.01");
        file.addAttribute("AN", "2");
        file.addAttribute("MAX.PROC", "2");
        file.setFormat(FORMAT);

        Map<String, String> na001 = new HashMap<>();
        na001.put("GT", "0/0");
        file.addSampleData(na001);
        Map<String, String> na002 = new HashMap<>();
        na002.put("GT", "0/1");
        file.addSampleData(na002);
        Map<String, String> na003 = new HashMap<>();
        na003.put("GT", "1/1");
        file.addSampleData(na003);

        return file;
    }

    @Test
    public void testConvertMongoVariantSourceEntry() {
        VariantSourceEntryMongo variantSource = mongoOperations.getConverter().read(VariantSourceEntryMongo.class,
                createMongoVariantSourceEntry());
        // We cannot check equality because it has a map String object that can contain arrays. This defaults to
        // reference comparison in that case instead of checking same elements in both sides.
        Assert.assertEquals(FILE_ID, variantSource.getFileId());
        Assert.assertEquals(STUDY_ID, variantSource.getStudyId());
        Assert.assertEquals(FORMAT, variantSource.getFormat());
        Assert.assertEquals(3, variantSource.getAttributes().size());
        Assert.assertEquals(3, variantSource.getSamples().size());
    }

    private Document createMongoVariantSourceEntry() {
        Document mongoFile = new Document(FILEID_FIELD, FILE_ID)
                .append(STUDYID_FIELD, STUDY_ID);
        mongoFile.append(ATTRIBUTES_FIELD,
                         new Document("QUAL", "0.01").append("AN", "2")
                        .append("MAX" + VariantSourceEntryMongo.CHARACTER_TO_REPLACE_DOTS + "PROC", "2"));
        mongoFile.append(FORMAT_FIELD, FORMAT);
        Document genotypeCodes = new Document();
        genotypeCodes.append("def", "0/0");
        genotypeCodes.append("0/1", Arrays.asList(1));
        genotypeCodes.append("1/1", Arrays.asList(2));
        mongoFile.append(SAMPLES_FIELD, genotypeCodes);

        mongoFile.put(SAMPLES_FIELD, new Document());
        ((Document) mongoFile.get(SAMPLES_FIELD)).put("def", "0/0");
        ((Document) mongoFile.get(SAMPLES_FIELD)).put("0/1", Arrays.asList(25));
        ((Document) mongoFile.get(SAMPLES_FIELD)).put("1/1", Arrays.asList(35));

        return mongoFile;
    }

    @Test
    public void testConvertFromVariantSourceEntryMongoToVariantSourceEntry() {
        VariantSourceEntryMongo variantSourceEntryMongo = new VariantSourceEntryMongo(createVariantSourceEntry());

        List<String> sampleNames = new ArrayList<String>();
        sampleNames.add("NA001");
        sampleNames.add("NA002");
        sampleNames.add("NA003");

        VariantSourceEntry variantSourceEntry
                = new VariantSourceEntry(variantSourceEntryMongo.getFileId(), variantSourceEntryMongo.getStudyId(),
                                         variantSourceEntryMongo.getSecondaryAlternates(), variantSourceEntryMongo.getFormat(),
                                         null, variantSourceEntryMongo.getAttributes(), variantSourceEntryMongo.deflateSamplesData(sampleNames.size()));
        Assert.assertEquals(FILE_ID, variantSourceEntry.getFileId());
        Assert.assertEquals(STUDY_ID, variantSourceEntry.getStudyId());
        Assert.assertEquals(FORMAT, variantSourceEntry.getFormat());
        Assert.assertEquals(3, variantSourceEntry.getAttributes().size());
        Assert.assertEquals(3, variantSourceEntry.getSamplesData().size());

        VariantSourceEntryWithSampleNames variantSourceEntryWithSampleNames = new VariantSourceEntryWithSampleNames(
                variantSourceEntry, sampleNames);
        Assert.assertEquals(FILE_ID, variantSourceEntryWithSampleNames.getFileId());
        Assert.assertEquals(STUDY_ID, variantSourceEntryWithSampleNames.getStudyId());
        Assert.assertEquals(FORMAT, variantSourceEntryWithSampleNames.getFormat());
        Assert.assertEquals(3, variantSourceEntryWithSampleNames.getAttributes().size());
        Assert.assertEquals(3, variantSourceEntryWithSampleNames.getSamplesDataMap().size());
        Assert.assertEquals("0/0", variantSourceEntryWithSampleNames.getSamplesDataMap().get("NA001").get("GT"));
        Assert.assertEquals("0/1", variantSourceEntryWithSampleNames.getSamplesDataMap().get("NA002").get("GT"));
        Assert.assertEquals("1/1", variantSourceEntryWithSampleNames.getSamplesDataMap().get("NA003").get("GT"));
    }


}
