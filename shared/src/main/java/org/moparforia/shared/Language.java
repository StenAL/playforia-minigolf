package org.moparforia.shared;

public enum Language {
    UNKNOWN("null", 0),
    BULGARIAN("bg", 1),
    GERMAN("de", 2),
    ENGLISH("en", 3),
    SPANISH("es", 4),
    ESTONIAN("et", 5),
    FINNISH("fi", 6),
    FRENCH("fr", 7),
    HUNGARIAN("hr", 8),
    ITALIAN("it", 9),
    LATVIAN("lv", 10),
    DUTCH("nl", 11),
    NORWEGIAN("no", 12),
    POLISH("po", 13),
    PORTUGUESE("pt", 14),
    ROMANIAN("ro", 15),
    RUSSIAN("ru", 16),
    SWEDISH("sv", 17),
    TURKISH("tr", 18),
    LITHUANIAN("lt", 19),
    ;

    private final String languageCode;
    private final int id;

    Language(String languageCode, int id) {
        this.languageCode = languageCode;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.languageCode;
    }

    public static Language fromString(String code) {
        for (Language l : Language.values()) {
            if (l.languageCode.equalsIgnoreCase(code)) {
                return l;
            }
        }
        throw new IllegalArgumentException("No language with code " + code + " found");
    }

    public static Language fromId(int id) {
        for (Language l : Language.values()) {
            if (l.id == id) {
                return l;
            }
        }
        throw new IllegalArgumentException("No language with id " + id + " found");
    }

    public int getId() {
        return id;
    }
}
