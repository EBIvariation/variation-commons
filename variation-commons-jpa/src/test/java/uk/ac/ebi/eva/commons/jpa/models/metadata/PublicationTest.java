package uk.ac.ebi.eva.commons.jpa.models.metadata;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by tom on 15/10/15.
 */
public class PublicationTest {

    Publication x, y, z, notx;

    @BeforeEach
    public void setUp() {
        x = new Publication("A good article", "PlosOne", "2", Arrays.asList("Mrs Example", "Mr Scientist", "Professor Java"), "1234", "Pubmed");
        y = new Publication("A good article", "PlosOne", "2", Arrays.asList("Mrs Example", "Mr Scientist", "Professor Java"), "1234", "Pubmed");
        z = new Publication("A good article", "PlosOne", "2", Arrays.asList("Mrs Example", "Mr Scientist", "Professor Java"), "1234", "Pubmed");
        notx = new Publication("A Title", "Nature", "1", Arrays.asList("Dr Researcher", "Ms Biologist", "Mr Biologist"), "thisisanid", "Pubmed");
    }

    @Test
    /**
     * A class is equal to itself.
     */
    public void testEqual_ToSelf() {
        assertTrue(x.equals(x), "Class equal to itself.");
    }

    /**
     * x.equals(WrongType) must return false;
     */
    @Test
    public void testPassIncompatibleType_isFalse() {
        assertFalse(x.equals("string"), "Passing incompatible object to equals should return false");
    }

    /**
     * x.equals(null) must return false;
     */
    @Test
    public void testNullReference_isFalse() {
        assertFalse(x.equals(null), "Passing null to equals should return false");
    }

    /**
     * 1. x, x.equals(x) must return true. 2. x and y, x.equals(y) must return
     * true if and only if y.equals(x) returns true.
     */
    @Test
    public void testEquals_isReflexive_isSymmetric() {
        assertTrue(x.equals(y), "Reflexive test fail x,y");
        assertTrue(y.equals(x), "Symmetric test fail y");

    }

    /**
     * 1. x.equals(y) returns true 2. y.equals(z) returns true 3. x.equals(z)
     * must return true
     */
    @Test
    public void testEquals_isTransitive() {
        assertTrue(x.equals(y), "Transitive test fails x,y");
        assertTrue(y.equals(z), "Transitive test fails y,z");
        assertTrue(x.equals(z), "Transitive test fails x,z");
    }

    /**
     * Repeated calls to equals consistently return true or false.
     */
    @Test
    public void testEquals_isConsistent() {
        assertTrue(x.equals(y), "Consistent test fail x,y");
        assertTrue(x.equals(y), "Consistent test fail x,y");
        assertTrue(x.equals(y), "Consistent test fail x,y");
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

        assertEquals(initial_hashcode, x.hashCode(), "Consistent hashcode test fails");
        assertEquals(initial_hashcode, x.hashCode(), "Consistent hashcode test fails");
    }

    /**
     * Objects that are equal using the equals method should return the same
     * integer.
     */
    @Test
    public void testHashcode_twoEqualsObjects_produceSameNumber() {
        int xhashcode = x.hashCode();
        int yhashcode = y.hashCode();

        assertEquals(xhashcode, yhashcode, "Equal object, return equal hashcode test fails");
    }

    /**
     * A more optimal implementation of hashcode ensures that if the objects are
     * unequal different integers are produced.
     */
    @Test
    public void testHashcode_twoUnEqualObjects_produceDifferentNumber() {
        int xhashcode = x.hashCode();
        int notxHashcode = notx.hashCode();

        assertTrue(!(xhashcode == notxHashcode), "Equal object, return unequal hashcode test fails");
    }

}
