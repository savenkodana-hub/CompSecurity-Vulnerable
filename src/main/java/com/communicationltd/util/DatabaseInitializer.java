package com.communicationltd.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void init() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    salt TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS packages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    package_name TEXT NOT NULL,
                    speed TEXT NOT NULL,
                    price REAL NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS sectors (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sector_name TEXT NOT NULL UNIQUE
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_email TEXT,
                    customer_name TEXT NOT NULL,
                    phone TEXT NOT NULL,
                    address TEXT NOT NULL,
                    package_id INTEGER,
                    sector_id INTEGER
                );
            """);

            if (!columnExists(conn, "customers", "sector_id")) {
                stmt.execute("ALTER TABLE customers ADD COLUMN sector_id INTEGER");
            }

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS password_history (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_email TEXT NOT NULL,
                    old_password_hash TEXT NOT NULL,
                    salt TEXT NOT NULL,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS password_reset_tokens (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    email TEXT NOT NULL,
                    token_hash TEXT NOT NULL,
                    used INTEGER DEFAULT 0,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                );
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO packages(id, package_name, speed, price)
                VALUES
                (1, 'Basic', '100MB', 99),
                (2, 'Premium', '500MB', 149),
                (3, 'Ultra', '1GB', 199);
            """);

            stmt.execute("""
                INSERT OR IGNORE INTO sectors(id, sector_name)
                VALUES
                (1, 'Private'),
                (2, 'Business'),
                (3, 'Education'),
                (4, 'Government');
            """);

            System.out.println("Database initialized successfully");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean columnExists(Connection conn, String tableName, String columnName) throws Exception {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ")")) {

            while (rs.next()) {
                if (columnName.equalsIgnoreCase(rs.getString("name"))) {
                    return true;
                }
            }

            return false;
        }
    }
}
