package agolf.game;

import com.aapeli.tools.Tools;

class GamePlayerInfoPanelTimerThread implements Runnable {

    private final GamePlayerInfoPanel gamePlayerInfoPanel;
    private boolean running;


    protected GamePlayerInfoPanelTimerThread(GamePlayerInfoPanel gamePlayerInfoPanel) {
        this.gamePlayerInfoPanel = gamePlayerInfoPanel;
        this.running = true;
        Thread thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    public void run() {
        do {
            Tools.sleep(1000L);
            if (this.running) {
                this.running = this.gamePlayerInfoPanel.run();
            }
        } while (this.running);

    }

    protected void stopRunning() {
        this.running = false;
    }
}
