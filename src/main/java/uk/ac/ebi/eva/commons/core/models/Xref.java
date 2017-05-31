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

/**
 * Base implementation of Xref model class.
 */
public class Xref implements IXref {

    private String id;

    private String src;

    public Xref(IXref xref) {
        this.id = xref.getId();
        this.src = xref.getSrc();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSrc() {
        return src;
    }
}