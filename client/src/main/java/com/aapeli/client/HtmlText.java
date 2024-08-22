package com.aapeli.client;

import java.awt.Graphics;
import java.util.Vector;

public class HtmlText {

    private Vector<HtmlLine> lines;
    public HtmlText(Graphics graphics, int var2, String var3) {
        this.lines = this.method1589(graphics, var2, var3);
    }

    public String toString() {
        int var1 = this.lines.size();
        String var2 = "[HtmlText: lines.size=" + var1 + "\n";

        for (int var3 = 0; var3 < var1; ++var3) {
            var2 = var2 + " " + this.lines.elementAt(var3).toString() + "\n";
        }

        var2 = var2 + "]";
        return var2;
    }

    public int print(Graphics var1, int var2, int var3) {
        int var4 = this.lines.size();
        int var5 = 0;

        for (int var7 = 0; var7 < var4; ++var7) {
            HtmlLine var6 = this.lines.elementAt(var7);
            var5 = var6.getHeight() + 5;
            if (var7 > 0) {
                var3 += var5;
            }

            var6.draw(var1, var2, var3);
        }

        return var3 + var5;
    }

    private Vector<HtmlLine> method1589(Graphics var1, int var2, String var3) {
        Class81 var4 = new Class81(this, var3, var1);
        Vector<HtmlLine> var5 = new Vector<>();
        boolean var6 = false;
        HtmlLine line = new HtmlLine(this, var1, var2, var6);

        String var8;
        while ((var8 = var4.method1611()) != null) {
            if (var8.equals("<br>")) {
                var5.addElement(line);
                line = new HtmlLine(this, var1, var2, var6);
            } else if (var8.equals("<center>")) {
                var6 = true;
                line = this.method1590(line, var5, var1, var2, var6);
            } else if (var8.equals("</center>")) {
                var6 = false;
                line = this.method1590(line, var5, var1, var2, var6);
            } else {
                int var9 = var1.getFontMetrics().stringWidth(var8);
                if (!line.method1604(var9)) {
                    var5.addElement(line);
                    line = new HtmlLine(this, var1, var2, var6);
                }

                line.addWord(var8, var1.getFont(), var9);
            }
        }

        this.method1591(line, var5);
        return var5;
    }

    private HtmlLine method1590(HtmlLine var1, Vector<HtmlLine> var2, Graphics var3, int var4, boolean var5) {
        this.method1591(var1, var2);
        return new HtmlLine(this, var3, var4, var5);
    }

    private void method1591(HtmlLine var1, Vector<HtmlLine> var2) {
        if (!var1.isEmpty()) {
            var2.addElement(var1);
        }

    }
}
