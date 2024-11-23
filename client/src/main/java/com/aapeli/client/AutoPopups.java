package com.aapeli.client;

import com.aapeli.applet.AApplet;
import com.aapeli.colorgui.ColorButton;

public class AutoPopups {

    private SignUpHandler signUpHandler;
    private TellFriendHandler tellFriendHandler;
    private long aLong1330;
    private static final String aString1331 = "facebook";

    public AutoPopups(AApplet var1) {
        if (!var1.param.getSiteName().equalsIgnoreCase("facebook")) {
            this.signUpHandler = new SignUpHandler(var1);
            this.tellFriendHandler = new TellFriendHandler(var1.param, var1.textManager, var1.imageManager);
        }

        this.aLong1330 = 0L;
    }

    public AutoPopups(Parameters var1, TextManager var2, ImageManager var3) {
        if (!var1.getSiteName().equalsIgnoreCase("facebook")) {
            this.tellFriendHandler = new TellFriendHandler(var1, var2, var3);
        }
    }

    public ColorButton getTellFriendButton() {
        return this.tellFriendHandler != null ? this.tellFriendHandler.method1699() : null;
    }

    public void gameFinished(boolean var1) {
        synchronized (this) {
            if (this.method1561()) {
                if (this.signUpHandler != null && this.signUpHandler.method1599(var1)) {
                    this.method1562();
                } else if (this.tellFriendHandler != null && this.tellFriendHandler.method1700()) {
                    this.method1562();
                }
            }
        }
    }

    public void personalRecord() {
        synchronized (this) {
            if (this.method1561()) {
                if (this.signUpHandler != null && this.signUpHandler.method1600()) {
                    this.method1562();
                }
            }
        }
    }

    public void rankingChanged(int var1, int var2) {
        synchronized (this) {
            if (this.method1561()) {
                if (this.signUpHandler != null && this.signUpHandler.method1601(var1, var2)) {
                    this.method1562();
                }
            }
        }
    }

    public void close() {
        synchronized (this) {
            if (this.signUpHandler != null) {
                this.signUpHandler.method1602();
            }

            if (this.tellFriendHandler != null) {
                this.tellFriendHandler.method1701();
            }
        }
    }

    private boolean method1561() {
        return System.currentTimeMillis() > this.aLong1330 + 15000L;
    }

    private void method1562() {
        this.aLong1330 = System.currentTimeMillis();
    }
}
