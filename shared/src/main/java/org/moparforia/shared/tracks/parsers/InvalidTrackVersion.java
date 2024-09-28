package org.moparforia.shared.tracks.parsers;

import java.io.IOException;

public class InvalidTrackVersion extends IOException {
    public InvalidTrackVersion() {}

    public InvalidTrackVersion(String s) {
        super(s);
    }
}
