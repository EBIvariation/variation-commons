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
package uk.ac.ebi.eva.commons.mongodb.entities.subdocuments;

import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.ac.ebi.eva.commons.core.models.IVariantSourceEntry;
import uk.ac.ebi.eva.commons.core.models.genotype.Genotype;
import uk.ac.ebi.eva.commons.core.utils.CompressionHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mongo database representation of VariantWithSamplesAndAnnotation Source entry.
 */
public class VariantSourceEntryMongo {

    private static final Logger logger = LoggerFactory.getLogger(VariantSourceEntryMongo.class);

    public final static char CHARACTER_TO_REPLACE_DOTS = (char) 163; // <-- £

    public final static String FILEID_FIELD = "fid";

    public final static String STUDYID_FIELD = "sid";

    public final static String ALTERNATES_FIELD = "alts";

    public final static String ATTRIBUTES_FIELD = "attrs";

    public final static String FORMAT_FIELD = "fm";

    public final static String SAMPLES_FIELD = "samp";

    public static final String DEFAULT = "def";

    @Field(FILEID_FIELD)
    private String fileId;

    @Field(STUDYID_FIELD)
    private String studyId;

    @Field(ALTERNATES_FIELD)
    private String[] alternates;

    @Field(ATTRIBUTES_FIELD)
    private BasicDBObject attributes;

    @Field(FORMAT_FIELD)
    private String format;

    @Field(SAMPLES_FIELD)
    private Map<String, Object> samples;

    VariantSourceEntryMongo() {
        this(null, null, null, null, null, null);
    }

    public VariantSourceEntryMongo(IVariantSourceEntry variantSourceEntry) {
        this(
                variantSourceEntry.getFileId(),
                variantSourceEntry.getStudyId(),
                variantSourceEntry.getSecondaryAlternates(),
                variantSourceEntry.getAttributes(),
                variantSourceEntry.getFormat(),
                variantSourceEntry.getSamplesData()
        );
    }

    public VariantSourceEntryMongo(String fileId, String studyId, String[] alternates, Map<String, String> attributes) {
        this(fileId, studyId, alternates, attributes, null, null);
    }

    public VariantSourceEntryMongo(String fileId, String studyId, String[] alternates, Map<String, String>
            attributes, String format, List<Map<String, String>> samplesData) {
        this.fileId = fileId;
        this.studyId = studyId;
        if (alternates != null && alternates.length > 0) {
            this.alternates = new String[alternates.length];
            System.arraycopy(alternates, 0, this.alternates, 0, alternates.length);
        }

        if (attributes != null) {
            this.attributes = buildAttributes(attributes);
        }

        if (format == null || samplesData == null || samplesData.isEmpty()) {
            this.format = null;
            this.samples = null;
        } else {
            this.format = format;
            this.samples = buildSampleData(samplesData);
        }
    }

    private BasicDBObject buildSampleData(List<Map<String, String>> samplesData) {
        Map<Genotype, List<Integer>> genotypeCodes = classifySamplesByGenotype(samplesData);

        // Get the most common genotype
        Genotype mostCommonGenotype = getMostCommonGenotype(genotypeCodes);

        // In Mongo, samples are stored in a map, classified by their genotype.
        // The most common genotype will be marked as "default" and the specific
        // positions where it is shown will not be stored. Example from 1000G:
        // "def" : 0|0,
        // "0|1" : [ 41, 311, 342, 358, 881, 898, 903 ],
        // "1|0" : [ 262, 290, 300, 331, 343, 369, 374, 391, 879, 918, 930 ]
        BasicDBObject mongoSamples = new BasicDBObject();
        if (mostCommonGenotype != null) {
            mongoSamples.append(DEFAULT, mostCommonGenotype.generateDatabaseString());
        }
        genotypeCodes.forEach(
                (genotype, sampleIndexes) -> {
                    if (!Objects.equals(genotype, mostCommonGenotype)) {
                        mongoSamples.append(genotype.generateDatabaseString(), sampleIndexes);
                    }
                }
        );
        return mongoSamples;
    }

    private Genotype getMostCommonGenotype(Map<Genotype, List<Integer>> genotypeCodes) {
        Genotype mostCommonGenotype = null;
        int maxSamplesInGenotype = 0;
        for (Map.Entry<Genotype, List<Integer>> entry : genotypeCodes.entrySet()) {
            int samplesInGenotype = entry.getValue().size();
            if (samplesInGenotype > maxSamplesInGenotype) {
                mostCommonGenotype = entry.getKey();
                maxSamplesInGenotype = samplesInGenotype;
            }
        }
        return mostCommonGenotype;
    }

    private Map<Genotype, List<Integer>> classifySamplesByGenotype(List<Map<String, String>> samplesData) {
        Map<Genotype, List<Integer>> genotypeCodes = new HashMap<>();

        for (int i = 0; i < samplesData.size(); i++) {
            String genotype = samplesData.get(i).get("GT");
            if (genotype != null) {
                Genotype g = new Genotype(genotype);
                List<Integer> samplesWithGenotype = genotypeCodes.get(g);
                if (samplesWithGenotype == null) {
                    samplesWithGenotype = new ArrayList<>();
                    genotypeCodes.put(g, samplesWithGenotype);
                }
                samplesWithGenotype.add(i);
            }
        }
        return genotypeCodes;
    }

    private BasicDBObject buildAttributes(Map<String, String> attributes) {
        BasicDBObject attrs = null;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            Object value = entry.getValue();
            if (entry.getKey().equals("src")) {
                String[] fields = entry.getValue().split("\t");
                StringBuilder sb = new StringBuilder();
                sb.append(fields[0]);
                for (int i = 1; i < fields.length && i < 8; i++) {
                    sb.append("\t").append(fields[i]);
                }
                try {
                    value = CompressionHelper.gzip(sb.toString());
                } catch (IOException ex) {
                    logger.error("Error compressing src field", ex);
                }
            }

            if (attrs == null) {
                attrs = new BasicDBObject(entry.getKey().replace('.', CHARACTER_TO_REPLACE_DOTS), value);
            } else {
                attrs.append(entry.getKey().replace('.', CHARACTER_TO_REPLACE_DOTS), value);
            }
        }
        return attrs;
    }

    public String getFileId() {
        return fileId;
    }

    public String getStudyId() {
        return studyId;
    }

    public String[] getSecondaryAlternates() {
        return alternates;
    }

    public String getFormat() {
        return format;
    }

    public Map<String, String> getAttributes() {
        Map<String, String> temp = new HashMap<>();
        if (attributes == null) {
            return temp;
        }
        for (String key : attributes.keySet()) {
            if (key.equals("src")) {
                try {
                    temp.put(key.replace(CHARACTER_TO_REPLACE_DOTS, '.'),
                            CompressionHelper.gunzip((byte[]) attributes.get(key)));
                } catch (IOException e) {
                    logger.error("Error decompressing src field", e);
                }
            } else {
                temp.put(key.replace(CHARACTER_TO_REPLACE_DOTS, '.'), (String) attributes.get(key));
            }
        }
        return temp;
    }

    public Map<String, Object> getSamples() {
        return samples;
    }

    public List<Map<String, String>> deflateSamplesData(int totalSamples) {
        List<Map<String, String>> temp = new ArrayList<>();
        if (totalSamples == 0 || samples == null) {
            return temp;
        }
        HashMap<String, String> defaultValue = new HashMap<>();
        defaultValue.put("GT", (String) samples.get(DEFAULT));
        for (int i = 0; i < totalSamples; i++) {
            temp.add(defaultValue);
        }
        for (String key : samples.keySet()) {
            if (key.equals(DEFAULT)) {
                continue;
            }
            HashMap<String, String> value = new HashMap<>();
            value.put("GT", key);
            for (int position : (ArrayList<Integer>) samples.get(key)) {
                temp.set(position, value);
            }
        }
        return temp;
    }

    /**
     * Creates a new set of {@link VariantSourceEntryMongo} generated from a collection of {@link IVariantSourceEntry}
     *
     * @param sourceEntries
     * @return
     * @throws NullPointerException if collection is null
     */
    public static Set<VariantSourceEntryMongo> createSourceEntries(Collection<? extends IVariantSourceEntry>
                                                                           sourceEntries) {
        return sourceEntries.stream().map(VariantSourceEntryMongo::new).collect(Collectors.toSet());
    }
}
