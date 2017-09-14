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
package uk.ac.ebi.eva.commons.mongodb.services;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.eva.commons.core.models.Region;
import uk.ac.ebi.eva.commons.core.models.ws.VariantSourceEntryWithSampleNames;
import uk.ac.ebi.eva.commons.core.models.ws.VariantWithSamplesAndAnnotation;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class})
@UsingDataSet(locations = {
        "/test-data/variants.json",
        "/test-data/annotations.json",
        "/test-data/files.json",
        "/test-data/annotation_metadata.json"})
public class VariantWithSamplesAndAnnotationServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("test-db");

    @Autowired
    private VariantWithSamplesAndAnnotationsService service;

    @Test
    public void testFindByRegionsAndComplexFilters() throws AnnotationMetadataNotFoundException {
        String chr = "11";
        long start = 190062;
        long end = 190064;
        Region region = new Region(chr, start, end);
        List<Region> regions = new ArrayList<>();
        regions.add(region);

        List<VariantWithSamplesAndAnnotation> variantEntityList = service.findByRegionsAndComplexFilters(
                regions, null, null, null, new PageRequest(0, 10000));

        assertEquals(1, variantEntityList.size());

        for (VariantSourceEntryWithSampleNames variantSourceEntry : variantEntityList.get(0).getSourceEntries()) {
            if (!variantSourceEntry.getFileId().equals("ERZX00051")) {
                continue;
            }
            assertNotEquals(0, variantSourceEntry.getSamplesData().size());
            Map<String, Map<String, String>> samplesData = variantSourceEntry.getSamplesDataMap();
            assertEquals("0|1", samplesData.get("HG03805").get("GT"));
        }
        assertNotNull(variantEntityList.get(0).getAnnotation());
    }

}

