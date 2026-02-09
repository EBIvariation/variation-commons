/*
 * Copyright 2024 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.BsonBinarySubType;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for loading test data from JSON files into MongoDB collections.
 * This replaces the nosqlunit @UsingDataSet annotation functionality.
 */
public class TestDataLoader {

    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public TestDataLoader(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Load test data from a JSON file into the appropriate MongoDB collections.
     * The JSON file should have collection names as keys and arrays of documents as values.
     *
     * @param jsonPath the classpath location of the JSON file (e.g., "/test-data/variants.json")
     * @throws IOException if the file cannot be read or parsed
     */
    public void load(String jsonPath) throws IOException {
        ClassPathResource resource = new ClassPathResource(jsonPath.startsWith("/") ? jsonPath.substring(1) : jsonPath);
        try (InputStream inputStream = resource.getInputStream()) {
            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(
                    inputStream,
                    new TypeReference<Map<String, List<Map<String, Object>>>>() {}
            );

            for (Map.Entry<String, List<Map<String, Object>>> entry : data.entrySet()) {
                String collectionName = entry.getKey();
                List<Map<String, Object>> documents = entry.getValue();

                for (Map<String, Object> doc : documents) {
                    Map<String, Object> convertedDoc = convertExtendedJson(doc);
                    Document document = new Document(convertedDoc);
                    mongoTemplate.insert(document, collectionName);
                }
            }
        }
    }

    /**
     * Recursively convert MongoDB extended JSON format to native BSON types.
     * Handles $oid, $date, $numberLong, $numberInt, $numberDouble, $numberDecimal
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> convertExtendedJson(Map<String, Object> map) {
        Map<String, Object> result = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            result.put(key, convertValue(value));
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private Object convertValue(Object value) {
        if (value instanceof Map) {
            Map<String, Object> mapValue = (Map<String, Object>) value;

            // Check for extended JSON types
            if (mapValue.size() == 2 && mapValue.containsKey("$binary") && mapValue.containsKey("$type")) {
                // Handle binary data: {"$binary": "base64data", "$type": "00"}
                String base64Data = (String) mapValue.get("$binary");
                String typeStr = (String) mapValue.get("$type");
                byte[] data = Base64.getDecoder().decode(base64Data);
                byte subType = (byte) Integer.parseInt(typeStr, 16);
                return new Binary(subType, data);
            }
            if (mapValue.size() == 1) {
                if (mapValue.containsKey("$oid")) {
                    return new ObjectId((String) mapValue.get("$oid"));
                }
                if (mapValue.containsKey("$date")) {
                    Object dateValue = mapValue.get("$date");
                    if (dateValue instanceof Map) {
                        Map<String, Object> dateMap = (Map<String, Object>) dateValue;
                        if (dateMap.containsKey("$numberLong")) {
                            return new Date(Long.parseLong((String) dateMap.get("$numberLong")));
                        }
                    } else if (dateValue instanceof Long) {
                        return new Date((Long) dateValue);
                    } else if (dateValue instanceof String) {
                        String dateStr = (String) dateValue;
                        // Try parsing as ISO 8601 date string first
                        try {
                            return Date.from(Instant.parse(dateStr));
                        } catch (DateTimeParseException e) {
                            // Fall back to parsing as numeric timestamp
                            return new Date(Long.parseLong(dateStr));
                        }
                    }
                }
                if (mapValue.containsKey("$numberLong")) {
                    return Long.parseLong((String) mapValue.get("$numberLong"));
                }
                if (mapValue.containsKey("$numberInt")) {
                    return Integer.parseInt((String) mapValue.get("$numberInt"));
                }
                if (mapValue.containsKey("$numberDouble")) {
                    return Double.parseDouble((String) mapValue.get("$numberDouble"));
                }
            }

            // Not an extended JSON type, recursively convert
            return convertExtendedJson(mapValue);
        } else if (value instanceof List) {
            List<Object> listValue = (List<Object>) value;
            List<Object> convertedList = new ArrayList<>();
            for (Object item : listValue) {
                convertedList.add(convertValue(item));
            }
            return convertedList;
        }

        return value;
    }

    /**
     * Load multiple JSON files.
     *
     * @param jsonPaths array of classpath locations of JSON files
     * @throws IOException if any file cannot be read or parsed
     */
    public void load(String... jsonPaths) throws IOException {
        for (String path : jsonPaths) {
            load(path);
        }
    }

    /**
     * Drop a specific collection.
     *
     * @param collectionName the name of the collection to drop
     */
    public void dropCollection(String collectionName) {
        mongoTemplate.dropCollection(collectionName);
    }

    /**
     * Drop multiple collections.
     *
     * @param collectionNames the names of collections to drop
     */
    public void dropCollections(String... collectionNames) {
        for (String name : collectionNames) {
            dropCollection(name);
        }
    }

    /**
     * Clean up all known test collections.
     */
    public void cleanupTestCollections() {
        dropCollections(
                "variants_1_2",
                "files",
                "annotation_metadata",
                "annotations",
                "features",
                "samples"
        );
    }
}
