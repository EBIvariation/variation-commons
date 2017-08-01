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
package uk.ac.ebi.eva.commons.mongodb.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.eva.commons.core.models.mongodb.FeatureCoordinatesMongo;

import java.util.List;

/**
 * This interface documents how features can be queried.
 * <p>
 * This interface is used by Spring to create the query methods for features.
 * Spring creates the implementation automatically by looking at the method name.
 */
public interface FeatureRepository extends MongoRepository<FeatureCoordinatesMongo, String> {

    List<FeatureCoordinatesMongo> findByIdOrName(String id, String name);

}
