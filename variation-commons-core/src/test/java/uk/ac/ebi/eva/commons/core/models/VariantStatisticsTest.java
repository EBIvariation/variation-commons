package uk.ac.ebi.eva.commons.core.models;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VariantStatisticsTest {

    @Test
    public void testChangeRefAltToUpperCase() {
        VariantStatistics variantStatistics = new VariantStatistics("a", "t", null,
                -1, -1, "", "", -1, -1, -1,
                -1, -1, -1, -1);
        assertEquals("A", variantStatistics.getRefAllele());
        assertEquals("T", variantStatistics.getAltAllele());
    }

}
