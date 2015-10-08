package embl.ebi.variation.commons.models.metadata;

import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
/**
 * Created by parce on 07/10/15.
 */
public class SampleTest {

//    @org.junit.Test
//    public void testAddFile() throws Exception {
//        // create a sample without files
//        Sample sample = new Sample(1);
//        assertThat(sample.getFiles(), nullValue());
//
//        // add one file to the sample
//        File file1 = new File(1);
//        sample.addFile(file1);
//        checkSampleHasFiles(sample, file1);
//        checkSampleInFiles(sample, file1);
//
//        // add again the same file object to the sample
//        sample.addFile(file1);
//        checkSampleHasFiles(sample, file1);
//        checkSampleInFiles(sample, file1);
//
//        // add again the same file in a different object instance
//        File anotherFile1 = new File(1);
//        sample.addFile(anotherFile1);
//        checkSampleHasFiles(sample, file1);
//        checkSampleHasFiles(sample, anotherFile1);
//        checkSampleInFiles(sample, file1, anotherFile1);
//
//        // add a different file
//        File file2 = new File(2);
//        sample.addFile(file2);
//        checkSampleHasFiles(sample, file1, file2);
//        checkSampleInFiles(sample, file1, file2);
//    }

    private void checkSampleHasFiles(Sample sample, File... files) {
        Set<File> sampleFiles = sample.getFiles();
        assertThat(sampleFiles, hasSize(files.length));
        assertThat(sampleFiles, containsInAnyOrder(files));
    }

    private void checkSampleInFiles(Sample sample, File... files) {
        for (File file : files) {
            Set<Sample> fileSamples = file.getSamples();
            assertThat(fileSamples, hasSize(1));
            assertThat(sample, isIn(fileSamples));
        }
    }
}