package org.moparforia.shared.tracks;

import java.util.List;

/**
 * Interface representing Manager that provides Tracks
 */
public interface TrackManager {

    /**
     * @param track Saves said track
     */
    void save(Track track);

    /**
     * Returns random tracks
     *
     * @param limit  Maximum tracks that should be returned, if there are not enough tracks, it can be return less
     * @param type   What category should be track belong to
     * @return Random tracks
     */
    List<Track> getRandomTracks(int limit, TrackCategory type);


    List<Track> getTracks();

    TrackSet getTrackSet(String name);

    List<TrackSet> getTrackSets();

    Track findTrackByName(String name);

    List<Track> findAllByCategory(TrackCategory category);

    /**
     * Loads all Tracks and TrackSets
     * @throws TrackLoadException Exception
     */
    void load(TracksLocation tracksLocation) throws TrackLoadException;

    /**
     * @return True, if manager is loaded
     */
    boolean isLoaded();
}
