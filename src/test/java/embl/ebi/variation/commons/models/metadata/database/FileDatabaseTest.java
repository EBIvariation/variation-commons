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

import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import embl.ebi.variation.commons.models.metadata.DatabaseTestConfiguration;
import embl.ebi.variation.commons.models.metadata.File;
import org.springframework.orm.jpa.JpaSystemException;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = DatabaseTestConfiguration.class)
public class FileDatabaseTest {

	@Autowired
	FileRepository repository;

	File file1, file2;

	@Before
	public void setUp() {
		file1 = new File("file1.vcf", File.Type.VCF, "7s6efgwe78748");
		file2 = new File("file2.vcf", File.Type.VCF, "7s6efgwe78748");
	}

	/**
	 * After saving a file in the database, when only the mandatory fields are
	 * set, their values must be the same before and after, and optional fields
	 * must be empty.
	 */
	@Test
	public void testSaveMandatoryFields() {
		File savedFile = repository.save(file1);

		assertNotNull(savedFile.getId());
		assertEquals(file1.getName(), savedFile.getName());
		assertEquals(file1.getType(), savedFile.getType());
		assertEquals(file1.getMd5(), savedFile.getMd5());
		assertThat(savedFile.getSamples(), empty());
		assertThat(savedFile.getFileGenerators(), empty());
	}

	/**
	 * Two different files are both inserted into the database.
	 */
	@Test
	public void testNonDuplicated() {
		repository.save(file1);
		repository.save(file2);
		assertEquals(2, repository.count());
	}

	/**
	 * Inserting the same file twice creates only one entry in the database.
	 */
	@Test
	public void testDuplicated() {
		File savedFile1 = repository.save(file1);
		File savedFile2 = repository.save(file1);
		assertEquals(1, repository.count());
		assertEquals(savedFile1, savedFile2);
	}

	/**
	 * A file before and after insertion must be considered equal. Two different
	 * files in the database can't be equal.
	 */
	@Test
	public void testEquals() {
		// Compare saved and unsaved entities
		File savedFile1 = repository.save(file1);
		File detachedFile1 = new File("file1.vcf", File.Type.VCF, "7s6efgwe78748");
		assertEquals(detachedFile1, savedFile1);

		File savedFile2 = repository.save(file2);
		File detachedFile2 = new File("file2.vcf", File.Type.VCF, "7s6efgwe78748");
		// Compare two saved entities
		assertEquals(detachedFile2, savedFile2);
		assertNotEquals(file1, savedFile2);
		assertNotEquals(savedFile1, savedFile2);
	}

	/**
	 * The hash code before and after insertion must be the same.
	 */
	@Test
	public void testHashCode() {
		File savedFile1 = repository.save(file1);
		assertEquals(file1.hashCode(), savedFile1.hashCode());
	}
        
        /**
         * Deleting one file leaves the other still in the database.
         */
        @Test
        public void testDelete() {
		repository.save(file1);
		repository.save(file2);
		assertEquals(2, repository.count());
		repository.delete(file1);
		assertEquals(1, repository.count());
                assertEquals(file2, repository.findAll().iterator().next());
        }
        
        /**
         * Updating a file assigning the unique key from other must fail when serialising
         * @todo How to report this kind of errors?
         */
        @Test(expected = JpaSystemException.class)
        public void testUpdateDuplicate() {
		File savedFile1 = repository.save(file1);
		File savedFile2 = repository.save(file2);
                
                savedFile1.setName(savedFile2.getName());
                repository.save(savedFile1);
                repository.findAll();
        }
}
