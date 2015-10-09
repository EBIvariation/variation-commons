package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by parce on 07/10/15.
 */
public class FileGeneratorTest {

    @Test
    public void testAddFile() throws Exception {
        // create a file generator without files
        FileGenerator generator = new Run("Run1");
        assertThat(generator.getFiles(), empty());

        // add one file to the generator
        File file1 = new File("file1.vcf", "vcf", "76ysdfhdf76sd6f78sd");
        generator.addFile(file1);
        checkGeneratorHasFiles(generator, file1);
        checkGeneratorInFiles(generator, file1);

        // add again the same file object to the generator
        generator.addFile(file1);
        checkGeneratorHasFiles(generator, file1);
        checkGeneratorInFiles(generator, file1);

        // add again the same file in a different object instance
        File anotherFile1 = new File("file1.vcf", "vcf", "76ysdfhdf76sd6f78sd");
        generator.addFile(anotherFile1);
        checkGeneratorHasFiles(generator, file1);
        checkGeneratorHasFiles(generator, anotherFile1);
        checkGeneratorInFiles(generator, file1, anotherFile1);

        // add a different file
        File file2 = new File("file1.vcf", "vcf", "aDifferentMd5");
        generator.addFile(file2);
        checkGeneratorHasFiles(generator, file1, file2);
        checkGeneratorInFiles(generator, file1, file2);
    }

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
}