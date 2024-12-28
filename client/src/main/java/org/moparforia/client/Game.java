package org.moparforia.client;

import agolf.GameApplet;
import com.aapeli.client.Parameters;
import java.applet.Applet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import org.moparforia.shared.Locale;

public class Game {
    private static final int WIDTH = 735;
    private static final int HEIGHT = 525;

    public Game(
            JFrame frame, String server, int port, Locale locale, String username, boolean verbose, boolean norandom) {
        Parameters parameters = getParameters(server, locale, username, port, verbose, norandom);
        Applet game = new GameApplet(parameters);

        game.setSize(WIDTH, HEIGHT);
        game.init();
        game.start();
        frame.add(game);
        frame.setSize(1280, 720);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private Parameters getParameters(
            String server, Locale locale, String username, int port, boolean verbose, boolean norandom) {
        Map<String, String> params = new HashMap<>();
        if (server.indexOf(':') == -1) { // is ipv4
            params.put("server", server);
        } else { // is ipv6
            params.put("server", "[" + server + "]");
        }
        params = new HashMap<>();
        params.put("initmessage", "Loading game...");
        params.put(
                "ld_page",
                "javascript:Playray.Notify.delegate({ jvm: { version: '%v', vendor: '%w', t1: '%r', t2: '%f' } })");
        params.put("server", server + ":" + port);
        params.put("locale", locale.toString());
        params.put("sitename", "playray");
        params.put("registerpage", "http://www.playforia.com/account/create/");
        params.put("creditpage", "http://www.playforia.com/shop/buy/");
        params.put("userinfopage", "http://www.playforia.com/community/user/");
        params.put("userinfotarget", "_blank");
        params.put("userlistpage", "javascript:Playray.GameFaceGallery('%n','#99FF99','agolf','%s')");
        params.put("json", "Playray.Notify.delegate(%o)");
        params.put("verbose", Boolean.toString(verbose));
        params.put("norandom", Boolean.toString(norandom));
        params.put("username", username);
        return new Parameters(params);
    }
}
