package com.communicationltd.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class PasswordHasher {

    public static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hash(String password, String salt) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(
                    getSecretKey().getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );

            mac.init(keySpec);

            String data = salt + password;
            byte[] result = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(result);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSecretKey() {
        String envSecret = System.getenv("HMAC_SECRET");
        if (envSecret != null && !envSecret.isBlank()) {
            return envSecret;
        }

        Properties properties = new Properties();

        try (InputStream input = PasswordHasher.class
                .getClassLoader()
                .getResourceAsStream("security-config.properties")) {

            if (input != null) {
                properties.load(input);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load security-config.properties", e);
        }

        String configuredSecret = properties.getProperty("hmac.secret");
        if (configuredSecret == null || configuredSecret.isBlank()) {
            throw new IllegalStateException("HMAC_SECRET or hmac.secret must be configured");
        }

        return configuredSecret;
    }
}
