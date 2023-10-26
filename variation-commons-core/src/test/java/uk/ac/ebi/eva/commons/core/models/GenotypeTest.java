package uk.ac.ebi.eva.commons.core.models;

import org.junit.Test;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;

import static org.junit.Assert.assertEquals;

public class GenotypeTest {

    @Test
    public void testChangeRefAltToUpperCase() {
        Genotype genotype = new Genotype("", "a", "t");

        assertEquals("A", genotype.getReference());
        assertEquals("T", genotype.getAlternate());
    }


}
