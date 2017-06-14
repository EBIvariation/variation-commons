/*
 * Copyright 2016 EMBL - European Bioinformatics Institute
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

import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.ac.ebi.eva.commons.core.models.Annotation;
import uk.ac.ebi.eva.commons.core.models.Region;
import uk.ac.ebi.eva.commons.core.models.VariantSourceEntryWithSamples;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.core.models.VariantWithSamplesAndAnnotations;
import uk.ac.ebi.eva.commons.mongodb.entity.AnnotationDocument;
import uk.ac.ebi.eva.commons.mongodb.entity.VariantDocument;
import uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.mongodb.filter.VariantEntityRepositoryFilter;
import uk.ac.ebi.eva.commons.mongodb.repositories.AnnotationRepository;
import uk.ac.ebi.eva.commons.mongodb.repositories.VariantRepository;
import uk.ac.ebi.eva.commons.mongodb.repositories.VariantSourceRepository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VariantWithAnnotationsService {

    @Autowired
    private VariantRepository variantRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private VariantSourceRepository variantSourceRepository;

    public List<VariantWithSamplesAndAnnotations> findByGenesAndComplexFilters(List<String> geneIds,
                                                                               List<VariantEntityRepositoryFilter> filters,
                                                                               List<String> exclude, Pageable pageable) {
        return convert(variantRepository.findByGenesAndComplexFilters(geneIds, filters, exclude, pageable));
    }

    private List<VariantWithSamplesAndAnnotations> convert(List<VariantDocument> variantDocuments) {
        Table<String, String, List<String>> studyFileIdsToSamples = variantSourceRepository.findAndIndexSamples();
        Map<String, Set<AnnotationDocument>> indexedAnnotations = annotationRepository
                .findAndIndexAnnotationsOfVariants(variantDocuments);

        return variantDocuments.stream()
                .map(variant -> convert(variant, studyFileIdsToSamples, indexedAnnotations.get(variant.getId())))
                .collect(Collectors.toList());
    }

    private static VariantWithSamplesAndAnnotations convert(VariantDocument variantDocument,
                                                            Table<String, String, List<String>> sampleNames,
                                                            Set<AnnotationDocument> annotations) {
        VariantWithSamplesAndAnnotations variant = new VariantWithSamplesAndAnnotations(
                variantDocument.getChromosome(),
                variantDocument.getStart(),
                variantDocument.getEnd(),
                variantDocument.getReference(),
                variantDocument.getAlternate());
        variant.setIds(variantDocument.getIds());
        variant.addSourceEntries(convert(variantDocument.getSourceEntries(), sampleNames));
        variant.setAnnotations(convert(annotations));
        return variant;
    }

    private static Set<Annotation> convert(Set<AnnotationDocument> annotations) {
        return annotations.stream().map(annotation -> new Annotation(annotation)).collect(Collectors.toSet());
    }

    private static List<VariantSourceEntryWithSamples> convert(Set<VariantSourceEntryMongo> sourceEntries,
                                                               Table<String, String, List<String>> sampleNames) {
        return sourceEntries.stream()
                .map(entry -> convert(entry, sampleNames.get(entry.getStudyId(), entry.getFileId())))
                .collect(Collectors.toList());
    }

    private static VariantSourceEntryWithSamples convert(VariantSourceEntryMongo sourceEntryMongo,
                                                         List<String> samples) {
        return new VariantSourceEntryWithSamples(sourceEntryMongo, samples);
    }

    public Long countByGenesAndComplexFilters(List<String> geneIds, List<VariantEntityRepositoryFilter> filters) {
        return variantRepository.countByGenesAndComplexFilters(geneIds, filters);
    }

    public List<VariantWithSamplesAndAnnotations> findByRegionsAndComplexFilters(List<Region> regions,
                                                                                 List<VariantEntityRepositoryFilter> filters,
                                                                                 List<String> exclude, Pageable pageable) {
        return convert(variantRepository.findByRegionsAndComplexFilters(regions, filters, exclude, pageable));
    }

    public Long countByRegionsAndComplexFilters(List<Region> regions, List<VariantEntityRepositoryFilter> filters) {
        return variantRepository.countByRegionsAndComplexFilters(regions, filters);
    }

    public List<VariantWithSamplesAndAnnotations> findByIdsAndComplexFilters(String id,
                                                                             List<VariantEntityRepositoryFilter> filters,
                                                                             List<String> exclude, Pageable pageable) {
        return convert(variantRepository.findByIdsAndComplexFilters(id, filters, exclude, pageable));
    }

    public Long countByIdsAndComplexFilters(String id, List<VariantEntityRepositoryFilter> filters) {
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
