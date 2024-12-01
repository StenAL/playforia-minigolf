package com.aapeli.client;

import com.aapeli.colorgui.ColorButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.security.MessageDigest;

class TellFriendHandler implements ActionListener {

    private static final Color aColor1499 = new Color(64, 160, 255);
    private Parameters aParameters1500;
    private TextManager aTextManager1501;
    private ImageManager anImageManager1502;
    private MessageDigest aMessageDigest1503;
    private String aString1504;
    private String aString1505;
    private int anInt1506;
    private int anInt1507;
    private TellFriendWindow tellFriendWindow;

    protected TellFriendHandler(Parameters var1, TextManager var2, ImageManager var3) {
        this.aParameters1500 = var1;
        this.aTextManager1501 = var2;
        this.anImageManager1502 = var3;
        this.anInt1506 = var1.getTellFriend() ? 1 : 2;
        this.anInt1507 = 0;

        try {
            this.aMessageDigest1503 = MessageDigest.getInstance("MD5");
        } catch (Exception | Error e) {
            this.anInt1506 = 0;
        }

        this.aString1504 = var1.getTellFriendPage();
        this.aString1505 = var1.getTellFriendTarget();
        if (this.aString1504 == null || this.aString1505 == null) {
            this.anInt1506 = 0;
        }
    }

    public void actionPerformed(ActionEvent var1) {
        if (this.tellFriendWindow == null) {
            this.method1704();
        } else {
            this.tellFriendWindow.toFront();
        }
    }

    protected ColorButton method1699() {
        if (this.anInt1506 != 0 && this.aParameters1500.getSession() == null) {
            this.anInt1506 = 0;
        }

        if (this.anInt1506 == 0) {
            return null;
        } else {
            ColorButton var1 = new ColorButton(this.aTextManager1501.getShared("TellFriend_ExtButton"));
            var1.setBackground(aColor1499);
            var1.setForeground(Color.black);
            var1.setSize(90, 20);
            var1.addActionListener(this);
            return var1;
        }
    }

    protected boolean method1700() {
        if (this.anInt1506 != 1) {
            return false;
        } else if (this.aParameters1500.getSession() == null) {
            this.anInt1506 = 0;
            return false;
        } else {
            ++this.anInt1507;
            if (this.anInt1507 == 3) {
                try {
                    this.method1704();
                    return true;
                } catch (Exception var2) {
                }
            }

            return false;
        }
    }

    protected void method1701() {
        try {
            if (this.tellFriendWindow != null) {
                this.tellFriendWindow.close();
            }
        } catch (Exception var2) {
        }
    }

    protected boolean method1702(String[] var1, String var2, int var3, int var4, int var5) {
        try {
            int var6 = var2.length();
            if (var6 > 1500) {
                var2 = var2.substring(0, 1497).trim() + "...";
            }

            String var7 = this.method1705(var1, var2);
            var7 = method1708(var7 + "Voi");
            byte[] var8 = this.method1706(var7 + (char) var3 + (char) var4 + (char) var4 + (char) var5);
            String var9 = this.method1707(var8);
            String var10 = "session=" + method1708(this.aParameters1500.getSession()) + "&";
            var6 = var1.length;

            for (int var11 = 0; var11 < var6; ++var11) {
                if (var1[var11] != null) {
                    var10 = var10 + "emails[]=" + method1708(var1[var11]) + "&";
                }
            }

            if (var2 != null) {
                var10 = var10 + "message=" + method1708(var2) + "&";
            }

            var10 = var10 + "hash=" + var9;
            URL var14 = new URL(this.aString1504 + "?" + var10);
            this.aParameters1500.getAppletContext().showDocument(var14, this.aString1505);
            return true;
        } catch (Exception | Error e) {
        }

        return false;
    }

    protected void method1703() {
        this.tellFriendWindow = null;
    }

    private void method1704() {
        this.tellFriendWindow = new TellFriendWindow(this.aTextManager1501, this.anImageManager1502, this);
        this.tellFriendWindow.method241(this.aParameters1500.getApplet());
        this.anInt1506 = 2;
    }

    private String method1705(String[] var1, String var2) {
        String var3 = "";
        for (String text : var1) {
            if (text != null) {
                var3 = var3 + text;
            }
        }

        if (var2 != null) {
            var3 = var3 + var2;
        }

        var3 = var3 + "Miksei";
        return var3 + "Aita".replace('t', 'n');
    }

    private byte[] method1706(String var1) {
        this.aMessageDigest1503.reset();
        this.aMessageDigest1503.update((var1 + "Perjantai").getBytes());
        return this.aMessageDigest1503.digest();
    }

    private String method1707(byte[] var1) {
        int var2 = var1.length;
        StringBuffer var3 = new StringBuffer(var2 * 2);

        for (byte b : var1) {
            int var5 = b >= 0 ? b : 256 + b;
            if (var5 < 16) {
                var3.append('0');
            }

            var3.append(Integer.toHexString(var5));
        }

        return var3.toString();
    }

    private static String method1708(String var0) {
        char[] var1 = var0.toCharArray();
        StringBuffer var2 = new StringBuffer(var1.length);

        for (char c : var1) {
            if ((c < 97 || c > 122) && (c < 65 || c > 90) && (c < 48 || c > 57)) {
                char var4 = c;
                if (var4 > 255) {
                    var4 = 255;
                }

                var2.append('%');
                var2.append(Integer.toHexString(var4 / 16));
                var2.append(Integer.toHexString(var4 % 16));
            } else {
                var2.append(c);
            }
        }

        return var2.toString();
    }
}
