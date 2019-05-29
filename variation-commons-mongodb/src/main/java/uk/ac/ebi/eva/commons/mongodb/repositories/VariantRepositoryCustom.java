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

import org.springframework.data.domain.Pageable;
import uk.ac.ebi.eva.commons.core.models.Region;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.filter.VariantRepositoryFilter;

import java.util.List;
import java.util.Set;

/**
 * Interface to declare additional repository methods with a custom implementation,
 * instead of the one that Spring Data would provide by default.
 */
interface VariantRepositoryCustom {

    /**
     * Query for variants with a specified ID (eg. RS IDs), and whose attributes match those values specified in the
     * filters: study, consequence type, minor allele frequency and protein substitution scores (Polyphen and SIFT).
     *
     * @param ids
     * @param filters List of VariantRepositoryFilter objects by which to filter the query
     * @param exclude List of strings, each matching a field in the variant Mongo documents. Fields specified in the
     *                list will be excluded from the returned document(s)
     * @return VariantEntities whose values are within the bounds of the filters
     */
    List<VariantMongo> findByIdsAndComplexFilters(List<String> ids, List<VariantRepositoryFilter> filters,
                                                  List<String> exclude,
                                                  Pageable pageable);

    Long countByIdsAndComplexFilters(List<String> ids, List<VariantRepositoryFilter> filters);

    List<VariantMongo> findByGenesAndComplexFilters(List<String> geneIds, List<VariantRepositoryFilter> filters,
                                                    List<String> exclude, Pageable pageable);

    Long countByGenesAndComplexFilters(List<String> geneIds, List<VariantRepositoryFilter> filters);


    /**
     * Query for variants within a set of specified genomic regions, and whose attributes match those values specified
     * in the filters: study, consequence type, minor allele frequency and protein substitution scores (Polyphen and
     * SIFT).
     *
     * @param regions List of region objects to invlude in query
     * @param filters List of VariantRepositoryFilter objects by which to filter the query
     * @param exclude List of strings, each matching a field in the variant Mongo documents. Fields specified in the
     *                list will be excluded from the returned document(s)
     * @return VariantEntities whose values are within the bounds of the filters
     */
    List<VariantMongo> findByRegionsAndComplexFilters(List<Region> regions, List<VariantRepositoryFilter> filters,
                                                      List<String> exclude, Pageable pageable);

    Long countByRegionsAndComplexFilters(List<Region> regions, List<VariantRepositoryFilter> filters);

    /**
     * Query for distinct chromosomes for variants in the collection
     *
     * @return List of chromosome names
     */
    Set<String> findDistinctChromosomes();

    List<VariantMongo> findByChromosomeAndOtherBeaconFilters(String chr,List<VariantRepositoryFilter> filters);
}
