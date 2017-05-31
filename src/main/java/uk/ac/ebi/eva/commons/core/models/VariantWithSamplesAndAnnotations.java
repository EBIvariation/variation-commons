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

import uk.ac.ebi.eva.commons.mongodb.entity.AnnotationDocument;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A mutation in the genome, defined as a change from a reference to an alternate allele in a certain position of
 * said genome.
 */
public class VariantWithSamplesAndAnnotations extends Variant {

    /**
     * Annotations of the genomic variation.
     */
    private final Set<Annotation> annotations;


    public VariantWithSamplesAndAnnotations(String chromosome, int start, int end, String reference, String alternate) {
        super(chromosome, start, end, reference, alternate);
        annotations = new HashSet<>();
    }

    public void addAnnotation(Annotation annotation){
        this.annotations.add(annotation);
    }

    public void setAnnotations(Set<Annotation> annotations){
        this.annotations.clear();
        this.annotations.addAll(annotations);
    }

    public Set<Annotation> getAnnotations(){
        return Collections.unmodifiableSet(annotations);
    }

    @Override
    public Collection<VariantSourceEntryWithSamples> getSourceEntries() {
        return (Collection<VariantSourceEntryWithSamples>) super.getSourceEntries();
    }

}
