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
import java.util.Arrays;
import java.util.List;

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

    public List<VariantRepositoryFilter> getBeaconFilters(Long start,Long startMin,Long startMax,Long end,Long endMin,Long endMax,String referenceBases,String alternateBases,VariantType variantType,List<String> studies){
        return this.withStart(start)
                .withStartMin(startMin)
                .withStartMax(startMax)
                .withEnd(end)
                .withEndMin(endMin)
                .withEndMax(endMax)
                .withReferenceBases(referenceBases)
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

    public FilterBuilder withVariantTypes(VariantType variantType){
        if(variantType!=null){
            filters.add(new VariantRepositoryTypeFilter(new ArrayList<>(Arrays.asList(variantType))));
        }
        return this;
    }
    public FilterBuilder withAlternates(List<String> alternates) {
        if (alternates != null && !alternates.isEmpty()) {
            filters.add(new VariantRepositoryAlternateFilter(alternates));
        }
        return this;
    }

    public FilterBuilder withAlternates(String alternate){
        if(alternate!=null){
            filters.add(new VariantRepositoryAlternateFilter(new ArrayList<>(Arrays.asList(alternate))));
        }
        return this;
    }

    public FilterBuilder withReferenceBases(String referenceBases){
        if(referenceBases!=null){
            filters.add(new VariantRepositoryReferenceBasesFilter(new ArrayList<>(Arrays.asList(referenceBases))));
        }
        return this;
    }

    public FilterBuilder withStart(Long start){
        if(start!=null){
            filters.add(new VariantRepositoryStartFilter(start,RelationalOperator.EQ));
        }
        return this;
    }

    public FilterBuilder withStartMin(Long startMin){
        if(startMin!=null){
            filters.add(new VariantRepositoryStartFilter(startMin,RelationalOperator.GTE));
        }
        return this;
    }

    public FilterBuilder withStartMax(Long startMax){
        if(startMax!=null){
            filters.add(new VariantRepositoryStartFilter(startMax,RelationalOperator.LTE));
        }
        return this;
    }

    public FilterBuilder withEnd(Long end){
        if(end!=null){
            filters.add(new VariantRepositoryEndFilter(end,RelationalOperator.EQ));
        }
        return this;
    }

    public FilterBuilder withEndMin(Long endMin){
        if(endMin!=null){
            filters.add(new VariantRepositoryEndFilter(endMin,RelationalOperator.GTE));
        }
        return this;
    }

    public FilterBuilder withEndMax(Long endMax){
        if(endMax!=null){
            filters.add(new VariantRepositoryEndFilter(endMax,RelationalOperator.LTE));
        }
        return this;
    }
}
