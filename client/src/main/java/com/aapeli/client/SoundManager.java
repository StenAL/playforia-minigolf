package com.aapeli.client;

import com.aapeli.applet.AApplet;

import java.applet.AudioClip;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

public final class SoundManager implements Runnable {

    private static final String[] methodLookup = {"stop", "play", "loop"};
    private AApplet applet;
    private URL sharedSoundDir;
    private final boolean loadSoundClipsOnRegister; // if false, users must call startLoading() to bulk load all registered sound clips
    private final boolean debug;
    private boolean startupDebug;
    private Hashtable<Integer, SoundClip> clientSounds;
    private Hashtable<String, SoundClip> sharedSounds;
    private boolean clipLoaderThreadRunning;
    public int audioChoicerIndex;


    public SoundManager(AApplet applet) {
        this(applet, true, false);
    }

    public SoundManager(AApplet applet, boolean debug) {
        this(applet, true, debug);
    }

    public SoundManager(AApplet applet, boolean loadClipsOnRegister, boolean debug) {
        this.startupDebug = false;
        this.applet = applet;
        this.loadSoundClipsOnRegister = loadClipsOnRegister;
        this.debug = debug;
        this.audioChoicerIndex = 0;
        this.loadClientSounds();
        this.sharedSoundDir = this.getClass().getResource("/sound/shared/");

        this.sharedSounds = new Hashtable<>();
        this.clipLoaderThreadRunning = false;
        if (loadClipsOnRegister) {
            this.loadAllSoundClips();
        }

    }

    @Override
    public void run() {
        if (this.debug) {
            System.out.println("SoundManager.run(): Thread started");
        }

        boolean anySoundClipsNotDefined;
        do {
            anySoundClipsNotDefined = false;
            Enumeration<SoundClip> soundClips = this.clientSounds.elements();

            SoundClip soundClip;
            while (soundClips.hasMoreElements()) {
                soundClip = soundClips.nextElement();
                if (!soundClip.isDefined()) {
                    soundClip.defineClip();
                    anySoundClipsNotDefined = true;
                }
            }

            soundClips = this.sharedSounds.elements();

            while (soundClips.hasMoreElements()) {
                soundClip = soundClips.nextElement();
                if (!soundClip.isDefined()) {
                    soundClip.defineClip();
                    anySoundClipsNotDefined = true;
                }
            }
        } while (anySoundClipsNotDefined);

        this.clipLoaderThreadRunning = false;
        if (this.debug) {
            System.out.println("SoundManager.run(): Thread finished");
        }

    }

    public void defineSharedSoundClip(String filename) {
        int clipNameLength = filename.lastIndexOf(".");
        String clipName = filename.substring(0, clipNameLength);
        this.defineSharedSoundClip(clipName, filename);
    }

    public void defineSharedSoundClip(String clipName, String soundFile) {
        if (this.debug) {
            System.out.println("SoundManager.defineSound(\"" + clipName + "\",\"" + soundFile + "\")");
        }

        if (this.startupDebug) {
            this.applet.printSUD("SoundManager: Defining sound \"" + soundFile + "\"");
        }

        SoundClip soundClip = new SoundClip(this.applet, this.sharedSoundDir, soundFile, this.debug);
        this.sharedSounds.put(clipName, soundClip);
        if (this.loadSoundClipsOnRegister) {
            this.loadAllSoundClips();
        }

    }

    public void startLoading() {
        this.loadAllSoundClips();
    }

    public void play(String clipName) {
        this.handleSharedSoundClip(clipName, 1);
    }

    public void loop(String clipName) {
        this.handleSharedSoundClip(clipName, 2);
    }

    public void stop(String clipName) {
        this.handleSharedSoundClip(clipName, 0);
    }

    public void playChallenge() {
        if (this.debug) {
            System.out.println("SoundManager.playChallenge()");
        }

        this.playAudioClip(1);
    }

    public void playGameMove() {
        if (this.debug) {
            System.out.println("SoundManager.playGameMove()");
        }

        this.playAudioClip(2);
    }

    public void playNotify() {
        if (this.debug) {
            System.out.println("SoundManager.playNotify()");
        }

        this.playAudioClip(3);
    }

    public void playIllegal() {
        if (this.debug) {
            System.out.println("SoundManager.playIllegal()");
        }

        this.playAudioClip(4);
    }

    public void playTimeLow() {
        if (this.debug) {
            System.out.println("SoundManager.playTimeLow()");
        }

        this.playAudioClip(5);
    }

    public void playGameWinner() {
        if (this.debug) {
            System.out.println("SoundManager.playGameWinner()");
        }

        this.playAudioClip(6);
    }

    public void playGameLoser() {
        if (this.debug) {
            System.out.println("SoundManager.playGameLoser()");
        }

        this.playAudioClip(7);
    }

    public void playGameDraw() {
        if (this.debug) {
            System.out.println("SoundManager.playGameDraw()");
        }

        this.playAudioClip(8);
    }

    public void destroy() {
        this.sharedSounds.clear();
        this.sharedSounds = null;
        this.clientSounds.clear();
        this.clientSounds = null;
        this.sharedSoundDir = null;
        this.applet = null;
    }

    public void enableSUD() {
        this.startupDebug = true;
    }

    protected boolean isDebug() {
        return this.debug;
    }

    private void loadClientSounds() {
        URL clientSoundsDir = this.getClass().getResource("/sound/shared/");

        this.clientSounds = new Hashtable<>();
        this.defineSoundClip(1, clientSoundsDir, "challenge");
        this.defineSoundClip(2, clientSoundsDir, "gamemove");
        this.defineSoundClip(3, clientSoundsDir, "notify");
        this.defineSoundClip(4, clientSoundsDir, "illegal");
        this.defineSoundClip(5, clientSoundsDir, "timelow");
        this.defineSoundClip(6, clientSoundsDir, "game-winner");
        this.defineSoundClip(7, clientSoundsDir, "game-loser");
        this.defineSoundClip(8, clientSoundsDir, "game-draw");
    }

    private void defineSoundClip(int id, URL soundDir, String clipName) {
        this.clientSounds.put(id, new SoundClip(this.applet, soundDir, clipName + ".au", this.debug));
    }

    private synchronized void loadAllSoundClips() {
        if (!this.clipLoaderThreadRunning) {
            this.clipLoaderThreadRunning = true;
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void playAudioClip(int id) {
        SoundClip soundClip = this.clientSounds.get(id);
        if (soundClip != null && this.audioChoicerIndex != 1) {
            AudioClip audioClip = soundClip.getAudioClip();
            if (audioClip != null) {
                audioClip.play();
            }
        }
    }

    private void handleSharedSoundClip(String clipName, int methodIndex) {
        try {
            if (this.debug) {
                System.out.println("SoundManager." + methodLookup[methodIndex] + "(\"" + clipName + "\")");
            }

            SoundClip soundClip = this.sharedSounds.get(clipName);
            if (soundClip != null) {
                AudioClip audioClip = soundClip.getAudioClip();
                if (audioClip != null) {
                    if (methodIndex == 0) {
                        audioClip.stop();
                    } else if (methodIndex == 1) {
                        audioClip.play();
                    } else if (methodIndex == 2) {
                        audioClip.loop();
                    }
                } else if (this.debug) {
                    System.out.println("SoundManager." + methodLookup[methodIndex] + "(\"" + clipName + "\"): AudioClip not ready!");
                }
            } else if (this.debug) {
                System.out.println("SoundManager." + methodLookup[methodIndex] + "(\"" + clipName + "\"): SoundClip not found!");
                Thread.dumpStack();
            }
        } catch (Exception var5) {
            System.out.println("SoundManager: Unexpected exception \"" + var5 + "\" when playing \"" + clipName + "\"");
        } catch (Error var6) {
            System.out.println("SoundManager: Unexpected error \"" + var6 + "\" when playing \"" + clipName + "\"");
        }

    }
}
