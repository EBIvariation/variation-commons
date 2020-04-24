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
package uk.ac.ebi.eva.commons.batch.io;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.test.MetaDataInstanceFactory;

import uk.ac.ebi.eva.commons.core.models.IVariant;
import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;
import uk.ac.ebi.eva.commons.core.utils.CompressionHelper;
import uk.ac.ebi.eva.commons.core.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * {@link VcfReader}
 * <p>
 * input: a Vcf file
 * <p>
 * output: a list of variants each time its `.read()` is called.
 */
public class CoordinatesVcfReaderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final String INPUT_FILE_PATH = "/input-files/vcf/genotyped.vcf.gz";

    private static final String INPUT_FILE_WITH_NON_VARIANTS_PATH = "/input-files/vcf/wrong_same_ref_alt.vcf.gz";

    @Rule
    public TemporaryFolder temporaryFolderRule = new TemporaryFolder();

    @Test
    public void shouldReadAllLines() throws Exception {
        ExecutionContext executionContext = MetaDataInstanceFactory.createStepExecution().getExecutionContext();

        // input vcf
        File input = FileUtils.getResourceFile(INPUT_FILE_PATH);

        CoordinatesVcfLineMapper lineMapper = new CoordinatesVcfLineMapper();
        VcfReader vcfReader = new VcfReader(lineMapper, input);
        vcfReader.setSaveState(false);
        vcfReader.open(executionContext);

        consumeReader(input, vcfReader);
    }

    @Test
    public void shouldIgnoreNonVariants() throws Exception {
        ExecutionContext executionContext = MetaDataInstanceFactory.createStepExecution().getExecutionContext();

        // input vcf
        File input = FileUtils.getResourceFile(INPUT_FILE_WITH_NON_VARIANTS_PATH);

        CoordinatesVcfLineMapper lineMapper = new CoordinatesVcfLineMapper();
        VcfReader vcfReader = new VcfReader(lineMapper, input);
        vcfReader.setSaveState(false);
        vcfReader.open(executionContext);

        int actualCount = 0;
        int actualRows = 0;
        List<Variant> variants;
        while ((variants = vcfReader.read()) != null) {
            actualCount += variants.size();
            actualRows++;
        }
        long expectedRows = FileUtils.countNonCommentLines(new GZIPInputStream(new FileInputStream(input)));
        long nonVariantCount = 1;   // counted externally on the test file
        long extraMultiallelicCount = 3+1+1+1; // extra alternate alleles, e.g. ALT=A,C,CC,CCC counts as 3 extra alleles, compared with the number of rows
        assertEquals(expectedRows, actualRows);
        assertEquals(expectedRows + extraMultiallelicCount - nonVariantCount, actualCount);
    }

    @Test
    public void testUncompressedVcf() throws Exception {
        ExecutionContext executionContext = MetaDataInstanceFactory.createStepExecution().getExecutionContext();

        // uncompress the input VCF into a temporary file
        File input = FileUtils.getResourceFile(INPUT_FILE_PATH);
        File tempFile = temporaryFolderRule.newFile();
        CompressionHelper.uncompress(input.getAbsolutePath(), tempFile);

        CoordinatesVcfLineMapper lineMapper = new CoordinatesVcfLineMapper();
        VcfReader vcfReader = new VcfReader(lineMapper, input);
        vcfReader.setSaveState(false);
        vcfReader.open(executionContext);

        consumeReader(input, vcfReader);
    }

    private void consumeReader(File inputFile, VcfReader vcfReader) throws Exception {
        List<Variant> variants;
        int count = 0;

        // consume the reader and check that the variants and the VariantSource have meaningful data
        while ((variants = vcfReader.read()) != null) {
            assertTrue(variants.size() > 0);

            for (Variant variant : variants) {
                assertEquals(0, variant.getSourceEntries().size());
            }

            count++;
        }

        // VcfReader should get all the lines from the file
        long expectedCount = FileUtils.countNonCommentLines(new GZIPInputStream(new FileInputStream(inputFile)));
        assertEquals(expectedCount, count);
    }
}
