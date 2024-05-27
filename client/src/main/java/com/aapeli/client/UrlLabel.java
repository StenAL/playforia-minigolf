package com.aapeli.client;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlLabel extends IPanel implements MouseListener {

    public static final int ALIGN_LEFT = -1;
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int TARGET_SAME = 0;
    public static final int TARGET_NEW = 1;
    private static final String[] urlTargets = new String[2];
    private static final Cursor defaultCursor;
    private static final Cursor handCursor;
    private static final Font fontDialog11;
    private Applet applet;
    private Font currentFont;
    private URL url;
    private String text;
    private String linkPlaceholderText;
    private String suffix;
    private int prefixWidth;
    private int linkTextWidth;
    private int textWidth;
    private int alignment;
    private Color backgroundColor;
    private Color foregroundMainColor;
    private Color foregroundSecondaryColor;
    private Image backgroundImage;
    private int backgroundImageOffsetX;
    private int backgroundImageOffsetY;
    private String urlTarget;
    private static final String aString3247 = "Dialog";


    public UrlLabel(Applet applet) {
        this(applet, fontDialog11, null, null);
    }

    public UrlLabel(Applet applet, String var2, String var3) {
        this(applet, fontDialog11, var2, var3);
    }

    public UrlLabel(Applet applet, Font font, String var3, String var4) {
        this.applet = applet;
        this.backgroundColor = null;
        this.foregroundMainColor = Color.black;
        this.foregroundSecondaryColor = Color.blue;
        this.backgroundImage = null;
        this.alignment = -1;
        this.urlTarget = "_blank";
        this.currentFont = font;
        this.setText(var3, var4);
    }

    public UrlLabel(Applet applet, String var2, String var3, int width, int height) {
        this(applet, fontDialog11, var2, var3);
        this.setSize(width, height);
    }

    public void update(Graphics graphics) {
        Dimension size = this.getSize();
        int width = size.width;
        int height = size.height;
        if (this.backgroundImage != null) {
            graphics.drawImage(this.backgroundImage, 0, 0, width, height, this.backgroundImageOffsetX, this.backgroundImageOffsetY, this.backgroundImageOffsetX + width, this.backgroundImageOffsetY + height, this);
        } else if (!this.drawBackgroundImage(graphics)) {
            graphics.setColor(this.backgroundColor != null ? this.backgroundColor : this.getBackground());
            graphics.fillRect(0, 0, width, height);
        }

        if (this.text != null) {
            int x = 2;
            int fontSize = this.currentFont.getSize();
            int y = height / 2 + fontSize * 3 / 8;
            int var8 = height / 2 + fontSize / 2;
            if (this.alignment == 0) {
                x = width / 2 - this.textWidth / 2;
            }

            if (this.alignment == 1) {
                x = width - 2 - this.textWidth;
            }

            if (this.linkPlaceholderText != null && this.suffix != null) {
                graphics.setColor(this.foregroundSecondaryColor);
                graphics.drawLine(x + this.prefixWidth, var8, x + this.prefixWidth + this.linkTextWidth, var8);
            }

            graphics.setFont(this.currentFont);
            graphics.setColor(this.foregroundMainColor);
            graphics.drawString(this.text, x, y);
            if (this.linkPlaceholderText != null && this.suffix != null) {
                graphics.setColor(this.foregroundSecondaryColor);
                graphics.drawString(this.linkPlaceholderText, x + this.prefixWidth, y);
                graphics.setColor(this.foregroundMainColor);
                graphics.drawString(this.suffix, x + this.prefixWidth + this.linkTextWidth, y);
            }

        }
    }

    public void mouseEntered(MouseEvent var1) {
    }

    public void mouseExited(MouseEvent var1) {
    }

    public void mousePressed(MouseEvent var1) {
    }

    public void mouseReleased(MouseEvent var1) {
        this.applet.getAppletContext().showDocument(this.url, this.urlTarget);
    }

    public void mouseClicked(MouseEvent var1) {
    }

    public void setText(String text, String url) {
        if (text == null) {
            this.text = this.linkPlaceholderText = this.suffix = null;
            this.url = null;
            this.setCursor(defaultCursor);
            this.repaint();
        } else {
            this.url = null;
            if (url != null) {
                try {
                    this.url = new URL(url);
                } catch (MalformedURLException e) {
                    ;
                }
            }

            FontMetrics fontMetrics = this.applet.getFontMetrics(this.currentFont);
            int linkTextStart = text.indexOf('<');
            int linkTextEnd = text.indexOf('>');
            if (this.url != null && linkTextStart != -1 && linkTextEnd >= linkTextStart) {
                this.text = text.substring(0, linkTextStart);
                this.linkPlaceholderText = text.substring(linkTextStart + 1, linkTextEnd);
                this.suffix = text.substring(linkTextEnd + 1);
                this.prefixWidth = fontMetrics.stringWidth(this.text);
                this.linkTextWidth = fontMetrics.stringWidth(this.linkPlaceholderText);
                this.textWidth = this.prefixWidth + this.linkTextWidth + fontMetrics.stringWidth(this.suffix);
                this.setCursor(handCursor);
                this.removeMouseListener(this);
                this.addMouseListener(this);
                this.repaint();
            } else {
                this.text = text;
                this.textWidth = fontMetrics.stringWidth(this.text);
                this.linkPlaceholderText = this.suffix = null;
                this.setCursor(defaultCursor);
                this.repaint();
            }
        }
    }

    public void setAlign(int alignment) {
        this.alignment = alignment;
        this.repaint();
    }

    public void setForeground(Color color) {
        if (color != null) {
            int r = color.getRed();
            int g = color.getGreen();
            int b = color.getBlue();
            Color secondary = new Color((r * 3 + 0) / 4, (g * 3 + 0) / 4, (b * 3 + 255) / 4);
            this.setForeground(color, secondary);
        }
    }

    public void setForeground(Color var1, Color var2) {
        this.foregroundMainColor = var1;
        this.foregroundSecondaryColor = var2;
        this.repaint();
    }

    public void setBackground(Color color) {
        this.backgroundColor = color;
        this.repaint();
    }

    public void setBackgroundImage(Image image, int offsetX, int offsetY) {
        this.backgroundImage = image;
        this.backgroundImageOffsetX = offsetX;
        this.backgroundImageOffsetY = offsetY;
        this.repaint();
    }

    public void setTarget(int i) {
        this.setTarget(urlTargets[i]);
    }

    public void setTarget(String target) {
        this.urlTarget = target;
    }

    static {
        urlTargets[0] = "_top";
        urlTargets[1] = "_blank";
        defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        handCursor = new Cursor(Cursor.HAND_CURSOR);
        fontDialog11 = new Font("Dialog", Font.PLAIN, 11);
    }

    private static char[] method822(String var0) {
        char[] var10000 = var0.toCharArray();
        char[] var10001 = var10000;

        while (true) {
            int var10002 = var10001.length;
            var10001 = var10000;
            int var4 = var10002;
            if (var10002 >= 2) {
                break;
            }

            char[] var1 = var10001;
            int var2 = var4;
            var10000 = var1;
            char[] var10003 = var1;
            var10002 = var2;
            var10001 = var10003;
            if (var10002 != 0) {
                var10001 = var10000;
                boolean var3 = false;
                var10003[0] = (char) (var10003[0] ^ 54);
                break;
            }
        }

        return var10001;
    }

    private static String method823(char[] var0) {
        int var10000 = var0.length;
        int var1 = 0;
        char[] var10001 = var0;
        if (var10000 > 1) {
            var10001 = var0;
            if (var10000 <= var1) {
                return (new String(var0)).intern();
            }
        }

        do {
            char[] var10002 = var10001;
            int var10003 = var1;

            while (true) {
                char var10004 = var10002[var10003];
                byte var10005;
                switch (var1 % 5) {
                    case 0:
                        var10005 = 41;
                        break;
                    case 1:
                        var10005 = 61;
                        break;
                    case 2:
                        var10005 = 79;
                        break;
                    case 3:
                        var10005 = 79;
                        break;
                    default:
                        var10005 = 54;
                }

                var10002[var10003] = (char) (var10004 ^ var10005);
                ++var1;
                if (var10000 != 0) {
                    break;
                }

                var10003 = var10000;
                var10002 = var10001;
            }
        } while (var10000 > var1);

        return (new String(var10001)).intern();
    }
}
