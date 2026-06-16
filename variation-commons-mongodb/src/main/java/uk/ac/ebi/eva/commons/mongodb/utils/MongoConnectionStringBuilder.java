package uk.ac.ebi.eva.commons.mongodb.utils;

import com.mongodb.ConnectionString;
import com.mongodb.ReadPreference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static java.lang.String.format;

// Adapted from https://github.com/mongodb/mongo-hadoop/blob/20208a027ad8638e56dfcf040773f176d6ee059f/core/src/main/java/com/mongodb/hadoop/util/MongoClientURIBuilder.java#L1
public class MongoConnectionStringBuilder {
    private String host;
    private Integer port;
    private String database;
    private String userName;
    private String password;
    private boolean portProvidedAsPartOfHost = false;
    private final Map<String, String> params = new LinkedHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MongoConnectionStringBuilder.class);

    public MongoConnectionStringBuilder() {
    }

    public MongoConnectionStringBuilder host(final String host) {
        // Use localhost by default if no host is provided.
        String hostToUse = (Objects.nonNull(host) && !host.isEmpty()) ? host : "localhost";
        if (hostToUse.contains(":")) {
            this.portProvidedAsPartOfHost = true;
        }
        this.host = hostToUse;
        return this;
    }

    public MongoConnectionStringBuilder port(final Integer port) {
        if (this.portProvidedAsPartOfHost) {
            logger.warn("Port already provided in parameter 'hosts'. Therefore, ignoring 'port' parameter...");
            return this;
        }
        // Sometimes Spring parameters will encode integer parameters as 0 when left blank
        this.port = (Objects.nonNull(port) && port != 0) ? port : 27017;
        return this;
    }

    public MongoConnectionStringBuilder database(final String database) {
        this.database = database;
        return this;
    }

    public MongoConnectionStringBuilder username(final String userName) {
        if (Objects.nonNull(userName)) {
            this.userName = URLEncoder.encode(userName, StandardCharsets.UTF_8);
        }
        return this;
    }

    public MongoConnectionStringBuilder password(final String password) {
        if (Objects.nonNull(password)) {
            this.password = URLEncoder.encode(password, StandardCharsets.UTF_8);
        }
        return this;
    }

    public MongoConnectionStringBuilder authenticationDatabase(final String authenticationDatabase) {
        if (Objects.nonNull(userName) && !userName.isEmpty() &&
                Objects.nonNull(password) && !password.isEmpty()) {
            return this.param("authSource", authenticationDatabase);
        }
        logger.warn("No username or password provided. Therefore, ignoring 'authentication-database' parameter...");
        return this;
    }

    public MongoConnectionStringBuilder authenticationMechanism(final String authenticationMechanism) {
        if (Objects.nonNull(userName) && !userName.isEmpty() &&
                Objects.nonNull(password) && !password.isEmpty()) {
            return this.param("authMechanism", authenticationMechanism);
        }
        logger.warn("No username or password provided. Therefore, ignoring 'authenticationMechanism' parameter...");
        return this;
    }

    public MongoConnectionStringBuilder readPreference(final ReadPreference readPreference) {
        return this.param("readPreference", readPreference.getName());
    }

    public MongoConnectionStringBuilder param(final String key, final String value) {
        if (Objects.nonNull(value) && !value.isEmpty()) {
            this.params.put(key, value);
        }
        return this;
    }

    public ConnectionString build() {
        StringBuilder uri = new StringBuilder("mongodb://");
        if (Objects.nonNull(userName) && !userName.isEmpty() &&
                Objects.nonNull(password) && !password.isEmpty()) {
            uri.append(format("%s:%s@", userName, password));
        }
        uri.append(host);
        if (!this.portProvidedAsPartOfHost) {
            uri.append(":").append(this.port);
        }
        uri.append("/");
        if (Objects.nonNull(database) && !database.isEmpty()) {
            uri.append(database);
        }
        if (!params.isEmpty()) {
            boolean paramAdded = false;
            for (Entry<String, String> entry : params.entrySet()) {
                uri.append(paramAdded ? "&" : "?");
                paramAdded = true;
                uri.append(format("%s=%s", entry.getKey(), entry.getValue()));
            }
        }
        return new ConnectionString(uri.toString());
    }
}
