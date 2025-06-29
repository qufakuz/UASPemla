package kosapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UlasanDAO {
    private Connection conn;

    public UlasanDAO() {
        conn = Database.connect();
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS ulasan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "kosan_id INTEGER," +
                "nama_client TEXT," +
                "rating INTEGER," +
                "ulasan TEXT" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertUlasan(Ulasan ulasan) {
        String sql = "INSERT INTO ulasan(kosan_id, nama_client, rating, komentar) VALUES (?, ?, ?, ?)"; // ganti ulasan → komentar
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ulasan.getKosanId());
            pstmt.setString(2, ulasan.getNamaClient());
            pstmt.setInt(3, ulasan.getRating());
            pstmt.setString(4, ulasan.getKomentar()); // sesuaikan dengan getter
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ulasan> getUlasanByKosan(int kosanId) {
        List<Ulasan> list = new ArrayList<>();
        String sql = "SELECT * FROM ulasan WHERE kosan_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, kosanId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Ulasan(
                        rs.getInt("kosan_id"),
                        rs.getString("nama_client"),
                        rs.getInt("rating"),
                        rs.getString("komentar") // ganti ulasan → komentar
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateUlasan(Ulasan old, Ulasan updated) {
        String sql = "UPDATE ulasan SET rating = ?, komentar = ? WHERE kosan_id = ? AND nama_client = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, updated.getRating());
            pstmt.setString(2, updated.getKomentar());
            pstmt.setInt(3, old.getKosanId());
            pstmt.setString(4, old.getNamaClient());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUlasan(Ulasan ulasan) {
        String sql = "DELETE FROM ulasan WHERE kosan_id = ? AND nama_client = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ulasan.getKosanId());
            pstmt.setString(2, ulasan.getNamaClient());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
