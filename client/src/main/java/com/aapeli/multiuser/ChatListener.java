package com.aapeli.multiuser;

public interface ChatListener {

    void localUserSay(String var1);

    void localUserSayPrivately(String to, String message);

    void localUserAdminCommand(String command, String parameter1);

    void localUserAdminCommand(String command, String parameter1, String parameter2);
}
