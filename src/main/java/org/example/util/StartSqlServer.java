package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class StartSqlServer {

    public StartSqlServer() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (os.contains("win")) {
                processBuilder = new ProcessBuilder("C:\\xampp\\mysql\\bin\\mysqld.exe");
            } else if (os.contains("mac")) {
                processBuilder = new ProcessBuilder("sudo", "/Applications/XAMPP/xamppfiles/bin/mysql.server", "start");
            } else {
                System.out.println("Nepodržan operativni sistem.");
                return;
            }

            processBuilder.inheritIO();
            Process process = processBuilder.start();

            Thread.sleep(5000);

            if (testConnection()) {
                System.out.println("MySQL server je uspešno pokrenut i dostupan.");
            } else {
                System.err.println("MySQL server nije dostupan. Proveri konfiguraciju.");
            }

        } catch (Exception e) {
            System.err.println("Greška prilikom pokretanja MySQL servera: " + e.getMessage());
        }
    }

    private boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/GYM", "root", "")) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
