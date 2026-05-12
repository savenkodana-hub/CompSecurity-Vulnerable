package com.communicationltd.dao;

import com.communicationltd.model.User;
import com.communicationltd.util.DatabaseConnection;
import java.sql.*;

public class UserDao {

    public static boolean registerUser(String username, String email, String hashedPassword, String salt) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            String sql = "INSERT INTO users(username, email, password, salt) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setString(3, hashedPassword);
                ps.setString(4, salt);

                ps.executeUpdate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setPasswordHash(rs.getString("password"));
            user.setSalt(rs.getString("salt"));

            return user;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
}
