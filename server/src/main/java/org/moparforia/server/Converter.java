package org.moparforia.server;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import org.moparforia.shared.tracks.parsers.TrackConverter;
import picocli.CommandLine;

@CommandLine.Command(
        name = "convert",
        mixinStandardHelpOptions = true,
        description = "Converts all tracks in the folder into v2 track file versions",
        version = "1.0")
public class Converter implements Callable<Integer> {

    @CommandLine.Parameters
    private Path rootDir;

    @Override
    public Integer call() throws Exception {
        TrackConverter.convertTracks(rootDir);
        return 0;
    }
}
