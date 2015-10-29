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

package embl.ebi.variation.commons.models.metadata;

import embl.ebi.variation.commons.models.metadata.database.StudyRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by tom on 29/10/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = DatabaseTestConfiguration.class)
public class StudyRelationTest {

    @Autowired
    StudyRepository repository;

    Study parentStudy1, parentStudy2;
    Study childStudy1, childStudy2, childStudy3, childStudy4, childStudy5, childStudy6;

    @Before
    public void setUp(){
        parentStudy1 = new Study("P Study 1", "P1", "Parent study number 1", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        parentStudy2 = new Study("P Study 2", "P2", "Parent study number 2", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);

        childStudy1 = new Study("C Study 1", "C1", "Child study number 1", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy2 = new Study("C Study 2", "C2", "Child study number 2", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy3 = new Study("C Study 3", "C3", "Child study number 3", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy4 = new Study("C Study 4", "C4", "Child study number 4", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy5 = new Study("C Study 5", "C5", "Child study number 5", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
        childStudy6 = new Study("C Study 6", "C6", "Child study number 6", Study.Material.DNA, Study.Scope.MULTI_ISOLATE);
    }

    @Test
    public void testParentToChild(){
        // Check parent study has no child studies
        checkChildStudiesInParent(parentStudy1, new HashSet<Study>());

        Set<Study> childStudies = new HashSet<>();
        childStudies.add(childStudy1);
        parentStudy1.setChildStudies(childStudies);
        checkChildStudiesInParent(parentStudy1, childStudies);

        Study savedParentStudy1 = repository.save(parentStudy1);
        checkChildStudiesInParent(savedParentStudy1, childStudies);
    }

    @Test
    public void testChildToParent(){
        Set<Study> childStudies = new HashSet<>();
        childStudies.add(childStudy1);
        childStudies.add(childStudy2);
        childStudies.add(childStudy3);
        childStudies.add(childStudy4);
        childStudies.add(childStudy5);
        childStudies.add(childStudy6);

        checkParentStudiesInChildren(null, childStudies);

        parentStudy1.setChildStudies(childStudies);
        checkParentStudiesInChildren(parentStudy1, childStudies);

        Iterable<Study> savedChildStudies = repository.save(childStudies);
        checkParentStudiesInChildren(parentStudy1, savedChildStudies);
    }

    private void checkChildStudiesInParent(Study parentStudy, Set<Study> childStudies){
        assertEquals(parentStudy.getChildStudies(), childStudies);
    }

    private void checkParentStudiesInChildren(Study parentStudy, Iterable<Study> childStudies){
        for(Study childStudy: childStudies){
            assertEquals(childStudy.getParentStudy(), parentStudy);
        }
    }

}
