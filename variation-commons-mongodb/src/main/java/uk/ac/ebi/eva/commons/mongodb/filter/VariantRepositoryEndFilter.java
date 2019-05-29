package uk.ac.ebi.eva.commons.mongodb.filter;

import uk.ac.ebi.eva.commons.mongodb.entities.VariantMongo;

public class VariantRepositoryEndFilter extends VariantRepositoryFilter<Long>{

    private static String FIELD = VariantMongo.END_FIELD;

    public VariantRepositoryEndFilter(Long end,RelationalOperator relationalOperator){
        super(FIELD,end,relationalOperator);
    }
}
