/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.core.models;

/**
 * Type of variation, which depends mostly on its length.
 */
public enum VariantType {

    /**
     * SNVs involve a single nucleotide, without changes in length
     */
    SNV,

    /**
     * MNVs involve multiple nucleotides, without changes in length
     */
    MNV,
    /**
     * INS denotes insertions
     */
    INS,
    /**
     * DEL denotes deletions
     */
    DEL,
    /**
     * Indels are insertions or deletions of less than SV_THRESHOLD (50) nucleotides
     */
    INDEL,

    /**
     * Structural variations are large changes of more than SV_THRESHOLD nucleotides
     */
    SV,

    /**
     * Copy-number variations alter the number of copies of a region
     */
    CNV,

    /**
     * No alternate alleles found mean that only the reference was reported
     */
    NO_ALTERNATE

}
