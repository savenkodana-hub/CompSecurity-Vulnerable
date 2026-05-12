package com.communicationltd.security;

import com.communicationltd.config.PasswordPolicyConfig;

public class PasswordValidator {

    private static final String WEAK_PASSWORD_MESSAGE =
            "Password is too weak, please try again";

    public static String validate(String password) {

        if (password == null || password.length() < PasswordPolicyConfig.getMinLength()) {
            return WEAK_PASSWORD_MESSAGE;
        }

        if (PasswordPolicyConfig.requireUppercase() && !password.matches(".*[A-Z].*")) {
            return WEAK_PASSWORD_MESSAGE;
        }

        if (PasswordPolicyConfig.requireLowercase() && !password.matches(".*[a-z].*")) {
            return WEAK_PASSWORD_MESSAGE;
        }

        if (PasswordPolicyConfig.requireDigit() && !password.matches(".*\\d.*")) {
            return WEAK_PASSWORD_MESSAGE;
        }

        if (PasswordPolicyConfig.requireSpecial() && !password.matches(".*[^a-zA-Z0-9].*")) {
            return WEAK_PASSWORD_MESSAGE;
        }

        String allowedPattern = PasswordPolicyConfig.getAllowedPattern();
        if (allowedPattern != null && !allowedPattern.isBlank() && !password.matches(allowedPattern)) {
            return WEAK_PASSWORD_MESSAGE;
        }

        for (String word : PasswordPolicyConfig.getForbiddenWords()) {
            if (!word.isBlank() && password.toLowerCase().contains(word.toLowerCase())) {
                return WEAK_PASSWORD_MESSAGE;
            }
        }

        return null;
    }
}
