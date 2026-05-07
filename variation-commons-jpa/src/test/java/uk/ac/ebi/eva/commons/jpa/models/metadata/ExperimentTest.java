package uk.ac.ebi.eva.commons.jpa.models.metadata;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by parce on 08/10/15.
 */
public class ExperimentTest {

    @Test
    public void testAddRun() throws Exception {
        // create a experiment without runs
        Experiment experiment = new Experiment("Experiment1", null, null, null, null, null, null, null, null, null, null);
        assertThat(experiment.getRuns(), empty());
        Study study = new Study("Some study", "PRJEB12345", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);

        // add one run to the experiment
        String run1Alias = "Run1";
        Run run1 = new Run(run1Alias);
        experiment.addRun(run1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentInRun(experiment, run1);

        // add again the same run object to the experiment
        experiment.addRun(run1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentInRun(experiment, run1);

        // add again the same run in a different object instance
        Run anotherRun1 = new Run(run1Alias);
        experiment.addRun(anotherRun1);
        checkExperimentHasRun(experiment, run1);
        checkExperimentHasRun(experiment, anotherRun1);
        checkExperimentInRun(experiment, run1, anotherRun1);

        // add another run, experiment and array
        String run2Alias = "Run2";
        Run run2 = new Run(run2Alias);
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