package com.aapeli.client;

import java.awt.Image;

class BackgroundLoaderThread implements Runnable {

    private IPanel panel;
    private ImageManager imageManager;
    private String backgroundKey;
    private int xOffset;
    private int yOffset;
    private boolean isSharedImage;
    private boolean running;
    private final IPanel anIPanel1415;

    protected BackgroundLoaderThread(
            IPanel unused,
            IPanel panel,
            ImageManager imageManager,
            String backgroundKey,
            int xOffset,
            int yOffset,
            boolean isSharedImage) {
        this.anIPanel1415 = unused;
        this.panel = panel;
        this.imageManager = imageManager;
        this.backgroundKey = backgroundKey;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.isSharedImage = isSharedImage;
        this.running = true;
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void run() {
        Image image;
        if (!this.isSharedImage) {
            image = this.imageManager.getGameImage(this.backgroundKey);
        } else {
            image = this.imageManager.getShared(this.backgroundKey);
        }

        if (this.running) {
            this.panel.setBackground(image, this.xOffset, this.yOffset);
        }
    }

    protected void stop() {
        this.running = false;
    }
}
