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
        // INTENTIONALLY VULNERABLE FOR COURSEWORK DEMO.
        // Part A section 4 / Part B demo: Add Customer SQL Injection and Stored XSS storage.
        // Customer input is stored without sanitization and concatenated directly into SQL.
        String sql = "INSERT INTO customers(user_email, customer_name, phone, address, package_id, sector_id) VALUES ('"
                + email + "', '"
                + customerName + "', '"
                + phone + "', '"
                + address + "', "
                + packageId + ", "
                + sectorId + ")";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
