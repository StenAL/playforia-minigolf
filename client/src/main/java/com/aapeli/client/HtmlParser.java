package com.aapeli.client;

import java.awt.Font;
import java.awt.Graphics;

class HtmlParser {

    private String rawText;
    private Graphics graphics;
    private final HtmlText htmlText;

    protected HtmlParser(HtmlText htmlText, String rawText, Graphics graphics) {
        this.htmlText = htmlText;
        this.rawText = rawText.trim();
        this.graphics = graphics;
    }

    protected String getNextWord() {
        if (this.rawText.length() == 0) {
            return null;
        } else {
            char c = this.rawText.charAt(0);
            this.rawText = this.rawText.trim();
            if (this.isWhitespace(c)) {
                return " ";
            } else {
                Font font;
                if (this.tryToConsumeTag("strong", "b")) {
                    font = this.graphics.getFont();
                    this.graphics.setFont(new Font(font.getName(), Font.BOLD, font.getSize()));
                    return this.getNextWord();
                } else if (this.tryToConsumeTag("/strong", "/b")) {
                    font = this.graphics.getFont();
                    this.graphics.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
                    return this.getNextWord();
                } else if (this.tryToConsumeTag("big")) {
                    font = this.graphics.getFont();
                    this.graphics.setFont(new Font(font.getName(), font.getStyle(), font.getSize() + 5));
                    return this.getNextWord();
                } else if (this.tryToConsumeTag("/big")) {
                    font = this.graphics.getFont();
                    this.graphics.setFont(new Font(font.getName(), font.getStyle(), font.getSize() - 5));
                    return this.getNextWord();
                } else if (this.tryToConsumeTag("small")) {
                    font = this.graphics.getFont();
                    this.graphics.setFont(new Font(font.getName(), font.getStyle(), font.getSize() - 3));
                    return this.getNextWord();
                } else if (this.tryToConsumeTag("/small")) {
                    font = this.graphics.getFont();
                    this.graphics.setFont(new Font(font.getName(), font.getStyle(), font.getSize() + 3));
                    return this.getNextWord();
                } else if (this.tryToConsumeTag("br", "br/", "br /")) {
                    return "<br>";
                } else if (this.tryToConsumeTag("center")) {
                    return "<center>";
                } else if (this.tryToConsumeTag("/center")) {
                    return "</center>";
                } else {
                    int wordLength = this.textLengthUntilNextWord();
                    String word = this.rawText.substring(0, wordLength);
                    this.rawText = this.rawText.substring(wordLength);
                    return word;
                }
            }
        }
    }

    private int textLengthUntilNextWord() {
        int textLength = this.rawText.length();

        for (int i = 1; i < textLength; ++i) {
            char c = this.rawText.charAt(i);
            if (this.isWhitespace(c) || c == '<') {
                return i;
            }
        }

        return textLength;
    }

    private boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r';
    }

    private boolean tryToConsumeTag(String tag) {
        return this.tryToConsumeTag(new String[] {"<" + tag + ">"});
    }

    private boolean tryToConsumeTag(String tag1, String tag2) {
        return this.tryToConsumeTag(new String[] {"<" + tag1 + ">", "<" + tag2 + ">"});
    }

    private boolean tryToConsumeTag(String tag1, String tag2, String tag3) {
        return this.tryToConsumeTag(new String[] {"<" + tag1 + ">", "<" + tag2 + ">", "<" + tag3 + ">"});
    }

    private boolean tryToConsumeTag(String[] tags) {
        String rawText = this.rawText.toLowerCase();

        for (String tag : tags) {
            if (rawText.startsWith(tag)) {
                int l = tag.length();
                this.rawText = this.rawText.substring(l);
                return true;
            }
        }

        return false;
    }
}
