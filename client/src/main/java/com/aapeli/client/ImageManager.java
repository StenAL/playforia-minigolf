package com.aapeli.client;

import com.aapeli.tools.Tools;
import java.applet.Applet;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.util.Hashtable;
import javax.imageio.ImageIO;

public final class ImageManager {

    private Applet applet;
    private Hashtable<String, String> imageAliases;
    private final boolean isDebug;
    private Hashtable<String, Image> gameImages;
    private Hashtable<String, Image> sharedImages;

    public ImageManager(Applet applet, boolean isDebug) {
        this.applet = applet;
        this.isDebug = isDebug;
        this.gameImages = new Hashtable<>();
        this.sharedImages = new Hashtable<>();
        this.imageAliases = new Hashtable<>();
    }

    public void setImageAliases(String[][] imageAliases) {
        if (imageAliases != null) {
            for (String[] aliases : imageAliases) {
                this.imageAliases.put(aliases[0], aliases[1]);
            }
        }
    }

    public String defineGameImage(String fileName) {
        return this.defineGameImage(this.removeExtension(fileName), fileName);
    }

    public String defineGameImage(String name, String imageFileName) {
        if (this.isDebug) {
            System.out.println("ImageManager.defineGameImage(\"" + name + "\",\"" + imageFileName + "\")");
        }

        try {
            Image image = ImageIO.read(this.getClass().getResource("/picture/agolf/" + getAlias(imageFileName)));
            this.gameImages.put(name, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return name;
    }

    public String defineSharedImage(String fileName) {
        return defineSharedImage(removeExtension(fileName), fileName);
    }

    public String defineSharedImage(String name, String imageFileName) {
        if (this.isDebug) {
            System.out.println("ImageManager.defineSharedImage(\"" + name + "\",\"" + imageFileName + "\")");
        }

        try {
            Image image = ImageIO.read(this.getClass().getResource("/picture/shared/" + getAlias(imageFileName)));
            this.sharedImages.put(name, image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return name;
    }

    public void unloadGameImage(String name) {
        this.gameImages.remove(name);
    }

    public double getImageLoadProgress() {
        return 1.0D;
    }

    public Image getGameImage(String name) {
        return this.gameImages.get(this.getAlias(name));
    }

    public boolean isGameImageDefined(String name) {
        return this.gameImages.contains(this.getAlias(name));
    }

    public Image getShared(String name) {
        String extensionlessName = this.removeExtension(name);
        return this.sharedImages.get(extensionlessName);
    }

    public int getWidth(Image image) {
        return image.getWidth(this.applet);
    }

    public int getHeight(Image image) {
        return image.getHeight(this.applet);
    }

    public int[] getPixels(Image image, int width, int height) {
        return this.getPixels(image, 0, 0, width, height);
    }

    public int[] getPixels(Image image, int x, int y, int width, int height) {
        int[] pixels = new int[width * height];
        PixelGrabber pixelGrabber = new PixelGrabber(image, x, y, width, height, pixels, 0, width);

        try {
            pixelGrabber.grabPixels();
        } catch (InterruptedException e) {
        }

        return pixels;
    }

    public Image createImage(int[] pixels, int width, int height) {
        return this.createImage(pixels, width, height, null);
    }

    public Image createImage(int[] pixels, int width, int height, Component parent) {
        if (parent == null) {
            parent = this.applet;
        }

        Image image = parent.createImage(new MemoryImageSource(width, height, pixels, 0, width));

        while (!parent.prepareImage(image, parent)) {
            Tools.sleep(20L);
        }

        return image;
    }

    public Image[] separateImages(Image image, int length) {
        return this.separateImages(image, length, 1)[0];
    }

    public Image[][] separateImages(Image image, int length, int rows) {
        if (this.isDebug) {
            System.out.println("ImageManager.separateImages(...," + length + "," + rows + ")");
        }

        int width = this.getWidth(image);
        int height = this.getHeight(image);
        int imageWidth = width / length;
        int imageHeight = height / rows;
        if (this.isDebug && (width % length > 0 || height % rows > 0)) {
            System.out.println("ImageManager.separateImages(...,"
                    + length
                    + ","
                    + rows
                    + "): Warning! Source image can not be divided to "
                    + length
                    + "*"
                    + rows
                    + " blocks");
            Thread.dumpStack();
        }

        int[] allPixels = this.getPixels(image, width, height);
        Image[][] images = new Image[rows][length];

        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < length; ++col) {
                int[] pixels = new int[imageWidth * imageHeight];

                for (int y = 0; y < imageHeight; ++y) {
                    for (int x = 0; x < imageWidth; ++x) {
                        pixels[y * imageWidth + x] =
                                allPixels[row * width * imageHeight + y * width + col * imageWidth + x];
                    }
                }

                images[row][col] = this.createImage(pixels, imageWidth, imageHeight);
            }
        }

        return images;
    }

    public void destroy() {
        this.imageAliases.clear();
        this.imageAliases = null;
        this.applet = null;
    }

    public Applet getApplet() {
        return this.applet;
    }

    private String removeExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    private String getAlias(String image) {
        return this.imageAliases.getOrDefault(image, image);
    }
}
