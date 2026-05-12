package com.communicationltd.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:sqlite:communication_ltd.db";

    public static Connection getConnection() throws Exception {
        Connection conn = DriverManager.getConnection(DB_URL);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA busy_timeout = 10000");
            stmt.execute("PRAGMA journal_mode = WAL");
        }

        return conn;
    }
}