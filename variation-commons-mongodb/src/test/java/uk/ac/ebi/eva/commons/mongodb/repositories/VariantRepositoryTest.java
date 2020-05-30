/*
 * European Variation Archive (EVA) - Open-access database of all types of genetic
 * variation data from all species
 *
 * Copyright 2016 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.mongodb.repositories;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.mongodb.MongoDbConfigurationBuilder;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import uk.ac.ebi.eva.commons.core.models.Region;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.filter.FilterBuilder;
import uk.ac.ebi.eva.commons.mongodb.filter.VariantRepositoryFilter;
import uk.ac.ebi.eva.commons.mongodb.test.rule.FixSpringMongoDbRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for VariantRepository
 * <p>
 * Load data from json using lordofthejars nosqlunit.
 */
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:eva.properties")
@UsingDataSet(locations = {
        "/test-data/variants.json",
        "/test-data/annotations.json",
        "/test-data/files.json"})
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class VariantRepositoryTest {

    private static final String TEST_DB = "test-db";

    @Autowired
    private ApplicationContext applicationContext;

    @Rule
    public MongoDbRule mongoDbRule = new FixSpringMongoDbRule(
            MongoDbConfigurationBuilder.mongoDb().databaseName(TEST_DB).build());

    @Autowired
    private VariantRepository variantRepository;

    @Test
    public void checkFieldPresence() throws IOException {

        List<Region> regions = new ArrayList<>();
        regions.add(new Region("11", 190000L, 190300L));

        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<String> exclude = new ArrayList<>();

        List<VariantMongo> variantEntityList = variantRepository
                .findByRegionsAndComplexFilters(regions, filters, exclude,
                        new PageRequest(0, 100000000));

        for (VariantMongo currVariantEntity : variantEntityList) {
            assertFalse(currVariantEntity.getSourceEntries().isEmpty());
            assertFalse(currVariantEntity.getIds().isEmpty());
            for (VariantSourceEntryMongo variantSourceEntry : currVariantEntity.getSourceEntries()) {
                assertFalse(variantSourceEntry.getAttributes().isEmpty());
            }
        }

    }

    @Test
    public void checkSourceLinePresence() throws IOException {

        VariantMongo variantEntityWithSourceLine = variantRepository.findById("11_190010_G_A").get();

        assertFalse(variantEntityWithSourceLine.getSourceEntries().isEmpty());
        assertFalse(variantEntityWithSourceLine.getIds().isEmpty());
        assertEquals("NC_027300.1\t369\t.\tC\tT\t23494.8\tPASS\t.",
                     variantEntityWithSourceLine.getSourceEntries().stream().findFirst().get().getAttributes()
                                                .get("src"));
    }

    @Test
    public void testVariantIdIsFound() {
        List<String> ids = new ArrayList<>();
        ids.add("rs148957270");

        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<String> exclude = new ArrayList<>();
        List<VariantMongo> variantEntityList = variantRepository
                .findByIdsAndComplexFilters(ids, filters, exclude, null);
        assertNotNull(variantEntityList);
        assertTrue(variantEntityList.size() > 0);
        Set<String> idSet = new HashSet<>();
        idSet.add(ids.get(0));
        idSet.add("ss254803838");
        assertEquals(idSet, variantEntityList.get(0).getIds());
    }

    @Test
    public void testVariantIdsFoundAndNotFound() {
        List<String> ids = new ArrayList<>();

        // Ids that should be found
        ids.add("rs148957270");
        ids.add("rs575961545");

        // Id that should be not found
        ids.add("a1b1c1d1e1");

        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<String> exclude = new ArrayList<>();
        List<VariantMongo> variantEntityList = variantRepository
                .findByIdsAndComplexFilters(ids, filters, exclude, null);
        assertNotNull(variantEntityList);
        assertTrue(variantEntityList.size() > 0);

        // Only the 2 first ids should be found
        assertEquals(2, variantEntityList.size());

        // And the rs are contained in the returned information, either in the first or the second (order independent)
        assertTrue(variantEntityList.get(0).getIds().contains(ids.get(0)) || variantEntityList.get(0).getIds().contains(ids.get(1)));
        assertTrue(variantEntityList.get(1).getIds().contains(ids.get(0)) || variantEntityList.get(1).getIds().contains(ids.get(1)));

    }

    @Test
    public void testCountByIdsAndComplexFilters() {
        List<String> ids = new ArrayList<>();
        ids.add("rs575961545");
        ids.add("rs148957270");
        List<VariantRepositoryFilter> filters = new ArrayList<>();

        Long count = variantRepository.countByIdsAndComplexFilters(ids, filters);
        assertEquals(new Long(2), count);
    }

    @Test
    public void testCountByIdsAndComplexFiltersZeroCount() {
        List<String> ids = new ArrayList<>();
        ids.add("not_a_real_id");

        List<VariantRepositoryFilter> filters = new ArrayList<>();
        Long count = variantRepository.countByIdsAndComplexFilters(ids, filters);
        assertEquals(new Long(0), count);
    }

    @Test
    public void testNonExistentVariantIdIsNotFound() {
        List<String> ids = new ArrayList<>();
        ids.add("notarealid");

        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<String> exclude = new ArrayList<>();
        List<VariantMongo> variantEntityList = variantRepository
                .findByIdsAndComplexFilters(ids, filters, exclude, null);
        assertNotNull(variantEntityList);
        assertTrue(variantEntityList.size() == 0);
    }

    @Test
    public void testVariantRegionIsFound() {
        String chr = "11";
        long start = 190013;
        long end = 190013;
        Region region = new Region(chr, start, end);
        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<String> exclude = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<VariantMongo> variantEntityList = variantRepository
                .findByRegionsAndComplexFilters(regions, filters, exclude, new PageRequest(0, 1000000));
        assertNotNull(variantEntityList);
        assertTrue(variantEntityList.size() > 0);
        assertEquals(chr, variantEntityList.get(0).getChromosome());
        assertEquals(start, variantEntityList.get(0).getStart());
        assertEquals(end, variantEntityList.get(0).getStart());
    }

    @Test
    public void testVariantRegionIsFoundMultiple() {
        Region region = new Region("11", 190000L, 194000L);
        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<String> exclude = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<VariantMongo> variantEntityList = variantRepository
                .findByRegionsAndComplexFilters(regions, filters, exclude, new PageRequest(0, 1000000));
        assertNotNull(variantEntityList);
        assertTrue(variantEntityList.size() > 0);
        assertEquals(478, variantEntityList.size());
        VariantMongo prevVariantEntity = variantEntityList.get(0);
        for (VariantMongo currVariantEntity : variantEntityList) {
            assertTrue(prevVariantEntity.getStart() <= currVariantEntity.getStart());
        }
    }

    @Test
    public void testCountByRegionsAndComplexFilters() {
        Region region = new Region("11", 191000L, 194000L);
        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        Long count = variantRepository.countByRegionsAndComplexFilters(regions, filters);
        assertEquals(new Long(418), count);
    }

    @Test
    public void testNonExistentVariantRegionIsNotFound() {
        Region region = new Region("11", 61098L, 60916L);
        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<String> exclude = new ArrayList<>();
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<VariantMongo> variantEntityList = variantRepository
                .findByRegionsAndComplexFilters(regions, filters, exclude, new PageRequest(0, 1000000));
        assertNotNull(variantEntityList);
        assertTrue(variantEntityList.size() == 0);
    }

    @Test
    public void testRegionIsFoundWithConsequenceType() {
        List<String> cts = new ArrayList<>();
        cts.add("SO:0001566");
        List<VariantRepositoryFilter> filters = new FilterBuilder().withConsequenceType(cts).build();
        Region region = new Region("11", 190000L, 194000L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 270);
    }

    @Test
    public void testRegionIsFoundWithMafGreaterThan() {
        Region region = new Region("11", 191000L, 193000L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<VariantRepositoryFilter> filters = new FilterBuilder().withMaf(">0.125").build();
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 13);
    }

    @Test
    public void testRegionIsFoundWithMafGreaterThanEquals() {
        Region region = new Region("11", 191000L, 193_000L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<VariantRepositoryFilter> filters = new FilterBuilder().withMaf(">=0.125").build();
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 13);
    }

    @Test
    public void testRegionIsFoundWithMafEquals() {
        Region region = new Region("11", 192000L, 194000L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<VariantRepositoryFilter> filters = new FilterBuilder().withMaf("=0.5").build();
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 3);
    }

    @Test
    public void testRegionIsFoundWithPolyphenGreaterThan() {
        Region region = new Region("11", 190000L, 193719L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<VariantRepositoryFilter> filters = new FilterBuilder().withPolyphenScore(">0.5").build();
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 4);
    }

    @Test
    public void testRegionIsFoundWithSiftLessThan() {
        Region region = new Region("11", 190000L, 193719L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<VariantRepositoryFilter> filters = new FilterBuilder().withSiftScore("<0.5").build();
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 11);
    }

    @Test
    public void testRegionIsFoundWithStudies() {
        List<String> studies = new ArrayList<>();
        studies.add("PRJEB6930");
        List<VariantRepositoryFilter> filters = new FilterBuilder().withStudies(studies).build();
        Region region = new Region("11", 192000L, 193000L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 41);
    }

    @Test
    public void testRegionIsFoundWithFiles() {
        List<String> files = new ArrayList<>();
        files.add("ERZ019961");
        List<VariantRepositoryFilter> filters = new FilterBuilder().withFiles(files).build();
        Region region = new Region("11", 191000L, 192000L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 2);
    }

    @Test
    public void testRegionIsFoundWithTypes() {
        List<VariantType> types = new ArrayList<>();
        types.add(VariantType.INDEL);
        List<VariantRepositoryFilter> filters = new FilterBuilder().withVariantTypes(types).build();
        Region region = new Region("11", 192000L, 194000L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 17);
    }

    @Test
    public void testRegionIsFoundWithAlternates() {
        List<String> alternates = new ArrayList<>();
        alternates.add("T");
        List<VariantRepositoryFilter> filters = new FilterBuilder().withAlternates(alternates).build();
        Region region = new Region("11", 192000L, 194000L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);
        List<String> exclude = new ArrayList<>();
        testFiltersHelperRegion(regions, filters, exclude, 93);
    }

    @Test
    public void testFindByRegionsAndComplexFilters() {

        List<Region> regions = new ArrayList<>();
        regions.add(new Region("11", 193000L, 193300L));
        regions.add(new Region("11", 190100L, 190200L));
        regions.add(new Region("11", 190000L, 190200L));

        List<VariantRepositoryFilter> filters = new ArrayList<>();
        List<String> exclude = new ArrayList<>();

        testFindByRegionsAndComplexFiltersHelper(regions, filters, exclude, 106);

        regions = new ArrayList<>();
        regions.add(new Region("11", 190001L, 190079L)); //7

        testFindByRegionsAndComplexFiltersHelper(regions, filters, null, 7);

        regions.add(new Region("11", 190150L, 190250L)); //3

        testFindByRegionsAndComplexFiltersHelper(regions, filters, null, 10);

        regions.add(new Region("11", 191222L, 191333L)); //6

        testFindByRegionsAndComplexFiltersHelper(regions, filters, null, 16);
    }

    @Test
    public void testFindByRegionsAndComplexFiltersExcludeSingleRoot() {
        List<Region> regions = new ArrayList<>();
        regions.add(new Region("11", 193000L, 193300L));

        List<String> exclude = new ArrayList<>();
        exclude.add(VariantMongo.FILES_FIELD);
        List<VariantRepositoryFilter> filters = new ArrayList<>();

        List<VariantMongo> variantEntityList = variantRepository
                .findByRegionsAndComplexFilters(regions, filters, exclude, new PageRequest(0, 10000));
        assertNotNull(variantEntityList);
        for (VariantMongo currVariantEntity : variantEntityList) {
            assertTrue(currVariantEntity.getSourceEntries().isEmpty());
        }
    }

    @Test
    public void testFindByRegionsAndComplexFiltersExcludeAttributes() {
        List<Region> regions = new ArrayList<>();
        regions.add(new Region("11", 193000L, 193300L));

        List<String> exclude = new ArrayList<>();
        exclude.add(
                VariantMongo.FILES_FIELD + "." + VariantSourceEntryMongo.ATTRIBUTES_FIELD);
        List<VariantRepositoryFilter> filters = new ArrayList<>();

        List<VariantMongo> variantEntityList = variantRepository
                .findByRegionsAndComplexFilters(regions, filters, exclude, new PageRequest(0, 10000));

        assertNotNull(variantEntityList);
        for (VariantMongo currVariantEntity : variantEntityList) {
            for (VariantSourceEntryMongo variantSourceEntry : currVariantEntity.getSourceEntries()) {
                assertFalse(variantSourceEntry.getFileId().isEmpty());
                assertTrue(variantSourceEntry.getAttributes().isEmpty());
            }
        }
    }

    @Test
    public void testFindDistinctChromosomesByStudyId() {
        Set<String> chromosomeSet = variantRepository.findDistinctChromosomes();

        Set<String> expectedChromosomes = new HashSet<>();
        expectedChromosomes.add("11");
        expectedChromosomes.add("9");
        expectedChromosomes.add("2");

        assertEquals(expectedChromosomes, chromosomeSet);
    }

    @Test
    public void testCountByChromosomeAndStartAndEndAndAltAndStudy() {
        List<String> studies = new ArrayList<>();
        studies.add("PRJEB6930");
        Long count = (long) variantRepository
                .findByChromosomeAndStartAndAltAndStudyIn("11", 190057, "T", studies).size();
        assertEquals(new Long(1), count);
    }

    @Test
    public void testCountByChromosomeAndStartAndTypeAndStudy() {
        List<String> studies = new ArrayList<>();
        studies.add("PRJX00001");
        int count = variantRepository.findByChromosomeAndStartAndTypeAndStudyIn("11", 190523,
                VariantType.SNV, studies).size();
        assertEquals(1, count);
    }

    @Test
    public void testSamplesDataIsNotEmpty() {
        Region region = new Region("11", 190062L, 190064L);
        List<Region> regions = new ArrayList<>();
        regions.add(region);

        List<VariantMongo> variantEntityList = variantRepository.
                findByRegionsAndComplexFilters(regions, null, null, new PageRequest(0, 10000));

        assertEquals(1, variantEntityList.size());
    }

    @Test
    public void testFindByChromosomeAndStudyInSortedBy() {
        Sort ascendingStartOrder = new Sort(Sort.Direction.ASC, "start");
        Sort descendingStartOrder = new Sort(Sort.Direction.DESC, "start");

        // first and last variant for one study
        VariantMongo firstVariant =
                variantRepository.findOneByChromosomeAndStudyInSorted("11", Collections.singletonList("PRJEB8661"),
                                                                      ascendingStartOrder);
        VariantMongo lastVariant =
                variantRepository.findOneByChromosomeAndStudyInSorted("11", Collections.singletonList("PRJEB8661"),
                                                                      descendingStartOrder);
        assertEquals(193051L, firstVariant.getStart());
        assertEquals(193959L, lastVariant.getStart());

        // first and last variant for two studies
        firstVariant =
                variantRepository.findOneByChromosomeAndStudyInSorted("11", Arrays.asList("PRJEB8661", "PRJEB6930"),
                                                                      ascendingStartOrder);
        lastVariant =
                variantRepository.findOneByChromosomeAndStudyInSorted("11", Arrays.asList("PRJEB8661", "PRJEB6930"),
                                                                      descendingStartOrder);
        assertEquals(190010L, firstVariant.getStart());
        assertEquals(194190L, lastVariant.getStart());


        // in a chromosome with one variant, the first and the last variants are the same
        firstVariant =
                variantRepository.findOneByChromosomeAndStudyInSorted("9", Collections.singletonList("PRJEB5829"),
                                                                      ascendingStartOrder);
        lastVariant =
                variantRepository.findOneByChromosomeAndStudyInSorted("9", Collections.singletonList("PRJEB5829"),
                                                                      descendingStartOrder);
        assertEquals(firstVariant.getId(), lastVariant.getId());
        assertEquals(10099L, firstVariant.getStart());

    }

    private void testFiltersHelperRegion(List<Region> regions, List<VariantRepositoryFilter> filters,
                                         List<String> exclude, int expectedResultLength) {
        List<VariantMongo> variantEntityList =
                variantRepository.findByRegionsAndComplexFilters(regions, filters, exclude,
                        new PageRequest(0, 10000));
        assertNotNull(variantEntityList);
        assertEquals(expectedResultLength, variantEntityList.size());
    }

    private void testFindByRegionsAndComplexFiltersHelper(List<Region> regions, List<VariantRepositoryFilter> filters,
                                                          List<String> exclude, int expectedResultLength) {
        List<VariantMongo> variantEntityList = variantRepository
                .findByRegionsAndComplexFilters(regions, filters, exclude, new PageRequest(0, 100000000));
        assertNotNull(variantEntityList);
        assertTrue(variantEntityList.size() > 0);
        VariantMongo prevVariantEntity = variantEntityList.get(0);
        for (VariantMongo currVariantEntity : variantEntityList) {
            if (prevVariantEntity.getChromosome().equals(currVariantEntity.getChromosome())) {
                assertTrue(prevVariantEntity.getStart() <= currVariantEntity.getStart());
            }
        }
        assertEquals(expectedResultLength, variantEntityList.size());
    }
}
