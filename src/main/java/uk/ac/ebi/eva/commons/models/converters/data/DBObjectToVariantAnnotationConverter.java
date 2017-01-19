/*
 * Copyright 2015-2016 OpenCB
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
package uk.ac.ebi.eva.commons.models.converters.data;

import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.opencb.biodata.models.variant.annotation.ConsequenceType;
import org.opencb.biodata.models.variant.annotation.ConsequenceTypeMappings;
import org.opencb.biodata.models.variant.annotation.Score;
import org.opencb.biodata.models.variant.annotation.VariantAnnotation;
import org.opencb.biodata.models.variant.annotation.Xref;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

import java.util.LinkedList;
import java.util.List;

public class DBObjectToVariantAnnotationConverter implements Converter<DBObject, VariantAnnotation> {

    public final static String CONSEQUENCE_TYPE_FIELD = "ct";
    public static final String GENE_NAME_FIELD = "gn";
    public static final String ENSEMBL_GENE_ID_FIELD = "ensg";
    public static final String ENSEMBL_TRANSCRIPT_ID_FIELD = "enst";
    public static final String RELATIVE_POS_FIELD = "relPos";
    public static final String CODON_FIELD = "codon";
    public static final String STRAND_FIELD = "strand";
    public static final String BIOTYPE_FIELD = "bt";
    public static final String C_DNA_POSITION_FIELD = "cDnaPos";
    public static final String CDS_POSITION_FIELD = "cdsPos";
    public static final String AA_POSITION_FIELD = "aaPos";
    public static final String AA_CHANGE_FIELD = "aaChange";
    public static final String SO_ACCESSION_FIELD = "so";
    public static final String PROTEIN_SUBSTITUTION_SCORE_FIELD = "ps_score";
    public static final String POLYPHEN_FIELD = "polyphen";
    public static final String SIFT_FIELD = "sift";

    public static final String XREFS_FIELD = "xrefs";
    public final static String XREF_ID_FIELD = "id";
    public final static String XREF_SOURCE_FIELD = "src";

    public static final String CONSERVED_REGION_SCORE_FIELD = "cr_score";
    public final static String SCORE_SCORE_FIELD = "sc";
    public final static String SCORE_SOURCE_FIELD = "src";
    public final static String SCORE_DESCRIPTION_FIELD = "desc";

    public static final String CLINICAL_DATA_FIELD = "clinicalData";

    protected static Logger logger = LoggerFactory.getLogger(DBObjectToVariantAnnotationConverter.class);

    @Override
    public VariantAnnotation convert(DBObject object) {
        VariantAnnotation va = new VariantAnnotation();

        //ConsequenceType
        List<ConsequenceType> consequenceTypes = new LinkedList<>();
        Object cts = object.get(CONSEQUENCE_TYPE_FIELD);
        if(cts != null && cts instanceof BasicDBList) {
            for (Object o : ((BasicDBList) cts)) {
                if(o instanceof DBObject) {
                    DBObject ct = (DBObject) o;

                    //SO accession name
                    List<String> soAccessionNames = new LinkedList<>();
                    if(ct.containsField(SO_ACCESSION_FIELD)) {
                        if (ct.get(SO_ACCESSION_FIELD) instanceof List) {
                            List<Integer> list = (List) ct.get(SO_ACCESSION_FIELD);
                            for (Integer so : list) {
                                soAccessionNames.add(ConsequenceTypeMappings.accessionToTerm.get(so));
                            }
                        } else {
                            soAccessionNames.add(ConsequenceTypeMappings.accessionToTerm.get(ct.get(SO_ACCESSION_FIELD)));
                        }
                    }

                    //ProteinSubstitutionScores
                    List<Score> proteinSubstitutionScores = new LinkedList<>();
                    if(ct.containsField(PROTEIN_SUBSTITUTION_SCORE_FIELD)) {
                        List<DBObject> list = (List) ct.get(PROTEIN_SUBSTITUTION_SCORE_FIELD);
                        for (DBObject dbObject : list) {
                            proteinSubstitutionScores.add(new Score(
                                    getDefault(dbObject, SCORE_SCORE_FIELD, 0.0),
                                    getDefault(dbObject, SCORE_SOURCE_FIELD, ""),
                                    getDefault(dbObject, SCORE_DESCRIPTION_FIELD, "")
                            ));
                        }
                    }
                    if (ct.containsField(POLYPHEN_FIELD)) {
                        DBObject dbObject = (DBObject) ct.get(POLYPHEN_FIELD);
                        proteinSubstitutionScores.add(new Score(getDefault(dbObject, SCORE_SCORE_FIELD, 0.0),
                                "Polyphen",
                                getDefault(dbObject, SCORE_DESCRIPTION_FIELD, "")));
                    }
                    if (ct.containsField(SIFT_FIELD)) {
                        DBObject dbObject = (DBObject) ct.get(SIFT_FIELD);
                        proteinSubstitutionScores.add(new Score(getDefault(dbObject, SCORE_SCORE_FIELD, 0.0),
                                "Sift",
                                getDefault(dbObject, SCORE_DESCRIPTION_FIELD, "")));
                    }


                    consequenceTypes.add(new ConsequenceType(
                            getDefault(ct, GENE_NAME_FIELD, "") /*.toString()*/,
                            getDefault(ct, ENSEMBL_GENE_ID_FIELD, "") /*.toString()*/,
                            getDefault(ct, ENSEMBL_TRANSCRIPT_ID_FIELD, "") /*.toString()*/,
                            getDefault(ct, STRAND_FIELD, "") /*.toString()*/,
                            getDefault(ct, BIOTYPE_FIELD, "") /*.toString()*/,
                            getDefault(ct, C_DNA_POSITION_FIELD, 0),
                            getDefault(ct, CDS_POSITION_FIELD, 0),
                            getDefault(ct, AA_POSITION_FIELD, 0),
                            getDefault(ct, AA_CHANGE_FIELD, "") /*.toString() */,
                            getDefault(ct, CODON_FIELD, "") /*.toString() */,
                            proteinSubstitutionScores,
                            soAccessionNames));
                }
            }

        }
        va.setConsequenceTypes(consequenceTypes);

        //Conserved Region Scores
        List<Score> conservedRegionScores = new LinkedList<>();
        if(object.containsField(CONSERVED_REGION_SCORE_FIELD)) {
            List<DBObject> list = (List) object.get(CONSERVED_REGION_SCORE_FIELD);
            for (DBObject dbObject : list) {
                conservedRegionScores.add(new Score(
                        getDefault(dbObject, SCORE_SCORE_FIELD, 0.0),
                        getDefault(dbObject, SCORE_SOURCE_FIELD, ""),
                        getDefault(dbObject, SCORE_DESCRIPTION_FIELD, "")
                ));
            }
        }
        va.setConservedRegionScores(conservedRegionScores);

        //XREfs
        List<Xref> xrefs = new LinkedList<>();
        Object xrs = object.get(XREFS_FIELD);
        if(xrs != null && xrs instanceof BasicDBList) {
            for (Object o : (BasicDBList) xrs) {
                if(o instanceof DBObject) {
                    DBObject xref = (DBObject) o;

                    xrefs.add(new Xref(
                            (String) xref.get(XREF_ID_FIELD),
                            (String) xref.get(XREF_SOURCE_FIELD))
                    );
                }
            }
        }
        va.setXrefs(xrefs);

        return va;
    }

    //Utils
    private String getDefault(DBObject object, String key, String defaultValue) {
        Object o = object.get(key);
        if (o != null ) {
            return o.toString();
        } else {
            return defaultValue;
        }
    }

    private int getDefault(DBObject object, String key, int defaultValue) {
        Object o = object.get(key);
        if (o != null ) {
            if (o instanceof Integer) {
                return (Integer) o;
            } else {
                try {
                    return Integer.parseInt(o.toString());
                } catch (Exception e) {
                    return defaultValue;
                }
            }
        } else {
            return defaultValue;
        }
    }

    private double getDefault(DBObject object, String key, double defaultValue) {
        Object o = object.get(key);
        if (o != null ) {
            if (o instanceof Double) {
                return (Double) o;
            } else {
                try {
                    return Double.parseDouble(o.toString());
                } catch (Exception e) {
                    return defaultValue;
                }
            }
        } else {
            return defaultValue;
        }
    }

}
