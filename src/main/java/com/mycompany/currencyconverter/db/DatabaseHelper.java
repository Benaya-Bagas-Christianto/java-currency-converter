package com.mycompany.currencyconverter.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:currency_history.db";

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS conversion_history (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " from_currency TEXT NOT NULL,\n"
                + " to_currency TEXT NOT NULL,\n"
                + " amount REAL NOT NULL,\n"
                + " result REAL NOT NULL,\n"
                + " conversion_date DATETIME DEFAULT CURRENT_TIMESTAMP\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            System.out.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void saveHistory(String from, String to, double amount, double result) {
        String sql = "INSERT INTO conversion_history(from_currency, to_currency, amount, result) VALUES(?,?,?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, from);
            pstmt.setString(2, to);
            pstmt.setDouble(3, amount);
            pstmt.setDouble(4, result);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }

    public static List<String[]> getHistory() {
        List<String[]> historyList = new ArrayList<>();
        String sql = "SELECT from_currency, to_currency, amount, result, conversion_date FROM conversion_history ORDER BY id DESC LIMIT 50";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String[] row = new String[5];
                row[0] = rs.getString("from_currency");
                row[1] = rs.getString("to_currency");
                row[2] = String.valueOf(rs.getDouble("amount"));
                row[3] = String.valueOf(rs.getDouble("result"));
                row[4] = rs.getString("conversion_date");
                historyList.add(row);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving history: " + e.getMessage());
        }
        return historyList;
    }

    public static void clearHistory() {
        String sql = "DELETE FROM conversion_history";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            System.out.println("Error clearing history: " + e.getMessage());
        }
    }
}
