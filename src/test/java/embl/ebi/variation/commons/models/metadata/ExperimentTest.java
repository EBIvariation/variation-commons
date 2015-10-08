package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by parce on 08/10/15.
 */
public class ExperimentTest {

    @Test
    public void testAddRun() throws Exception {
        // create a experiment without runs
        Experiment experiment = new Experiment(1);
        assertThat(experiment.getRuns(), nullValue());

        // add one run to the experiment
        Run run1 = new Run(1);
        experiment.addRun(run1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentInRun(experiment, run1);

        // add again the same run object to the experiment
        experiment.addRun(run1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentInRun(experiment, run1);

        // add again the same run in a different object instance
        Run anotherRun1 = new Run(1);
        experiment.addRun(anotherRun1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentHasRun(experiment, anotherRun1);
        checkExperimentInRun(experiment, run1, anotherRun1);

        // add another run, experiment and array
        Run run2 = new Run(2);
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