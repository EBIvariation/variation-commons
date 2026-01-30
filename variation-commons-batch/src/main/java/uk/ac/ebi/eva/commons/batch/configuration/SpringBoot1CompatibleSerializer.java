package uk.ac.ebi.eva.commons.batch.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;

/**
 * ExecutionContext serializer using Jackson.
 *
 * Note: XStreamExecutionContextStringSerializer has been removed in Spring Batch 5.x.
 * This class now only supports Jackson-based serialization format.
 * Legacy XStream-serialized data will need to be migrated separately if still present.
 */
public class SpringBoot1CompatibleSerializer implements ExecutionContextSerializer {
    private final Jackson2ExecutionContextStringSerializer jackson = new Jackson2ExecutionContextStringSerializer();

    public SpringBoot1CompatibleSerializer() {
    }

    @Override
    public Map<String, Object> deserialize(InputStream inputStream) throws IOException {
        return jackson.deserialize(inputStream);
    }

    @Override
    public void serialize(Map<String, Object> object, OutputStream outputStream) throws IOException {
        jackson.serialize(object, outputStream);
    }
}
