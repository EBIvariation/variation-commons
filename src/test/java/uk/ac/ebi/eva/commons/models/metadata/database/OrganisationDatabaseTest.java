/*
 * Copyright 2015 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this organisation except in compliance with the License.
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
package uk.ac.ebi.eva.commons.models.metadata.database;

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.ebi.eva.commons.models.metadata.DatabaseTestConfiguration;
import uk.ac.ebi.eva.commons.models.metadata.Organisation;

import java.util.Iterator;
import org.springframework.orm.jpa.JpaSystemException;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = DatabaseTestConfiguration.class)
public class OrganisationDatabaseTest {

	@Autowired
	OrganisationRepository repository;

	Organisation organisation1, organisation2;

	@Before
	public void setUp() {
		organisation1 = new Organisation("EMBL-EBI", "Hinxton, Cambridgeshire, UK");
		organisation2 = new Organisation("CIPF", "Valencia, Spain");
	}

	/**
	 * After saving a organisation in the database, when only the mandatory fields are
	 * set, their values must be the same before and after, and optional fields
	 * must be empty.
	 */
	@Test
	public void testSaveMandatoryFields() {
		Organisation savedOrganisation = repository.save(organisation1);

		assertNotNull(savedOrganisation.getId());
		assertEquals(organisation1.getName(), savedOrganisation.getName());
		assertEquals(organisation1.getAddress(), savedOrganisation.getAddress());
		assertNull(savedOrganisation.getEmail());
	}

	/**
	 * Two different organisations are both inserted into the database.
	 */
	@Test
	public void testNonDuplicated() {
		repository.save(organisation1);
		repository.save(organisation2);
		assertEquals(2, repository.count());
	}

	/**
	 * Inserting the same organisation twice creates only one entry in the database.
	 */
	@Test
	public void testDuplicated() {
		Organisation savedOrganisation1 = repository.save(organisation1);
		Organisation savedOrganisation2 = repository.save(organisation1);
		assertEquals(1, repository.count());
		assertEquals(savedOrganisation1, savedOrganisation2);
	}

	/**
	 * A organisation before and after insertion must be considered equal. Two different
	 * organisations in the database can't be equal.
	 */
	@Test
	public void testEquals() {
		// Compare saved and unsaved entities
		Organisation savedOrganisation1 = repository.save(organisation1);
		Organisation detachedOrganisation1 = new Organisation(organisation1.getName(), organisation1.getAddress());
		assertEquals(detachedOrganisation1, savedOrganisation1);

		Organisation savedOrganisation2 = repository.save(organisation2);
		Organisation detachedOrganisation2 = new Organisation(organisation2.getName(), organisation2.getAddress());
		// Compare two saved entities
		assertEquals(detachedOrganisation2, savedOrganisation2);
		assertNotEquals(organisation1, savedOrganisation2);
		assertNotEquals(savedOrganisation1, savedOrganisation2);
	}

	/**
	 * The hash code before and after insertion must be the same.
	 */
	@Test
	public void testHashCode() {
		Organisation savedOrganisation1 = repository.save(organisation1);
		assertEquals(organisation1.hashCode(), savedOrganisation1.hashCode());
	}
        
	/**
	 * Deleting one organisation leaves the other still in the database.
	 */
	@Test
	public void testDelete() {
	repository.save(organisation1);
	repository.save(organisation2);
	assertEquals(2, repository.count());
	repository.delete(organisation1);
	assertEquals(1, repository.count());
		assertEquals(organisation2, repository.findAll().iterator().next());
	}

	/**
	 * Updating a organisation assigning only part of the unique key from other must not fail when serialising
	 */
	@Test
	public void testUpdateNonDuplicate() {
	Organisation savedOrganisation1 = repository.save(organisation1);
	Organisation savedOrganisation2 = repository.save(organisation2);

		savedOrganisation1.setName(organisation2.getName());
		repository.save(savedOrganisation1);

		Iterator<Organisation> iterator = repository.findAll().iterator();

		Organisation retrievedOrganisation1 = iterator.next();
		assertEquals(organisation2.getName(), retrievedOrganisation1.getName());
		assertEquals(organisation1.getAddress(), retrievedOrganisation1.getAddress());

		Organisation retrievedOrganisation2 = iterator.next();
		assertEquals(organisation2.getName(), retrievedOrganisation2.getName());
		assertEquals(organisation2.getAddress(), retrievedOrganisation2.getAddress());
	}
        
	/**
	 * Updating a organisation assigning the unique key from other must fail when serialising
	 * @todo How to report this kind of errors?
	 */
	@Test(expected = JpaSystemException.class)
	public void testUpdateDuplicate() {
	Organisation savedOrganisation1 = repository.save(organisation1);
	Organisation savedOrganisation2 = repository.save(organisation2);

		savedOrganisation1.setName(organisation2.getName());
		savedOrganisation1.setAddress(organisation2.getAddress());
		repository.save(savedOrganisation1);
		repository.findAll();
	}
}
