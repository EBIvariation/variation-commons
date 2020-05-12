/*
 * Copyright 2019 EMBL - European Bioinformatics Institute
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
import uk.ac.ebi.eva.commons.core.models.DefaultLocusRangeMetadata;
import uk.ac.ebi.eva.commons.mongodb.entities.DefaultLocusRangeMetadataMongo;
import uk.ac.ebi.eva.commons.mongodb.repositories.DefaultLocusRangeMetadataRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mongo persistence service to access {@link DefaultLocusRangeMetadata}
 */
@Service
public class DefaultLocusRangeMetadataService {

    @Autowired
    private DefaultLocusRangeMetadataRepository repository;

    public List<DefaultLocusRangeMetadata> findAllByOrderByChromosomeAscStartAscEndAsc() {
        return convert(repository.findAllByOrderByChromosomeAscStartAscEndAsc());
    }

    private List<DefaultLocusRangeMetadata> convert(List<DefaultLocusRangeMetadataMongo> defaultLocusRangeMetadatas) {
        return defaultLocusRangeMetadatas.stream().map(DefaultLocusRangeMetadata::new).collect(Collectors.toList());
    }

}
