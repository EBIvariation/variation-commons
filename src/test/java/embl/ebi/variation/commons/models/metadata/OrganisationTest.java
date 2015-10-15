package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;


/**
 * Created by tom on 15/10/15.
 */
public class OrganisationTest {

    static final class Fixture {
        static Organisation x = new Organisation("Sanger", "Wellcome Genome Campus");
        static Organisation y = new Organisation("Sanger", "Wellcome Genome Campus");
        static Organisation z = new Organisation("Sanger", "Wellcome Genome Campus");
        static Organisation notx = new Organisation("Sanger", "Wellcome Genome Campus");
    }

    @Test
    public void testSetEmailGoodEmails(){
        assertEquals(attemptSetEmail(Fixture.x, "test1@domain.com"), null);
        assertEquals(attemptSetEmail(Fixture.x, "abcd@gmail.co.uk"), null);
        assertEquals(attemptSetEmail(Fixture.x, "example@ebi.ac.uk"), null);
        assertEquals(attemptSetEmail(Fixture.x, "asasdf.asdfadsf@alesefd.uk"), null);
        assertEquals(attemptSetEmail(Fixture.x, "testing_gfasdf123@yahoo.com"), null);
    }

    @Test
    public void testSetStudyAccessionBadAccs(){
        assertThat(attemptSetEmail(Fixture.x, "notanemail"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(Fixture.x, ""), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(Fixture.x, "123"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(Fixture.x, "test_gmail.com"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(Fixture.x, "test@ebi"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(Fixture.x, "@gmail.com"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(Fixture.x, "gmail.com"), instanceOf(IllegalArgumentException.class));

    }

    private Throwable attemptSetEmail(Organisation organisation,  String email){
        Throwable e = null;
        try {
            organisation.setEmail(email);
        } catch (Throwable ex) {
            e = ex;
        }
        return e;
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
