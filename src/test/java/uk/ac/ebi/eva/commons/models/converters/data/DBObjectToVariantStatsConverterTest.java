package uk.ac.ebi.eva.commons.models.converters.data;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opencb.biodata.models.feature.Genotype;
import org.opencb.biodata.models.variant.Variant;
import org.opencb.biodata.models.variant.stats.VariantStats;

import static org.junit.Assert.assertEquals;

public class DBObjectToVariantStatsConverterTest {
    
    private static BasicDBObject mongoStats;
    private static VariantStats stats;
    
    @BeforeClass
    public static void setUpClass() {
        mongoStats = new BasicDBObject(uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MAF_FIELD, 0.1);
        mongoStats.append(uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MGF_FIELD, 0.01);
        mongoStats.append(uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MAFALLELE_FIELD, "A");
        mongoStats.append(uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MGFGENOTYPE_FIELD, "A/A");
        mongoStats.append(uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MISSALLELE_FIELD, 10);
        mongoStats.append(uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MISSGENOTYPE_FIELD, 5);
        
        BasicDBObject genotypes = new BasicDBObject();
        genotypes.append("0/0", 100);
        genotypes.append("0/1", 50);
        genotypes.append("1/1", 10);
        mongoStats.append(uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.NUMGT_FIELD, genotypes);
        
        stats = new VariantStats(null, -1, null, null, Variant.VariantType.SNV, 0.1f, 0.01f, "A", "A/A", 10, 5, -1, -1, -1, -1, -1);
        stats.addGenotype(new Genotype("0/0"), 100);
        stats.addGenotype(new Genotype("0/1"), 50);
        stats.addGenotype(new Genotype("1/1"), 10);
    }
    
    @Test
    public void testConvertToDataModelType() {
        uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter converter = new uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter();
        VariantStats converted = converter.convertToDataModelType(mongoStats);
        assertEquals(stats, converted);
    }
    
    @Test
    public void testConvertToStorageType() {
        uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter converter = new uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter();
        DBObject converted = converter.convertToStorageType(stats);
        
        assertEquals(stats.getMaf(), (float) converted.get(
                uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MAF_FIELD), 1e-6);
        assertEquals(stats.getMgf(), (float) converted.get(
                uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MGF_FIELD), 1e-6);
        assertEquals(stats.getMafAllele(), converted.get(
                uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MAFALLELE_FIELD));
        assertEquals(stats.getMgfGenotype(), converted.get(
                uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MGFGENOTYPE_FIELD));
        
        assertEquals(stats.getMissingAlleles(), converted.get(
                uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MISSALLELE_FIELD));
        assertEquals(stats.getMissingGenotypes(), converted.get(
                uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.MISSGENOTYPE_FIELD));
        
        assertEquals(100, ((DBObject) converted.get(
                uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.NUMGT_FIELD)).get("0/0"));
        assertEquals(50, ((DBObject) converted.get(
                uk.ac.ebi.eva.commons.models.converters.data.DBObjectToVariantStatsConverter.NUMGT_FIELD)).get("0/1"));
        assertEquals(10, ((DBObject) converted.get(DBObjectToVariantStatsConverter.NUMGT_FIELD)).get("1/1"));
    }
}
