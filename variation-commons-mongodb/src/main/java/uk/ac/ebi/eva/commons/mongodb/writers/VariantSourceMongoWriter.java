/*
 * Copyright 2016-2017 EMBL - European Bioinformatics Institute
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

package uk.ac.ebi.eva.commons.mongodb.writers;

import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import uk.ac.ebi.eva.commons.core.models.IVariantSource;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantSourceMongo;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Write a list of {@link IVariantSource} into MongoDB
 */
public class VariantSourceMongoWriter extends MongoItemWriter<IVariantSource> {

    private MongoOperations mongoOperations;

    private String collection;

    public VariantSourceMongoWriter(MongoOperations mongoOperations, String collection) {
        super();
        Assert.notNull(mongoOperations, "A Mongo instance is required");
        Assert.hasText(collection, "A collection name is required");
        setCollection(collection);
        setTemplate(mongoOperations);

        this.mongoOperations = mongoOperations;
        this.collection = collection;

        createIndexes();
    }

    private void createIndexes() {
        IndexOptions indexOptions = new IndexOptions().background(true).unique(true);
        mongoOperations.getCollection(collection).createIndex(
                new Document(VariantSourceMongo.STUDYID_FIELD, 1).append(VariantSourceMongo.FILEID_FIELD, 1)
                                                                 .append(VariantSourceMongo.FILENAME_FIELD, 1),
                indexOptions);
    }

    @Override
    public void write(List<? extends IVariantSource> items) throws Exception {
        List<VariantSourceMongo> convertedList = items.stream()
                .map(VariantSourceMongo::new)
                .collect(Collectors.toList());
        super.write(convertedList);
    }
}
