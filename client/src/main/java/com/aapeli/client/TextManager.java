package com.aapeli.client;

import com.aapeli.tools.Tools;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.moparforia.shared.Language;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class TextManager {

    private Language language;
    private Map<String, String> texts;
    private String errorMessage;
    private boolean debug;

    public TextManager(Parameters parameters, boolean debug) {
        this.debug = debug;
        this.texts = new HashMap<>();
        this.errorMessage = null;
        this.language = parameters.getLanguage();
        this.loadTexts();
    }

    public void setLanguage(Language language) {
        this.language = language;
        this.loadTexts();
    }

    public String getText(String key) {
        return this.getInternal(key, null);
    }

    public boolean isAvailable(String key) {
        return this.texts.containsKey(key.toLowerCase());
    }

    public String getIfAvailable(String key, String fallback) {
        return this.texts.getOrDefault(key.toLowerCase(), fallback);
    }

    public String getText(String key, String... args) {
        return this.getInternal(key, args);
    }

    public String getText(String key, int... args) {
        String[] stringArgs = Arrays.stream(args).mapToObj(String::valueOf).toArray(String[]::new);
        return this.getInternal(key, stringArgs);
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
            var18 = var18 + this.getText("SeparatorDecimal");
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
            result = result + this.getText("SeparatorHourMinute") + (minutes < 10 ? "0" : "");
        }

        if (includeMinutes) {
            result = result + minutes + this.getText("SeparatorMinuteSecond") + (seconds < 10 ? "0" : "");
        }

        result = result + seconds;
        if (includeSecondFraction) {
            result = result
                    + this.getText("SeparatorSecondFraction")
                    + (secondFraction < 10 ? "0" : "")
                    + secondFraction;
        }

        return result;
    }

    public String getDateWithTodayYesterday(long timestamp) {
        return this.getDate(timestamp, 2);
    }

    public void destroy() {
        if (this.texts != null) {
            this.texts.clear();
            this.texts = null;
        }

        this.language = null;
        this.errorMessage = null;
    }

    protected Language getLanguage() {
        return this.language;
    }

    private String getInternal(String key, String[] arguments) {
        if (this.texts == null && this.errorMessage != null) {
            return "[" + this.errorMessage + "]";
        } else {
            String localizedString = this.texts.get(key.toLowerCase());
            if (localizedString == null) {
                return this.getFallbackString(key, arguments);
            } else {
                if (arguments != null) {
                    int argumentsCount = arguments.length;
                    for (int i = 0; i < argumentsCount; ++i) {
                        localizedString = localizedString.replaceFirst("%" + (i + 1), arguments[i]);
                    }
                }

                return localizedString;
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

    private String method1726(long n, boolean separateThousands) {
        if ((n <= -1000L || n >= 1000L) && separateThousands) {
            boolean var4 = n < 0L;
            if (var4) {
                n = -n;
            }

            String var5 = "";
            String var6 = this.getText("SeparatorThousand");

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
            String result = this.getText("DateFormat");
            result = result.replaceFirst("%1", "" + day);
            result = result.replaceFirst("%2", this.getText("DateMonth" + month));
            result = result.replaceFirst("%3", "" + year);
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
                        result = this.getText("DateYesterday");
                    }

                    if (timestamp >= startOfToday && timestamp < endOfToday) {
                        result = this.getText("DateToday");
                    }
                } catch (Exception e) {
                }

                return result;
            }
        }
    }

    private void loadTexts() {
        String resourcePath = "/l10n/" + this.language + "/AGolf.xml";
        try {
            InputStream in = this.getClass().getResourceAsStream(resourcePath);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(in);

            NodeList localizationNodes = document.getElementsByTagName("str");
            Map<String, String> table = new HashMap<>();

            for (int i = 0; i < localizationNodes.getLength(); ++i) {
                Node node = localizationNodes.item(i);
                String key =
                        node.getAttributes().getNamedItem("key").getNodeValue().toLowerCase();
                String translation = node.getTextContent();

                Node reverseNode = node.getAttributes().getNamedItem("reverse");
                boolean reverse = false;
                if (reverseNode != null) {
                    reverse = Tools.getBoolean(reverseNode.getTextContent());
                }
                if (reverse) {
                    translation = new StringBuffer(translation).reverse().toString();
                }

                table.put(key, translation);
            }

            this.texts = table;
        } catch (Exception e) {
            System.out.println("Failed to read localization file '" + resourcePath + "'");
            this.errorMessage = "XML read error";
        }
    }
}
