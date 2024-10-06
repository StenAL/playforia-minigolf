package com.aapeli.client;

import java.awt.Image;

class ImageLoadTask {

    private String name;
    private Image image;
    private int loadAttempts;

    protected ImageLoadTask(String name, Image image) {
        this.name = name;
        this.image = image;
        this.loadAttempts = 0;
    }

    protected String getName() {
        return this.name;
    }

    protected Image getImage() {
        return this.image;
    }

    protected void addLoadAttempt() {
        ++this.loadAttempts;
    }

    protected boolean tooManyLoadAttempts() {
        return this.loadAttempts >= 3;
    }

    protected void destroy() {
        this.image.flush();
        this.image = null;
        this.name = null;
    }
}
