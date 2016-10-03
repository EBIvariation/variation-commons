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

package uk.ac.ebi.eva.commons.models.metadata;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Cristina Yenyxe Gonzalez Garcia <cyenyxe@ebi.ac.uk>
 */
public class AnalysisTest {
    
    Study study;
    
    @Before
    public void setUp() {
        study = new Study("Some study", "PRJEB12345", "Study description", Study.Material.UNKNOWN, Study.Scope.UNKNOWN);
    }
    
    @Test
    public void testConstructorSuccessful() {
        Analysis analysis1 = new Analysis("Analysis1", "Analysis1", "Description");
        assertEquals(analysis1.getAlias(), "Analysis1");
        assertEquals(analysis1.getTitle(), "Analysis1");
        assertEquals(analysis1.getDescription(), "Description");
        assertNull(analysis1.getCentre());
        assertNull(analysis1.getPlatform());
        assertNull(analysis1.getSoftware());
        assertFalse(analysis1.isImputation());
        assertNull(analysis1.getDate());
        
        Analysis analysis2 = new Analysis("Analysis2", "Analysis2", "Description", null, null, null, true, null);
        assertEquals(analysis2.getAlias(), "Analysis2");
        assertEquals(analysis2.getTitle(), "Analysis2");
        assertEquals(analysis2.getDescription(), "Description");
        assertNull(analysis2.getCentre());
        assertNull(analysis2.getPlatform());
        assertNull(analysis2.getSoftware());
        assertTrue(analysis2.isImputation());
        assertNull(analysis2.getDate());
        
        Analysis analysis3 = new Analysis("Analysis3", "Analysis3", "Description", "Centre", "Platform", "Software", false, new Date());
        assertEquals(analysis3.getAlias(), "Analysis3");
        assertEquals(analysis3.getTitle(), "Analysis3");
        assertEquals(analysis3.getDescription(), "Description");
        assertEquals(analysis3.getCentre(), "Centre");
        assertEquals(analysis3.getPlatform(), "Platform");
        assertEquals(analysis3.getSoftware(), "Software");
        assertFalse(analysis3.isImputation());
        assertNotNull(analysis3.getDate());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNoAlias() {
        Analysis analysis = new Analysis(null, "Title", "Description", null, null, null, true, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNoTitle() {
        Analysis analysis = new Analysis("Analysis1", null, "Description", null, null, null, true, null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNoDescription() {
        Analysis analysis = new Analysis("Analysis1", "Title", null, null, null, null, true, null);
    }
    
}
