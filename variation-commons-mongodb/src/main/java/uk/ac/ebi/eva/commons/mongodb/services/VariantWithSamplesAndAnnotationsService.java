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
import uk.ac.ebi.eva.commons.core.models.IAnnotationMetadata;
import uk.ac.ebi.eva.commons.core.models.Region;
import uk.ac.ebi.eva.commons.core.models.VariantStatistics;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.core.models.ws.VariantSourceEntryWithSampleNames;
import uk.ac.ebi.eva.commons.core.models.ws.VariantWithSamplesAndAnnotation;
import uk.ac.ebi.eva.commons.mongodb.entities.AnnotationMetadataMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.AnnotationMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantStatisticsMongo;
import uk.ac.ebi.eva.commons.mongodb.filter.VariantRepositoryFilter;
import uk.ac.ebi.eva.commons.mongodb.repositories.AnnotationMetadataRepository;
import uk.ac.ebi.eva.commons.mongodb.repositories.AnnotationRepository;
import uk.ac.ebi.eva.commons.mongodb.repositories.VariantRepository;
import uk.ac.ebi.eva.commons.mongodb.repositories.VariantSourceRepository;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mongo persistence service to retrieve {@link VariantWithSamplesAndAnnotation}
 */
@Service
public class VariantWithSamplesAndAnnotationsService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private VariantSourceRepository variantSourceRepository;


    @Autowired
    private AnnotationMetadataRepository annotationMetadataRepository;


    public List<VariantWithSamplesAndAnnotation> findByGenesAndComplexFilters(List<String> geneIds,
                                                                              List<VariantRepositoryFilter> variantRepositoryFilters,
                                                                              IAnnotationMetadata annotationMetadata,
                                                                              List<String> exclude, Pageable pageable)
            throws AnnotationMetadataNotFoundException {
        return convert(variantRepository.findByGenesAndComplexFilters(geneIds, variantRepositoryFilters, exclude, pageable),
                       annotationMetadata);
    }

    private List<VariantWithSamplesAndAnnotation> convert(List<VariantMongo> variantMongos,
                                                          IAnnotationMetadata annotationMetadata)
            throws AnnotationMetadataNotFoundException {
        Table<String, String, List<String>> studyFileIdsToSamples = variantSourceRepository.findAndIndexSamples();

        if (annotationMetadata == null) {
            List<AnnotationMetadataMongo> annotationMetadataList = annotationMetadataRepository.findByDefaultVersionTrue();
            if (annotationMetadataList.size() > 0) {
                annotationMetadata = annotationMetadataList.get(0);
            }
        } else if (annotationMetadataRepository.findByCacheVersionAndVepVersion(annotationMetadata.getCacheVersion(),
                                                                                annotationMetadata.getVepVersion())
                                               .size() == 0) {
            throw new AnnotationMetadataNotFoundException(annotationMetadataRepository.findAllByOrderByCacheVersionDescVepVersionDesc(),
                                                          annotationMetadata);
        }


        Map<String, AnnotationMongo> indexedAnnotations =
                (annotationMetadata != null) ?
                        annotationRepository.findAndIndexAnnotationsOfVariants(variantMongos, annotationMetadata)
                        : new HashMap<>();

        List<VariantWithSamplesAndAnnotation> variantsList =
                variantMongos.stream().map(variant ->
                                                   convert(
                                                           variant,
                                                           studyFileIdsToSamples,
                                                           indexedAnnotations.getOrDefault(variant.getId(), null)))
                             .collect(Collectors.toList());

        return variantsList;
    }

    private static VariantWithSamplesAndAnnotation convert(VariantMongo variantMongo,
                                                           Table<String, String, List<String>> sampleNames,
                                                           AnnotationMongo annotation) {
        VariantWithSamplesAndAnnotation variant = new VariantWithSamplesAndAnnotation(
                variantMongo.getChromosome(),
                variantMongo.getStart(),
                variantMongo.getEnd(),
                variantMongo.getReference(),
                variantMongo.getAlternate());
        variant.setIds(variantMongo.getIds());
        Table<String, String, Map<String, VariantStatistics>> variantStatisticsMongosTable
                = variantStatsMongoToTable(variantMongo.getVariantStatsMongo(), variantMongo);
        variant.addSourceEntries(convert(variantMongo.getSourceEntries(), sampleNames, variantStatisticsMongosTable));
        if (annotation != null) {
            variant.setAnnotation(new Annotation(annotation));
        }
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
        return new VariantSourceEntryWithSampleNames(sourceEntryMongo.getFileId(), sourceEntryMongo.getStudyId(),
                                                     sourceEntryMongo.getSecondaryAlternates(), sourceEntryMongo.getFormat(),
                                                     cohortIdToVariantStatsMongoMap, sourceEntryMongo.getAttributes(),
                                                     joinSamplesDataWithSampleNamesHelper(sourceEntryMongo, samples));
    }

    public Long countByGenesAndComplexFilters(List<String> geneIds, List<VariantRepositoryFilter> variantRepositoryFilters) {
        return variantRepository.countByGenesAndComplexFilters(geneIds, variantRepositoryFilters);
    }

    public List<VariantWithSamplesAndAnnotation> findByRegionsAndComplexFilters(List<Region> regions,
                                                                                List<VariantRepositoryFilter> variantRepositoryFilters,
                                                                                IAnnotationMetadata annotationMetadata,
                                                                                List<String> exclude,
                                                                                Pageable pageable)
            throws AnnotationMetadataNotFoundException {
        return convert(variantRepository.findByRegionsAndComplexFilters(regions, variantRepositoryFilters, exclude, pageable),
                       annotationMetadata);
    }

    public Long countByRegionsAndComplexFilters(List<Region> regions, List<VariantRepositoryFilter> variantRepositoryFilters) {
        return variantRepository.countByRegionsAndComplexFilters(regions, variantRepositoryFilters);
    }

    public List<VariantWithSamplesAndAnnotation> findByIdsAndComplexFilters(String id,
                                                                            List<VariantRepositoryFilter> variantRepositoryFilters,
                                                                            IAnnotationMetadata annotationMetadata,
                                                                            List<String> exclude, Pageable pageable)
            throws AnnotationMetadataNotFoundException {
        return convert(variantRepository.findByIdsAndComplexFilters(id, variantRepositoryFilters, exclude, pageable),
                       annotationMetadata);
    }

    public Long countByIdsAndComplexFilters(String id, List<VariantRepositoryFilter> variantRepositoryFilters) {
        return variantRepository.countByIdsAndComplexFilters(id, variantRepositoryFilters);
    }

    public List<VariantWithSamplesAndAnnotation> findByChromosomeAndStartAndReferenceAndAlternate(String chromosome,
                                                                                                  long start,
                                                                                                  String reference,
                                                                                                  String alternate,
                                                                                                  IAnnotationMetadata annotationMetadata)
            throws AnnotationMetadataNotFoundException {
        return convert(
                variantRepository.findByChromosomeAndStartAndReferenceAndAlternate(chromosome, start, reference, alternate),
                annotationMetadata);
    }

    public List<VariantWithSamplesAndAnnotation> findByChromosomeAndStartAndReference(String chr, long start, String ref,
                                                                                      IAnnotationMetadata annotationMetadata)
            throws AnnotationMetadataNotFoundException {
        return convert(variantRepository.findByChromosomeAndStartAndReference(chr, start, ref), annotationMetadata);
    }

    public List<VariantWithSamplesAndAnnotation> findByChromosomeAndStartAndReferenceAndAlternateAndStudyIn(
            String chromosome,
            long start,
            String reference,
            String alternate,
            List<String> studyIds, IAnnotationMetadata annotationMetadata) throws AnnotationMetadataNotFoundException {
        return convert(variantRepository.findByChromosomeAndStartAndReferenceAndAlternateAndStudyIn(chromosome, start,
                reference, alternate, studyIds), annotationMetadata);
    }

    public List<VariantWithSamplesAndAnnotation> findByChromosomeAndStartAndReferenceAndStudyIn(String chromosome,
                                                                                                long start,
                                                                                                String reference,
                                                                                                List<String> studyIds,
                                                                                                IAnnotationMetadata annotationMetadata)
            throws AnnotationMetadataNotFoundException {
        return convert(variantRepository.findByChromosomeAndStartAndReferenceAndStudyIn(chromosome, start,
                reference, studyIds), annotationMetadata);
    }

    public List<VariantWithSamplesAndAnnotation> findByChromosomeAndStartAndAltAndStudyIn(String chr, long start,
                                                                                          String alt,
                                                                                          List<String> studyIds,
                                                                                          IAnnotationMetadata annotationMetadata)
            throws AnnotationMetadataNotFoundException {
        return convert(variantRepository.findByChromosomeAndStartAndAltAndStudyIn(chr, start, alt, studyIds),
                       annotationMetadata);
    }

    public List<VariantWithSamplesAndAnnotation> findByChromosomeAndStartAndTypeAndStudyIn(String chr, long start,
                                                                                           VariantType type,
                                                                                           List<String> studyIds,
                                                                                           IAnnotationMetadata annotationMetadata)
            throws AnnotationMetadataNotFoundException {
        return convert(variantRepository.findByChromosomeAndStartAndTypeAndStudyIn(chr, start, type, studyIds),
                       annotationMetadata);
    }

    public Set<String> findDistinctChromosomes() {
        return variantRepository.findDistinctChromosomes();
    }

    /**
     * Joins the list of sample data from the VariantSourceEntryMongo with their correspondent name from the sample list.
     *
     * Currently this method is also needed by VariantWithSamplesNames. VariantSourceEntryMongo will eventually stop
     * being part of a different class hierarchy and this particular implementation will be no longer needed, once
     * the number of samples for a source entry is added to the database.
     *
     * @param variantSourceEntry
     * @param samples
     * @return
     */
    private static LinkedHashMap<String, Map<String, String>> joinSamplesDataWithSampleNamesHelper(
            VariantSourceEntryMongo variantSourceEntry,
            List<String> samples) {
        LinkedHashMap<String, Map<String, String>> temp;
        if (variantSourceEntry == null || samples == null) {
            temp = new LinkedHashMap<>();
        } else {
            temp = VariantSourceEntryWithSampleNames.joinSamplesDataWithSampleNames(variantSourceEntry.deflateSamplesData(samples.size()), samples);
        }
        return temp;
    }
}
