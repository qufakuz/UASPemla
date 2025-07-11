package kosapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:kosapp.db";
    private static Connection conn = null;

    public static Connection connect() {
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(URL);
                createTableIfNotExists(); // Buat semua tabel jika belum ada
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    private static void createTableIfNotExists() {
        String sqlKosan = "CREATE TABLE IF NOT EXISTS kosan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama TEXT NOT NULL," +
                "alamat TEXT NOT NULL," +
                "harga INTEGER NOT NULL," +
                "imagePath TEXT," +
                "deskripsi TEXT" +
                ");";

        String sqlUlasan = "CREATE TABLE IF NOT EXISTS ulasan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "kosan_id INTEGER NOT NULL," +
                "nama_client TEXT NOT NULL," +
                "rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5)," +
                "komentar TEXT," +
                "FOREIGN KEY(kosan_id) REFERENCES kosan(id)" +
                ");";

        String sqlPemesanan = "CREATE TABLE IF NOT EXISTS pemesanan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "kosan_id INTEGER NOT NULL," +
                "nama_kosan TEXT NOT NULL," +  
                "nama_client TEXT NOT NULL," +
                "tanggal_pesan TEXT NOT NULL" +
                ");";
        String sqlClientUser = "CREATE TABLE IF NOT EXISTS client_user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nama TEXT NOT NULL," +
                "email TEXT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL" +
                ");";
        



        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sqlKosan);
            stmt.execute(sqlUlasan);
            stmt.execute(sqlPemesanan); // Tambahkan riwayat pemesanan
            stmt.execute(sqlClientUser);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
