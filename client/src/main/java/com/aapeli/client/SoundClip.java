package com.aapeli.client;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;

class SoundClip {

    private Applet applet;
    private URL dir;
    private String file;
    private boolean debug;
    private AudioClip audioClip;
    private boolean defined;

    protected SoundClip(Applet applet, URL dir, String file, boolean debug) {
        this.applet = applet;
        this.dir = dir;
        this.file = file;
        this.debug = debug;
        this.audioClip = null;
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

            // todo this.audioClip = this.applet.getAudioClip(this.dir, this.file);
            URL url = dir;
            try {
                url = new URL(dir, file);
            } catch (Exception ex) {
                System.out.println("SoundClip.defineClip(): failed to load sound clip");
            }
            audioClip = Applet.newAudioClip(url);
            this.defined = true;
        }
    }

    protected AudioClip getAudioClip() {
        return this.audioClip;
    }
}
