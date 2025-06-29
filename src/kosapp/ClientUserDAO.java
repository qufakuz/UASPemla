package kosapp;

import java.sql.*;

public class ClientUserDAO {
    public static ClientUser login(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kosapp.db")) {
            String sql = "SELECT * FROM client_user WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ClientUser(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("nama"),
                        rs.getString("email")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
