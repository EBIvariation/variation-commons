package uk.ac.ebi.eva.commons.test.configurations;

import com.mongodb.MongoClient;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.net.UnknownHostException;

@Configuration
@ImportAutoConfiguration(MongoAutoConfiguration.class)
public class MongoOperationsConfiguration {

    private static final String DUMMY_STATIC = "dummy_test";

    @Bean
    public String mongoCollectionsFiles() {
        return "non-existent-files";
    }

    @Bean
    public String mongoCollectionsVariants() {
        return "non-existent-variants";
    }

    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

    @Bean
    public MongoOperations mongoOperations(MongoClient mongoClient, MongoMappingContext mongoMappingContext)
            throws UnknownHostException {
        MongoDbFactory mongoFactory = new SimpleMongoDbFactory(mongoClient, DUMMY_STATIC);

        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoFactory);
        MappingMongoConverter mappingMongoConverter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        mappingMongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        // Customization: replace dots with pound sign
        mappingMongoConverter.setMapKeyDotReplacement("£");

        return new MongoTemplate(mongoFactory, mappingMongoConverter);
    }


}
