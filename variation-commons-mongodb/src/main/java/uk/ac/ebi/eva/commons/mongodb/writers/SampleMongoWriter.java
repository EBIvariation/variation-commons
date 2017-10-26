/*
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
package uk.ac.ebi.eva.commons.mongodb.writers;

import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import uk.ac.ebi.eva.commons.mongodb.entities.SampleMongo;

public class SampleMongoWriter extends MongoItemWriter<SampleMongo> {

    private final MongoOperations mongoOperations;

    private String collection;

    public SampleMongoWriter(String collection, MongoOperations mongoOperations) {
        Assert.notNull(mongoOperations, "A Mongo instance is required");
        Assert.hasText(collection, "A collection name is required");

        this.collection = collection;
        this.mongoOperations = mongoOperations;
        this.setTemplate(mongoOperations);
        this.createIndexes();
    }

    private void createIndexes() {
        // TODO: any indexes to add? if we are just going to look for id, then this method and the collection
        // field are not needed
    }
}
