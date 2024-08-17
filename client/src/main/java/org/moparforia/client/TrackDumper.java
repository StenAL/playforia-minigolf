package org.moparforia.client;

import org.moparforia.client.trackdump.ConnListener;
import org.moparforia.client.trackdump.Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class TrackDumper implements ConnListener {

    static int track_basic = 1;
    static int track_traditional = 2;
    static int track_modern = 3;
    static int track_hio = 4;
    static int track_short = 5;
    static int track_long = 6;
    int track_type = -1;
    Connection conn = null;
    String map_dir = "trackdump/";
    int tries = 0;

    public TrackDumper(int track_type, String map_dir) {
        this.track_type = track_type;
        this.map_dir = map_dir;
        conn = new Connection(this, aStringArray2369);
        conn.connect();
        System.out.println("started downloading " + track_type + " into " + map_dir);
    }

    public static void main(String[] args) {
        new TrackDumper(track_basic, "tracks/basic/");
        new TrackDumper(track_traditional, "tracks/traditional/");
        new TrackDumper(track_modern, "tracks/modern/");
        new TrackDumper(track_hio, "tracks/hio/");
        new TrackDumper(track_short, "tracks/short/");
        new TrackDumper(track_long, "tracks/long/");
    }

    @Override
    public void dataReceived(String cmd) {
        try {
            String[] args = cmd.split("\t");
            if(args[0].equals("versok")) {
                conn.writeData("language\ten_US");
                conn.writeData("logintype\tnr");
            }
            else if(args[0].equals("status")) {
                if(args[1].equals("login")) {
                    conn.writeData("login");
                }
                else if(args[1].equals("lobbyselect")) {
                    String something = args[2];
                    conn.writeData("lobbyselect\trnop");
                }
                else if(args[1].equals("lobby")) {
                    String id = args[2];
                    if(id.equals("1"))
                        conn.writeData("lobby\ttracksetlist");
                }
                else if(args[1].equals("game")) {
                    // ok thanks
                }
            }
            else if(args[0].equals("basicinfo")) {
                // do nothing
            }
            else if(args[0].equals("lobbyselect")) {
                if(args[1].equals("nop")) {
                    String single = args[2];
                    String dual = args[3];
                    String multi = args[4];
                    conn.writeData("lobbyselect\tselect\t1");
                }
            }
            else if(args[0].equals("lobby")) {
                if(args[1].equals("numberofusers")) {
                    // do nothing
                }
                else if(args[1].equals("users")) {
                    // fuck those losers
                }
                else if(args[1].equals("ownjoin")) {
                    // yyaaayy that's us right?
                }
                else if(args[1].equals("tracksetlist")) {
                    // >implying we give a fuck

                    conn.writeData("lobby\tcspt\t20\t" + track_type + "\t0");
                }
            }
            else if(args[0].equals("game")) {
                if(args[1].equals("gameinfo")) {
                    // didn't read lol
                }
                else if(args[1].equals("players")) {
                    // noope, singleplayer
                }
                else if(args[1].equals("owninfo")) {
                    // !!
                }
                else if(args[1].equals("start")) {
                    // ???
                }
                else if(args[1].equals("starttrack")) {
                    boolean var17 = args[2].equals("ttm1");
                    boolean canPlay = args[2].equals("ttm2");
                    boolean var23 = var17 || canPlay;
                    boolean hasPlayed = false;
                    int startIndex = var23 ? 5 : 4;
                    int argsLen = args.length;
                    String author = null;
                    String name = null;
                    String data = null;
                    String fullInstruction = "";
                    for (int commandIndex = startIndex; commandIndex < argsLen; ++commandIndex) {
                        char var12 = args[commandIndex].charAt(0);
                        if (var12 == 'A') {
                            author = args[commandIndex].substring(2);
                            //tabSplit[commandIndex] = "A fc_and_vortex_co";
                        }

                        if (var12 == 'N') {
                            name = args[commandIndex].substring(2);
                            //tabSplit[commandIndex] = "N DA REIGHSTAFFFFFFFGGG GHETTO";
                        }

                        if (var12 == 'T') {
                            data = args[commandIndex].substring(2);
                        }

                        if (var12 == 'T' && args[commandIndex].charAt(2) == '!') {// a track we already played?
                            hasPlayed = true;
                        }

                        fullInstruction = fullInstruction + args[commandIndex];
                        if (commandIndex < argsLen - 1) {
                            fullInstruction = fullInstruction + '\n';
                        }
                    }
                    if(!hasPlayed) {
                        String checksum = MD5Checksum.getMD5Checksum(fullInstruction.getBytes());
                        File file = new File(map_dir, checksum + ".track");
                        if(!file.exists()) {
                            System.out.println("track " + checksum);
                            PrintStream out = null;
                            try {
                                out = new PrintStream(new FileOutputStream(file, false));
                                out.print(fullInstruction);
                            }
                            catch (Exception e) { }
                            finally {
                                if (out != null) out.close();
                            }
                        }
                    }
                }
                else if(args[1].equals("startturn")) {
                    conn.writeData("game\tskip");
                }
                else if(args[1].equals("end")) {
                    conn.writeData("game\tnewgame");
                }
            }
        } catch (Exception ex) {
            System.err.println("dataReceived error: " + ex);
            this.conn.disconnect();
        }
    }

    @Override
    public void connectionLost(int var1) {
        System.err.println("connectionLost: " + var1);
        if(tries < 5) {
            conn.connect();
            tries++;
        }
    }

    @Override
    public void notifyConnectionDown() {
        System.out.println("disconnected");
        if(tries < 5) {
            conn.connect();
            tries++;
        }
    }

    @Override
    public void notifyConnectionUp() {
        System.out.println("connected");
    }

    static String[] aStringArray2369 = new String[] {
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
}
