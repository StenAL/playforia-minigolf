package org.moparforia.server.net;


import io.netty.util.AttributeKey;


public final class ClientState {

    public static final AttributeKey<ClientState> CLIENT_STATE_ATTRIBUTE_KEY =
            AttributeKey.valueOf("MESSAGE_COUNTS");

    private long sentCount;
    private long recvCount;
    private long lastActivity;

    public ClientState() {
        this.sentCount = 0;
        this.recvCount = 0;
        this.lastActivity = System.currentTimeMillis();
    }

    public long getSentCount() {
        return sentCount;
    }

    public void setSentCount(long sentCount) {
        this.sentCount = sentCount;
    }

    public long getRecvCount() {
        return recvCount;
    }

    public void setRecvCount(long recvCount) {
        this.recvCount = recvCount;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }
}
