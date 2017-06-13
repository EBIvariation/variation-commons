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

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.eva.commons.core.models.VariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.VariantSourceEntryWithSamples;
import uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.test.configurations.MongoOperationConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantSourceEntryMongo.ATTRIBUTES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantSourceEntryMongo.FILEID_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantSourceEntryMongo.FORMAT_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantSourceEntryMongo.SAMPLES_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantSourceEntryMongo.STUDYID_FIELD;

@RunWith(SpringRunner.class)
@TestPropertySource({"classpath:test-mongo.properties"})
@ContextConfiguration(classes = {MongoOperationConfiguration.class})
public class VariantSourceEntryConversionTest {

    public static final String FILE_ID = "f1";
    public static final String STUDY_ID = "s1";
    public static final String FORMAT = "GT";
    @Autowired
    private MongoOperations mongoOperations;

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

    private BasicDBObject createMongoVariantSourceEntry() {
        BasicDBObject mongoFile = new BasicDBObject(VariantSourceEntryMongo.FILEID_FIELD, FILE_ID)
                .append(VariantSourceEntryMongo.STUDYID_FIELD, STUDY_ID);
        mongoFile.append(VariantSourceEntryMongo.ATTRIBUTES_FIELD,
                new BasicDBObject("QUAL", "0.01").append("AN", "2")
                        .append("MAX" + VariantSourceEntryMongo.CHARACTER_TO_REPLACE_DOTS + "PROC", "2"));
        mongoFile.append(VariantSourceEntryMongo.FORMAT_FIELD, FORMAT);
        BasicDBObject genotypeCodes = new BasicDBObject();
        genotypeCodes.append("def", "0/0");
        genotypeCodes.append("0/1", Arrays.asList(1));
        genotypeCodes.append("1/1", Arrays.asList(2));
        mongoFile.append(VariantSourceEntryMongo.SAMPLES_FIELD, genotypeCodes);

        mongoFile.put(VariantSourceEntryMongo.SAMPLES_FIELD, new BasicDBObject());
        ((DBObject) mongoFile.get(VariantSourceEntryMongo.SAMPLES_FIELD)).put("def", "0/0");
        ((DBObject) mongoFile.get(VariantSourceEntryMongo.SAMPLES_FIELD)).put("0/1", Arrays.asList(25));
        ((DBObject) mongoFile.get(VariantSourceEntryMongo.SAMPLES_FIELD)).put("1/1", Arrays.asList(35));

        return mongoFile;
    }

    @Test
    public void testConvertVariantSourceEntryWithoutStatsToMongo() {
        VariantSourceEntryMongo variantSourceEntryMongo = new VariantSourceEntryMongo(createVariantSourceEntry());
        DBObject converted = (DBObject) mongoOperations.getConverter().convertToMongoType(variantSourceEntryMongo);
        Assert.assertEquals(FILE_ID, converted.get(FILEID_FIELD));
        Assert.assertEquals(STUDY_ID, converted.get(STUDYID_FIELD));
        Assert.assertEquals(FORMAT, converted.get(FORMAT_FIELD));
        Assert.assertEquals(3, ((BasicDBObject) converted.get(ATTRIBUTES_FIELD)).size());
        Assert.assertEquals(3, ((BasicDBObject) converted.get(SAMPLES_FIELD)).size());
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

    @Test
    public void testConvertFromVariantSourceEntryMongoToVariantSourceEntry(){
        VariantSourceEntryMongo variantSourceEntryMongo = new VariantSourceEntryMongo(createVariantSourceEntry());

        List<String> sampleNames = new ArrayList<String>();
        sampleNames.add("NA001");
        sampleNames.add("NA002");
        sampleNames.add("NA003");

        VariantSourceEntry variantSourceEntry = new VariantSourceEntry(variantSourceEntryMongo, sampleNames.size());
        Assert.assertEquals(FILE_ID, variantSourceEntry.getFileId());
        Assert.assertEquals(STUDY_ID, variantSourceEntry.getStudyId());
        Assert.assertEquals(FORMAT, variantSourceEntry.getFormat());
        Assert.assertEquals(3, variantSourceEntry.getAttributes().size());
        Assert.assertEquals(3, variantSourceEntry.getSamplesData().size());

        VariantSourceEntryWithSamples variantSourceEntryWithSamples = new VariantSourceEntryWithSamples(
                variantSourceEntry, sampleNames);
        Assert.assertEquals(FILE_ID, variantSourceEntryWithSamples.getFileId());
        Assert.assertEquals(STUDY_ID, variantSourceEntryWithSamples.getStudyId());
        Assert.assertEquals(FORMAT, variantSourceEntryWithSamples.getFormat());
        Assert.assertEquals(3, variantSourceEntryWithSamples.getAttributes().size());
        Assert.assertEquals(3, variantSourceEntryWithSamples.getSamplesDataMap().size());
        Assert.assertEquals("0/0", variantSourceEntryWithSamples.getSamplesDataMap().get("NA001").get("GT"));
        Assert.assertEquals("0/1", variantSourceEntryWithSamples.getSamplesDataMap().get("NA002").get("GT"));
        Assert.assertEquals("1/1", variantSourceEntryWithSamples.getSamplesDataMap().get("NA003").get("GT"));
    }



}
