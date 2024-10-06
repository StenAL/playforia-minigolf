package com.aapeli.client;

import com.aapeli.applet.AApplet;
import com.aapeli.tools.Tools;
import java.applet.Applet;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

public final class ImageManager {

    private Applet applet;
    private Hashtable<String, String> imageAliases;
    private boolean validImageDir;
    private final boolean isDebug;
    private final ImageLoaderThread imageLoaderThread;

    public ImageManager(Applet applet) {
        this(applet, "src/main/resources/picture/", true);
    }

    public ImageManager(Applet applet, boolean isDebug) {
        this(applet, "src/main/resources/picture/", isDebug);
    }

    public ImageManager(Applet applet, String imageDir) {
        this(applet, imageDir, true);
    }

    public ImageManager(Applet applet, String imageDir, boolean isDebug) {
        this.applet = applet;
        this.isDebug = isDebug;

        // TODO: Remove this code if it doesn't cause any problems in a few releases, I rewritten
        // the functionality
        this.validImageDir = true;
        if (imageDir != null && imageDir.length() > 0) {
            this.validImageDir = false;
        }

        this.imageAliases = new Hashtable<>();
        this.imageLoaderThread = new ImageLoaderThread(applet, isDebug);
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

        Image image = Toolkit.getDefaultToolkit()
                .createImage(this.getClass().getResource("/picture/agolf/" + getAlias(imageFileName)));
        this.imageLoaderThread.registerGameImage(name, image);
        return name;
    }

    public String defineSharedImage(String fileName) {
        return defineSharedImage(removeExtension(fileName), fileName);
    }

    public String defineSharedImage(String name, String imageFileName) {
        if (this.isDebug) {
            System.out.println("ImageManager.defineSharedImage(\"" + name + "\",\"" + imageFileName + "\")");
        }

        Image image = Toolkit.getDefaultToolkit()
                .createImage(this.getClass().getResource("/picture/shared/" + getAlias(imageFileName)));
        this.imageLoaderThread.registerSharedImage(name, image);
        return name;
    }

    public void unloadGameImage(String name) {
        this.imageLoaderThread.unloadGameImage(name);
    }

    public void startLoadingImages() {
        this.imageLoaderThread.startLoaderThread();
    }

    public boolean isLoadingFinished() {
        return this.imageLoaderThread.getLoadQueueSize() == 0;
    }

    public int getPercentOfImagesLoaded() {
        if (this.imageLoaderThread.getLoadQueueSize() == 0) {
            return 100;
        } else {
            int p = (int) (100.0D * this.getImageLoadProgress() + 0.5D);
            if (p == 0 && this.imageLoaderThread.getNumberOfLoadedImages() > 0) {
                p = 1;
            } else if (p == 100) {
                p = 99;
            }

            return p;
        }
    }

    public double getImageLoadProgress() {
        int queueSize = this.imageLoaderThread.getLoadQueueSize();
        if (queueSize == 0) {
            return 1.0D;
        } else {
            int loadedImages = this.imageLoaderThread.getNumberOfLoadedImages();
            int total = loadedImages + queueSize;
            return (double) loadedImages / (double) total;
        }
    }

    public Image getGameImage(String name) {
        return this.imageLoaderThread.getGameImage(this.getAlias(name));
    }

    public boolean isGameImageDefined(String name) {
        return this.imageLoaderThread.containsGameImage(this.getAlias(name));
    }

    public Image getGameImageIfLoaded(String name) {
        return this.imageLoaderThread.getGameImageIfLoaded(this.getAlias(name));
    }

    public Image getGameImageNonblocking(String name) {
        return this.imageLoaderThread.getGameImageNonblocking(this.getAlias(name));
    }

    public Image getShared(String name) {
        return this.getShared(name, false);
    }

    public Image getShared(String name, boolean nonblocking) {
        String extensionlessName = this.removeExtension(name);
        Image image = this.imageLoaderThread.getSharedImageIfLoaded(extensionlessName);
        if (image != null) {
            return image;
        } else {
            synchronized (this) {
                if (!this.imageLoaderThread.containsSharedImage(extensionlessName)) {
                    URL codebaseURL = this.applet.getCodeBase();

                    try {
                        if (codebaseURL.getProtocol().equalsIgnoreCase("file")) {
                            codebaseURL = new URL(codebaseURL, FileUtil.RESOURCE_DIR + "picture/");
                        } else {
                            codebaseURL = new URL(codebaseURL, "../Shared/picture/");
                        }
                    } catch (MalformedURLException e) {
                    }

                    URL url = codebaseURL;
                    try {
                        url = new URL(codebaseURL, name);
                    } catch (Exception ex) {
                    }
                    image = Toolkit.getDefaultToolkit().createImage(url);
                    this.imageLoaderThread.registerSharedImage(extensionlessName, image);
                }
            }
            return nonblocking ? null : this.imageLoaderThread.getSharedImage(extensionlessName);
        }
    }

    public int getWidth(Image image) {
        return image.getWidth(this.applet);
    }

    public int getHeight(Image image) {
        return image.getHeight(this.applet);
    }

    public int[] getPixels(Image image) {
        return this.getPixels(image, 0, 0, this.getWidth(image), this.getHeight(image));
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

    public Image getAlphaMultipliedImage(Image image, double opacity) {
        int width = this.getWidth(image);
        int height = this.getHeight(image);
        int[] pixels = this.getPixels(image, width, height);
        return this.createImage(this.multiplyAlpha(pixels, opacity), width, height);
    }

    public int[] multiplyAlpha(int[] pixels, double opacity) {
        int length = pixels.length;
        int[] newPixels = new int[length];

        for (int i = 0; i < length; ++i) {
            long alpha = ((long) pixels[i] & 4278190080L) >> 24;
            alpha = (long) ((double) alpha * opacity);
            if (alpha < 0L) {
                alpha = 0L;
            } else if (alpha > 255L) {
                alpha = 255L;
            }

            newPixels[i] = (int) ((alpha << 24) + ((long) pixels[i] & 16777215L));
        }

        return newPixels;
    }

    public void destroy() {
        this.imageLoaderThread.destroy();
        this.imageAliases.clear();
        this.imageAliases = null;
        this.applet = null;
    }

    public Applet getApplet() {
        return this.applet;
    }

    public void enableSUD(AApplet applet) {
        this.imageLoaderThread.setStartupDebugApplet(applet);
    }

    protected void registerRemoteImage(URL url) {
        String stringUrl = url.toString();
        synchronized (this) {
            if (!this.imageLoaderThread.containsRemoteImage(stringUrl)) {
                Image var4 = Toolkit.getDefaultToolkit().createImage(url);
                this.imageLoaderThread.registerRemoteImage(stringUrl, var4);
            }
        }
    }

    protected Image getRemoteImageIfLoaded(String name) {
        return this.imageLoaderThread.getRemoteImageIfLoaded(name);
    }

    private String removeExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    private String getAlias(String image) {
        return this.imageAliases.getOrDefault(image, image);
    }
}
