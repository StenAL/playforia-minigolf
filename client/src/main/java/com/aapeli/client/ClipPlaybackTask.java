package com.aapeli.client;

class ClipPlaybackTask {

    private String clipName;
    private long timeoutTimestamp;


    public ClipPlaybackTask(String clipName, int timeoutMs) {
        this.clipName = clipName;
        this.timeoutTimestamp = System.currentTimeMillis() + (long) timeoutMs;
    }

    protected String getClipName() {
        return this.clipName;
    }

    protected long getTimeoutTimestamp() {
        return this.timeoutTimestamp;
    }
}
