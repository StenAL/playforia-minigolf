package com.aapeli.applet;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class RetryCanvas extends Canvas implements MouseMotionListener, MouseListener {

    private static final Color backgroundColor = new Color(64, 64, 224);
    private static final Color textColor = new Color(224, 224, 255);
    private static final Font dialog12 = new Font("Dialog", Font.PLAIN, 12);
    private Color backgroundBrighter;
    private Color backgroundBrightest;
    private Color backgroundDarker;
    private Color aColor134;
    private String message;
    private boolean hovered;
    private boolean mousePressed;
    private ActionListener listener;
    private int canvasWidth;
    private int canvasHeight;
    private Image image;
    private Graphics graphics;

    protected RetryCanvas(String message, int canvasWidth, int canvasHeight, ActionListener listener) {
        this.setBackground(backgroundColor);
        this.setForeground(textColor);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.setSize(canvasWidth, canvasHeight);
        this.setFont(dialog12);
        this.message = message;
        this.listener = listener;
        this.hovered = false;
        this.mousePressed = false;
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    public void addNotify() {
        super.addNotify();
        this.hovered = false;
        this.mousePressed = false;
        this.repaint();
    }

    public void paint(Graphics graphics) {
        this.update(graphics);
    }

    public void update(Graphics graphics) {
        if (this.image == null) {
            this.image = this.createImage(this.canvasWidth, this.canvasHeight);
            this.graphics = this.image.getGraphics();
        }

        boolean hovered = this.hovered;
        Color background = hovered ? this.backgroundBrighter : backgroundColor;
        this.method220(this.graphics, background, this.canvasWidth, this.canvasHeight);
        if (this.aColor134 != null) {
            this.graphics.setColor(this.aColor134);
        }

        this.drawButton(this.graphics, this.canvasWidth, this.canvasHeight);
        this.graphics.setColor(textColor);
        this.graphics.setFont(dialog12);
        this.graphics.drawString(
                this.message,
                this.canvasWidth / 2 - this.getFontMetrics(dialog12).stringWidth(this.message) / 2,
                this.canvasHeight / 2 + dialog12.getSize() * 3 / 8 + 1);
        graphics.drawImage(this.image, 0, 0, this);
    }

    public void setBackground(Color color) {
        super.setBackground(color);
        this.backgroundBrighter = this.translateColor(color, 32);
        this.backgroundBrightest = this.translateColor(color, 48);
        this.backgroundDarker = this.translateColor(color, -48);
        this.repaint();
    }

    public void mouseEntered(MouseEvent e) {
        this.hovered = true;
        this.repaint();
    }

    public void mouseExited(MouseEvent e) {
        this.hovered = false;
        this.mousePressed = false;
        this.repaint();
    }

    public void mousePressed(MouseEvent e) {
        this.mousePressed = true;
        this.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        boolean wasPressed = this.mousePressed;
        this.mousePressed = false;
        this.repaint();
        if (wasPressed) {
            ActionEvent action = new ActionEvent(this, 1001, this.message);
            this.listener.actionPerformed(action);
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

    private Color translateColor(Color color, int offset) {
        int r = this.translateColor(color.getRed(), offset);
        int g = this.translateColor(color.getGreen(), offset);
        int b = this.translateColor(color.getBlue(), offset);
        return new Color(r, g, b);
    }

    private int translateColor(int base, int offset) {
        base += offset;
        if (base < 0) {
            base = 0;
        } else if (base > 255) {
            base = 255;
        }

        return base;
    }

    private void method220(Graphics graphics, Color background, int width, int height) {
        int baseR = background.getRed();
        int baseG = background.getGreen();
        int baseB = background.getBlue();
        int r = baseR;
        int g = baseG;
        int b = baseB;

        int y;
        for (y = height / 2; y >= 0; --y) {
            graphics.setColor(new Color(r, g, b));
            graphics.drawLine(0, y, width - 1, y);
            r = this.translateColor(r, 3);
            g = this.translateColor(g, 3);
            b = this.translateColor(b, 3);
        }

        r = baseR;
        g = baseG;
        b = baseB;

        for (y = height / 2 + 1; y < height; ++y) {
            r = this.translateColor(r, -3);
            g = this.translateColor(g, -3);
            b = this.translateColor(b, -3);
            graphics.setColor(new Color(r, g, b));
            graphics.drawLine(0, y, width - 1, y);
        }
    }

    private void drawButton(Graphics graphics, int width, int height) {
        if (this.aColor134 == null) {
            graphics.setColor(!this.mousePressed ? this.backgroundDarker : this.backgroundBrightest);
        }

        graphics.drawRect(0, 0, width - 1, height - 1);
        if (this.aColor134 == null) {
            graphics.setColor(!this.mousePressed ? this.backgroundBrightest : this.backgroundDarker);
        }

        graphics.drawLine(0, 0, width - 1, 0);
        graphics.drawLine(0, 0, 0, height - 1);
    }
}
