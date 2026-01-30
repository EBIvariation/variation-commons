/*
 * Copyright 2023 EMBL - European Bioinformatics Institute
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

package uk.ac.ebi.eva.commons.mongodb.readers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.mongodb.configuration.EvaRepositoriesConfiguration;
import uk.ac.ebi.eva.commons.mongodb.configuration.MongoRepositoryTestConfiguration;
import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@ExtendWith(SpringExtension.class)
@TestPropertySource({"classpath:eva.properties"})
@ContextConfiguration(classes = {MongoRepositoryTestConfiguration.class, EvaRepositoriesConfiguration.class})
public class MongoDbCursorItemReaderTest {

    private static final String TEST_DB = "test-db";

    private static final String REFERENCE_ALLELE = "A";

    @Autowired
    private MongoTemplate mongoTemplate;

    //Required by nosql-unit
    @Autowired
    private ApplicationContext applicationContext;

    private MongoDbCursorItemReader<VariantMongo> reader;

    @BeforeEach
    public void setUp() {
        reader = new MongoDbCursorItemReader<>();
        reader.setMongoTemplate(mongoTemplate);
        reader.setTargetType(VariantMongo.class);
        mongoTemplate.getDb().drop();
        mongoTemplate.insert(new VariantMongo(VariantType.SNV, "chr1", 1L, 1L, 1, "A", "G"));
        mongoTemplate.insert(new VariantMongo(VariantType.SNV, "chr1", 1L, 1L, 1, "A", "T"));
        mongoTemplate.insert(new VariantMongo(VariantType.DEL, "chr1", 2L, 2L, 1, "T", ""));
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.getDb().drop();
        reader.close();
    }

    @Test
    public void basicStringQuery() throws Exception {
        reader.setQuery("{'ref': '" + REFERENCE_ALLELE + "'}");
        reader.open(new ExecutionContext());
        List<VariantMongo> variants = readIntoList();
        assertEquals(2, variants.size());

        for (VariantMongo variant : variants) {
            assertEquals(REFERENCE_ALLELE, variant.getReference());
        }
    }

    @Test
    public void basicQuery() throws Exception {
        reader.setQuery(new Query(where("ref").is(REFERENCE_ALLELE)));
        reader.open(new ExecutionContext());
        List<VariantMongo> variants = readIntoList();
        assertEquals(2, variants.size());

        for (VariantMongo variant : variants) {
            assertEquals(REFERENCE_ALLELE, variant.getReference());
        }
    }

    private List<VariantMongo> readIntoList() throws Exception {
        List<VariantMongo> variants = new ArrayList<>();
        VariantMongo variant;

        while ((variant = reader.read()) != null) {
            variants.add(variant);
        }

        return variants;
    }

}
