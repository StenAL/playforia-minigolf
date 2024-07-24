package org.moparforia.server.net.packethandlers.golf;

import org.moparforia.server.Server;
import org.moparforia.server.game.Lobby;
import org.moparforia.server.game.LobbyType;
import org.moparforia.server.game.Player;
import org.moparforia.server.game.gametypes.golf.ChampionshipGame;
import org.moparforia.server.game.gametypes.golf.TrainingGame;
import org.moparforia.server.net.Packet;
import org.moparforia.server.net.PacketHandler;
import org.moparforia.server.net.PacketType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LobbyCreateSinglePlayerHandler implements PacketHandler {
    @Override
    public PacketType getType() {
        return PacketType.DATA;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("(lobby|lobbyselect)\\tcsp(t|c)\\t(\\d+)(?:\\t(\\d+)\\t(\\d+))?");
    }                 //CLIENT> WRITE "d 5 lobby	cspt	10	7	0"

    @Override
    public boolean handle(Server server, Packet packet, Matcher message) {
        Player player = packet.getChannel().attr(Player.PLAYER_ATTRIBUTE_KEY).get();
        int number = Integer.parseInt(message.group(3));
        if (message.group(2).equals("t")) { // training
            int trackType = Integer.parseInt(message.group(4));
            int water = Integer.parseInt(message.group(5));
            if (message.group(1).equals("lobbyselect")) {
                server.getLobby(LobbyType.SINGLE).addPlayer(player, Lobby.JOIN_TYPE_NORMAL);
            }
            new TrainingGame(player, server.getNextGameId(), trackType, number, water);

        } else if (message.group(2).equals("c")) { // championship
            new ChampionshipGame(player, server.getNextGameId(), number);
        } else {
            return false;
        }
        return true;
    }
}
