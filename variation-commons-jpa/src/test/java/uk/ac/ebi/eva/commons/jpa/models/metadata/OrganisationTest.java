package uk.ac.ebi.eva.commons.jpa.models.metadata;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * Created by tom on 15/10/15.
 */
public class OrganisationTest {

    Organisation x, y, z, notx;

    @Before
    public void setUp() {
        x = new Organisation("Sanger", "Wellcome Genome Campus");
        y = new Organisation("Sanger", "Wellcome Genome Campus");
        z = new Organisation("Sanger", "Wellcome Genome Campus");
        notx = new Organisation("Uni of Cam", "Cambridge");
    }

    @Test
    public void testSetEmailGoodEmails() {
        Organisation testOrganisation = new Organisation("Sanger", "Wellcome Genome Campus");

        assertEquals(attemptSetEmail(testOrganisation, null), null);
        assertEquals(attemptSetEmail(testOrganisation, "test1@domain.com"), null);
        assertEquals(attemptSetEmail(testOrganisation, "abcd@gmail.co.uk"), null);
        assertEquals(attemptSetEmail(testOrganisation, "example@ebi.ac.uk"), null);
        assertEquals(attemptSetEmail(testOrganisation, "asasdf.asdfadsf@alesefd.uk"), null);
        assertEquals(attemptSetEmail(testOrganisation, "testing_gfasdf123@yahoo.com"), null);
    }

    @Test
    public void testSetEmailBadEmails() {
        Organisation testOrganisation = new Organisation("Sanger", "Wellcome Genome Campus");

        assertThat(attemptSetEmail(testOrganisation, "notanemail"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(testOrganisation, ""), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(testOrganisation, "123"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(testOrganisation, "test_gmail.com"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(testOrganisation, "test@ebi"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(testOrganisation, "@gmail.com"), instanceOf(IllegalArgumentException.class));
        assertThat(attemptSetEmail(testOrganisation, "gmail.com"), instanceOf(IllegalArgumentException.class));
    }

    private Throwable attemptSetEmail(Organisation organisation, String email) {
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
     * 1. x, x.equals(x) must return true.
     * 2. x and y, x.equals(y) must return true if and only if y.equals(x) returns true.
     */
    @Test
    public void testEquals_isReflexive_isSymmetric() {
        assertTrue("Reflexive test fail x,y", x.equals(y));
        assertTrue("Symmetric test fail y", y.equals(x));
    }

    /**
     * 1. x.equals(y) returns true
     * 2. y.equals(z) returns true
     * 3. x.equals(z) must return true
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
     * Objects that are equal using the equals method should return the same integer.
     */
    @Test
    public void testHashcode_twoEqualsObjects_produceSameNumber() {
        int xhashcode = x.hashCode();
        int yhashcode = y.hashCode();

        assertEquals("Equal object, return equal hashcode test fails", xhashcode, yhashcode);
    }

    /**
     * A more optimal implementation of hashcode ensures
     * that if the objects are unequal different integers are produced.
     */
    @Test
    public void testHashcode_twoUnEqualObjects_produceDifferentNumber() {
        int xhashcode = x.hashCode();
        int notxHashcode = notx.hashCode();

        assertTrue("Equal object, return unequal hashcode test fails", !(xhashcode == notxHashcode));
    }

}
