/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.eva.commons.core.models.VariantStats;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;
import uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantStatsMongo;
import uk.ac.ebi.eva.commons.test.configurations.MongoOperationConfiguration;

@RunWith(SpringRunner.class)
@TestPropertySource({"classpath:test-mongo.properties"})
@ContextConfiguration(classes = {MongoOperationConfiguration.class})
public class VariantStatsConversionTest {

    public static final String FILE_ID = "f1";
    public static final String STUDY_ID = "s1";
    private static final String COHORT_ID = "ALL";
    public static final String MAF_ALLELE = "A";
    public static final String MGF_GENOTYPE = "A/A";
    public static final float MAF = 0.1f;
    public static final float MGF = 0.01f;
    public static final int MISSING_ALLELES = 10;
    public static final int MISSING_GENOTYPES = 5;

    @Autowired
    private MongoOperations mongoOperations;

    @Test
    public void testConvertVariantStatsToMongo() {
        VariantStats stats = new VariantStats(
                null,
                -1,
                null,
                null,
                VariantType.SNV,
                MAF,
                MGF,
                MAF_ALLELE,
                MGF_GENOTYPE,
                MISSING_ALLELES,
                MISSING_GENOTYPES,
                -1,
                -1,
                -1,
                -1,
                -1);

        stats.addGenotype(new Genotype("0/0"), 100);
        stats.addGenotype(new Genotype("0/1"), 50);
        stats.addGenotype(new Genotype("1/1"), 10);
        VariantStatsMongo variantStatsMongo = new VariantStatsMongo(STUDY_ID, FILE_ID, COHORT_ID, stats);
        DBObject converted = (DBObject) mongoOperations.getConverter().convertToMongoType(variantStatsMongo);

        Assert.assertEquals(STUDY_ID, converted.get(VariantStatsMongo.STUDY_ID));
        Assert.assertEquals(FILE_ID, converted.get(VariantStatsMongo.FILE_ID));
        Assert.assertEquals(COHORT_ID, converted.get(VariantStatsMongo.COHORT_ID));
        Assert.assertEquals(MGF, converted.get(VariantStatsMongo.MGF_FIELD));
        Assert.assertEquals(MAF_ALLELE, converted.get(VariantStatsMongo.MAFALLELE_FIELD));
        Assert.assertEquals(MGF_GENOTYPE, converted.get(VariantStatsMongo.MGFGENOTYPE_FIELD));
        Assert.assertEquals(MISSING_ALLELES, converted.get(VariantStatsMongo.MISSALLELE_FIELD));
        Assert.assertEquals(MISSING_GENOTYPES, converted.get(VariantStatsMongo.MISSGENOTYPE_FIELD));
        Assert.assertNotNull(converted.get(VariantStatsMongo.NUMGT_FIELD));
        Assert.assertEquals(3, ((BasicDBObject) converted.get(VariantStatsMongo.NUMGT_FIELD)).size());

    }

    @Test
    public void testConvertMongoToVariantStats() {
        BasicDBObject mongoStats = new BasicDBObject(VariantStatsMongo.MAF_FIELD, 0.1);
        mongoStats.append(VariantStatsMongo.STUDY_ID, STUDY_ID);
        mongoStats.append(VariantStatsMongo.FILE_ID, FILE_ID);
        mongoStats.append(VariantStatsMongo.COHORT_ID, COHORT_ID);
        mongoStats.append(VariantStatsMongo.MGF_FIELD, 0.01);
        mongoStats.append(VariantStatsMongo.MAFALLELE_FIELD, "A");
        mongoStats.append(VariantStatsMongo.MGFGENOTYPE_FIELD, "A/A");
        mongoStats.append(VariantStatsMongo.MISSALLELE_FIELD, 10);
        mongoStats.append(VariantStatsMongo.MISSGENOTYPE_FIELD, 5);

        BasicDBObject genotypes = new BasicDBObject();
        genotypes.append("0/0", 100);
        genotypes.append("0/1", 50);
        genotypes.append("1/1", 10);
        mongoStats.append(VariantStatsMongo.NUMGT_FIELD, genotypes);

        VariantStatsMongo converted = mongoOperations.getConverter().read(VariantStatsMongo.class, mongoStats);

        Assert.assertEquals(STUDY_ID, converted.getStudyId());
        Assert.assertEquals(FILE_ID, converted.getFileId());
        Assert.assertEquals(COHORT_ID, converted.getCohortId());
        Assert.assertEquals(MGF, converted.getMgf(), 0.0f);
        Assert.assertEquals(MAF_ALLELE, converted.getMafAllele());
        Assert.assertEquals(MGF_GENOTYPE, converted.getMgfGenotype());
        Assert.assertEquals(MISSING_ALLELES, converted.getMissingAlleles());
        Assert.assertEquals(MISSING_GENOTYPES, converted.getMissingGenotypes());
        Assert.assertNotNull(converted.getGenotypesCount());
        Assert.assertEquals(3, converted.getGenotypesCount().size());
    }

}
