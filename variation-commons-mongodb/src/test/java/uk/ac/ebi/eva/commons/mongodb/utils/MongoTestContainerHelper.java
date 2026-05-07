package uk.ac.ebi.eva.commons.mongodb.utils;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MongoTestContainerHelper {

    private static final String MONGO_IMAGE = "mongo:6.0";

    @Container
    @ServiceConnection
    static MongoDBContainer mongo = new MongoDBContainer(MONGO_IMAGE);
}