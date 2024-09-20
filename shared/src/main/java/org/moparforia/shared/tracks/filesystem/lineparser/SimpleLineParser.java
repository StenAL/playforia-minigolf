package org.moparforia.shared.tracks.filesystem.lineparser;

import java.util.function.Function;

/** Line parser that returns entire contents of the line */
public class SimpleLineParser extends SingleArgumentLineParser<String> {

    public SimpleLineParser(String parameter_name) {
        super(parameter_name, Function.identity());
    }
}
