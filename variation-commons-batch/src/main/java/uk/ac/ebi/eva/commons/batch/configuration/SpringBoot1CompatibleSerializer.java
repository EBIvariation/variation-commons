package uk.ac.ebi.eva.commons.batch.configuration;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.batch.core.repository.dao.XStreamExecutionContextStringSerializer;


/**
 * ExecutionContext serialization/deserialization from Job repository changed in mechanism
 * from XStream to Jackson in Spring Boot 2 (which includes the Spring Batch 4 library)
 * - see https://docs.spring.io/spring-batch/docs/4.0.4.RELEASE/reference/html/whatsnew.html#whatsNewSerialization
 *
 * This class enables reading ExecutionContext entries written by Spring Boot 1.5 (Spring Batch 3)
 * as well as Spring Boot 2.1 (Spring Batch 4) and write entries in Spring Boot 2 (Spring Batch 4) format
 *
 * Adapted from https://stackoverflow.com/a/55375553
 */
public class SpringBoot1CompatibleSerializer implements ExecutionContextSerializer {
    private final XStreamExecutionContextStringSerializer xStream = new XStreamExecutionContextStringSerializer();
    private final Jackson2ExecutionContextStringSerializer jackson = new Jackson2ExecutionContextStringSerializer();

    public SpringBoot1CompatibleSerializer() throws Exception {
        xStream.afterPropertiesSet();
    }

    @Override
    public Map<String, Object> deserialize(InputStream inputStream) throws IOException {
        InputStream repeatableInputStream = ensureMarkSupported(inputStream);
        repeatableInputStream.mark(Integer.MAX_VALUE);

        try {
            return jackson.deserialize(repeatableInputStream);
        } catch (JsonProcessingException e) {
            repeatableInputStream.reset();
            return xStream.deserialize(repeatableInputStream);
        }
    }

    private static InputStream ensureMarkSupported(InputStream in) {
        return in.markSupported() ? in : new BufferedInputStream(in);
    }

    @Override
    public void serialize(Map<String, Object> object, OutputStream outputStream) throws IOException {
        jackson.serialize(object, outputStream);
    }
}
