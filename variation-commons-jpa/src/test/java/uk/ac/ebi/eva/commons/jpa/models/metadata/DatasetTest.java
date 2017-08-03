package uk.ac.ebi.eva.commons.jpa.models.metadata;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by parce on 08/10/15.
 */
public class DatasetTest {

    @Test
    public void testAddFileGenerator() throws Exception {
        // create a dataset without file generators
        Dataset dataset = new Dataset("Center", false, 0, null, false);
        assertThat(dataset.getFileGenerators(), empty());

        // add one run to the dataset
        Run run1 = new Run("Run1");
        dataset.addFileGenerator(run1);
        checkDatasetHasFileGenerators(dataset, run1);
        checkDatasetInFileGenerators(dataset, run1);
        // add again the same run object to the dataset
        dataset.addFileGenerator(run1);
        checkDatasetHasFileGenerators(dataset, run1);
        checkDatasetInFileGenerators(dataset, run1);
        // add again the same run in a different object instance
        Run anotherRun1 = new Run("Run1");
        dataset.addFileGenerator(anotherRun1);
        checkDatasetHasFileGenerators(dataset, run1);
        checkDatasetHasFileGenerators(dataset, anotherRun1);
        checkDatasetInFileGenerators(dataset, run1, anotherRun1);

        // add one array to the dataset
        Array array1 = new Array("Array1");
        dataset.addFileGenerator(array1);
        checkDatasetHasFileGenerators(dataset, run1, array1);
        checkDatasetInFileGenerators(dataset, run1, array1);
        // add again the same array object to the dataset
        dataset.addFileGenerator(array1);
        checkDatasetHasFileGenerators(dataset, run1, array1);
        checkDatasetInFileGenerators(dataset, run1, array1);
        // add again the same array in a different object instance
        Array anotherArray1 = new Array("Array1");
        dataset.addFileGenerator(anotherArray1);
        checkDatasetHasFileGenerators(dataset, run1, array1);
        checkDatasetHasFileGenerators(dataset, run1, anotherArray1);
        checkDatasetInFileGenerators(dataset, run1, array1, anotherArray1);

        // add one analysis to the dataset
        Analysis analysis1 = new Analysis("Analysis1", "Analysis1", "Description", null, null, null, false, null);
        dataset.addFileGenerator(analysis1);
        checkDatasetHasFileGenerators(dataset, run1, array1, analysis1);
        checkDatasetInFileGenerators(dataset, run1, array1, analysis1);
        // add again the same analysis object to the dataset
        dataset.addFileGenerator(analysis1);
        checkDatasetHasFileGenerators(dataset, run1, array1, analysis1);
        checkDatasetInFileGenerators(dataset, run1, array1, analysis1);
        // add again the same analysis in a different object instance
        Analysis anotherAnalysis1 = new Analysis("Analysis1", "Analysis1", "Description", null, null, null, false, null);
        dataset.addFileGenerator(anotherAnalysis1);
        checkDatasetHasFileGenerators(dataset, run1, array1, analysis1);
        checkDatasetHasFileGenerators(dataset, run1, array1, anotherAnalysis1);
        checkDatasetInFileGenerators(dataset, run1, array1, analysis1, anotherAnalysis1);

        // add another run and dataset
        Run run2 = new Run("Run2");
        dataset.addFileGenerator(run2);
        Array array2 = new Array("Array2");
        dataset.addFileGenerator(array2);
        Analysis analysis2 = new Analysis("Analysis2", "Analysis2", "Description", null, null, null, false, null);
        dataset.addFileGenerator(analysis2);
        checkDatasetHasFileGenerators(dataset, run1, array1, analysis1, run2, array2, analysis2);
        checkDatasetInFileGenerators(dataset, run1, array1, analysis1, run2, array2, analysis2);
    }

    private void checkDatasetHasFileGenerators(Dataset dataset, FileGenerator... generators) {
        Set<FileGenerator> datasetFileGenerators = dataset.getFileGenerators();
        assertThat(datasetFileGenerators, hasSize(generators.length));
        assertThat(datasetFileGenerators, containsInAnyOrder(generators));
    }

    private void checkDatasetInFileGenerators(Dataset dataset, FileGenerator... generators) {
        for (FileGenerator generator : generators) {
            assertEquals(dataset, generator.getDataset());
        }
    }
}