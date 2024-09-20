package org.moparforia.shared;

public enum Locale {
    EN_US("en_US", Language.ENGLISH),
    FI_FI("fi_FI", Language.FINNISH),
    SV_SE("sv_SE", Language.SWEDISH);

    private final String localeCode;
    private final Language language;

    Locale(String localeCode, Language language) {
        this.localeCode = localeCode;
        this.language = language;
    }

    public Language getLanguage() {
        return this.language;
    }

    @Override
    public String toString() {
        return this.localeCode;
    }
}
