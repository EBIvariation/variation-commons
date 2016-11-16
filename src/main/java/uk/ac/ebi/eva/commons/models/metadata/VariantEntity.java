package uk.ac.ebi.eva.commons.models.metadata;

import org.opencb.biodata.models.variant.Variant;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Tom Smith
 */
@Document(collection = "variants")
public class VariantEntity extends Variant {

    public VariantEntity(Variant variant) {
        setIds(variant.getIds());
        setChromosome(variant.getChromosome());
        setStart(variant.getStart());
        setEnd(variant.getEnd());
        setLength(variant.getLength());
        setAlternate(variant.getAlternate());
        setReference(variant.getReference());
        setType(variant.getType());
        setAnnotation(variant.getAnnotation());
        setSourceEntries(variant.getSourceEntries());
    }

}
