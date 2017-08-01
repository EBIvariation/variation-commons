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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.ebi.eva.commons.core.models.FeatureCoordinates;
import uk.ac.ebi.eva.commons.core.models.mongodb.FeatureCoordinatesMongo;
import uk.ac.ebi.eva.commons.mongodb.repositories.FeatureRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mongo service to access {@link FeatureCoordinates}
 */
@Service
public class FeatureService {

    @Autowired
    private FeatureRepository repository;

    public List<FeatureCoordinates> findByIdOrName(String id, String name) {
        return convert(repository.findByIdOrName(id, name));
    }

    private List<FeatureCoordinates> convert(List<FeatureCoordinatesMongo> features) {
        return features.stream().map(FeatureCoordinates::new).collect(Collectors.toList());
    }
}
