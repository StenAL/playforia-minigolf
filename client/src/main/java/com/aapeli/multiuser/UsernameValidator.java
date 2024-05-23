package com.aapeli.multiuser;

import java.util.regex.Pattern;

public class UsernameValidator {
    public static Pattern INVALID_VALID_USERNAME_PATTERN = Pattern.compile("[^a-zA-Z0-9 ]");

    public static boolean isValidUsername(String username) {
        boolean invalid = INVALID_VALID_USERNAME_PATTERN.matcher(username).find() || username.trim().isEmpty();
        return !invalid;

    }
}
