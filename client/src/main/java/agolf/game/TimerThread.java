package agolf.game;

import com.aapeli.tools.Tools;

class TimerThread implements Runnable {

    private final PlayerInfoPanel playerInfoPanel;
    private boolean running;


    protected TimerThread(PlayerInfoPanel playerInfoPanel) {
        this.playerInfoPanel = playerInfoPanel;
        this.running = true;
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void run() {
        do {
            Tools.sleep(1000L);
            if (this.running) {
                this.running = this.playerInfoPanel.run();
            }
        } while (this.running);

    }

    protected void stopRunning() {
        this.running = false;
    }
}
