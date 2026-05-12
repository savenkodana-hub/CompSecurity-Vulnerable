package com.communicationltd.security;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class TokenGenerator {

    public static String generateToken(String email) {
        try {
            String raw = email + System.currentTimeMillis() + new SecureRandom().nextInt();

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] hash = md.digest(raw.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString().substring(0, 6);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}