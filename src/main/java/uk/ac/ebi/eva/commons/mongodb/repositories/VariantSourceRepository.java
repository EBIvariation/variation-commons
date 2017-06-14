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
import uk.ac.ebi.eva.commons.mongodb.entity.VariantSourceDocument;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Extension of Spring's MongoRepository for VariantSourceDocument class.
 * <p>
 * This interface queries the VariantSourceDocument collection- i.e. collection containing information on files.
 * <p>
 * Methods include: finding all "VariantSourceDocument"s in the collection, finding "VariantSourceDocument"s with either
 * studyId matching given value, or studyName matching given value.
 */
public interface VariantSourceRepository extends MongoRepository<VariantSourceDocument, String> {

    List<VariantSourceDocument> findAll();

    List<VariantSourceDocument> findByStudyIdOrStudyName(String studyId, String studyName);

    List<VariantSourceDocument> findByStudyIdIn(List<String> studyIds, Pageable pageable);

    long countByStudyIdIn(List<String> studyIds);

    List<VariantSourceDocument> findByFileIdIn(List<String> fileIds, Pageable pageable);

    long countByFileIdIn(List<String> fileIds);

    default Table<String, String, List<String>> findAndIndexSamples() {
        List<VariantSourceDocument> variantSourceEntities = findAll();

        Table<String, String, List<String>> studyFileIdsToPositionSamples = HashBasedTable.create();
        for (VariantSourceDocument variantSourceDocument : variantSourceEntities) {
            final Map<String, Integer> sampleNamesToPosition = variantSourceDocument.getSamplesPosition();
            if (sampleNamesToPosition == null) {
                continue;
            }

            String[] samples = new String[sampleNamesToPosition.size()];
            for (Map.Entry<String, Integer> entry : sampleNamesToPosition.entrySet()) {
                samples[entry.getValue()] = entry.getKey();
            }

            String studyId = variantSourceDocument.getStudyId();
            String fileId = variantSourceDocument.getFileId();

            studyFileIdsToPositionSamples.put(studyId, fileId, Arrays.asList(samples));
        }
        return studyFileIdsToPositionSamples;
    }
}
