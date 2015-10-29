/*
 * Copyright 2015 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package embl.ebi.variation.commons.models.metadata.database;

import embl.ebi.variation.commons.models.metadata.DatabaseTestConfiguration;
import embl.ebi.variation.commons.models.metadata.Study;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = DatabaseTestConfiguration.class)
public class StudyDatabaseTest {

    @Autowired
    StudyRepository repository;

    Study study1, study2;

    @Before
    public void setUp() {
        // Study(String title, String alias, String description, Material material, Scope scope)

        study1 = new Study("study1", "studyA", "A study", Study.Material.OTHER, Study.Scope.OTHER);
        study2 = new Study("study2", "studyA", "A study", Study.Material.OTHER, Study.Scope.OTHER);
    }

    /**
     * After saving a study in the database, when only the mandatory fields are
     * set, their values must be the same before and after, and optional fields
     * must be empty.
     */
    @Test
    public void testSaveMandatoryFields() {
        Study savedStudy = repository.save(study1);

        assertNotNull(savedStudy.getId());
        assertEquals(study1.getTitle(), savedStudy.getTitle());
        assertEquals(study1.getAlias(), savedStudy.getAlias());
        assertEquals(study1.getDescription(), savedStudy.getDescription());
        assertThat(savedStudy.getChildStudies(), empty());
        assertThat(savedStudy.getFileGenerators(), empty());
        assertThat(savedStudy.getPublications(), empty());
        assertThat(savedStudy.getUris(), empty());
        assertEquals(savedStudy.getParentStudy(), null);
        assertEquals(savedStudy.getBroker(), null);
        assertEquals(savedStudy.getCentre(), null);
        assertEquals(savedStudy.getParentStudy(), null);
        assertEquals(savedStudy.getStudyAccession(), null);
        assertEquals(savedStudy.getType(), null);
    }

    /**
     * Two different studies are both inserted into the database.
     */
    @Test
    public void testNonDuplicated() {
        repository.save(study1);
        repository.save(study2);
        assertEquals(2, repository.count());
    }

    /**
     * Inserting the same study twice creates only one entry in the database.
     */
    @Test
    public void testDuplicated() {
        Study savedStudy1 = repository.save(study1);
        Study savedStudy2 = repository.save(study1);
        assertEquals(1, repository.count());
        assertEquals(savedStudy1, savedStudy2);
    }

    /**
     * A study before and after insertion must be considered equal. Two different
     * studies in the database can't be equal.
     */
    @Test
    public void testEquals() {
        // Compare saved and unsaved entities
        Study savedStudy1 = repository.save(study1);
        Study detachedStudy1 = new Study("study1", "studyA", "A study", Study.Material.OTHER, Study.Scope.OTHER);
        assertEquals(detachedStudy1, savedStudy1);

        Study savedStudy2 = repository.save(study2);
        Study detachedStudy2 = new Study("study2", "studyA", "A study", Study.Material.OTHER, Study.Scope.OTHER);
        // Compare two saved entities
        assertEquals(detachedStudy2, savedStudy2);
        assertNotEquals(study1, savedStudy2);
        assertNotEquals(savedStudy1, savedStudy2);
    }

    /**
     * The hash code before and after insertion must be the same.
     */
    @Test
    public void testHashCode() {
        Study savedStudy1 = repository.save(study1);
        assertEquals(study1.hashCode(), savedStudy1.hashCode());
    }

    /**
     * Deleting one study leaves the other still in the database.
     */
    @Test
    public void testDelete() {
        repository.save(study1);
        repository.save(study2);
        assertEquals(2, repository.count());
        repository.delete(study1);
        assertEquals(1, repository.count());
        assertEquals(study2, repository.findAll().iterator().next());
    }

    /**
     * Updating a study assigning the unique key from other must fail when serialising
     * @todo How to report this kind of errors?
     */
    @Test(expected = JpaSystemException.class)
    public void testUpdateDuplicate() {
        Study savedStudy1 = repository.save(study1);
        Study savedStudy2 = repository.save(study2);
        savedStudy1.setTitle(savedStudy2.getTitle());
        repository.save(savedStudy1);
        repository.findAll();
    }
}