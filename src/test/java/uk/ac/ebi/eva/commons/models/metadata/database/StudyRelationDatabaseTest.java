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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.eva.commons.models.metadata.DatabaseTestConfiguration;
import uk.ac.ebi.eva.commons.models.metadata.Study;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by tom on 29/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = DatabaseTestConfiguration.class)
public class StudyRelationDatabaseTest {

    @Autowired
    StudyRepository repository;

    Study parentStudy1, parentStudy2;
    Study childStudy1, childStudy2, childStudy3, childStudy4, childStudy5, childStudy6;

    @Before
    public void setUp() {
        parentStudy1 = new Study("P Study 1", "P1", "Parent study number 1", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        parentStudy2 = new Study("P Study 2", "P2", "Parent study number 2", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);

        childStudy1 = new Study("C Study 1", "C1", "Child study number 1", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy2 = new Study("C Study 2", "C2", "Child study number 2", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy3 = new Study("C Study 3", "C3", "Child study number 3", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy4 = new Study("C Study 4", "C4", "Child study number 4", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy5 = new Study("C Study 5", "C5", "Child study number 5", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy6 = new Study("C Study 6", "C6", "Child study number 6", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
    }

    /**
     * Check parent study has no child studies.
     */
    @Test
    public void testNoChildren() {
        Set<Study> emptyChildren = new HashSet<>();
        checkChildStudiesInParent(parentStudy1, emptyChildren);
        checkParentStudiesInChildren(parentStudy1, emptyChildren);
    }

    @Test
    public void testSingleChild() {
        Set<Study> singleChildStudy = new HashSet<>();
        singleChildStudy.add(childStudy1);
        parentStudy1.setChildStudies(singleChildStudy);
        checkChildStudiesInParent(parentStudy1, singleChildStudy);
        checkParentStudiesInChildren(parentStudy1, singleChildStudy);

        Study savedParentStudy1 = repository.save(parentStudy1);
        checkChildStudiesInParent(savedParentStudy1, singleChildStudy);
        checkParentStudiesInChildren(savedParentStudy1, singleChildStudy);
        assertEquals(2, repository.count());
    }

    @Test
    public void testMultipleChildren() {
        Set<Study> childStudies = new HashSet<>();
        childStudies.add(childStudy1);
        childStudies.add(childStudy2);
        childStudies.add(childStudy3);
        childStudies.add(childStudy4);
        childStudies.add(childStudy5);
        childStudies.add(childStudy6);

        checkParentStudiesInChildren(null, childStudies);

        parentStudy1.setChildStudies(childStudies);
        checkChildStudiesInParent(parentStudy1, childStudies);
        checkParentStudiesInChildren(parentStudy1, childStudies);

        Iterable<Study> savedChildStudies = repository.save(childStudies);
        checkChildStudiesInParent(parentStudy1, new HashSet<>((Collection<Study>) savedChildStudies));
        checkParentStudiesInChildren(parentStudy1, savedChildStudies);

        assertEquals(7, repository.count());
    }

    @Test
    public void testMultilevelStudyHierarchy() {
        Set<Study> children = new HashSet<>();
        children.add(childStudy1);
        children.add(childStudy2);

        Set<Study> grandchildren1 = new HashSet<>();
        grandchildren1.add(childStudy3);
        grandchildren1.add(childStudy4);

        Set<Study> grandchildren2 = new HashSet<>();
        grandchildren2.add(childStudy5);
        grandchildren2.add(childStudy6);

        checkParentStudiesInChildren(null, children);
        checkParentStudiesInChildren(null, grandchildren1);
        checkParentStudiesInChildren(null, grandchildren2);

        parentStudy1.setChildStudies(children);
        childStudy1.setChildStudies(grandchildren1);
        childStudy2.setChildStudies(grandchildren2);

        checkChildStudiesInParent(parentStudy1, children);
        checkParentStudiesInChildren(parentStudy1, children);

        checkChildStudiesInParent(childStudy1, grandchildren1);
        checkParentStudiesInChildren(childStudy1, grandchildren1);

        checkChildStudiesInParent(childStudy2, grandchildren2);
        checkParentStudiesInChildren(childStudy2, grandchildren2);


        Study savedParentStudy = repository.save(parentStudy1);
        checkChildStudiesInParent(savedParentStudy, children);
        checkParentStudiesInChildren(savedParentStudy, children);

        assertEquals(7, repository.count());
    }


    private void checkChildStudiesInParent(Study parentStudy, Set<Study> childStudies) {
        assertEquals(parentStudy.getChildStudies(), childStudies);
    }

    private void checkParentStudiesInChildren(Study parentStudy, Iterable<Study> childStudies) {
        for (Study childStudy : childStudies) {
            assertEquals(childStudy.getParentStudy(), parentStudy);
        }
    }
}
