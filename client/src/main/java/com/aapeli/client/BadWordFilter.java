package com.aapeli.client;

import java.util.StringTokenizer;
import org.moparforia.shared.Language;

public final class BadWordFilter {

    private static final String aString1335 = "0123456789 l |¦!¡( @¤× ª°º¹²³ ©® ¥ßµ¢ àáâãåçèéêëìíîïñòóôõøùúûüýÿæ";
    private static final String aString1336 = "oizeasgtbp i iiiic aox aooize cr ybuc aaaäoceeeeiiiinooooouuuuyye";
    private static final String aString1337 = "¦!¡ []{}() ~ ª°º¹²³* `´\"";
    private static final String aString1338 = "||| |||||| - ''''''' '''";
    private static final String[] aStringArray1339 = new String[] {
        "|<", "<>", "><", "/\\/\\", "\\/\\/", "/\\/", "/\\", "\\/", "/-\\", "|-|", "|\\/|", "|/\\|", "|\\|", "|/|",
        "|_|", "_|", "|_", "(_)", "_)", "(_", "||", "'|'", "|3", "|)", "|'"
    };
    private static final String[] aStringArray1340 = new String[] {
        "kk", "|z", "xx", "~xy ", "dbc", "nnn", "aa", "vv", "aaa", "hhh", "~xy ", "dbc", "nnn", "nnn", "f`a", "y",
        "y", "f`a", "y", "y", "|z", "ttt", "bb", "dd", "pp"
    };
    private static final String[] aStringArray1341 = new String[] {"He'll", "he'll"};
    private String aString1342;
    private String aString1343;
    private String[] aStringArray1344;
    private String[] aStringArray1345;
    private char[] aCharArray1346;

    public BadWordFilter(TextManager var1) {
        this(var1, true, null);
    }

    protected BadWordFilter(TextManager var1, boolean var2) {
        this(var1, var2, null);
    }

    protected BadWordFilter(TextManager var1, boolean var2, String var3) {
        String var4 = var1.getText(var2 ? "BadWords" : "BadNicks");
        String var5 = var1.getText("GoodWords");
        if (var3 == null) {
            var3 = var1.getText("CurseChars");
        }

        this.aString1342 = aString1335;
        this.aString1343 = aString1336;
        if (var1.getLanguage().equals(Language.FINNISH)) {
            this.aString1342 = this.aString1342 + "bdgw";
            this.aString1343 = this.aString1343 + "ptkv";
        }

        StringTokenizer var6 = new StringTokenizer(var4, ",");
        int var7 = var6.countTokens();
        this.aStringArray1344 = new String[var7];

        int var8;
        for (var8 = 0; var8 < var7; ++var8) {
            this.aStringArray1344[var8] = this.method1570(var6.nextToken(), this.aString1342, this.aString1343);
        }

        var6 = new StringTokenizer(var5, ",");
        var7 = var6.countTokens();
        this.aStringArray1345 = new String[var7];

        for (var8 = 0; var8 < var7; ++var8) {
            this.aStringArray1345[var8] = this.method1570(var6.nextToken(), this.aString1342, this.aString1343);
        }

        this.aCharArray1346 = new char[var3.length()];

        for (var8 = 0; var8 < this.aCharArray1346.length; ++var8) {
            this.aCharArray1346[var8] = var3.charAt(var8);
        }
    }

    public boolean containsBadWords(String var1) {
        int[] var2 = this.method1566(var1);
        for (int i : var2) {
            if (i < 0) {
                return true;
            }
        }

        return false;
    }

    public String filter(String var1) {
        try {
            int[] var2 = this.method1566(var1);
            int var3 = var2.length;
            StringBuffer var4 = new StringBuffer(var3);

            for (int var5 = 0; var5 < var3; ++var5) {
                if (var2[var5] != 0 && var2[var5] != 1) {
                    var4.append(this.method1575());
                } else {
                    var4.append(var1.charAt(var5));
                }
            }

            return var4.toString();
        } catch (Exception var6) {
            return var1;
        }
    }

    private int[] method1566(String var1) {
        int var2 = var1.length();
        int[] var3 = new int[var2];

        for (int var4 = 0; var4 < var2; ++var4) {
            var3[var4] = this.method1567(var1.charAt(var4));
        }

        int[] var6 = new int[var2];

        for (int var5 = 0; var5 < var2; ++var5) {
            var6[var5] = 0;
        }

        this.method1568(var1, var6);
        this.method1569(var1, var6, var3, this.aStringArray1345, 1);
        this.method1569(var1, var6, var3, this.aStringArray1344, -1);
        return var6;
    }

    private int method1567(char var1) {
        return Character.isUpperCase(var1) ? 1 : (Character.isLowerCase(var1) ? -1 : 0);
    }

    private void method1568(String var1, int[] var2) {
        for (String text : aStringArray1341) {
            int var4 = text.length();

            for (int var5 = 0; (var5 = var1.indexOf(text, var5)) >= 0; var5 += var4) {
                for (int var6 = 0; var6 < var4; ++var6) {
                    var2[var5 + var6] = 1;
                }
            }
        }
    }

    private void method1569(String var1, int[] var2, int[] var3, String[] var4, int var5) {
        String var6 = var1.toLowerCase();
        this.method1572(var6, var2, var3, var4, var5);
        var6 = this.method1570(var6, this.aString1342, this.aString1343);
        this.method1572(var6, var2, var3, var4, var5);
        var6 = var1.toLowerCase();
        var6 = this.method1570(var6, "¦!¡ []{}() ~ ª°º¹²³* `´\"", "||| |||||| - ''''''' '''");
        var6 = this.method1571(var6, aStringArray1339, aStringArray1340);
        this.method1572(var6, var2, var3, var4, var5);
        var6 = this.method1570(var6, this.aString1342, this.aString1343);
        this.method1572(var6, var2, var3, var4, var5);
    }

    private String method1570(String var1, String var2, String var3) {
        int var4 = var2.length();

        for (int var5 = 0; var5 < var4; ++var5) {
            char var6 = var2.charAt(var5);
            if (var6 != 32) {
                var1 = var1.replace(var6, var3.charAt(var5));
            }
        }

        return var1;
    }

    private String method1571(String var1, String[] var2, String[] var3) {
        int var4 = var2.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            var1 = var1.replaceAll(var2[var5], var3[var5]);
        }

        return var1;
    }

    private void method1572(String var1, int[] var2, int[] var3, String[] var4, int var5) {
        int var6 = var1.length();

        for (String text : var4) {
            for (int var8 = 0; var8 < var6; ++var8) {
                this.method1573(var1, var8, var2, var3, text, var5);
            }
        }
    }

    private void method1573(String var1, int var2, int[] var3, int[] var4, String var5, int var6) {
        int var7 = this.method1574(var1, var2, var3);
        if (var7 == var2) {
            int var8 = var1.length();
            int var9 = var5.length();
            int var10 = 1;
            int var11 = var7;
            char var13 = var5.charAt(0);
            boolean var14 = true;
            int var15 = 0;
            int var16 = 0;

            while (true) {
                char var17 = var1.charAt(var7);
                if (var17 == var13 && var10 < var9 && var5.charAt(var10) == var13) {
                    ++var10;
                }

                int var18;
                if (var17 != var13) {
                    if (var14) {
                        return;
                    }

                    if (var10 == var9) {
                        for (var18 = var2; var18 < var11; ++var18) {
                            var3[var18] = var6;
                        }

                        return;
                    }

                    var13 = var5.charAt(var10);
                    if (var17 != var13) {
                        return;
                    }

                    ++var10;
                }

                var14 = false;
                if (var6 == 1) {
                    if (var4[var7] == 0) {
                        return;
                    }

                    ++var15;
                    if (var15 == 2) {
                        var16 = var4[var7];
                    } else if (var15 > 2 && var4[var7] != var16) {
                        return;
                    }
                }

                ++var7;
                if (var7 == var8) {
                    if (var10 != var9) {
                        return;
                    }

                    for (var18 = var2; var18 < var7; ++var18) {
                        var3[var18] = var6;
                    }

                    return;
                }

                var11 = var7;
                int var12 = this.method1574(var1, var7, var3);
                if (var12 == -1) {
                    if (var10 != var9) {
                        return;
                    }

                    for (var18 = var2; var18 < var7; ++var18) {
                        var3[var18] = var6;
                    }

                    return;
                }

                if (var6 == 1 && var12 > var7) {
                    if (var10 != var9) {
                        return;
                    }

                    for (var18 = var2; var18 < var11; ++var18) {
                        var3[var18] = var6;
                    }

                    return;
                }

                var7 = var12;
            }
        }
    }

    private int method1574(String var1, int var2, int[] var3) {
        int var4 = var1.length();

        do {
            if (var3[var2] != 0) {
                return -1;
            }

            char var5 = var1.charAt(var2);
            if (var5 >= 97 && var5 <= 122 || var5 == 228 || var5 == 246) {
                return var2;
            }

            ++var2;
        } while (var2 < var4);

        return -1;
    }

    private char method1575() {
        int var1 = (int) (Math.random() * (double) this.aCharArray1346.length);
        return this.aCharArray1346[var1];
    }
}
