package uk.ac.ebi.eva.commons.mongodb.utils;

import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class MongoUtilsTest {

    // All parameters passed
    @Test
    public void testConstructMongoClientURIAllParams() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("localhost", 27018, "testDB", "username",
                                                                   "password", "authdb", "SCRAM-SHA-1", "primary")
                                          .getConnectionString();
        assertEquals("mongodb://username:password@localhost:27018/testDB?authSource=authdb" +
                             "&authMechanism=SCRAM-SHA-1&readPreference=primary", mongoClientURI);
    }

    // No authentication mechanism or read preference
    @Test
    public void testConstructMongoClientURINoAuthMechanismAndReadPref() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("localhost", 27018, "testDB", "username",
                                                                   "password", "authdb").getConnectionString();
        assertEquals("mongodb://username:password@localhost:27018/testDB" +
                             "?authSource=authdb&readPreference=primary", mongoClientURI);
    }

    // Ensure that multiple hosts are honored
    @Test
    public void testMultipleHosts() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("host1:27017,host2:27018", 27019,
                                                                   "", "", "", "").getConnectionString();
        // Ensure separate port parameter is overridden by the ports provided in the hosts parameter
        assertEquals("mongodb://host1:27017,host2:27018/?readPreference=primary", mongoClientURI);
    }

    // No parameters passed results in sane defaults
    @Test
    public void testNoParams() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("", null, "", "", "", "").getConnectionString();
        assertEquals("mongodb://localhost:27017/?readPreference=primary", mongoClientURI);
    }

    // Authentication params ignored when no username/password is provided
    @Test
    public void testAuthParamsIgnoredWhenNoUserNameAndPassword() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("", null, "", "", "", "authdb", "authmech", "").getConnectionString();
        assertEquals("mongodb://localhost:27017/?readPreference=primary", mongoClientURI);
    }

    @Test
    public void testAuthParamsIgnoredWhenNoUserNameAndPasswordIsNull() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("", null, "", null, null, "authdb", "authmech", "").getConnectionString();
        assertEquals("mongodb://localhost:27017/?readPreference=primary", mongoClientURI);
    }
}
