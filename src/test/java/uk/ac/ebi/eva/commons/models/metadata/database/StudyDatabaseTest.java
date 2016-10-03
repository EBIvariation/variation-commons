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
package uk.ac.ebi.eva.commons.models.metadata.database;

import uk.ac.ebi.eva.commons.models.metadata.Analysis;
import uk.ac.ebi.eva.commons.models.metadata.DatabaseTestConfiguration;
import uk.ac.ebi.eva.commons.models.metadata.FileGenerator;
import uk.ac.ebi.eva.commons.models.metadata.Organisation;
import uk.ac.ebi.eva.commons.models.metadata.Study;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = DatabaseTestConfiguration.class)
public class StudyDatabaseTest {

    @Autowired
    StudyRepository repository;

    @Autowired
    AnalysisRepository analysisRepository;

    @Autowired
    FileGeneratorRepository fileGeneratorRepository;

    @Autowired
    OrganisationRepository organisationRepository;

    Study study1, study2;
    Organisation organisation1, organisation2;

    @Before
    public void setUp() {
        // Study(String title, String alias, String description, Material material, Scope scope)

        study1 = new Study("study1", "studyA", "A study", Study.Material.OTHER, Study.Scope.OTHER);
        study2 = new Study("study2", "studyA", "A study", Study.Material.OTHER, Study.Scope.OTHER);

        organisation1 = new Organisation("Sanger Institute", "Wellcome Genome Campus");
        organisation2 = new Organisation("EBI", "Wellcome Genome Campus");
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
     * Updating a study assigning the unique key from other must fail when serialising.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateDuplicate() {
        Study savedStudy1 = repository.save(study1);
        Study savedStudy2 = repository.save(study2);
        savedStudy1.setTitle(savedStudy2.getTitle());
        repository.save(savedStudy1);
        repository.findAll();
    }

    /**
     * Saving an study with associated analysis, should persist also them
     */
    @Test
    public void addManyAnalysisToOneStudy() {
        FileGenerator analysis1 = new Analysis("An1", "Analysis 1", "This is one analysis");
        FileGenerator analysis2 = new Analysis("An2", "Analysis 2", "This is other analysis");
        study1.addFileGenerator(analysis1);
        study1.addFileGenerator(analysis2);
        Study savedStudy1 = repository.save(study1);

        // check that study has been saved
        assertEquals(1, repository.count());
        assertEquals(study1, savedStudy1);
        assertThat(savedStudy1.getFileGenerators(), containsInAnyOrder(analysis1, analysis2));

        // check that analysis have been saved and that can be retrieved using Analisys and FileGenerator repositories
        assertEquals(2, analysisRepository.count());
        assertThat(analysisRepository.findAll(), containsInAnyOrder(analysis1, analysis2));
        assertEquals(2, fileGeneratorRepository.count());
        assertThat(fileGeneratorRepository.findAll(), containsInAnyOrder(analysis1, analysis2));
    }

    /**
     * One analysis only can be associated to one study
     */
    @Test
    public void addOneAnalisysToManyStudies() {
        FileGenerator analysis1 = new Analysis("An1", "Analysis 1", "This is one analysis");
        study1.addFileGenerator(analysis1);
        Study savedStudy1 = repository.save(study1);
        study2.addFileGenerator(analysis1);
        Study savedStudy2 = repository.save(study2);

        // check that studies have been saved
        assertEquals(2, repository.count());
        assertEquals(study1, savedStudy1);
        assertEquals(study2, savedStudy2);

        // check that just study2 has a file generator, and that file generator is the analysis 1
        Study studyFromRepository1 = repository.findOne(savedStudy1.getId());
        Study studyFromRepository2 = repository.findOne(savedStudy2.getId());
        assertEquals(studyFromRepository1.getFileGenerators().size(), 0);
        assertEquals(studyFromRepository2.getFileGenerators().size(), 1);
        assertEquals(studyFromRepository2.getFileGenerators().iterator().next(), analysis1);

        // check that just one analysis have been saved and that can be retrieved using Analysis
        // and FileGenerator repositories
        assertEquals(1, analysisRepository.count());
        Analysis savedAnalysis = analysisRepository.findAll().iterator().next();
        assertEquals(savedAnalysis, analysis1);
        assertEquals(1, fileGeneratorRepository.count());
        FileGenerator savedFileGenerator = fileGeneratorRepository.findAll().iterator().next();
        assertEquals(savedFileGenerator, analysis1);
    }

    @Test
    public void testDeleteStudyButNotAnalysys() {
        Analysis analysis1 = new Analysis("An1", "Analysis 1", "This is one analysis");
        Analysis analysis2 = new Analysis("An2", "Analysis 2", "This is other analysis");
        study1.addFileGenerator(analysis1);
        study1.addFileGenerator(analysis2);
        Study savedStudy = repository.save(study1);

        // check that study and analysys have been saved
        assertEquals(1, repository.count());
        assertEquals(2, analysisRepository.count());
        assertEquals(2, fileGeneratorRepository.count());

        // delete the study and check that the file generators have not been removed
        repository.delete(savedStudy);
        assertEquals(0, repository.count());
        assertEquals(2, analysisRepository.count());
        assertEquals(2, fileGeneratorRepository.count());
    }

    @Test
    public void testDeleteAnalysisButNotStudy() {
        Analysis analysis1 = new Analysis("An1", "Analysis 1", "This is one analysis");
        Analysis analysis2 = new Analysis("An2", "Analysis 2", "This is other analysis");
        study1.addFileGenerator(analysis1);
        study1.addFileGenerator(analysis2);
        Study savedStudy = repository.save(study1);

        // check that study and analysys have been saved
        assertEquals(1, repository.count());
        assertEquals(2, analysisRepository.count());

        // delete and analysis
        savedStudy.removeFileGenerator(analysis1);
        analysisRepository.delete(analysis1);

        assertEquals(1, analysisRepository.count());
        assertEquals(1, repository.count());
        assertEquals(1, savedStudy.getFileGenerators().size());

        // delete and analysis and check that the study has been updated
        savedStudy.removeFileGenerator(analysis2);
        analysisRepository.delete(analysis2);

        assertEquals(0, analysisRepository.count());
        assertEquals(1, repository.count());
        assertEquals(0, savedStudy.getFileGenerators().size());
    }

    @Test
    public void testDeleteAnalysisButNotStudyUsingFileGeneratorRepository() {
        Analysis analysis1 = new Analysis("An1", "Analysis 1", "This is one analysis");
        Analysis analysis2 = new Analysis("An2", "Analysis 2", "This is other analysis");
        study1.addFileGenerator(analysis1);
        study1.addFileGenerator(analysis2);
        Study savedStudy = repository.save(study1);

        // check that study and analysys have been saved
        assertEquals(1, repository.count());
        assertEquals(2, fileGeneratorRepository.count());

        // delete and analysis
        savedStudy.removeFileGenerator(analysis1);
        fileGeneratorRepository.delete(analysis1);

        assertEquals(1, fileGeneratorRepository.count());
        assertEquals(1, repository.count());
        assertEquals(1, savedStudy.getFileGenerators().size());

        // delete and analysis and check that the study has been updated
        savedStudy.removeFileGenerator(analysis2);
        fileGeneratorRepository.delete(analysis2);

        assertEquals(0, fileGeneratorRepository.count());
        assertEquals(1, repository.count());
        assertEquals(0, savedStudy.getFileGenerators().size());
    }


    @Test
    public void testSetCentreSaveOrganisation() {
        study1.setCentre(organisation1);
        assertEquals(organisation1, study1.getCentre());

        Study savedStudy1 = repository.save(study1);
        assertEquals(savedStudy1.getCentre(), organisation1);
    }

    @Test
    public void testSetBrokerSaveOrganisation() {
        study2.setBroker(organisation2);
        assertEquals(organisation2, study2.getBroker());
        Study savedStudy1 = repository.save(study2);
        assertEquals(savedStudy1.getBroker(), organisation2);

    }
}
