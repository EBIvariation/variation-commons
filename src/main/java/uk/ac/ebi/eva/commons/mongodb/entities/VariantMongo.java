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

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.core.models.IVariant;
import uk.ac.ebi.eva.commons.core.models.VariantType;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.AnnotationIndexMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.HgvsMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantAtMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantSourceEntryMongo;
import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.VariantStatisticsMongo;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Mapped class for variant collection in mongo
 */
@Document(collection = "#{mongoCollectionsVariants}")
public class VariantMongo {

    private static final int CHUNK_SIZE_SMALL = 1000;

    private static final int CHUNK_SIZE_BIG = 10000;

    public static final String ONE_THOUSAND_STRING = CHUNK_SIZE_SMALL / 1000 + "k";

    public static final String TEN_THOUSAND_STRING = CHUNK_SIZE_BIG / 1000 + "k";

    public final static String TYPE_FIELD = "type";

    public final static String CHROMOSOME_FIELD = "chr";

    public final static String START_FIELD = "start";

    public final static String END_FIELD = "end";

    public final static String LENGTH_FIELD = "len";

    public final static String REFERENCE_FIELD = "ref";

    public final static String ALTERNATE_FIELD = "alt";

    public static final String AT_FIELD = "_at";

    public final static String HGVS_FIELD = "hgvs";

    public final static String IDS_FIELD = "ids";

    public final static String FILES_FIELD = "files";

    public final static String STATS_FIELD = "st";

    public final static String ANNOTATION_FIELD = "annot";

    @Id
    private String id;

    @Field(TYPE_FIELD)
    private VariantType type;

    @Field(CHROMOSOME_FIELD)
    private String chromosome;

    @Field(START_FIELD)
    private int start;

    @Field(END_FIELD)
    private int end;

    @Field(LENGTH_FIELD)
    private int length;

    @Field(REFERENCE_FIELD)
    private String reference;

    @Field(ALTERNATE_FIELD)
    private String alternate;

    @Field(AT_FIELD)
    private VariantAtMongo at;

    @Field(HGVS_FIELD)
    private Set<HgvsMongo> hgvs;

    @Field(IDS_FIELD)
    private Set<String> ids;

    @Field(FILES_FIELD)
    private Set<VariantSourceEntryMongo> variantSourceEntries;

    @Field(STATS_FIELD)
    private Set<VariantStatisticsMongo> variantStatsMongo;

    @Field(ANNOTATION_FIELD)
    private Set<AnnotationIndexMongo> indexedAnnotations;

    VariantMongo() {
        // Empty document constructor for spring-data
        this(
                null,
                null,
                null,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    public VariantMongo(IVariant variant) {
        this(
                buildVariantId(
                        variant.getChromosome(),
                        variant.getStart(),
                        variant.getReference(),
                        variant.getAlternate()),
                variant.getType(),
                variant.getChromosome(),
                variant.getStart(),
                variant.getEnd(),
                variant.getLength(),
                variant.getReference(),
                variant.getAlternate(),
                generateAtField(variant.getChromosome(), variant.getStart()),
                HgvsMongo.createHgvsMongo(variant.getHgvs()),
                variant.getIds(),
                VariantSourceEntryMongo.createSourceEntries(variant.getSourceEntries()),
                null,
                null
        );
    }

    public VariantMongo(VariantType type, String chromosome, int start, int end, int length, String reference,
                        String alternate) {
        this(
                buildVariantId(chromosome, start, reference, alternate),
                type,
                chromosome,
                start,
                end,
                length,
                reference,
                alternate,
                generateAtField(chromosome, start),
                null,
                null,
                null,
                null,
                null
        );
    }

    public VariantMongo(String id, VariantType type, String chromosome, int start, int end, int length,
                        String reference, String alternate, VariantAtMongo at, Set<HgvsMongo> hgvs, Set<String> ids,
                        Set<VariantSourceEntryMongo> variantSourceEntries, Set<VariantStatisticsMongo> variantStatsMongo,
                        Set<AnnotationIndexMongo> indexedAnnotations) {
        this.id = id;
        this.type = type;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.length = length;
        this.reference = reference;
        this.alternate = alternate;
        this.at = at;
        this.hgvs = new LinkedHashSet<>();
        if (hgvs != null && !hgvs.isEmpty()) {
            this.hgvs.addAll(hgvs);
        }
        this.ids = new LinkedHashSet<>();
        if (ids != null && !ids.isEmpty()) {
            this.ids.addAll(ids);
        }
        this.variantSourceEntries = new HashSet<>();
        if (variantSourceEntries != null && !variantSourceEntries.isEmpty()) {
            this.variantSourceEntries.addAll(variantSourceEntries);
        }
        this.variantStatsMongo = new HashSet<>();
        if (variantStatsMongo != null && !variantStatsMongo.isEmpty()) {
            this.variantStatsMongo.addAll(variantStatsMongo);
        }
        this.indexedAnnotations = new HashSet<>();
        if (indexedAnnotations != null && !indexedAnnotations.isEmpty()) {
            this.indexedAnnotations.addAll(indexedAnnotations);
        }
    }

    public static String buildVariantId(String chromosome, int start, String reference, String alternate) {
        StringBuilder builder = new StringBuilder(chromosome);
        builder.append("_");
        builder.append(start);
        builder.append("_");
        if (!reference.equals("-")) {
            if (reference.length() < 50) {
                builder.append(reference);
            } else {
                builder.append(new String(DigestUtils.sha1(reference)));
            }
        }

        builder.append("_");
        if (!alternate.equals("-")) {
            if (alternate.length() < 50) {
                builder.append(alternate);
            } else {
                builder.append(new String(DigestUtils.sha1(alternate)));
            }
        }

        return builder.toString();
    }

    public static VariantAtMongo generateAtField(String chromosome, int start) {
        int smallChunkId = start / CHUNK_SIZE_SMALL;
        int bigChunkId = start / CHUNK_SIZE_BIG;
        String chunkSmall = chromosome + "_" + smallChunkId + "_" + ONE_THOUSAND_STRING;
        String chunkBig = chromosome + "_" + bigChunkId + "_" + TEN_THOUSAND_STRING;

        return new VariantAtMongo(chunkSmall, chunkBig);
    }

    public String getId() {
        return id;
    }

    public VariantType getType() {
        return type;
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

    public int getLength() {
        return length;
    }

    public String getReference() {
        return reference;
    }

    public String getAlternate() {
        return alternate;
    }

    public VariantAtMongo getAt() {
        return at;
    }

    public Map<String, Set<String>> getHgvs() {
        Map<String, Set<String>> map = new LinkedHashMap<>();
        for (HgvsMongo item : hgvs) {
            map.putIfAbsent(item.getType(), new HashSet<>());
            map.get(item.getType()).add(item.getName());
        }
        return map;
    }

    public Set<String> getIds() {
        return ids;
    }

    public Set<VariantSourceEntryMongo> getSourceEntries() {
        return variantSourceEntries;
    }

    public Set<VariantStatisticsMongo> getVariantStatsMongo() {
        return variantStatsMongo;
    }

    public Set<AnnotationIndexMongo> getIndexedAnnotations() {
        return indexedAnnotations;
    }

    public Set<String> getAnnotationIds() {
        Set<String> ids = new HashSet<>();
        for (AnnotationIndexMongo indexedAnnotation : indexedAnnotations) {
            ids.add(AnnotationMongo.buildAnnotationId(
                    chromosome,
                    start,
                    reference,
                    alternate,
                    indexedAnnotation.getVepVersion(),
                    indexedAnnotation.getVepCacheVersion()));
        }
        return ids;
    }
}
