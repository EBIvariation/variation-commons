/*
 * Copyright 2014-2016 EMBL - European Bioinformatics Institute
 * Copyright 2015 OpenCB
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
package uk.ac.ebi.eva.commons.core.models.factories;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.ac.ebi.eva.commons.core.models.VariantCoreFields;
import uk.ac.ebi.eva.commons.core.models.factories.exception.IncompleteInformationException;
import uk.ac.ebi.eva.commons.core.models.factories.exception.NonVariantException;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;
import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;
import uk.ac.ebi.eva.commons.core.models.pipeline.VariantSourceEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Class that parses VCF lines to create Variants.
 */
public class VariantVcfFactory {

    private static final Logger logger = LoggerFactory.getLogger(VariantVcfFactory.class);

    /**
     * Creates a list of Variant objects using the fields in a record of a VCF
     * file. A new Variant object is created per allele, so several of them can
     * be created from a single line.
     * <p>
     * Start/end coordinates assignment tries to work as similarly as possible
     * as Ensembl does, except for insertions, where start is greater than end:
     * http://www.ensembl.org/info/docs/tools/vep/vep_formats.html#vcf
     *
     * @param fileId,
     * @param studyId
     * @param line    Contents of the line in the file
     * @return The list of Variant objects that can be created using the fields from a VCF record
     */
    public List<Variant> create(String fileId, String studyId,
                                String line) throws IllegalArgumentException, NonVariantException,
            IncompleteInformationException {

        String[] fields = line.split("\t");
        if (fields.length < 8) {
            throw new IllegalArgumentException("Not enough fields provided (min 8)");
        }

        String chromosome = getChromosomeWithoutPrefix(fields);
        int position = getPosition(fields);
        Set<String> ids = new HashSet<>(); //EVA-942 - Ignore IDs submitted through VCF
        String reference = getReference(fields);
        String[] alternateAlleles = getAlternateAlleles(fields, chromosome, position, reference);
        float quality = getQuality(fields);
        String filter = getFilter(fields);
        String info = getInfo(fields);
        String format = getFormat(fields);

        List<VariantCoreFields> generatedKeyFields = buildVariantCoreFields(chromosome, position, reference,
                                                                            alternateAlleles);

        List<Variant> variants = new LinkedList<>();
        // Now create all the Variant objects read from the VCF record
        for (int altAlleleIdx = 0; altAlleleIdx < alternateAlleles.length; altAlleleIdx++) {
            VariantCoreFields keyFields = generatedKeyFields.get(altAlleleIdx);
            Variant variant = new Variant(chromosome, keyFields.getStart(), keyFields.getEnd(),
                                          keyFields.getReference(),
                                          keyFields.getAlternate());
            String[] secondaryAlternates = getSecondaryAlternates(altAlleleIdx, alternateAlleles);
            VariantSourceEntry file = new VariantSourceEntry(fileId, studyId, secondaryAlternates, format);
            variant.addSourceEntry(file);

            boolean hasSamplesData = parseSplitSampleData(variant, fileId, studyId, fields, alternateAlleles,
                                                          secondaryAlternates, altAlleleIdx);
            // Fill the rest of fields (after samples because INFO depends on them)
            boolean hasFrequenciesOrAllelesCounts = setOtherFields(variant, fileId, studyId, ids, quality, filter, info,
                                                                   format, altAlleleIdx, alternateAlleles, line);
            if (!hasSamplesData && !hasFrequenciesOrAllelesCounts) {
                throw new IncompleteInformationException(keyFields);
            }
            variants.add(variant);
        }

        return variants;
    }

    /**
     * Replace "chr" references only at the beginning of the chromosome name.
     * For instance, tomato has SL2.40ch00 and that should be kept that way
     */
    private String getChromosomeWithoutPrefix(String[] fields) {
        String chromosome = fields[0];
        boolean ignoreCase = true;
        int startOffset = 0;
        String prefixToRemove = "chr";
        if (chromosome.regionMatches(ignoreCase, startOffset, prefixToRemove, startOffset, prefixToRemove.length())) {
            return chromosome.substring(prefixToRemove.length());
        }
        return chromosome;
    }

    private int getPosition(String[] fields) {
        return Integer.parseInt(fields[1]);
    }

    private Set<String> getIds(String[] fields) {
        Set<String> ids = new HashSet<>();
        if (!fields[2].equals(".")) {    // note!: we store a "." as an empty set, not a set with an empty string
            ids.addAll(Arrays.asList(fields[2].split(";")));
        }
        return ids;
    }

    private String getReference(String[] fields) {
        return fields[3].equals(".") ? "" : fields[3];
    }

    private String[] getAlternateAlleles(String[] fields, String chromosome, int position, String reference) {
        return fields[4].split(",");
    }

    private float getQuality(String[] fields) {
        return fields[5].equals(".") ? -1 : Float.parseFloat(fields[5]);
    }

    private String getFilter(String[] fields) {
        return fields[6].equals(".") ? "" : fields[6];
    }

    private String getInfo(String[] fields) {
        return fields[7].equals(".") ? "" : fields[7];
    }

    private String getFormat(String[] fields) {
        return (fields.length <= 8 || fields[8].equals(".")) ? "" : fields[8];
    }

    private List<VariantCoreFields> buildVariantCoreFields(String chromosome, int position, String reference,
                                                          String[] alternateAlleles) {
        List<VariantCoreFields> generatedKeyFields = new ArrayList<>();

        for (int i = 0; i < alternateAlleles.length; i++) { // This index is necessary for getting the samples where the mutated allele is present
            VariantCoreFields keyFields = new VariantCoreFields(chromosome, position, reference, alternateAlleles[i]);

            // Since the reference and alternate alleles won't necessarily match
            // the ones read from the VCF file but they are still needed for
            // instantiating the variants, they must be updated
            alternateAlleles[i] = keyFields.getAlternate();
            generatedKeyFields.add(keyFields);
        }
        return generatedKeyFields;
    }

    protected String[] getSecondaryAlternates(int numAllele, String[] alternateAlleles) {
        String[] secondaryAlternates = new String[alternateAlleles.length - 1];
        for (int i = 0, j = 0; i < alternateAlleles.length; i++) {
            if (i != numAllele) {
                secondaryAlternates[j++] = alternateAlleles[i];
            }
        }
        return secondaryAlternates;
    }

    protected boolean parseSplitSampleData(Variant variant, String fileId, String studyId, String[] fields,
                                        String[] alternateAlleles, String[] secondaryAlternates,
                                        int alternateAlleleIdx) throws NonVariantException {

        String[] formatFields = variant.getSourceEntry(fileId, studyId).getFormat().split(":");

        boolean hasSampleData = fields.length >= 9;

        if (hasSampleData) {
            boolean allGenotypesAreRefOrMissingValues = true;

            for (int i = 9; i < fields.length; i++) {
                Map<String, String> map = new TreeMap<>();

                // Fill map of a sample
                String[] sampleFields = fields[i].split(":");

                // Samples may remove the trailing fields (only GT is mandatory),
                // so the loop iterates to sampleFields.length, not formatFields.length
                for (int j = 0; j < sampleFields.length; j++) {
                    String formatField = formatFields[j];
                    String sampleField = processSampleField(alternateAlleleIdx, formatField, sampleFields[j]);
                    if (allGenotypesAreRefOrMissingValues && formatField.equalsIgnoreCase("GT")) {
                        if (genotypeHasAlternateAllele(sampleField)) {
                            allGenotypesAreRefOrMissingValues = false;
                        }
                    }

                    map.put(formatField, sampleField);
                }

                // Add sample to the variant entry in the source file
                variant.getSourceEntry(fileId, studyId).addSampleData(map);
            }
            if (allGenotypesAreRefOrMissingValues) {
                throw new NonVariantException("All genotypes are reference or missing values");
            }
        }

        return hasSampleData;
    }

    /**
     * If this is a field other than the genotype (GT), return unmodified. Otherwise,
     * see {@link VariantVcfFactory#processGenotypeField(int, java.lang.String)}
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

    private boolean genotypeHasAlternateAllele(String sampleField) {
        return Arrays.stream(sampleField.split("[/|]")).anyMatch(allele -> allele.equals("1"));
    }

    protected boolean setOtherFields(Variant variant, String fileId, String studyId, Set<String> ids, float quality,
                                  String filter, String info, String format, int numAllele, String[] alternateAlleles,
                                  String line) throws NonVariantException {

        // Fields not affected by the structure of REF and ALT fields
        variant.setIds(ids);

        if (quality > -1) {
            variant.getSourceEntry(fileId, studyId)
                   .addAttribute("QUAL", String.valueOf(quality));
        }
        if (!filter.isEmpty()) {
            variant.getSourceEntry(fileId, studyId).addAttribute("FILTER", filter);
        }

        boolean hasCountsOrFrequenciesInInfoField = false;
        if (!info.isEmpty()) {
            hasCountsOrFrequenciesInInfoField = parseInfo(variant, fileId, studyId, info, numAllele);
        }
        variant.getSourceEntry(fileId, studyId).addAttribute("src", line);

        return hasCountsOrFrequenciesInInfoField;
    }

    protected boolean parseInfo(Variant variant, String fileId, String studyId, String info,
                             int numAllele) throws NonVariantException {

        VariantSourceEntry file = variant.getSourceEntry(fileId, studyId);
        boolean hasCountsOrFrequencies = false;

        for (String var : info.split(";")) {
            String[] splits = var.split("=");
            if (splits.length == 2) {
                switch (splits[0]) {
                    case "ACC":
                        // Managing accession ID for the allele
                        String[] ids = splits[1].split(",");
                        file.addAttribute(splits[0], ids[numAllele]);
                        break;
                    case "AC":
                        // TODO For now, only one alternate is supported
                        String[] counts = splits[1].split(",");
                        if (counts[numAllele].equals("0")) {
                            throw new NonVariantException("Alternate allele count is 0");
                        }
                        file.addAttribute(splits[0], counts[numAllele]);
                        hasCountsOrFrequencies = true;
                        break;
                    case "AF":
                        // TODO For now, only one alternate is supported
                        String[] frequencies = splits[1].split(",");
                        if (frequencies[numAllele].equals("0")) {
                            throw new NonVariantException("Alternate allele frequency is 0");
                        }
                        file.addAttribute(splits[0], frequencies[numAllele]);
                        hasCountsOrFrequencies = true;
                        break;
                    case "AN":
//                        // TODO For now, only two alleles (reference and one alternate) are supported, but this should be changed
//                        file.addAttribute(splits[0], "2");
                        if (splits[1].equals("0")) {
                            throw new NonVariantException("Total number of alleles is 0");
                        }
                        break;
                    case "NS":
                        // Count the number of samples that are associated with the allele
                        file.addAttribute(splits[0], String.valueOf(file.getSamplesData().size()));
                        break;
                    case "DP":
                        int dp = 0;
                        for (Map<String, String> sampleData : file.getSamplesData()) {
                            String sampleDp = sampleData.get("DP");
                            if (StringUtils.isNumeric(sampleDp)) {
                                dp += Integer.parseInt(sampleDp);
                            }
                        }
                        file.addAttribute(splits[0], String.valueOf(dp));
                        break;
                    case "MQ":
                    case "MQ0":
                        int mq = 0;
                        int mq0 = 0;
                        for (Map<String, String> sampleData : file.getSamplesData()) {
                            String sampleGq = sampleData.get("GQ");
                            if (StringUtils.isNumeric(sampleGq)) {
                                int gq = Integer.parseInt(sampleGq);
                                mq += gq * gq;
                                if (gq == 0) {
                                    mq0++;
                                }
                            }
                        }
                        file.addAttribute("MQ", String.valueOf(mq));
                        file.addAttribute("MQ0", String.valueOf(mq0));
                        break;
                    default:
                        file.addAttribute(splits[0], splits[1]);
                        break;
                }
            } else {
                variant.getSourceEntry(fileId, studyId).addAttribute(splits[0], "");
            }

        }
        return hasCountsOrFrequencies;
    }

    /**
     * In multiallelic variants, we have a list of alternates, where numAllele is the one whose variant we are parsing
     * now. If we are parsing the first variant (numAllele == 0) A1 refers to first alternative, (i.e.
     * alternateAlleles[0]), A2 to second alternative (alternateAlleles[1]), and so on. However, if numAllele == 1, A1
     * refers to second alternate (alternateAlleles[1]), A2 to first (alternateAlleles[0]) and higher alleles remain
     * unchanged. Moreover, if NumAllele == 2, A1 is third alternate, A2 is first alternate and A3 is second alternate.
     * It's also assumed that A0 would be the reference, so it remains unchanged too.
     * <p>
     * This pattern of the first allele moving along (and swapping) is what describes this function. Also, look
     * VariantVcfFactory.getSecondaryAlternates().
     *
     * @param parsedAllele the value of parsed alleles. e.g. 1 if genotype was "A1" (first allele).
     * @param numAllele current variant of the alternates.
     * @return the correct allele index depending on numAllele.
     */
    protected static int mapToMultiallelicIndex(int parsedAllele, int numAllele) {
        int correctedAllele = parsedAllele;
        if (parsedAllele > 0) {
            if (parsedAllele == numAllele + 1) {
                correctedAllele = 1;
            } else if (parsedAllele < numAllele + 1) {
                correctedAllele = parsedAllele + 1;
            }
        }
        return correctedAllele;
    }
}
