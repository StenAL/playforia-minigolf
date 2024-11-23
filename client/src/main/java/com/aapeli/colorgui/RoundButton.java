package com.aapeli.colorgui;

import com.aapeli.client.IPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class RoundButton extends IPanel implements MouseMotionListener, MouseListener {

    private static final Color DEFAULT_BACKGROUND_COLOR = new Color(224, 224, 224);
    private static final Color DEFAULT_FOREGROUND_COLOR = new Color(0, 0, 0);
    private static final Color DEFAULT_BORDER_COLOR = new Color(255, 255, 255);
    private static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);
    private Color backgroundColor;
    private Color foregroundColor;
    private Color backgroundColorLight;
    private Color borderColor;
    private String label;
    private Font font;
    private String secondaryLabel;
    private Font secondaryFont;
    private Image baseImage;
    private Image hoveredImage;
    private boolean mouseHoveredOver;
    private boolean mousePressedOver;
    private List<ActionListener> listeners;
    private Image image;
    private Graphics graphics;
    private int width;
    private int height;
    private RoundButtonBlinkingThread blinkingThread;
    private boolean isBlinked;

    public RoundButton(String text) {
        this(text, null, null);
    }

    public RoundButton(String text, Image baseImage, Image hoveredImage) {
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
        this.setForeground(DEFAULT_FOREGROUND_COLOR);
        this.setBorderColor(DEFAULT_BORDER_COLOR);
        this.setFont(DEFAULT_FONT);
        this.setLabel(text);
        this.setSecondaryFont(DEFAULT_FONT);
        this.setSecondaryLabel(null);
        this.baseImage = baseImage;
        this.hoveredImage = hoveredImage;
        this.mouseHoveredOver = this.mousePressedOver = false;
        this.listeners = new ArrayList<>();
        this.blinkingThread = null;
        this.isBlinked = false;
        this.addMouseMotionListener(this);
        this.addMouseListener(this);
    }

    public void addNotify() {
        super.addNotify();
        this.mouseHoveredOver = this.mousePressedOver = false;
        this.repaint();
    }

    public void update(Graphics g) {
        Dimension size = this.getSize();
        int width = size.width;
        int height = size.height;
        if (this.image == null || width != this.width || height != this.height) {
            this.image = this.createBuffer(width, height);
            this.graphics = this.image.getGraphics();
            this.width = width;
            this.height = height;
        }

        boolean enabled = this.isEnabled();
        this.drawBackground(this.graphics);
        if (this.baseImage == null) {
            Color backgroundColor = this.mouseHoveredOver && enabled ? this.backgroundColorLight : this.backgroundColor;
            if (this.blinkingThread != null) {
                backgroundColor = this.translateColor(backgroundColor, this.isBlinked ? 32 : -32);
            }

            this.drawButton(this.graphics, backgroundColor, width, height, enabled);
        } else {
            this.graphics.drawImage(this.mouseHoveredOver && enabled ? this.hoveredImage : this.baseImage, 0, 0, this);
        }

        if (this.label != null) {
            this.graphics.setColor(
                    enabled
                            ? this.foregroundColor
                            : this.getColorForGradient(this.foregroundColor, this.backgroundColor, 0.25D));
            Font sizeAdjustedFont = this.getSizeAdjustedFont(this.font, this.label, width - 2);
            Font sizeAdjustedSecondaryFont = null;
            int labelY = height / 2 + sizeAdjustedFont.getSize() * 3 / 8 + 1;
            int secondaryLabelY = -1;
            if (this.secondaryLabel != null) {
                sizeAdjustedSecondaryFont =
                        this.getSizeAdjustedFont(this.secondaryFont, this.secondaryLabel, width - 2);
                int yPadding = (height - sizeAdjustedFont.getSize() - sizeAdjustedSecondaryFont.getSize()) / 3;
                labelY = yPadding + sizeAdjustedFont.getSize();
                secondaryLabelY = height - yPadding - sizeAdjustedSecondaryFont.getSize() / 8 - 1;
            }

            this.graphics.setFont(sizeAdjustedFont);
            this.drawText(
                    this.graphics,
                    this.label,
                    width / 2 - this.getFontMetrics(sizeAdjustedFont).stringWidth(this.label) / 2,
                    labelY);
            if (this.secondaryLabel != null) {
                this.graphics.setFont(sizeAdjustedSecondaryFont);
                this.drawText(
                        this.graphics,
                        this.secondaryLabel,
                        width / 2
                                - this.getFontMetrics(sizeAdjustedSecondaryFont).stringWidth(this.secondaryLabel) / 2,
                        secondaryLabelY);
            }
        }

        g.drawImage(this.image, 0, 0, this);
    }

    public void mouseEntered(MouseEvent e) {
        this.mouseHoveredOver = true;
        this.repaint();
    }

    public void mouseExited(MouseEvent e) {
        this.mouseHoveredOver = false;
        this.repaint();
    }

    public void mousePressed(MouseEvent e) {
        this.mousePressedOver = true;
        this.repaint();
    }

    public void mouseReleased(MouseEvent e) {
        boolean wasPressed = this.mousePressedOver;
        this.mousePressedOver = false;
        this.repaint();
        if (wasPressed) {
            this.processActionEvent();
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {}

    public void setBackground(Color backgroundColor) {
        if (backgroundColor == null) {
            backgroundColor = DEFAULT_BACKGROUND_COLOR;
        }

        super.setBackground(backgroundColor);
        this.backgroundColor = backgroundColor;
        this.backgroundColorLight = this.translateColor(backgroundColor, 32);
        this.repaint();
    }

    public void setForeground(Color foregroundColor) {
        if (foregroundColor == null) {
            foregroundColor = DEFAULT_FOREGROUND_COLOR;
        }

        super.setForeground(foregroundColor);
        this.foregroundColor = foregroundColor;
        this.repaint();
    }

    public void setFont(Font font) {
        super.setFont(font);
        this.font = font;
        this.repaint();
    }

    public void setLabel(String label) {
        this.label = label;
        this.repaint();
    }

    public String getLabel() {
        return this.label;
    }

    public void setSecondaryFont(Font secondaryFont) {
        this.secondaryFont = secondaryFont;
        this.repaint();
    }

    public void setSecondaryLabel(String secondaryLabel) {
        this.secondaryLabel = secondaryLabel;
        this.repaint();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        this.repaint();
    }

    public Dimension getPreferredSize() {
        return this.baseImage == null
                ? new Dimension(
                        13 + this.getFontMetrics(this.font).stringWidth(this.label) + 13, 5 + this.font.getSize() + 5)
                : new Dimension(this.baseImage.getWidth(null), this.baseImage.getHeight(null));
    }

    public void addActionListener(ActionListener listener) {
        synchronized (this.listeners) {
            this.listeners.add(listener);
        }
    }

    public void removeActionListener(ActionListener listener) {
        synchronized (this.listeners) {
            this.listeners.remove(listener);
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.repaint();
    }

    public void setFlashing(boolean flashing) {
        if (flashing) {
            if (this.blinkingThread != null) {
                return;
            }

            this.blinkingThread = new RoundButtonBlinkingThread(this);
            Thread thread = new Thread(this.blinkingThread);
            thread.setDaemon(true);
            thread.start();
        } else {
            if (this.blinkingThread == null) {
                return;
            }

            this.blinkingThread.stop();
            this.blinkingThread = null;
            this.isBlinked = false;
            this.repaint();
        }
    }

    public void processActionEvent() {
        synchronized (this.listeners) {
            if (this.listeners.size() != 0) {
                ActionEvent e = new ActionEvent(this, 1001, this.label);
                for (ActionListener listener : listeners) {
                    listener.actionPerformed(e);
                }
            }
        }
    }

    public Image createBuffer(int width, int height) {
        return this.createImage(width, height);
    }

    public void drawText(Graphics g, String text, int x, int y) {
        g.drawString(text, x, y);
    }

    public boolean isHighlighted() {
        return this.mouseHoveredOver;
    }

    private void drawButton(Graphics g, Color backgroundColor, int width, int height, boolean enabled) {
        int borderRadius = (int) (Math.sqrt(height) + 1.8D);
        double borderThickness = Math.sqrt(Math.sqrt(height)) - 0.34D;
        Color parentBackground = this.getParent().getBackground();
        Color borderColor = this.borderColor;
        if (!enabled) {
            borderColor = this.getColorForGradient(borderColor, parentBackground, 0.5D);
        }

        for (int y = 0; y < height; ++y) {
            Color pixelColor = this.getColorForGradient(backgroundColor, y, height);
            if (!enabled) {
                pixelColor = this.getColorForGradient(pixelColor, parentBackground, 0.5D);
            }

            for (int x = 0; x < width; ++x) {
                int boundedX = x;
                int boundedY = y;
                if (y < borderRadius) {
                    boundedY = borderRadius;
                } else if (y >= height - borderRadius) {
                    boundedY = height - borderRadius - 1;
                }

                if (x < borderRadius) {
                    boundedX = borderRadius;
                } else if (x >= width - borderRadius) {
                    boundedX = width - borderRadius - 1;
                }

                double dX = x - boundedX;
                double dY = y - boundedY;
                double distanceFromBoundingBox = Math.sqrt(dX * dX + dY * dY);
                if (distanceFromBoundingBox <= (double) borderRadius + 1.0D
                        && distanceFromBoundingBox <= (double) borderRadius) {
                    if (distanceFromBoundingBox > (double) borderRadius - borderThickness) { //
                        if (distanceFromBoundingBox <= (double) borderRadius - borderThickness + 0.5D) {
                            g.setColor(this.getColorForGradient(borderColor, pixelColor, 0.5D));
                        } else if (distanceFromBoundingBox > (double) borderRadius - 0.5D) {
                            g.setColor(this.getColorForGradient(borderColor, parentBackground, 0.5D));
                        } else { // exactly on border
                            g.setColor(borderColor);
                        }
                    } else {
                        g.setColor(pixelColor);
                    }

                    g.fillRect(x, y, 1, 1);
                }
            }
        }
    }

    private Color getColorForGradient(Color color, int y, int height) {
        double gradientProgress = (double) y / (double) height;
        Color lightColor;
        Color darkColor;
        if (y < height / 2) {
            lightColor = color.brighter();
            darkColor = color;
            gradientProgress *= 2.0D;
        } else {
            lightColor = color;
            darkColor = color.darker();
            gradientProgress = (gradientProgress - 0.5D) * 2.0D;
        }

        if (gradientProgress < 0.0D) {
            gradientProgress = 0.0D;
        } else if (gradientProgress > 1.0D) {
            gradientProgress = 1.0D;
        }

        return this.getColorForGradient(lightColor, darkColor, gradientProgress);
    }

    private Color getColorForGradient(Color lightColor, Color darkColor, double gradientProgress) {
        int r1 = lightColor.getRed();
        int g1 = lightColor.getGreen();
        int b1 = lightColor.getBlue();
        int r2 = darkColor.getRed();
        int g2 = darkColor.getGreen();
        int b2 = darkColor.getBlue();
        int deltaR = r2 - r1;
        int deltaG = g2 - g1;
        int deltaB = b2 - b1;
        int r = (int) ((double) r1 + (double) deltaR * gradientProgress + 0.5D);
        int g = (int) ((double) g1 + (double) deltaG * gradientProgress + 0.5D);
        int b = (int) ((double) b1 + (double) deltaB * gradientProgress + 0.5D);
        return new Color(r, g, b);
    }

    private Color translateColor(Color color, int offset) {
        int r = color.getRed() + offset;
        int g = color.getGreen() + offset;
        int b = color.getBlue() + offset;
        if (r < 0) {
            r = 0;
        } else if (r > 255) {
            r = 255;
        }

        if (g < 0) {
            g = 0;
        } else if (g > 255) {
            g = 255;
        }

        if (b < 0) {
            b = 0;
        } else if (b > 255) {
            b = 255;
        }

        return new Color(r, g, b);
    }

    private Font getSizeAdjustedFont(Font font, String text, int width) {
        int stringWidth = this.getFontMetrics(font).stringWidth(text);
        if (stringWidth <= width) {
            return font;
        } else {
            int fontSize = font.getSize();

            do {
                Font newFont = font;
                int newWidth = stringWidth;
                --fontSize;
                font = new Font(font.getName(), font.getStyle(), fontSize);
                stringWidth = this.getFontMetrics(font).stringWidth(text);
                if (stringWidth >= newWidth) {
                    return newFont;
                }
            } while (stringWidth > width && fontSize > 9);

            return font;
        }
    }

    public void innerSetFlashState(boolean state) {
        this.isBlinked = state;
        this.repaint();
    }
}
