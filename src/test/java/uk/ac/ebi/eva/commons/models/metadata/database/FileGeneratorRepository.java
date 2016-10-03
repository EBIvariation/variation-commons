package uk.ac.ebi.eva.commons.models.metadata.database;

import org.springframework.data.repository.CrudRepository;

import uk.ac.ebi.eva.commons.models.metadata.FileGenerator;

/**
 * Created by parce on 03/11/15.
 */
public interface FileGeneratorRepository extends CrudRepository<FileGenerator, Long> {
}
