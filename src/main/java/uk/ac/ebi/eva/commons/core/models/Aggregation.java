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
 * Level of aggregation of the sample data in a variant source
 */
public enum Aggregation {

    /**
     * Full listing of sample genotypes provided
     */
    NONE,

    /**
     * Only aggregated fields AF, AC and AN as described in the VCF specification, are provided
     */
    BASIC,

    /**
     * Only aggregated fields as in the Exome Variation Server (EVS) provided
     */
    EVS,

    /**
     * Only aggregated fields as in the Exome Aggregation Consortium (ExAC) provided
     */
    EXAC

}
