/*
 * Copyright 2018 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.core.models.factories;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * {@link VariantVcfFactory}
 * input: a genotyped VCF
 * output: a List of Variants
 */
public class VariantVcfFactoryTest {

    private static final String FILE_ID = "fileId";

    private static final String STUDY_ID = "studyId";

    private static VariantVcfFactory factory;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setupClass() {
        factory = instantiateAbstractVcfFactory();
    }

    /**
     * Create an object of the abstract class VariantVcfFactory to test the code to parse the basic VCF fields, that
     * is common to all the factories. The method 'parseSplitSampleData' doesn't need to do anything
     */
    private static VariantVcfFactory instantiateAbstractVcfFactory() {
        return new VariantVcfFactory() {
            @Override
            protected void parseSplitSampleData(VariantSourceEntry variantSourceEntry, String[] fields,
                                                int alternateAlleleIdx) {
            }
        };
    }

    @Test
    public void testRemoveChrPrefixInAnyCase() {
        String line;

        line = "chr1\t1000\t.\tT\tG\t.\t.\t.";
        List<Variant> expResult = Collections.singletonList(new Variant("chr1", 1000, 1000, "T", "G"));
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "Chr1\t1000\t.\tT\tG\t.\t.\t.";
        expResult = Collections.singletonList(new Variant("Chr1", 1000, 1000, "T", "G"));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "CHR1\t1000\t.\tT\tG\t.\t.\t.";
        expResult = Collections.singletonList(new Variant("CHR1", 1000, 1000, "T", "G"));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateVariantFromVcfSameLengthRefAlt() {
        // Test when there are differences at the end of the sequence
        String line = "1\t1000\trs123\tTCACCC\tTGACGG\t.\t.\t.";

        List<Variant> expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1001, 1005, "CACCC", "GACGG"));

        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        // Test when there are not differences at the end of the sequence
        line = "1\t1000\trs123\tTCACCC\tTGACGC\t.\t.\t.";

        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1001, 1004, "CACC", "GACG"));

        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateVariantFromVcfInsertionEmptyRef() {
        String line = "1\t1000\trs123\t.\tTGACGC\t.\t.\t.";

        List<Variant> expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1000, 1000 + "TGACGC".length() - 1, "", "TGACGC"));

        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateVariantFromVcfDeletionEmptyAlt() {
        String line = "1\t999\trs123\tGTCACCC\tG\t.\t.\t.";

        List<Variant> expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1000, 1000 + "TCACCC".length() - 1, "TCACCC", ""));

        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateVariantFromVcfIndelNotEmptyFields() {
        String line = "1\t1000\trs123\tCGATT\tTAC\t.\t.\t.";

        List<Variant> expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1000, 1000 + "CGATT".length() - 1, "CGATT", "TAC"));
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\tAT\tA\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1001, 1001, "T", ""));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\tGATC\tG\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1001, 1003, "ATC", ""));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\t.\tATC\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1000, 1002, "", "ATC"));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\tA\tATC\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1001, 1002, "", "TC"));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\tAC\tACT\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1002, 1002, "", "T"));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        // Printing those that are not currently managed
        line = "1\t1000\trs123\tAT\tT\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1000, 1000, "A", ""));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\tATC\tTC\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1000, 1000, "A", ""));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\tATC\tAC\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1001, 1001, "T", ""));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\tAC\tATC\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1001, 1001, "", "T"));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);

        line = "1\t1000\trs123\tATC\tGC\t.\t.\t.";
        expResult = new LinkedList<>();
        expResult.add(new Variant("1", 1000, 1001, "AT", "G"));
        result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);
    }

    @Test
    public void testCreateVariantFromVcfCoLocatedVariants_MainFields() {
        String line = "1\t10040\trs123\tTGACGTAACGATT\tT,TGACGTAACGGTT,TGACGTAATAC\t.\t.\t.\tGT\t0/0\t0/1\t0/2\t1/3"; // 4 samples

        // Check proper conversion of main fields
        List<Variant> expResult = new LinkedList<>();
        expResult.add(new Variant("1", 10040, 10040 + "TGACGTAACGAT".length() - 1, "TGACGTAACGAT", ""));
        expResult.add(new Variant("1", 10050, 10050 + "A".length() - 1, "A", "G"));
        expResult.add(new Variant("1", 10048, 10048 + "CGATT".length() - 1, "CGATT", "TAC"));

        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);
    }

    @Test
    public void testVariantIds() {
        // EVA-942 - Unless configured (see below), by default we ignore IDs from submitter's VCF
        Set<String> emptySet = Collections.emptySet();

        // test that an ID is ignored
        checkIds(factory, "1\t1000\trs123\tC\tT\t.\t.\t.", emptySet);

        // test that several ID are ignored
        checkIds(factory, "1\t1000\trs123;rs456\tC\tT\t.\t.\t.", emptySet);

        // test that a missing ID ('.') is not added to the IDs set
        checkIds(factory, "1\t1000\t.\tC\tT\t.\t.\t.", emptySet);


        // EVA-1898 - needed for eva-accession-clustering, test that ID is read if explicitly configured
        VariantVcfFactory accessionedVariantFactory = instantiateAbstractVcfFactory();
        accessionedVariantFactory.setIncludeIds(true);

        // test that an ID is properly read
        checkIds(accessionedVariantFactory, "1\t1000\trs123\tC\tT\t.\t.\t.", Collections.singleton("rs123"));

        // test that a missing ID ('.') is not added to the IDs set
        checkIds(factory, "1\t1000\t.\tC\tT\t.\t.\t.", emptySet);
        checkIds(accessionedVariantFactory, "1\t1000\trs123;.\tC\tT\t.\t.\t.", Collections.singleton("rs123"));

        // test that the ';' is used as the ID separator (as of VCF 4.2)
        checkIds(accessionedVariantFactory, "1\t1000\trs123;rs456\tC\tT\t.\t.\t.",
                 Stream.of("rs123", "rs456").collect(Collectors.toSet()));

        // test that the ',' is NOT used as the ID separator (as of VCF 4.2)
        checkIds(accessionedVariantFactory, "1\t1000\trs123,rs456\tC\tT\t.\t.\t.",
                 Collections.singleton("rs123,rs456"));
    }

    @Test
    public void testChangeRefAltToUpperCase(){
       String line = "chr1\t1000\t.\tt\tg\t.\t.\t.";
        List<Variant> expResult = Collections.singletonList(new Variant("chr1", 1000, 1000, "T", "G"));
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);
    }

    private void checkIds(VariantVcfFactory variantVcfFactory, String vcfLine, Set<String> expectedIds) {
        List<Variant> expectedVariants = new LinkedList<>();
        expectedVariants.add(new Variant("1", 1000, 1000, "C", "T"));
        expectedVariants.get(0).setIds(expectedIds);

        List<Variant> parsedVariants = variantVcfFactory.create(FILE_ID, STUDY_ID, vcfLine);

        assertEquals(expectedVariants, parsedVariants);
        assertEquals(expectedIds, parsedVariants.get(0).getIds());
    }
}
