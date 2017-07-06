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
package uk.ac.ebi.eva.commons.mongodb.services;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import uk.ac.ebi.eva.commons.core.models.Annotation;
import uk.ac.ebi.eva.commons.core.models.Region;
import uk.ac.ebi.eva.commons.core.models.VariantStatistics;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.core.models.ws.VariantSourceEntryWithSampleNames;
import uk.ac.ebi.eva.commons.core.models.ws.VariantWithSamplesAndAnnotations;
import uk.ac.ebi.eva.commons.mongodb.entities.AnnotationMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantStatisticsMongo;
import uk.ac.ebi.eva.commons.mongodb.filter.VariantRepositoryFilter;
import uk.ac.ebi.eva.commons.mongodb.repositories.AnnotationRepository;
import uk.ac.ebi.eva.commons.mongodb.repositories.VariantRepository;
import uk.ac.ebi.eva.commons.mongodb.repositories.VariantSourceRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mongo persistence service to retrieve {@link VariantWithSamplesAndAnnotations}
 */
@Service
public class VariantWithSamplesAndAnnotationsService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private VariantSourceRepository variantSourceRepository;

    public List<VariantWithSamplesAndAnnotations> findByGenesAndComplexFilters(List<String> geneIds,
                                                                               List<VariantRepositoryFilter> filters,
                                                                               List<String> exclude, Pageable pageable) {
        return convert(variantRepository.findByGenesAndComplexFilters(geneIds, filters, exclude, pageable));
    }

    private List<VariantWithSamplesAndAnnotations> convert(List<VariantMongo> variantMongos) {
        Table<String, String, List<String>> studyFileIdsToSamples = variantSourceRepository.findAndIndexSamples();
        Map<String, Set<AnnotationMongo>> indexedAnnotations = annotationRepository
                .findAndIndexAnnotationsOfVariants(variantMongos);

        List<VariantWithSamplesAndAnnotations> variantsList =
                variantMongos.stream().map(variant ->
                                                   convert(variant, studyFileIdsToSamples,
                                                           indexedAnnotations.getOrDefault(variant.getId(), new HashSet<>())))
                             .collect(Collectors.toList());

        VariantStatistics.calculateStatsForVariantsList(variantsList, null);
        return variantsList;
    }

    private static VariantWithSamplesAndAnnotations convert(VariantMongo variantMongo,
                                                            Table<String, String, List<String>> sampleNames,
                                                            Set<AnnotationMongo> annotations) {
        VariantWithSamplesAndAnnotations variant = new VariantWithSamplesAndAnnotations(
                variantMongo.getChromosome(),
                variantMongo.getStart(),
                variantMongo.getEnd(),
                variantMongo.getReference(),
                variantMongo.getAlternate());
        variant.setIds(variantMongo.getIds());
        Table<String, String, Map<String, VariantStatistics>> variantStatisticsMongosTable
                = variantStatsMongoToTable(variantMongo.getVariantStatsMongo(), variantMongo);
        variant.addSourceEntries(convert(variantMongo.getSourceEntries(), sampleNames, variantStatisticsMongosTable));
        variant.setAnnotations(convert(annotations));
        return variant;
    }

    private static Table<String, String, Map<String, VariantStatistics>> variantStatsMongoToTable(
            Set<VariantStatisticsMongo> variantStatisticsMongos, VariantMongo variantMongo) {
        Table<String, String, Map<String, VariantStatistics>> variantStatisticsMongosTable = HashBasedTable.create();
        for (VariantStatisticsMongo variantStatisticsMongo : variantStatisticsMongos) {
            String studyId = variantStatisticsMongo.getStudyId();
            String fileId = variantStatisticsMongo.getFileId();
            String cohortId = variantStatisticsMongo.getCohortId();
            VariantStatistics variantStatistics =
                    new VariantStatistics(variantStatisticsMongo, variantMongo.getReference(),
                                          variantMongo.getAlternate(), variantMongo.getType());
            if (variantStatisticsMongosTable.contains(studyId, fileId)) {
                variantStatisticsMongosTable.get(studyId, fileId).put(cohortId, variantStatistics);
            } else {
                Map<String, VariantStatistics> cohortIdToVariantStatsMongo = new HashMap<>();
                cohortIdToVariantStatsMongo.put(cohortId, variantStatistics);
                variantStatisticsMongosTable.put(studyId, fileId, cohortIdToVariantStatsMongo);
            }

        }
        return variantStatisticsMongosTable;
    }

    private static Set<Annotation> convert(Set<AnnotationMongo> annotations) {
        return annotations.stream().map(Annotation::new).collect(Collectors.toSet());
    }

    private static List<VariantSourceEntryWithSampleNames> convert(Set<VariantSourceEntryMongo> sourceEntries,
                                                                   Table<String, String, List<String>> sampleNames,
                                                                   Table<String, String, Map<String, VariantStatistics>> variantStatisticsMongosTable) {
        return sourceEntries.stream()
                .map(entry -> convert(entry, sampleNames.get(entry.getStudyId(), entry.getFileId()),
                                      variantStatisticsMongosTable.get(entry.getStudyId(), entry.getFileId())))
                .collect(Collectors.toList());
    }

    private static VariantSourceEntryWithSampleNames convert(VariantSourceEntryMongo sourceEntryMongo,
                                                             List<String> samples,
                                                             Map<String, VariantStatistics> cohortIdToVariantStatsMongoMap) {
        return new VariantSourceEntryWithSampleNames(sourceEntryMongo, samples, cohortIdToVariantStatsMongoMap);
    }

    public Long countByGenesAndComplexFilters(List<String> geneIds, List<VariantRepositoryFilter> filters) {
        return variantRepository.countByGenesAndComplexFilters(geneIds, filters);
    }

    public List<VariantWithSamplesAndAnnotations> findByRegionsAndComplexFilters(List<Region> regions,
                                                                                 List<VariantRepositoryFilter> filters,
                                                                                 List<String> exclude, Pageable pageable) {
        return convert(variantRepository.findByRegionsAndComplexFilters(regions, filters, exclude, pageable));
    }

    public Long countByRegionsAndComplexFilters(List<Region> regions, List<VariantRepositoryFilter> filters) {
        return variantRepository.countByRegionsAndComplexFilters(regions, filters);
    }

    public List<VariantWithSamplesAndAnnotations> findByIdsAndComplexFilters(String id,
                                                                             List<VariantRepositoryFilter> filters,
                                                                             List<String> exclude, Pageable pageable) {
        return convert(variantRepository.findByIdsAndComplexFilters(id, filters, exclude, pageable));
    }

    public Long countByIdsAndComplexFilters(String id, List<VariantRepositoryFilter> filters) {
        return variantRepository.countByIdsAndComplexFilters(id, filters);
    }

    public List<VariantWithSamplesAndAnnotations> findByChromosomeAndStartAndReferenceAndAlternate(String chromosome, int start,
                                                                                                   String reference, String alternate) {
        return convert(variantRepository.findByChromosomeAndStartAndReferenceAndAlternate(chromosome, start,
                reference,
                alternate));
    }

    public List<VariantWithSamplesAndAnnotations> findByChromosomeAndStartAndReference(String chr, int start, String ref) {
        return convert(variantRepository.findByChromosomeAndStartAndReference(chr, start, ref));
    }

    public List<VariantWithSamplesAndAnnotations> findByChromosomeAndStartAndReferenceAndAlternateAndStudyIn(String chromosome,
                                                                                                             int start,
                                                                                                             String reference,
                                                                                                             String alternate,
                                                                                                             List<String> studyIds) {
        return convert(variantRepository.findByChromosomeAndStartAndReferenceAndAlternateAndStudyIn(chromosome, start,
                reference, alternate, studyIds));
    }

    public List<VariantWithSamplesAndAnnotations> findByChromosomeAndStartAndReferenceAndStudyIn(String chromosome, int start,
                                                                                                 String reference,
                                                                                                 List<String> studyIds) {
        return convert(variantRepository.findByChromosomeAndStartAndReferenceAndStudyIn(chromosome, start,
                reference, studyIds));
    }

    public List<VariantWithSamplesAndAnnotations> findByChromosomeAndStartAndAltAndStudyIn(String chr, int start, String alt,
                                                                                           List<String> studyIds) {
        return convert(variantRepository.findByChromosomeAndStartAndAltAndStudyIn(chr, start, alt, studyIds));
    }

    public List<VariantWithSamplesAndAnnotations> findByChromosomeAndStartAndTypeAndStudyIn(String chr, int start, VariantType type,
                                                                                            List<String> studyIds) {
        return convert(variantRepository.findByChromosomeAndStartAndTypeAndStudyIn(chr, start, type, studyIds));
    }

    public Set<String> findDistinctChromosomes() {
        return variantRepository.findDistinctChromosomes();
    }
}
