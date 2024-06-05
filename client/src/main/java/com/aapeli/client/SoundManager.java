package com.aapeli.client;

import com.aapeli.applet.AApplet;

import javax.sound.sampled.Clip;
import java.net.URI;
import java.util.Enumeration;
import java.util.Hashtable;

public final class SoundManager implements Runnable {

    private static final String[] methodLookup = {"stop", "play", "loop"};
    private AApplet applet;
    private URI sharedSoundDir;
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

        SoundClip soundClip = new SoundClip(this.sharedSoundDir, soundFile, this.debug);
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
        try {
            this.sharedSoundDir = this.getClass().getResource("/sound/shared/").toURI();
            this.clientSounds = new Hashtable<>();
            URI clientSoundsDir = this.getClass().getResource("/sound/shared/").toURI();
            this.defineSoundClip(1, clientSoundsDir, "challenge");
            this.defineSoundClip(2, clientSoundsDir, "gamemove");
            this.defineSoundClip(3, clientSoundsDir, "notify");
            this.defineSoundClip(4, clientSoundsDir, "illegal");
            this.defineSoundClip(5, clientSoundsDir, "timelow");
            this.defineSoundClip(6, clientSoundsDir, "game-winner");
            this.defineSoundClip(7, clientSoundsDir, "game-loser");
            this.defineSoundClip(8, clientSoundsDir, "game-draw");
        } catch (Exception e) {
            System.out.println("SoundManager.loadClientSounds(): failed to load sounds: " + e);
        }
    }

    private void defineSoundClip(int id, URI soundDir, String clipName) {
        this.clientSounds.put(id, new SoundClip(soundDir, clipName + ".au", this.debug));
    }

    private synchronized void loadAllSoundClips() {
        if (!this.clipLoaderThreadRunning) {
            this.clipLoaderThreadRunning = true;
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void playClip(int id) {
        SoundClip soundClip = this.clientSounds.get(id);
        if (soundClip != null && this.audioChoicerIndex != 1) {
            Clip clip = soundClip.getClip();
            if (clip != null) {
                clip.setFramePosition(0);
                clip.start();
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
                Clip clip = soundClip.getClip();
                if (clip != null) {
                    if (methodIndex == 0) {
                        clip.stop();
                    } else if (methodIndex == 1) {
                        clip.setFramePosition(0);
                        clip.start();
                    } else if (methodIndex == 2) {
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    }
                } else if (this.debug) {
                    System.out.println("SoundManager." + methodLookup[methodIndex] + "(\"" + clipName + "\"): AudioClip not ready!");
                }
            } else if (this.debug) {
                System.out.println("SoundManager." + methodLookup[methodIndex] + "(\"" + clipName + "\"): SoundClip not found!");
                Thread.dumpStack();
            }
        } catch (Exception e) {
            System.out.println("SoundManager: Unexpected exception \"" + e + "\" when playing \"" + clipName + "\"");
        } catch (Error e) {
            System.out.println("SoundManager: Unexpected error \"" + e + "\" when playing \"" + clipName + "\"");
        }

    }
}
