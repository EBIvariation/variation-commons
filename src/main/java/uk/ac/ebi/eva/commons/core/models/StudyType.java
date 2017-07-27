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
package uk.ac.ebi.eva.commons.core.models;

import java.util.HashMap;
import java.util.Map;

public enum StudyType {

    COLLECTION("Collection"),

    FAMILY("Family"),

    TRIO("Trio"),

    CONTROL("Control Set"),

    CASE("Case Set"),

    CASE_CONTROL("Case-Control"),

    PAIRED("Paired"),

    PAIRED_TUMOR("Tumor vs. Matched-Normal"),

    TIME_SERIES("Time Series"),

    AGGREGATE("Aggregate"),

    SOMATIC("Somatic");

    private final String symbol;
    private static final Map<String, StudyType> stringToEnum = new HashMap();

    StudyType(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return this.symbol;
    }

    public static StudyType fromString(String symbol) {
        return stringToEnum.get(symbol);
    }

    static {
        for (StudyType op : values()) {
            stringToEnum.put(op.toString(), op);
        }
    }
}