package uk.ac.ebi.eva.commons.core.models.ws;

import uk.ac.ebi.eva.commons.core.models.IScore;
import uk.ac.ebi.eva.commons.core.models.Score;

public class ScoreWithSource extends Score {

    private String source;

    public ScoreWithSource() {
        this(null, null);
    }

    public ScoreWithSource(Double score, String description) {
        this(score, description, null);
    }

    public ScoreWithSource(IScore score) {
        this(score.getScore(), score.getDescription());
    }

    public ScoreWithSource(Double score, String description, String source) {
        super(score, description);
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
