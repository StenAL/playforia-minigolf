package com.aapeli.multiuser;

import com.aapeli.client.ImageManager;
import com.aapeli.client.TextManager;
import java.awt.Image;
import org.moparforia.shared.Language;

public final class Languages {
    private final TextManager textManager;
    private final Image[] flagImages;

    public Languages(TextManager textManager, ImageManager imageManager) {
        this.textManager = textManager;
        Image languageFlags = imageManager.getImage("language-flags");
        this.flagImages = imageManager.separateImages(languageFlags, Language.values().length);
    }

    public Image getFlag(int language) {
        return this.flagImages[language];
    }

    public String getName(int id) {
        return this.textManager.getText("Language_" + Language.fromId(id));
    }
}
