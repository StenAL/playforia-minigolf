package org.moparforia.shared.tracks.filesystem.lineparser;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import org.moparforia.shared.tracks.parsers.LineParser;

public class BestTimeLineParser implements LineParser {

    private final String date_parameter;
    private final String player_parameter;

    public BestTimeLineParser(String date_parameter, String player_parameter) {
        this.date_parameter = date_parameter;
        this.player_parameter = player_parameter;
    }

    @Override
    public Map<String, Object> apply(String s) {
        Map<String, Object> map = new HashMap<>();
        String[] parts = s.split(",");
        map.put(player_parameter, parts[0]);
        long epochMilli = Long.parseLong(parts[1]);
        LocalDate date =
                Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDate();
        map.put(date_parameter, date);
        return map;
    }
}
