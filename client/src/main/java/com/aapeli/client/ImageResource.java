package com.aapeli.client;

import java.awt.Image;

class ImageResource {

    private String aString1404;
    private Image image;
    private int anInt1406;
    private final ImageTracker aImageTracker_1407;

    protected ImageResource(ImageTracker var1, String var2, Image image) {
        this.aImageTracker_1407 = var1;
        this.aString1404 = var2;
        this.image = image;
        this.anInt1406 = 0;
    }

    protected String method1648() {
        return this.aString1404;
    }

    protected Image getImage() {
        return this.image;
    }

    protected void method1650() {
        ++this.anInt1406;
    }

    protected boolean method1651() {
        return this.anInt1406 >= 3;
    }

    protected void method1652() {
        this.image.flush();
        this.image = null;
        this.aString1404 = null;
    }
}
