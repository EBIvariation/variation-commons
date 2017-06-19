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

import java.util.List;

public class VariantEntityRepositoryFileFilter extends VariantEntityRepositoryFilter<List<String>> {

    private static final String FIELD = VariantEntityRepositoryFilter.FILE_ID_FIELD;

    public VariantEntityRepositoryFileFilter(List<String> files) {
        super(FIELD, false, files, RelationalOperator.IN);
    }
}
