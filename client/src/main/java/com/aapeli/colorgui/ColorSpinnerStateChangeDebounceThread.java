package com.aapeli.colorgui;

import com.aapeli.tools.Tools;

class ColorSpinnerStateChangeDebounceThread implements Runnable {

    private ColorSpinner colorSpinner;
    private int delay;
    private boolean running;

    protected ColorSpinnerStateChangeDebounceThread(ColorSpinner spinner, int delay) {
        this.colorSpinner = spinner;
        this.delay = delay;
        this.running = true;
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void run() {
        Tools.sleep(this.delay);
        if (this.running) {
            this.colorSpinner.notifyListeners();
        }
    }

    protected void stop() {
        this.running = false;
    }
}
