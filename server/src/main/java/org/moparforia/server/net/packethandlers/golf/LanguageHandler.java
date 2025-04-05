package org.moparforia.server.net.packethandlers.golf;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.moparforia.server.Server;
import org.moparforia.server.game.Player;
import org.moparforia.server.net.Packet;
import org.moparforia.server.net.PacketHandler;
import org.moparforia.server.net.PacketType;
import org.moparforia.shared.Language;

public class LanguageHandler implements PacketHandler {

    @Override
    public PacketType getType() {
        return PacketType.DATA;
    }

    @Override
    public Pattern getPattern() {
        return Pattern.compile("language\\t(.*)");
    }

    @Override
    public boolean handle(Server server, Packet packet, Matcher message) {
        Player player = packet.getChannel().attr(Player.PLAYER_ATTRIBUTE_KEY).get();
        player.setLanguage(Language.fromString(message.group(1)));
        return true;
    }
}
