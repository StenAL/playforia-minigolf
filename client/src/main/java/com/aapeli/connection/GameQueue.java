package com.aapeli.connection;

import java.util.ArrayList;
import java.util.List;

class GameQueue {

    private List<String> commands = new ArrayList<>();
    private int count = 0;
    private ConnCipher connCipher = new ConnCipher((int) (Math.random() * 19.0D));
    protected long sendSeqNum;


    protected GameQueue() {
        this.connCipher.initialise(ConnCipher.getRandomSeed());
        this.sendSeqNum = 0L;
    }

    protected void add(String command) {
        synchronized (this) {
            long sendSequenceNumber = this.sendSeqNum++;
            command = this.connCipher.encrypt(sendSequenceNumber + " " + command);
            this.commands.add(command);
        }
    }

    protected String pop() {
        if (this.commands.size() <= this.count) {
            return null;
        } else {
            String var1 = this.commands.get(this.count);
            var1 = this.connCipher.decrypt(var1);
            if (this.commands.size() > 3) {
                this.commands.removeFirst();
            } else {
                ++this.count;
            }

            return var1;
        }
    }

    protected void clear() {
        this.count = 0;
    }
}
