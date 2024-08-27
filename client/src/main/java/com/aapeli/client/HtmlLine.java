package com.aapeli.client;

import java.awt.Font;
import java.awt.Graphics;
import java.util.Vector;

class HtmlLine {

    private int anInt1366;
    private boolean aBoolean1367;
    private Vector<HtmlWord> words;
    private int nextWordStart;
    private int height;
    private final HtmlText aHtmlText1371;


    protected HtmlLine(HtmlText htmlText, Graphics graphics, int var3, boolean var4) {
        this.aHtmlText1371 = htmlText;
        this.anInt1366 = var3;
        this.aBoolean1367 = var4;
        this.words = new Vector<>();
        this.nextWordStart = 0;
        this.height = graphics.getFont().getSize();
    }

    public String toString() {
        int wordsLength = this.words.size();
        String s = "[HtmlLine: words.size=" + wordsLength + "\n";

        for (int i = 0; i < wordsLength; ++i) {
            s = s + " " + this.words.elementAt(i).toString() + "\n";
        }

        s = s + "'relatx'=" + this.nextWordStart + " 'height'=" + this.height + "]";
        return s;
    }

    protected boolean method1604(int var1) {
        return this.nextWordStart + var1 <= this.anInt1366;
    }

    protected void addWord(String text, Font font, int length) {
        if (!this.isEmpty() || text.trim().length() != 0) {
            HtmlWord word = new HtmlWord(this, text, font, this.nextWordStart, length);
            this.words.addElement(word);
            this.nextWordStart += length;
            int fontSize = font.getSize();
            if (fontSize > this.height) {
                this.height = fontSize;
            }

        }
    }

    protected boolean isEmpty() {
        return this.words.isEmpty();
    }

    protected int getHeight() {
        return this.height;
    }

    protected void draw(Graphics graphics, int x, int y) {
        int wordsCount = this.words.size();
        HtmlWord word;
        int wordsLength;
        if (this.aBoolean1367) {
            wordsLength = 0;

            for (int i = 0; i < wordsCount; ++i) {
                word = this.words.elementAt(i);
                wordsLength += word.getLength();
            }

            x += (this.anInt1366 - wordsLength) / 2;
        }

        for (wordsLength = 0; wordsLength < wordsCount; ++wordsLength) {
            word = this.words.elementAt(wordsLength);
            word.draw(graphics, x, y);
        }

    }
}
