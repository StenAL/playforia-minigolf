package com.aapeli.client;

import com.aapeli.applet.AApplet;
import com.aapeli.tools.Tools;
import java.applet.Applet;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

class ImageLoaderThread implements Runnable {

    private Applet applet;
    private boolean debug;

    private List<ImageLoadTask> loadQueue;
    private Hashtable<String, Image> images;
    private AApplet startupDebugApplet;
    private Thread thread;
    private boolean destroyed;

    protected ImageLoaderThread(Applet applet, boolean debug) {
        this.applet = applet;
        this.debug = debug;
        this.loadQueue = new ArrayList<>();
        this.images = new Hashtable<>();
        this.startupDebugApplet = null;
        this.destroyed = false;
    }

    public void run() {
        while (!this.destroyed && this.loadImages()) {
            Tools.sleep(10L);
        }

        synchronized (this) {
            this.thread = null;
        }
    }

    protected void registerGameImage(String name, Image image) {
        name = "N\t" + name;
        synchronized (this) {
            if (!this.isDefined(name)) {
                this.loadQueue.add(new ImageLoadTask(name, image));
            }
        }
    }

    protected void registerSharedImage(String name, Image image) {
        name = "S\t" + name;
        synchronized (this) {
            if (!this.isDefined(name)) {
                this.loadQueue.addFirst(new ImageLoadTask(name, image));
            }
        }
    }

    protected void registerRemoteImage(String name, Image image) {
        name = "C\t" + name;
        synchronized (this) {
            if (!this.isDefined(name)) {
                this.loadQueue.add(new ImageLoadTask(name, image));
            }
        }
    }

    protected synchronized void startLoaderThread() {
        if (this.thread == null) {
            if (!this.loadQueue.isEmpty()) {
                if (!this.destroyed) {
                    this.thread = new Thread(this);
                    this.thread.start();
                }
            }
        }
    }

    protected Image getGameImage(String name) {
        return this.getImage("N\t" + name, false);
    }

    protected Image getGameImageIfLoaded(String name) {
        return this.getImageIfLoaded("N\t" + name);
    }

    protected Image getGameImageNonblocking(String imageAlias) {
        return this.getImage("N\t" + imageAlias, true);
    }

    protected Image getSharedImage(String name) {
        return this.getImage("S\t" + name, false);
    }

    protected Image getSharedImageIfLoaded(String name) {
        return this.getImageIfLoaded("S\t" + name);
    }

    protected Image getRemoteImageIfLoaded(String name) {
        return this.getImageIfLoaded("C\t" + name);
    }

    protected boolean containsGameImage(String name) {
        return this.isDefined("N\t" + name);
    }

    protected boolean containsSharedImage(String name) {
        return this.isDefined("S\t" + name);
    }

    protected boolean containsRemoteImage(String name) {
        return this.isDefined("C\t" + name);
    }

    protected void unloadGameImage(String name) {
        name = "N\t" + name;
        synchronized (this) {
            if (this.images.remove(name) == null) {
                this.removeFromQueue(name);
            }
        }
    }

    protected int getLoadQueueSize() {
        return this.loadQueue.size();
    }

    protected int getNumberOfLoadedImages() {
        return this.images.size();
    }

    protected void destroy() {
        this.destroyed = true;
        if (this.thread != null) {
            int sleepTime = 500;
            byte step = 50;

            while (this.thread != null && sleepTime > 0) {
                sleepTime -= step;
                Tools.sleep(step);
            }
        }

        for (Image image : this.images.values()) {
            try {
                image.flush();
            } catch (Exception e) {
            }
        }

        this.images.clear();
        this.images = null;
        for (ImageLoadTask imageLoadTask : this.loadQueue) {
            try {
                imageLoadTask.destroy();
            } catch (Exception e) {
            }
        }

        this.loadQueue.clear();
        this.loadQueue = null;
        this.applet = null;
    }

    protected void setStartupDebugApplet(AApplet startupDebugApplet) {
        this.startupDebugApplet = startupDebugApplet;
    }

    private Image getImage(String name, boolean nonBlocking) {
        Image image;
        ImageLoadTask imageLoadTask;
        synchronized (this) {
            image = this.getImageIfLoaded(name);
            if (image != null) {
                return image;
            }

            imageLoadTask = this.getImageLoadTask(name);
            if (imageLoadTask == null) {
                return null;
            }
        }

        this.startLoaderThread();
        if (nonBlocking) {
            return imageLoadTask.getImage();
        } else {
            do {
                Tools.sleep(100L);
                image = this.images.get(name);
            } while (image == null);

            return image;
        }
    }

    private Image getImageIfLoaded(String key) {
        Image image = this.images.get(key);
        if (image != null) {
            return image;
        } else {
            this.startLoaderThread();
            return null;
        }
    }

    private synchronized boolean isDefined(String name) {
        return this.images.containsKey(name) ? true : this.getImageLoadTask(name) != null;
    }

    private synchronized ImageLoadTask getImageLoadTask(String name) {
        for (ImageLoadTask task : this.loadQueue) {
            if (task.getName().equals(name)) {
                return task;
            }
        }

        return null;
    }

    private boolean loadImages() {
        ImageLoadTask task;
        synchronized (this) {
            if (this.loadQueue.isEmpty()) {
                return false;
            }

            task = this.loadQueue.getFirst();
        }

        String name = task.getName();
        Image imageToLoad = task.getImage();
        task.addLoadAttempt();
        if (this.startupDebugApplet != null) {
            this.startupDebugApplet.printSUD("ImageTracker: Start loading image \"" + name + "\"");
        }

        int maxLoadTime = 5000;
        byte step = 10;

        while (!this.applet.prepareImage(imageToLoad, this.applet)) {
            if (this.destroyed) {
                return false;
            }

            maxLoadTime -= step;
            if (maxLoadTime <= 0) {
                this.imageLoadingTimedOut(name);
                return true;
            }

            Tools.sleep(step);
        }

        if (this.startupDebugApplet != null) {
            this.startupDebugApplet.printSUD("ImageTracker: Finished loading image \"" + name + "\"");
        }

        if (this.debug) {
            System.out.println("ImageTracker: Loaded image \"" + name + "\", moving from 'notloaded' to 'loaded'");
        }

        synchronized (this) {
            this.removeFromQueue(name);
            this.images.put(name, imageToLoad);
            return true;
        }
    }

    private synchronized ImageLoadTask removeFromQueue(String name) {
        int n = this.loadQueue.size();

        for (int i = 0; i < n; ++i) {
            ImageLoadTask resource = this.loadQueue.get(i);
            if (resource.getName().equals(name)) {
                this.loadQueue.remove(i);
                return resource;
            }
        }

        return null;
    }

    private synchronized void imageLoadingTimedOut(String name) {
        ImageLoadTask task = this.removeFromQueue(name);
        if (task != null && !task.tooManyLoadAttempts()) {
            this.loadQueue.add(task);
        }
    }
}
