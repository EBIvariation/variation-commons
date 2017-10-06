/*
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

package uk.ac.ebi.eva.commons.core.models;

public class VariantKeyFields {

    private int start;
    private int end;

    private String reference;
    private String alternate;

    public VariantKeyFields(int start, int end, String reference, String alternate) {
        this.start = start;
        this.end = end;
        this.reference = reference;
        this.alternate = alternate;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getReference() {
        return reference;
    }

    public String getAlternate() {
        return alternate;
    }
}