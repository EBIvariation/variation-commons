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
package uk.ac.ebi.eva.commons.mongodb.projections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.HgvsMongo;
import uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.VariantAtMongo;

import java.util.Map;
import java.util.Set;

import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.ALTERNATE_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.AT_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.CHROMOSOME_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.END_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.HGVS_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.LENGTH_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.REFERENCE_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.START_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.TYPE_FIELD;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.buildVariantId;
import static uk.ac.ebi.eva.commons.mongodb.entity.VariantMongo.generateAtField;
import static uk.ac.ebi.eva.commons.mongodb.entity.subdocuments.HgvsMongo.createHgvsMongo;

/**
 * Simplified representation of variant to be used when inserting or updating a variant
 */
public class SimplifiedVariant {

    @Id
    private String id;

    @Field(TYPE_FIELD)
    private VariantType variantType;

    @Field(CHROMOSOME_FIELD)
    private String chromosome;

    @Field(START_FIELD)
    private int start;

    @Field(END_FIELD)
    private int end;

    @Field(LENGTH_FIELD)
    private int length;

    @Field(REFERENCE_FIELD)
    private String reference;

    @Field(ALTERNATE_FIELD)
    private String alternate;

    @Field(AT_FIELD)
    private VariantAtMongo at;

    @Field(HGVS_FIELD)
    private Set<HgvsMongo> hgvs;

    public SimplifiedVariant(VariantType variantType, String chromosome, int start, int end, int length,
                             String reference, String alternate, Map<String, Set<String>> hgvs) {
        this.id = buildVariantId(chromosome, start, reference, alternate);
        this.variantType = variantType;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.length = length;
        this.reference = reference;
        this.alternate = alternate;
        this.at = generateAtField(chromosome, start);
        this.hgvs = createHgvsMongo(hgvs);
    }

}
