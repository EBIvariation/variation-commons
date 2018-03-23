package uk.ac.ebi.eva.commons.core.models.factories.exception;

import uk.ac.ebi.eva.commons.core.models.VariantCoreFields;

public class IncompleteInformationException extends RuntimeException {

    public IncompleteInformationException(VariantCoreFields variant) {
        super("The variant " + variant + " has no samples data, AC and AF INFO tags");
    }
}
