/*
 * Copyright 2014-2018 EMBL - European Bioinformatics Institute
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
import uk.ac.ebi.eva.commons.core.models.factories.exception.NonVariantException;
import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VariantGenotypedVcfFactoryTest {

    private static final String FILE_ID = "fileId";

    private static final String STUDY_ID = "studyId";

    private static VariantVcfFactory factory = new VariantGenotypedVcfFactory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testCreateVariant_Samples() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\t.\tGT\t0/0\t0/1\t0/.\t./1\t1/1"; // 5 samples

        // Initialize expected variants
        Variant var0 = new Variant("1", 10041, 10041 + "C".length() - 1, "T", "C");
        VariantSourceEntry file0 = new VariantSourceEntry(FILE_ID, STUDY_ID);
        var0.addSourceEntry(file0);

        // Initialize expected samples
        Map<String, String> na001 = new HashMap<>();
        na001.put("GT", "0/0");
        Map<String, String> na002 = new HashMap<>();
        na002.put("GT", "0/1");
        Map<String, String> na003 = new HashMap<>();
        na003.put("GT", "0/.");
        Map<String, String> na004 = new HashMap<>();
        na004.put("GT", "./1");
        Map<String, String> na005 = new HashMap<>();
        na005.put("GT", "1/1");

        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na001);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na002);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na003);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na004);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na005);

        // Check proper conversion of samples
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(1, result.size());

        Variant getVar0 = result.get(0);
        assertEquals(var0.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData(),
                     getVar0.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData());
    }

    @Test
    public void testCreateVariantFromVcfMultiallelicVariants_Samples() {
        String line = "1\t123456\t.\tT\tC,G\t110\tPASS\t.\tGT:AD:DP:GQ:PL\t0/1:10,5:17:94:94,0,286\t0/2:3,8:15:43:222,0,43\t0/0:.:18:.:.\t1/2:7,6:13:99:162,0,180"; // 4 samples

        // Initialize expected variants
        Variant var0 = new Variant("1", 123456, 123456, "T", "C");
        VariantSourceEntry file0 = new VariantSourceEntry(FILE_ID, STUDY_ID);
        var0.addSourceEntry(file0);

        Variant var1 = new Variant("1", 123456, 123456, "T", "G");
        VariantSourceEntry file1 = new VariantSourceEntry(FILE_ID, STUDY_ID);
        var1.addSourceEntry(file1);


        // Initialize expected samples in variant 1 (alt allele C)
        Map<String, String> na001_C = new HashMap<>();
        na001_C.put("GT", "0/1");
        na001_C.put("AD", "10,5");
        na001_C.put("DP", "17");
        na001_C.put("GQ", "94");
        na001_C.put("PL", "94,0,286");
        Map<String, String> na002_C = new HashMap<>();
        na002_C.put("GT", "0/2");
        na002_C.put("AD", "3,8");
        na002_C.put("DP", "15");
        na002_C.put("GQ", "43");
        na002_C.put("PL", "222,0,43");
        Map<String, String> na003_C = new HashMap<>();
        na003_C.put("GT", "0/0");
        na003_C.put("AD", ".");
        na003_C.put("DP", "18");
        na003_C.put("GQ", ".");
        na003_C.put("PL", ".");
        Map<String, String> na004_C = new HashMap<>();
        na004_C.put("GT", "1/2");
        na004_C.put("AD", "7,6");
        na004_C.put("DP", "13");
        na004_C.put("GQ", "99");
        na004_C.put("PL", "162,0,180");

        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na001_C);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na002_C);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na003_C);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na004_C);

        // Initialize expected samples in variant 2 (alt allele G)
        Map<String, String> na001_G = new HashMap<>();
        na001_G.put("GT", "0/2");
        na001_G.put("AD", "10,5");
        na001_G.put("DP", "17");
        na001_G.put("GQ", "94");
        na001_G.put("PL", "94,0,286");
        Map<String, String> na002_G = new HashMap<>();
        na002_G.put("GT", "0/1");
        na002_G.put("AD", "3,8");
        na002_G.put("DP", "15");
        na002_G.put("GQ", "43");
        na002_G.put("PL", "222,0,43");
        Map<String, String> na003_G = new HashMap<>();
        na003_G.put("GT", "0/0");
        na003_G.put("AD", ".");
        na003_G.put("DP", "18");
        na003_G.put("GQ", ".");
        na003_G.put("PL", ".");
        Map<String, String> na004_G = new HashMap<>();
        na004_G.put("GT", "2/1");
        na004_G.put("AD", "7,6");
        na004_G.put("DP", "13");
        na004_G.put("GQ", "99");
        na004_G.put("PL", "162,0,180");
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na001_G);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na002_G);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na003_G);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na004_G);


        // Check proper conversion of samples and alternate alleles
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(2, result.size());

        Variant getVar0 = result.get(0);
        assertEquals(
                var0.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData(),
                getVar0.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData());
        assertArrayEquals(new String[]{"G"}, getVar0.getSourceEntry(FILE_ID, STUDY_ID).getSecondaryAlternates());

        Variant getVar1 = result.get(1);
        assertEquals(
                var1.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData(),
                getVar1.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData());
        assertArrayEquals(new String[]{"C"}, getVar1.getSourceEntry(FILE_ID, STUDY_ID).getSecondaryAlternates());
    }

    @Test
    public void testCreateVariantFromVcfCoLocatedVariants_Samples() {
        String line = "1\t10040\trs123\tT\tC,GC\t.\t.\t.\tGT\t0/0\t0/1\t0/2\t1/1\t1/2\t2/2"; // 6 samples

        // Initialize expected variants
        Variant var0 = new Variant("1", 10041, 10041 + "C".length() - 1, "T", "C");
        VariantSourceEntry file0 = new VariantSourceEntry(FILE_ID, STUDY_ID);
        var0.addSourceEntry(file0);

        Variant var1 = new Variant("1", 10050, 10050 + "GC".length() - 1, "T", "GC");
        VariantSourceEntry file1 = new VariantSourceEntry(FILE_ID, STUDY_ID);
        var1.addSourceEntry(file1);

        // Initialize expected samples in variant 1 (alt allele C)
        Map<String, String> na001_C = new HashMap<>();
        na001_C.put("GT", "0/0");
        Map<String, String> na002_C = new HashMap<>();
        na002_C.put("GT", "0/1");
        Map<String, String> na003_C = new HashMap<>();
        na003_C.put("GT", "0/2");
        Map<String, String> na004_C = new HashMap<>();
        na004_C.put("GT", "1/1");
        Map<String, String> na005_C = new HashMap<>();
        na005_C.put("GT", "1/2");
        Map<String, String> na006_C = new HashMap<>();
        na006_C.put("GT", "2/2");

        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na001_C);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na002_C);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na003_C);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na004_C);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na005_C);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na006_C);

        // Initialize expected samples in variant 2 (alt allele GC)
        Map<String, String> na001_GC = new HashMap<>();
        na001_GC.put("GT", "0/0");
        Map<String, String> na002_GC = new HashMap<>();
        na002_GC.put("GT", "0/2");
        Map<String, String> na003_GC = new HashMap<>();
        na003_GC.put("GT", "0/1");
        Map<String, String> na004_GC = new HashMap<>();
        na004_GC.put("GT", "2/2");
        Map<String, String> na005_GC = new HashMap<>();
        na005_GC.put("GT", "2/1");
        Map<String, String> na006_GC = new HashMap<>();
        na006_GC.put("GT", "1/1");

        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na001_GC);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na002_GC);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na003_GC);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na004_GC);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na005_GC);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na006_GC);

        // Check proper conversion of samples
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(2, result.size());

        Variant getVar0 = result.get(0);
        assertEquals(
                var0.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData(),
                getVar0.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData());
        assertArrayEquals(new String[]{"GC"},
                          getVar0.getSourceEntry(FILE_ID, STUDY_ID).getSecondaryAlternates());

        Variant getVar1 = result.get(1);
        assertEquals(
                var1.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData(),
                getVar1.getSourceEntry(FILE_ID, STUDY_ID).getSamplesData());
        assertArrayEquals(new String[]{"C"},
                          getVar1.getSourceEntry(FILE_ID, STUDY_ID).getSecondaryAlternates());
    }

    @Test
    public void testCreateVariantWithMissingGenotypes() {
        String line = "1\t1407616\t.\tC\tG\t43.74\tPASS\t.\tGT:AD:DP:GQ:PL\t./.:.:.:.:.\t1/1:0,2:2:6:71,6,0\t./.:.:.:.:.\t./.:.:.:.:.";

        // Initialize expected variants
        Variant var0 = new Variant("1", 1407616, 1407616, "C", "G");
        VariantSourceEntry file0 = new VariantSourceEntry(FILE_ID, STUDY_ID);
        var0.addSourceEntry(file0);

        // Initialize expected samples
        Map<String, String> na001 = new HashMap<>();
        na001.put("GT", "./.");
        na001.put("AD", ".");
        na001.put("DP", ".");
        na001.put("GQ", ".");
        na001.put("PL", ".");
        Map<String, String> na002 = new HashMap<>();
        na002.put("GT", "1/1");
        na002.put("AD", "0,2");
        na002.put("DP", "2");
        na002.put("GQ", "6");
        na002.put("PL", "71,6,0");
        Map<String, String> na003 = new HashMap<>();
        na003.put("GT", "./.");
        na003.put("AD", ".");
        na003.put("DP", ".");
        na003.put("GQ", ".");
        na003.put("PL", ".");
        Map<String, String> na004 = new HashMap<>();
        na004.put("GT", "./.");
        na004.put("AD", ".");
        na004.put("DP", ".");
        na004.put("GQ", ".");
        na004.put("PL", ".");

        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na001);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na002);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na003);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na004);


        // Check proper conversion of samples
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(1, result.size());

        Variant getVar0 = result.get(0);
        VariantSourceEntry getFile0 = getVar0.getSourceEntry(FILE_ID, STUDY_ID);

        Map<String, String> na001Data = getFile0.getSampleData(0);
        assertEquals("./.", na001Data.get("GT"));
        assertEquals(".", na001Data.get("AD"));
        assertEquals(".", na001Data.get("DP"));
        assertEquals(".", na001Data.get("GQ"));
        assertEquals(".", na001Data.get("PL"));

        Map<String, String> na002Data = getFile0.getSampleData(1);
        assertEquals("1/1", na002Data.get("GT"));
        assertEquals("0,2", na002Data.get("AD"));
        assertEquals("2", na002Data.get("DP"));
        assertEquals("6", na002Data.get("GQ"));
        assertEquals("71,6,0", na002Data.get("PL"));

        Map<String, String> na003Data = getFile0.getSampleData(2);
        assertEquals("./.", na003Data.get("GT"));
        assertEquals(".", na003Data.get("AD"));
        assertEquals(".", na003Data.get("DP"));
        assertEquals(".", na003Data.get("GQ"));
        assertEquals(".", na003Data.get("PL"));

        Map<String, String> na004Data = getFile0.getSampleData(3);
        assertEquals("./.", na004Data.get("GT"));
        assertEquals(".", na004Data.get("AD"));
        assertEquals(".", na004Data.get("DP"));
        assertEquals(".", na004Data.get("GQ"));
        assertEquals(".", na004Data.get("PL"));
    }

    @Test
    public void testParseInfo() {
        String line = "1\t123456\t.\tT\tC,G\t110\tPASS\tAN=3;AC=1,2;AF=0.125,0.25;DP=63;NS=4;MQ=10685\tGT:AD:DP:GQ:PL\t"
                + "0/1:10,5:17:94:94,0,286\t0/2:3,8:15:43:222,0,43\t0/0:.:18:.:.\t0/2:7,6:13:0:162,0,180"; // 4 samples

        // Initialize expected variants
        Variant var0 = new Variant("1", 123456, 123456, "T", "C");
        VariantSourceEntry file0 = new VariantSourceEntry(FILE_ID, STUDY_ID);
        var0.addSourceEntry(file0);

        Variant var1 = new Variant("1", 123456, 123456, "T", "G");
        VariantSourceEntry file1 = new VariantSourceEntry(FILE_ID, STUDY_ID);
        var1.addSourceEntry(file1);


        // Initialize expected samples
        Map<String, String> na001 = new HashMap<>();
        na001.put("GT", "0/1");
        na001.put("AD", "10,5");
        na001.put("DP", "17");
        na001.put("GQ", "94");
        na001.put("PL", "94,0,286");
        Map<String, String> na002 = new HashMap<>();
        na002.put("GT", "0/1");
        na002.put("AD", "3,8");
        na002.put("DP", "15");
        na002.put("GQ", "43");
        na002.put("PL", "222,0,43");
        Map<String, String> na003 = new HashMap<>();
        na003.put("GT", "0/0");
        na003.put("AD", ".");
        na003.put("DP", "18");
        na003.put("GQ", ".");
        na003.put("PL", ".");
        Map<String, String> na004 = new HashMap<>();
        na004.put("GT", "0/1");
        na004.put("AD", "7,6");
        na004.put("DP", "13");
        na004.put("GQ", "0");
        na004.put("PL", "162,0,180");

        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na001);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na002);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na003);
        var0.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na004);

        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na001);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na002);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na003);
        var1.getSourceEntry(FILE_ID, STUDY_ID).addSampleData(na004);


        // Check proper conversion of samples
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(2, result.size());

        Variant getVar0 = result.get(0);
        VariantSourceEntry getFile0 = getVar0.getSourceEntry(FILE_ID, STUDY_ID);
        assertEquals(4, Integer.parseInt(getFile0.getAttribute("NS")));
//        assertEquals(2, Integer.parseInt(getFile0.getAttribute("AN")));
        assertEquals(1, Integer.parseInt(getFile0.getAttribute("AC")));
        assertEquals(0.125, Double.parseDouble(getFile0.getAttribute("AF")), 1e-8);
        assertEquals(63, Integer.parseInt(getFile0.getAttribute("DP")));
        assertEquals(10685, Integer.parseInt(getFile0.getAttribute("MQ")));
        assertEquals(1, Integer.parseInt(getFile0.getAttribute("MQ0")));

        Variant getVar1 = result.get(1);
        VariantSourceEntry getFile1 = getVar1.getSourceEntry(FILE_ID, STUDY_ID);
        assertEquals(4, Integer.parseInt(getFile1.getAttribute("NS")));
//        assertEquals(2, Integer.parseInt(getFile1.getAttribute("AN")));
        assertEquals(2, Integer.parseInt(getFile1.getAttribute("AC")));
        assertEquals(0.25, Double.parseDouble(getFile1.getAttribute("AF")), 1e-8);
        assertEquals(63, Integer.parseInt(getFile1.getAttribute("DP")));
        assertEquals(10685, Integer.parseInt(getFile1.getAttribute("MQ")));
        assertEquals(1, Integer.parseInt(getFile1.getAttribute("MQ0")));
    }

    @Test
    public void testVariantWithNoSampleInformation() {
        // a variant with frequency but no genotypes, will be valid for the VariantAggregatedVcfFactory, but not for
        // VariantGenotypedVcfFactory, that expects to find genotypes
        String line = "1\t1000\t.\tT\tG\t.\t.\tAF=0.5";

        thrown.expect(IllegalArgumentException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void testRequireNoEvidence() {
        // a variant with frequency but no genotypes, will be valid for the VariantAggregatedVcfFactory, but not for
        // VariantGenotypedVcfFactory, that expects to find genotypes
        String line = "1\t1000\t.\tT\tG\t.\t.\tAF=0.5";

        thrown.expect(UnsupportedOperationException.class);
        factory.setRequireEvidence(false);
    }

    @Test
    public void testMultiallelicVariantWitnNoSampleInformation() {
        // a variant with frequency but no genotypes, will be valid for the VariantAggregatedVcfFactory, but not for
        // VariantGenotypedVcfFactory, that expects to find genotypes
        String line = "1\t1000\t.\tT\tG,C\t.\t.\tAF=0.5";

        thrown.expect(IllegalArgumentException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void variantWithNoAltGenotypes() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\t.\tGT\t0/0\t0|0\t./.\t0|0\t./.";

        thrown.expect(NonVariantException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void haploidVariantWithNoAltGenotypes() {
        String line = "Y\t10040\trs123\tT\tC\t.\t.\t.\tGT\t.\t0\t0\t.";

        thrown.expect(NonVariantException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void triploidVariantWithNoAltGenotypes() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\t.\tGT\t0/0/0\t0|0|0\t././.\t0|0|0\t././.";

        thrown.expect(NonVariantException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void variantWhereAllGenotypesAreMissingValues() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\t.\tGT\t./.\t./.\t./.\t./.\t./.";

        thrown.expect(NonVariantException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void variantWhereAllGenotypesAreReference() {
        String line = "1\t10040\trs123\tT\tC\t.\t.\t.\tGT\t0/0\t0|0\t0/0\t0|0\t0/0";

        thrown.expect(NonVariantException.class);
        factory.create(FILE_ID, STUDY_ID, line);
    }

    @Test
    public void multiallelicVariantAndOneAlleleHasNoGenotypes() {
        String line = "Y\t10040\trs123\tT\tC,G,A\t.\t.\t.\tGT\t0/0\t0/2\t./.\t2/2\t0/3\t2/3";

        List<Variant> variantList = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(2, variantList.size());
        assertEquals("G", variantList.get(0).getAlternate());
        assertEquals("A", variantList.get(1).getAlternate());
    }

    @Test
    public void infoAttributesWontBeValidated() {
        List<Variant> expResult = new LinkedList<>();
        expResult.add(new Variant("chr1", 1000, 1000, "T", "G"));
        String line;

        // the variant is valid if it has some alternate genotypes, even if AF, AC or AN are zero
        line = "chr1\t1000\t.\tT\tG\t.\t.\tAF=0;AC=0;AN=0\tGT\t0/1";
        List<Variant> result = factory.create(FILE_ID, STUDY_ID, line);
        assertEquals(expResult, result);
    }

}
