package com.aapeli.multiuser;

import com.aapeli.client.ImageManager;
import com.aapeli.client.TextManager;
import java.awt.Image;
import org.moparforia.shared.Locale;

public final class Languages {

    public static final int LANGUAGE_UNKNOWN = 0;
    public static final String[][] languageData = {
        {
            "null", "-",
        },
        {
            "bg", "null",
        },
        {
            "de", "null",
        },
        {
            "en", "null",
        },
        {
            "es", "null",
        },
        {
            "et", "ee",
        },
        {
            "fi", "null",
        },
        {
            "fr", "null",
        },
        {
            "hu", "null",
        },
        {
            "it", "null",
        },
        {
            "lv", "null",
        },
        {
            "nl", "null",
        },
        {
            "no", "null",
        },
        {
            "pl", "null",
        },
        {
            "pt", "null",
        },
        {
            "ro", "null",
        },
        {
            "ru", "null",
        },
        {
            "sv", "se",
        },
        {
            "tr", "null",
        },
        {
            "lt", "null",
        },
    };
    private TextManager textManager;
    private ImageManager imageManager;
    private Image[] flagImages;

    public Languages(TextManager textManager, ImageManager imageManager) {
        this.textManager = textManager;
        this.imageManager = imageManager;
    }

    public static int getLanguageId(Locale locale) {
        if (locale == null) {
            return LANGUAGE_UNKNOWN;
        } else {
            String language = locale.getLanguage().toString();

            for (int j = 1; j < languageData.length; ++j) {
                if (language.equals(languageData[j][0])) {
                    return j;
                }

                if (languageData[j][1] != null && language.equals(languageData[j][1])) {
                    return j;
                }
            }

            return LANGUAGE_UNKNOWN;
        }
    }

    public Image getFlag(int language) {
        if (this.flagImages == null) {
            Image languageFlags = this.imageManager.getShared("language-flags.png");
            this.flagImages = this.imageManager.separateImages(languageFlags, languageData.length);
        }

        return this.flagImages[language];
    }

    public String getName(int var1) {
        return this.textManager.getShared("Language_" + languageData[var1][0]);
    }
}
