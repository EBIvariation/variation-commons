package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
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
        Run run1 = new Run(study, "Run1");
        study.addFileGenerator(run1);
        checkStudyHasFileGenerators(study, run1);
        checkStudyInFileGenerators(study, run1);
        // add again the same run object to the study
        study.addFileGenerator(run1);
        checkStudyHasFileGenerators(study, run1);
        checkStudyInFileGenerators(study, run1);
        // add again the same run in a different object instance
        Run anotherRun1 = new Run(study, "Run1");
        study.addFileGenerator(anotherRun1);
        checkStudyHasFileGenerators(study, run1);
        checkStudyHasFileGenerators(study, anotherRun1);
        checkStudyInFileGenerators(study, run1, anotherRun1);

        // add one array to the dataset
        Array array1 = new Array(study, "Array1");
        study.addFileGenerator(array1);
        checkStudyHasFileGenerators(study, run1, array1);
        checkStudyInFileGenerators(study, run1, array1);
        // add again the same array object to the dataset
        study.addFileGenerator(array1);
        checkStudyHasFileGenerators(study, run1, array1);
        checkStudyInFileGenerators(study, run1, array1);
        // add again the same array in a different object instance
        Array anotherArray1 = new Array(study, "Array1");
        study.addFileGenerator(anotherArray1);
        checkStudyHasFileGenerators(study, run1, array1);
        checkStudyHasFileGenerators(study, run1, anotherArray1);
        checkStudyInFileGenerators(study, run1, array1, anotherArray1);

        // add one analysis to the dataset
        Analysis analysis1 = new Analysis(study, "Analysis1", "Analysis1", "Description");
        study.addFileGenerator(analysis1);
        checkStudyHasFileGenerators(study, run1, array1, analysis1);
        checkStudyInFileGenerators(study, run1, array1, analysis1);
        // add again the same analysis object to the dataset
        study.addFileGenerator(analysis1);
        checkStudyHasFileGenerators(study, run1, array1, analysis1);
        checkStudyInFileGenerators(study, run1, array1, analysis1);
        // add again the same analysis in a different object instance
        Analysis anotherAnalysis1 = new Analysis(study, "Analysis1", "Analysis1", "Description");
        study.addFileGenerator(anotherAnalysis1);
        checkStudyHasFileGenerators(study, run1, array1, analysis1);
        checkStudyHasFileGenerators(study, run1, array1, anotherAnalysis1);
        checkStudyInFileGenerators(study, run1, array1, analysis1, anotherAnalysis1);

        // add another run, array and analysis
        Run run2 = new Run(study, "Run2");
        study.addFileGenerator(run2);
        Array array2 = new Array(study, "Array2");
        study.addFileGenerator(array2);
        Analysis analysis2 = new Analysis(study, "Analysis2", "Analysis2", "Description");
        study.addFileGenerator(analysis2);
        checkStudyHasFileGenerators(study, run1, array1, analysis1, run2, array2, analysis2);
        checkStudyInFileGenerators(study, run1, array1, analysis1, run2, array2, analysis2);
    }

    private void checkStudyHasFileGenerators(Study study, FileGenerator... generators) {
        Set<FileGenerator> studyFileGenerators = study.getFileGenerators();
        assertThat(studyFileGenerators, hasSize(generators.length));
        assertThat(studyFileGenerators, containsInAnyOrder(generators));
    }

    private void checkStudyInFileGenerators(Study study, FileGenerator... generators) {
        for (FileGenerator generator : generators) {
            assertEquals(generator.study, study);
        }
    }

    @Test
    public void testSetStudyAccessionBadAccs(){
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
    }

    @Test
    public void testSetStudyAccessionGoodAccs(){
        Study study = new Study("PRJEB123", null, null, null, null);

        assertEquals(attemptSetAccession(study, "PRJEB123"), null);
        assertEquals(attemptSetAccession(study, "PRJEA324234"), null);
        assertEquals(attemptSetAccession(study, "PRJNA346"), null);
        assertEquals(attemptSetAccession(study, "PRJEB1"), null);
        assertEquals(attemptSetAccession(study, "PRJEA1324"), null);
        assertEquals(attemptSetAccession(study, "PRJEB632"), null);
    }

    private Throwable attemptSetAccession(Study study,  String testAcc){
        Throwable e = null;
        try {
            study.setStudyAccession(testAcc);
        } catch (Throwable ex) {
            e = ex;
        }
        return e;
    }

    @Test
    public void testAddPublication(){
        Study study = new Study("PRJEB12345", null, null, null, null);

        Publication publication1 = new Publication(1234, "Pubmed", "test title", "test journal");
        Publication publication2 = new Publication(1235, "Pubmed", "this is a title", "some journal");
        study.addPublication(publication1);
        study.addPublication(publication2);

        Set<Publication> pubs = new HashSet<>();
        pubs.add(publication1);
        pubs.add(publication2);

        assertThat(study.getPublications(), hasSize(pubs.size()));
        assertThat(study.getPublications(), containsInAnyOrder(pubs.toArray()));

        for (Publication publication: pubs) {
            assertThat(publication.getStudies(), containsInAnyOrder(study));
        }
    }

    @Test
    public void testSetPublication(){
        Study study = new Study("PRJEB12345", null, null, null, null);

        Publication publication1 = new Publication(1234, "Pubmed", "test title", "test journal");
        Publication publication2 = new Publication(1235, "Pubmed", "this is a title", "some journal");
        Set<Publication> pubs = new HashSet<>();
        pubs.add(publication1);
        pubs.add(publication2);

        study.setPublications(pubs);

        assertThat(study.getPublications(), hasSize(pubs.size()));
        assertThat(study.getPublications(), containsInAnyOrder(pubs.toArray()));

        for (Publication publication: pubs) {
            assertThat(publication.getStudies(), containsInAnyOrder(study));
        }
    }

    @Test
    public void testAddCentre(){
        Study study = new Study("PRJEB12345", null, null, null, null);

        Organisation organisation1 = new Organisation("EBI_test");
        study.setCentre(organisation1);

        assertEquals(study.getCentre(), organisation1);
    }


    @Test
    public void testAddChildStudy(){
        Study pStudy = new Study("PRJEB12345", null, null, null, null);

        Study cStudy1 = new Study("PRJEB2354", null, null, null, null);
        Study cStudy2 = new Study("PRJEB2354", null, null, null, null);

        Set<Study> cStudies = new HashSet<>();

        cStudies.add(cStudy1);
        pStudy.addChildStudy(cStudy1);
        testAddChildStudyHelper(pStudy, cStudies);

        cStudies.add(cStudy1);
        pStudy.addChildStudy(cStudy1);
        testAddChildStudyHelper(pStudy, cStudies);

        cStudies.add(cStudy2);
        pStudy.addChildStudy(cStudy2);
        testAddChildStudyHelper(pStudy, cStudies);

        for(Study cStudy: cStudies){
            assertEquals(cStudy.getParentStudy(), pStudy);
        }

    }

    private void testAddChildStudyHelper(Study pStudy, Set studies) {
        assertThat(pStudy.getChildStudies(), hasSize(studies.toArray().length));
        assertThat(pStudy.getChildStudies(), containsInAnyOrder(studies.toArray()));
    }

}
