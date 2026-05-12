package com.communicationltd.security;

public class InputSanitizer {

    public static String sanitize(String input) {
        if (input == null) return "";

        return input.replace("<", "&lt;")
                .replace(">", "&gt;")
                .trim();
    }
}