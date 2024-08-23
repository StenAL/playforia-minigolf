package com.aapeli.connection;

import com.aapeli.tools.Tools;
import java.util.ArrayList;
import java.util.List;

class GamePacketQueue implements Runnable {

    private Connection conn;
    private ConnListener connListener;
    private List<String> packets;
    private boolean running;
    private Thread thread;


    protected GamePacketQueue(Connection conn, ConnListener connListener) {
        this.conn = conn;
        this.connListener = connListener;
        this.packets = new ArrayList<>();
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void run() {
        while (true) {
            try {
                Tools.sleep(50L);

                String packet;
                while ((packet = this.nextGamePacket()) != null) {
                    this.connListener.dataReceived(packet);
                }

                if (this.running) {
                    continue;
                }
            } catch (Exception e) {
                this.running = false;
                this.conn.handleCrash();
            }

            return;
        }
    }

    protected synchronized void addGamePacket(String command) {
        this.packets.add(command);
    }

    protected void stop() {
        this.running = false;
    }

    private synchronized String nextGamePacket() {
        if (!this.packets.isEmpty() && this.running) {
            String packet = this.packets.getFirst();
            this.packets.removeFirst();
            return packet;
        } else {
            return null;
        }
    }
}
