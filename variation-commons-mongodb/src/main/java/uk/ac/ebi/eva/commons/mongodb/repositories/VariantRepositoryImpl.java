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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import uk.ac.ebi.eva.commons.core.models.Region;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.AnnotationIndexMongo;
import uk.ac.ebi.eva.commons.mongodb.filter.VariantRepositoryFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Concrete implementation of the VariantRepository interface (relationship inferred by Spring),
 * due to a custom DBObject to VariantEntity conversion
 * <p>
 * <p>It also implements the VariantRepositoryCustom interface,
 * to provide an explicit implementation of the region query, using a margin for efficiency.
 */
public class VariantRepositoryImpl implements VariantRepositoryCustom {

    private MongoTemplate mongoTemplate;

    private final int MARGIN = 5000;

    private static final String GENE_IDS_FIELD = VariantMongo.ANNOTATION_FIELD + "." + AnnotationIndexMongo.XREFS_FIELD;

    @Autowired
    public VariantRepositoryImpl(MongoDbFactory mongoDbFactory, MappingMongoConverter mappingMongoConverter) {
        mongoTemplate = new MongoTemplate(mongoDbFactory, mappingMongoConverter);
    }

    @Override
    public List<VariantMongo> findByIdsAndComplexFilters(String id, List<VariantRepositoryFilter> filters,
                                                         List<String> exclude, Pageable pageable) {
        Query query = new Query(Criteria.where(VariantMongo.IDS_FIELD).is(id));
        return findByComplexFiltersHelper(query, filters, exclude, pageable);
    }

    @Override
    public Long countByIdsAndComplexFilters(String id, List<VariantRepositoryFilter> filters) {
        Criteria criteria = Criteria.where("ids").is(id);
        return countByComplexFiltersHelper(criteria, filters);
    }

    @Override
    public List<VariantMongo> findByGenesAndComplexFilters(List<String> geneIds,
                                                           List<VariantRepositoryFilter> filters,
                                                           List<String> exclude, Pageable pageable) {
        Query query = new Query(Criteria.where(GENE_IDS_FIELD).in(geneIds));
        return findByComplexFiltersHelper(query, filters, exclude, pageable);
    }

    @Override
    public Long countByGenesAndComplexFilters(List<String> geneIds, List<VariantRepositoryFilter> filters) {
        Criteria criteria = Criteria.where(GENE_IDS_FIELD).in(geneIds);
        return countByComplexFiltersHelper(criteria, filters);
    }

    @Override
    public List<VariantMongo> findByRegionsAndComplexFilters(List<Region> regions,
                                                             List<VariantRepositoryFilter> filters,
                                                             List<String> exclude, Pageable pageable) {
        Query query = new Query();
        Criteria criteria = getRegionsCriteria(regions);
        query.addCriteria(criteria);
        return findByComplexFiltersHelper(query, filters, exclude, pageable);
    }

    @Override
    public Long countByRegionsAndComplexFilters(List<Region> regions, List<VariantRepositoryFilter> filters) {
        Criteria criteria = getRegionsCriteria(regions);
        return countByComplexFiltersHelper(criteria, filters);
    }

    @Override
    public Set<String> findDistinctChromosomes() {
        return new HashSet<>(mongoTemplate.getCollection(mongoTemplate.getCollectionName(VariantMongo.class))
                .distinct(VariantMongo.CHROMOSOME_FIELD));
    }

    private List<VariantMongo> findByComplexFiltersHelper(Query query, List<VariantRepositoryFilter> filters,
                                                          List<String> exclude, Pageable pageable) {

        addFilterCriteriaToQuery(query, filters);

        ArrayList<String> sortProperties = new ArrayList<String>();
        sortProperties.add(VariantMongo.CHROMOSOME_FIELD);
        sortProperties.add(VariantMongo.START_FIELD);
        query.with(new Sort(Sort.Direction.ASC, sortProperties));

        Pageable pageable1 = (pageable != null) ? pageable : new PageRequest(0, 10);
        query.with(pageable1);

        if (exclude != null && !exclude.isEmpty()) {
            exclude.forEach(e -> query.fields().exclude(e));
        }

        return mongoTemplate.find(query, VariantMongo.class);
    }

    private void addFilterCriteriaToQuery(Query query, List<VariantRepositoryFilter> filters) {
        if (filters != null && filters.size() > 0) {
            List<Criteria> criteriaList = getFiltersCriteria(filters);
            for (Criteria criteria : criteriaList) {
                query.addCriteria(criteria);
            }
        }
    }

    private long countByComplexFiltersHelper(Criteria existingCriteria, List<VariantRepositoryFilter> filters) {
        List<Criteria> criteriaList = getFiltersCriteria(filters);
        criteriaList.add(existingCriteria);
        Criteria criteria = new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));

        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group().count().as("count")
        );

        AggregationResults<VariantAggregationCount> aggregationResults =
                mongoTemplate.aggregate(aggregation, VariantMongo.class, VariantAggregationCount.class);

        return aggregationResults.getMappedResults().size() > 0
                ? aggregationResults.getMappedResults().get(0).getCount() : 0;
    }

    private class VariantAggregationCount {
        private long count;

        public long getCount() {
            return count;
        }
    }

    private List<Criteria> getFiltersCriteria(List<VariantRepositoryFilter> filters) {
        List<Criteria> criteriaList = new ArrayList<>();
        for (VariantRepositoryFilter filter : filters) {
            criteriaList.add(filter.getCriteria());
        }
        return criteriaList;
    }

    private Criteria getRegionsCriteria(List<Region> regions) {
        List<Criteria> orRegionCriteria = new ArrayList<>();

        regions.forEach(region -> orRegionCriteria.add(
                Criteria.where(VariantMongo.CHROMOSOME_FIELD).is(region.getChromosome())
                        .and(VariantMongo.START_FIELD).lte(region.getEnd()).gt(region.getStart() - MARGIN)
                        .and(VariantMongo.END_FIELD).gte(region.getStart()).lt(region.getEnd() + MARGIN)));

        return new Criteria().orOperator(orRegionCriteria.toArray(new Criteria[orRegionCriteria.size()]));
    }

}