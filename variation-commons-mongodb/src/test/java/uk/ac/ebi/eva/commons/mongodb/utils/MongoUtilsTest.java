package uk.ac.ebi.eva.commons.mongodb.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class MongoUtilsTest {

    // All parameters passed
    @Test
    public void testConstructMongoClientURIAllParams() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("localhost", 27018, "testDB", "username",
                                                                   "password", "authdb", "SCRAM-SHA-1", "primary")
                                          .getURI();
        assertEquals("mongodb://username:password@localhost:27018/testDB?authSource=authdb" +
                             "&authMechanism=SCRAM-SHA-1&readPreference=primary", mongoClientURI);
    }

    // No authentication mechanism or read preference
    @Test
    public void testConstructMongoClientURINoAuthMechanismAndReadPref() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("localhost", 27018, "testDB", "username",
                                                                   "password", "authdb").getURI();
        assertEquals("mongodb://username:password@localhost:27018/testDB" +
                             "?authSource=authdb&readPreference=primary", mongoClientURI);
    }

    // Ensure that multiple hosts are honored
    @Test
    public void testMultipleHosts() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("host1:27017,host2:27018", 27019,
                                                                   "", "", "", "").getURI();
        // Ensure separate port parameter is overridden by the ports provided in the hosts parameter
        assertEquals("mongodb://host1:27017,host2:27018/?readPreference=primary", mongoClientURI);
    }

    // No parameters passed
    @Test
    public void testNoParams() throws UnsupportedEncodingException {
        String mongoClientURI = MongoUtils.constructMongoClientURI("", null, "", "", "", "").getURI();
        assertEquals("mongodb://localhost:27017/?readPreference=primary", mongoClientURI);
    }
}
