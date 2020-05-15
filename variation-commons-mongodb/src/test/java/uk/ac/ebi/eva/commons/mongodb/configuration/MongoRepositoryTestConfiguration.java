/*
 * European Variation Archive (EVA) - Open-access database of all types of genetic
 * variation data from all species
 *
 * Copyright 2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.util.Assert;

@Configuration
@EntityScan(basePackages = {"uk.ac.ebi.eva.commons.mongodb.entities"})
@EnableMongoRepositories(basePackages = "uk.ac.ebi.eva.commons.mongodb.repositories")
@EnableMongoAuditing
@AutoConfigureDataMongo
public class MongoRepositoryTestConfiguration {

    @Bean
    public String mongoCollectionsAnnotationMetadata(
            @Value("${eva.mongo.collections.annotation-metadata:#{null}}") String collectionAnnotationMetadata) {
        Assert.notNull(collectionAnnotationMetadata);
        return collectionAnnotationMetadata;
    }

    @Bean
    public String mongoCollectionsAnnotations(
            @Value("${eva.mongo.collections.annotations:#{null}}") String collectionAnnotations) {
        Assert.notNull(collectionAnnotations);
        return collectionAnnotations;
    }

    @Bean
    public String mongoCollectionsFeatures(
            @Value("${eva.mongo.collections.features:#{null}}") String collectionFeatures) {
        Assert.notNull(collectionFeatures);
        return collectionFeatures;
    }

    @Bean
    public String mongoCollectionsVariants(
            @Value("${eva.mongo.collections.variants:#{null}}") String collectionVariants) {
        Assert.notNull(collectionVariants);
        return collectionVariants;
    }

    @Bean
    public String mongoCollectionsFiles(@Value("${eva.mongo.collections.files:#{null}}") String collectionFiles) {
        Assert.notNull(collectionFiles);
        return collectionFiles;
    }

    @Bean
    public String mongoCollectionsSamples(@Value("${eva.mongo.collections.samples:#{null}}") String collectionSamples) {
        Assert.notNull(collectionSamples);
        return collectionSamples;
    }
}
