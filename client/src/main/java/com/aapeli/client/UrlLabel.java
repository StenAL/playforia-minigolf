package com.aapeli.client;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UrlLabel extends IPanel implements MouseListener {

    public static final int ALIGN_LEFT = -1;
    public static final int ALIGN_CENTER = 0;
    public static final int ALIGN_RIGHT = 1;
    public static final int TARGET_SAME = 0;
    public static final int TARGET_NEW = 1;
    private static final String[] urlTargets = new String[] {"_top", "_blank"};
    private static final Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
    private static final Font fontDialog11 = new Font("Dialog", Font.PLAIN, 11);
    private Font currentFont;
    private URI uri;
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

    public UrlLabel() {
        this(fontDialog11, null, null);
    }

    public UrlLabel(Font font, String text, String url) {
        this.backgroundColor = null;
        this.foregroundMainColor = Color.black;
        this.foregroundSecondaryColor = Color.blue;
        this.backgroundImage = null;
        this.alignment = ALIGN_LEFT;
        this.urlTarget = "_blank";
        this.currentFont = font;
        this.setText(text, url);
    }

    public void update(Graphics graphics) {
        Dimension size = this.getSize();
        int width = size.width;
        int height = size.height;
        if (this.backgroundImage != null) {
            graphics.drawImage(
                    this.backgroundImage,
                    0,
                    0,
                    width,
                    height,
                    this.backgroundImageOffsetX,
                    this.backgroundImageOffsetY,
                    this.backgroundImageOffsetX + width,
                    this.backgroundImageOffsetY + height,
                    this);
        } else if (!this.drawBackgroundImage(graphics)) {
            graphics.setColor(this.backgroundColor != null ? this.backgroundColor : this.getBackground());
            graphics.fillRect(0, 0, width, height);
        }

        if (this.text != null) {
            int x = 2;
            int fontSize = this.currentFont.getSize();
            int textHeight = height / 2 + fontSize * 3 / 8;
            int y = height / 2 + fontSize / 2;
            if (this.alignment == 0) {
                x = width / 2 - this.textWidth / 2;
            }

            if (this.alignment == ALIGN_RIGHT) {
                x = width - 2 - this.textWidth;
            }

            if (this.linkPlaceholderText != null && this.suffix != null) {
                graphics.setColor(this.foregroundSecondaryColor);
                graphics.drawLine(x + this.prefixWidth, y, x + this.prefixWidth + this.linkTextWidth, y);
            }

            graphics.setFont(this.currentFont);
            graphics.setColor(this.foregroundMainColor);
            graphics.drawString(this.text, x, textHeight);
            if (this.linkPlaceholderText != null && this.suffix != null) {
                graphics.setColor(this.foregroundSecondaryColor);
                graphics.drawString(this.linkPlaceholderText, x + this.prefixWidth, textHeight);
                graphics.setColor(this.foregroundMainColor);
                graphics.drawString(this.suffix, x + this.prefixWidth + this.linkTextWidth, textHeight);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
        try {
            Desktop.getDesktop().browse(this.uri);
        } catch (IOException ex) {
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void setText(String text, String url) {
        if (text == null) {
            this.text = this.linkPlaceholderText = this.suffix = null;
            this.uri = null;
            this.setCursor(defaultCursor);
            this.repaint();
        } else {
            this.uri = null;
            if (url != null) {
                try {
                    this.uri = new URI(url);
                } catch (URISyntaxException e) {
                }
            }

            FontMetrics fontMetrics = this.getFontMetrics(this.currentFont);
            int linkTextStart = text.indexOf('<');
            int linkTextEnd = text.indexOf('>');
            if (this.uri != null && linkTextStart != -1 && linkTextEnd >= linkTextStart) {
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

    public void setForeground(Color mainColor, Color secondaryColor) {
        this.foregroundMainColor = mainColor;
        this.foregroundSecondaryColor = secondaryColor;
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
}
