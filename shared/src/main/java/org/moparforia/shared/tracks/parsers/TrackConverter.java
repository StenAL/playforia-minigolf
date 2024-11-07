package org.moparforia.shared.tracks.parsers;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.moparforia.shared.tracks.Track;
import org.moparforia.shared.tracks.TrackCategory;
import org.moparforia.shared.tracks.stats.TrackStats;

public class TrackConverter {
    private static final Logger logger = Logger.getLogger(TrackConverter.class.getName());

    public static List<TrackStats> loadOldTracks(Path path) throws IOException {
        List<TrackStats> tracks = new ArrayList<>();
        for (TrackCategory type : TrackCategory.values()) {
            if (type == TrackCategory.ALL || type == TrackCategory.UNKNOWN) {
                continue;
            }
            Path tracksPath = path.resolve(type.getDir());
            if (!Files.exists(tracksPath)) {
                logger.warning("Directory tracks/" + type.getDir() + " for type " + type + " was not found, ignoring.");
                continue;
            }
            TrackFileParser parser = new TrackFileParser(type);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(
                    tracksPath, entry -> entry.toString().endsWith(".track"));
            for (Path filePath : directoryStream) {
                tracks.add(parser.parseStats(filePath));
            }
        }
        return tracks;
    }

    public static void convertTracks(Path rootDir) throws IOException {
        List<TrackStats> allStats = loadOldTracks(rootDir);
        Collection<TrackStats> uniqueStats = consolidateTracks(allStats);
        Path tracks = rootDir.resolve("tracks");
        if (!Files.exists(tracks)) {
            Files.createDirectory(tracks);
        }

        int counter = 0;
        int ignored = 0;
        for (TrackStats stat : uniqueStats) {
            String name = stat.getTrack().getName().replaceAll("\\W+", "") + ".track";
            Path path = tracks.resolve(name);
            if (Files.exists(path)) {
                logger.info("Track " + stat.getTrack().getName() + " is already converted, ignoring");
                ignored++;
                continue;
            }
            Files.createFile(path);
            try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(path))) {
                writer.write(stat.serialize(System.lineSeparator()));
            }
            counter++;
        }
        logger.info("Converted " + counter + " tracks, ignored " + ignored + " tracks");
    }

    /**
     * Consolidates categories of duplicate tracks
     *
     * @param allStats List of all TrackStats
     * @return Consolidated collection of TrackStats
     */
    public static Collection<TrackStats> consolidateTracks(List<TrackStats> allStats) {
        Map<String, Track> distinct = allStats.stream()
                .map(TrackStats::getTrack)
                .collect(Collectors.toMap(Track::getName, p -> p, TrackConverter::combineTracks));

        return allStats.stream()
                .filter(stat -> distinct.containsKey(stat.getTrack().getName()))
                .collect(
                        Collectors.toMap(stats -> stats.getTrack().getName(), stats -> stats, (first, second) -> first))
                .values();
    }

    private static Track combineTracks(Track one, Track two) {
        Set<TrackCategory> categories = new HashSet<>(one.getCategories());
        categories.addAll(two.getCategories());
        one.setCategories(categories);
        return one;
    }
}
