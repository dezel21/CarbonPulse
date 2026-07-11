package carbonpulse.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Sesuaikan dengan konfigurasi MySQL/XAMPP laptop masing-masing
    private static final String URL = "jdbc:mysql://localhost:3307/db_carbonpulse";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Kosongkan jika pakai XAMPP default

    public static Connection getConnection() throws SQLException {
        try {
            // Memaksa Java memuat Driver MySQL Connector ke memori
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("[ERROR] Driver JDBC MySQL tidak ditemukan!");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}