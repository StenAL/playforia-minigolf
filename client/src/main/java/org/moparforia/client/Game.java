package org.moparforia.client;

import agolf.AGolf;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;

public class Game {
    private static final int WIDTH = 735;
    private static final int HEIGHT = 525;

    public Game(
            JFrame frame, String server, int port, String lang, String username, boolean verbose, boolean norandom) {
        Applet game = new AGolf();

        game.setStub(new Stub(server, lang, username, port, verbose, norandom));
        game.setSize(WIDTH, HEIGHT);
        game.init();
        game.start();
        frame.add(game);
        frame.setSize(1280, 720);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    class Stub implements AppletStub {
        private final Map<String, String> params;
        private String server;

        public Stub(String server, String lang, String username, int port, boolean verbose, boolean norandom) {
            if (server.indexOf(':') == -1) { // is ipv4
                this.server = server;
            } else { // is ipv6
                this.server = "[" + server + "]";
            }
            params = new HashMap<>();
            params.put("initmessage", "Loading game...");
            params.put(
                    "ld_page",
                    "javascript:Playray.Notify.delegate({ jvm: { version: '%v', vendor: '%w', t1: '%r', t2: '%f' } })");
            params.put("image", "/appletloader_playforia.gif");
            /*if(serverBox.isSelected()) {
                params.put("server", "149.255.111.161" + ":" + g.port);
            } else {
                params.put("server", "game05.playforia.net" + ":" + g.port);
            }*/

            params.put("server", server + ":" + port);

            params.put("language", lang.substring(0, 2)); // use first part of en_US, fi_FI or sv_SE
            params.put("locale", lang);
            params.put("sitename", "playray");
            params.put("quitpage", "http://www.playforia.com/games/");
            params.put("regremindshowtime", "3,8,15,25,50,100,1000");
            params.put("registerpage", "http://www.playforia.com/account/create/");
            params.put("creditpage", "http://www.playforia.com/shop/buy/");
            params.put("userinfopage", "http://www.playforia.com/community/user/");
            params.put("userinfotarget", "_blank");
            params.put("userlistpage", "javascript:Playray.GameFaceGallery('%n','#99FF99','agolf','%s')");
            params.put("guestautologin", "true");
            params.put("disableguestlobbychat", "true");
            params.put("json", "Playray.Notify.delegate(%o)");
            params.put("centerimage", "true");
            params.put("java_arguments", "-Xmx128m");
            params.put("localizationUrl", "");
            params.put("sharedLocalizationUrl", "");
            params.put("verbose", Boolean.toString(verbose));
            params.put("norandom", Boolean.toString(norandom));
            params.put("username", username);

            // if(serverBox.isSelected())
            // params.put("tracktestmode", "true");
            // params.put("session", "7vkBHjUIcQKg-J,c2bXzYdy,lJd");
            // params.put("sessionlang", "en");
        }

        public boolean isActive() {
            return true;
        }

        public URL getDocumentBase() {
            try {
                return new URL("http://" + this.server + "/AGolf/");
            } catch (Exception ex) {
                System.err.println("getdocumentbase exc eption");
                return null;
            }
        }

        public URL getCodeBase() {
            return getDocumentBase();
        }

        public String getParameter(String name) {
            if (!params.containsKey(name)) return "";
            return params.get(name);
        }

        public AppletContext getAppletContext() {
            return null;
        }

        public void appletResize(int width, int height) {}
    }
}
