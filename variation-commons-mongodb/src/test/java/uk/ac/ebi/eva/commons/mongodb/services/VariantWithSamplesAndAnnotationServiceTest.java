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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
        Region region = new Region("11", 190062L, 190064L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);

        List<VariantWithSamplesAndAnnotation> variantEntityList = service.findByRegionsAndComplexFilters(
                regions, null, null, null, new PageRequest(0, 10000));

        assertEquals(1, variantEntityList.size());

        for (VariantSourceEntryWithSampleNames variantSourceEntry : variantEntityList.get(0).getSourceEntries()) {
            if (variantSourceEntry.getFileId().equals("ERZX00051")) {
                assertEquals(28, variantSourceEntry.getCohortStats().size());
                assertFalse(variantSourceEntry.getSamplesData().isEmpty());
                Map<String, Map<String, String>> samplesData = variantSourceEntry.getSamplesDataMap();
                assertEquals("0|1", samplesData.get("HG03805").get("GT"));
            } else if (variantSourceEntry.getFileId().equals("ERZX00075")) {
                assertEquals(1, variantSourceEntry.getCohortStats().size());
                assertTrue(variantSourceEntry.getSamplesData().isEmpty());
            }
        }
        assertNotNull(variantEntityList.get(0).getAnnotation());
    }

    @Test
    public void testFindChromosomeBoundaries() {
        // single study in filter
        assertEquals(193051L, service.findChromosomeLowestReportedCoordinate("11", Collections.singletonList("PRJEB8661")).longValue());
        assertEquals(193959L, service.findChromosomeHighestReportedCoordinate("11", Collections.singletonList("PRJEB8661")).longValue());

        // two studies in filter
        assertEquals(190010L, service.findChromosomeLowestReportedCoordinate("11", Arrays.asList("PRJEB8661", "PRJEB6930")).longValue());
        assertEquals(194190L, service.findChromosomeHighestReportedCoordinate("11", Arrays.asList("PRJEB8661", "PRJEB6930")).longValue());

        // null is returned if a study has no variants in a chromosome
        assertNull(service.findChromosomeLowestReportedCoordinate("11", Arrays.asList("PRJEB5870")));
        assertNull(service.findChromosomeHighestReportedCoordinate("11", Arrays.asList("PRJEB5870")));
    }

    @Test
    public void testCountTotalNumberOfVariants() {
        // the returned number of variants should be the same as the number of variants in the test database
        assertEquals(498, service.countTotalNumberOfVariants());
    }

}

