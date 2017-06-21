/*
 * Copyright 2015 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this analysis except in compliance with the License.
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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.eva.commons.models.metadata.Analysis;
import uk.ac.ebi.eva.commons.models.metadata.DatabaseTestConfiguration;
import uk.ac.ebi.eva.commons.models.metadata.File;
import uk.ac.ebi.eva.commons.models.metadata.FileGenerator;

import java.util.Date;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = DatabaseTestConfiguration.class)
public class AnalysisDatabaseTest {

    @Autowired
    AnalysisRepository repository;
    @Autowired
    FileRepository fileRepository;
    @Autowired
    FileGeneratorRepository fileGeneratorRepository;

    Analysis analysis1, analysis2, analysis3;

    @Before
    public void setUp() {
        analysis1 = new Analysis("analysis1", "Analysis 1 title", "Analysis 1 description");
        analysis2 = new Analysis("analysis2", "Analysis 2 title", "Analysis 2 description");

        analysis3 = new Analysis("analysis3", "Analysis 3 title", "Analysis 3 description", "EMBL-EBI", "Illumina", "EVA", true, new Date());
    }

    /**
     * After saving an analysis in the database, when only the mandatory fields
     * are set, their values must be the same before and after, and optional
     * fields must be empty.
     */
    @Test
    public void testSaveMandatoryFields() {
        Analysis savedAnalysis = repository.save(analysis1);

        assertNotNull(savedAnalysis.getId());
        assertEquals(analysis1.getAlias(), savedAnalysis.getAlias());
        assertEquals(analysis1.getTitle(), savedAnalysis.getTitle());
        assertEquals(analysis1.getDescription(), savedAnalysis.getDescription());
        assertNull(savedAnalysis.getCentre());
        assertNull(savedAnalysis.getPlatform());
        assertNull(savedAnalysis.getSoftware());
        assertFalse(savedAnalysis.isImputation());
        assertNull(savedAnalysis.getDate());
        assertNull(savedAnalysis.getStudy());
        assertNull(savedAnalysis.getDataset());
        assertThat(savedAnalysis.getFiles(), empty());
    }

    /**
     * After saving an analysis in the database, when all the fields (excluding
     * relationships) are set, their values must be the same before and after,
     * and relationship optional fields must be empty.
     */
    @Test
    public void testSaveNonMandatoryFields() {
        Analysis savedAnalysis = repository.save(analysis3);

        assertNotNull(savedAnalysis.getId());
        assertEquals(analysis3.getAlias(), savedAnalysis.getAlias());
        assertEquals(analysis3.getTitle(), savedAnalysis.getTitle());
        assertEquals(analysis3.getDescription(), savedAnalysis.getDescription());
        assertEquals(analysis3.getCentre(), savedAnalysis.getCentre());
        assertEquals(analysis3.getPlatform(), savedAnalysis.getPlatform());
        assertEquals(analysis3.getSoftware(), savedAnalysis.getSoftware());
        assertTrue(savedAnalysis.isImputation());
        assertNotNull(savedAnalysis.getDate());
        assertNull(savedAnalysis.getStudy());
        assertNull(savedAnalysis.getDataset());
        assertThat(savedAnalysis.getFiles(), empty());
    }

    /**
     * Two different analysis are both inserted into the database.
     */
    @Test
    public void testNonDuplicated() {
        repository.save(analysis1);
        repository.save(analysis2);
        assertEquals(2, repository.count());
        assertEquals(2, fileGeneratorRepository.count());
    }

    /**
     * Inserting the same analysis twice creates only one entry in the database.
     */
    @Test
    public void testDuplicated() {
        Analysis savedAnalysis1 = repository.save(analysis1);
        Analysis savedAnalysis2 = repository.save(analysis1);
        assertEquals(1, repository.count());
        assertEquals(1, fileGeneratorRepository.count());
        assertEquals(savedAnalysis1, savedAnalysis2);
    }

    /**
     * A analysis before and after insertion must be considered equal. Two
     * different analysis in the database can't be equal.
     */
    @Test
    public void testEquals() {
        // Compare saved and unsaved entities
        Analysis savedAnalysis1 = repository.save(analysis1);
        Analysis detachedAnalysis1 = new Analysis("analysis1", "Analysis 1 title", "Analysis 1 description");
        assertEquals(detachedAnalysis1, savedAnalysis1);

        Analysis savedAnalysis2 = repository.save(analysis2);
        Analysis detachedAnalysis2 = new Analysis("analysis2", "Analysis 2 title", "Analysis 2 description");
        // Compare two saved entities
        assertEquals(detachedAnalysis2, savedAnalysis2);
        assertNotEquals(analysis1, savedAnalysis2);
        assertNotEquals(savedAnalysis1, savedAnalysis2);
    }

    /**
     * The hash code before and after insertion must be the same.
     */
    @Test
    public void testHashCode() {
        Analysis savedAnalysis1 = repository.save(analysis1);
        assertEquals(analysis1.hashCode(), savedAnalysis1.hashCode());
    }

    /**
     * Deleting one analysis leaves the other still in the database.
     */
    @Test
    public void testDelete() {
        repository.save(analysis1);
        repository.save(analysis2);
        assertEquals(2, repository.count());
        assertEquals(2, fileGeneratorRepository.count());
        repository.delete(analysis1);
        assertEquals(1, repository.count());
        assertEquals(1, fileGeneratorRepository.count());
        assertEquals(analysis2, repository.findAll().iterator().next());
    }

    /**
     * Updating a analysis assigning the unique key from other must fail when
     * serialising
     *
     * @todo How to report this kind of errors?
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDuplicate() {
        Analysis savedAnalysis1 = repository.save(analysis1);
        Analysis savedAnalysis2 = repository.save(analysis2);

        savedAnalysis1.setAlias("analysis2");
        repository.save(savedAnalysis1);
        repository.findAll();
    }

    /**
     * If we add some files to an analysis, and save the analysis into the database, the files also will be saved
     */
    @Test
    public void testAddManyFilesToOneAnalyis() {
        File vcfFile = new File("file.vcf", File.Type.VCF, "7sd7fsd89fsd7dsf");
        File cramFile = new File("file.cram", File.Type.CRAM, "3sdasasdasd219fsd3845");
        analysis1.addFile(vcfFile);
        analysis1.addFile(cramFile);
        Analysis savedAnalysis1 = repository.save(analysis1);

        // check that analysis has been saved
        assertEquals(1, repository.count());
        assertEquals(analysis1, savedAnalysis1);
        assertEquals(2, savedAnalysis1.getFiles().size());
        assertThat(savedAnalysis1.getFiles(), containsInAnyOrder(vcfFile, cramFile));

        // check that the fileGenerator repository also "contains" the saved analysis
        assertEquals(1, fileGeneratorRepository.count());
        assertEquals(fileGeneratorRepository.findAll().iterator().next(), savedAnalysis1);

        // check that files have been saved
        assertEquals(2, fileRepository.count());
        assertThat(fileRepository.findAll(), containsInAnyOrder(vcfFile, cramFile));
    }

    /**
     * If we add one file to many analysis, and save the analysis into the database,
     * the file also will be saved
     */
    @Test
    public void testAddOneFileToManyAnalyis() {
        File vcfFile = new File("file.vcf", File.Type.VCF, "7sd7fsd89fsd7dsf");
        analysis1.addFile(vcfFile);
        Analysis savedAnalysis1 = repository.save(analysis1);
        analysis2.addFile(vcfFile);
        Analysis savedAnalysis2 = repository.save(analysis2);

        // check that analysis have been saved
        assertEquals(2, repository.count());
        assertEquals(analysis1, savedAnalysis1);
        assertEquals(analysis2, savedAnalysis2);
        assertEquals(1, savedAnalysis1.getFiles().size());
        assertEquals(1, savedAnalysis2.getFiles().size());
        assertThat(savedAnalysis1.getFiles(), contains(vcfFile));
        assertThat(savedAnalysis2.getFiles(), contains(vcfFile));

        // check that the fileGenerator repository also "contains" the saved analysis
        assertEquals(2, fileGeneratorRepository.count());
        FileGenerator[] savedAnalysis = {savedAnalysis1, savedAnalysis2};
        assertThat(fileGeneratorRepository.findAll(), containsInAnyOrder(savedAnalysis));

        // check that file have been saved
        assertEquals(1, fileRepository.count());
        File savedFile = fileRepository.findAll().iterator().next();
        assertEquals(savedFile, vcfFile);
    }

    /**
     * If we add some files to an analysis, and save the analysis into the database, the files also will be saved
     */
    @Test
    public void testAddManyFilesToManyAnalyis() {
        File vcfFile = new File("file.vcf", File.Type.VCF, "7sd7fsd89fsd7dsf");
        File cramFile = new File("file.cram", File.Type.CRAM, "3sdasasdasd219fsd3845");
        analysis1.addFile(vcfFile);
        analysis1.addFile(cramFile);
        Analysis savedAnalysis1 = repository.save(analysis1);
        analysis2.addFile(vcfFile);
        analysis2.addFile(cramFile);
        Analysis savedAnalysis2 = repository.save(analysis2);

        // check that analysis has been saved
        assertEquals(2, repository.count());
        assertEquals(analysis1, savedAnalysis1);
        assertEquals(analysis2, savedAnalysis2);
        assertEquals(2, savedAnalysis1.getFiles().size());
        assertEquals(2, savedAnalysis2.getFiles().size());
        assertThat(savedAnalysis1.getFiles(), containsInAnyOrder(vcfFile, cramFile));
        assertThat(savedAnalysis2.getFiles(), containsInAnyOrder(vcfFile, cramFile));

        // check that the fileGenerator repository also "contains" the saved analysis
        assertEquals(2, fileGeneratorRepository.count());
        FileGenerator[] savedAnalysis = {savedAnalysis1, savedAnalysis2};
        assertThat(fileGeneratorRepository.findAll(), containsInAnyOrder(savedAnalysis));

        // check that files have been saved
        assertEquals(2, fileRepository.count());
        assertThat(fileRepository.findAll(), containsInAnyOrder(vcfFile, cramFile));
    }
}
