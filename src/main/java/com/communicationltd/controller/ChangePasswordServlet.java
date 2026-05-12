package com.communicationltd.controller;

import com.communicationltd.security.PasswordHasher;
import com.communicationltd.security.PasswordValidator;
import com.communicationltd.config.PasswordPolicyConfig;
import com.communicationltd.util.DatabaseConnection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

@WebServlet("/change-password")
public class ChangePasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String email = (String) session.getAttribute("email");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            response.getWriter().println("Passwords do not match");
            return;
        }

        String validation = PasswordValidator.validate(newPassword);
        if (validation != null) {
            response.getWriter().println(validation);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            PreparedStatement selectUser = conn.prepareStatement(
                    "SELECT password, salt FROM users WHERE email = ?"
            );
            selectUser.setString(1, email);
            ResultSet userRs = selectUser.executeQuery();

            if (!userRs.next()) {
                response.getWriter().println("User not found");
                return;
            }

            String currentHash = userRs.getString("password");
            String currentSalt = userRs.getString("salt");

            String oldPasswordHash = PasswordHasher.hash(oldPassword, currentSalt);

            if (!currentHash.equals(oldPasswordHash)) {
                response.getWriter().println("Current password is incorrect");
                return;
            }

            PreparedStatement historyCheck = conn.prepareStatement(
                    "SELECT old_password_hash, salt FROM password_history WHERE user_email = ? ORDER BY id DESC LIMIT ?"
            );
            historyCheck.setString(1, email);
            historyCheck.setInt(2, PasswordPolicyConfig.getPasswordHistoryLimit());
            ResultSet historyRs = historyCheck.executeQuery();

            while (historyRs.next()) {
                String oldHistoryHash = historyRs.getString("old_password_hash");
                String oldHistorySalt = historyRs.getString("salt");

                String newPasswordWithOldSalt = PasswordHasher.hash(newPassword, oldHistorySalt);

                if (oldHistoryHash.equals(newPasswordWithOldSalt)) {
                    response.getWriter().println("You cannot use one of your last 3 passwords");
                    return;
                }
            }

            String newPasswordWithCurrentSalt = PasswordHasher.hash(newPassword, currentSalt);
            if (currentHash.equals(newPasswordWithCurrentSalt)) {
                response.getWriter().println("You cannot use your current password");
                return;
            }

            PreparedStatement saveHistory = conn.prepareStatement(
                    "INSERT INTO password_history(user_email, old_password_hash, salt) VALUES (?, ?, ?)"
            );
            saveHistory.setString(1, email);
            saveHistory.setString(2, currentHash);
            saveHistory.setString(3, currentSalt);
            saveHistory.executeUpdate();

            String newSalt = generateSalt();
            String newHash = PasswordHasher.hash(newPassword, newSalt);

            PreparedStatement update = conn.prepareStatement(
                    "UPDATE users SET password = ?, salt = ? WHERE email = ?"
            );
            update.setString(1, newHash);
            update.setString(2, newSalt);
            update.setString(3, email);
            update.executeUpdate();

            session.setAttribute("passwordChanged", "Password changed successfully");
            response.sendRedirect("dashboard.jsp");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
