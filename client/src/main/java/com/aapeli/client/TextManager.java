package com.aapeli.client;

import com.aapeli.tools.EncodedXmlReader;
import com.aapeli.tools.Tools;
import com.aapeli.tools.XmlUnit;
import java.applet.Applet;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

public final class TextManager implements Runnable {

    private Parameters parameters;
    private Thread textLoaderThread;
    private String language;
    private Hashtable gameTable;
    private Hashtable sharedTable;
    private String errorMessage;
    private boolean useLanguageFiles; // true == use language xml files, false == use locale .loc files
    private boolean debug;

    public TextManager(Applet applet, String var2) {
        this(applet, var2, false);
    }

    public TextManager(Applet applet, String var2, boolean debug) {
        this(debug);
        this.language = var2;
        this.useLanguageFiles = false;
        this.loadTexts(applet);
    }

    public TextManager(Parameters parameters) {
        this(parameters, false, false);
    }

    public TextManager(Parameters parameters, boolean debug) {
        this(parameters, false, debug);
    }

    public TextManager(Parameters parameters, boolean loadTextsInSeparateThread, boolean debug) {
        this(debug);
        this.parameters = parameters;
        String language = parameters.getTranslationLang();
        if (language != null) {
            this.language = language;
            this.useLanguageFiles = true;
        } else {
            this.language = parameters.getLocale();
            this.useLanguageFiles = false;
        }

        if (loadTextsInSeparateThread) {
            this.textLoaderThread = new Thread(this);
            this.textLoaderThread.start();
        } else {
            this.loadTexts(parameters.getApplet());
        }
    }

    private TextManager(boolean debug) {
        this.debug = debug;
        this.gameTable = new Hashtable();
        this.sharedTable = new Hashtable();
        this.errorMessage = null;
        this.textLoaderThread = null;
    }

    public void run() {
        if (this.debug) {
            System.out.println("TextManager.run(): Start loading texts");
        }

        this.loadTexts(this.parameters.getApplet());
        this.textLoaderThread = null;
        if (this.debug) {
            System.out.println("TextManager.run(): Finished loading texts");
        }
    }

    public String getGame(String key) {
        return this.getGame(key, (String[]) null);
    }

    public boolean isAvailable(String key) {
        return this.getText(key, 1) != null;
    }

    public String getIfAvailable(String key) {
        return this.getIfAvailable(key, null);
    }

    public String getIfAvailable(String key, String fallback) {
        String result = this.getText(key, 1);
        return result != null ? result : fallback;
    }

    public String getGame(String key, String argument1) {
        String[] arguments = new String[] {argument1};
        return this.getGame(key, arguments);
    }

    public String getGame(String key, String var2, String var3) {
        String[] arguments = new String[] {var2, var3};
        return this.getGame(key, arguments);
    }

    public String getGame(String key, String argument2, String argument3, String argument4) {
        String[] arguments = new String[] {argument2, argument3, argument4};
        return this.getGame(key, arguments);
    }

    public String getGame(String key, String argument2, String argument3, String argument4, String argument5) {
        String[] arguments = new String[] {argument2, argument3, argument4, argument5};
        return this.getGame(key, arguments);
    }

    public String getGame(
            String key, String argument2, String argument3, String argument4, String argument5, String argument6) {
        String[] arguments = new String[] {argument2, argument3, argument4, argument5, argument6};
        return this.getGame(key, arguments);
    }

    public String getGame(String key, int argument2) {
        String[] arguments = new String[] {"" + argument2};
        return this.getGame(key, arguments);
    }

    public String getGame(String key, int argument2, int argument3) {
        String[] arguments = new String[] {"" + argument2, "" + argument3};
        return this.getGame(key, arguments);
    }

    public String getGame(String key, int argument2, int argument3, int argument4) {
        String[] arguments = new String[] {"" + argument2, "" + argument3, "" + argument4};
        return this.getGame(key, arguments);
    }

    public String getGame(String key, int argument2, int argument3, int argument4, int argument5) {
        String[] arguments = new String[] {"" + argument2, "" + argument3, "" + argument4, "" + argument5};
        return this.getGame(key, arguments);
    }

    public String getNumber(long var1) {
        return this.method1726(var1, true);
    }

    public String getDecimalNumber(double var1) {
        if (var1 == 0.0D) {
            return "0";
        } else {
            double var3 = var1 < 0.0D ? -var1 : var1;

            int var5;
            for (var5 = 0; var3 < 100.0D; ++var5) {
                var3 *= 10.0D;
            }

            return this.getNumber(var1, var5);
        }
    }

    public String getNumber(double var1, int var3) {
        return this.getNumber(var1, true, var3);
    }

    public String getNumber(double var1, boolean var3, int var4) {
        if (var4 <= 0) {
            return this.method1726((long) var1, var3);
        } else {
            boolean var5 = var1 < 0.0D;
            if (var5) {
                var1 = -var1;
            }

            long var6 = 1L;

            for (int var8 = 0; var8 < var4; ++var8) {
                var6 *= 10L;
            }

            long var17 = (long) (var1 * (double) var6 + 0.5D);
            long var10 = var17 / var6;
            long var12 = var17 % var6;
            String var14 = "" + var12;
            int var15 = var4 - var14.length();

            for (int var16 = 0; var16 < var15; ++var16) {
                var14 = "0" + var14;
            }

            String var18 = var5 ? "-" : "";
            var18 = var18 + this.method1726(var10, var3);
            var18 = var18 + this.getShared("SeparatorDecimal");
            var18 = var18 + var14;
            return var18;
        }
    }

    public String getTime(long seconds) {
        return this.getTime(seconds * 1000L, false);
    }

    public String getTime(long time, boolean var3) {
        boolean isNegative = time < 0L;
        if (isNegative) {
            time = -time;
        }

        int secondFraction = (int) ((time % 1000L + 5L) / 10L);
        if (!var3) {
            time += 500L;
        }

        time /= 1000L;
        int seconds = (int) (time % 60L);
        time /= 60L;
        int minutes = (int) (time % 60L);
        int hours = (int) (time / 60L);
        boolean includeHours = hours > 0;
        boolean includeMinutes = includeHours || minutes > 0 || !var3;
        boolean includeSecondFraction = var3 && hours == 0;
        String result = isNegative ? "-" : "";
        if (includeHours) {
            result = result + hours;
        }

        if (includeHours && includeMinutes) {
            result = result + this.getShared("SeparatorHourMinute") + (minutes < 10 ? "0" : "");
        }

        if (includeMinutes) {
            result = result + minutes + this.getShared("SeparatorMinuteSecond") + (seconds < 10 ? "0" : "");
        }

        result = result + seconds;
        if (includeSecondFraction) {
            result = result
                    + this.getShared("SeparatorSecondFraction")
                    + (secondFraction < 10 ? "0" : "")
                    + secondFraction;
        }

        return result;
    }

    public String getDate(long timestamp, boolean includeTime) {
        return this.getDate(timestamp, includeTime ? 1 : 0);
    }

    public String getClock(long var1, boolean var3) {
        return this.method1728(var1, var3 ? 1 : 0);
    }

    public String getCurrentDateAndClock(boolean var1) {
        long var2 = System.currentTimeMillis();
        String var4 = this.getDate(var2, var1) + " " + this.getClock(var2, var1);
        return var4;
    }

    public String getDateWithTodayYesterday(long timestamp) {
        return this.getDate(timestamp, 2);
    }

    public char getDecimalSeparator() {
        String var1 = this.getShared("SeparatorDecimal");
        return var1.charAt(0);
    }

    public String getShared(String key) {
        return this.getShared(key, (String[]) null);
    }

    public String getShared(String key, String argument) {
        String[] arguments = new String[] {argument};
        return this.getShared(key, arguments);
    }

    public String getShared(String key, String argument1, String argument2) {
        String[] arguments = new String[] {argument1, argument2};
        return this.getShared(key, arguments);
    }

    public String getShared(String key, String argument2, String argument3, String argument4) {
        String[] arguments = new String[] {argument2, argument3, argument4};
        return this.getShared(key, arguments);
    }

    public String getShared(String key, String argument2, String argument3, String argument4, String argument5) {
        String[] arguments = new String[] {argument2, argument3, argument4, argument5};
        return this.getShared(key, arguments);
    }

    public String getWithQuantity(String key, int quantity) {
        return this.getGame(key, new String[] {"" + quantity}, quantity);
    }

    public String getWithQuantity(String key, String[] arguments, int quantity) {
        return this.getGame(key, arguments, quantity);
    }

    public boolean isLoadingFinished() {
        return this.textLoaderThread == null;
    }

    public void waitLoadingFinished() {
        while (!this.isLoadingFinished()) {
            Tools.sleep(50L);
        }
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public void destroy() {
        if (this.textLoaderThread == null) {
            if (this.gameTable != null) {
                this.gameTable.clear();
                this.gameTable = null;
            }

            if (this.sharedTable != null) {
                this.sharedTable.clear();
                this.sharedTable = null;
            }

            this.parameters = null;
            this.language = null;
            this.errorMessage = null;
        }
    }

    protected String getLanguage() {
        return this.language;
    }

    private String getGame(String key, String[] arguments) {
        return this.getGame(key, arguments, 1);
    }

    private String getGame(String key, String[] arguments, int quantity) {
        String result = this.getText(key, arguments, quantity);
        if (result != null) {
            return result;
        } else {
            result = this.getText(key, quantity);
            if (arguments != null) {
                int argumentsCount = arguments.length;

                for (int i = 0; i < argumentsCount; ++i) {
                    result = Tools.replaceFirst(result, "%" + (i + 1), arguments[i]);
                }
            }

            return result;
        }
    }

    private String getText(String key, String[] arguments, int quantity) {
        if (this.textLoaderThread != null) {
            return "[Loading texts...]";
        } else if (this.gameTable == null && this.errorMessage != null) {
            return "[" + this.errorMessage + "]";
        } else {
            String result = this.getText(key, quantity);
            if (result == null) {
                if (this.debug) {
                    System.out.println("TextManager.getText(\"" + key + "\"): Key not found");
                }

                return this.getFallbackString(key, arguments);
            } else {
                return null;
            }
        }
    }

    private String getFallbackString(String key, String[] arguments) {
        String var3 = "{" + key + "}";
        if (arguments != null) {

            for (String s : arguments) {
                var3 = var3 + " (" + s + ")";
            }
        }

        return var3;
    }

    private String getShared(String key, String[] arguments) {
        return this.getShared(key, arguments, 1);
    }

    private String getShared(String key, String[] arguments, int quantity) {
        if (this.textLoaderThread != null) {
            return "[Loading texts...]";
        } else if (this.sharedTable == null && this.errorMessage != null) {
            return "[" + this.errorMessage + "]";
        } else {
            String localizedString = this.getSharedString(key, quantity);
            if (localizedString == null) {
                return this.getFallbackString(key, arguments);
            } else {
                if (arguments != null) {
                    int argumentsCount = arguments.length;
                    for (int i = 0; i < argumentsCount; ++i) {
                        localizedString = Tools.replaceFirst(localizedString, "%" + (i + 1), arguments[i]);
                    }
                }

                return localizedString;
            }
        }
    }

    private String method1726(long n, boolean separateThousands) {
        if ((n <= -1000L || n >= 1000L) && separateThousands) {
            boolean var4 = n < 0L;
            if (var4) {
                n = -n;
            }

            String var5 = "";
            String var6 = this.getShared("SeparatorThousand");

            do {
                int var7 = (int) (n % 1000L);
                var5 = var7 + var5;
                n /= 1000L;
                if (n > 0L) {
                    if (var7 < 10) {
                        var5 = "00" + var5;
                    } else if (var7 < 100) {
                        var5 = "0" + var5;
                    }

                    var5 = var6 + var5;
                }
            } while (n > 0L);

            if (var4) {
                var5 = "-" + var5;
            }

            return var5;
        } else {
            return "" + n;
        }
    }

    /**
     * @param mode -- 0 == just date, no time, standard format; 1 == just date, no time, locale
     *     format; 2 ==
     */
    private String getDate(long timestamp, int mode) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        if (mode == 0) {
            return year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
        } else {
            String result = this.getShared("DateFormat");
            result = Tools.replaceFirst(result, "%1", "" + day);
            result = Tools.replaceFirst(result, "%2", this.getShared("DateMonth" + month));
            result = Tools.replaceFirst(result, "%3", "" + year);
            if (mode == 1) {
                return result;
            } else {
                try {
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);
                    long startOfToday = today.getTime().getTime();
                    long startOfYesterday = startOfToday - 86400000L;
                    long endOfToday = startOfToday + 86400000L;
                    if (timestamp >= startOfYesterday && timestamp < startOfToday) {
                        result = this.getShared("DateYesterday");
                    }

                    if (timestamp >= startOfToday && timestamp < endOfToday) {
                        result = this.getShared("DateToday");
                    }
                } catch (Exception e) {
                }

                return result;
            }
        }
    }

    private String method1728(long var1, int var3) {
        Calendar var4 = Calendar.getInstance();
        var4.setTime(new Date(var1));
        boolean var5 = true;
        if (var3 == 1 && this.getShared("ClockHours").equals("12")) {
            var5 = false;
        }

        int var6 = var4.get(var5 ? Calendar.HOUR_OF_DAY : Calendar.HOUR);
        int var7 = var4.get(Calendar.MINUTE);
        String var8 = "";
        if (!var5) {
            if (var6 == 0) {
                var6 = 12;
            }

            int var9 = var4.get(Calendar.AM_PM);
            if (var9 == 0) {
                var8 = this.getShared("ClockAM");
            } else if (var9 == 1) {
                var8 = this.getShared("ClockPM");
            }
        }

        if (var3 == 0) {
            return (var6 < 10 ? "0" : "") + var6 + "-" + (var7 < 10 ? "0" : "") + var7;
        } else {
            String var10 = this.getShared("ClockFormat");
            var10 = Tools.replaceFirst(var10, "%1", "" + var6);
            var10 = Tools.replaceFirst(var10, "%2", (var7 < 10 ? "0" : "") + var7);
            if (!var5) {
                var10 = Tools.replaceFirst(var10, "%3", var8);
            }

            return var10;
        }
    }

    protected String getText(String key, int quantity) {
        key = key.toLowerCase();
        if (this.useLanguageFiles) {
            LocalizationNode localizationNode = (LocalizationNode) this.gameTable.get(key);
            return localizationNode == null ? null : localizationNode.getLocalization(quantity);
        } else {
            return (String) this.gameTable.get(key);
        }
    }

    protected String getSharedString(String key, int quantity) {
        key = key.toLowerCase();
        if (this.useLanguageFiles) {
            LocalizationNode localizationNode = (LocalizationNode) this.sharedTable.get(key);
            return localizationNode == null ? null : localizationNode.getLocalization(quantity);
        } else {
            return (String) this.sharedTable.get(key);
        }
    }

    private void loadTexts(Applet applet) {
        if (this.useLanguageFiles) {
            this.loadLanguageFiles(applet);
        } else {
            this.loadLocaleFiles(applet);
        }
    }

    private void loadLocaleFiles(Applet applet) {
        URL codeBase = applet.getCodeBase();
        this.gameTable = this.loadLocalizationTable(codeBase);

        try {
            if (FileUtil.isFileUrl(codeBase)) {
                codeBase = new URL(codeBase, FileUtil.RESOURCE_DIR);
            } else {
                codeBase = new URL(codeBase, "../Shared/");
            }
        } catch (MalformedURLException e) {
        }

        this.sharedTable = this.loadLocalizationTable(codeBase);
    }

    private Hashtable<String, String> loadLocalizationTable(URL baseUrl) {
        Hashtable<String, String> localizationTable = new Hashtable<>();
        BufferedReader reader = null;
        String languageFileName = this.language + ".loc";

        try {
            URL localeUrl = new URL(baseUrl, "locale/");
            localeUrl = new URL(localeUrl, languageFileName);
            InputStream inputStream = localeUrl.openStream();

            InputStreamReader inputStreamReader;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "Cp1252");
            } catch (UnsupportedEncodingException e) {
                inputStreamReader = new InputStreamReader(inputStream);
            }

            reader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() > 0 && line.charAt(0) != '#') {
                    int equalSignLocation = line.indexOf('=');
                    if (equalSignLocation <= 0) {
                        if (this.debug) {
                            System.out.println(
                                    "Missing '='-character in \"" + this.language + "\"-locale file: \"" + line + "\"");
                            Thread.dumpStack();
                        }
                    } else {
                        String key = line.substring(0, equalSignLocation).trim();
                        if (key.length() == 0) {
                            if (this.debug) {
                                System.out.println(
                                        "Empty key in \"" + this.language + "\"-locale file: \"" + line + "\"");
                                Thread.dumpStack();
                            }
                        } else {
                            localizationTable.put(
                                    key.toLowerCase(),
                                    line.substring(equalSignLocation + 1).trim());
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            if (this.debug) {
                System.out.println("Missing localization file \"" + languageFileName + "\"");
            }

            this.errorMessage = "Texts for '" + this.language + "' not available";
            localizationTable = null;
        } catch (Exception e) {
            if (this.debug) {
                e.printStackTrace();
            }

            this.errorMessage = e.toString();
            localizationTable = null;
        }

        try {
            reader.close();
        } catch (Exception e) {
        }

        return localizationTable;
    }

    private void loadLanguageFiles(Applet applet) {
        URL codeBase = applet.getCodeBase();
        String var5 = null;
        int slashLocation = this.language.indexOf('/');
        if (slashLocation > 0) {
            var5 = this.language.substring(slashLocation + 1);
            this.language = this.language.substring(0, slashLocation);
        }

        String languageDirectory;
        String gameFilename;
        String var7;
        int var8;
        if (FileUtil.isFileUrl(codeBase)) {
            var7 = codeBase.toString();
            var8 = var7.indexOf(':', var7.indexOf(':') + 1) + 2;
            int var9 = var7.indexOf('/', var8);

            try {
                URL var10 = new URL(codeBase, FileUtil.LANGUAGE_DIR);
                var10 = new URL(var10, this.language + "/");
                languageDirectory = var10.toExternalForm();
            } catch (MalformedURLException e) {
                languageDirectory = "file:" + FileUtil.LANGUAGE_DIR + this.language + "/";
            }

            gameFilename = var7.substring(var8, var9);
        } else {
            var7 = codeBase.toString();
            var8 = var7.length();
            if (var7.charAt(var8 - 1) == '/') {
                var7 = var7.substring(0, var8 - 1);
                --var8;
            }

            int slashLocation2 = var7.lastIndexOf('/');
            languageDirectory = var7.substring(0, slashLocation2 + 1) + "l10n/" + this.language + "/";
            gameFilename = var7.substring(slashLocation2 + 1);
        }

        if (var5 != null) {
            gameFilename = var5;
        }

        this.gameTable = this.readTable(languageDirectory + gameFilename + ".xml");
        this.sharedTable = this.readTable(languageDirectory + "Shared.xml");
    }

    private Hashtable<String, LocalizationNode> readTable(String fileUrl) {
        EncodedXmlReader reader = new EncodedXmlReader(fileUrl, /*this.aBoolean1519*/ true);
        XmlUnit unit = reader.readXmlUnit();
        if (unit == null) {
            System.out.println("Failed to read localization file '" + fileUrl + "'");
            this.errorMessage = "XML read error";
            return null;
        } else {
            XmlUnit[] children = unit.getChildren("str");
            Hashtable<String, LocalizationNode> table = new Hashtable<>();

            for (XmlUnit child : children) {
                table.put(
                        child.getAttribute("key").toLowerCase(),
                        new LocalizationNode(
                                this, this.language, child, Tools.getBoolean(child.getAttribute("reverse"))));
            }

            return table;
        }
    }
}
