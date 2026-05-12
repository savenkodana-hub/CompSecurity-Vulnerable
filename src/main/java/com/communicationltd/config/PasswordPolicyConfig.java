package com.communicationltd.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

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
        Set<String> forbiddenWords = new LinkedHashSet<>();

        String words = get("password.forbidden.words", "password,admin,123456,qwerty");
        for (String word : words.split("\\s*,\\s*")) {
            if (!word.isBlank()) {
                forbiddenWords.add(word);
            }
        }

        forbiddenWords.addAll(loadDictionaryWords());

        return forbiddenWords.toArray(new String[0]);
    }

    private static Set<String> loadDictionaryWords() {
        Set<String> words = new LinkedHashSet<>();

        try (InputStream input = PasswordPolicyConfig.class
                .getClassLoader()
                .getResourceAsStream("dictionary.txt")) {

            if (input == null) {
                return words;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String word = line.trim();
                    if (!word.isBlank()) {
                        words.add(word);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load dictionary.txt", e);
        }

        return words;
    }
}
