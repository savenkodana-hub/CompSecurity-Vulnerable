package com.communicationltd.controller;

import com.communicationltd.security.PasswordHasher;
import com.communicationltd.security.PasswordValidator;
import com.communicationltd.config.PasswordPolicyConfig;
import com.communicationltd.util.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("resetEmail") == null) {
            response.sendRedirect("forgot-password.jsp");
            return;
        }

        String email = (String) session.getAttribute("resetEmail");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        if (!newPassword.equals(confirmPassword)) {
            showResetPasswordError(request, response, "Passwords do not match");
            return;
        }

        String validation = PasswordValidator.validate(newPassword);
        if (validation != null) {
            showResetPasswordError(request, response, validation);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {

            PreparedStatement selectUser = conn.prepareStatement(
                    "SELECT password, salt FROM users WHERE email = ?"
            );
            selectUser.setString(1, email);
            ResultSet userRs = selectUser.executeQuery();

            if (!userRs.next()) {
                showResetPasswordError(request, response, "User not found");
                return;
            }

            String currentHash = userRs.getString("password");
            String currentSalt = userRs.getString("salt");

            PreparedStatement historyCheck = conn.prepareStatement(
                    "SELECT old_password_hash, salt FROM password_history WHERE user_email = ? ORDER BY id DESC LIMIT ?"
            );
            historyCheck.setString(1, email);
            historyCheck.setInt(2, PasswordPolicyConfig.getPasswordHistoryLimit());
            ResultSet historyRs = historyCheck.executeQuery();

            while (historyRs.next()) {
                String oldHash = historyRs.getString("old_password_hash");
                String oldSalt = historyRs.getString("salt");

                String checkHash = PasswordHasher.hash(newPassword, oldSalt);

                if (oldHash.equals(checkHash)) {
                    showResetPasswordError(request, response, "You cannot use one of your last 3 passwords");
                    return;
                }
            }

            String checkCurrent = PasswordHasher.hash(newPassword, currentSalt);
            if (currentHash.equals(checkCurrent)) {
                showResetPasswordError(request, response, "You cannot use your current password");
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

            PreparedStatement updateUser = conn.prepareStatement(
                    "UPDATE users SET password = ?, salt = ? WHERE email = ?"
            );
            updateUser.setString(1, newHash);
            updateUser.setString(2, newSalt);
            updateUser.setString(3, email);
            updateUser.executeUpdate();

            PreparedStatement markUsed = conn.prepareStatement(
                    "UPDATE password_reset_tokens SET used = 1 WHERE email = ?"
            );
            markUsed.setString(1, email);
            markUsed.executeUpdate();

            session.removeAttribute("resetEmail");

            request.setAttribute("loginSuccess", "Password changed successfully, please log in");
            request.getRequestDispatcher("/login.jsp").forward(request, response);

        } catch (Exception e) {
            showResetPasswordError(request, response, "Could not reset password, please try again");
        }
    }

    private void showResetPasswordError(HttpServletRequest request, HttpServletResponse response, String message)
            throws ServletException, IOException {

        request.setAttribute("resetPasswordError", message);
        request.getRequestDispatcher("/new-password.jsp").forward(request, response);
    }

    private static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
