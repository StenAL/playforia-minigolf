package com.aapeli.colorgui;

import com.aapeli.client.StringDraw;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

class ColorListNode {

    private int x;
    private int y;
    private int width;
    private int height;
    private int iconWidth;
    private boolean hasBackgroundImage;
    private Font font;
    private Font fontBold;
    private ColorListItem item;
    private Color color;
    private String text;
    private Image icon;
    private boolean hasIcon;

    /** Node for items (e.g. players) */
    protected ColorListNode(
            int x,
            int y,
            int width,
            int height,
            int iconWidth,
            boolean hasBackgroundImage,
            Font font,
            Font fontBold,
            ColorListItem item) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.iconWidth = iconWidth;
        this.hasBackgroundImage = hasBackgroundImage;
        this.font = font;
        this.fontBold = fontBold;
        this.item = item;
        this.hasIcon = false;
    }

    /** Node for groups (e.g. languages) */
    protected ColorListNode(
            int x,
            int y,
            int width,
            int height,
            int iconWidth,
            boolean hasBackgroundImage,
            Font font,
            Color color,
            String text,
            Image icon) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.iconWidth = iconWidth;
        this.hasBackgroundImage = hasBackgroundImage;
        this.font = font;
        this.color = color;
        this.text = text;
        this.icon = icon;
        this.hasIcon = icon != null;
    }

    protected void draw(Graphics g, ColorList colorList) {
        if (this.item != null) {
            this.drawItem(g, colorList);
        } else {
            this.drawGroup(g, colorList);
        }
    }

    protected boolean containsYCoordinate(int y) {
        return y >= this.y && y < this.y + this.height;
    }

    protected ColorListItem getItem() {
        return this.item;
    }

    private void drawItem(Graphics g, ColorList colorList) {
        Color color = this.item.getColor();
        if (this.item.isSelected()) {
            g.setColor(color);
            g.fillRect(this.x, this.y, this.width, this.height);
            color = this.getForegroundColor(color);
        }

        this.drawText(
                g,
                colorList,
                this.item.getIcon(),
                color,
                this.item.isBold() ? this.fontBold : this.font,
                this.item.getText(),
                this.item.getIconAfterText());
    }

    private void drawGroup(Graphics g, ColorList colorList) {
        if (this.hasIcon) {
            g.setColor(new Color(224, 224, 224));
            g.fillRect(this.x, this.y, this.width, this.height);
        }

        this.drawText(g, colorList, this.icon, this.color, this.font, this.text, null);
    }

    private void drawText(
            Graphics g, ColorList colorList, Image icon, Color color, Font font, String text, Image iconAfterText) {
        int x = 4;
        if (icon != null) {
            g.drawImage(icon, x, this.y + this.height / 2 - icon.getHeight(colorList) / 2, colorList);
            int iconWidth = this.iconWidth > 0 ? this.iconWidth : icon.getWidth(null);
            x += iconWidth + 3;
        }

        g.setColor(color);
        g.setFont(font);
        x += StringDraw.drawString(g, text, x, this.y + this.height * 3 / 4 + 1, -1);
        if (iconAfterText != null) {
            x += 4;
            g.drawImage(iconAfterText, x, this.y + this.height / 2 - iconAfterText.getHeight(colorList) / 2, colorList);
        }
    }

    private Color getForegroundColor(Color backgroundColor) {
        if (!this.hasBackgroundImage) {
            return ColorList.backgroundColor;
        } else {
            int r = backgroundColor.getRed();
            int g = backgroundColor.getGreen();
            int b = backgroundColor.getBlue();
            return new Color(255 - r, 255 - g, 255 - b);
        }
    }
}
