package uk.ac.ebi.eva.commons.models.metadata;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by tom on 07/10/15.
 */
public class StudyTest {

    Study x, y, z, notx;

    @Before
    public void setUp() {
        x = new Study("This is a title", "aliasA", "a great study", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        y = new Study("This is a title", "aliasA", "a great study", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        z = new Study("This is a title", "aliasA", "a great study", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        notx = new Study("This is a different title", "aliasB", "an ok study", Study.Material.OTHER, Study.Scope.COMMUNITY);
    }

    @Test
    public void testAddRun() {
        // the study x initially has no file generators
        assertThat(x.getFileGenerators(), empty());

        // add one run to the study
        Run run1 = new Run("Run1");
        x.addFileGenerator(run1);
        checkStudyHasFileGenerators(x, run1);
        checkStudyInFileGenerators(x, run1);

        // add again the same run object to the study
        x.addFileGenerator(run1);
        checkStudyHasFileGenerators(x, run1);
        checkStudyInFileGenerators(x, run1);

        // add again the same run in a different object instance
        Run anotherRun1 = new Run("Run1");
        x.addFileGenerator(anotherRun1);
        checkStudyHasFileGenerators(x, run1);
        checkStudyHasFileGenerators(x, anotherRun1);
        checkStudyInFileGenerators(x, run1, anotherRun1);

        // add another run, array and analysis
        Run run2 = new Run("Run2");
        x.addFileGenerator(run2);
        checkStudyHasFileGenerators(x, run1, run2);
        checkStudyInFileGenerators(x, run1, run2);
    }

    @Test
    public void testAddArray() {
        // the study x initially has no file generators
        assertThat(x.getFileGenerators(), empty());

        // add one array to the dataset
        Array array1 = new Array("Array1");
        x.addFileGenerator(array1);
        checkStudyHasFileGenerators(x, array1);
        checkStudyInFileGenerators(x, array1);

        // add again the same array object to the dataset
        x.addFileGenerator(array1);
        checkStudyHasFileGenerators(x, array1);
        checkStudyInFileGenerators(x, array1);

        // add again the same array in a different object instance
        Array anotherArray1 = new Array("Array1");
        x.addFileGenerator(anotherArray1);
        checkStudyHasFileGenerators(x, array1);
        checkStudyHasFileGenerators(x, anotherArray1);
        checkStudyInFileGenerators(x, array1, anotherArray1);

        // add another array
        Array array2 = new Array("Array2");
        x.addFileGenerator(array2);
        checkStudyHasFileGenerators(x, array1, array2);
        checkStudyInFileGenerators(x, array1, array2);
    }

    @Test
    public void testAddAnalysis() {
        // the study x initially has no file generators
        assertThat(x.getFileGenerators(), empty());

        // add one analysis to the dataset
        Analysis analysis1 = new Analysis("Analysis1", "Analysis1", "Description");
        x.addFileGenerator(analysis1);
        checkStudyHasFileGenerators(x, analysis1);
        checkStudyInFileGenerators(x, analysis1);

        // add again the same analysis object to the dataset
        x.addFileGenerator(analysis1);
        checkStudyHasFileGenerators(x, analysis1);
        checkStudyInFileGenerators(x, analysis1);

        // add again the same analysis in a different object instance
        Analysis anotherAnalysis1 = new Analysis("Analysis1", "Analysis1", "Description");
        x.addFileGenerator(anotherAnalysis1);
        checkStudyHasFileGenerators(x, analysis1);
        checkStudyHasFileGenerators(x, anotherAnalysis1);
        checkStudyInFileGenerators(x, analysis1, anotherAnalysis1);

        // add another analysis
        Analysis analysis2 = new Analysis("Analysis2", "Analysis2", "Description");
        x.addFileGenerator(analysis2);
        checkStudyHasFileGenerators(x, analysis1, analysis2);
        checkStudyInFileGenerators(x, analysis1, analysis2);
    }

    @Test
    public void testAddRunArrayAndAnalysis() throws Exception {
        // the study x initially has no file generators
        assertThat(x.getFileGenerators(), empty());

        // add one run, array, and analysis to the study
        Run run1 = new Run("Run1");
        x.addFileGenerator(run1);
        Array array1 = new Array("Array1");
        x.addFileGenerator(array1);
        Analysis analysis1 = new Analysis("Analysis1", "Analysis1", "Description");
        x.addFileGenerator(analysis1);
        checkStudyHasFileGenerators(x, run1, array1, analysis1);
        checkStudyInFileGenerators(x, run1, array1, analysis1);

        // add another run, array and analysis
        Run run2 = new Run("Run2");
        x.addFileGenerator(run2);
        Array array2 = new Array("Array2");
        x.addFileGenerator(array2);
        Analysis analysis2 = new Analysis("Analysis2", "Analysis2", "Description");
        x.addFileGenerator(analysis2);
        checkStudyHasFileGenerators(x, run1, array1, analysis1, run2, array2, analysis2);
        checkStudyInFileGenerators(x, run1, array1, analysis1, run2, array2, analysis2);
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
    public void testSetStudyAccessionBadAccs() {
        Study study = new Study("Some study", "PRJEB123", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);

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
    public void testSetStudyAccessionGoodAccs() {
        Study study = new Study("Some study", "PRJEB123", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);

        assertEquals(attemptSetAccession(study, "PRJEB123"), null);
        assertEquals(attemptSetAccession(study, "PRJEA324234"), null);
        assertEquals(attemptSetAccession(study, "PRJNA346"), null);
        assertEquals(attemptSetAccession(study, "PRJEB1"), null);
        assertEquals(attemptSetAccession(study, "PRJEA1324"), null);
        assertEquals(attemptSetAccession(study, "PRJEB632"), null);
    }

    private Throwable attemptSetAccession(Study study, String testAcc) {
        Throwable e = null;
        try {
            study.setStudyAccession(testAcc);
        } catch (Throwable ex) {
            e = ex;
        }
        return e;
    }

    @Test
    public void testAddPublication() {
        Study study = new Study("Some study", "PRJEB123", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);

        Publication publication1 = new Publication("test title", "test journal", "1",
                Arrays.asList("Mrs Example", "Mr Scientist", "Professor Java"), "1234", "Pubmed");
        Publication publication2 = new Publication("this is a title", "some journal", "2",
                Arrays.asList("Dr Researcher", "Ms Biologist", "Mr Biologist"), "1235", "Pubmed");
        study.addPublication(publication1);
        study.addPublication(publication2);

        Set<Publication> pubs = new HashSet<>();
        pubs.add(publication1);
        pubs.add(publication2);

        assertThat(study.getPublications(), hasSize(pubs.size()));
        assertThat(study.getPublications(), containsInAnyOrder(pubs.toArray()));

        for (Publication publication : pubs) {
            assertThat(publication.getStudies(), containsInAnyOrder(study));
        }
    }

    @Test
    public void testSetPublication() {
        Study study = new Study("Some study", "PRJEB123", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);

        Publication publication1 = new Publication("test title", "test journal", "1", new ArrayList<String>(), "1234", "Pubmed");
        Publication publication2 = new Publication("this is a title", "some journal", "2", new ArrayList<String>(), "1235", "Pubmed");
        Set<Publication> pubs = new HashSet<>();
        pubs.add(publication1);
        pubs.add(publication2);

        study.setPublications(pubs);

        assertThat(study.getPublications(), hasSize(pubs.size()));
        assertThat(study.getPublications(), containsInAnyOrder(pubs.toArray()));

        for (Publication publication : pubs) {
            assertThat(publication.getStudies(), containsInAnyOrder(study));
        }
    }

    @Test
    public void testAddCentre() {
        Study study = new Study("Some study", "PRJEB123", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);

        Organisation organisation1 = new Organisation("EBI_test", "Hinxton");
        study.setCentre(organisation1);

        assertEquals(study.getCentre(), organisation1);
    }

    @Test
    public void testAddChildStudy() {
        Study pStudy = new Study("Some study", "PRJEB12345", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);

        Study cStudy1 = new Study("Some study", "PRJEB2345", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);
        Study cStudy2 = new Study("Some study", "PRJEB2354", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);

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

        for (Study cStudy : cStudies) {
            assertEquals(cStudy.getParentStudy(), pStudy);
        }

    }

    private void testAddChildStudyHelper(Study pStudy, Set studies) {
        assertThat(pStudy.getChildStudies(), hasSize(studies.toArray().length));
        assertThat(pStudy.getChildStudies(), containsInAnyOrder(studies.toArray()));
    }

    @Test
    /**
     * A class is equal to itself.
     */
    public void testEqual_ToSelf() {
        assertTrue("Class equal to itself.", x.equals(x));
    }

    /**
     * x.equals(WrongType) must return false;
     */
    @Test
    public void testPassIncompatibleType_isFalse() {
        assertFalse("Passing incompatible object to equals should return false", x.equals("string"));
    }

    /**
     * x.equals(null) must return false;
     */
    @Test
    public void testNullReference_isFalse() {
        assertFalse("Passing null to equals should return false", x.equals(null));
    }

    /**
     * 1. x, x.equals(x) must return true. 2. x and y, x.equals(y) must return
     * true if and only if y.equals(x) returns true.
     */
    @Test
    public void testEquals_isReflexive_isSymmetric() {
        assertTrue("Reflexive test fail x,y", x.equals(y));
        assertTrue("Symmetric test fail y", y.equals(x));
    }

    /**
     * 1. x.equals(y) returns true 2. y.equals(z) returns true 3. x.equals(z)
     * must return true
     */
    @Test
    public void testEquals_isTransitive() {
        assertTrue("Transitive test fails x,y", x.equals(y));
        assertTrue("Transitive test fails y,z", y.equals(z));
        assertTrue("Transitive test fails x,z", x.equals(z));
    }

    /**
     * Repeated calls to equals consistently return true or false.
     */
    @Test
    public void testEquals_isConsistent() {
        assertTrue("Consistent test fail x,y", x.equals(y));
        assertTrue("Consistent test fail x,y", x.equals(y));
        assertTrue("Consistent test fail x,y", x.equals(y));
        assertFalse(notx.equals(x));
        assertFalse(notx.equals(x));
        assertFalse(notx.equals(x));
    }

    /**
     * Repeated calls to hashcode should consistently return the same integer.
     */
    @Test
    public void testHashcode_isConsistent() {
        int initial_hashcode = x.hashCode();

        assertEquals("Consistent hashcode test fails", initial_hashcode, x.hashCode());
        assertEquals("Consistent hashcode test fails", initial_hashcode, x.hashCode());
    }

    /**
     * Objects that are equal using the equals method should return the same
     * integer.
     */
    @Test
    public void testHashcode_twoEqualsObjects_produceSameNumber() {
        int xhashcode = x.hashCode();
        int yhashcode = y.hashCode();

        assertEquals("Equal object, return equal hashcode test fails", xhashcode, yhashcode);
    }

    /**
     * A more optimal implementation of hashcode ensures that if the objects are
     * unequal different integers are produced.
     */
    @Test
    public void testHashcode_twoUnEqualObjects_produceDifferentNumber() {
        int xhashcode = x.hashCode();
        int notxHashcode = notx.hashCode();

        assertNotEquals("Equal object, return unequal hashcode test fails", xhashcode, notxHashcode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelfChild() {
        x.addChildStudy(x);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSelfParent() {
        x.addChildStudy(x);
    }

}
