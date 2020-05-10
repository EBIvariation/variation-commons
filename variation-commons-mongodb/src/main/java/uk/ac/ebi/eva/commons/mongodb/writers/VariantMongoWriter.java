/*
 * Copyright 2016 EMBL - European Bioinformatics Institute
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

import com.mongodb.BulkWriteOperation;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.util.Assert;

import uk.ac.ebi.eva.commons.core.models.IVariant;
import uk.ac.ebi.eva.commons.core.models.IVariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.VariantStatistics;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.projections.SimplifiedVariant;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantStatisticsMongo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.ANNOTATION_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.DBSNP_IDS_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.IDS_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo.MAIN_ID_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.AnnotationIndexMongo.SO_ACCESSION_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.AnnotationIndexMongo.XREFS_FIELD;

/**
 * Write a list of {@link IVariant} into MongoDB
 */
public class VariantMongoWriter extends MongoItemWriter<IVariant> {

    private static final Logger logger = LoggerFactory.getLogger(VariantMongoWriter.class);

    public static final String BACKGROUND_INDEX = "background";

    private final MongoOperations mongoOperations;

    private final String collection;

    private final boolean includeStats;

    private final boolean includeSamples;

    public VariantMongoWriter(String collection, MongoOperations mongoOperations, boolean includeStats,
                              boolean includeSamples) {
        Assert.notNull(mongoOperations, "A Mongo instance is required");
        Assert.hasText(collection, "A collection name is required");

        this.mongoOperations = mongoOperations;
        this.collection = collection;
        setTemplate(mongoOperations);
        this.includeStats = includeStats;
        this.includeSamples = includeSamples;

        createIndexes();
    }

    private void createIndexes() {
        IndexOptions indexOptions = new IndexOptions().background(true);
        mongoOperations.getCollection(collection).createIndex(
                new Document(VariantMongo.CHROMOSOME_FIELD, 1)
                        .append(VariantMongo.START_FIELD, 1)
                        .append(VariantMongo.END_FIELD, 1),
                indexOptions);

        mongoOperations.getCollection(collection).createIndex(new Document(VariantMongo.IDS_FIELD, 1), indexOptions);

        String filesStudyIdField = String.format("%s.%s", VariantMongo.FILES_FIELD,
                                                 VariantSourceEntryMongo.STUDYID_FIELD);
        String filesFileIdField = String.format("%s.%s", VariantMongo.FILES_FIELD,
                                                VariantSourceEntryMongo.FILEID_FIELD);
        mongoOperations.getCollection(collection).createIndex(
                new Document(filesStudyIdField, 1).append(filesFileIdField, 1),
                indexOptions);

        mongoOperations.getCollection(collection).createIndex(
                new Document(ANNOTATION_FIELD + "." + XREFS_FIELD, 1),
                indexOptions);
        mongoOperations.getCollection(collection).createIndex(
                new Document(ANNOTATION_FIELD + "." + SO_ACCESSION_FIELD, 1),
                indexOptions);
    }

    @Override
    protected void doWrite(List<? extends IVariant> variants) {
        List<UpdateOneModel<Document>> updates = new ArrayList<>();
        for (IVariant variant : variants) {
            updates.add(new UpdateOneModel<>(generateQuery(variant), generateUpdate(variant),
                                             new UpdateOptions().upsert(true)));
        }
        if (!updates.isEmpty()) {
            mongoOperations.getCollection(collection).bulkWrite(updates);
        }
    }

    private Document generateQuery(IVariant variant) {
        String id = VariantMongo.buildVariantId(variant.getChromosome(), variant.getStart(),
                                                variant.getReference(), variant.getAlternate());

        // the chromosome and start appear just as shard keys, in an unsharded cluster they wouldn't be needed
        return new Document("_id", id)
                .append(VariantMongo.CHROMOSOME_FIELD, variant.getChromosome())
                .append(VariantMongo.START_FIELD, variant.getStart());
    }

    private Document generateUpdate(IVariant variant) {
        Assert.notNull(variant, "Variant should not be null. Please provide a valid Variant object");
        logger.trace("Convert variant {} into mongo object", variant);

        Document update = new Document();

        addOperatorAddToSet(variant, update);
        addOperatorSetOnInsert(variant, update);
        addOperatorSet(variant, update);

        return update;
    }

    private void addOperatorSetOnInsert(IVariant variant, Document update) {
        update.append("$setOnInsert", convertVariant(variant));
    }

    private void addOperatorSet(IVariant variant, Document update) {
        Document overwrite = new Document();
        if (variant.getMainId() != null) {
            overwrite.put(MAIN_ID_FIELD, variant.getMainId());
        }

        if (!overwrite.isEmpty()) {
            update.append("$set", overwrite);
        }
    }

    private void addOperatorAddToSet(IVariant variant, Document update) {
        Document addToSet = new Document();

        if (!variant.getSourceEntries().isEmpty()) {
            IVariantSourceEntry variantSourceEntry = getVariantSourceEntry(variant);

            addToSet.put(VariantMongo.FILES_FIELD, convertSourceEntry(variantSourceEntry));

            if (includeStats) {
                List<Document> statistics = convertStatistics(variantSourceEntry);
                addToSet.put(VariantMongo.STATISTICS_FIELD, new Document("$each", statistics));
            }
        }

        if (!variant.getIds().isEmpty()) {
            addToSet.put(IDS_FIELD, new Document("$each", variant.getIds()));
        }

        if (!variant.getDbsnpIds().isEmpty()) {
            addToSet.put(DBSNP_IDS_FIELD, new Document("$each", variant.getDbsnpIds()));
        }

        if (!addToSet.isEmpty()) {
            update.put("$addToSet", addToSet);
        }
    }

    private IVariantSourceEntry getVariantSourceEntry(IVariant variant) {
        Assert.isTrue(1 == variant.getSourceEntries().size(), "VariantMongoWriter assumes that there's only " +
                "one study being loaded, so there should only be 0 or 1 VariantSourceEntries inside any Variant");
        return variant.getSourceEntries().iterator().next();
    }

    private Document convertSourceEntry(IVariantSourceEntry variantSourceEntry) {
        VariantSourceEntryMongo variantSource;
        if (includeSamples) {
            variantSource = new VariantSourceEntryMongo(
                    variantSourceEntry.getFileId(),
                    variantSourceEntry.getStudyId(),
                    variantSourceEntry.getSecondaryAlternates(),
                    variantSourceEntry.getAttributes(),
                    variantSourceEntry.getFormat(),
                    variantSourceEntry.getSamplesData()
            );
        } else {
            variantSource = new VariantSourceEntryMongo(
                    variantSourceEntry.getFileId(),
                    variantSourceEntry.getStudyId(),
                    variantSourceEntry.getSecondaryAlternates(),
                    variantSourceEntry.getAttributes()
            );
        }
        return (Document) mongoOperations.getConverter().convertToMongoType(variantSource);
    }

    private List<Document> convertStatistics(IVariantSourceEntry variantSourceEntry) {
        List<VariantStatisticsMongo> variantStats = new ArrayList<>();
        for (Map.Entry<String, VariantStatistics> variantStatsEntry : variantSourceEntry.getCohortStats().entrySet()) {
            variantStats.add(new VariantStatisticsMongo(
                    variantSourceEntry.getStudyId(),
                    variantSourceEntry.getFileId(),
                    variantStatsEntry.getKey(),
                    variantStatsEntry.getValue()
            ));
        }
        return (List<Document>) mongoOperations.getConverter().convertToMongoType(variantStats);
    }

    private Document convertVariant(IVariant variant) {
        SimplifiedVariant simplifiedVariant = new SimplifiedVariant(
                variant.getType(),
                variant.getChromosome(),
                variant.getStart(),
                variant.getEnd(),
                variant.getLength(),
                variant.getReference(),
                variant.getAlternate(),
                variant.getHgvs());
        return (Document) mongoOperations.getConverter().convertToMongoType(simplifiedVariant);
    }
}