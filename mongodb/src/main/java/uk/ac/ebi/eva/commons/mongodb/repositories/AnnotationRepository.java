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
package uk.ac.ebi.eva.commons.mongodb.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import uk.ac.ebi.eva.commons.core.models.IAnnotationMetadata;
import uk.ac.ebi.eva.commons.core.models.mongodb.AnnotationMongo;
import uk.ac.ebi.eva.commons.core.models.mongodb.VariantMongo;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Repository to access the annotation collection.
 */
public interface AnnotationRepository extends MongoRepository<AnnotationMongo, String> {

    Set<AnnotationMongo> findByIdIn(Collection<String> ids);

    default Set<AnnotationMongo> findAnnotationsOfVariants(List<VariantMongo> variants) {
        Set<String> ids = new HashSet<>();
        for (VariantMongo variant : variants) {
            ids.addAll(variant.getAnnotationIds());
        }
        return findByIdIn(ids);
    }

    default Set<AnnotationMongo> findAnnotationsOfVariants(List<VariantMongo> variants,
                                                           IAnnotationMetadata annotationMetadata) {
        Set<String> ids = new HashSet<>();
        for (VariantMongo variant : variants) {
            ids.addAll(variant.getAnnotationIds(annotationMetadata));
        }
        return findByIdIn(ids);
    }


    default Map<String, Set<AnnotationMongo>> findAndIndexAnnotationsOfVariants(List<VariantMongo> variants) {
        Map<String, Set<AnnotationMongo>> indexedAnnotations = new HashMap<>();
        Set<AnnotationMongo> annotations = findAnnotationsOfVariants(variants);
        annotations.forEach(annotation -> {
            String variantId = annotation.buildVariantId();
            indexedAnnotations.putIfAbsent(variantId, new HashSet<>());
            indexedAnnotations.get(variantId).add(annotation);
        });
        return indexedAnnotations;
    }

    default Map<String, AnnotationMongo> findAndIndexAnnotationsOfVariants(List<VariantMongo> variants,
                                                                                IAnnotationMetadata annotationMetadata) {
        Map<String, AnnotationMongo> indexedAnnotations = new HashMap<>();
        Set<AnnotationMongo> annotations = findAnnotationsOfVariants(variants, annotationMetadata);
        annotations.forEach(annotation -> {
            String variantId = annotation.buildVariantId();
            indexedAnnotations.put(variantId, annotation);
        });
        return indexedAnnotations;
    }


    default AnnotationMongo findOne(String chromosome, int start, String referenceAllele, String alternativeAllele,
                                    String vepVersion, String vepCacheVersion) {
        return findOne(AnnotationMongo.buildAnnotationId(
                chromosome,
                start,
                referenceAllele,
                alternativeAllele,
                vepVersion,
                vepCacheVersion));
    }

}
