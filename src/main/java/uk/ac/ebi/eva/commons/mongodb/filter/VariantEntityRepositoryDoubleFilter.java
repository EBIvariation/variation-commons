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

public abstract class VariantEntityRepositoryDoubleFilter extends VariantEntityRepositoryFilter<Double> {

    public VariantEntityRepositoryDoubleFilter(String field, String inputValue) {
        super(field, getValueFromRelation(inputValue), getRelationalOperatorFromRelation(inputValue));
    }

    protected static Double getValueFromRelation(String relation) {
        return Double.parseDouble(relation.replaceAll("[^\\d.]", ""));
    }

    protected static RelationalOperator getRelationalOperatorFromRelation(String relation) {
        String relationalOperatorString = relation.replaceAll("[^<>=]", "");

        switch (relationalOperatorString) {
            case "=":
                return RelationalOperator.EQ;
            case ">":
                return RelationalOperator.GT;
            case "<":
                return RelationalOperator.LT;
            case ">=":
                return RelationalOperator.GTE;
            case "<=":
                return RelationalOperator.LTE;
            default:
                throw new IllegalArgumentException();
        }

    }

}
