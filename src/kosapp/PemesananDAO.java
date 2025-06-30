package kosapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PemesananDAO {
    private Connection conn = Database.connect();

    public void tambahPemesanan(Pemesanan p) {
        String sql = "INSERT INTO pemesanan(kosan_id, nama_kosan, nama_client, tanggal_pesan) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, p.getKosanId());
            pstmt.setString(2, p.getNamaKosan());
            pstmt.setString(3, p.getNamaClient());
            pstmt.setString(4, p.getTanggalPesan());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<Pemesanan> getRiwayatByClient(String namaClient) {
        List<Pemesanan> list = new ArrayList<>();
        String sql = "SELECT * FROM pemesanan WHERE nama_client = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, namaClient);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Pemesanan(
                                rs.getInt("kosan_id"),
                                rs.getString("nama_kosan"),       // ← ambil dari database juga
                                rs.getString("nama_client"),
                                rs.getString("tanggal_pesan")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
