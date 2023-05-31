package uk.ac.ebi.eva.commons.mongodb.utils;

import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static java.lang.String.format;

// Adapted from https://github.com/mongodb/mongo-hadoop/blob/20208a027ad8638e56dfcf040773f176d6ee059f/core/src/main/java/com/mongodb/hadoop/util/MongoClientURIBuilder.java#L1
public class MongoClientURIBuilder {
    private MongoClientOptions options;
    private MongoCredential credentials;
    private final List<String> hosts = new ArrayList<String>();
    private String database;
    private String userName;
    private String password;
    private Map<String, String> params = new LinkedHashMap<String, String>();

    private static Logger logger = LoggerFactory.getLogger(MongoClientURIBuilder.class);

    public MongoClientURIBuilder() {
    }

    public MongoClientURIBuilder(final MongoClientURI mongoClientURI) {
        List<String> list = mongoClientURI.getHosts();
        for (String s : list) {
            host(s);
        }
        database = mongoClientURI.getDatabase();
        userName = mongoClientURI.getUsername();
        if (mongoClientURI.getPassword() != null) {
            password = new String(mongoClientURI.getPassword());
        }
        options = mongoClientURI.getOptions();
        String uri = mongoClientURI.getURI();
        if (uri.contains("?")) {
            String query = uri.substring(uri.indexOf('?') + 1);
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] split = pair.split("=");
                param(split[0], split[1]);
            }
        }
    }

    public MongoClientURIBuilder host(final String host) {
        String hostToUse = (Objects.nonNull(host) && !host.isEmpty()) ? host : "localhost";
        return host(hostToUse, null);
    }

    public MongoClientURIBuilder host(final String newHost, final Integer newPort) {
        hosts.clear();
        addHost(newHost, newPort);

        return this;
    }

    public void addHost(final String newHost, final Integer newPort) {
        if (newHost.contains(":") && newPort == null) {
            hosts.add(newHost);
        } else {
            String host = newHost.isEmpty() ? "localhost" : newHost;
            Integer port = newPort == null ? 27017 : newPort;
            if (host.contains(":")) {
                host = host.substring(0, host.indexOf(':'));
            }
            hosts.add(format("%s:%d", host, port));
        }
    }

    public MongoClientURIBuilder port(final Integer port) {
        if (hosts.size() == 0) {
            host("localhost", port);
        }
        logger.warn("Port already provided in parameter 'hosts'. Therefore, ignoring 'port' parameter...");
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

    public MongoClientURIBuilder options(final MongoClientOptions options) {
        this.options = options;
        return this;
    }

    public MongoClientURIBuilder param(final String key, final String value) {
        if (Objects.nonNull(value) && !value.isEmpty()) {
            this.params.put(key, value);
        }
        return this;
    }

    public MongoClientURI build() {
        StringBuilder uri = new StringBuilder(format("mongodb://"));
        if (Objects.nonNull(userName) && !userName.isEmpty() &&
                Objects.nonNull(password) && !password.isEmpty()) {
            uri.append(format("%s:%s@", userName, password));
        }
        // Use localhost by default if no host is provided.
        if (hosts.isEmpty()) {
            uri.append("localhost");
        } else {
            for (int i = 0; i < hosts.size(); i++) {
                final String host = hosts.get(i);
                if (i != 0) {
                    uri.append(",");
                }
                uri.append(host);
            }
        }
        if (database != null) {
            uri.append(format("/%s", database));

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

    public MongoClientURIBuilder readPreference(final ReadPreference readPreference) {
        return this.param("readPreference", readPreference.getName());
    }
}
