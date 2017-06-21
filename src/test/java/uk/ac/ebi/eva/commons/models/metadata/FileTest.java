package uk.ac.ebi.eva.commons.models.metadata;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Created by parce on 07/10/15.
 */
public class FileTest {

    @Test
    public void testAddSample() throws Exception {
        // create a file without samples
        File file = new File("file1.vcf", File.Type.VCF, "7s6efgwe78748");
        assertThat(file.getSamples(), empty());
        assertThat(file.getFileGenerators(), empty());

        // add one file to the sample
        Sample sample1 = new Sample("alias1", "sample1", null, null, null, null, null, null, null, null, null, null);
        file.addSample(sample1);
        checkFileHasSamples(file, sample1);
        checkFileInSamples(file, sample1);

        // add the same file object to the sample
        file.addSample(sample1);
        checkFileHasSamples(file, sample1);
        checkFileInSamples(file, sample1);

        // add a different object containing the same file
        Sample anotherSample1 = new Sample("anotherAlias", "sample1", null, null, null, null, null, null, null, null, null, null);
        file.addSample(anotherSample1);
        checkFileHasSamples(file, sample1);
        checkFileHasSamples(file, anotherSample1);
        checkFileInSamples(file, sample1, anotherSample1);

        // add a diferent sample to the file
        Sample sample2 = new Sample("alias2", "sample2", null, null, null, null, null, null, null, null, null, null);
        file.addSample(sample2);
        checkFileHasSamples(file, sample1, sample2);
        checkFileInSamples(file, sample1, sample2);
    }


    private void checkFileHasSamples(File file, Sample... samples) {
        Set<Sample> fileSamples = file.getSamples();
        assertThat(fileSamples, hasSize(samples.length));
        assertThat(fileSamples, contains(samples));
    }

    private void checkFileInSamples(File file, Sample... samples) {
        for (Sample sample : samples) {
            Set<File> sampleFiles = sample.getFiles();
            assertThat(sampleFiles, hasSize(1));
            assertThat(sampleFiles, contains(file));
        }
    }
}