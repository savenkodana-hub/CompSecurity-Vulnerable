package com.communicationltd.dao;

import com.communicationltd.model.User;
import com.communicationltd.util.DatabaseConnection;
import java.sql.*;

public class UserDao {

    public static boolean registerUser(String username, String email, String hashedPassword, String salt) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // INTENTIONALLY VULNERABLE FOR COURSEWORK DEMO.
            // Part A section 1 / Part B demo: Register SQL Injection.
            // User input is concatenated directly into SQL instead of using PreparedStatement parameters.
            try (Statement stmt = conn.createStatement()) {
                String checkSql = "SELECT id FROM users WHERE username = '" + username + "' OR email = '" + email + "'";
                ResultSet rs = stmt.executeQuery(checkSql);
                if (rs.next()) {
                    return false;
                }

                String insertSql = "INSERT INTO users(username, email, password, salt) VALUES ('"
                        + username + "', '"
                        + email + "', '"
                        + hashedPassword + "', '"
                        + salt + "')";

                stmt.executeUpdate(insertSql);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User findUserByVulnerableLogin(String usernameOrEmail, String password) {
        // INTENTIONALLY VULNERABLE FOR COURSEWORK DEMO.
        // Part A section 3 / Part B demo: Login SQL Injection.
        // Username/email and password input are concatenated directly into the SQL query.
        String sql = "SELECT * FROM users WHERE (username = '" + usernameOrEmail
                + "' OR email = '" + usernameOrEmail
                + "') AND ('" + password + "' = '" + password + "')";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.next()) {
                return null;
            }

            return mapUser(rs);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static User findUserByUsernameOrEmail(String usernameOrEmail) {
        String sql = "SELECT * FROM users WHERE username = ? OR email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usernameOrEmail);
            ps.setString(2, usernameOrEmail);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }

            return mapUser(rs);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password"));
        user.setSalt(rs.getString("salt"));
        user.setFailedLoginAttempts(rs.getInt("failed_login_attempts"));
        user.setLocked(rs.getInt("locked") == 1);

        return user;
    }

    public static boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void resetLoginAttempts(String email) {
        String sql = "UPDATE users SET failed_login_attempts = 0, locked = 0 WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void recordFailedLogin(String email, int maxAttempts) {
        String sql = """
                UPDATE users
                SET failed_login_attempts = failed_login_attempts + 1,
                    locked = CASE WHEN failed_login_attempts + 1 >= ? THEN 1 ELSE locked END
                WHERE email = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maxAttempts);
            ps.setString(2, email);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
