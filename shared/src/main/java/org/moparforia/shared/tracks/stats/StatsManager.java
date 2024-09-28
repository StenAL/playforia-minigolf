package org.moparforia.shared.tracks.stats;

import org.moparforia.shared.tracks.Track;

public interface StatsManager {

    /**
     * @param track Track
     * @return Statistics object for said track
     */
    TrackStats getStats(Track track);

    /**
     * Rates track
     *
     * @param rating Rating from 1 to 10
     */
    void rate(Track track, int rating);

    /**
     * Add score to the player for that tracks
     *
     * @param player Name of the player that scored par
     * @param par Par
     */
    void addScore(Track track, String player, int par);

    /**
     * @return If the class support also setting score or is just meant for their retrieval
     */
    boolean isReadOnly();
}
