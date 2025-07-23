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
                // Za Windows
                processBuilder = new ProcessBuilder("C:\\xampp\\mysql\\bin\\mysqld.exe");
            } else if (os.contains("mac")) {
                // Za macOS (start komanda)
                processBuilder = new ProcessBuilder("sudo", "/Applications/XAMPP/xamppfiles/bin/mysql.server", "start");
            } else {
                System.out.println("Nepodržan operativni sistem.");
                return;
            }

            processBuilder.inheritIO(); // Prikazuje izlaz i greške u terminalu
            Process process = processBuilder.start();

            // Sačekaj nekoliko sekundi da se server podigne
            Thread.sleep(5000);

            // Proveri da li se može uspostaviti konekcija
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
