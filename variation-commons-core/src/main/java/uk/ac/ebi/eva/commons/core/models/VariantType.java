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
 * See https://docs.google.com/spreadsheets/d/1YH8qDBDu7C6tqULJNCrGw8uBjdW3ZT5OjTkJzGNOZ4E/edit#gid=1433496764
 */
public enum VariantType {

    /**
     * SNVs involve a single nucleotide, without changes in length
     */
    SNV,
    /**
     * DEL denotes deletions
     */
    DEL,
    /**
     * INS denotes insertions
     */
    INS,
    /**
     * Indels are insertions and deletions of less than SV_THRESHOLD (50) nucleotides
     */
    INDEL,
    /**
     * TANDEM_REPEAT - Short Tandem repeats - ex: (A)3/(A)6
     */
    TANDEM_REPEAT,
    /**
     * Named variation - ex: (ALI06)
     */
    SEQUENCE_ALTERATION,
    /**
     * No variation was observed - Legacy classification
     */
    NO_SEQUENCE_ALTERATION,
    /**
     * MNVs involve multiple nucleotides, without changes in length
     */
    MNV,
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
