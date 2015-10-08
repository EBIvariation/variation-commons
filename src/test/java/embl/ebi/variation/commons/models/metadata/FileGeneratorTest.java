package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by parce on 07/10/15.
 */
public class FileGeneratorTest {
//
//    @Test
//    public void testAddFile() throws Exception {
//        // create a file generator without files
//        FileGenerator generator = new FileGenerator(1);
//        assertThat(generator.getFiles(), nullValue());
//
//        // add one file to the generator
//        File file1 = new File(1);
//        generator.addFile(file1);
//        checkGeneratorHasFiles(generator, file1);
//        checkGeneratorInFiles(generator, file1);
//
//        // add again the same file object to the generator
//        generator.addFile(file1);
//        checkGeneratorHasFiles(generator, file1);
//        checkGeneratorInFiles(generator, file1);
//
//        // add again the same file in a different object instance
//        File anotherFile1 = new File(1);
//        generator.addFile(anotherFile1);
//        checkGeneratorHasFiles(generator, file1);
//        checkGeneratorInFiles(generator, file1);
//        checkGeneratorHasFiles(generator, anotherFile1);
//        checkGeneratorInFiles(generator, anotherFile1);
//
//        // add a different file
//        File file2 = new File(2);
//        generator.addFile(file2);
//        checkGeneratorHasFiles(generator, file1, file2);
//        checkGeneratorInFiles(generator, file1, file2);
//    }

    private void checkGeneratorHasFiles(FileGenerator generator, File... files) {
        Set<File> generatorFiles = generator.getFiles();
        assertThat(generatorFiles, hasSize(files.length));
        assertThat(generatorFiles, containsInAnyOrder(files));
    }

    private void checkGeneratorInFiles(FileGenerator generator, File... files) {
        for (File file : files) {
            Set<FileGenerator> fileGenerators = file.getFileGenerators();
            assertThat(fileGenerators, hasSize(1));
            assertThat(generator, isIn(fileGenerators));
        }
    }

    @Test
    public void testAddDataset() throws Exception {
//        // create a file generator without dataset
//        FileGenerator generator = new FileGenerator(1);
//        assertThat(generator.getDataset(), nullValue());
//
//        // set the dataset
//        Dataset dataset1 = new Dataset(1);
//        generator.setDataset(dataset1);
//        checkGeneratorHasDataset(generator, dataset1);
//        checkGeneratorInDataset(generator, dataset1);
//
//        // change the dataset
//        Dataset dataset2 = new Dataset(2);
//        generator.setDataset(dataset2);
//        checkGeneratorHasDataset(generator, dataset2);
//        checkGeneratorInDataset(generator, dataset2);

    }

    private void checkGeneratorInDataset(FileGenerator generator, Dataset dataset) {
        assertThat(generator, isIn(dataset.getFileGenerators()));
    }

    private void checkGeneratorHasDataset(FileGenerator generator, Dataset dataset){
        assertEquals(generator.getDataset(), dataset);
    }
}