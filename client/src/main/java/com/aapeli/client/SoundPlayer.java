package com.aapeli.client;

import com.aapeli.tools.Tools;

import java.util.Vector;

public final class SoundPlayer implements Runnable {

    private final SoundManager soundManager;
    private final Vector<ClipPlaybackTask>[] soundQueues; // index of queue == priority, higher == more important
    private boolean stayAlive;
    private final Thread thread;


    public SoundPlayer(SoundManager soundManager) {
        this.soundManager = soundManager;
        this.soundQueues = new Vector[10];

        for (int i = 0; i < 10; ++i) {
            this.soundQueues[i] = new Vector<>();
        }

        this.stayAlive = true;
        this.thread = new Thread(this);
        this.thread.setDaemon(true);
        this.thread.start();
    }

    @Override
    public void run() {
        if (this.soundManager.isDebug()) {
            System.out.println("SoundPlayer: Started");
        }

        do {
            Tools.sleep(1000L);
            String nextClipName;
            if (this.stayAlive) {
                while ((nextClipName = this.getNextClip()) != null) {
                    this.soundManager.play(nextClipName);
                }
            }
        } while (this.stayAlive);

        if (this.soundManager.isDebug()) {
            System.out.println("SoundPlayer: Stopped");
        }

    }

    public void play(String clipName) {
        this.play(clipName, 5, 1000);
    }

    public void play(String clipName, int priority) {
        this.play(clipName, priority, 1000);
    }

    public void play(String clipName, int priority, int timeoutMs) {
        if (priority < 0) {
            priority = 0;
        }

        if (priority >= 10) {
            priority = 9;
        }

        if (timeoutMs < 0) {
            timeoutMs = 0;
        }

        ClipPlaybackTask clipPlaybackTask = new ClipPlaybackTask(clipName, timeoutMs);
        synchronized (this.soundQueues[priority]) {
            this.soundQueues[priority].addElement(clipPlaybackTask);
        }

        this.thread.interrupt();
    }

    public void stop() {
        this.stayAlive = false;
    }

    private String getNextClip() {
        for (int i = 9; i >= 0; --i) {
            String soundTask = this.getFirstTaskFromQueue(this.soundQueues[i]);
            if (soundTask != null) {
                return soundTask;
            }
        }

        return null;
    }

    private String getFirstTaskFromQueue(Vector<ClipPlaybackTask> soundQueue) {
        ClipPlaybackTask clipPlaybackTask;
        synchronized (soundQueue) {
            if (soundQueue.size() == 0) {
                return null;
            }

            clipPlaybackTask = soundQueue.elementAt(0);
            soundQueue.removeElementAt(0);
        }

        String clipName = clipPlaybackTask.getClipName();
        if (System.currentTimeMillis() > clipPlaybackTask.getTimeoutTimestamp()) {
            if (this.soundManager.isDebug()) {
                System.out.println("SoundPlayer: \"" + clipName + "\" timed out");
            }

            return this.getFirstTaskFromQueue(soundQueue);
        } else {
            return clipName;
        }
    }
}
