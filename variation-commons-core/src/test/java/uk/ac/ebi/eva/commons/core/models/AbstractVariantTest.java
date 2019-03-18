/*
 * Copyright 2018 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.ebi.eva.commons.core.models;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;

import static org.junit.Assert.assertEquals;

public class AbstractVariantTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testNegativeStartCoordinateInConstructor() throws IllegalArgumentException {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.equalTo(AbstractVariant.VARIANT_START_COORDINATE_CANNOT_BE_NEGATIVE));
        new Variant("1", -1, 1, "C", "T");
    }

    @Test
    public void testNegativeStartCoordinateInRenormalization() throws IllegalArgumentException {
        Variant variant = new Variant("1", 1, 1, "C", "T");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.equalTo(AbstractVariant.VARIANT_START_COORDINATE_CANNOT_BE_NEGATIVE));
        variant.renormalize(-1, 1, "A", "T");
    }

    @Test
    public void testNegativeEndCoordinateInConstructor() throws IllegalArgumentException {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.equalTo(AbstractVariant.VARIANT_END_COORDINATE_CANNOT_BE_NEGATIVE));
        new Variant("1", 1, -1, "C", "T");
    }

    @Test
    public void testNegativeEndCoordinateInRenormalization() throws IllegalArgumentException {
        Variant variant = new Variant("1", 1, 1, "C", "T");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(CoreMatchers.equalTo(AbstractVariant.VARIANT_END_COORDINATE_CANNOT_BE_NEGATIVE));
        variant.renormalize(1, -1, "A", "T");
    }

    @Test
    public void testStartHigherThanEndInConstructor() throws IllegalArgumentException {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                CoreMatchers.equalTo(AbstractVariant.END_POSITION_MUST_BE_EQUAL_OR_GREATER_THAN_THE_START_POSITION));
        new Variant("1", 2, 1, "C", "T");
    }

    @Test
    public void testStartHigherThanEndInRenormalization() throws IllegalArgumentException {
        Variant variant = new Variant("1", 1, 1, "C", "T");
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(
                CoreMatchers.equalTo(AbstractVariant.END_POSITION_MUST_BE_EQUAL_OR_GREATER_THAN_THE_START_POSITION));
        variant.renormalize(1, 0, "A", "T");
    }

    @Test
    public void testStartEndAssignmentInConstructor() {
        Variant variant = new Variant("1", 1, 1, "C", "T");
        assertEquals(1, variant.getStart());
        assertEquals(1, variant.getEnd());
    }

    @Test
    public void testStartEndAssignmentInRenormalization() {
        Variant variant = new Variant("1", 2, 2, "C", "T");
        variant.renormalize(1, 2, "AC", "AT");
        assertEquals(1, variant.getStart());
        assertEquals(2, variant.getEnd());
    }

    @Test
    public void testRefAltAssignmentInConstructor() {
        Variant variant = new Variant("1", 1, 1, "C", "T");
        assertEquals("C", variant.getReference());
        assertEquals("T", variant.getAlternate());
    }

    @Test
    public void testRefAltAssignmentInRenormalization() {
        Variant variant = new Variant("1", 1, 1, "", "T");
        variant.renormalize(1, 2, "A", "AT");
        assertEquals("A", variant.getReference());
        assertEquals("AT", variant.getAlternate());
    }

    @Test
    public void testNullRefAssignmentInConstructor() {
        Variant variant = new Variant("1", 1, 1, null, "T");
        assertEquals("", variant.getReference());
        assertEquals("T", variant.getAlternate());
    }

    @Test
    public void testEmptyRefAssignmentInConstructor() {
        Variant variant = new Variant("1", 1, 1, "", "T");
        assertEquals("", variant.getReference());
        assertEquals("T", variant.getAlternate());
    }

    @Test
    public void testNullAltAssignmentInConstructor() {
        Variant variant = new Variant("1", 1, 1, "T", null);
        assertEquals("T", variant.getReference());
        assertEquals("", variant.getAlternate());
    }

    @Test
    public void testEmptyAltAssignmentInConstructor() {
        Variant variant = new Variant("1", 1, 1, "T", "");
        assertEquals("T", variant.getReference());
        assertEquals("", variant.getAlternate());
    }

}
