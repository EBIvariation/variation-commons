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
package uk.ac.ebi.eva.commons.mongodb.entities.subdocuments;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.core.models.IVariantStatistics;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Mongo database representation of VariantWithSamplesAndAnnotation Stats.
 */
public class VariantStatisticsMongo implements IVariantStatistics {

    public final static String COHORT_ID = "cid";

    public final static String STUDY_ID = "sid";

    public final static String FILE_ID = "fid";

    public final static String MAF_FIELD = "maf";

    public final static String MGF_FIELD = "mgf";

    public final static String MAFALLELE_FIELD = "mafAl";

    public final static String MGFGENOTYPE_FIELD = "mgfGt";

    public final static String MISSALLELE_FIELD = "missAl";

    public final static String MISSGENOTYPE_FIELD = "missGt";

    public final static String NUMGT_FIELD = "numGt";

    @Field(STUDY_ID)
    private String studyId;

    @Field(FILE_ID)
    private String fileId;

    @Field(COHORT_ID)
    private String cohortId;

    @Field(MAF_FIELD)
    private float maf;

    @Field(MGF_FIELD)
    private float mgf;

    @Field(MAFALLELE_FIELD)
    private String mafAllele;

    @Field(MGFGENOTYPE_FIELD)
    private String mgfGenotype;

    @Field(MISSALLELE_FIELD)
    private int missingAlleles;

    @Field(MISSGENOTYPE_FIELD)
    private int missingGenotypes;

    @Field(NUMGT_FIELD)
    private Map<String, Integer> numGt;

    VariantStatisticsMongo() {
        this(null, null, null, -1, -1, null, null, -1, -1, null);
    }

    public VariantStatisticsMongo(String studyId, String fileId, String cohortId, IVariantStatistics stats) {
        this(
                studyId,
                fileId,
                cohortId,
                stats.getMaf(),
                stats.getMgf(),
                stats.getMafAllele(),
                stats.getMgfGenotype(),
                stats.getMissingAlleles(),
                stats.getMissingGenotypes(),
                buildGenotypes(stats.getGenotypesCount()));
    }

    public VariantStatisticsMongo(String studyId, String fileId, String cohortId, float maf, float mgf, String mafAllele,
                                  String mgfGenotype, int missingAlleles, int missingGenotypes, Map<String, Integer> numGt) {
        this.studyId = studyId;
        this.fileId = fileId;
        this.cohortId = cohortId;
        this.maf = maf;
        this.mgf = mgf;
        this.mafAllele = mafAllele;
        this.mgfGenotype = mgfGenotype;
        this.missingAlleles = missingAlleles;
        this.missingGenotypes = missingGenotypes;
        this.numGt = new LinkedHashMap<>();
        if (numGt != null && !numGt.isEmpty()) {
            this.numGt.putAll(numGt);
        }
    }

    private static Map<String, Integer> buildGenotypes(Map<Genotype, Integer> genotypesCount) {
        Map<String, Integer> genotypes = new LinkedHashMap<>();
        for (Map.Entry<Genotype, Integer> g : genotypesCount.entrySet()) {
            String genotypeStr = g.getKey().toString().replace(".", "-1");
            genotypes.put(genotypeStr, g.getValue());
        }
        return genotypes;
    }

    public String getStudyId() {
        return studyId;
    }

    public String getFileId() {
        return fileId;
    }

    public String getCohortId() {
        return cohortId;
    }

    @Override
    public float getMaf() {
        return maf;
    }

    @Override
    public float getMgf() {
        return mgf;
    }

    @Override
    public String getMafAllele() {
        return mafAllele;
    }

    @Override
    public String getMgfGenotype() {
        return mgfGenotype;
    }

    @Override
    public int getMissingAlleles() {
        return missingAlleles;
    }

    @Override
    public int getMissingGenotypes() {
        return missingGenotypes;
    }

    @Override
    public Map<Genotype, Integer> getGenotypesCount() {
        Map<Genotype, Integer> genotypes = new LinkedHashMap<>();
        numGt.forEach((genotype, count) -> {
            genotypes.put(new Genotype(genotype.replace("-1", ".")), count);
        });
        return genotypes;
    }
}
