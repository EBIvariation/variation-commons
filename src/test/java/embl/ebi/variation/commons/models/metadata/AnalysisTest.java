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

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Cristina Yenyxe Gonzalez Garcia <cyenyxe@ebi.ac.uk>
 */
public class AnalysisTest {
    
    @Test
    public void testConstructorSuccessful() {
        Analysis analysis1 = new Analysis("Analysis1", "Analysis1", "Description", null, null, null, true, null);
        assertEquals(analysis1.getAlias(), "Analysis1");
        assertEquals(analysis1.getTitle(), "Analysis1");
        assertEquals(analysis1.getDescription(), "Description");
        assertNull(analysis1.getCentre());
        assertNull(analysis1.getPlatform());
        assertNull(analysis1.getSoftware());
        assertTrue(analysis1.isImputation());
        assertNull(analysis1.getDate());
        
        Analysis analysis2 = new Analysis("Analysis2", "Analysis2", "Description", "Centre", "Platform", "Software", false, new Date());
        assertEquals(analysis2.getAlias(), "Analysis2");
        assertEquals(analysis2.getTitle(), "Analysis2");
        assertEquals(analysis2.getDescription(), "Description");
        assertEquals(analysis2.getCentre(), "Centre");
        assertEquals(analysis2.getPlatform(), "Platform");
        assertEquals(analysis2.getSoftware(), "Software");
        assertFalse(analysis2.isImputation());
        assertNotNull(analysis2.getDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNoAlias() {
        Analysis analysis = new Analysis(null, "Title", "Description", null, null, null, true, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNoTitle() {
        Analysis analysis = new Analysis("Analysis1", null, "Description", null, null, null, true, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNoDescription() {
        Analysis analysis = new Analysis("Analysis1", "Title", null, null, null, null, true, null);
    }
    
}
