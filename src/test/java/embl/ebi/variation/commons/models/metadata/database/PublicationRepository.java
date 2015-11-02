package embl.ebi.variation.commons.models.metadata.database;

import embl.ebi.variation.commons.models.metadata.Publication;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by parce on 20/10/15.
 */
public interface PublicationRepository extends CrudRepository<Publication, Long> {
}
