package uk.ac.ebi.eva.commons.core.models.factories.exception;

import uk.ac.ebi.eva.commons.core.models.pipeline.Variant;

public class IncompleteInformationException extends RuntimeException {

    public IncompleteInformationException(Variant variant) {
        super("The variant " + variant + " has no allele frequencies or counts in its INFO tag");
    }
}
