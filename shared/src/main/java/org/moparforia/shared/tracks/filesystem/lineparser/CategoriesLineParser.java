package org.moparforia.shared.tracks.filesystem.lineparser;

import org.moparforia.shared.tracks.TrackCategory;
import org.moparforia.shared.tracks.parsers.LineParser;

import java.util.*;
import java.util.stream.Collectors;

public class CategoriesLineParser implements LineParser {

    @Override
    public Map<String, Object> apply(String line) {
        List<Integer> possibleValues = Arrays.stream(TrackCategory.values())
                .map(TrackCategory::getId)
                .collect(Collectors.toList());

        String[] parts = line.split(",");
        Map<String, Object> map = new HashMap<>();
        Set<TrackCategory> categories = Arrays.stream(parts)
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .filter(possibleValues::contains)
                .mapToObj(TrackCategory::getByTypeId)
                .distinct()
                .collect(Collectors.toSet());
        map.put("categories", categories);
        return map;
    }
}