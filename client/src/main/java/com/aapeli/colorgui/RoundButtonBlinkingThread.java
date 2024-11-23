package com.aapeli.colorgui;

import com.aapeli.tools.Tools;

class RoundButtonBlinkingThread implements Runnable {

    private RoundButton roundButton;
    private boolean running;

    protected RoundButtonBlinkingThread(RoundButton roundButton) {
        this.roundButton = roundButton;
        this.running = true;
    }

    public void run() {
        boolean state = false;

        do {
            Tools.sleep(500L);
            state = !state;
            if (this.running) {
                this.roundButton.innerSetFlashState(state);
            }
        } while (this.running);
    }

    protected void stop() {
        this.running = false;
    }
}
