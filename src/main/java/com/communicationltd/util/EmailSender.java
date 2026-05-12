package com.communicationltd.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.util.Properties;

public class EmailSender {

    public static void sendResetCode(String toEmail, String code) {

        final String fromEmail = getConfigValue("MAIL_USERNAME", "mail.username");
        final String appPassword = getConfigValue("MAIL_APP_PASSWORD", "mail.app.password");

        if (fromEmail == null || fromEmail.isBlank() || appPassword == null || appPassword.isBlank()) {
            throw new IllegalStateException("Mail username and app password must be configured");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", getConfigValue("MAIL_SMTP_HOST", "mail.smtp.host", "smtp.gmail.com"));
        props.put("mail.smtp.port", getConfigValue("MAIL_SMTP_PORT", "mail.smtp.port", "587"));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, appPassword);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );

            message.setSubject("Password Reset Code");
            message.setText("Your reset code is: " + code);

            Transport.send(message);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getConfigValue(String envName, String propertyName) {
        return getConfigValue(envName, propertyName, null);
    }

    private static String getConfigValue(String envName, String propertyName, String defaultValue) {
        String envValue = System.getenv(envName);
        if (envValue != null && !envValue.isBlank()) {
            return envValue;
        }

        Properties properties = loadMailProperties();
        return properties.getProperty(propertyName, defaultValue);
    }

    private static Properties loadMailProperties() {
        Properties properties = new Properties();

        try (InputStream input = EmailSender.class
                .getClassLoader()
                .getResourceAsStream("mail-config.properties")) {

            if (input != null) {
                properties.load(input);
            }

            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load mail-config.properties", e);
        }
    }
}
