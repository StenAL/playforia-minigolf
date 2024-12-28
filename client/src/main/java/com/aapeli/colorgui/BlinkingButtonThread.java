package com.aapeli.colorgui;

import com.aapeli.tools.Tools;

class BlinkingButtonThread implements Runnable {

    private ColorButton button;
    private boolean enabled;

    protected BlinkingButtonThread(ColorButton button) {
        this.button = button;
        this.enabled = true;
    }

    public void run() {
        boolean state = false;

        do {
            Tools.sleep(500L);
            state = !state;
            if (this.enabled) {
                this.button.setBlinkState(state);
            }
        } while (this.enabled);
    }

    protected void disable() {
        this.enabled = false;
    }
}
