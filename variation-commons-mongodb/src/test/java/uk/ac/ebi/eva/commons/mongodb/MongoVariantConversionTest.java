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

import com.mongodb.BasicDBList;
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
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.ws.VariantSourceEntryWithSampleNames;
import uk.ac.ebi.eva.commons.core.models.ws.VariantWithSamplesAndAnnotation;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.HgvsMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.mongodb.configurations.MongoOperationsConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@TestPropertySource({"classpath:test-mongo.properties"})
@ContextConfiguration(classes = {MongoOperationsConfiguration.class})
public class MongoVariantConversionTest {

    public static final int START = 1000;
    public static final int END = 1000;
    public static final String CHROMOSOME = "1";
    public static final String REFERENCE = "A";
    public static final String ALTERNATE = "C";
    public static final String RS_666 = "rs666";
    public static final String FORMAT = "GT:DP";
    public static final String FILE_ID = "f1";
    public static final String STUDY_ID = "s1";
    public static final String VARIANT_ID = "1_1000_A_C";

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    public void testConvertVariantWithFiles() {
        VariantMongo variant = new VariantMongo(buildVariantWithFiles());
        DBObject dbObject = (DBObject) mongoOperations.getConverter().convertToMongoType(variant);

        testBasicMongoVariant(dbObject);

        BasicDBList variantSources = (BasicDBList) dbObject.get(VariantMongo.FILES_FIELD);
        Assert.assertNotNull(variantSources);
        Assert.assertEquals(1, variantSources.size());
        BasicDBObject variantSource = (BasicDBObject) variantSources.get(0);
        Assert.assertNotNull(variantSource);
        Assert.assertEquals(FILE_ID, variantSource.get(VariantSourceEntryMongo.FILEID_FIELD));
        Assert.assertEquals(STUDY_ID, variantSource.get(VariantSourceEntryMongo.STUDYID_FIELD));
        Assert.assertEquals(FORMAT, variantSource.get(VariantSourceEntryMongo.FORMAT_FIELD));
        Assert.assertEquals(2, ((BasicDBObject) variantSource.get(VariantSourceEntryMongo.ATTRIBUTES_FIELD)).size());
        Assert.assertEquals(2, ((BasicDBObject) variantSource.get(VariantSourceEntryMongo.SAMPLES_FIELD)).size());
    }

    private VariantWithSamplesAndAnnotation buildVariantWithFiles() {
        VariantWithSamplesAndAnnotation variant = buildBasicVariant();
        variant.addSourceEntry(buildVariantSourceEntryWithSamples());
        return variant;
    }

    private VariantWithSamplesAndAnnotation buildBasicVariant() {
        VariantWithSamplesAndAnnotation variant = new VariantWithSamplesAndAnnotation(CHROMOSOME, START, END, REFERENCE, ALTERNATE);
        variant.setIds(Collections.singleton(RS_666));
        return variant;
    }

    private VariantSourceEntryWithSampleNames buildVariantSourceEntryWithSamples() {
        VariantSourceEntry variantSourceEntry = new VariantSourceEntry(FILE_ID, STUDY_ID);
        variantSourceEntry.addAttribute("QUAL", "0.01");
        variantSourceEntry.addAttribute("AN", "2");
        variantSourceEntry.setFormat(FORMAT);

        Map<String, String> na001 = new HashMap<>();
        na001.put("GT", "0/0");
        na001.put("DP", "4");
        variantSourceEntry.addSampleData(na001);
        Map<String, String> na002 = new HashMap<>();
        na002.put("GT", "0/1");
        na002.put("DP", "5");
        variantSourceEntry.addSampleData(na002);
        return new VariantSourceEntryWithSampleNames(variantSourceEntry, Arrays.asList("na001", "na002"));
    }

    private void testBasicMongoVariant(DBObject dbObject) {
        Assert.assertEquals(CHROMOSOME, dbObject.get(VariantMongo.CHROMOSOME_FIELD));
        Assert.assertEquals(START, dbObject.get(VariantMongo.START_FIELD));
        Assert.assertEquals(END, dbObject.get(VariantMongo.END_FIELD));
        Assert.assertEquals(REFERENCE, dbObject.get(VariantMongo.REFERENCE_FIELD));
        Assert.assertEquals(ALTERNATE, dbObject.get(VariantMongo.ALTERNATE_FIELD));
        Assert.assertTrue(((BasicDBList) dbObject.get(VariantMongo.IDS_FIELD)).contains(RS_666));
    }

    @Test
    public void testInverseConvertVariantWithFiles() {
        BasicDBObject mongoVariant = buildMongoVariantWithFiles();
        VariantMongo variant = mongoOperations.getConverter().read(VariantMongo.class, mongoVariant);
        Assert.assertEquals(VARIANT_ID, variant.getId());
        Assert.assertEquals(CHROMOSOME, variant.getChromosome());
        Assert.assertEquals(START, variant.getStart());
        Assert.assertEquals(END, variant.getEnd());
        Assert.assertEquals(REFERENCE, variant.getReference());
        Assert.assertEquals(ALTERNATE, variant.getAlternate());
        Assert.assertTrue(variant.getIds().contains(RS_666));
        Assert.assertNotNull(variant.getSourceEntries());
        Assert.assertFalse(variant.getSourceEntries().isEmpty());
        VariantSourceEntryMongo sourceEntry = variant.getSourceEntries().iterator().next();
        Assert.assertNotNull(sourceEntry.getAttributes());
        Assert.assertEquals(2, sourceEntry.getAttributes().keySet().size());
        Assert.assertEquals(2, sourceEntry.getSamples().size());
    }

    private BasicDBObject buildMongoVariantWithFiles() {
        BasicDBObject mongoVariant = buildMongoBasicVariant();

        BasicDBObject mongoFile = new BasicDBObject(VariantSourceEntryMongo.FILEID_FIELD, FILE_ID)
                .append(VariantSourceEntryMongo.FILEID_FIELD, STUDY_ID)
                .append(VariantSourceEntryMongo.ATTRIBUTES_FIELD,
                        new BasicDBObject("QUAL", "0.01").append("AN", "2"))
                .append(VariantSourceEntryMongo.FORMAT_FIELD, FORMAT);

        BasicDBObject genotypeCodes = new BasicDBObject();
        genotypeCodes.append("def", "0/0");
        genotypeCodes.append("0/1", Arrays.asList(1));
        mongoFile.append(VariantSourceEntryMongo.SAMPLES_FIELD, genotypeCodes);
        BasicDBList files = new BasicDBList();
        files.add(mongoFile);
        mongoVariant.append(VariantMongo.FILES_FIELD, files);

        return mongoVariant;
    }

    private BasicDBObject buildMongoBasicVariant() {
        VariantWithSamplesAndAnnotation variant = new VariantWithSamplesAndAnnotation(CHROMOSOME, START, END, REFERENCE, ALTERNATE);
        BasicDBObject mongoVariant = new BasicDBObject("_id", VARIANT_ID)
                .append(VariantMongo.IDS_FIELD, Collections.singleton(RS_666))
                .append(VariantMongo.TYPE_FIELD, variant.getType().name())
                .append(VariantMongo.CHROMOSOME_FIELD, CHROMOSOME)
                .append(VariantMongo.START_FIELD, START)
                .append(VariantMongo.END_FIELD, END)
                .append(VariantMongo.LENGTH_FIELD, variant.getLength())
                .append(VariantMongo.REFERENCE_FIELD, REFERENCE)
                .append(VariantMongo.ALTERNATE_FIELD, ALTERNATE);

        BasicDBList chunkIds = new BasicDBList();
        chunkIds.add("1_1_1k");
        chunkIds.add("1_0_10k");
        mongoVariant.append(VariantMongo.AT_FIELD, new BasicDBObject("chunkIds", chunkIds));

        BasicDBList hgvs = new BasicDBList();
        hgvs.add(new BasicDBObject(HgvsMongo.TYPE_FIELD, "genomic").append(HgvsMongo.NAME_FIELD, "1:g.1000A>C"));
        mongoVariant.append(VariantMongo.HGVS_FIELD, hgvs);
        return mongoVariant;
    }

    @Test
    public void testConvertVariantWithoutFiles() {
        VariantMongo variant = new VariantMongo(buildVariantWithFiles());
        DBObject dbObject = (DBObject) mongoOperations.getConverter().convertToMongoType(variant);
        testBasicMongoVariant(dbObject);
    }

    @Test
    public void testInverseConvertVariantWithoutFiles() {
        BasicDBObject mongoVariant = buildMongoBasicVariant();
        VariantMongo variant = mongoOperations.getConverter().read(VariantMongo.class, mongoVariant);
        Assert.assertEquals(VARIANT_ID, variant.getId());
        Assert.assertEquals(CHROMOSOME, variant.getChromosome());
        Assert.assertEquals(START, variant.getStart());
        Assert.assertEquals(END, variant.getEnd());
        Assert.assertEquals(REFERENCE, variant.getReference());
        Assert.assertEquals(ALTERNATE, variant.getAlternate());
        Assert.assertTrue(variant.getIds().contains(RS_666));
        Assert.assertNotNull(variant.getSourceEntries());
        Assert.assertTrue(variant.getSourceEntries().isEmpty());
    }

    /**
     * @see VariantMongo ids policy
     */
    @Test
    public void testConvertToDataModelTypeNullIds() {
        BasicDBObject mongoVariant = buildMongoBasicVariant();
        mongoVariant.remove(VariantMongo.IDS_FIELD);

        VariantMongo variant = mongoOperations.getConverter().read(VariantMongo.class, mongoVariant);
        assertNotNull(variant.getIds());
        assertTrue(variant.getIds().isEmpty());
    }

    /**
     * @see VariantMongo ids policy
     */
    @Test
    public void testConvertToDataModelTypeEmptyIds() {
        BasicDBObject mongoVariant = buildMongoBasicVariant();
        mongoVariant.put(VariantMongo.IDS_FIELD, new HashSet<String>());

        VariantMongo variant = mongoOperations.getConverter().read(VariantMongo.class, mongoVariant);
        assertNotNull(variant.getIds());
        assertTrue(variant.getIds().isEmpty());
    }
}
