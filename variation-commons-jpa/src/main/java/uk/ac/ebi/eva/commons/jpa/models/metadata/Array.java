/*
 * Copyright 2015 EMBL - European Bioinformatics Institute
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

package uk.ac.ebi.eva.commons.jpa.models.metadata;

/**
 * Created by parce on 05/10/15.
 *
 * @TODO Come back to this class for further design
 */
public class Array extends FileGenerator {

    public Array(String alias) {
        super(alias);
    }

    @Override
    public boolean equals(Object e) {
        if (e == this) {
            return true;
        } else if (!(e instanceof Array)) {
            return false;
        } else {
            return ((Array) e).getAlias().equals(alias);
        }
    }

    @Override
    public int hashCode() {
        return alias.hashCode();
    }
}
