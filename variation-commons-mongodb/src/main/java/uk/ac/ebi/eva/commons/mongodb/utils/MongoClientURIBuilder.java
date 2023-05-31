package uk.ac.ebi.eva.commons.mongodb.utils;

import com.mongodb.MongoClientURI;
import com.mongodb.ReadPreference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static java.lang.String.format;

// Adapted from https://github.com/mongodb/mongo-hadoop/blob/20208a027ad8638e56dfcf040773f176d6ee059f/core/src/main/java/com/mongodb/hadoop/util/MongoClientURIBuilder.java#L1
public class MongoClientURIBuilder {
    private String host;
    private Integer port;
    private String database;
    private String userName;
    private String password;
    private boolean portProvidedAsPartOfHost = false;
    private final Map<String, String> params = new LinkedHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MongoClientURIBuilder.class);

    public MongoClientURIBuilder() {
    }

    public MongoClientURIBuilder host(final String host) {
        String hostToUse = (Objects.nonNull(host) && !host.isEmpty()) ? host : "localhost";
        if (hostToUse.contains(":")) {
            this.portProvidedAsPartOfHost = true;
        }
        this.host = hostToUse;
        return this;
    }

    public MongoClientURIBuilder port(final Integer port) {
        if (this.portProvidedAsPartOfHost) {
            logger.warn("Port already provided in parameter 'hosts'. Therefore, ignoring 'port' parameter...");
            return this;
        }
        // Sometimes Spring parameters will encode integer parameters as 0 when left blank
        this.port = (Objects.nonNull(port) && port != 0) ? port : 27017;
        return this;
    }

    public MongoClientURIBuilder database(final String database) {
        this.database = database;
        return this;
    }

    public MongoClientURIBuilder username(final String userName) {
        this.userName = userName;
        return this;
    }

    public MongoClientURIBuilder password(final String password) throws UnsupportedEncodingException {
        this.password = URLEncoder.encode(password, StandardCharsets.UTF_8.toString());
        return this;
    }

    public MongoClientURIBuilder authenticationDatabase(final String authenticationDatabase) {
        if (Objects.nonNull(userName) && !userName.isEmpty() &&
                Objects.nonNull(password) && !password.isEmpty()) {
            return this.param("authSource", authenticationDatabase);
        }
        logger.warn("No username or password provided. Therefore, ignoring 'authentication-database' parameter...");
        return this;
    }

    public MongoClientURIBuilder authenticationMechanism(final String authenticationMechanism) {
        if (Objects.nonNull(userName) && !userName.isEmpty() &&
                Objects.nonNull(password) && !password.isEmpty()) {
            return this.param("authMechanism", authenticationMechanism);
        }
        logger.warn("No username or password provided. Therefore, ignoring 'authenticationMechanism' parameter...");
        return this;
    }

    public MongoClientURIBuilder readPreference(final ReadPreference readPreference) {
        return this.param("readPreference", readPreference.getName());
    }

    public MongoClientURIBuilder param(final String key, final String value) {
        if (Objects.nonNull(value) && !value.isEmpty()) {
            this.params.put(key, value);
        }
        return this;
    }

    public MongoClientURI build() {
        StringBuilder uri = new StringBuilder("mongodb://");
        if (Objects.nonNull(userName) && !userName.isEmpty() &&
                Objects.nonNull(password) && !password.isEmpty()) {
            uri.append(format("%s:%s@", userName, password));
        }
        // Use localhost by default if no host is provided.
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
        return new MongoClientURI(uri.toString());
    }
}
