package embl.ebi.variation.commons.models.metadata;

/**
 * Created by tom on 06/10/15.
 */
public class StudyEnums {

    public enum Scope {
        SINGLE_ISOLATE ("single isolate"),
        MULTI_ISOLATE ("multi-isolate"),
        SINGLE_CELL ("single cell"),
        COMMUNITY ("community"),
        UNKNOWN ("unknown"),
        OTHER ("other");

        private final String name;

        private Scope(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }


    public enum Material {
        DNA ("DNA"),
        EXONIC_RNA ("exonic RNA"),
        TRANSCRIBED_RNA ("transcribed RNA"),
        UNKNOWN ("unknown"),
        OTHER ("other");

        private final String name;

        private Material(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

}
