package org.moparforia.shared.tracks;

public enum TrackSetDifficulty {

    EASY(1), MEDIUM(2), HARD(3);

    private final int id;

    private TrackSetDifficulty(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
