package embl.ebi.variation.commons.models.metadata.database;

import embl.ebi.variation.commons.models.metadata.DatabaseTestConfiguration;
import embl.ebi.variation.commons.models.metadata.Publication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by parce on 20/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = DatabaseTestConfiguration.class)
public class PublicationDatabaseTest {

    @Autowired
    PublicationRepository repository;

    Publication publication1, publication2;

    @Before
    public void setUp() {
        publication1 = new Publication("Publication 1", "Journal 1", "111", Arrays.asList("Author 1", "Author 2"));
        publication2 = new Publication("Publication 2", "Journal 2", "111", Arrays.asList("Author 1", "Author 2"));
    }

    /**
     * After saving a file in the database, when only the mandatory fields are
     * set, their values must be the same before and after, and optional fields
     * must be empty.
     */
    @Test
    public void testSaveMandatoryFields() {
        Publication savedPublication = repository.save(publication1);

        assertNotNull(savedPublication.getId());
        assertEquals(publication1.getTitle(), savedPublication.getTitle());
        assertEquals(publication1.getJournal(), savedPublication.getJournal());
        assertEquals(publication1.getVolume(), savedPublication.getVolume());
        assertThat(publication1.getAuthors(), contains(savedPublication.getAuthors().toArray()));
    }
}
