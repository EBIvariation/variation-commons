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
package uk.ac.ebi.eva.commons.core.models.stats;


public class VariantSingleSampleStats {

    private String id;

    private int numMendelianErrors;

    private int numMissingGenotypes;

    private int numHomozygous;

    VariantSingleSampleStats() {
        this(null);
    }

    public VariantSingleSampleStats(String id) {
        this.id = id;
        this.numMendelianErrors = 0;
        this.numMissingGenotypes = 0;
        this.numHomozygous = 0;
    }

    public void incrementMendelianErrors() {
        this.numMendelianErrors++;
    }

    public void incrementMissingGenotypes() {
        this.numMissingGenotypes++;
    }

    public void incrementHomozygous() {
        this.numHomozygous++;
    }

    public String getId() {
        return id;
    }

    public int getNumMendelianErrors() {
        return numMendelianErrors;
    }

    public int getNumMissingGenotypes() {
        return numMissingGenotypes;
    }

    public int getNumHomozygous() {
        return numHomozygous;
    }

    public void incrementMendelianErrors(int mendelianErrors) {
        this.numMendelianErrors += mendelianErrors;
    }

    public void incrementMissingGenotypes(int missingGenotypes) {
        this.numMissingGenotypes += missingGenotypes;
    }

    public void incrementHomozygotesNumber(int homozygotesNumber) {
        this.numHomozygous += homozygotesNumber;

    }
}
