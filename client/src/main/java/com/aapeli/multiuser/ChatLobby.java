package com.aapeli.multiuser;

import com.aapeli.client.BadWordFilter;
import com.aapeli.client.ImageManager;
import com.aapeli.client.Parameters;
import com.aapeli.client.TextManager;
import com.aapeli.colorgui.ColorCheckbox;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

public class ChatLobby extends ChatBase {

    private ColorCheckbox noJoinAndPartMessagesCheckbox;
    private ColorCheckbox noGameMessagesChatbox;
    private boolean shouldMuteMessagesIfUserListSet;

    public ChatLobby(
            Parameters parameters,
            TextManager textManager,
            ImageManager imageManager,
            BadWordFilter badWordFilter,
            int width,
            int height) {
        this(parameters, textManager, imageManager, badWordFilter, false, false, width, height);
    }

    public ChatLobby(
            Parameters parameters,
            TextManager textManager,
            ImageManager imageManager,
            BadWordFilter badWordFilter,
            boolean useSmallFont,
            int width,
            int height) {
        this(parameters, textManager, imageManager, badWordFilter, useSmallFont, false, width, height);
    }

    public ChatLobby(
            Parameters parameters,
            TextManager textManager,
            ImageManager imageManager,
            BadWordFilter badWordFilter,
            boolean useSmallFont,
            boolean var6,
            int width,
            int height) {
        super(parameters, textManager, imageManager, badWordFilter, useSmallFont, var6, width, height);
        this.createSettingsCheckBoxes();
        this.resizeLayout();
        this.shouldMuteMessagesIfUserListSet = true;
    }

    public void setBackground(Color var1) {
        super.setBackground(var1);
        if (this.noJoinAndPartMessagesCheckbox != null) {
            this.noJoinAndPartMessagesCheckbox.setBackground(var1);
        }

        if (this.noGameMessagesChatbox != null) {
            this.noGameMessagesChatbox.setBackground(var1);
        }
    }

    public void setForeground(Color var1) {
        super.setForeground(var1);
        if (this.noJoinAndPartMessagesCheckbox != null) {
            this.noJoinAndPartMessagesCheckbox.setForeground(var1);
        }

        if (this.noGameMessagesChatbox != null) {
            this.noGameMessagesChatbox.setForeground(var1);
        }
    }

    public void setBackgroundImage(Image var1, int var2, int var3) {
        super.setBackgroundImage(var1, var2, var3);
        Point var4 = this.noJoinAndPartMessagesCheckbox.getLocation();
        Point var5 = this.noGameMessagesChatbox.getLocation();
        this.noJoinAndPartMessagesCheckbox.setBackgroundImage(var1, var2 + var4.x, var3 + var4.y);
        this.noGameMessagesChatbox.setBackgroundImage(var1, var2 + var5.x, var3 + var5.y);
    }

    public int setFullUserList(String[] list) {
        int users = super.setFullUserList(list);
        if (this.shouldMuteMessagesIfUserListSet) {
            if (users >= 30) {
                this.noJoinAndPartMessagesCheckbox.setState(true);
                if (users >= 40) {
                    this.noGameMessagesChatbox.setState(true);
                }
            }

            this.shouldMuteMessagesIfUserListSet = false;
        }

        return users;
    }

    public String userJoin(String var1) {
        return this.addToUserList(var1, false);
    }

    public String userJoin(String var1, boolean var2) {
        User var3 = this.addToUserListNew(var1, false);
        if (!this.noJoinAndPartMessagesCheckbox.getState()) {
            this.method889(
                    var3,
                    super.textManager.getShared(
                            "Chat_Lobby_User" + (var2 ? "ReturnedFromGame" : "Joined"), var3.getNick()));
        }

        return var3.getNick();
    }

    public void userLeft(String var1) {
        super.userList.removeUser(var1);
    }

    public void userLeft(String var1, boolean var2) {
        User var3 = super.userList.removeAndReturnUser(var1);
        if (var3 != null && !this.noJoinAndPartMessagesCheckbox.getState()) {
            this.method889(
                    var3, super.textManager.getShared("Chat_Lobby_UserLeft" + (var2 ? "ConnectionProblem" : ""), var1));
        }
    }

    public void userLeftCreatedGame(String var1, String var2) {
        User var3 = super.userList.removeAndReturnUser(var1);
        if (var3 != null && !this.noGameMessagesChatbox.getState()) {
            this.method889(var3, super.textManager.getShared("Chat_Lobby_UserCreatedGame", var1, var2));
        }
    }

    public void userLeftJoinedGame(String var1, String var2) {
        User var3 = super.userList.removeAndReturnUser(var1);
        if (var3 != null && !this.noGameMessagesChatbox.getState()) {
            this.method889(var3, super.textManager.getShared("Chat_Lobby_UserJoinedGame", var1, var2));
        }
    }

    public void userLeftWatchingGame(String var1, String var2) {
        User var3 = super.userList.removeAndReturnUser(var1);
        if (var3 != null && !this.noGameMessagesChatbox.getState()) {
            this.method889(var3, super.textManager.getShared("Chat_Lobby_UserWathicngGame", var1, var2));
        }
    }

    public void usersLeftStartedGame(String var1, String var2, String var3) {
        User var4 = super.userList.removeAndReturnUser(var1);
        User var5 = super.userList.removeAndReturnUser(var2);
        if (var4 != null && var5 != null && !this.noGameMessagesChatbox.getState()) {
            String var6;
            if (var3 != null) {
                var6 = super.textManager.getShared("Chat_Lobby_UsersStartedGame", var1, var2, var3);
            } else {
                var6 = super.textManager.getShared("Chat_Lobby_UsersStartedUnnamedGame", var1, var2);
            }

            this.method890(var4, var5, var6);
        }
    }

    public void usersLeftStartedGame(String var1, String var2) {
        this.usersLeftStartedGame(var1, var2, null);
    }

    public User getSelectedUserForChallenge() {
        User var1 = super.userList.getSelectedUser();
        if (var1 == null) {
            super.chatTextArea.addMessage(super.textManager.getShared("Chat_Lobby_CantChallengeNone"));
        } else {
            if (!var1.isLocal()) {
                return var1;
            }

            super.chatTextArea.addMessage(super.textManager.getShared("Chat_Lobby_CantChallengeSelf"));
        }

        return null;
    }

    public String getSelectedNickForChallenge() {
        User var1 = this.getSelectedUserForChallenge();
        return var1 != null ? var1.getNick() : null;
    }

    public boolean[] getCheckBoxStates() {
        boolean[] var1 =
                new boolean[] {this.noJoinAndPartMessagesCheckbox.getState(), this.noGameMessagesChatbox.getState()};
        return var1;
    }

    public void setCheckBoxStates(boolean var1, boolean var2) {
        this.noJoinAndPartMessagesCheckbox.setState(var1);
        this.noGameMessagesChatbox.setState(var2);
    }

    public boolean isNoJoinPartMessages() {
        return this.noJoinAndPartMessagesCheckbox.getState();
    }

    public boolean isNoGameMessages() {
        return this.noGameMessagesChatbox.getState();
    }

    public synchronized boolean useRoundButtons() {
        if (!super.useRoundButtons()) {
            return false;
        } else {
            this.noJoinAndPartMessagesCheckbox.setBoxPixelRoundedCorners(true);
            this.noGameMessagesChatbox.setBoxPixelRoundedCorners(true);
            return true;
        }
    }

    public void resizeLayout() {
        int width = super.width / 5;
        if (width < 100) {
            width = 100;
        }

        if (width > 150) {
            width = 150;
        }

        double var2 = ((double) super.height - 100.0D) / 100.0D;
        int height = (int) (20.0D + var2 * 5.0D);
        int var5 = (int) (15.0D + var2 * 5.0D);
        if (height < 20) {
            height = 20;
        }

        if (height > 25) {
            height = 25;
        }

        if (var5 < 15) {
            var5 = 15;
        }

        if (var5 > 20) {
            var5 = 20;
        }

        int var6 = super.width - 0 - 3 - width - 0;
        int var7 = super.height - 0 - var5 - 3 - height - 2 - 0;
        int var8 = (int) (50.0D + ((double) var6 - 200.0D) / 300.0D * 70.0D);
        if (var8 < 50) {
            var8 = 50;
        }

        if (var8 > 100) {
            var8 = 100;
        }

        int var9 = var6 - 1 - var8;
        int var10 = (var6 - 2) / 2;
        super.userList.setBounds(0, 0, width, super.height - 0 - 0);
        synchronized (this) {
            if (super.gui_globaloutput == null) {
                super.chatTextArea.setBounds(0 + width + 3, 0, var6, var7);
            } else {
                super.gui_globaloutput.setBounds(0 + width + 3, 0, var6, var7);
            }
        }

        int x = 0 + width + 3;
        int y = 0 + var7 + 2;
        super.inputTextField.setBounds(x, y, var9, height);
        int var13 = 0 + width + 3 + var9 + 1;
        super.sayButton.setBounds(var13, 0 + var7 + 2, var8, height);
        this.noJoinAndPartMessagesCheckbox.setBounds(0 + width + 3, super.height - 0 - var5, var10, var5);
        this.noGameMessagesChatbox.setBounds(0 + width + 3 + var10 + 2, super.height - 0 - var5, var10, var5);
        super.signupMessage.setBounds(x, y, var13 - x + var8, height);
    }

    private void createSettingsCheckBoxes() {
        this.noJoinAndPartMessagesCheckbox =
                new ColorCheckbox(super.textManager.getShared("Chat_Lobby_NoJoinPartMessages"));
        this.add(this.noJoinAndPartMessagesCheckbox);
        this.noGameMessagesChatbox = new ColorCheckbox(super.textManager.getShared("Chat_Lobby_NoGameMessages"));
        this.add(this.noGameMessagesChatbox);
    }
}
