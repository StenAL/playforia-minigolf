package org.moparforia.shared;

public enum Language {
    ENGLISH("en"),
    FINNISH("fi"),
    SWEDISH("sv");

    private final String languageCode;

    Language(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return this.languageCode;
    }
}
