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
package uk.ac.ebi.eva.commons.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.CohortCatMongo;

import java.util.Set;

@Document(collection = "#{mongoCollectionsSamples}")
public class SampleMongo {

    public static final String SEX_FIELD = "sex";

    public static final String FATHER_FIELD = "father";

    public static final String MOTHER_FIELD = "mother";

    public static final String COHORT_CAT_FIELD = "cohort_cat";

    @Id
    private String id;

    @Field(SEX_FIELD)
    private String sex;

    @Field(FATHER_FIELD)
    private String father;

    @Field(MOTHER_FIELD)
    private String mother;

    @Field(COHORT_CAT_FIELD)
    private Set<CohortCatMongo> cohortCat;

    public SampleMongo() {
    }

    public SampleMongo(String id, String sex, String father, String mother, Set<CohortCatMongo> cohortCat) {
        this.id = id;
        this.sex = sex;
        this.father = father;
        this.mother = mother;
        this.cohortCat = cohortCat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public Set<CohortCatMongo> getCohortCat() {
        return cohortCat;
    }

    public void setCohortCat(Set<CohortCatMongo> cohortCat) {
        this.cohortCat = cohortCat;
    }
}
