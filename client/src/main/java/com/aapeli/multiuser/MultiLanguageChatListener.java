package com.aapeli.multiuser;

import org.moparforia.shared.Language;

public interface MultiLanguageChatListener extends ChatListener {

    void localUserSay(Language language, String message);
}
