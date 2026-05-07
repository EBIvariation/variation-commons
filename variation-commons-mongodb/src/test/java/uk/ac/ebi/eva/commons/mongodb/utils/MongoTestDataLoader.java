package uk.ac.ebi.eva.commons.mongodb.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MongoTestDataLoader {

    private final MongoTemplate mongoTemplate;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public MongoTestDataLoader(MongoTemplate mongoTemplate,
                               ResourceLoader resourceLoader) {
        this(mongoTemplate, resourceLoader, new ObjectMapper());
    }

    public MongoTestDataLoader(MongoTemplate mongoTemplate,
                               ResourceLoader resourceLoader,
                               ObjectMapper objectMapper) {
        this.mongoTemplate = mongoTemplate;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
    }


    public void load(String resourcePath, String collectionName) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + resourcePath);

            try (InputStream is = resource.getInputStream()) {
                JsonNode root = objectMapper.readTree(is);

                JsonNode arrayNode = root.get(collectionName);
                if (arrayNode == null || !arrayNode.isArray()) {
                    throw new IllegalArgumentException("No array found for collection: " + collectionName);
                }

                List<Document> documents = new ArrayList<>();

                for (JsonNode node : arrayNode) {
                    Document doc = Document.parse(node.toString());
                    documents.add(doc);
                }

                // delete existing documents and insert the given documents
                mongoTemplate.getCollection(collectionName).deleteMany(new Document());
                mongoTemplate.getCollection(collectionName).insertMany(documents);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data", e);
        }
    }
}