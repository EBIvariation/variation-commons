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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.eva.commons.core.models.IVariant;
import uk.ac.ebi.eva.commons.core.models.IVariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.genotype.AllelesCode;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;
import uk.ac.ebi.eva.commons.core.models.pedigree.Pedigree;
import uk.ac.ebi.eva.commons.core.models.ws.VariantSourceEntryWithSampleNames;
import uk.ac.ebi.eva.commons.core.models.ws.VariantWithSamplesAndAnnotations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class VariantSourceStats {

    private final static Logger logger = LoggerFactory.getLogger(VariantSourceStats.class);

    private final String fileId;

    private final String studyId;

    private List<String> sampleNames;

    private VariantGlobalStats fileStats;

    private Map<String, VariantSingleSampleStats> samplesStats;

    VariantSourceStats() {
        this(null, null);
    }

    public VariantSourceStats(String fileId, String studyId) {
        this.fileId = fileId;
        this.studyId = studyId;
        this.sampleNames = new ArrayList<>();
        this.fileStats = new VariantGlobalStats();
        this.samplesStats = new LinkedHashMap<>();
    }

    public List<String> getSampleNames() {
        return sampleNames;
    }

    public void setSampleNames(List<String> sampleNames) {
        this.sampleNames = sampleNames;
        fileStats.setSamplesCount(sampleNames.size());
    }

    public VariantGlobalStats getFileStats() {
        return fileStats;
    }

    public void setFileStats(VariantGlobalStats fileStats) {
        this.fileStats = fileStats;
    }

    public void updateFileStats(List<IVariant> variants) {
        int incompleteVariantStats = 0;
        for (IVariant v : variants) {
            IVariantSourceEntry file = v.getSourceEntry(fileId, studyId);
            if (file != null) {
                try {
                    fileStats.update(file.getStats());
                } catch (NullPointerException e) {
                    incompleteVariantStats++;
                }
            }
        }
        if (incompleteVariantStats != 0) {
            logger.warn("{} VariantStats have needed members as null", incompleteVariantStats);
        }
    }

    public Map<String, VariantSingleSampleStats> getSamplesStats() {
        return samplesStats;
    }

    public VariantSingleSampleStats getSampleStats(String sampleName) {
        return samplesStats.get(sampleName);
    }

    public void setSamplesStats(Map<String, VariantSingleSampleStats> variantSampleStats) {
        this.samplesStats = variantSampleStats;
    }

    public void updateSampleStats(List<VariantWithSamplesAndAnnotations> variants, Pedigree pedigree) {
        for (VariantWithSamplesAndAnnotations v : variants) {
            VariantSourceEntryWithSampleNames file = v.getSourceEntry(fileId, studyId);
            if (file == null) {
                // The variant is not contained in this file
                continue;
            }

            for (Map.Entry<String, Map<String, String>> sample : file.getSamplesDataMap().entrySet()) {
                String sampleName = sample.getKey();
                VariantSingleSampleStats sampleStats = samplesStats.get(sampleName);
                if (sampleStats == null) {
                    sampleStats = new VariantSingleSampleStats(sampleName);
                    samplesStats.put(sampleName, sampleStats);
                }

                Genotype g = new Genotype(sample.getValue().get("GT"), v.getReference(), v.getAlternate());

                // Count missing genotypes (one or both alleles missing)
                if (g.getCode() != AllelesCode.ALLELES_OK) {
                    sampleStats.incrementMissingGenotypes();
                }

//                // TODO Check mendelian errors
//                if (pedigree != null) {
//                    Individual ind = pedigree.getIndividual(sampleName);
//                    if (g.getCode() == AllelesCode.ALLELES_OK && isMendelianError(ind, g, record)) {
//                        sampleStats.incrementMendelianErrors();
//                    }
//                }

                // Count homozygous (not haploid)
                if (g.getCode() != AllelesCode.HAPLOID && g.getAllele(0) == g.getAllele(1)) {
                    sampleStats.incrementHomozygous();
                }
            }
        }
    }

    public String getFileId() {
        return fileId;
    }

    public String getStudyId() {
        return studyId;
    }

}
