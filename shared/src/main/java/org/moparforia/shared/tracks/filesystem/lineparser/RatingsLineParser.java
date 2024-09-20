package org.moparforia.shared.tracks.filesystem.lineparser;

import java.util.Collections;
import java.util.Map;
import org.moparforia.shared.tracks.parsers.LineParser;

public class RatingsLineParser implements LineParser {
    @Override
    public Map<String, Object> apply(String line) {
        String[] parts = line.split(",");
        int[] ratings = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            ratings[i] = Integer.parseInt(parts[i]);
        }
        return Collections.singletonMap("ratings", ratings);
    }
}
