/*
 * Copyright 2015-2017 EMBL - European Bioinformatics Institute
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
package uk.ac.ebi.eva.commons.mongodb.entities.subdocuments;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.core.models.IScore;

/**
 * Mongo database representation of a score and its description.
 */
public class ScoreMongo implements IScore {

    public final static String SCORE_SCORE_FIELD = "sc";

    public final static String SCORE_DESCRIPTION_FIELD = "desc";

    @Field(value = SCORE_SCORE_FIELD)
    private Double score;

    @Field(value = SCORE_DESCRIPTION_FIELD)
    private String description;

    ScoreMongo() {
        // Spring empty constructor
    }

    public ScoreMongo(IScore score) {
        this(score.getScore(), score.getDescription());
    }

    public ScoreMongo(Double score, String description) {
        this.score = score;
        this.description = description;
    }

    @Override
    public Double getScore() {
        return score;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreMongo scoreMongo1 = (ScoreMongo) o;

        if (score != null ? !score.equals(scoreMongo1.score) : scoreMongo1.score != null) return false;
        return description != null ? description.equals(scoreMongo1.description) : scoreMongo1.description == null;
    }

    @Override
    public int hashCode() {
        int result = score != null ? score.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
