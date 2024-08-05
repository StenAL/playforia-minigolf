package com.aapeli.tools;

import java.util.Vector;

public class QuickTimer implements Runnable {

    private int anInt1727;
    private Vector<QuickTimerListener> aVector1728;
    private boolean stopped;
    private static final String aString1730 = "QuickTimer.start() called after QuickTimer.stopAll() was called";


    public QuickTimer(int var1) {
        this(var1, null, false);
    }

    public QuickTimer(int var1, QuickTimerListener var2) {
        this(var1, var2, true);
    }

    private QuickTimer(int var1, QuickTimerListener var2, boolean var3) {
        this.anInt1727 = var1;
        this.aVector1728 = new Vector<>();
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
            for (QuickTimerListener var2: this.aVector1728) {
                if (var2 != null) {
                    var2.qtFinished();
                }
            }

        }
    }

    public void addListener(QuickTimerListener var1) {
        this.aVector1728.addElement(var1);
    }

    public void removeListener(QuickTimerListener var1) {
        this.aVector1728.removeElement(var1);
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
