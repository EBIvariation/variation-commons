package uk.ac.ebi.eva.commons.mongodb;

import org.junit.Test;
import uk.ac.ebi.eva.commons.mongodb.entities.projections.SimplifiedVariant;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;

public class SimplifiedVariantTest {

    @Test
    public void testChangeRefAltToUpperCase() throws NoSuchFieldException, IllegalAccessException {
        SimplifiedVariant simplifiedVariant = new SimplifiedVariant(null, "", -1, -1,
                -1, "a", "t", null);

        Field refField = SimplifiedVariant.class.getDeclaredField("reference");
        Field altField = SimplifiedVariant.class.getDeclaredField("alternate");
        refField.setAccessible(true);
        altField.setAccessible(true);
        String refValue = (String) refField.get(simplifiedVariant);
        String altValue = (String) altField.get(simplifiedVariant);

        assertEquals("A", refValue);
        assertEquals("T", altValue);

    }
}
