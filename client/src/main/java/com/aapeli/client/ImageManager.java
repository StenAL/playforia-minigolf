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
    private final ImageTracker imageTracker;


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

        // TODO: Remove this code if it doesn't cause any problems in a few releases, I rewritten the functionality
        this.validImageDir = true;
        if (imageDir != null && imageDir.length() > 0) {
            this.validImageDir = false;
        }

        this.imageAliases = new Hashtable<>();
        this.imageTracker = new ImageTracker(applet, isDebug);
    }

    public void setImageAliases(String[][] imageAliases) {
        if (imageAliases != null) {
            for (String[] aliases : imageAliases) {
                this.imageAliases.put(aliases[0], aliases[1]);
            }
        }
    }

    public String defineImage(String var1) {
        return this.defineImage(this.removeExtension(var1), var1);
    }

    public String defineImage(String imageAlias, String imageFileName) {
        if (this.isDebug) {
            System.out.println("ImageManager.defineImage(\"" + imageAlias + "\",\"" + imageFileName + "\")");
        }

        Image var3 = Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/picture/agolf/" +getAlias(imageFileName)));
        this.imageTracker.registerImage(imageAlias, var3);
        return imageAlias;
    }

    public String defineSharedImage(String var1) {
        return defineSharedImage(removeExtension(var1), var1);
    }

    public String defineSharedImage(String imageAlias, String imageFileName) {
        if (this.isDebug) {
            System.out.println("ImageManager.defineSharedImage(\"" + imageAlias + "\",\"" + imageFileName + "\")");
        }

        Image var3 = Toolkit.getDefaultToolkit().createImage(this.getClass().getResource("/picture/shared/" +getAlias(imageFileName)));
        this.imageTracker.method1624(imageAlias, var3);
        return imageAlias;
    }

    public void unDefineImage(String var1) {
        this.imageTracker.method1636(var1);
    }

    public void startLoadingImages() {
        this.imageTracker.method1626();
    }

    public boolean isLoadingFinished() {
        return this.imageTracker.numberOfImages() == 0;
    }

    public int getPercentOfImagesLoaded() {
        if (this.imageTracker.numberOfImages() == 0) {
            return 100;
        } else {
            int var1 = (int) (100.0D * this.getImageLoadProgress() + 0.5D);
            if (var1 == 0 && this.imageTracker.method1638() > 0) {
                var1 = 1;
            } else if (var1 == 100) {
                var1 = 99;
            }

            return var1;
        }
    }

    public double getImageLoadProgress() {
        int var1 = this.imageTracker.numberOfImages();
        if (var1 == 0) {
            return 1.0D;
        } else {
            int var2 = this.imageTracker.method1638();
            int var3 = var2 + var1;
            return 1.0D * (double) var2 / (double) var3;
        }
    }

    public Image getImage(String var1) {
        return this.imageTracker.getNImage(this.getAlias(var1));
    }

    public boolean isDefined(String var1) {
        return this.imageTracker.containsNImage(this.getAlias(var1));
    }

    public Image getIfAvailable(String var1) {
        return this.imageTracker.getNImageFromTable(this.getAlias(var1));
    }

    public Image getEvenNotLoaded(String var1) {
        return this.imageTracker.method1629(this.getAlias(var1));
    }

    public Image getShared(String var1) {
        return this.getShared(var1, false);
    }

    public Image getShared(String var1, boolean var2) {
        String var3 = this.removeExtension(var1);
        Image var4 = this.imageTracker.method1631(var3);
        if (var4 != null) {
            return var4;
        } else {
            synchronized (this) {
                if (!this.imageTracker.containsSImage(var3)) {
                    URL codebaseURL = this.applet.getCodeBase();

                    try {
                        if (codebaseURL.getProtocol().equalsIgnoreCase("file")) {
                            codebaseURL = new URL(codebaseURL, FileUtil.RESOURCE_DIR + "picture/");
                        } else {
                            codebaseURL = new URL(codebaseURL, "../Shared/picture/");
                        }
                    } catch (MalformedURLException var9) {
                    }

                    URL url = codebaseURL;
                    try {
                        url = new URL(codebaseURL, var1);
                    } catch (Exception ex) {
                    }
                    var4 = Toolkit.getDefaultToolkit().createImage(url);
                    //todo var4 = this.anApplet1354.getImage(var6, var1);
                    //var4 = this.applet.getImage(var6, var1);
                    this.imageTracker.method1624(var3, var4);
                }
            }
            return var2 ? null : this.imageTracker.method1630(var3);
        }
    }

    public int getWidth(Image var1) {
        return var1.getWidth(this.applet);
    }

    public int getHeight(Image var1) {
        return var1.getHeight(this.applet);
    }

    public int[] getPixels(Image var1) {
        return this.getPixels(var1, 0, 0, this.getWidth(var1), this.getHeight(var1));
    }

    public int[] getPixels(Image var1, int var2, int var3) {
        return this.getPixels(var1, 0, 0, var2, var3);
    }

    public int[] getPixels(Image var1, int var2, int var3, int var4, int var5) {
        int[] var6 = new int[var4 * var5];
        PixelGrabber var7 = new PixelGrabber(var1, var2, var3, var4, var5, var6, 0, var4);

        try {
            var7.grabPixels();
        } catch (InterruptedException var9) {
        }

        return var6;
    }

    public Image createImage(int[] var1, int var2, int var3) {
        return this.createImage(var1, var2, var3, null);
    }

    public Image createImage(int[] var1, int var2, int var3, Component var4) {
        if (var4 == null) {
            var4 = this.applet;
        }

        Image var5 = var4.createImage(new MemoryImageSource(var2, var3, var1, 0, var2));

        while (!var4.prepareImage(var5, var4)) {
            Tools.sleep(20L);
        }

        return var5;
    }

    public Image[] separateImages(Image var1, int var2) {
        return this.separateImages(var1, var2, 1)[0];
    }

    public Image[][] separateImages(Image var1, int var2, int var3) {
        if (this.isDebug) {
            System.out.println("ImageManager.separateImages(...," + var2 + "," + var3 + ")");
        }

        int var4 = this.getWidth(var1);
        int var5 = this.getHeight(var1);
        int var6 = var4 / var2;
        int var7 = var5 / var3;
        if (this.isDebug && (var4 % var2 > 0 || var5 % var3 > 0)) {
            System.out.println("ImageManager.separateImages(...," + var2 + "," + var3 + "): Warning! Source image can not be divided to " + var2 + "*" + var3 + " blocks");
            Thread.dumpStack();
        }

        int[] var8 = this.getPixels(var1, var4, var5);
        Image[][] var10 = new Image[var3][var2];

        for (int var11 = 0; var11 < var3; ++var11) {
            for (int var12 = 0; var12 < var2; ++var12) {
                int[] var9 = new int[var6 * var7];

                for (int var13 = 0; var13 < var7; ++var13) {
                    for (int var14 = 0; var14 < var6; ++var14) {
                        var9[var13 * var6 + var14] = var8[var11 * var4 * var7 + var13 * var4 + var12 * var6 + var14];
                    }
                }

                var10[var11][var12] = this.createImage(var9, var6, var7);
            }
        }

        return var10;
    }

    public Image getAlphaMultipliedImage(Image var1, double var2) {
        int var4 = this.getWidth(var1);
        int var5 = this.getHeight(var1);
        int[] var6 = this.getPixels(var1, var4, var5);
        return this.createImage(this.multiplyAlpha(var6, var2), var4, var5);
    }

    public int[] multiplyAlpha(int[] var1, double var2) {
        int var4 = var1.length;
        int[] var5 = new int[var4];

        for (int var8 = 0; var8 < var4; ++var8) {
            long var6 = ((long) var1[var8] & 4278190080L) >> 24;
            var6 = (long) ((double) var6 * var2);
            if (var6 < 0L) {
                var6 = 0L;
            } else if (var6 > 255L) {
                var6 = 255L;
            }

            var5[var8] = (int) ((var6 << 24) + ((long) var1[var8] & 16777215L));
        }

        return var5;
    }

    public void destroy() {
        this.imageTracker.removeAllImageResources();
        this.imageAliases.clear();
        this.imageAliases = null;
        this.applet = null;
    }

    public Applet getApplet() {
        return this.applet;
    }

    public void enableSUD(AApplet var1) {
        this.imageTracker.method1640(var1);
    }

    protected void method1594(URL var1) {
        String var2 = var1.toString();
        synchronized (this) {
            if (!this.imageTracker.containsCImage(var2)) {
                //todo Image var4 = this.applet.getImage(var1);
                Image var4 = Toolkit.getDefaultToolkit().createImage(var1);
                this.imageTracker.method1625(var2, var4);
            }

        }
    }

    protected Image method1595(String var1) {
        return this.imageTracker.method1632(var1);
    }

    private String removeExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    private String getAlias(String var1) {
        return this.imageAliases.getOrDefault(var1, var1);
    }
}
