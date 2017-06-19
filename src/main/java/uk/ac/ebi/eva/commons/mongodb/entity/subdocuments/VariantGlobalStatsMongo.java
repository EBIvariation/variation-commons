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
package uk.ac.ebi.eva.commons.mongodb.entity.subdocuments;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Mongo database representation of VariantGlobalStatsMongo
 */
public class VariantGlobalStatsMongo {

    public final static String STATISTICS_NUMSAMPLES_FIELD = "nSamp";

    public final static String STATISTICS_NUMVARIANTS_FIELD = "nVar";

    public final static String STATISTICS_NUMSNPS_FIELD = "nSnp";

    public final static String STATISTICS_NUMINDELS_FIELD = "nIndel";

    public final static String STATISTICS_NUMSTRUCTURAL_FIELD = "nSv";

    public final static String STATISTICS_NUMPASSFILTERS_FIELD = "nPass";

    public final static String STATISTICS_NUMTRANSITIONS_FIELD = "nTi";

    public final static String STATISTICS_NUMTRANSVERSIONS_FIELD = "nTv";

    public final static String STATISTICS_MEANQUALITY_FIELD = "meanQ";

    @Field(STATISTICS_NUMVARIANTS_FIELD)
    private int variantsCount;

    @Field(STATISTICS_NUMSAMPLES_FIELD)
    private int samplesCount;

    @Field(STATISTICS_NUMSNPS_FIELD)
    private int snpsCount;

    @Field(STATISTICS_NUMINDELS_FIELD)
    private int indelsCount;

    @Field(STATISTICS_NUMSTRUCTURAL_FIELD)
    private int structuralCount;

    @Field(STATISTICS_NUMPASSFILTERS_FIELD)
    private int passCount;

    @Field(STATISTICS_NUMTRANSITIONS_FIELD)
    private int transitionsCount;

    @Field(STATISTICS_NUMTRANSVERSIONS_FIELD)
    private int transversionsCount;

    @Field(STATISTICS_MEANQUALITY_FIELD)
    private float meanQuality;

    VariantGlobalStatsMongo() {
        // Spring empty constructor
    }

    public VariantGlobalStatsMongo(int variantsCount, int samplesCount, int snpsCount, int indelsCount, int structuralCount,
                                   int passCount, int transitionsCount, int transversionsCount, float meanQuality) {
        this.variantsCount = variantsCount;
        this.samplesCount = samplesCount;
        this.snpsCount = snpsCount;
        this.indelsCount = indelsCount;
        this.structuralCount = structuralCount;
        this.passCount = passCount;
        this.transitionsCount = transitionsCount;
        this.transversionsCount = transversionsCount;
        this.meanQuality = meanQuality;
    }

    public int getVariantsCount() {
        return variantsCount;
    }

    public int getSamplesCount() {
        return samplesCount;
    }

    public int getSnpsCount() {
        return snpsCount;
    }

    public int getIndelsCount() {
        return indelsCount;
    }

    public int getStructuralCount() {
        return structuralCount;
    }

    public int getPassCount() {
        return passCount;
    }

    public int getTransitionsCount() {
        return transitionsCount;
    }

    public int getTransversionsCount() {
        return transversionsCount;
    }

    public float getMeanQuality() {
        return meanQuality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VariantGlobalStatsMongo)) return false;

        VariantGlobalStatsMongo that = (VariantGlobalStatsMongo) o;

        if (variantsCount != that.variantsCount) return false;
        if (samplesCount != that.samplesCount) return false;
        if (snpsCount != that.snpsCount) return false;
        if (indelsCount != that.indelsCount) return false;
        if (structuralCount != that.structuralCount) return false;
        if (passCount != that.passCount) return false;
        if (transitionsCount != that.transitionsCount) return false;
        if (transversionsCount != that.transversionsCount) return false;
        return Float.compare(that.meanQuality, meanQuality) == 0;
    }

    @Override
    public int hashCode() {
        int result = variantsCount;
        result = 31 * result + samplesCount;
        result = 31 * result + snpsCount;
        result = 31 * result + indelsCount;
        result = 31 * result + structuralCount;
        result = 31 * result + passCount;
        result = 31 * result + transitionsCount;
        result = 31 * result + transversionsCount;
        result = 31 * result + (meanQuality != +0.0f ? Float.floatToIntBits(meanQuality) : 0);
        return result;
    }
}