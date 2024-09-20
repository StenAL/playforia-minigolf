package com.aapeli.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Point;

public class IPanel extends Panel {

    private Image backgroundImage;
    private int backgroundImageXOffset;
    private int backgroundImageYOffset;
    private Class84 aClass84_647;
    private Object anObject648 = new Object();
    public static int anInt649;

    public void paint(Graphics graphics) {
        this.update(graphics);
    }

    public void update(Graphics graphics) {
        this.drawBackground(graphics);
    }

    public void setBackground(Image image) {
        this.setBackground(image, 0, 0);
    }

    public void setBackground(Image image, int xOffset, int yOffset) {
        this.backgroundImage = image;
        this.backgroundImageXOffset = xOffset;
        this.backgroundImageYOffset = yOffset;
        this.recursiveRepaint();
    }

    public void setBackground(ImageManager imageManager, String imageKey) {
        this.setBackground(imageManager, imageKey, 0, 0);
    }

    public void setBackground(ImageManager imageManager, String imageKey, int xOffset, int yOffset) {
        Image image = imageManager.getIfAvailable(imageKey);
        if (image != null) {
            this.setBackground(image, xOffset, yOffset);
        } else {
            Object var6 = this.anObject648;
            synchronized (this.anObject648) {
                if (this.aClass84_647 != null) {
                    this.aClass84_647.method1653();
                }

                this.aClass84_647 = new Class84(this, this, imageManager, imageKey, xOffset, yOffset, false);
            }
        }
    }

    public void setSharedBackground(ImageManager var1, String var2, int var3, int var4) {
        Object var5 = this.anObject648;
        synchronized (this.anObject648) {
            if (this.aClass84_647 != null) {
                this.aClass84_647.method1653();
            }

            this.aClass84_647 = new Class84(this, this, var1, var2, var3, var4, true);
        }
    }

    public void drawBackground(Graphics var1) {
        if (!this.drawBackgroundImage(var1)) {
            Component var2 = this.getParent();
            if (var2 == null) {
                var2 = this;
            }

            Dimension var3 = this.getSize();
            var1.setColor(var2.getBackground());
            var1.fillRect(0, 0, var3.width, var3.height);
        }
    }

    public boolean drawBackgroundImage(Graphics graphics) {
        Object[] backgroundData = this.getBackgroundAndLocation(0, 0);
        if (backgroundData == null) {
            return false;
        } else {
            Image backgroundImage = (Image) backgroundData[0];
            int backgroundImageXOffset = (Integer) backgroundData[1];
            int backgroundImageYOffset = (Integer) backgroundData[2];
            Dimension size = this.getSize();
            graphics.drawImage(
                    backgroundImage,
                    0,
                    0,
                    size.width,
                    size.height,
                    -backgroundImageXOffset,
                    -backgroundImageYOffset,
                    -backgroundImageXOffset + size.width,
                    -backgroundImageYOffset + size.height,
                    null);
            return true;
        }
    }

    public void recursiveRepaint() {
        this.repaint();
        Component[] components = this.getComponents();
        if (components != null) {
            int componentsCount = components.length;
            if (componentsCount != 0) {
                for (Component component : components) {
                    if (component instanceof IPanel) {
                        ((IPanel) component).recursiveRepaint();
                    } else {
                        component.repaint();
                    }
                }
            }
        }
    }

    public Object[] getBackgroundAndLocation(int var1, int var2) {
        if (this.backgroundImage != null) {
            return new Object[] {
                this.backgroundImage, this.backgroundImageXOffset + var1, this.backgroundImageYOffset + var2
            };
        } else {
            Container var3 = this.getParent();
            if (var3 == null) {
                return null;
            } else if (!(var3 instanceof IPanel)) {
                return null;
            } else {
                Point var4 = this.getLocation();
                var1 -= var4.x;
                var2 -= var4.y;
                IPanel var5 = (IPanel) var3;
                return var5.getBackgroundAndLocation(var1, var2);
            }
        }
    }
}
