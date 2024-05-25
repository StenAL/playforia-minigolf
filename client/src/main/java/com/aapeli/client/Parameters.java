package com.aapeli.client;

import com.aapeli.applet.AApplet;
import com.aapeli.tools.Tools;

import java.applet.Applet;
import java.applet.AppletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

public final class Parameters {

    private static final String LOCALHOST = "127.0.0.1";
    //private static final String aString1416 = "192.168.1.23";
    private static final String ENGLISH_LANGUAGE = "en";
    private static final String PLAYFORIA_SITE_NAME = "playforia";
    private static final String PLAYFORIA_QUIT_PAGE = "http://www.playforia.com/";
    private static final String QUIT_TARGET = "_top";
    private Applet applet;
    private AApplet aApplet;
    private String codeBaseHost;
    private String documentBaseHost;
    private String serverIp;
    private String locale;
    private String translationLanguage;
    private String chatLang;
    private String siteName;
    private String username;
    private String sessionLocale;
    private String session;
    private String welcomeMessage;
    private String quitTarget;
    private String urlRegisterPage;
    private String urlVipPage;
    private String urlUserInfoPage;
    private String urlTargetUserInfo;
    private String urlUserListPage;
    private String urlTargetUserList;
    private String urlTellFriendPage;
    private String urlTargetTellFriend;
    private String characterImageDir;
    private String tournamentRound;
    private String subgame;
    private String ticket;
    private String json;
    private boolean tellFriend;
    private boolean guestAutoLogin;
    private boolean disableGuestLobbyChat;
    private int serverPort;
    private URL urlCreditPage;
    private URL quitPageUrl;
    private String[][] imageAliases;
    private int[] anIntArray1454;
    private int anInt1455;
    private String[] aStringArray1456;
    private String aString1457;
    private boolean debug;


    public Parameters(Applet applet) {
        this(applet, false);
    }

    public Parameters(Applet applet, boolean debug) {
        this.applet = applet;
        if (applet instanceof AApplet) {
            this.aApplet = (AApplet) applet;
        }

        this.codeBaseHost = applet.getCodeBase().getHost().toLowerCase();
        this.documentBaseHost = applet.getDocumentBase().getHost().toLowerCase();
        this.anInt1455 = 0;
        this.debug = debug;
        this.init();
    }

    public static boolean getBooleanValue(String key) {
        if (key == null) {
            return false;
        }
        return key.equalsIgnoreCase("true") ||
               key.equalsIgnoreCase("t") ||
               key.equalsIgnoreCase("yes") ||
               key.equalsIgnoreCase("y") ||
               key.equals("1") ||
               key.equals("1.0") ||
               key.equals("1,0");
    }

    public String getParameter(String key) {
        String value = this.applet.getParameter(key);
        if (value == null) {
            value = this.applet.getParameter(key.toLowerCase());
        }

        if (value == null) {
            value = this.applet.getParameter(key.toUpperCase());
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

    public String getLocale() {
        return this.locale;
    }

    public String getTranslationLang() {
        return this.translationLanguage;
    }

    public String getUsername() {
        return this.username;
    }

    public String getChatLang() {
        return this.chatLang != null ? this.chatLang : this.translationLanguage;
    }

    public String getLang() {
        return this.getChatLang();
    }

    public String getSiteName() {
        return this.siteName;
    }

    public String getSessionLocale() {
        return this.sessionLocale;
    }

    public String getSession() {
        return this.session;
    }

    public void removeSession() {
        this.session = null;
    }

    public String getWelcomeMessage() {
        return this.welcomeMessage;
    }

    public void removeWelcomeMessage() {
        this.welcomeMessage = null;
    }

    public String getRegisterPage() {
        return this.urlRegisterPage;
    }

    public String getVipPage() {
        return this.urlVipPage;
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

                this.showUrl(this.toURL(this.urlUserInfoPage + var1), this.urlTargetUserInfo);
                return true;
            }

            if (var2.startsWith("javascript:")) {
                URL var3 = this.toURL(Tools.replaceFirst(this.urlUserInfoPage, "%n", var1));
                if (var3 == null) {
                    return false;
                }

                this.showUrl(var3, this.urlTargetUserInfo);
                return true;
            }
        } catch (Exception e) {
            ;
        }

        return false;
    }

    public void showPlayerList(String[] var1) {
        this.showPlayerList(var1, (String) null);
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
            ;
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
            ;
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
            ;
        }

    }

    public boolean showRegisterPage() {
        return this.showUrl(this.toURL(this.urlRegisterPage), null);
    }

    public void showCreditPurchasePage() {
        this.showCreditPurchasePage(true);
    }

    public void showCreditPurchasePage(boolean openInNewTab) {
        this.showUrl(this.urlCreditPage, openInNewTab ? "_blank" : null);
    }

    public boolean isCreditPurchasePageAvailable() {
        return this.urlCreditPage != null;
    }

    public void showQuitPage() {
        this.showUrl(this.quitPageUrl, this.quitTarget);
    }

    public String[][] getImageAliases() {
        return this.imageAliases;
    }

    public boolean isGuestAutoLogin() {
        return this.guestAutoLogin;
    }

    public void noGuestAutoLogin() {
        this.guestAutoLogin = false;
    }

    public boolean isGuestLobbyChattingDisabled() {
        return this.disableGuestLobbyChat;
    }

    public String getTicket() {
        return this.ticket;
    }

    public boolean callJavaScriptJSON(String json) {
        if (this.debug) {
            System.out.println("Parameters.callJavaScriptJSON(\"" + json + "\")");
        }

        if (this.json == null) {
            return false;
        } else {
            try {
                json = Tools.replaceAll(json, "'", "\\'");
                String var2 = Tools.replaceFirst(this.json, "%o", "'" + json + "'");
                URL var3 = this.toURL(var2);
                if (var3 == null) {
                    return false;
                } else {
                    this.showUrl(var3, null);
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    public void updateWebPageInfoBox(int var1, int var2, int var3) {
        if (this.json != null) {
            if (var1 >= 0 || var2 >= 0 || var3 >= 0) {
                String var4 = "{info:{";
                if (var1 >= 0) {
                    var4 = var4 + "credits:" + var1;
                }

                if (var2 >= 0) {
                    if (var1 >= 0) {
                        var4 = var4 + ',';
                    }

                    var4 = var4 + "chips:" + var2;
                }

                if (var3 >= 0) {
                    if (var1 >= 0 || var2 >= 0) {
                        var4 = var4 + ',';
                    }

                    var4 = var4 + "ranking:" + var3;
                }

                var4 = var4 + "}}";
                this.callJavaScriptJSON(var4);
            }
        }
    }

    public Applet getApplet() {
        return this.applet;
    }

    public AApplet getAApplet() {
        return this.aApplet;
    }

    public void destroy() {
        this.serverIp = null;
        this.locale = null;
        this.translationLanguage = null;
        this.chatLang = null;
        this.siteName = null;
        this.sessionLocale = null;
        this.session = null;
        this.welcomeMessage = null;
        this.quitTarget = null;
        this.urlRegisterPage = null;
        this.urlVipPage = null;
        this.urlUserInfoPage = null;
        this.urlTargetUserInfo = null;
        this.urlUserListPage = null;
        this.urlTargetUserList = null;
        this.urlTellFriendPage = null;
        this.urlTargetTellFriend = null;
        this.characterImageDir = null;
        this.tournamentRound = null;
        this.subgame = null;
        this.ticket = null;
        this.json = null;
        this.urlCreditPage = null;
        this.imageAliases = null;
        this.anIntArray1454 = null;
        this.aStringArray1456 = null;
        this.aString1457 = null;
        this.documentBaseHost = null;
        this.codeBaseHost = null;
    }

    protected AppletContext getAppletContext() {
        return this.applet.getAppletContext();
    }

    protected boolean getTellFriend() {
        return this.tellFriend;
    }

    protected String getTellFriendPage() {
        return this.urlTellFriendPage;
    }

    protected String getTellFriendTarget() {
        return this.urlTargetTellFriend;
    }

    protected String getTournamentRound() {
        return this.tournamentRound;
    }

    protected String getSubgame() {
        return this.subgame;
    }

    private void init() {
        this.serverIp = this.getParamServer();
        this.serverPort = this.getParamPort();
        this.locale = this.getParamLocale();
        this.translationLanguage = this.getParamLanguage();
        this.chatLang = this.getParamChatLanguage();
        this.siteName = this.getParamSiteName();
        this.sessionLocale = this.getParameter("sessionlocale");
        this.session = this.getParameter("session");
        this.welcomeMessage = this.getParameter("welcomemessage");
        if (this.welcomeMessage == null) {
            this.welcomeMessage = this.getParameter("gamewelcome");
        }

        this.quitPageUrl = this.getParamQuitPage();
        this.quitTarget = this.getParamQuitTarget();
        this.urlRegisterPage = this.getParameter("registerpage");
        this.urlVipPage = this.getParameter("vippage");
        this.urlCreditPage = this.toURL(this.getParameter("creditpage"));
        this.urlUserInfoPage = this.getParameter("userinfopage");
        this.urlTargetUserInfo = this.getParameter("userinfotarget");
        this.urlUserListPage = this.getParameter("userlistpage");
        this.urlTargetUserList = this.getParameter("userlisttarget");
        this.tellFriend = Tools.getBoolean(this.getParameter("tellfriend"));
        this.urlTellFriendPage = this.getParameter("tellfriendpage");
        this.urlTargetTellFriend = this.getParameter("tellfriendtarget");
        this.anIntArray1454 = this.getParamRegRemindShowTime();
        this.characterImageDir = this.getParameter("characterimagedir");
        this.imageAliases = this.getParamImageAliases();
        this.guestAutoLogin = Tools.getBoolean(this.getParameter("guestautologin"));
        this.disableGuestLobbyChat = Tools.getBoolean(this.getParameter("disableguestlobbychat"));
        this.tournamentRound = this.getParameter("tournamentround");
        this.subgame = this.getParameter("subgame");
        this.ticket = this.getParameter("ticket");
        this.json = this.getParameter("json");
        this.username = this.getParameter("username");
        if (this.json != null) {
            this.json = Tools.replaceFirst(this.json, "\'%o\'", "%o");
            if (!this.json.toLowerCase().startsWith("javascript:")) {
                this.json = "javascript:" + this.json;
            }
        }
    }

    private String getParamServer() {
        try {
            String server = this.getParameter("server");
            int portIndex = server.lastIndexOf(':');
            return server.substring(0, portIndex);
        } catch (Exception e) {
            return this.codeBaseHost.length() > 0 ? this.codeBaseHost : LOCALHOST;
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

    private String getParamLocale() {
        String locale;
        try {
            locale = this.getParameter("locale");
            if (locale != null) {
                return locale;
            }
        } catch (Exception e) {
            ;
        }

        if (this.codeBaseHost.endsWith("aapeli.com")) {
            return "fi";
        } else if (this.codeBaseHost.endsWith("playray.com")) {
            return ENGLISH_LANGUAGE;
        } else {
            if (this.codeBaseHost.endsWith(".playforia.com")) {
                try {
                    locale = this.codeBaseHost.substring(0, this.codeBaseHost.indexOf(46));
                    if (locale.length() > 0 && !locale.equals("www")) {
                        return locale;
                    }
                } catch (Exception e) {
                    ;
                }
            }

            if (this.codeBaseHost.contains("playray")) {
                try {
                    locale = this.codeBaseHost.substring(this.codeBaseHost.lastIndexOf(46) + 1);
                    if (locale.length() > 0) {
                        return locale;
                    }
                } catch (Exception e) {
                    ;
                }
            }

            return ENGLISH_LANGUAGE;
        }
    }

    private String getParamLanguage() {
        try {
            String language = this.getParameter("lang");
            if (language != null) {
                return language;
            }

            language = this.getParameter("language");
            if (language != null) {
                return language;
            }
        } catch (Exception e) {
            ;
        }

        return null;
    }

    private String getParamChatLanguage() {
        try {
            String chatLanguage = this.getParameter("chatlang");
            if (chatLanguage != null) {
                return chatLanguage;
            }

            chatLanguage = this.getParameter("serverlang");
            if (chatLanguage != null) {
                return chatLanguage;
            }
        } catch (Exception e) {
            ;
        }

        return null;
    }

    private String getParamSiteName() {
        try {
            String siteName = this.getParameter("sitename");
            if (siteName != null) {
                return siteName;
            }
        } catch (Exception e) {
            ;
        }

        if (this.documentBaseHost.contains("aapeli.")) {
            return "aapeli";
        } else if (this.documentBaseHost.contains("playforia.")) {
            return PLAYFORIA_SITE_NAME;
        } else if (this.documentBaseHost.contains("playray.")) {
            return "playray";
        }
        return PLAYFORIA_SITE_NAME;
    }

    private URL getParamQuitPage() {
        URL quitPage = this.toURL(this.getParameter("quitpage"));
        if (quitPage != null) {
            return quitPage;
        } else {
            quitPage = this.toURL(this.documentBaseHost);
            return quitPage != null ? quitPage : this.toURL(PLAYFORIA_QUIT_PAGE);
        }
    }

    private String getParamQuitTarget() {
        String quitTarget = this.getParameter("quittarget");
        return quitTarget != null ? quitTarget : QUIT_TARGET;
    }

    private String[][] getParamImageAliases() {
        String imageAliases = this.getParameter("imagealias");
        if (imageAliases == null) {
            return null;
        } else {
            StringTokenizer tokenizer = new StringTokenizer(imageAliases, " ");
            int tokenCount = tokenizer.countTokens();
            if (tokenCount == 0) {
                return null;
            } else {
                String[][] result = new String[tokenCount][2];

                for (int i = 0; i < tokenCount; ++i) {
                    String token = tokenizer.nextToken();
                    int var6 = token.indexOf(':');
                    if (var6 <= 0 || var6 == token.length() - 1) {
                        return null;
                    }

                    result[i][0] = token.substring(0, var6);
                    result[i][1] = this.method1670(token.substring(var6 + 1));
                }

                return result;
            }
        }
    }

    private String method1670(String var1) {
        StringTokenizer var2 = new StringTokenizer(var1, ",");
        int var3 = var2.countTokens();
        if (var3 <= 1) {
            return var1;
        } else {
            int var4 = (int) (Math.random() * (double) var3);

            while (true) {
                var1 = var2.nextToken();
                if (var4 == 0) {
                    return var1;
                }

                --var4;
            }
        }
    }

    private int[] getParamRegRemindShowTime() {
        String regRemindShowTime = this.getParameter("regremindshowtime");
        if (regRemindShowTime == null) {
            return null;
        } else {
            StringTokenizer tokenizer = new StringTokenizer(regRemindShowTime, ",");
            int tokenCount = tokenizer.countTokens();
            if (tokenCount == 0) {
                return null;
            } else {
                int[] result = new int[tokenCount];

                for (int i = 0; i < tokenCount; ++i) {
                    try {
                        result[i] = Integer.parseInt(tokenizer.nextToken());
                    } catch (NumberFormatException e) {
                        return null;
                    }

                    if (result[i] <= 0) {
                        return null;
                    }

                    if (i > 0 && result[i] <= result[i - 1]) {
                        return null;
                    }
                }

                return result;
            }
        }
    }

    private URL toURL(String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private void showPlayerList(String[] nicks, boolean[] winners, String subgame) {
        if (this.debug) {
            System.out.println("Parameters.showPlayerList(...): " + (nicks != null ? "nicks.length=" + nicks.length : "null") + ", " + (winners != null ? "winners.length=" + winners.length : "null"));
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

                    this.showUrl(this.toURL(var8), this.urlTargetUserList);
                }
            } else {
                if (var4.startsWith("javascript:")) {
                    var8 = this.urlUserListPage;
                    var8 = Tools.replaceFirst(var8, "%n", result != null ? result : "");
                    var8 = Tools.replaceFirst(var8, "%s", subgame != null ? subgame : "");
                    URL var9 = this.toURL(var8);
                    if (var9 == null) {
                        return;
                    }

                    this.showUrl(var9, this.urlTargetUserList);
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

    private boolean showUrl(URL url, String target) {
        if (url == null) {
            return false;
        } else {
            AppletContext appletContext = this.applet.getAppletContext();
            if (target != null) {
                appletContext.showDocument(url, target);
            } else {
                appletContext.showDocument(url);
            }

            return true;
        }
    }
}
