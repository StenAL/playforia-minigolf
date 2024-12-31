package com.aapeli.client;

import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

class HtmlLine {

    private int anInt1366;
    private boolean aBoolean1367;
    private List<HtmlWord> words;
    private int nextWordStart;
    private int height;

    protected HtmlLine(Graphics graphics, int var3, boolean var4) {
        this.anInt1366 = var3;
        this.aBoolean1367 = var4;
        this.words = new ArrayList<>();
        this.nextWordStart = 0;
        this.height = graphics.getFont().getSize();
    }

    public String toString() {
        int wordsLength = this.words.size();
        String s = "[HtmlLine: words.size=" + wordsLength + "\n";

        for (HtmlWord word : this.words) {
            s = s + " " + word.toString() + "\n";
        }

        s = s + "'relatx'=" + this.nextWordStart + " 'height'=" + this.height + "]";
        return s;
    }

    protected boolean method1604(int var1) {
        return this.nextWordStart + var1 <= this.anInt1366;
    }

    protected void addWord(String text, Font font, int length) {
        if (!this.isEmpty() || text.trim().length() != 0) {
            HtmlWord word = new HtmlWord(text, font, this.nextWordStart, length);
            this.words.add(word);
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
        int wordsLength;
        if (this.aBoolean1367) {
            wordsLength = 0;

            for (HtmlWord htmlWord : this.words) {
                wordsLength += htmlWord.getLength();
            }

            x += (this.anInt1366 - wordsLength) / 2;
        }

        for (wordsLength = 0; wordsLength < wordsCount; ++wordsLength) {
            HtmlWord word = this.words.get(wordsLength);
            word.draw(graphics, x, y);
        }
    }
}
