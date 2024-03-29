/*
 * European Variation Archive (EVA) - Open-access database of all types of genetic
 * variation data from all species
 *
 * Copyright 2017 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.ebi.eva.commons.mongodb.filter;

import uk.ac.ebi.eva.commons.core.models.VariantType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for building filters for querying using the VariantRepository
 */
public class FilterBuilder {

    private List<VariantRepositoryFilter> filters = new ArrayList<>();

    public List<VariantRepositoryFilter> getVariantEntityRepositoryFilters(String maf,
                                                                           String polyphenScore,
                                                                           String siftScore,
                                                                           List<String> studies,
                                                                           List<String> consequenceType) {
        return this.withMaf(maf)
                   .withPolyphenScore(polyphenScore)
                   .withSiftScore(siftScore)
                   .withStudies(studies)
                   .withConsequenceType(consequenceType)
                   .build();
    }

    public List<VariantRepositoryFilter> getBeaconFilters(String referenceBases, String alternateBases,
                                                          VariantType variantType, List<String> studies) {
        return this.withReferenceBases(referenceBases)
                   .withAlternates(alternateBases)
                   .withVariantTypes(variantType)
                   .withStudies(studies).build();
    }

    public List<VariantRepositoryFilter> build() {
        return filters;
    }

    public FilterBuilder withMaf(String maf) {
        if (maf != null && !maf.isEmpty()) {
            filters.add(new VariantRepositoryMafFilter(maf));
        }
        return this;
    }

    public FilterBuilder withPolyphenScore(String polyphenScore) {
        if (polyphenScore != null && !polyphenScore.isEmpty()) {
            filters.add(new VariantRepositoryPolyphenFilter(polyphenScore));
        }
        return this;
    }

    public FilterBuilder withSiftScore(String siftScore) {
        if (siftScore != null && !siftScore.isEmpty()) {
            filters.add(new VariantRepositorySiftFilter(siftScore));
        }
        return this;
    }

    public FilterBuilder withStudies(List<String> studies) {
        if (studies != null && !studies.isEmpty()) {
            filters.add(new VariantRepositoryStudyFilter(studies));
        }
        return this;
    }

    public FilterBuilder withConsequenceType(List<String> consequenceType) {
        if (consequenceType != null && !consequenceType.isEmpty()) {
            filters.add(new VariantRepositoryConsequenceTypeFilter(consequenceType));
        }
        return this;
    }

    public FilterBuilder withFiles(List<String> files) {
        if (files != null && !files.isEmpty()) {
            filters.add(new VariantRepositoryFileFilter(files));
        }
        return this;
    }

    public FilterBuilder withVariantTypes(List<VariantType> types) {
        if (types != null && !types.isEmpty()) {
            filters.add(new VariantRepositoryTypeFilter(types));
        }
        return this;
    }

    public FilterBuilder withVariantTypes(VariantType variantType) {
        if (variantType != null) {
            filters.add(new VariantRepositoryTypeFilter(Collections.singletonList(variantType)));
        }
        return this;
    }

    public FilterBuilder withAlternates(List<String> alternates) {
        if (alternates != null && !alternates.isEmpty()) {
            List<String> alternatesUppercase = alternates.stream().map(a->a.toUpperCase()).collect(Collectors.toList());
            filters.add(new VariantRepositoryAlternateFilter(alternatesUppercase));
        }
        return this;
    }

    public FilterBuilder withAlternates(String alternate) {
        if (alternate != null) {
            filters.add(new VariantRepositoryAlternateFilter(Collections.singletonList(alternate.toUpperCase())));
        }
        return this;
    }

    public FilterBuilder withReferenceBases(String referenceBases) {
        if (referenceBases != null) {
            filters.add(new VariantRepositoryReferenceBasesFilter(Collections.singletonList(referenceBases.toUpperCase())));
        }
        return this;
    }

}
