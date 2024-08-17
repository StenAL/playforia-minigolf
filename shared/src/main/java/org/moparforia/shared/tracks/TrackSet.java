package org.moparforia.shared.tracks;

import java.util.List;

public class TrackSet {

    private String name;
    private TrackSetDifficulty difficulty;
    private List<Track> tracks;

    public TrackSet(String name, TrackSetDifficulty difficulty, List<Track> tracks) {
        this.name = name;
        this.difficulty = difficulty;
        this.tracks = tracks;
    }

    public String getName() {
        return name;
    }

    public TrackSetDifficulty getDifficulty() {
        return difficulty;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof TrackSet)) {
            return false;
        }
        TrackSet t = (TrackSet) o;
        return difficulty == t.difficulty && name.equals(t.name) && tracks.equals(t.tracks);
    }
}
