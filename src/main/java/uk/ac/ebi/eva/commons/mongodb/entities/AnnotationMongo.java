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
package uk.ac.ebi.eva.commons.mongodb.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.util.StringUtils;
import uk.ac.ebi.eva.commons.core.models.IAnnotation;
import uk.ac.ebi.eva.commons.core.models.IConsequenceType;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.ConsequenceTypeMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.XrefMongo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Mapped class for annotations collection in mongo
 */
@Document(collection = "#{mongoCollectionsAnnotations}")
public class AnnotationMongo implements IAnnotation {

    public static final String CHROMOSOME_FIELD = "chr";

    public static final String START_FIELD = "start";

    public static final String END_FIELD = "end";

    public static final String VEP_VERSION_FIELD = "vepv";

    public static final String VEP_CACHE_VERSION_FIELD = "cachev";

    public static final String CONSEQUENCE_TYPE_FIELD = "ct";

    public static final String XREFS_FIELD = "xrefs";

    @Id
    private String id;

    @Field(value = CHROMOSOME_FIELD)
    private String chromosome;

    @Field(value = START_FIELD)
    private int start;

    @Field(value = END_FIELD)
    private int end;

    @Field(value = VEP_VERSION_FIELD)
    private String vepVersion;

    @Field(value = VEP_CACHE_VERSION_FIELD)
    private String vepCacheVersion;

    @Field(value = CONSEQUENCE_TYPE_FIELD)
    private Set<ConsequenceTypeMongo> consequenceTypes;

    @Field(value = XREFS_FIELD)
    private Set<XrefMongo> xrefs;

    AnnotationMongo() {
        // Empty document constructor for spring-data
        this(
                null,
                null,
                -1,
                -1,
                null,
                null,
                null,
                null
        );
    }

    public AnnotationMongo(String chromosome, int start, int end, String referenceAllele, String alternativeAllele,
                           String vepVersion, String vepCacheVersion) {
        this(
                buildAnnotationId(chromosome, start, referenceAllele, alternativeAllele, vepVersion, vepCacheVersion),
                chromosome,
                start,
                end,
                vepVersion,
                vepCacheVersion,
                null,
                null
        );
    }

    /**
     * Private copy constructor
     *
     * @param annotation
     */
    private AnnotationMongo(AnnotationMongo annotation) {
        this(
                annotation.getId(),
                annotation.getChromosome(),
                annotation.getStart(),
                annotation.getEnd(),
                annotation.getVepVersion(),
                annotation.getVepCacheVersion(),
                annotation.getXrefs(),
                annotation.getConsequenceTypes()
        );
    }

    public AnnotationMongo(String id, String chromosome, int start, int end, String vepVersion,
                           String vepCacheVersion, Set<XrefMongo> xrefs, Set<ConsequenceTypeMongo> consequenceTypes) {
        this.id = id;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.vepVersion = vepVersion;
        this.vepCacheVersion = vepCacheVersion;
        this.xrefs = new HashSet<>();
        if (xrefs != null) {
            this.xrefs.addAll(xrefs);
        }
        this.consequenceTypes = new HashSet<>();
        if (consequenceTypes != null) {
            this.consequenceTypes.addAll(consequenceTypes);
        }
    }

    @Override
    public String getChromosome() {
        return chromosome;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    public String getId() {
        return id;
    }

    @Override
    public Set<XrefMongo> getXrefs() {
        return Collections.unmodifiableSet(xrefs);
    }

    @Override
    public Set<ConsequenceTypeMongo> getConsequenceTypes() {
        return Collections.unmodifiableSet(consequenceTypes);
    }

    @Override
    public String getVepVersion() {
        return vepVersion;
    }

    @Override
    public String getVepCacheVersion() {
        return vepCacheVersion;
    }

    public static String buildAnnotationId(String chromosome, int start, String reference, String alternate,
                                           String vepVersion, String vepCacheVersion) {
        StringBuilder builder = new StringBuilder(VariantMongo.buildVariantId(
                chromosome, start,
                reference,
                alternate));
        builder.append("_");
        builder.append(vepVersion);
        builder.append("_");
        builder.append(vepCacheVersion);
        return builder.toString();
    }

    /**
     * Builds the variant id from the current annotation id. In essence we remove the two extra fields added at the end
     * of the id and the underscores.
     *
     * @return
     */
    public String buildVariantId() {
        return getId().substring(0, getId().length() - vepVersion.length() - vepCacheVersion.length() - 2);
    }

    /**
     * Concatenate two annotations into a new one.
     *
     * @param annotation
     * @return A new instance with the concatenated array of consequence types and computed xrefs
     */
    public AnnotationMongo concatenate(AnnotationMongo annotation) {
        AnnotationMongo temp = new AnnotationMongo(this);
        if (annotation.getConsequenceTypes() != null) {
            temp.addConsequenceTypes(annotation.getConsequenceTypes());
        }
        return temp;
    }

    private void addConsequenceTypes(Set<ConsequenceTypeMongo> consequenceTypes) {
        consequenceTypes.forEach(this::addConsequenceType);
    }

    private void addConsequenceType(ConsequenceTypeMongo consequenceType) {
        consequenceTypes.add(consequenceType);
        xrefs.addAll(generateXrefsFromConsequenceType(consequenceType));
    }

    private Set<XrefMongo> generateXrefsFromConsequenceType(IConsequenceType consequenceType) {
        Set<XrefMongo> xrefs = new HashSet<>();
        if (StringUtils.hasText(consequenceType.getGeneName())) {
            xrefs.add(new XrefMongo(consequenceType.getGeneName(), "HGNC"));
        }
        if (StringUtils.hasText(consequenceType.getEnsemblGeneId())) {
            xrefs.add(new XrefMongo(consequenceType.getEnsemblGeneId(), "ensemblGene"));
        }
        if (StringUtils.hasText(consequenceType.getEnsemblTranscriptId())) {
            xrefs.add(new XrefMongo(consequenceType.getEnsemblTranscriptId(), "ensemblTranscript"));
        }
        return xrefs;
    }
}
