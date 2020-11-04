package org.moparforia.shared.tracks.parsers;

import org.moparforia.shared.tracks.Track;
import org.moparforia.shared.tracks.stats.TrackStats;

import java.io.IOException;
import java.nio.file.Path;

public interface TrackParser {
    Track parseTrack(Path path) throws IOException;

    TrackStats parseStats(Path path) throws IOException;
}
