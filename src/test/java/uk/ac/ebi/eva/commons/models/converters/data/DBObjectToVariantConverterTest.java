/*
 * Copyright 2015-2016 OpenCB
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.models.converters.data;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.VariantSourceEntry;
import org.opencb.commons.utils.CryptoUtils;
import org.opencb.opencga.storage.core.variant.VariantStorageManager;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToSamplesConverter;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantSourceEntryConverter;
import org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantStatsConverter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DBObjectToVariantConverterTest {

    private BasicDBObject mongoVariant;
    private Variant variant;
    protected VariantSourceEntry variantSourceEntry;

    @Before
    public void setUp() {
        //Setup variant
        variant = new Variant("1", 1000, 1000, "A", "C");
        variant.setId("rs666");

        //Setup variantSourceEntry
        variantSourceEntry = new VariantSourceEntry("f1", "s1");
        variantSourceEntry.addAttribute("QUAL", "0.01");
        variantSourceEntry.addAttribute("AN", "2");
        variantSourceEntry.setFormat("GT:DP");

        Map<String, String> na001 = new HashMap<>();
        na001.put("GT", "0/0");
        na001.put("DP", "4");
        variantSourceEntry.addSampleData("NA001", na001);
        Map<String, String> na002 = new HashMap<>();
        na002.put("GT", "0/1");
        na002.put("DP", "5");
        variantSourceEntry.addSampleData("NA002", na002);
        variant.addSourceEntry(variantSourceEntry);

        //Setup mongoVariant
        mongoVariant = new BasicDBObject("_id", "1_1000_A_C")
                .append(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD, variant.getIds())
                .append(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.TYPE_FIELD, variant.getType().name())
                .append(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.CHROMOSOME_FIELD, variant.getChromosome())
                .append(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.START_FIELD, variant.getStart())
                .append(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.END_FIELD, variant.getStart())
                .append(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.LENGTH_FIELD, variant.getLength())
                .append(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.REFERENCE_FIELD, variant.getReference())
                .append(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.ALTERNATE_FIELD, variant.getAlternate());

        BasicDBList chunkIds = new BasicDBList();
        chunkIds.add("1_1_1k");
        chunkIds.add("1_0_10k");
        mongoVariant.append("_at", new BasicDBObject("chunkIds", chunkIds));

        BasicDBList hgvs = new BasicDBList();
        hgvs.add(new BasicDBObject("type", "genomic").append("name", "1:g.1000A>C"));
        mongoVariant.append("hgvs", hgvs);
    }

    @Test
    public void testConvertToDataModelTypeWithFiles() {
        variant.addSourceEntry(variantSourceEntry);

        // MongoDB object

        BasicDBObject mongoFile = new BasicDBObject(DBObjectToVariantSourceEntryConverter.FILEID_FIELD, variantSourceEntry.getFileId())
                .append(DBObjectToVariantSourceEntryConverter.STUDYID_FIELD, variantSourceEntry.getStudyId());
        mongoFile.append(DBObjectToVariantSourceEntryConverter.ATTRIBUTES_FIELD,
                         new BasicDBObject("QUAL", "0.01").append("AN", "2"));
        mongoFile.append(DBObjectToVariantSourceEntryConverter.FORMAT_FIELD, variantSourceEntry.getFormat());
        BasicDBObject genotypeCodes = new BasicDBObject();
        genotypeCodes.append("def", "0/0");
        genotypeCodes.append("0/1", Arrays.asList(1));
        mongoFile.append(DBObjectToVariantSourceEntryConverter.SAMPLES_FIELD, genotypeCodes);
        BasicDBList files = new BasicDBList();
        files.add(mongoFile);
        mongoVariant.append("files", files);

        List<String> sampleNames = Lists.newArrayList("NA001", "NA002");
        org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter converter = new org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter(
                new DBObjectToVariantSourceEntryConverter(
                        VariantStorageManager.IncludeSrc.FULL,
                        new DBObjectToSamplesConverter(sampleNames)),
                new DBObjectToVariantStatsConverter());
        Variant converted = converter.convertToDataModelType(mongoVariant);
        assertEquals(variant, converted);
    }

    @Test
    public void testConvertToStorageTypeWithFiles() {

        variant.addSourceEntry(variantSourceEntry);

        // MongoDB object
        BasicDBObject mongoFile = new BasicDBObject(DBObjectToVariantSourceEntryConverter.FILEID_FIELD, variantSourceEntry.getFileId())
                .append(DBObjectToVariantSourceEntryConverter.STUDYID_FIELD, variantSourceEntry.getStudyId());
        mongoFile.append(DBObjectToVariantSourceEntryConverter.ATTRIBUTES_FIELD,
                         new BasicDBObject("QUAL", "0.01").append("AN", "2"));
        mongoFile.append(DBObjectToVariantSourceEntryConverter.FORMAT_FIELD, variantSourceEntry.getFormat());
        BasicDBObject genotypeCodes = new BasicDBObject();
        genotypeCodes.append("def", "0/0");
        genotypeCodes.append("0/1", Arrays.asList(1));
        mongoFile.append(DBObjectToVariantSourceEntryConverter.SAMPLES_FIELD, genotypeCodes);
//        mongoFile.append(DBObjectToVariantConverter.STATS_FIELD, Collections.emptyList());
        BasicDBList files = new BasicDBList();
        files.add(mongoFile);
        mongoVariant.append("files", files);

        List<String> sampleNames = Lists.newArrayList("NA001", "NA002");
        org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter converter = new org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter(
                new DBObjectToVariantSourceEntryConverter(
                        VariantStorageManager.IncludeSrc.FULL,
                        new DBObjectToSamplesConverter(sampleNames)),
                null);
        DBObject converted = converter.convertToStorageType(variant);
        assertFalse(converted.containsField(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD)); //IDs must be added manually.
        converted.put(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD, variant.getIds());  //Add IDs
        assertEquals(mongoVariant, converted);
    }


    @Test
    public void testConvertToDataModelTypeWithoutFiles() {
        org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter converter = new org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter();
        Variant converted = converter.convertToDataModelType(mongoVariant);
        assertEquals(variant, converted);
    }

    @Test
    public void testConvertToStorageTypeWithoutFiles() {
        org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter converter = new org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter();
        DBObject converted = converter.convertToStorageType(variant);
        assertFalse(converted.containsField(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD)); //IDs must be added manually.
        converted.put(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD, variant.getIds());  //Add IDs
        assertEquals(mongoVariant, converted);
    }

    /** @see org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter ids policy   */
    @Test
    public void testConvertToDataModelTypeNullIds() {
        mongoVariant.remove(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD);

        org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter converter = new org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter();
        Variant converted = converter.convertToDataModelType(mongoVariant);
        assertNotNull(converted.getIds());
        assertTrue(converted.getIds().isEmpty());

        variant.setIds(new HashSet<String>());
        assertEquals(variant, converted);
    }

    /** @see org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter ids policy   */
    @Test
    public void testConvertToStorageTypeNullIds() {
        variant.setIds(null);

        org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter converter = new org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter();
        DBObject converted = converter.convertToStorageType(variant);
        assertFalse(converted.containsField(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD)); //IDs must be added manually.

        mongoVariant.remove(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD);
        assertEquals(mongoVariant, converted);
    }

    /** @see org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter ids policy   */
    @Test
    public void testConvertToDataModelTypeEmptyIds() {
        mongoVariant.put(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD, new HashSet<String>());

        org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter converter = new org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter();
        Variant converted = converter.convertToDataModelType(mongoVariant);
        assertNotNull(converted.getIds());
        assertTrue(converted.getIds().isEmpty());

        variant.setIds(new HashSet<String>());
        assertEquals(variant, converted);
    }

    /** @see org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter ids policy   */
    @Test
    public void testConvertToStorageTypeEmptyIds() {
        variant.setIds(new HashSet<String>());

        org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter converter = new org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter();
        DBObject converted = converter.convertToStorageType(variant);
        assertFalse(converted.containsField(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD)); //IDs must be added manually.

        mongoVariant.remove(org.opencb.opencga.storage.mongodb.variant.DBObjectToVariantConverter.IDS_FIELD);
        assertEquals(mongoVariant, converted);
    }

    @Test
    public void testBuildStorageId() {
        DBObjectToVariantConverter converter = new DBObjectToVariantConverter();

        // SNV
        Variant v1 = new Variant("1", 1000, 1000, "A", "C");
        assertEquals("1_1000_A_C", converter.buildStorageId(v1));

        // Indel
        Variant v2 = new Variant("1", 1000, 1002, "", "CA");
        assertEquals("1_1000__CA", converter.buildStorageId(v2));

        // Structural
        String alt = "ACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGTACGT";
        Variant v3 = new Variant("1", 1000, 1002, "TAG", alt);
        assertEquals("1_1000_TAG_" + new String(CryptoUtils.encryptSha1(alt)), converter.buildStorageId(v3));
    }

}
