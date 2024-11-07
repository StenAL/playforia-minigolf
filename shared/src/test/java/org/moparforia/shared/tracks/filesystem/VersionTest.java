package org.moparforia.shared.tracks.filesystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.moparforia.shared.tracks.TrackLoadException;
import org.moparforia.shared.tracks.TrackManager;
import org.moparforia.shared.tracks.TracksLocation;
import org.moparforia.shared.tracks.util.FileSystemExtension;

public class VersionTest {
    @RegisterExtension
    final FileSystemExtension extension = new FileSystemExtension("v2/invalid");

    @Test
    void testTrackManagerInvalidVersions() throws IOException, URISyntaxException, TrackLoadException {
        extension.copyAll();
        TracksLocation tracksLocation = new TracksLocation(extension.getFileSystem(), "tracks");

        TrackManager manager = new FileSystemTrackManager();
        manager.load(tracksLocation);

        assertEquals(1, manager.getTracks().size());
    }
}
