package embl.ebi.variation.commons.models.metadata;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by tom on 15/10/15.
 */
public class PublicationTest {


    static final class Fixture {
        static Publication x = new Publication("1234", "Pubmed", "A good article", "PlosOne", "2", Arrays.asList("Mrs Example", "Mr Scientist", "Professor Java"));
        static Publication y = new Publication("1234", "Pubmed", "A good article", "PlosOne", "2", Arrays.asList("Mrs Example", "Mr Scientist", "Professor Java"));
        static Publication z = new Publication("1234", "Pubmed", "A good article", "PlosOne", "2", Arrays.asList("Mrs Example", "Mr Scientist", "Professor Java"));
        static Publication notx = new Publication("thisisanid", "Pubmed", "A Title", "Nature", "1", Arrays.asList("Dr Researcher", "Ms Biologist", "Mr Biologist"));
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
