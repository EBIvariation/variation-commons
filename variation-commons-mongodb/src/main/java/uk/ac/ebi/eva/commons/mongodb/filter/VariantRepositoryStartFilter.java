package uk.ac.ebi.eva.commons.mongodb.filter;

import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;

public class VariantRepositoryStartFilter extends VariantRepositoryFilter<Long>{

    private static String FIELD = VariantMongo.START_FIELD;
    public VariantRepositoryStartFilter(Long start, RelationalOperator relationalOperator){
        super(FIELD,start,relationalOperator);
    }
}
