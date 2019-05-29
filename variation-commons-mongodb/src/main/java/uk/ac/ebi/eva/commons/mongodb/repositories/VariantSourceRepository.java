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

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Extension of Spring's MongoRepository for VariantSourceMongo class.
 * <p>
 * This interface queries the VariantSourceMongo collection- i.e. collection containing information on files.
 * <p>
 * Methods include: finding all "VariantSourceMongo"s in the collection, finding "VariantSourceMongo"s with either
 * studyId matching given value, or studyName matching given value.
 */
public interface VariantSourceRepository extends MongoRepository<VariantSourceMongo, String> {

    List<VariantSourceMongo> findAll();

    List<VariantSourceMongo> findByStudyIdOrStudyName(String studyId, String studyName);

    List<VariantSourceMongo> findByStudyIdIn(List<String> studyIds, Pageable pageable);

    long countByStudyIdIn(List<String> studyIds);

    List<VariantSourceMongo> findByFileIdIn(List<String> fileIds, Pageable pageable);

    long countByFileIdIn(List<String> fileIds);

    default Table<String, String, List<String>> findAndIndexSamples() {
        List<VariantSourceMongo> variantSourceEntities = findAll();

        Table<String, String, List<String>> studyFileIdsToPositionSamples = HashBasedTable.create();
        for (VariantSourceMongo variantSourceMongo : variantSourceEntities) {
            final Map<String, Integer> sampleNamesToPosition = variantSourceMongo.getSamplesPosition();
            if (sampleNamesToPosition == null) {
                continue;
            }

            String[] samples = new String[sampleNamesToPosition.size()];
            for (Map.Entry<String, Integer> entry : sampleNamesToPosition.entrySet()) {
                samples[entry.getValue()] = entry.getKey();
            }

            String studyId = variantSourceMongo.getStudyId();
            String fileId = variantSourceMongo.getFileId();

            studyFileIdsToPositionSamples.put(studyId, fileId, Arrays.asList(samples));
        }
        return studyFileIdsToPositionSamples;
    }
}
