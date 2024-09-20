package org.moparforia.server.game.gametypes.golf;

import java.util.List;
import org.moparforia.server.game.Lobby;
import org.moparforia.server.game.LobbyType;
import org.moparforia.server.game.Player;
import org.moparforia.server.game.gametypes.GolfGame;
import org.moparforia.shared.tracks.Track;
import org.moparforia.shared.tracks.TrackCategory;

public class TrainingGame extends GolfGame {

    public TrainingGame(Player p, int gameId, int tracksType, int numberOfTracks, int water) {
        super(
                gameId,
                LobbyType.SINGLE,
                "derp",
                null,
                false,
                numberOfTracks,
                PERM_EVERYONE,
                tracksType,
                STROKES_UNLIMITED,
                STROKETIMEOUT_INFINITE,
                water,
                COLLSION_YES,
                SCORING_STROKE,
                SCORING_WEIGHT_END_NONE,
                1);

        Lobby lob = p.getLobby();
        if (addPlayer(p)) {
            lob.addGame(this);
            startGame();
        } else {

            // todo: if adding da player failed init!!
        }
    }

    @Override
    public List<Track> initTracks() {
        return manager.getRandomTracks(numberOfTracks, TrackCategory.getByTypeId(tracksType));
    }
}
