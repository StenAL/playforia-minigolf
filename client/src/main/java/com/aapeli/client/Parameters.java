package com.aapeli.client;

import com.aapeli.tools.Tools;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import org.moparforia.shared.Language;

public final class Parameters {

    private static final String LOCALHOST = "127.0.0.1";
    private String serverIp;
    private Language language;
    private String username;
    private String session;
    private String urlRegisterPage;
    private String urlUserInfoPage;
    private String urlTargetUserInfo;
    private String urlUserListPage;
    private String urlTargetUserList;
    private int serverPort;
    private int anInt1455;
    private String[] aStringArray1456;
    private String aString1457;
    private boolean debug;
    private Map<String, String> params;

    public Parameters(Map<String, String> params) {
        this.params = params;
        this.anInt1455 = 0;
        this.init();
    }

    public static boolean getBooleanValue(String key) {
        if (key == null) {
            return false;
        }
        return key.equalsIgnoreCase("true")
                || key.equalsIgnoreCase("t")
                || key.equalsIgnoreCase("yes")
                || key.equalsIgnoreCase("y")
                || key.equals("1")
                || key.equals("1.0")
                || key.equals("1,0");
    }

    public String getParameter(String key) {
        String value = this.params.get(key);
        if (value == null) {
            value = this.params.get(key.toLowerCase());
        }

        if (value == null) {
            value = this.params.get(key.toUpperCase());
        }

        if (value == null) {
            return null;
        } else {
            value = value.trim();
            return value.length() == 0 ? null : value;
        }
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public int getServerPort() {
        return this.serverPort;
    }

    public String getUsername() {
        return this.username;
    }

    public Language getLanguage() {
        return this.language;
    }

    public String getSession() {
        return this.session;
    }

    public void removeSession() {
        this.session = null;
    }

    public String getRegisterPage() {
        return this.urlRegisterPage;
    }

    public boolean showPlayerCard(String var1) {
        if (this.debug) {
            System.out.println("Parameters.showPlayerCard(\"" + var1 + "\")");
        }

        try {
            if (this.urlUserInfoPage == null) {
                return false;
            }

            if (var1.charAt(0) == '~') {
                return false;
            }

            String var2 = this.urlUserInfoPage.toLowerCase();
            if (var2.startsWith("http:")) {
                if (this.urlTargetUserInfo == null) {
                    return false;
                }

                this.showUri(this.toURI(this.urlUserInfoPage + var1), this.urlTargetUserInfo);
                return true;
            }

            if (var2.startsWith("javascript:")) {
                URI uri = this.toURI(this.urlUserInfoPage.replaceFirst("%n", var1));
                if (uri == null) {
                    return false;
                }

                this.showUri(uri, this.urlTargetUserInfo);
                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    public void showPlayerList(String[] var1) {
        this.showPlayerList(var1, null);
    }

    public void showPlayerList(String[] nicks, String var2) {
        try {
            if (nicks == null) {
                if (this.debug) {
                    System.out.println("Parameters.showPlayerList(null,...)");
                }

                this.removePlayerList();
                return;
            }

            if (this.debug) {
                System.out.println("Parameters.showPlayerList(...): nicks.length=" + nicks.length);
            }

            if (this.method1675(nicks, var2)) {
                return;
            }

            this.showPlayerList(nicks, null, var2);
            this.aStringArray1456 = nicks;
            this.aString1457 = var2;
            this.anInt1455 = 1;
        } catch (Exception e) {
        }
    }

    public void showPlayerListWinners(boolean[] winners) {
        try {
            if (winners == null) {
                if (this.debug) {
                    System.out.println("Parameters.showPlayerListWinners(null)");
                }

                this.removePlayerListWinners();
                return;
            }

            if (this.debug) {
                System.out.println("Parameters.showPlayerListWinners(...): winners.length=" + winners.length);
            }

            boolean winnerExists = false;

            for (boolean winner : winners) {
                if (winner) {
                    winnerExists = true;
                }
            }

            if (!winnerExists) {
                this.removePlayerListWinners();
                return;
            }

            if (this.anInt1455 == 0) {
                return;
            }

            this.showPlayerList(this.aStringArray1456, winners, this.aString1457);
            this.anInt1455 = 2;
        } catch (Exception e) {
        }
    }

    public void removePlayerListWinners() {
        this.showPlayerList(this.aStringArray1456, this.aString1457);
    }

    public void removePlayerList() {
        try {
            if (this.anInt1455 == 0) {
                return;
            }

            this.showPlayerList(null, null, null);
            this.anInt1455 = 0;
        } catch (Exception e) {
        }
    }

    public void destroy() {
        this.serverIp = null;
        this.language = null;
        this.session = null;
        this.urlRegisterPage = null;
        this.urlUserInfoPage = null;
        this.urlTargetUserInfo = null;
        this.urlUserListPage = null;
        this.urlTargetUserList = null;
        this.aStringArray1456 = null;
        this.aString1457 = null;
    }

    private void init() {
        this.serverIp = this.getParamServer();
        this.serverPort = this.getParamPort();
        this.language = this.getParamLanguage();
        this.session = this.getParameter("session");
        this.urlRegisterPage = this.getParameter("registerpage");
        this.urlUserInfoPage = this.getParameter("userinfopage");
        this.urlTargetUserInfo = this.getParameter("userinfotarget");
        this.urlUserListPage = this.getParameter("userlistpage");
        this.urlTargetUserList = this.getParameter("userlisttarget");
        this.username = this.getParameter("username");
        this.debug = Tools.getBoolean(this.getParameter("verbose"));
    }

    private String getParamServer() {
        try {
            String server = this.getParameter("server");
            int portIndex = server.lastIndexOf(':');
            return server.substring(0, portIndex);
        } catch (Exception e) {
            return LOCALHOST;
        }
    }

    private int getParamPort() {
        try {
            String server = this.getParameter("server");
            int portIndex = server.lastIndexOf(':');
            return Integer.parseInt(server.substring(portIndex + 1));
        } catch (Exception e) {
            try {
                return Integer.parseInt(this.getParameter("port"));
            } catch (Exception e2) {
                return 4200;
            }
        }
    }

    private Language getParamLanguage() {
        try {
            String language = this.getParameter("language");
            if (language != null) {
                return Language.fromString(language);
            }

        } catch (Exception e) {
        }

        return null;
    }

    private URI toURI(String s) {
        try {
            return new URI(s);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private void showPlayerList(String[] nicks, boolean[] winners, String subgame) {
        if (this.debug) {
            System.out.println("Parameters.showPlayerList(...): "
                    + (nicks != null ? "nicks.length=" + nicks.length : "null")
                    + ", "
                    + (winners != null ? "winners.length=" + winners.length : "null"));
        }

        if (this.urlUserListPage != null) {
            String result = null;
            if (nicks != null) {
                result = "";
                int len = nicks.length;

                for (int i = 0; i < len; ++i) {
                    result = result + nicks[i];
                    if (winners != null) {
                        result = result + (winners[i] ? "*" : "");
                    }

                    if (i < len - 1) {
                        result = result + ',';
                    }
                }

                result = this.htmlEncode(result);
            }

            String var8;
            String var4 = this.urlUserListPage.toLowerCase();
            if (var4.startsWith("http:")) {
                if (this.urlTargetUserList != null) {
                    var8 = this.urlUserListPage;
                    if (result != null) {
                        var8 = var8 + result;
                        if (subgame != null) {
                            var8 = var8 + "&subgame=" + subgame;
                        }
                    }

                    this.showUri(this.toURI(var8), this.urlTargetUserList);
                }
            } else {
                if (var4.startsWith("javascript:")) {
                    var8 = this.urlUserListPage;
                    var8 = var8.replaceFirst("%n", result != null ? result : "");
                    var8 = var8.replaceFirst("%s", subgame != null ? subgame : "");
                    URI uri = this.toURI(var8);
                    if (uri == null) {
                        return;
                    }

                    this.showUri(uri, this.urlTargetUserList);
                }
            }
        }
    }

    private String htmlEncode(String s) {
        int len = s.length();
        StringBuffer result = new StringBuffer(len * 3);

        for (int i = 0; i < len; ++i) {
            char c = s.charAt(i);
            if ((c < 65 || c > 90) && (c < 97 || c > 122) && (c < 48 || c > 57) && c != 45 && c != 126 && c != 44) {
                String hex = Integer.toHexString(c & 255);
                result.append('%');
                if (hex.length() < 2) {
                    result.append(0);
                }

                result.append(hex);
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    private boolean method1675(String[] var1, String var2) {
        if (this.anInt1455 != 1) {
            return false;
        } else if (var2 == null && this.aString1457 != null) {
            return false;
        } else if (var2 != null && this.aString1457 == null) {
            return false;
        } else if (var2 != null && this.aString1457 != null && !var2.equals(this.aString1457)) {
            return false;
        } else {
            int var3 = var1.length;
            if (var3 != this.aStringArray1456.length) {
                return false;
            } else {
                for (int var4 = 0; var4 < var3; ++var4) {
                    if (!var1[var4].equals(this.aStringArray1456[var4])) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    private boolean showUri(URI uri, String target) {
        if (uri == null) {
            return false;
        } else {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                return false;
            }
            return true;
        }
    }
}
