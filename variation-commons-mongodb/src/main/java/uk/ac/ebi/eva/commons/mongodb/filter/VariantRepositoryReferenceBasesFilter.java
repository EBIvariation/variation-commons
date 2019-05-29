package uk.ac.ebi.eva.commons.mongodb.filter;

import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;

import java.util.List;

public class VariantRepositoryReferenceBasesFilter extends VariantRepositoryFilter<List<String>> {

    private static final String FIELD = VariantMongo.REFERENCE_FIELD;

    public VariantRepositoryReferenceBasesFilter(List<String> referenceBases){
        super(FIELD,referenceBases,RelationalOperator.IN);
    }
}
