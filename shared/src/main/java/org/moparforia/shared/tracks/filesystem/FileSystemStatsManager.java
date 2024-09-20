package org.moparforia.shared.tracks.filesystem;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.moparforia.shared.tracks.Track;
import org.moparforia.shared.tracks.TracksLocation;
import org.moparforia.shared.tracks.parsers.TrackParser;
import org.moparforia.shared.tracks.parsers.VersionedTrackFileParser;
import org.moparforia.shared.tracks.stats.StatsManager;
import org.moparforia.shared.tracks.stats.TrackStats;

public class FileSystemStatsManager implements StatsManager {
    private static FileSystemStatsManager instance;

    private final Logger logger = Logger.getLogger(FileSystemStatsManager.class.getName());
    private final TrackParser parser = new VersionedTrackFileParser();

    private Map<Track, TrackStats> stats;

    public FileSystemStatsManager() {}

    public static FileSystemStatsManager getInstance() {
        if (instance == null) {
            instance = new FileSystemStatsManager();
        }
        return instance;
    }

    public void load(TracksLocation tracksLocation) throws IOException {
        stats = loadStats(tracksLocation);
        logger.info("Loaded stats for " + stats.size() + " tracks");
    }

    public Map<Track, TrackStats> loadStats(TracksLocation tracksLocation) throws IOException {
        List<TrackStats> tracks = new ArrayList<>();

        Path tracksPath = tracksLocation.fileSystem().getPath(tracksLocation.path(), "tracks");
        if (!Files.exists(tracksPath)) {
            logger.warning("Directory " + tracksLocation.path() + "/tracks was not found, ignoring.");
            return Collections.emptyMap();
        }
        DirectoryStream<Path> directoryStream =
                Files.newDirectoryStream(tracksPath, entry -> entry.toString().endsWith(".track"));
        for (Path filePath : directoryStream) {
            try {
                tracks.add(parser.parseStats(filePath));
            } catch (IOException e) {
                logger.warning("Unable to parse file " + filePath);
                logger.info(e.toString());
            }
        }
        return tracks.stream().collect(Collectors.toMap(TrackStats::getTrack, stats -> stats));
    }

    @Override
    public TrackStats getStats(Track track) {
        if (!stats.containsKey(track)) {
            throw new IllegalArgumentException("Couldn't find stats for track " + track.getName() + " on filesystem");
        }
        return stats.get(track);
    }

    @Override
    public void rate(Track track, int rating) {
        // No-op, FileSystemStats are readonly
    }

    @Override
    public void addScore(Track track, String player, int par) {
        // No-op, FileSystemStats are readonly
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
