package com.aapeli.multiuser;

import com.aapeli.client.ImageManager;
import java.awt.Image;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.moparforia.shared.Language;

public final class Languages {
    private final Map<Language, Image> flagImages;

    public Languages(ImageManager imageManager) {
        Image languageFlags = imageManager.getImage("language-flags");
        Image[] separatedImages = imageManager.separateImages(languageFlags, Language.values().length);
        this.flagImages = Arrays.stream(Language.values())
                .collect(Collectors.toMap(Function.identity(), l -> separatedImages[l.getId()]));
    }

    public Image getFlag(Language language) {
        return this.flagImages.get(language);
    }
}
