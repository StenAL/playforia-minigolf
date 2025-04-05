package com.aapeli.client;

import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.Clip;

public final class SoundManager implements Runnable {
    private final boolean debug;
    private Map<Integer, SoundClip> sounds;
    private boolean clipLoaderThreadRunning;
    public int audioChoicerIndex;

    public SoundManager(boolean shouldLoadClips, boolean debug) {
        this.debug = debug;
        this.audioChoicerIndex = 0;
        this.defineSounds();

        this.clipLoaderThreadRunning = false;
        if (shouldLoadClips) {
            this.start();
        }
    }

    @Override
    public void run() {
        if (this.debug) {
            System.out.println("SoundManager.run(): Thread started");
        }

        for (SoundClip soundClip : this.sounds.values()) {
            try {
                if (!soundClip.isLoaded()) {
                    soundClip.load();
                }
            } catch (Exception e) {
                System.out.println("SoundManager.run(): Failed to define clip " + soundClip.getUrl() + ": " + e);
            }
        }

        this.clipLoaderThreadRunning = false;
        if (this.debug) {
            System.out.println("SoundManager.run(): Thread finished");
        }
    }

    public void startLoading() {
        this.start();
    }

    public void playChallenge() {
        if (this.debug) {
            System.out.println("SoundManager.playChallenge()");
        }

        this.playClip(1);
    }

    public void playGameMove() {
        if (this.debug) {
            System.out.println("SoundManager.playGameMove()");
        }

        this.playClip(2);
    }

    public void playNotify() {
        if (this.debug) {
            System.out.println("SoundManager.playNotify()");
        }

        this.playClip(3);
    }

    public void playIllegal() {
        if (this.debug) {
            System.out.println("SoundManager.playIllegal()");
        }

        this.playClip(4);
    }

    public void playTimeLow() {
        if (this.debug) {
            System.out.println("SoundManager.playTimeLow()");
        }

        this.playClip(5);
    }

    public void playGameWinner() {
        if (this.debug) {
            System.out.println("SoundManager.playGameWinner()");
        }

        this.playClip(6);
    }

    public void playGameLoser() {
        if (this.debug) {
            System.out.println("SoundManager.playGameLoser()");
        }

        this.playClip(7);
    }

    public void playGameDraw() {
        if (this.debug) {
            System.out.println("SoundManager.playGameDraw()");
        }

        this.playClip(8);
    }

    public void destroy() {
        this.sounds.clear();
        this.sounds = null;
    }

    protected boolean isDebug() {
        return this.debug;
    }

    private void defineSounds() {
        try {
            this.sounds = new HashMap<>();
            this.defineSoundClip(1, "/sound/shared/challenge.au");
            this.defineSoundClip(2, "/sound/shared/gamemove.au");
            this.defineSoundClip(3, "/sound/shared/notify.au");
            this.defineSoundClip(4, "/sound/shared/illegal.au");
            this.defineSoundClip(5, "/sound/shared/timelow.au");
            this.defineSoundClip(6, "/sound/shared/game-winner.au");
            this.defineSoundClip(7, "/sound/shared/game-loser.au");
            this.defineSoundClip(8, "/sound/shared/game-draw.au");
        } catch (Exception e) {
            System.out.println("SoundManager.loadClientSounds(): failed to load sounds: " + e);
        }
    }

    private void defineSoundClip(int id, String resourcePath) {
        this.sounds.put(id, new SoundClip(this.getClass().getResource(resourcePath), this.debug));
    }

    private synchronized void start() {
        if (!this.clipLoaderThreadRunning) {
            this.clipLoaderThreadRunning = true;
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void playClip(int id) {
        SoundClip soundClip = this.sounds.get(id);
        if (soundClip != null && this.audioChoicerIndex != 1) {
            Clip clip = soundClip.getClip();
            if (clip != null) {
                clip.setFramePosition(0);
                clip.start();
            }
        }
    }
}
