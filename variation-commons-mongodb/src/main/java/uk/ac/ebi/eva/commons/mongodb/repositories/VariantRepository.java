/*
 * European Variation Archive (EVA) - Open-access database of all types of genetic
 * variation data from all species
 *
 * Copyright 2016 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.mongodb.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;

import java.util.List;
import java.util.stream.Stream;

/**
 * Spring MongoRepository for {@link VariantMongo} class.
 * <p>
 * Methods include querying by id, and by region.
 */
@Repository
public interface VariantRepository extends MongoRepository<VariantMongo, String>,
        VariantRepositoryCustom {

    @Query("{'chr': ?0, 'start': ?1, 'ref': ?2, 'alt': ?3}")
    List<VariantMongo> findByChromosomeAndStartAndReferenceAndAlternate(String chromosome, long start,
                                                                        String reference, String alternate);

    @Query("{'chr': ?0, 'start': ?1, 'ref': ?2, 'alt': ?3, 'files.sid': {$in : ?4}}")
    List<VariantMongo> findByChromosomeAndStartAndReferenceAndAlternateAndStudyIn(String chromosome, long start,
                                                                                  String reference, String alternate,
                                                                                  List<String> studyIds);

    @Query("{'chr': ?0, 'start': ?1, 'ref': ?2, 'files.sid': {$in : ?3}}")
    List<VariantMongo> findByChromosomeAndStartAndReferenceAndStudyIn(String chromosome, long start, String reference,
                                                                      List<String> studyIds);

    @Query("{'chr': ?0, 'start': ?1, 'ref': ?2}")
    List<VariantMongo> findByChromosomeAndStartAndReference(String chr, long start, String ref);

    @Query(value = "{'chr': ?0, 'start': ?1, 'alt': ?2, 'files.sid': {$in : ?3}}}")
    List<VariantMongo> findByChromosomeAndStartAndAltAndStudyIn(String chr, long start, String alt,
                                                                List<String> studyIds);

    @Query(value = "{'chr': ?0, 'start': ?1, 'type': ?2, 'files.sid': {$in : ?3}}}")
    List<VariantMongo> findByChromosomeAndStartAndTypeAndStudyIn(String chr, long start, VariantType type,
                                                                 List<String> studyIds);

    @Query(value = "{'chr': ?0, 'files.sid': {$in : ?1}}}")
    Stream<VariantMongo> findAllByChromosomeAndStudyInSorted(String chr, List<String> studyIds, Sort sort);

    default VariantMongo findOneByChromosomeAndStudyInSorted(String chr, List<String> studyIds, Sort sort) {
        return findAllByChromosomeAndStudyInSorted(chr, studyIds, sort).findFirst().get();
    }
}
