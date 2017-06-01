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

import uk.ac.ebi.eva.commons.models.data.VariantSourceEntity;

/**
 * Spring MongoRepository for VariantStudySummary class.
 * <p>
 * Please note that this interface queries the VariantSourceEntity collection, and returns VariantStudySummary
 * which is a subset (mongo projection) with the fields related to the study.
 * <p>
 * The implementation is in {@link VariantStudySummaryRepositoryImpl}
 */
public interface VariantStudySummaryRepository
        extends MongoRepository<VariantSourceEntity, String>, VariantStudySummaryRepositoryCustom {

}

