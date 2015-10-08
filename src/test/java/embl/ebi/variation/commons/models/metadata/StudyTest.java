package embl.ebi.variation.commons.models.metadata;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by tom on 07/10/15.
 */
public class StudyTest {

    @Test
    public void testAddFileGenerator() throws Exception {
        // create a study without file generators
        Study study = new Study("PRJEA12345", null, null, null, null);
        assertThat(study.getFileGenerators(), empty());

        // add one run to the study
        String runStableId1 = "EGAR00001";
        Run run1 = new Run(runStableId1, null, null, null, null, null, null, null);
        study.addFileGenerator(run1);
        checkStudyHasFileGenerators(study, run1);
        checkStudyInFileGenerators(study, run1);
        // add again the same run object to the study
        study.addFileGenerator(run1);
        checkStudyHasFileGenerators(study, run1);
        checkStudyInFileGenerators(study, run1);
        // add again the same run in a different object instance
        Run anotherRun1 = new Run(runStableId1, null, null, null, null, null, null, null);
        study.addFileGenerator(anotherRun1);
        checkStudyHasFileGenerators(study, run1);
        checkStudyHasFileGenerators(study, anotherRun1);
        checkStudyInFileGenerators(study, run1, anotherRun1);

        // add one analysis to the study
        String analysisAccession1 = "EGAA00001";
        Analysis analysis1 = new Analysis(analysisAccession1, null, null, null, null, null, null, null, false);
        study.addFileGenerator(analysis1);
        checkStudyHasFileGenerators(study, run1, analysis1);
        checkStudyInFileGenerators(study, run1, analysis1);
        // add again the same analysis object to the study
        study.addFileGenerator(analysis1);
        checkStudyHasFileGenerators(study, run1, analysis1);
        checkStudyInFileGenerators(study, run1, analysis1);
        // add again the same analysis in a different object instance
        Analysis anotherAnalysis1 = new Analysis(analysisAccession1, null, null, null, null, null, null, null, false);
        study.addFileGenerator(anotherAnalysis1);
        checkStudyHasFileGenerators(study, run1, analysis1);
        checkStudyHasFileGenerators(study, run1, anotherAnalysis1);
        checkStudyInFileGenerators(study, run1, analysis1, anotherAnalysis1);

        // add another run and study
        String runStableId2 = "EGAR00002";
        Run run2 = new Run(runStableId2, null, null, null, null, null, null, null);
        study.addFileGenerator(run2);
//        Array array2 = new Array(2);
//        study.addFileGenerator(array2);
        String analysisAccession2 = "EGAA00002";
        Analysis analysis2 = new Analysis(analysisAccession2, null, null, null, null, null, null, null, false);
        study.addFileGenerator(analysis2);
        checkStudyHasFileGenerators(study, run1, analysis1, run2, analysis2);
        checkStudyInFileGenerators(study, run1, analysis1, run2, analysis2);
    }

    private void checkStudyHasFileGenerators(Study study, FileGenerator... generators) {
        Set<FileGenerator> studyFileGenerators = study.getFileGenerators();
        assertThat(studyFileGenerators, hasSize(generators.length));
        assertThat(studyFileGenerators, containsInAnyOrder(generators));
    }

    private void checkStudyInFileGenerators(Study study, FileGenerator... generators) {
        for (FileGenerator generator : generators) {
            assertThat(generator.studies, hasItem(study));
        }
    }


    @Test(expected = IllegalArgumentException.class)
    public void testConstructorStudyAccession(){
        Study study = new Study("PRA1234", null, null, null, null);
    }

    @Test
    public void testSetStudyAccession(){
        Study study = new Study("PRJEB123", null, null, null, null);

        assertThat(attemptSetAccession(study, "PRJR1234"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, ""), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "PRJEC123"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "PRJN1234"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "PRJ"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "PRJEBA1"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "PRJEA123A"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "PRJEA"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "PRJNA"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "PR"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "prjea1213"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "prjna256"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetAccession(study, "Prjeb122"), instanceOf(IllegalArgumentException.class));

        assertEquals(attemptSetAccession(study, "PRJEB123"), null);
        assertEquals(attemptSetAccession(study, "PRJEA324234"), null);
        assertEquals(attemptSetAccession(study, "PRJNA346"), null);
        assertEquals(attemptSetAccession(study, "PRJEB1"), null);
        assertEquals(attemptSetAccession(study, "PRJEA1324"), null);
        assertEquals(attemptSetAccession(study, "PRJEB632"), null);

    }

    private Throwable attemptSetAccession(Study study, String testAcc){
        Throwable e = null;
        try {
            study.setStudyAccession(testAcc);
        } catch (Throwable ex) {
            e = ex;
        }
        return e;
    }

}
