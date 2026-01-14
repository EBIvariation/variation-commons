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
 * Base implementation of {@link IAnnotationMetadata}
 */
public class AnnotationMetadata implements IAnnotationMetadata {

    private String vepVersion;

    private String cacheVersion;

    private boolean defaultVersion;

    AnnotationMetadata() {
        //Spring empty constructor
        this(null, null);
    }

    public AnnotationMetadata(IAnnotationMetadata annotationMetadata) {
        this(annotationMetadata.getVepVersion(), annotationMetadata.getCacheVersion(),
                annotationMetadata.isDefaultVersion());
    }

    public AnnotationMetadata(String vepVersion, String cacheVersion) {
        this(vepVersion, cacheVersion, false);
    }

    public AnnotationMetadata(String vepVersion, String cacheVersion, boolean defaultVersion) {
        this.vepVersion = vepVersion;
        this.cacheVersion = cacheVersion;
        this.defaultVersion = defaultVersion;
    }

    @Override
    public String getVepVersion() {
        return vepVersion;
    }

    @Override
    public String getCacheVersion() {
        return cacheVersion;
    }

    @Override
    public boolean isDefaultVersion() {
        return defaultVersion;
    }

    public void setDefaultVersion(boolean defaultVersion) {
        this.defaultVersion = defaultVersion;
    }

    @Override
    public String toString() {
        return "AnnotationMetadata{" +
                "vepVersion='" + vepVersion + '\'' +
                ", cacheVersion='" + cacheVersion + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotationMetadata)) return false;

        AnnotationMetadata that = (AnnotationMetadata) o;

        if (vepVersion != null ? !vepVersion.equals(that.vepVersion) : that.vepVersion != null) return false;
        return cacheVersion != null ? cacheVersion.equals(that.cacheVersion) : that.cacheVersion == null;

    }

    @Override
    public int hashCode() {
        int result = vepVersion != null ? vepVersion.hashCode() : 0;
        result = 31 * result + (cacheVersion != null ? cacheVersion.hashCode() : 0);
        return result;
    }
}
