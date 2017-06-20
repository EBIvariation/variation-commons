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

import uk.ac.ebi.eva.commons.mongodb.entities.subdocuments.ScoreMongo;

import java.util.Set;

/**
 * Basic implementation of consequence type model
 */
public class ConsequenceType implements IConsequenceType {

    private String geneName;

    private String ensemblGeneId;

    private String ensemblTranscriptId;

    private String strand;

    private String biotype;

    private Integer cDnaPosition;

    private Integer cdsPosition;

    private Integer aaPosition;

    private String aaChange;

    private String codon;

    private ScoreMongo sift;

    private ScoreMongo polyphen;

    private Set<Integer> soAccessions;

    private Integer relativePosition;

    public ConsequenceType(String geneName, String ensemblGeneId, String ensemblTranscriptId, String strand,
                           String biotype, Integer cDnaPosition, Integer cdsPosition, Integer aaPosition,
                           String aaChange, String codon, ScoreMongo sift, ScoreMongo polyphen, Set<Integer> soAccessions,
                           Integer relativePosition) {
        this.geneName = geneName;
        this.ensemblGeneId = ensemblGeneId;
        this.ensemblTranscriptId = ensemblTranscriptId;
        this.strand = strand;
        this.biotype = biotype;
        this.cDnaPosition = cDnaPosition;
        this.cdsPosition = cdsPosition;
        this.aaPosition = aaPosition;
        this.aaChange = aaChange;
        this.codon = codon;
        this.sift = sift;
        this.polyphen = polyphen;
        this.soAccessions = soAccessions;
        this.relativePosition = relativePosition;
    }

    public ConsequenceType(IConsequenceType consequenceType) {
        this(
                consequenceType.getGeneName(),
                consequenceType.getEnsemblGeneId(),
                consequenceType.getEnsemblTranscriptId(),
                consequenceType.getStrand(),
                consequenceType.getBiotype(),
                consequenceType.getcDnaPosition(),
                consequenceType.getCdsPosition(),
                consequenceType.getAaPosition(),
                consequenceType.getAaChange(),
                consequenceType.getCodon(),
                consequenceType.getSift(),
                consequenceType.getPolyphen(),
                consequenceType.getSoAccessions(),
                consequenceType.getRelativePosition()
        );
    }

    @Override
    public String getGeneName() {
        return geneName;
    }

    @Override
    public String getEnsemblGeneId() {
        return ensemblGeneId;
    }

    @Override
    public String getEnsemblTranscriptId() {
        return ensemblTranscriptId;
    }

    @Override
    public String getStrand() {
        return strand;
    }

    @Override
    public String getBiotype() {
        return biotype;
    }

    @Override
    public Integer getcDnaPosition() {
        return cDnaPosition;
    }

    @Override
    public Integer getCdsPosition() {
        return cdsPosition;
    }

    @Override
    public Integer getAaPosition() {
        return aaPosition;
    }

    @Override
    public String getAaChange() {
        return aaChange;
    }

    @Override
    public String getCodon() {
        return codon;
    }

    @Override
    public ScoreMongo getSift() {
        return sift;
    }

    @Override
    public ScoreMongo getPolyphen() {
        return polyphen;
    }

    @Override
    public Set<Integer> getSoAccessions() {
        return soAccessions;
    }

    @Override
    public Integer getRelativePosition() {
        return relativePosition;
    }

}
