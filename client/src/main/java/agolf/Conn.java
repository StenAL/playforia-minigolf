package agolf;

import com.aapeli.applet.AApplet;
import com.aapeli.connection.ConnListener;
import com.aapeli.connection.Connection;
import com.aapeli.tools.Tools;

public class Conn implements ConnListener {

    private static final String[] cipherCmds = new String[] {
            "status\t",
            "basicinfo\t",
            "numberofusers\t",
            "users\t",
            "ownjoin\t",
            "joinfromgame\t",
            "say\t",
            "logintype\t",
            "login",
            "lobbyselect\t",
            "select\t",
            "back",
            "challenge\t",
            "cancel\t",
            "accept\t",
            "cfail\t",
            "nouser",
            "nochall",
            "cother",
            "cbyother",
            "refuse",
            "afail",
            "gsn\t",
            "lobby\tnc\t",
            "lobby\t",
            "lobby",
            "tracksetlist\t",
            "tracksetlist",
            "gamelist\t",
            "full\t",
            "add\t",
            "change\t",
            "remove\t",
            "gameinfo\t",
            "players",
            "owninfo\t",
            "game\tstarttrack\t",
            "game\tstartturn\t",
            "game\tstart",
            "game\tbeginstroke\t",
            "game\tendstroke\t",
            "game\tresetvoteskip",
            "game\t",
            "game",
            "quit",
            "join\t",
            "part\t",
            "cspt\t",
            "qmpt",
            "cspc\t",
            "jmpt\t",
            "tracklist\t",
            "Tiikoni",
            "Leonardo",
            "Ennaji",
            "Hoeg",
            "Darwin",
            "Dante",
            "ConTrick",
            "Dewlor",
            "Scope",
            "SuperGenuis",
            "Zwan",
            "\tT !\t",
            "\tcr\t",
            "rnop",
            "nop\t",
            "error"
    };
    private GameContainer gameContainer;
    private Connection connection;
    private String aString2372;
    private String aString2373;


    protected Conn(GameContainer var1) {
        this.gameContainer = var1;
        this.aString2372 = this.aString2373 = null;
    }

    public void dataReceived(String var1) {
        try {
            this.handlePacket(var1);
            this.aString2373 = var1;
        } catch (Exception var4) {
            Exception var2 = var4;

            try {
                this.writeData("error-debug\t" + this.gameContainer.gameApplet.method32() + "\t" + var2.toString().trim().replace('\n', '\\') + "\t" + var1.replace('\t', '\\') + "\t" + this.aString2373.replace('\t', '\\') + "\t" + this.aString2372.replace('\t', '\\'));
            } catch (Exception var3) {
            }

            this.gameContainer.gameApplet.setEndState(var4);
            this.connection.closeConnection();
        }
    }

    public void connectionLost(int var1) {
        if (var1 != 2 && var1 != 3) {
            if (var1 == 4) {
                this.gameContainer.gameApplet.setEndState(AApplet.END_ERROR_VERSION);
            }

        } else {
            this.gameContainer.gameApplet.setEndState(AApplet.END_ERROR_CONNECTION);
        }
    }

    public void notifyConnectionDown() {
    }

    public void notifyConnectionUp() {
    }

    protected boolean method1158() {
        this.connection = new Connection(this.gameContainer.gameApplet, this, cipherCmds);
        return this.connection.openConnection();
    }

    protected void sendVersion() {
        this.gameContainer.gameApplet.setGameState(0);
        this.writeData("version\t" + 35);
    }

    public void writeData(String var1) {
        this.aString2372 = var1;
        this.connection.writeData(var1);
    }

    protected void disconnect() {
        if (this.connection != null) {
            this.connection.closeConnection();
        }
    }

    private void handlePacket(String cmd) {
        String[] args = Tools.separateString(cmd, "\t");
        if (args[0].equals("error")) {
            if (args[1].equals("vernotok")) {
                this.gameContainer.gameApplet.setEndState(AApplet.END_ERROR_VERSION);
            } else if (args[1].equals("serverfull")) {
                this.gameContainer.gameApplet.setEndState(AApplet.END_ERROR_SERVERFULL);
            }

            this.connection.closeConnection();
        } else if (args[0].equals("versok")) {
            this.writeData("language\t" + this.gameContainer.params.getChatLang());
            String var4 = this.gameContainer.params.getSessionLocale();
            if (var4 != null) {
                this.writeData("sessionlocale\t" + var4);
            }

            this.writeData("logintype\t" + (this.gameContainer.synchronizedTrackTestMode.get() ? "ttm" : (this.gameContainer.gameApplet.hasSession() ? "reg" : "nr")));
        } else if (args[0].equals("basicinfo")) {
            this.gameContainer.gameApplet.setGameSettings(args[1].equals("t"), Integer.parseInt(args[2]), args[3].equals("t"), args[4].equals("t"));
        } else if (args[0].equals("broadcast")) {
            if (this.gameContainer.lobbyPanel != null) {
                this.gameContainer.lobbyPanel.broadcastMessage(args[1]);
            }

            if (this.gameContainer.gamePanel != null) {
                this.gameContainer.gamePanel.broadcastMessage(args[1]);
            }

        }
        else if (args[0].equals("status")) {
            if (args[1].equals("login")) {
                if (args.length == 2) {
                    this.gameContainer.gameApplet.setGameState(1);
                    return;
                }

                byte var3 = 0;
                if (args[2].equals("nickinuse")) {
                    var3 = 4;
                }

                if (args[2].equals("rlf")) {
                    var3 = 5;
                }

                if (args[2].equals("invalidnick")) {
                    var3 = 6;
                }

                if (args[2].equals("forbiddennick")) {
                    var3 = 7;
                }

                this.gameContainer.gameApplet.setGameState(1, var3);
                return;
            }

            if (args[1].equals("lobbyselect")) {
                this.gameContainer.gameApplet.setGameState(2, args.length > 2 ? Integer.parseInt(args[2]) : 0);
                return;
            }

            if (args[1].equals("lobby")) {
                if (args.length == 2) {
                    this.gameContainer.gameApplet.setGameState(3, Integer.MIN_VALUE);
                    return;
                }

                if (args[2].equals("tt")) {
                    this.gameContainer.gameApplet.setGameState(3, -1, args[3].equals("t") ? 1 : 0);
                    return;
                }

                if (!args[2].equals("1") && !args[2].equals("1h")) {
                    if (args[2].equals("2")) {
                        this.gameContainer.gameApplet.setGameState(3, 2);
                        return;
                    }

                    if (args.length == 3) {
                        this.gameContainer.gameApplet.setGameState(3, 3);
                        return;
                    }

                    this.gameContainer.gameApplet.setGameState(3, 3, Integer.parseInt(args[3]));
                    return;
                }

                this.gameContainer.gameApplet.setGameState(3, 1, args[2].equals("1") ? 1 : -1);
                //enables tracklistadmin this.aGameContainer_2370.gameApplet.setGameState(3, -1, 1);
                return;
            }

            if (args[1].equals("game")) {
                this.gameContainer.gameApplet.setGameState(4);
                return;
            }
        }

        if (args[0].equals("lobbyselect")) {
            this.gameContainer.lobbySelectionPanel.handlePacket(args);
        } else if (args[0].equals("lobby")) {
            this.gameContainer.lobbyPanel.handlePacket(args);
        }
        else if (args[0].equals("game")) {
            this.gameContainer.gamePanel.handlePacket(args);
        }
    }
}
