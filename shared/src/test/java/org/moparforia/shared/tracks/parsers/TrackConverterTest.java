package org.moparforia.shared.tracks.parsers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.moparforia.shared.tracks.Track;
import org.moparforia.shared.tracks.TracksLocation;
import org.moparforia.shared.tracks.filesystem.FileSystemStatsManager;
import org.moparforia.shared.tracks.stats.TrackStats;
import org.moparforia.shared.tracks.util.FileSystemExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrackConverterTest {
    private final String[] DIRS = new String[]{
            "tracks/modern",
            "tracks/traditional",
            "tracks/short",
            "tracks/long",
            "tracks/basic",
            "tracks/hio",
    };

    @RegisterExtension
    final FileSystemExtension extension = new FileSystemExtension("v1/", DIRS);

    @Test
    void testConvertTracks() throws IOException, URISyntaxException {
        for (String dir : DIRS) {
            extension.copyDir(dir);
        }
        FileSystemStatsManager statsManager = new FileSystemStatsManager();
        TracksLocation tracksLocation = new TracksLocation(this.extension.getFileSystem(), "tracks");


        Path tracks = extension.getFileSystem().getPath("tracks");
        List<TrackStats> stats = TrackConverter.loadOldTracks(tracks);
        Collection<TrackStats> consolidated = TrackConverter.consolidateTracks(stats);

        TrackConverter.convertTracks(tracks);

        statsManager.load(tracksLocation);

        for (TrackStats stat : consolidated) {
            Track track = stat.getTrack();
            assertEquals(stat, statsManager.getStats(track));
        }
    }
}