/*
 * Copyright 2018 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.core.models.factories;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.ac.ebi.eva.commons.core.models.VariantStatistics;
import uk.ac.ebi.eva.commons.core.models.factories.exception.IncompleteInformationException;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;
import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;

import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * {@link VariantAggregatedVcfFactory}
 * input: a generic aggregated VCF
 * output: a List of Variants
 */
public class VariantAggregatedVcfFactoryTest {

    private static final String FILE_ID = "fileId";
    private static final String STUDY_ID = "studyId";

    private VariantAggregatedVcfFactory factory = new VariantAggregatedVcfFactory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void parseAC_AN() {
        String line = "1\t54722\t.\tTTC\tT,TCTC\t999\tPASS\tDP4=3122,3282,891,558;DP=22582;INDEL;IS=3,0.272727;VQSLOD=6.76;AN=3854;AC=889,61;TYPE=del,ins;HWE=0;ICF=-0.155251";   // structure like uk10k

        List<Variant> variants = factory.create(FILE_ID, STUDY_ID, line);

        VariantStatistics stats = variants.get(0).getSourceEntry(FILE_ID, STUDY_ID).getStats();
        assertEquals(2904, stats.getRefAlleleCount());
        assertEquals(889, stats.getAltAlleleCount());

        stats = variants.get(1).getSourceEntry(FILE_ID, STUDY_ID).getStats();
        assertEquals(2904, stats.getRefAlleleCount());
        assertEquals(61, stats.getAltAlleleCount());
        assertEquals(0.015827711, stats.getMaf(), 0.0001);
    }

    @Test
    public void parseGTC() {
        String line = "20\t61098\trs6078030\tC\tT\t51254.56\tPASS\tAC=225;AN=996;GTC=304,163,31";   // structure like gonl

        List<Variant> variants = factory.create(FILE_ID, STUDY_ID, line);

        VariantStatistics stats = variants.get(0).getSourceEntry(FILE_ID, STUDY_ID).getStats();
        assertEquals(new Integer(304), stats.getGenotypesCount().get(new Genotype("0/0", "C", "T")));
        assertEquals(new Integer(163), stats.getGenotypesCount().get(new Genotype("0/1", "C", "T")));
        assertEquals(new Integer(31), stats.getGenotypesCount().get(new Genotype("T/T", "C", "T")));
        assertEquals(0.225903614, stats.getMaf(), 0.0001);
    }

    @Test
    public void parseCustomGTC() {
        String line = "1\t1225579\t.\tG\tA,C\t170.13\tPASS\tAC=3,8;AN=534;AF=0.006,0.015;HPG_GTC=0/0:258,0/1:1,0/2:6,1/1:1,1/2:0,2/2:1,./.:0";  // structure like HPG

        Properties properties = new Properties();
        properties.put("ALL.GTC", "HPG_GTC");
        properties.put("ALL.AC", "AC");
        properties.put("ALL.AN", "AN");
        properties.put("ALL.AF", "AF");
        List<Variant> variants = new VariantAggregatedVcfFactory(properties).create(FILE_ID, STUDY_ID, line);

        VariantStatistics stats = variants.get(0).getSourceEntry(FILE_ID, STUDY_ID).getCohortStats("ALL");
        assertEquals(523, stats.getRefAlleleCount());
        assertEquals(3, stats.getAltAlleleCount());
        assertEquals(0.006, stats.getAltAlleleFreq(), 0.0001);
        assertEquals(3.0 / 534, stats.getMaf(), 0.0001);
        assertEquals(new Integer(258), stats.getGenotypesCount().get(new Genotype("0/0", "G", "A")));
        assertEquals(new Integer(1), stats.getGenotypesCount().get(new Genotype("0/1", "G", "A")));
        assertEquals(new Integer(1), stats.getGenotypesCount().get(new Genotype("A/A", "G", "A")));
        assertEquals(new Integer(6), stats.getGenotypesCount().get(new Genotype("0/2", "G", "A")));
        assertEquals(new Integer(0), stats.getGenotypesCount().get(new Genotype("./.", "G", "A")));

        stats = variants.get(1).getSourceEntry(FILE_ID, STUDY_ID).getCohortStats("ALL");
        assertEquals(new Integer(6), stats.getGenotypesCount().get(new Genotype("0/1", "G", "C")));

    }

    @Test
    public void parseWithGTS() {
        String line = "1\t861255\t.\tA\tG\t.\tPASS\tAC=2;AF=0.0285714285714286;AN=70;GTS=GG,GA,AA;GTC=1,0,34";

        List<Variant> variants = factory.create(FILE_ID, STUDY_ID, line);

        VariantStatistics stats = variants.get(0).getSourceEntry(FILE_ID, STUDY_ID).getStats();
        assertEquals(new Integer(34), stats.getGenotypesCount().get(new Genotype("0/0", "A", "G")));
        assertEquals(new Integer(0), stats.getGenotypesCount().get(new Genotype("0/1", "A", "G")));
        assertEquals(new Integer(1), stats.getGenotypesCount().get(new Genotype("G/G", "A", "G")));
        assertEquals(2.0 / 70, stats.getMaf(), 0.0001);
    }

    @Test
    public void getGenotype() {
        for (int i = 0; i < 11; i++) {
            Integer alleles[] = new Integer[2];
            VariantAggregatedVcfFactory.getGenotype(i, alleles);
            System.out.println("alleles[" + i + "] = " + alleles[0] + "/" + alleles[1]);
        }

        Integer alleles[] = new Integer[2];
        VariantAggregatedVcfFactory.getGenotype(0, alleles);    // 0/0
        assertEquals(alleles[0], alleles[1]);
        VariantAggregatedVcfFactory.getGenotype(2, alleles);    // 1/1
        assertEquals(alleles[0], alleles[1]);
        VariantAggregatedVcfFactory.getGenotype(5, alleles);    // 2/2
        assertEquals(alleles[0], alleles[1]);
        assertEquals(alleles[0], new Integer(2));
    }

    @Test
    public void variantWithAlleleFrequencyZero() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\tAF=0";

        List<Variant> variantList = factory.create(FILE_ID, STUDY_ID, line);
        assertTrue(variantList.isEmpty());
    }

    @Test
    public void multiallelicWithAlleleFrequencyZero() {
        String line = "1\t10040\trs123\tT\tC,G,A\t.\t.\tAF=0.5,0,0.2";

        List<Variant> variantList = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(2, variantList.size());
        assertEquals("C", variantList.get(0).getAlternate());
        assertEquals("A", variantList.get(1).getAlternate());
    }

    @Test
    public void variantWithAlleleCountZero() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\tAN=5;AC=0";

        List<Variant> variantList = factory.create(FILE_ID, STUDY_ID, line);
        assertTrue(variantList.isEmpty());
    }

    @Test
    public void multiAlleleWithOneAlleleCountZero() {
        String line = "1\t10040\trs123\tT\tC,G,A\t.\t.\tAN=7;AC=5,0,2";

        List<Variant> variantList = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(2, variantList.size());
        assertEquals("C", variantList.get(0).getAlternate());
        assertEquals("A", variantList.get(1).getAlternate());
    }

    @Test
    public void variantWithAlleleTotalNumberZero() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\tAN=0;AC=5";

        List<Variant> variantList = factory.create(FILE_ID, STUDY_ID, line);
        assertTrue(variantList.isEmpty());
    }

    @Test
    public void variantWithAlleleTotalNumberButNotAlleleCount() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\tAN=5";

        thrown.expect(IncompleteInformationException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void variantWithAlleleCountButNotAlleleTotalNumber() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\tAC=5";

        thrown.expect(IncompleteInformationException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void testVariantWithNoAlleleCountsOrFrequency() {
        String line = "1\t1000\t.\tT\tG\t.\t.\tAA=A";

        thrown.expect(IncompleteInformationException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void allowVariantWithNoAlleleCountsOrFrequency() {
        String line = "1\t1000\t.\tT\tG\t.\t.\tAA=A";

        factory.setRequireEvidence(false);

        List<Variant> variants = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(1, variants.size());
        assertEquals(1000, variants.get(0).getStart());
        assertEquals("A", variants.get(0).getSourceEntries().iterator().next().getAttribute("AA"));
    }

    @Test
    public void testMultiallelicVariantWithNoAlleleCountsOrFrequency() {
        String line = "1\t1000\t.\tT\tG,A\t.\t.\tAA=A";

        thrown.expect(IncompleteInformationException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }
}
