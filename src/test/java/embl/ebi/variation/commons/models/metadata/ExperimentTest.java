package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by parce on 08/10/15.
 */
public class ExperimentTest {

    @Test
    public void testAddRun() throws Exception {
        // create a experiment without runs
        String experimentStableId = "EGAE000001";
        Experiment experiment = new Experiment(experimentStableId, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(experiment.getRuns(), empty());

        // add one run to the experiment
        String runStableId1 = "EGAR00001";
        Run run1 = new Run(runStableId1, null, null, null, null, null, null, null);
        experiment.addRun(run1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentInRun(experiment, run1);

        // add again the same run object to the experiment
        experiment.addRun(run1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentInRun(experiment, run1);

        // add again the same run in a different object instance
        Run anotherRun1 = new Run(runStableId1, null, null, null, null, null, null, null);
        experiment.addRun(anotherRun1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentHasRun(experiment, anotherRun1);
        checkExperimentInRun(experiment, run1, anotherRun1);

        // add another run, experiment and array
        String runStableId2 = "EGAR00002";
        Run run2 = new Run(runStableId2, null, null, null, null, null, null, null);
        experiment.addRun(run2);
        checkExperimentHasRun(experiment, run1, run2);
        checkExperimentInRun(experiment, run1, run2);
    }

    private void checkExperimentHasRun(Experiment experiment, Run... runs) {
        Set<Run> experimentRuns = experiment.getRuns();
        assertThat(experimentRuns, hasSize(runs.length));
        assertThat(experimentRuns, containsInAnyOrder(runs));
    }

    private void checkExperimentInRun(Experiment experiment, Run... runs) {
        for (Run run : runs) {
            assertEquals(experiment, run.getExperiment());
        }
    }
}