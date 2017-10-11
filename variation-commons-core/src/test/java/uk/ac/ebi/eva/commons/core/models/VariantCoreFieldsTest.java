/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
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

import org.junit.Test;

import static org.junit.Assert.*;

public class VariantCoreFieldsTest {

    @Test
    public void testSnp() {
        VariantCoreFields snp = new VariantCoreFields("chr1", 1000L, "A", "C");
        assertEquals("chr1", snp.getChromosome());
        assertEquals(1000L, snp.getStart());
        assertEquals(1000L, snp.getEnd());
        assertEquals("A", snp.getReference());
        assertEquals("C", snp.getAlternate());
    }

    @Test
    public void testSingleNucleotideInsertions() {
        // nucleotide inserted before context nucleotide
        VariantCoreFields insertion = new VariantCoreFields("chr1", 1000, "A", "TA");
        assertEquals("chr1", insertion.getChromosome());
        assertEquals(1000, insertion.getStart());
        assertEquals(1000, insertion.getEnd());
        assertEquals("", insertion.getReference());
        assertEquals("T", insertion.getAlternate());

        // nucleotide inserted after context nucleotide
        insertion = new VariantCoreFields("chr1", 1000, "A", "AT");
        assertEquals("chr1", insertion.getChromosome());
        assertEquals(1001, insertion.getStart());
        assertEquals(1001, insertion.getEnd());
        assertEquals("", insertion.getReference());
        assertEquals("T", insertion.getAlternate());
    }

    @Test
    public void testSingleNucleotideDeletions() {
        // nucleotide inserted before context nucleotide
        VariantCoreFields deletion = new VariantCoreFields("chr1", 1000, "TA", "A");
        assertEquals("chr1", deletion.getChromosome());
        assertEquals(1000, deletion.getStart());
        assertEquals(1000, deletion.getEnd());
        assertEquals("T", deletion.getReference());
        assertEquals("", deletion.getAlternate());

        // nucleotide deleted after context nucleotide
        deletion = new VariantCoreFields("chr1", 1000, "TA", "T");
        assertEquals("chr1", deletion.getChromosome());
        assertEquals(1001, deletion.getStart());
        assertEquals(1001, deletion.getEnd());
        assertEquals("A", deletion.getReference());
        assertEquals("", deletion.getAlternate());
    }

    @Test
    public void testMNVs() {
        // two alleles ending with different nucleotide
        VariantCoreFields insertion = new VariantCoreFields("chr1", 1000, "TCACCC", "TGACGG");
        assertEquals("chr1", insertion.getChromosome());
        assertEquals(1001, insertion.getStart());
        assertEquals(1005, insertion.getEnd());
        assertEquals("CACCC", insertion.getReference());
        assertEquals("GACGG", insertion.getAlternate());

        // two alleles ending with same nucleotide
        insertion = new VariantCoreFields("chr1", 1000, "TCACCC", "TGACGC");
        assertEquals("chr1", insertion.getChromosome());
        assertEquals(1001, insertion.getStart());
        assertEquals(1004, insertion.getEnd());
        assertEquals("CACC", insertion.getReference());
        assertEquals("GACG", insertion.getAlternate());
    }

    @Test
    public void testMultiNucleotideInsertions() {
        // single context nucleotide
        VariantCoreFields insertion = new VariantCoreFields("chr1", 1000, "A", "ATCT");
        assertEquals("chr1", insertion.getChromosome());
        assertEquals(1001, insertion.getStart());
        assertEquals(1003, insertion.getEnd());
        assertEquals("", insertion.getReference());
        assertEquals("TCT", insertion.getAlternate());

        // several common nucleotides
        insertion = new VariantCoreFields("chr1", 1000, "AT", "ATC");
        assertEquals("chr1", insertion.getChromosome());
        assertEquals(1002, insertion.getStart());
        assertEquals(1002, insertion.getEnd());
        assertEquals("", insertion.getReference());
        assertEquals("C", insertion.getAlternate());
    }

    @Test
    public void testMultiNucleotideDeletions() {
        // nucleotide inserted before context nucleotide
        VariantCoreFields deletion = new VariantCoreFields("chr1", 1000, "GATC", "G");
        assertEquals("chr1", deletion.getChromosome());
        assertEquals(1001, deletion.getStart());
        assertEquals(1003, deletion.getEnd());
        assertEquals("ATC", deletion.getReference());
        assertEquals("", deletion.getAlternate());

         // several common nucleotides
        deletion = new VariantCoreFields("chr1", 1000, "ATC", "TC");
        assertEquals("chr1", deletion.getChromosome());
        assertEquals(1000, deletion.getStart());
        assertEquals(1000, deletion.getEnd());
        assertEquals("A", deletion.getReference());
        assertEquals("", deletion.getAlternate());
    }

    @Test
    public void testOtherIndels() {
        VariantCoreFields deletion = new VariantCoreFields("chr1", 1000, "CGATT", "TAC");
        assertEquals("chr1", deletion.getChromosome());
        assertEquals(1000, deletion.getStart());
        assertEquals(1004, deletion.getEnd());
        assertEquals("CGATT", deletion.getReference());
        assertEquals("TAC", deletion.getAlternate());

        deletion = new VariantCoreFields("chr1", 1000, "ATC", "AC");
        assertEquals("chr1", deletion.getChromosome());
        assertEquals(1001, deletion.getStart());
        assertEquals(1001, deletion.getEnd());
        assertEquals("T", deletion.getReference());
        assertEquals("", deletion.getAlternate());
    }
}