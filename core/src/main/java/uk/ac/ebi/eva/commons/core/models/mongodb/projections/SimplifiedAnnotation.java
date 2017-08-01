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
package uk.ac.ebi.eva.commons.core.models.mongodb.projections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.core.models.mongodb.AnnotationMongo;

import static uk.ac.ebi.eva.commons.core.models.mongodb.AnnotationMongo.CHROMOSOME_FIELD;
import static uk.ac.ebi.eva.commons.core.models.mongodb.AnnotationMongo.END_FIELD;
import static uk.ac.ebi.eva.commons.core.models.mongodb.AnnotationMongo.START_FIELD;
import static uk.ac.ebi.eva.commons.core.models.mongodb.AnnotationMongo.VEP_CACHE_VERSION_FIELD;
import static uk.ac.ebi.eva.commons.core.models.mongodb.AnnotationMongo.VEP_VERSION_FIELD;

/**
 * Simplified form of {@link AnnotationMongo} used to improve the update of annotations in mongo.
 */
public class SimplifiedAnnotation {

    @Id
    private String id;

    @Field(value = CHROMOSOME_FIELD)
    private String chromosome;

    @Field(value = START_FIELD)
    private int start;

    @Field(value = END_FIELD)
    private int end;

    @Field(value = VEP_VERSION_FIELD)
    private String vepVersion;

    @Field(value = VEP_CACHE_VERSION_FIELD)
    private String vepCacheVersion;

    public SimplifiedAnnotation(AnnotationMongo annotation) {
        this.id = annotation.getId();
        this.chromosome = annotation.getChromosome();
        this.start = annotation.getStart();
        this.end = annotation.getEnd();
        this.vepVersion = annotation.getVepVersion();
        this.vepCacheVersion = annotation.getVepCacheVersion();
    }
}
