package com.aapeli.tools;

import java.util.Calendar;
import java.util.StringTokenizer;

public class Tools {

    private static long aLong1731 = -1L;
    private static long aLong1732 = -1L;

    public static boolean sleep(long var0) {
        if (var0 <= 0L) {
            return true;
        } else {
            try {
                Thread.sleep(var0);
                return true;
            } catch (InterruptedException var3) {
                return false;
            }
        }
    }

    public static String changeToSaveable(String var0) {
        int var1 = var0.length();
        StringBuffer var2 = new StringBuffer(var1 * 2);

        for (int var4 = 0; var4 < var1; ++var4) {
            char var3 = var0.charAt(var4);
            if (var3 == '^') {
                var2.append("$p");
            } else if (var3 == '$') {
                var2.append("$d");
            } else {
                var2.append(var3);
            }
        }

        return var2.toString();
    }

    public static String changeFromSaveable(String s) {
        int length = s.length();
        StringBuffer sb = new StringBuffer(length);

        for (int i = 0; i < length; ++i) {
            char c = s.charAt(i);
            if (c == '$') {
                ++i;
                c = s.charAt(i);
                if (c == 'p') {
                    sb.append('^');
                } else {
                    if (c != 'd') {
                        System.out.println("Program error: Tools.changeFromSaveable(\""
                                + s
                                + "\"), "
                                + "unexpected character '"
                                + c
                                + "' after '$'");
                        return null;
                    }

                    sb.append('$');
                }
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String[] separateString(String var0, String var1) {
        StringTokenizer var2 = new StringTokenizer(var0, var1);
        int var3 = var2.countTokens();
        String[] var4 = new String[var3];

        for (int var5 = 0; var5 < var3; ++var5) {
            var4[var5] = var2.nextToken();
        }

        return var4;
    }

    public static String replaceFirst(String var0, String var1, String var2) {
        int var3 = var0.indexOf(var1);
        if (var3 == -1) {
            return var0;
        } else {
            var0 = var0.substring(0, var3) + var2 + var0.substring(var3 + var1.length());
            return var0;
        }
    }

    public static String replaceAll(String var0, String var1, String var2) {
        int var3 = var1.length();
        int var4 = var2.length();

        int var6;
        for (int var5 = 0; (var6 = var0.indexOf(var1, var5)) >= 0; var5 = var6 + var4) {
            var0 = var0.substring(0, var6) + var2 + var0.substring(var6 + var3);
        }

        return var0;
    }

    public static boolean getBoolean(String var0) {
        if (var0 != null && var0.length() > 0) {
            var0 = var0.toLowerCase();
            char var1 = var0.charAt(0);
            if (var1 == 't' || var1 == 'y' || var0.equals("on") || var1 == '1') {
                return true;
            }
        }

        return false;
    }

    public static String reverse(String var0) {
        if (var0 == null) {
            return null;
        } else {
            int var1 = var0.length();
            if (var1 == 0) {
                return var0;
            } else {
                StringBuffer var2 = new StringBuffer(var1);

                for (int var3 = 0; var3 < var1; ++var3) {
                    var2.append(var0.charAt(var1 - 1 - var3));
                }

                return var2.toString();
            }
        }
    }

    public static void printTimeElapsed(String message) {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);
        int ms = cal.get(Calendar.MILLISECOND);
        if (hour < 10) {
            sb.append(0);
        }

        sb.append(hour).append(':');
        if (min < 10) {
            sb.append(0);
        }

        sb.append(min).append(':');
        if (sec < 10) {
            sb.append(0);
        }

        sb.append(sec).append(':');
        if (ms < 100) {
            sb.append(0);
        }

        if (ms < 10) {
            sb.append(0);
        }

        sb.append(ms).append(' ');
        if (aLong1731 < 0L) {
            aLong1731 = aLong1732 = System.currentTimeMillis();
            sb.append("00:00:000 00:00:000");
        } else {
            long var7 = System.currentTimeMillis();
            int var9 = (int) (var7 - aLong1731);
            int var10 = (int) (var7 - aLong1732);
            aLong1732 = var7;
            method1875(sb, var9);
            sb.append(' ');
            method1875(sb, var10);
        }

        sb.append("] ").append(message);
        System.out.println(sb);
    }

    private static void method1875(StringBuffer var0, int var1) {
        int var2 = var1 / 60000;
        var1 -= var2 * 60000;
        int var3 = var1 / 1000;
        int var4 = var1 - var3 * 1000;
        if (var2 < 10) {
            var0.append(0);
        }

        var0.append(var2).append(':');
        if (var3 < 10) {
            var0.append(0);
        }

        var0.append(var3).append(':');
        if (var4 < 100) {
            var0.append(0);
            if (var4 < 10) {
                var0.append(0);
            }
        }

        var0.append(var4);
    }
}
