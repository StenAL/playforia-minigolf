package org.moparforia.shared.tracks.filesystem.lineparser;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import org.moparforia.shared.tracks.parsers.LineParser;

public class SingleArgumentLineParser<T> implements LineParser {

    private final String parameter_name;
    private final Function<String, T> converter;

    public SingleArgumentLineParser(String parameter_name, Function<String, T> converter) {
        this.parameter_name = parameter_name;
        this.converter = converter;
    }

    @Override
    public Map<String, Object> apply(String line) {
        return Collections.singletonMap(parameter_name, converter.apply(line));
    }
}
