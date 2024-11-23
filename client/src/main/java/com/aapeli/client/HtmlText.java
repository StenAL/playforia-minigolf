package com.aapeli.client;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class HtmlText {

    private List<HtmlLine> lines;

    public HtmlText(Graphics graphics, int var2, String text) {
        this.lines = this.createLines(graphics, var2, text);
    }

    public String toString() {
        int var1 = this.lines.size();
        String var2 = "[HtmlText: lines.size=" + var1 + "\n";

        for (HtmlLine line : this.lines) {
            var2 = var2 + " " + line.toString() + "\n";
        }

        var2 = var2 + "]";
        return var2;
    }

    public int print(Graphics var1, int var2, int var3) {
        int var4 = this.lines.size();
        int var5 = 0;

        for (int var7 = 0; var7 < var4; ++var7) {
            HtmlLine var6 = this.lines.get(var7);
            var5 = var6.getHeight() + 5;
            if (var7 > 0) {
                var3 += var5;
            }

            var6.draw(var1, var2, var3);
        }

        return var3 + var5;
    }

    private List<HtmlLine> createLines(Graphics g, int var2, String text) {
        HtmlParser parser = new HtmlParser(this, text, g);
        List<HtmlLine> lines = new ArrayList<>();
        boolean var6 = false;
        HtmlLine line = new HtmlLine(this, g, var2, var6);

        String word;
        while ((word = parser.getNextWord()) != null) {
            if (word.equals("<br>")) {
                lines.add(line);
                line = new HtmlLine(this, g, var2, var6);
            } else if (word.equals("<center>")) {
                var6 = true;
                line = this.method1590(line, lines, g, var2, var6);
            } else if (word.equals("</center>")) {
                var6 = false;
                line = this.method1590(line, lines, g, var2, var6);
            } else {
                int var9 = g.getFontMetrics().stringWidth(word);
                if (!line.method1604(var9)) {
                    lines.add(line);
                    line = new HtmlLine(this, g, var2, var6);
                }

                line.addWord(word, g.getFont(), var9);
            }
        }

        this.method1591(line, lines);
        return lines;
    }

    private HtmlLine method1590(HtmlLine var1, List<HtmlLine> var2, Graphics var3, int var4, boolean var5) {
        this.method1591(var1, var2);
        return new HtmlLine(this, var3, var4, var5);
    }

    private void method1591(HtmlLine var1, List<HtmlLine> var2) {
        if (!var1.isEmpty()) {
            var2.add(var1);
        }
    }
}
