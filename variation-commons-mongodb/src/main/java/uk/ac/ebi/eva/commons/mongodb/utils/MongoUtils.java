package uk.ac.ebi.eva.commons.mongodb.utils;

import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

public class MongoUtils {
    public static MongoClientURI constructMongoClientURI(String host, Integer port, String databaseName, String userName,
                                                         String password, String authenticationDatabase,
                                                         String authenticationMechanism, String readPreference)
            throws UnsupportedEncodingException {
        ReadPreference readPreferenceToUse = Objects.nonNull(readPreference) && !readPreference.isEmpty() ?
                ReadPreference.valueOf(readPreference) : ReadPreference.primary();
        return new MongoClientURIBuilder().host(host).port(port).database(databaseName).username(userName)
                                          .password(password).authenticationDatabase(authenticationDatabase)
                                          .authenticationMechanism(authenticationMechanism)
                                          .readPreference(readPreferenceToUse)
                                          .build();
    }

    public static MongoClientURI constructMongoClientURI(String host, Integer port, String databaseName, String username,
                                                         String password, String authenticationDatabase)
            throws UnsupportedEncodingException {
        return MongoUtils.constructMongoClientURI(host, port, databaseName, username, password,
                                                  authenticationDatabase, null, null);
    }
}
