package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by parce on 08/10/15.
 */
public class DatasetTest {

    @Test
    public void testAddFileGenerator() throws Exception {
        // create a dataset without file generators
        String datasetSubmissionId = "EGAD00001";
        Dataset dataset = new Dataset(datasetSubmissionId, null, 0, null, false, 0, null, null, null, null, null, null, false, null);
        assertThat(dataset.getFileGenerators(), empty());

        // add one run to the dataset
        String runStableId1 = "EGAR00001";
        Run run1 = new Run(runStableId1, null, null, null, null, null, null, null);
        dataset.addFileGenerator(run1);
        checkDatasetHasFileGenerators(dataset, run1);
        checkDatasetInFileGenerators(dataset, run1);
        // add again the same run object to the dataset
        dataset.addFileGenerator(run1);
        checkDatasetHasFileGenerators(dataset, run1);
        checkDatasetInFileGenerators(dataset, run1);
        // add again the same run in a different object instance
        Run anotherRun1 = new Run(runStableId1, null, null, null, null, null, null, null);
        dataset.addFileGenerator(anotherRun1);
        checkDatasetHasFileGenerators(dataset, run1);
        checkDatasetHasFileGenerators(dataset, anotherRun1);
        checkDatasetInFileGenerators(dataset, run1, anotherRun1);

//        // add one array to the dataset
//        Array array1 = new Array(1);
//        dataset.addFileGenerator(array1);
//        checkDatasetHasFileGenerators(dataset, run1, array1);
//        checkDatasetInFileGenerators(dataset, run1, array1);
//        // add again the same array object to the dataset
//        dataset.addFileGenerator(array1);
//        checkDatasetHasFileGenerators(dataset, run1, array1);
//        checkDatasetInFileGenerators(dataset, run1, array1);
//        // add again the same array in a different object instance
//        Array anotherArray1 = new Array(1);
//        dataset.addFileGenerator(anotherArray1);
//        checkDatasetHasFileGenerators(dataset, run1, array1);
//        checkDatasetHasFileGenerators(dataset, run1, anotherArray1);
//        checkDatasetInFileGenerators(dataset, run1, array1, anotherArray1);

        // add one analysis to the dataset
        String analysisAccession1 = "EGAA00001";
        Analysis analysis1 = new Analysis(analysisAccession1, null, null, null, null, null, null, null, false);
        dataset.addFileGenerator(analysis1);
        checkDatasetHasFileGenerators(dataset, run1, analysis1);
        checkDatasetInFileGenerators(dataset, run1, analysis1);
        // add again the same analysis object to the dataset
        dataset.addFileGenerator(analysis1);
        checkDatasetHasFileGenerators(dataset, run1, analysis1);
        checkDatasetInFileGenerators(dataset, run1, analysis1);
        // add again the same analysis in a different object instance
        Analysis anotherAnalysis1 = new Analysis(analysisAccession1, null, null, null, null, null, null, null, false);
        dataset.addFileGenerator(anotherAnalysis1);
        checkDatasetHasFileGenerators(dataset, run1, analysis1);
        checkDatasetHasFileGenerators(dataset, run1, anotherAnalysis1);
        checkDatasetInFileGenerators(dataset, run1, analysis1, anotherAnalysis1);

        // add another run and dataset
        String runStableId2 = "EGAR00002";
        Run run2 = new Run(runStableId2, null, null, null, null, null, null, null);
        dataset.addFileGenerator(run2);
//        Array array2 = new Array(2);
//        dataset.addFileGenerator(array2);
        String analysisAccession2 = "EGAA00002";
        Analysis analysis2 = new Analysis(analysisAccession2, null, null, null, null, null, null, null, false);
        dataset.addFileGenerator(analysis2);
        checkDatasetHasFileGenerators(dataset, run1, analysis1, run2, analysis2);
        checkDatasetInFileGenerators(dataset, run1, analysis1, run2, analysis2);
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