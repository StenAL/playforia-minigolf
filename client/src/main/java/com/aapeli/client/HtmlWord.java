package com.aapeli.client;

import java.awt.Font;
import java.awt.Graphics;

class HtmlWord {

    private String word;
    private Font font;
    private int start;
    private int length;
    private final HtmlLine htmlLine;

    protected HtmlWord(HtmlLine htmlLine, String word, Font font, int start, int length) {
        this.htmlLine = htmlLine;
        this.word = word;
        this.font = font;
        this.start = start;
        this.length = length;
    }

    public String toString() {
        return "[HtmlWord: 'word'=\""
                + this.word
                + "\" 'font'=\""
                + this.font.toString()
                + "\" 'relx'="
                + this.start
                + "]";
    }

    protected int getLength() {
        return this.length;
    }

    protected void draw(Graphics graphics, int x, int y) {
        graphics.setFont(this.font);
        graphics.drawString(this.word, x + this.start, y);
    }
}
