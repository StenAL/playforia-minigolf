package org.moparforia.shared.tracks.parsers;

import java.util.Map;
import java.util.function.Function;

/** Class that takes line and returns map of parsed parameters */
public interface LineParser extends Function<String, Map<String, Object>> {}
