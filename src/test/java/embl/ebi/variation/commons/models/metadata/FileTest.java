package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by parce on 07/10/15.
 */
public class FileTest {

//    @Test
//    public void testAddSample() throws Exception {
//        // create a file without samples
//        File file = new File(1);
//        assertThat(file.getSamples(), nullValue());
//
//        // add one file to the sample
//        Sample sample1 = new Sample(1);
//        file.addSample(sample1);
//        checkFileHasSamples(file, sample1);
//        checkFileInSamples(file, sample1);
//
//        // add the same file object to the sample
//        file.addSample(sample1);
//        checkFileHasSamples(file, sample1);
//        checkFileInSamples(file, sample1);
//
//        // add a different object containing the same file
//        Sample anotherSample1 = new Sample(1);
//        file.addSample(anotherSample1);
//        checkFileHasSamples(file, sample1);
//        checkFileHasSamples(file, anotherSample1);
//        checkFileInSamples(file, sample1, anotherSample1);
//
//        // add a diferent sample to the file
//        Sample sample2 = new Sample(2);
//        file.addSample(sample2);
//        checkFileHasSamples(file, sample1, sample2);
//        checkFileInSamples(file, sample1, sample2);
//    }


    private void checkFileHasSamples(File file, Sample... samples) {
        Set<Sample> fileSamples = file.getSamples();
        assertThat(fileSamples, hasSize(samples.length));
        assertThat(fileSamples, contains(samples));
    }

    private void checkFileInSamples(File file, Sample... samples) {
        for (Sample sample: samples) {
            Set<File> sampleFiles = sample.getFiles();
            assertThat(sampleFiles, hasSize(1));
            assertThat(sampleFiles, contains(file));
        }
    }

    @Test
//    public void testAddFileGenerator() throws Exception {
//        // create a file without generators
//        File file = new File(1);
//        assertThat(file.getFileGenerators(), nullValue());
//
//        // add a generator of class Run to the file
//        Run run1 = new Run(1);
//        file.addFileGenerator(run1);
//        checkFileHasGenerators(file, run1);
//        checkFileInGenerators(file, run1);
//        // add the same Run to the file
//        file.addFileGenerator(run1);
//        checkFileHasGenerators(file, run1);
//        checkFileInGenerators(file, run1);
//        // add another object containing the same Run
//        Run run1Again = new Run(1);
//        file.addFileGenerator(run1Again);
//        checkFileHasGenerators(file, run1);
//        checkFileHasGenerators(file, run1Again);
//        checkFileInGenerators(file, run1, run1Again);
//
//        // add generator of a Array class with the same id
//        Array array = new Array(1);
//        file.addFileGenerator(array);
//        checkFileHasGenerators(file, run1, array);
//        checkFileInGenerators(file, run1, array);
//        // add the same array again
//        file.addFileGenerator(array);
//        checkFileHasGenerators(file, run1, array);
//        checkFileInGenerators(file, run1, array);
//        // add another array Object with the same id
//        Array array1Again = new Array(1);
//        file.addFileGenerator(array1Again);
//        checkFileHasGenerators(file, run1, array);
//        checkFileHasGenerators(file, run1, array1Again);
//        checkFileInGenerators(file, run1, array, array1Again);
//
//        // add a generator of analysis class
//        Analysis analysis = new Analysis(1);
//        file.addFileGenerator(analysis);
//        checkFileHasGenerators(file, run1, array, analysis);
//        checkFileInGenerators(file, run1, array, analysis);
//        // add the same analysis again
//        file.addFileGenerator(analysis);
//        checkFileHasGenerators(file, run1, array, analysis);
//        checkFileInGenerators(file, run1, array, analysis);
//        // add another analysis with the same id
//        Analysis analysis1Again = new Analysis(1);
//        file.addFileGenerator(analysis1Again);
//        checkFileHasGenerators(file, run1, array, analysis);
//        checkFileHasGenerators(file, run1, array, analysis1Again);
//        checkFileInGenerators(file, run1, array, analysis, analysis1Again);
//
//        // add a different run, analysis, and array
//        Run run2 = new Run(2);
//        file.addFileGenerator(run2);
//        Array array2 = new Array(2);
//        file.addFileGenerator(array2);
//        Analysis analysis2 = new Analysis(2);
//        file.addFileGenerator(analysis2);
//        checkFileHasGenerators(file, run1, array, analysis, run2, array2, analysis2);
//        checkFileInGenerators(file, run1, array, analysis, run2, array2, analysis2);
//
//    }

    private void checkFileHasGenerators(File file, FileGenerator... generators) {
        Set<FileGenerator> fileGenerators = file.getFileGenerators();
        assertThat(fileGenerators, hasSize(generators.length));
        assertThat(fileGenerators, containsInAnyOrder(generators));
    }

    private void checkFileInGenerators(File file, FileGenerator... generators) {
        for (FileGenerator generator : generators) {
            Set<File> generatorFiles = generator.getFiles();
            assertThat(generatorFiles, hasSize(1));
            assertThat(generatorFiles, contains(file));
        }
    }
}