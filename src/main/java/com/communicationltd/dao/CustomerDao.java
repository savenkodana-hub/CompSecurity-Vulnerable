package com.communicationltd.dao;

import com.communicationltd.util.DatabaseConnection;
import java.sql.*;

public class CustomerDao {

    public static boolean hasCustomer(String email) {
        String sql = "SELECT id FROM customers WHERE user_email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ הפונקציה שחסרה לך
    public static String getCustomerFirstName(String email) {
        String sql = "SELECT customer_name FROM customers WHERE user_email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("customer_name");
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void addCustomer(String email, String customerName, String phone, String address, int packageId, int sectorId) {
        String sql = """
                INSERT INTO customers(user_email, customer_name, phone, address, package_id, sector_id)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, customerName);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setInt(5, packageId);
            ps.setInt(6, sectorId);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
