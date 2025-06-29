package kosapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KosanDAO {
    private Connection conn;

    public KosanDAO() {
        conn = Database.connect();
    }

    public List<Kosan> getAllKosan() {
        List<Kosan> list = new ArrayList<>();
        String sql = "SELECT * FROM kosan";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Kosan k = new Kosan(
                        rs.getInt("id"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getInt("harga"),
                        rs.getString("imagePath"),
                        rs.getString("deskripsi")
                );
                list.add(k);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertKosan(Kosan kosan) {
        String sql = "INSERT INTO kosan(nama, alamat, harga, imagePath, deskripsi) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kosan.getNama());
            pstmt.setString(2, kosan.getAlamat());
            pstmt.setInt(3, kosan.getHarga());
            pstmt.setString(4, kosan.getImagePath());
            pstmt.setString(5, kosan.getDeskripsi());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    public void updateKosan(Kosan kosan) {
        String sql = "UPDATE kosan SET nama = ?, alamat = ?, harga = ?, imagePath = ?, deskripsi = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, kosan.getNama());
            pstmt.setString(2, kosan.getAlamat());
            pstmt.setInt(3, kosan.getHarga());
            pstmt.setString(4, kosan.getImagePath());
            pstmt.setString(5, kosan.getDeskripsi());
            pstmt.setInt(6, kosan.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    public void deleteKosan(int id) {
        String sql = "DELETE FROM kosan WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }
}
