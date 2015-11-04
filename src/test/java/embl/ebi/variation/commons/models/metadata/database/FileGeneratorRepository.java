package embl.ebi.variation.commons.models.metadata.database;

import embl.ebi.variation.commons.models.metadata.FileGenerator;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by parce on 03/11/15.
 */
public interface FileGeneratorRepository extends CrudRepository<FileGenerator, Long> {
}
