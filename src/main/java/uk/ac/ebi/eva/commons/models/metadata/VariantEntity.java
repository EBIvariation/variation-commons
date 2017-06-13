/*
 * Copyright 2016-2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.models.metadata;

import org.opencb.biodata.models.variant.Variant;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Subclass of org.opencb.biodata.models.variant.Variant.
 * Purpose is to allow the specifying of collection name with @Document annotation.
 */
@Document(collection = "variants_1_2")
public class VariantEntity extends Variant {

    public VariantEntity(Variant variant) {
        setIds(variant.getIds());
        setChromosome(variant.getChromosome());
        setStart(variant.getStart());
        setEnd(variant.getEnd());
        setLength(variant.getLength());
        setAlternate(variant.getAlternate());
        setReference(variant.getReference());
        setType(variant.getType());
        setHgvs(variant.getHgvs());
        setAnnotation(variant.getAnnotation());
        setSourceEntries(variant.getSourceEntries());
    }

    public VariantEntity() {
    }

    public VariantEntity(String chromosome, int start, int end, String reference, String alternate) {
        super(chromosome, start, end, reference, alternate);
    }
}
