package org.moparforia.shared.tracks.filesystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.moparforia.shared.tracks.TrackLoadException;
import org.moparforia.shared.tracks.TrackManager;
import org.moparforia.shared.tracks.util.FileSystemExtension;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VersionTest {
    @RegisterExtension
    final FileSystemExtension extension = new FileSystemExtension("v2/invalid");

    @Test
    void testTrackManagerInvalidVersions() throws IOException, URISyntaxException, TrackLoadException {
        extension.copyAll();

        TrackManager manager = new FileSystemTrackManager(extension.getFileSystem());
        manager.load();

        assertEquals(1, manager.getTracks().size());
    }

}
