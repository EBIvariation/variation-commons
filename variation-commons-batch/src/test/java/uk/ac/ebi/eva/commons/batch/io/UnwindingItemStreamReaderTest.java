/*
 * Copyright 2016 EMBL - European Bioinformatics Institute
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.test.MetaDataInstanceFactory;

import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;
import uk.ac.ebi.eva.commons.core.utils.CompressionHelper;
import uk.ac.ebi.eva.commons.core.utils.FileUtils;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.ac.ebi.eva.commons.batch.io.UnwindingItemReaderTest.consumeReader;

public class UnwindingItemStreamReaderTest {

    @TempDir
    Path temporaryFolder;

    private static final String INPUT_FILE_PATH = "/input-files/vcf/genotyped.vcf.gz";

    private static final String INPUT_WRONG_FILE_PATH = "/input-files/vcf/wrong_same_ref_alt.vcf.gz";

    private static final String FILE_ID = "5";

    private static final String STUDY_ID = "7";

    @Test
    public void shouldReadAllLines() throws Exception {
        ExecutionContext executionContext = MetaDataInstanceFactory.createStepExecution().getExecutionContext();

        // input vcf
        File input = FileUtils.getResourceFile(INPUT_FILE_PATH);

        VcfReader vcfReader = new VcfReader(FILE_ID, STUDY_ID, input);
        vcfReader.setSaveState(false);

        UnwindingItemStreamReader<Variant> unwindingItemStreamReader = new UnwindingItemStreamReader<>(vcfReader);
        unwindingItemStreamReader.open(executionContext);

        consumeReader(input, unwindingItemStreamReader);
    }

    @Test
    public void invalidFileShouldFail() throws Exception {
        ExecutionContext executionContext = MetaDataInstanceFactory.createStepExecution().getExecutionContext();

        // input vcf
        File input = FileUtils.getResourceFile(INPUT_WRONG_FILE_PATH);

        VcfReader vcfReader = new VcfReader(FILE_ID, STUDY_ID, input);
        vcfReader.setSaveState(false);

        UnwindingItemStreamReader<Variant> unwindingItemStreamReader = new UnwindingItemStreamReader<>(vcfReader);
        unwindingItemStreamReader.open(executionContext);

        // consume the reader and check that a wrong variant raise an exception
        assertThrows(FlatFileParseException.class, () -> {
            while (unwindingItemStreamReader.read() != null) {
            }
        });
    }

    @Test
    public void testUncompressedVcf() throws Exception {
        ExecutionContext executionContext = MetaDataInstanceFactory.createStepExecution().getExecutionContext();

        // uncompress the input VCF into a temporary file
        File input = FileUtils.getResourceFile(INPUT_FILE_PATH);
        File tempFile = temporaryFolder.resolve("tempFile.vcf").toFile();
        CompressionHelper.uncompress(input.getAbsolutePath(), tempFile);

        VcfReader vcfReader = new VcfReader(FILE_ID, STUDY_ID, tempFile);
        vcfReader.setSaveState(false);

        UnwindingItemStreamReader<Variant> unwindingItemStreamReader = new UnwindingItemStreamReader<>(vcfReader);
        unwindingItemStreamReader.open(executionContext);

        consumeReader(input, unwindingItemStreamReader);
    }

}
