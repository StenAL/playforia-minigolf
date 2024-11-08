package org.moparforia.shared;

public enum Language {
    ENGLISH("en"),
    FINNISH("fi"),
    SWEDISH("sv"),
    FRENCH("fr");

    private final String languageCode;

    Language(String languageCode) {
        this.languageCode = languageCode;
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
}
