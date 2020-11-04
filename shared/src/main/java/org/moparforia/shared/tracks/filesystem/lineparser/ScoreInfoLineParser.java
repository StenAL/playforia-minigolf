package org.moparforia.shared.tracks.filesystem.lineparser;

import org.moparforia.shared.tracks.parsers.LineParser;

import java.util.HashMap;
import java.util.Map;

public class ScoreInfoLineParser implements LineParser {
    @Override
    public Map<String, Object> apply(String s) {
        Map<String, Object> map = new HashMap<>();
        String[] scoreInfo = s.split(",");
        map.put("attempts", Integer.parseInt(scoreInfo[0]));
        map.put("strokes", Integer.parseInt(scoreInfo[1]));
        map.put("bestPar", Integer.parseInt(scoreInfo[2]));
        map.put("numberOfBestPar", Integer.parseInt(scoreInfo[3]));
        return map;
    }
}
