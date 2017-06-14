package uk.ac.ebi.eva.commons.models.metadata.database;

import org.springframework.data.repository.CrudRepository;
import uk.ac.ebi.eva.commons.models.metadata.Publication;

/**
 * Created by parce on 20/10/15.
 */
public interface PublicationRepository extends CrudRepository<Publication, Long> {
}
