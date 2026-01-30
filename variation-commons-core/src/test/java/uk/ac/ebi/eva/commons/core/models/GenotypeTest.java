package uk.ac.ebi.eva.commons.core.models;

import org.junit.jupiter.api.Test;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenotypeTest {

    @Test
    public void testChangeRefAltToUpperCase() {
        Genotype genotype = new Genotype("", "a", "t");

        assertEquals("A", genotype.getReference());
        assertEquals("T", genotype.getAlternate());
    }


}
