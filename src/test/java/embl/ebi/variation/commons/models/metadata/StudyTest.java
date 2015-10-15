package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Created by tom on 07/10/15.
 */
public class StudyTest {

    static final class Fixture {
        static Study x = new Study("This is a title", "aliasA", "a great study", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        static Study y = new Study("This is a title", "aliasA", "a great study", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        static Study z = new Study("This is a title", "aliasA", "a great study", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        static Study notx = new Study("This is a different title", "aliasB", "an ok study", Study.Material.OTHER, Study.Scope.COMMUNITY);
    }

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

        Publication publication1 = new Publication("1234", "Pubmed", "test title", "test journal", "1", Arrays.asList("Mrs Example", "Mr Scientist", "Professor Java"));
        Publication publication2 = new Publication("1235", "Pubmed", "this is a title", "some journal", "2", Arrays.asList("Dr Researcher", "Ms Biologist", "Mr Biologist"));
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

        Publication publication1 = new Publication("1234", "Pubmed", "test title", "test journal", "1", new ArrayList<String>());
        Publication publication2 = new Publication("1235", "Pubmed", "this is a title", "some journal", "2", new ArrayList<String>());
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

        Organisation organisation1 = new Organisation("EBI_test", "Hinxton");
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

    @Test
    /**
     * A class is equal to itself.
     */
    public void testEqual_ToSelf() {

        assertTrue("Class equal to itself.", Fixture.x.equals(Fixture.x));
    }

    /**
     * x.equals(WrongType) must return false;
     *
     */
    @Test
    public void testPassIncompatibleType_isFalse() {
        assertFalse("Passing incompatible object to equals should return false", Fixture.x.equals("string"));
    }

    /**
     * x.equals(null) must return false;
     *
     */
    @Test
    public void testNullReference_isFalse() {
        assertFalse("Passing null to equals should return false", Fixture.x.equals(null));
    }

    /**
     * 1. x, x.equals(x) must return true.
     * 2. x and y, x.equals(y) must return true if and only if y.equals(x) returns true.
     */
    @Test
    public void testEquals_isReflexive_isSymmetric() {

        assertTrue("Reflexive test fail x,y", Fixture.x.equals(Fixture.y));
        assertTrue("Symmetric test fail y", Fixture.y.equals(Fixture.x));

    }

    /**
     * 1. x.equals(y) returns true
     * 2. y.equals(z) returns true
     * 3. x.equals(z) must return true
     */
    @Test
    public void testEquals_isTransitive() {

        assertTrue("Transitive test fails x,y", Fixture.x.equals(Fixture.y));
        assertTrue("Transitive test fails y,z", Fixture.y.equals(Fixture.z));
        assertTrue("Transitive test fails x,z", Fixture.x.equals(Fixture.z));
    }

    /**
     * Repeated calls to equals consistently return true or false.
     */
    @Test
    public void testEquals_isConsistent() {

        assertTrue("Consistent test fail x,y", Fixture.x.equals(Fixture.y));
        assertTrue("Consistent test fail x,y", Fixture.x.equals(Fixture.y));
        assertTrue("Consistent test fail x,y", Fixture.x.equals(Fixture.y));
        assertFalse(Fixture.notx.equals(Fixture.x));
        assertFalse(Fixture.notx.equals(Fixture.x));
        assertFalse(Fixture.notx.equals(Fixture.x));

    }

    /**
     * Repeated calls to hashcode should consistently return the same integer.
     */
    @Test
    public void testHashcode_isConsistent() {

        int initial_hashcode = Fixture.x.hashCode();

        assertEquals("Consistent hashcode test fails", initial_hashcode, Fixture.x.hashCode());
        assertEquals("Consistent hashcode test fails", initial_hashcode, Fixture.x.hashCode());
    }

    /**
     * Objects that are equal using the equals method should return the same integer.
     */
    @Test
    public void testHashcode_twoEqualsObjects_produceSameNumber() {

        int xhashcode = Fixture.x.hashCode();
        int yhashcode = Fixture.y.hashCode();

        assertEquals("Equal object, return equal hashcode test fails", xhashcode, yhashcode);
    }

    /**
     * A more optimal implementation of hashcode ensures
     * that if the objects are unequal different integers are produced.
     *
     */
    @Test
    public void testHashcode_twoUnEqualObjects_produceDifferentNumber() {

        int xhashcode = Fixture.x.hashCode();
        int notxHashcode = Fixture.notx.hashCode();

        assertTrue("Equal object, return unequal hashcode test fails", !(xhashcode == notxHashcode));
    }

}
