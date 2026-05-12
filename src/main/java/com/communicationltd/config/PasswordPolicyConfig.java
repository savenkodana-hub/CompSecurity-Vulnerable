package com.communicationltd.config;

import java.io.InputStream;
import java.util.Properties;

public class PasswordPolicyConfig {

    private static final Properties PROPERTIES = loadProperties();

    private static Properties loadProperties() {
        Properties properties = new Properties();

        try (InputStream input = PasswordPolicyConfig.class
                .getClassLoader()
                .getResourceAsStream("password-config.properties")) {

            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load password-config.properties", e);
        }

        return properties;
    }

    private static String get(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static int getMinLength() {
        return Integer.parseInt(get("password.min.length", "10"));
    }

    public static boolean requireUppercase() {
        return Boolean.parseBoolean(get("password.require.uppercase", "true"));
    }

    public static boolean requireLowercase() {
        return Boolean.parseBoolean(get("password.require.lowercase", "true"));
    }

    public static boolean requireDigit() {
        return Boolean.parseBoolean(get("password.require.digit", "true"));
    }

    public static boolean requireSpecial() {
        return Boolean.parseBoolean(get("password.require.special", "true"));
    }

    public static String getAllowedPattern() {
        return get("password.allowed.pattern", "");
    }

    public static int getPasswordHistoryLimit() {
        return Integer.parseInt(get("password.history.limit", "3"));
    }

    public static int getMaxLoginAttempts() {
        return Integer.parseInt(get("login.max.attempts", "3"));
    }

    public static String[] getForbiddenWords() {
        String words = get("password.forbidden.words", "password,admin,123456,qwerty");
        return words.split("\\s*,\\s*");
    }
}
