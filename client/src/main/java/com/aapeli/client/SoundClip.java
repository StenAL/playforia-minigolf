package com.aapeli.client;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URI;

class SoundClip {
    private URI dir;
    private String file;
    private boolean debug;
    private Clip clip;
    private boolean defined;


    protected SoundClip(URI dir, String file, boolean debug) {
        this.dir = dir;
        this.file = file;
        this.debug = debug;
        this.clip = null;
        this.defined = false;
    }

    protected boolean isDefined() {
        return this.defined;
    }

    protected void defineClip() {
        if (!this.defined) {
            if (this.debug) {
                System.out.println("SoundClip.defineClip(): 'dir'=\"" + this.dir + "\", 'file'=\"" + this.file + "\"");
            }

            //todo this.audioClip = this.applet.getAudioClip(this.dir, this.file);
            try {
                URI uri = dir.resolve(file);
                AudioInputStream sound = AudioSystem.getAudioInputStream(uri.toURL());
                this.clip = AudioSystem.getClip();
                clip.open(sound);
                this.defined = true;
            } catch (Exception ex) {
                System.out.println("SoundClip.defineClip(): failed to load sound clip");
            }
        }
    }

    protected Clip getClip() {
        return this.clip;
    }
}
