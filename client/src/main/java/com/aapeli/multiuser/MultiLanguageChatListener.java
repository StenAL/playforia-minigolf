package com.aapeli.multiuser;

public interface MultiLanguageChatListener extends ChatListener {

    void localUserSay(int language, String message);
}
