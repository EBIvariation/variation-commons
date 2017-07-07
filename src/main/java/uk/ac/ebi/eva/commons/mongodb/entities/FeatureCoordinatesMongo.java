/*
 * Copyright 2016 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.core.models.IFeatureCoordinates;

/**
 * Mapped class for feature collection in mongo
 */
@Document(collection = "#{mongoCollectionsFeatures}")
public class FeatureCoordinatesMongo implements IFeatureCoordinates {

    //TODO check if these are the intended names for the database structure
    private static final String NAME_FIELD = "name";

    private static final String FEATURE_FIELD = "feature";

    private static final String CHROMOSOME_FIELD = "chromosome";

    private static final String START_FIELD = "start";

    private static final String END_FIELD = "end";

    @Id
    private String id;

    @Field(NAME_FIELD)
    private String name;

    @Field(FEATURE_FIELD)
    private String feature;

    @Field(CHROMOSOME_FIELD)
    private String chromosome;

    @Field(START_FIELD)
    private int start;

    @Field(END_FIELD)
    private int end;

    FeatureCoordinatesMongo() {
        // Spring empty constructor
    }

    public FeatureCoordinatesMongo(String id, String name, String feature, String chromosome, int start, int end) {
        this.id = id;
        this.name = name;
        this.feature = feature;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChromosome() {
        return chromosome;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getFeature() {
        return feature;
    }

}

