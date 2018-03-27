package uk.ac.ebi.eva.commons.core.models.factories.exception;

import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;

public class IncompleteInformationException extends RuntimeException {

    public IncompleteInformationException(Variant variant) {
        // TODO: variant to string is not defined
        super("The variant " + variant + " has no samples data, AC nor AF INFO tags");
    }
}
