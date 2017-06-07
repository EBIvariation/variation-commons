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
package uk.ac.ebi.eva.commons.mongodb.converters;

import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import uk.ac.ebi.eva.commons.mongodb.entity.VariantEntity;

public class DBObjectToVariantEntityConverter implements Converter<DBObject, VariantEntity> {

    protected static Logger logger = LoggerFactory.getLogger(DBObjectToVariantEntityConverter.class);

    @Override
    public VariantEntity convert(DBObject dbObject) {
        DBObjectToVariantConverter converter =
                new DBObjectToVariantConverter(
                        new DBObjectToVariantSourceEntryConverter(new DBObjectToSamplesConverter()),
                        new DBObjectToVariantStatsConverter()
                );
        return new VariantEntity(converter.convert(dbObject));
    }

}
