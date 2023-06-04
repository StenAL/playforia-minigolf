package org.moparforia.server;

import org.moparforia.shared.ManifestVersionProvider;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        description = "Starts Minigolf Server",
        name = "server",
        mixinStandardHelpOptions = true,
        versionProvider = ManifestVersionProvider.class,
        subcommands = {
                Converter.class
        }
)
public class Launcher implements Callable<Integer> {

    public static final String DEFAULT_HOST = "0.0.0.0";
    public static final String DEFAULT_PORT = "4242";
    public static final String DEFAULT_TRACKS_DIRECTORY = "tracks";

    @CommandLine.Option(
            names = {"--hostname", "-ip"},
            description = "Sets server hostname",
            defaultValue = DEFAULT_HOST
    )
    private String host;

    @CommandLine.Option(
            names = {"--port", "-p"},
            description = "Sets server port",
            defaultValue = DEFAULT_PORT
    )
    private int port;

    @CommandLine.Option(
            names = {"--tracks-dir", "-t"},
            description = "Sets where to look for tracks and track sets",
            defaultValue = DEFAULT_TRACKS_DIRECTORY
    )
    private String tracksDirectory;

    public static void main(String... args) {
        Launcher launcher = new Launcher();
        new CommandLine(launcher)
                .setCaseInsensitiveEnumValuesAllowed(true)
                .execute(args);
    }

    @Override
    public Integer call() {
        getServer(host, port, tracksDirectory).start();
        return 0;
    }

    public Server getServer(String host, int port, String tracksDirectory) {
        return new Server(host, port, tracksDirectory);
    }
}
