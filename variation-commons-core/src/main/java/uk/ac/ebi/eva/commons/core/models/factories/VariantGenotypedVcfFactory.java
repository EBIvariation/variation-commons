/*
 * Copyright 2014-2018 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.core.models.factories;

import uk.ac.ebi.eva.commons.core.models.factories.exception.IncompleteInformationException;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;
import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * Implementation of {@link VariantVcfFactory} that can parse VCF files with sample data, like genotypes
 */
public class VariantGenotypedVcfFactory extends VariantVcfFactory {

    protected void parseSplitSampleData(VariantSourceEntry variantSourceEntry, String[] fields,
                                        int alternateAlleleIdx) {
        if (fields.length < 9) {
            throw new IllegalArgumentException("Genotyped VCFs should have column FORMAT and at least one further " +
                                                       "sample columns, i.e. there should be at least 10 columns");
        }

        String[] formatFields = variantSourceEntry.getFormat().split(":");

        for (int i = 9; i < fields.length; i++) {
            Map<String, String> map = new TreeMap<>();

            // Fill map of a sample
            String[] sampleFields = fields[i].split(":");

            // Samples may remove the trailing fields (only GT is mandatory),
            // so the loop iterates to sampleFields.length, not formatFields.length
            for (int j = 0; j < sampleFields.length; j++) {
                String formatField = formatFields[j];
                map.put(formatField, processSampleField(alternateAlleleIdx, formatField, sampleFields[j]));
            }

            // Add sample to the variant entry in the source file
            variantSourceEntry.addSampleData(map);
        }
    }


    /**
     * If this is a field other than the genotype (GT), return unmodified. Otherwise,
     * see {@link VariantGenotypedVcfFactory#processGenotypeField(int, java.lang.String)}
     *
     * @param alternateAlleleIdx current alternate being processed. 0 for first alternate, 1 or more for a secondary alternate.
     * @param formatField as shown in the FORMAT column. most probably the GT field.
     * @param sampleField parsed value in a column of a sample, such as a genotype, e.g. "0/0".
     * @return processed sample field, ready to be stored.
     */
    private String processSampleField(int alternateAlleleIdx, String formatField, String sampleField) {
        if (formatField.equalsIgnoreCase("GT")) {
            return processGenotypeField(alternateAlleleIdx, sampleField);
        } else {
            return sampleField;
        }
    }

    /**
     * Intern the genotype String into the String pool to avoid storing lots of "0/0". In case that the variant is
     * multiallelic and we are currently processing one of the secondary alternates (T is the only secondary alternate
     * in a variant like A -> C,T), change the allele codes to represent the current alternate as allele 1. For details
     * on changing this indexes, see {@link VariantVcfFactory#mapToMultiallelicIndex(int, int)}
     *
     * @param alternateAlleleIdx current alternate being processed. 0 for first alternate, 1 or more for a secondary alternate.
     * @param genotype first field in the samples column, e.g. "0/0"
     * @return the processed genotype string, as described above (interned and changed if multiallelic).
     */
    private String processGenotypeField(int alternateAlleleIdx, String genotype) {
        boolean isNotTheFirstAlternate = alternateAlleleIdx >= 1;
        if (isNotTheFirstAlternate) {
            Genotype parsedGenotype = new Genotype(genotype);

            StringBuilder genotypeStr = new StringBuilder();
            for (int allele : parsedGenotype.getAllelesIdx()) {
                if (allele < 0) { // Missing
                    genotypeStr.append(".");
                } else {
                    // Replace numerical indexes when they refer to another alternate allele
                    genotypeStr.append(String.valueOf(mapToMultiallelicIndex(allele, alternateAlleleIdx)));
                }
                genotypeStr.append(parsedGenotype.isPhased() ? "|" : "/");
            }
            genotype = genotypeStr.substring(0, genotypeStr.length() - 1);
        }

        return genotype.intern();
    }

    @Override
    protected boolean checkVariantInformation(Variant variant, String fileId, String studyId)
            throws IncompleteInformationException {
        if (!super.checkVariantInformation(variant, fileId, studyId)) {
            return false;
        }
        VariantSourceEntry variantSourceEntry = variant.getSourceEntry(fileId, studyId);
        if (!hasAlternateAlleleCalls(variantSourceEntry)) {
            logger.warn("The variant {} has no alternate allele genotype calls and will be discarded as a non-variant",
                        variant);
            return false;
        }
        return true;
    }

    private boolean hasAlternateAlleleCalls(VariantSourceEntry variantSourceEntry) {
        boolean hasAlternateAlleleCalls = false;

        List<Map<String, String>> samplesData = variantSourceEntry.getSamplesData();
        if (!samplesData.isEmpty()) {
            if (samplesData.stream().map(m -> m.get("GT")).anyMatch(this::genotypeHasAlternateAllele)) {
                hasAlternateAlleleCalls = true;
            }
        }

        return hasAlternateAlleleCalls;
    }


    private boolean genotypeHasAlternateAllele(String sampleField) {
        // the alternate allele index could be originally other than 1, but the processGenotypeField method has
        // updated those indexes so all calls to the alternate allele in this variant has now index 1
        return Arrays.stream(sampleField.split("[/|]")).anyMatch(allele -> allele.equals("1"));
    }

    @Override
    public void setRequireEvidence(boolean requireEvidence) {
        if (requireEvidence) {
            this.requireEvidence = requireEvidence;
        } else {
            throw new UnsupportedOperationException("There is no use case at the moment for "
                                                            + this.getClass().getSimpleName()
                                                            + " to not require evidence. This class expects all "
                                                            + "variants to have frequencies or genotypes.");
        }
    }
}
