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
package uk.ac.ebi.eva.commons.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "#{mongoCollectionsAnnotationMetadata}")
public class AnnotationMetadata {

    private static final String VEP_VERSION_FIELD = "vepv";

    private static final String CACHE_VERSION_FIELD = "cachev";

    @Id
    private String id;

    @Field(VEP_VERSION_FIELD)
    private String vepVersion;

    @Field(CACHE_VERSION_FIELD)
    private String cacheVersion;

    AnnotationMetadata() {
        // Empty document constructor for spring-data
    }

    public AnnotationMetadata(String vepVersion, String cacheVersion) {
        this(generateId(vepVersion, cacheVersion), vepVersion, cacheVersion);
    }

    public AnnotationMetadata(String id, String vepVersion, String cacheVersion) {
        this.id = id;
        this.vepVersion = vepVersion;
        this.cacheVersion = cacheVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    static String generateId(String vepVersion, String cacheVersion) {
        return vepVersion + "_" + cacheVersion;
    }

    public String getVepVersion() {
        return vepVersion;
    }

    public void setVepVersion(String vepVersion) {
        this.vepVersion = vepVersion;
    }

    public String getCacheVersion() {
        return cacheVersion;
    }

    public void setCacheVersion(String cacheVersion) {
        this.cacheVersion = cacheVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotationMetadata)) return false;

        AnnotationMetadata that = (AnnotationMetadata) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (vepVersion != null ? !vepVersion.equals(that.vepVersion) : that.vepVersion != null) return false;
        return cacheVersion != null ? cacheVersion.equals(that.cacheVersion) : that.cacheVersion == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (vepVersion != null ? vepVersion.hashCode() : 0);
        result = 31 * result + (cacheVersion != null ? cacheVersion.hashCode() : 0);
        return result;
    }
}
