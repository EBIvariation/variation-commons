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

import org.bson.BsonArray;
import org.bson.BsonString;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.ws.VariantSourceEntryWithSampleNames;
import uk.ac.ebi.eva.commons.core.models.ws.VariantWithSamplesAndAnnotation;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.HgvsMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@TestPropertySource({"classpath:eva.properties"})
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class MongoVariantConversionTest {

    private static final String TEST_DB = "test-db";

    public static final long START = 10000000000L;
    public static final long END = 10000000000L;
    public static final String CHROMOSOME = "1";
    public static final String REFERENCE = "A";
    public static final String ALTERNATE = "C";
    public static final String RS_666 = "rs666";
    public static final String FORMAT = "GT:DP";
    public static final String FILE_ID = "f1";
    public static final String STUDY_ID = "s1";
    public static final String VARIANT_ID = "1_10000000000_A_C";

    @Autowired
    private MongoOperations mongoOperations;

    //Required by nosql-unit
    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testConvertVariantWithFiles() {
        VariantMongo variant = new VariantMongo(buildVariantWithFiles());
        Document document = (Document) mongoOperations.getConverter().convertToMongoType(variant);

        testBasicMongoVariant(document);

        List<Document> variantSources = (List<Document>) document.get(VariantMongo.FILES_FIELD);
        Assertions.assertNotNull(variantSources);
        Assertions.assertEquals(1, variantSources.size());
        Document variantSource = (Document) variantSources.get(0);
        Assertions.assertNotNull(variantSource);
        Assertions.assertEquals(FILE_ID, variantSource.get(VariantSourceEntryMongo.FILEID_FIELD));
        Assertions.assertEquals(STUDY_ID, variantSource.get(VariantSourceEntryMongo.STUDYID_FIELD));
        Assertions.assertEquals(FORMAT, variantSource.get(VariantSourceEntryMongo.FORMAT_FIELD));
        Assertions.assertEquals(2, ((Document) variantSource.get(VariantSourceEntryMongo.ATTRIBUTES_FIELD)).size());
        Assertions.assertEquals(2, ((Document) variantSource.get(VariantSourceEntryMongo.SAMPLES_FIELD)).size());
    }

    private VariantWithSamplesAndAnnotation buildVariantWithFiles() {
        VariantWithSamplesAndAnnotation variant = buildBasicVariant();
        variant.addSourceEntry(buildVariantSourceEntryWithSamples());
        return variant;
    }

    private VariantWithSamplesAndAnnotation buildBasicVariant() {
        VariantWithSamplesAndAnnotation variant = new VariantWithSamplesAndAnnotation(CHROMOSOME, START, END, REFERENCE, ALTERNATE, null);
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

    private void testBasicMongoVariant(Document document) {
        Assertions.assertEquals(CHROMOSOME, document.get(VariantMongo.CHROMOSOME_FIELD));
        Assertions.assertEquals(START, document.get(VariantMongo.START_FIELD));
        Assertions.assertEquals(END, document.get(VariantMongo.END_FIELD));
        Assertions.assertEquals(REFERENCE, document.get(VariantMongo.REFERENCE_FIELD));
        Assertions.assertEquals(ALTERNATE, document.get(VariantMongo.ALTERNATE_FIELD));
        Assertions.assertTrue(((List<Document>) document.get(VariantMongo.IDS_FIELD)).contains(RS_666));
    }

    @Test
    public void testInverseConvertVariantWithFiles() {
        Document mongoVariant = buildMongoVariantWithFiles();
        VariantMongo variant = mongoOperations.getConverter().read(VariantMongo.class, mongoVariant);
        Assertions.assertEquals(VARIANT_ID, variant.getId());
        Assertions.assertEquals(CHROMOSOME, variant.getChromosome());
        Assertions.assertEquals(START, variant.getStart());
        Assertions.assertEquals(END, variant.getEnd());
        Assertions.assertEquals(REFERENCE, variant.getReference());
        Assertions.assertEquals(ALTERNATE, variant.getAlternate());
        Assertions.assertTrue(variant.getIds().contains(RS_666));
        Assertions.assertNotNull(variant.getSourceEntries());
        Assertions.assertFalse(variant.getSourceEntries().isEmpty());
        VariantSourceEntryMongo sourceEntry = variant.getSourceEntries().iterator().next();
        Assertions.assertNotNull(sourceEntry.getAttributes());
        Assertions.assertEquals(2, sourceEntry.getAttributes().keySet().size());
        Assertions.assertEquals(2, sourceEntry.getSamples().size());
    }

    private Document buildMongoVariantWithFiles() {
        Document mongoVariant = buildMongoBasicVariant();

        Document mongoFile = new Document(VariantSourceEntryMongo.FILEID_FIELD, FILE_ID)
                .append(VariantSourceEntryMongo.FILEID_FIELD, STUDY_ID)
                .append(VariantSourceEntryMongo.ATTRIBUTES_FIELD,
                        new Document("QUAL", "0.01").append("AN", "2"))
                .append(VariantSourceEntryMongo.FORMAT_FIELD, FORMAT);

        Document genotypeCodes = new Document();
        genotypeCodes.append("def", "0/0");
        genotypeCodes.append("0/1", Arrays.asList(1));
        mongoFile.append(VariantSourceEntryMongo.SAMPLES_FIELD, genotypeCodes);
        List<Document> files = new ArrayList<>();
        files.add(mongoFile);
        mongoVariant.append(VariantMongo.FILES_FIELD, files);

        return mongoVariant;
    }

    private Document buildMongoBasicVariant() {
        VariantWithSamplesAndAnnotation variant = new VariantWithSamplesAndAnnotation(CHROMOSOME, START, END, REFERENCE,
                ALTERNATE, null);
        Document mongoVariant = new Document("_id", VARIANT_ID)
                .append(VariantMongo.IDS_FIELD, Collections.singleton(RS_666))
                .append(VariantMongo.TYPE_FIELD, variant.getType().name())
                .append(VariantMongo.CHROMOSOME_FIELD, CHROMOSOME)
                .append(VariantMongo.START_FIELD, START)
                .append(VariantMongo.END_FIELD, END)
                .append(VariantMongo.LENGTH_FIELD, variant.getLength())
                .append(VariantMongo.REFERENCE_FIELD, REFERENCE)
                .append(VariantMongo.ALTERNATE_FIELD, ALTERNATE);

        BsonArray chunkIds = new BsonArray();
        chunkIds.add(new BsonString("1_1_1k"));
        chunkIds.add(new BsonString("1_0_10k"));
        mongoVariant.append(VariantMongo.AT_FIELD, new Document("chunkIds", chunkIds));

        List<Document> hgvs = new ArrayList<>();
        hgvs.add(new Document(HgvsMongo.TYPE_FIELD, "genomic").append(HgvsMongo.NAME_FIELD, "1:g.1000A>C"));
        mongoVariant.append(VariantMongo.HGVS_FIELD, hgvs);
        return mongoVariant;
    }

    @Test
    public void testConvertVariantWithoutFiles() {
        VariantMongo variant = new VariantMongo(buildVariantWithFiles());
        Document document = (Document) mongoOperations.getConverter().convertToMongoType(variant);
        testBasicMongoVariant(document);
    }

    @Test
    public void testInverseConvertVariantWithoutFiles() {
        Document mongoVariant = buildMongoBasicVariant();
        VariantMongo variant = mongoOperations.getConverter().read(VariantMongo.class, mongoVariant);
        Assertions.assertEquals(VARIANT_ID, variant.getId());
        Assertions.assertEquals(CHROMOSOME, variant.getChromosome());
        Assertions.assertEquals(START, variant.getStart());
        Assertions.assertEquals(END, variant.getEnd());
        Assertions.assertEquals(REFERENCE, variant.getReference());
        Assertions.assertEquals(ALTERNATE, variant.getAlternate());
        Assertions.assertTrue(variant.getIds().contains(RS_666));
        Assertions.assertNotNull(variant.getSourceEntries());
        Assertions.assertTrue(variant.getSourceEntries().isEmpty());
    }

    /**
     * @see VariantMongo ids policy
     */
    @Test
    public void testConvertToDataModelTypeNullIds() {
        Document mongoVariant = buildMongoBasicVariant();
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
        Document mongoVariant = buildMongoBasicVariant();
        mongoVariant.put(VariantMongo.IDS_FIELD, new HashSet<String>());

        VariantMongo variant = mongoOperations.getConverter().read(VariantMongo.class, mongoVariant);
        assertNotNull(variant.getIds());
        assertTrue(variant.getIds().isEmpty());
    }

    @Test
    public void testChangeRefAltToUpperCase() {
        VariantMongo variantMongo = new VariantMongo(new VariantWithSamplesAndAnnotation("chr1", START,
                END, "a", "t", null));
        Assertions.assertEquals("A", variantMongo.getReference());
        Assertions.assertEquals("T", variantMongo.getAlternate());
    }

}
