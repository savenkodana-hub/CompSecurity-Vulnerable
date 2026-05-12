package com.communicationltd.dao;

import com.communicationltd.config.PasswordPolicyConfig;
import com.communicationltd.security.PasswordHasher;
import com.communicationltd.util.DatabaseConnection;

import java.sql.*;

public class PasswordHistoryDao {

    public static boolean wasUsedRecently(String email, String newPassword) {
        String sql = """
                SELECT old_password_hash, salt
                FROM password_history
                WHERE user_email = ?
                ORDER BY id DESC
                LIMIT ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt(2, PasswordPolicyConfig.getPasswordHistoryLimit());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String oldHash = rs.getString("old_password_hash");
                String oldSalt = rs.getString("salt");

                String newHashWithOldSalt = PasswordHasher.hash(newPassword, oldSalt);

                if (oldHash.equals(newHashWithOldSalt)) {
                    return true;
                }
            }

            return false;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveOldPassword(String email, String oldHash, String oldSalt) {
        String sql = "INSERT INTO password_history(user_email, old_password_hash, salt) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, oldHash);
            ps.setString(3, oldSalt);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
