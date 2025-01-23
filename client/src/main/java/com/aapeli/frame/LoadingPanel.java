package com.aapeli.frame;

import com.aapeli.client.Parameters;
import com.aapeli.client.TextManager;
import com.aapeli.colorgui.RoundButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class LoadingPanel extends Panel implements Runnable, ActionListener {

    private static final Font fontDialog14 = new Font("Dialog", Font.PLAIN, 14);
    private static final Font fontDialog20b = new Font("Dialog", Font.BOLD, 20);
    private AbstractGameFrame gameFrame;
    private Parameters parameters;
    private TextManager textManager;
    private String loadingMessage;
    private double actualProgress;
    private double renderedProgress;
    private double aDouble586;
    private int updateInterval;
    private boolean needsRepaint;
    private boolean aBoolean589;
    private boolean loaded;
    private boolean destroyed;
    private Image panelImage;
    private Graphics panelGraphics;
    private boolean aBoolean595;
    private RoundButton startGameButton;
    private RoundButton paymentOptionsButton;
    private int startGameClicked;

    protected LoadingPanel(AbstractGameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.loadingMessage = null;
        this.actualProgress = 0.0D;
        this.renderedProgress = 0.0D;
        this.aDouble586 = 0.0018D;
        this.updateInterval = 50;
        this.loaded = false;
        this.destroyed = false;
        this.needsRepaint = true;
        this.aBoolean589 = true;
        this.setSize(gameFrame.contentWidth, gameFrame.contentHeight);
        this.setPreferredSize(new Dimension(gameFrame.contentWidth, gameFrame.contentHeight));
        this.startGameClicked = -1;
    }

    public void paint(Graphics graphics) {
        this.update(graphics);
    }

    public synchronized void update(Graphics graphics) {
        if (!this.destroyed) {
            AbstractGameFrame gameFrame = this.gameFrame;
            if (gameFrame != null) {
                int width = gameFrame.contentWidth;
                int height = gameFrame.contentHeight;
                if (this.panelImage == null) {
                    this.panelImage = this.createImage(width, height);
                    this.panelGraphics = this.panelImage.getGraphics();
                    this.needsRepaint = true;
                }

                Color background = this.getBackground();
                if (background.equals(Color.black)) {
                    background = new Color(24, 24, 24);
                }

                boolean needsRepaint = this.needsRepaint;
                this.needsRepaint = false;
                if (needsRepaint) {
                    this.drawGradient(this.panelGraphics, background, 0, 32, 0, height, 0, width, this.aBoolean589);
                    this.aBoolean589 = false;
                    if (this.loadingMessage != null && this.startGameClicked == -1) {
                        this.panelGraphics.setColor(this.getForeground());
                        this.drawLoadingMessage(this.panelGraphics, fontDialog14, this.loadingMessage);
                    }
                }

                if (this.startGameClicked == -1) {
                    this.drawGradient(this.panelGraphics, Color.white, 0, 48, 25, 40, 5, width - 5, true);
                    int var7 = (int) ((double) (width - 10) * this.renderedProgress);
                    if (var7 > 0) {
                        this.drawGradient(this.panelGraphics, Color.green, 144, 144, 25, 40, 5, 5 + var7, true);
                    }

                    this.panelGraphics.setColor(Color.black);
                    this.panelGraphics.drawRect(5, 25, width - 10 - 1, 14);
                }

                graphics.drawImage(this.panelImage, 0, 0, this);
            }
        }
    }

    public void setBackground(Color var1) {
        super.setBackground(var1);
        this.needsRepaint = true;
        this.repaint();
    }

    public void run() {
        do {
            try {
                Thread.sleep(this.updateInterval);
            } catch (InterruptedException e) {
            }

            if (this.destroyed) {
                return;
            }

            boolean done = false;
            if (this.renderedProgress < this.actualProgress) {
                this.renderedProgress += this.getNextProgressIncrementToRender();
                if (this.renderedProgress > 1.0D) {
                    this.renderedProgress = 1.0D;
                }

                done = true;
            }

            if (this.actualProgress >= 1.0D && this.gameFrame.isDebug()) {
                this.renderedProgress = 1.0D;
                done = true;
            }

            if (done) {
                this.repaint();
            }
        } while (this.renderedProgress < 1.0D);

        this.loaded = true;
    }

    public void actionPerformed(ActionEvent action) {
        if (action.getSource() == this.startGameButton) {
            this.startGameClicked = 1;
        } else {
            this.gameFrame.setEndState(AbstractGameFrame.END_QUIT_BUYCOINS);
            this.parameters.showCreditPurchasePage(false);
        }
    }

    protected void init(Parameters parameters, TextManager textManager) {
        this.parameters = parameters;
        this.textManager = textManager;
    }

    protected void start() {
        Thread thread = new Thread(this);
        thread.start();
    }

    protected void setLoadingMessage(String message) {
        this.loadingMessage = message;
        this.needsRepaint = true;
        this.repaint();
    }

    protected void addProgress(double progress) {
        this.actualProgress += progress;
    }

    protected void setActualProgress(double actualProgress) {
        this.actualProgress = actualProgress;
    }

    protected void method468(double var1) {
        this.aDouble586 *= var1;
    }

    protected Image getImage() {
        return this.panelImage;
    }

    protected void method470() {
        this.updateInterval = 25;
    }

    protected boolean isLoaded() {
        return this.loaded;
    }

    protected synchronized void destroy() {
        this.destroyed = true;
        this.loadingMessage = null;
        if (this.panelGraphics != null) {
            this.panelGraphics.dispose();
            this.panelGraphics = null;
        }

        if (this.panelImage != null) {
            this.panelImage.flush();
            this.panelImage = null;
        }

        this.gameFrame = null;
    }

    private void drawGradient(
            Graphics var1, Color color, int top, int bottom, int var5, int var6, int var7, int var8, boolean var9) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int topRed = red + top;
        int topGreen = green + top;
        int topBlue = blue + top;
        int btmRed = red - bottom;
        int btmGreen = green - bottom;
        int btmBlue = blue - bottom;
        if (topRed > 255) {
            topRed = 255;
        }

        if (topGreen > 255) {
            topGreen = 255;
        }

        if (topBlue > 255) {
            topBlue = 255;
        }

        if (btmRed < 0) {
            btmRed = 0;
        }

        if (btmGreen < 0) {
            btmGreen = 0;
        }

        if (btmBlue < 0) {
            btmBlue = 0;
        }

        if (var9) {
            this.drawGradient(var1, var5, var6, var7, var8, topRed, btmRed, topGreen, btmGreen, topBlue, btmBlue);
        } else {
            this.drawGradient2(var1, var5, var6, var7, var8, topRed, btmRed, topGreen, btmGreen, topBlue, btmBlue);
        }
    }

    private void drawGradient(
            Graphics var1,
            int var2,
            int var3,
            int var4,
            int var5,
            int var6,
            int var7,
            int var8,
            int var9,
            int var10,
            int var11) {
        for (int var17 = var2; var17 < var3; ++var17) {
            double var12 = (double) (var17 - var2) / (double) (var3 - var2);
            int var14 = (int) ((double) var6 + (double) (var7 - var6) * var12);
            int var15 = (int) ((double) var8 + (double) (var9 - var8) * var12);
            int var16 = (int) ((double) var10 + (double) (var11 - var10) * var12);
            var1.setColor(new Color(var14, var15, var16));
            var1.drawLine(var4, var17, var5 - 1, var17);
        }
    }

    private void drawGradient2(
            Graphics var1,
            int var2,
            int var3,
            int var4,
            int var5,
            int var6,
            int var7,
            int var8,
            int var9,
            int var10,
            int var11) {
        int var20 = -1;

        for (int var21 = var2; var21 < var3; ++var21) {
            double var12 = (double) (var21 - var2) / (double) (var3 - var2);

            for (int var22 = var4; var22 < var5; ++var22) {
                double var14;
                if (var22 == var4) {
                    var14 = 0.0D;
                } else {
                    var14 = Math.random() * 1.98D - 0.99D;
                }

                int var16 = (int) ((double) var6 + (double) (var7 - var6) * var12 + var14);
                int var17 = (int) ((double) var8 + (double) (var9 - var8) * var12 + var14);
                int var18 = (int) ((double) var10 + (double) (var11 - var10) * var12 + var14);
                int var19 = var16 * 256 * 256 + var17 * 256 + var18;
                if (var22 == var4) {
                    var20 = var19;
                    var1.setColor(new Color(var19));
                    var1.drawLine(var4, var21, var5, var21);
                } else if (var19 != var20) {
                    var1.setColor(new Color(var19));
                    var1.fillRect(var22, var21, 1, 1);
                }
            }
        }
    }

    private void drawLoadingMessage(Graphics g, Font font, String s) {
        while (this.getFontMetrics(font).stringWidth(s) > this.gameFrame.contentWidth - 12) {
            font = new Font(font.getName(), font.getStyle(), font.getSize() - 1);
        }

        g.setFont(font);
        g.drawString(this.loadingMessage, 6, 19);
    }

    private double getNextProgressIncrementToRender() {
        return this.aDouble586;
    }
}
