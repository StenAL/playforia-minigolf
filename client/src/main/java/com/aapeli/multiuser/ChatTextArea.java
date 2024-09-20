package com.aapeli.multiuser;

import com.aapeli.client.BadWordFilter;
import com.aapeli.client.TextManager;
import com.aapeli.colorgui.ColorTextArea;
import java.awt.Font;
import java.util.Hashtable;

public class ChatTextArea extends ColorTextArea {

    public static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);
    public static final Font SMALL_FONT = new Font("Dialog", Font.PLAIN, 11);
    private TextManager textManager;
    private BadWordFilter badWordFilter;
    private Hashtable<String, Integer> aHashtable4730;

    public ChatTextArea(TextManager textManager, int width, int height) {
        this(textManager, null, width, height, null);
    }

    public ChatTextArea(TextManager textManager, int width, int height, Font font) {
        this(textManager, null, width, height, font);
    }

    public ChatTextArea(TextManager textManager, BadWordFilter badWordFilter, int width, int height) {
        this(textManager, badWordFilter, width, height, null);
    }

    public ChatTextArea(TextManager textManager, BadWordFilter badWordFilter, int width, int height, Font font) {
        super(width, height, font != null ? font : DEFAULT_FONT);
        this.textManager = textManager;
        this.badWordFilter = badWordFilter;
        this.aHashtable4730 = new Hashtable<>();
    }

    public void addOwnSay(String var1, String var2) {
        this.method856(3, var1, var2, true);
    }

    public void addOwnSayPrivately(String var1, String var2, String var3) {
        this.method857(3, var1, var2, var3, true);
    }

    public void addSay(String var1, String var2) {
        this.method856(0, var1, var2, false);
    }

    public void addSayPrivately(String var1, String var2, String var3) {
        this.method857(5, var1, var2, var3, false);
    }

    public void addJoinMessage(String var1) {
        this.addMessage(2, var1);
    }

    public void addPartMessage(String var1) {
        this.addMessage(1, var1);
    }

    public void addStartedGameMessage(String var1) {
        this.addMessage(7, var1);
    }

    public void addSheriffSay(String text) {
        this.addImportantLine(6, this.textManager.getShared("Chat_SheriffSay", text));
    }

    public void addServerSay(String text) {
        this.addText(6, this.textManager.getShared("Chat_ServerSay", text));
    }

    public void addLocalizedServerSay(String text) {
        this.addText(6, text);
    }

    public void addBroadcastMessage(String text) {
        this.addImportantLine(6, this.textManager.getShared("Chat_ServerBroadcast", text));
    }

    public void addWelcomeMessage(String text) {
        if (text != null) {
            this.addText(6, text);
        }
    }

    public void addPlainMessage(String text) {
        this.addText(7, text);
    }

    public void addMessage(String var1) {
        this.addMessage(7, var1);
    }

    public void addHighlightMessage(String var1) {
        this.addMessage(6, var1);
    }

    public void addErrorMessage(String var1) {
        this.addMessage(1, var1);
    }

    public void addFloodMessage() {
        this.addMessage(7, this.textManager.getShared("Chat_MessageFlood"));
    }

    public void addPrivateMessageUserLeftMessage(String var1) {
        this.addMessage(6, this.textManager.getShared("Chat_MessagePrivateMessageUserLeft", var1));
    }

    public void setUserColor(String var1, int var2) {
        this.aHashtable4730.put(var1, var2);
    }

    public void removeUserColor(String var1) {
        this.aHashtable4730.remove(var1);
    }

    public TextManager getTextManager() {
        return this.textManager;
    }

    public BadWordFilter getBadWordFilter() {
        return this.badWordFilter;
    }

    private void addMessage(int var1, String text) {
        this.addText(this.method858(var1), this.textManager.getShared("Chat_Message", text));
    }

    private void method856(int var1, String var2, String var3, boolean var4) {
        var3 = this.method860(var3, var4);
        if (var3.length() > 4 && var3.toLowerCase().startsWith("/me ")) {
            this.addText(
                    this.method859(var2, var1),
                    this.textManager.getShared("Chat_UserAction", var2, var3.substring(4)),
                    var4);
        } else {
            this.addText(this.method859(var2, var1), this.textManager.getShared("Chat_UserSay", var2, var3), var4);
        }
    }

    private void method857(int var1, String var2, String var3, String var4, boolean var5) {
        var4 = this.method860(var4, var5);
        this.addText(
                this.method859(var2, var1), this.textManager.getShared("Chat_UserSayPrivate", var2, var3, var4), var5);
    }

    private int method858(int var1) {
        return this.aHashtable4730.size() == 0 ? var1 : 7;
    }

    private int method859(String var1, int var2) {
        Integer var3 = this.aHashtable4730.get(var1);
        return var3 == null ? var2 : var3;
    }

    private String method860(String var1, boolean var2) {
        var1 = this.method861(var1);
        var1 = this.method862(var1, var2);
        return var1;
    }

    private String method861(String var1) {
        char[] var2 = var1.toCharArray();
        boolean var3 = false;
        int var4 = var2.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            if (var2[var5] < 32
                    || var2[var5] == 127
                    || var2[var5] >= 128 && var2[var5] <= 159
                    || var2[var5] == 8232
                    || var2[var5] == 8233
                    || var2[var5] == '\ufff9'
                    || var2[var5] == '\ufffa'
                    || var2[var5] == '\ufffb'
                    || var2[var5] == 8206
                    || var2[var5] == 8207
                    || var2[var5] == 8234
                    || var2[var5] == 8238
                    || var2[var5] == '\uf0da') {
                var2[var5] = 32;
                var3 = true;
            }

            if (var2[var5] == '\uf0da') {
                var2[var5] = 32;
                var3 = true;
            }

            if (var2[var5] == 304) {
                var2[var5] = 73;
                var3 = true;
            }
        }

        if (var3) {
            var1 = new String(var2);
        }

        return var1;
    }

    private String method862(String var1, boolean var2) {
        if (this.badWordFilter != null && !var2) {
            var1 = this.badWordFilter.filter(var1);
        }

        return var1;
    }
}
