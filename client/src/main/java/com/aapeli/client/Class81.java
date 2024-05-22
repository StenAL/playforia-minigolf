package com.aapeli.client;

import com.aapeli.client.HtmlText;

import java.awt.Font;
import java.awt.Graphics;

class Class81 {

    private String aString1379;
    private Graphics aGraphics1380;
    private final HtmlText aHtmlText1381;

    protected Class81(HtmlText var1, String var2, Graphics var3) {
        this.aHtmlText1381 = var1;
        this.aString1379 = var2.trim();
        this.aGraphics1380 = var3;
    }

    protected String method1611() {
        if (this.aString1379.length() == 0) {
            return null;
        } else {
            char var1 = this.aString1379.charAt(0);
            this.aString1379 = this.aString1379.trim();
            if (this.method1613(var1)) {
                return " ";
            } else {
                Font var4;
                if (this.method1615("strong", "b")) {
                    var4 = this.aGraphics1380.getFont();
                    this.aGraphics1380.setFont(new Font(var4.getName(), 1, var4.getSize()));
                    return this.method1611();
                } else if (this.method1615("/strong", "/b")) {
                    var4 = this.aGraphics1380.getFont();
                    this.aGraphics1380.setFont(new Font(var4.getName(), 0, var4.getSize()));
                    return this.method1611();
                } else if (this.method1614("big")) {
                    var4 = this.aGraphics1380.getFont();
                    this.aGraphics1380.setFont(new Font(var4.getName(), var4.getStyle(), var4.getSize() + 5));
                    return this.method1611();
                } else if (this.method1614("/big")) {
                    var4 = this.aGraphics1380.getFont();
                    this.aGraphics1380.setFont(new Font(var4.getName(), var4.getStyle(), var4.getSize() - 5));
                    return this.method1611();
                } else if (this.method1614("small")) {
                    var4 = this.aGraphics1380.getFont();
                    this.aGraphics1380.setFont(new Font(var4.getName(), var4.getStyle(), var4.getSize() - 3));
                    return this.method1611();
                } else if (this.method1614("/small")) {
                    var4 = this.aGraphics1380.getFont();
                    this.aGraphics1380.setFont(new Font(var4.getName(), var4.getStyle(), var4.getSize() + 3));
                    return this.method1611();
                } else if (this.method1616("br", "br/", "br /")) {
                    return "<br>";
                } else if (this.method1614("center")) {
                    return "<center>";
                } else if (this.method1614("/center")) {
                    return "</center>";
                } else {
                    int var2 = this.method1612();
                    String var3 = this.aString1379.substring(0, var2);
                    this.aString1379 = this.aString1379.substring(var2);
                    return var3;
                }
            }
        }
    }

    private int method1612() {
        int var1 = this.aString1379.length();

        for (int var3 = 1; var3 < var1; ++var3) {
            char var2 = this.aString1379.charAt(var3);
            if (this.method1613(var2) || var2 == 60) {
                return var3;
            }
        }

        return var1;
    }

    private boolean method1613(char var1) {
        return var1 == 32 || var1 == 9 || var1 == 10 || var1 == 13;
    }

    private boolean method1614(String var1) {
        return this.method1617(new String[]{"<" + var1 + ">"});
    }

    private boolean method1615(String var1, String var2) {
        return this.method1617(new String[]{"<" + var1 + ">", "<" + var2 + ">"});
    }

    private boolean method1616(String var1, String var2, String var3) {
        return this.method1617(new String[]{"<" + var1 + ">", "<" + var2 + ">", "<" + var3 + ">"});
    }

    private boolean method1617(String[] var1) {
        String var2 = this.aString1379.toLowerCase();

        for (int var3 = 0; var3 < var1.length; ++var3) {
            if (var2.startsWith(var1[var3])) {
                int var4 = var1[var3].length();
                this.aString1379 = this.aString1379.substring(var4);
                return true;
            }
        }

        return false;
    }
}
