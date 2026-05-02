package uk.ac.ebi.eva.commons.mongodb.utils;

import com.mongodb.ConnectionString;
import com.mongodb.ReadPreference;

import java.util.Objects;

public class MongoUtils {
    public static ConnectionString constructMongoConnectionString(String host, Integer port, String databaseName, String userName,
                                                                  String password, String authenticationDatabase,
                                                                  String authenticationMechanism, String readPreference) {
        ReadPreference readPreferenceToUse = Objects.nonNull(readPreference) && !readPreference.isEmpty() ?
                ReadPreference.valueOf(readPreference) : ReadPreference.primary();
        return new MongoConnectionStringBuilder().host(host).port(port).database(databaseName).username(userName)
                .password(password).authenticationDatabase(authenticationDatabase)
                .authenticationMechanism(authenticationMechanism)
                .readPreference(readPreferenceToUse)
                .build();
    }

    public static ConnectionString constructMongoConnectionString(String host, Integer port, String databaseName, String username,
                                                                  String password, String authenticationDatabase) {
        return MongoUtils.constructMongoConnectionString(host, port, databaseName, username, password,
                authenticationDatabase, null, null);
    }
}
