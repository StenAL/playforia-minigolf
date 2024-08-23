package com.aapeli.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class StringDraw {

    public static final int ALIGN_LEFT = -1;
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int ALIGN_CENTER_LEFT_ALWAYS_VISIBLE = -2;
    public static final int ALIGN_CENTER_RIGHT_ALWAYS_VISIBLE = 2;


    public static int drawString(Graphics g, String text, int x, int y, int alignment) {
        return drawOutlinedString(g, null, text, x, y, alignment);
    }

    public static int drawOutlinedString(Graphics g, Color outlineColor, String text, int x, int y, int alignment) {
        int textWidth = getStringWidth(g, text);
        if (alignment == ALIGN_CENTER || alignment == ALIGN_CENTER_LEFT_ALWAYS_VISIBLE || alignment == ALIGN_CENTER_RIGHT_ALWAYS_VISIBLE) {
            x -= textWidth / 2;
        }

        if (alignment == ALIGN_RIGHT) {
            x -= textWidth;
        }

        if (alignment == ALIGN_CENTER_LEFT_ALWAYS_VISIBLE || alignment == ALIGN_CENTER_RIGHT_ALWAYS_VISIBLE) {
            Rectangle bounds = g.getClipBounds();
            if (bounds != null) {
                if (alignment == ALIGN_CENTER_LEFT_ALWAYS_VISIBLE) {
                    if (outlineColor == null && x < 0) {
                        x = 0;
                    } else if (outlineColor != null && x < 1) {
                        x = 1;
                    }
                } else if (outlineColor == null && x + textWidth >= bounds.width) {
                    x = bounds.width - 1 - textWidth;
                } else if (outlineColor != null && x + textWidth >= bounds.width - 1) {
                    x = bounds.width - 2 - textWidth;
                }
            }
        }

        if (outlineColor != null) {
            Color oldColour = g.getColor();
            g.setColor(outlineColor);
            g.drawString(text, x - 1, y);
            g.drawString(text, x + 1, y);
            g.drawString(text, x, y - 1);
            g.drawString(text, x, y + 1);
            g.setColor(oldColour);
        }

        g.drawString(text, x, y);
        return textWidth;
    }

    public static int[] drawStringWithMaxWidth(Graphics g, String text, int var2, int var3, int var4, int var5) {
        return drawOutlinedStringWithMaxWidth(g, null, text, var2, var3, var4, var5);
    }

    public static int[] drawOutlinedStringWithMaxWidth(Graphics g, Color outlineColor, String text, int x, int y, int alignment, int maxWidth) {
        Font font = g.getFont();
        FontMetrics fontMetrics = g.getFontMetrics(font);
        List<String> lines = createLines(fontMetrics, text, maxWidth);
        int fontSize = font.getSize();
        int lineHeight = fontSize + (fontSize + 4) / 5;
        if (outlineColor != null) {
            lineHeight += 2;
        }

        int[] linesData = new int[]{lines.size(), 0, 0}; // 0 == number of lines, 1 == height, 2 == width
        linesData[1] = linesData[0] * lineHeight;
        linesData[2] = 0;

        for (String line: lines) {
            int lineWidth = drawOutlinedString(g, outlineColor, line, x, y, alignment);
            if (lineWidth > linesData[2]) {
                linesData[2] = lineWidth;
            }

            y += lineHeight;
        }

        return linesData;
    }

    public static int drawString(Graphics g, String text, int x, int y, int alignment, int maxWidth) {
        int[] linesData = drawOutlinedStringWithMaxWidth(g, null, text, x, y, alignment, maxWidth);
        return linesData[2];
    }

    public static int getStringWidth(Graphics g, String text) {
        return getStringWidth(g, g.getFont(), text);
    }

    public static int getStringWidth(Graphics g, Font font, String text) {
        return getStringWidth(g.getFontMetrics(font), text);
    }

    public static int getStringWidth(Component component, Font font, String text) {
        return getStringWidth(component.getFontMetrics(font), text);
    }

    public static int getStringWidth(FontMetrics fontMetrics, String text) {
        return fontMetrics.stringWidth(text);
    }

    public static List<String> createLines(Graphics g, String text, int maximumWidth) {
        return createLines(g, g.getFont(), text, maximumWidth);
    }

    public static List<String> createLines(Graphics g, Font font, String text, int maximumWidth) {
        return createLines(g.getFontMetrics(font), text, maximumWidth);
    }

    public static List<String> createLines(Component component, Font font, String text, int maximumWidth) {
        return createLines(component.getFontMetrics(font), text, maximumWidth);
    }

    public static List<String> createLines(FontMetrics fontMetrics, String text, int maximumWidth) {
        List<String> lines = new ArrayList<>();
        createLinesPrivate(lines, text, fontMetrics, maximumWidth);
        return lines;
    }

    private static void createLinesPrivate(List<String> lines, String text, FontMetrics fontMetrics, int maximumWidth) {
        String line = text;
        int linebreak = text.indexOf('\n');
        if (linebreak >= 0) {
            line = text.substring(0, linebreak);
        }

        boolean lineIsEmpty = false;

        while (!lineIsEmpty && getStringWidth(fontMetrics, line) > maximumWidth) {
            String var5 = line;
            line = splitAtSpace(line);
            if (line.length() == 0) {
                line = var5;
                lineIsEmpty = true;
            }
        }

        lines.add(line);
        int l = line.length();
        if (l < text.length()) {
            String newText = text.substring(l);
            if (Character.isWhitespace(newText.charAt(0))) {
                newText = newText.substring(1);
            }

            if (newText.length() > 0) {
                createLinesPrivate(lines, newText, fontMetrics, maximumWidth);
            }
        }

    }

    private static String splitAtSpace(String line) {
        int spaceLocation = line.lastIndexOf(' ');
        if (spaceLocation == -1) {
            spaceLocation = line.length() - 1;
        }

        return line.substring(0, spaceLocation);
    }
}
