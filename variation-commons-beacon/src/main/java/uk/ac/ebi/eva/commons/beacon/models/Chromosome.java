package uk.ac.ebi.eva.commons.beacon.models;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Reference name (chromosome). Accepting values 1-22, X, Y.
 */
public enum Chromosome {
    _1("1"),
    _2("2"),
    _3("3"),
    _4("4"),
    _5("5"),
    _6("6"),
    _7("7"),
    _8("8"),
    _9("9"),
    _10("10"),
    _11("11"),
    _12("12"),
    _13("13"),
    _14("14"),
    _15("15"),
    _16("16"),
    _17("17"),
    _18("18"),
    _19("19"),
    _20("20"),
    _21("21"),
    _22("22"),
    X("X"),
    Y("Y"),
    MT("MT");

    private String value;

    Chromosome(String value) {
        this.value = value;
    }

    @JsonCreator
    public static Chromosome fromValue(String text) {
        for (Chromosome b : Chromosome.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toString() {
        return String.valueOf(value);
    }
}
