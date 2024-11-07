package org.moparforia.shared.tracks;

import java.nio.file.FileSystem;

public record TracksLocation(FileSystem fileSystem, String path) {}
