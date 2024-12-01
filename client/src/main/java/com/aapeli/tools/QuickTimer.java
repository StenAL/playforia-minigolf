package com.aapeli.tools;

import java.util.ArrayList;
import java.util.List;

public class QuickTimer implements Runnable {

    private int anInt1727;
    private List<QuickTimerListener> listeners;
    private boolean stopped;

    public QuickTimer(int var1) {
        this(var1, null, false);
    }

    public QuickTimer(int var1, QuickTimerListener var2) {
        this(var1, var2, true);
    }

    private QuickTimer(int var1, QuickTimerListener var2, boolean var3) {
        this.anInt1727 = var1;
        this.listeners = new ArrayList<>();
        if (var2 != null) {
            this.addListener(var2);
        }

        this.stopped = false;
        if (var3) {
            this.start();
        }
    }

    public QuickTimer(QuickTimerListener var1, int var2) {
        this(var2, var1);
    }

    public void run() {
        Tools.sleep(this.anInt1727);
        if (!this.stopped) {
            for (QuickTimerListener var2 : this.listeners) {
                if (var2 != null) {
                    var2.qtFinished();
                }
            }
        }
    }

    public void addListener(QuickTimerListener listener) {
        synchronized (this.listeners) {
            this.listeners.add(listener);
        }
    }

    public void removeListener(QuickTimerListener listener) {
        synchronized (this.listeners) {
            this.listeners.remove(listener);
        }
    }

    public void start() {
        if (this.stopped) {
            throw new IllegalStateException("QuickTimer.start() called after QuickTimer.stopAll() was called");
        } else {
            Thread var1 = new Thread(this);
            var1.setDaemon(true);
            var1.start();
        }
    }

    public void stopAll() {
        this.stopped = true;
    }
}
