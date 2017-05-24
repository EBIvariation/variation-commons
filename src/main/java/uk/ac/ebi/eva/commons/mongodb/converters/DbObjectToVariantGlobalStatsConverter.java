/*
 * Copyright 2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.converters;

import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.stats.VariantGlobalStats;
import org.springframework.core.convert.converter.Converter;

public class DbObjectToVariantGlobalStatsConverter implements Converter<DBObject, VariantGlobalStats> {

    public final static String NUMSAMPLES_FIELD = "nSamp";
    public final static String NUMVARIANTS_FIELD = "nVar";
    public final static String NUMSNPS_FIELD = "nSnp";
    public final static String NUMINDELS_FIELD = "nIndel";
    public final static String NUMSTRUCTURAL_FIELD = "nSv";
    public final static String NUMPASSFILTERS_FIELD = "nPass";
    public final static String NUMTRANSITIONS_FIELD = "nTi";
    public final static String NUMTRANSVERSIONS_FIELD = "nTv";
    public final static String MEANQUALITY_FIELD = "meanQ";

    @Override
    public VariantGlobalStats convert(DBObject statsObject) {
        return new VariantGlobalStats(
                (int) statsObject.get(NUMVARIANTS_FIELD),
                (int) statsObject.get(NUMSAMPLES_FIELD),
                (int) statsObject.get(NUMSNPS_FIELD),
                (int) statsObject.get(NUMINDELS_FIELD),
                0, // TODO Add structural variants to schema!
                (int) statsObject.get(NUMPASSFILTERS_FIELD),
                (int) statsObject.get(NUMTRANSITIONS_FIELD),
                (int) statsObject.get(NUMTRANSVERSIONS_FIELD),
                -1,
                ((Double) statsObject.get(MEANQUALITY_FIELD)).floatValue(),
                null
        );
    }
}
