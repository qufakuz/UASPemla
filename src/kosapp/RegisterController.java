package kosapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;


public class RegisterController {

    @FXML private TextField namaField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    @FXML
    private void handleRegister() {
        String nama = namaField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (nama.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Semua field harus diisi.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kosapp.db")) {
            String sql = "INSERT INTO client_user (username, password, nama, email) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, nama);
            stmt.setString(4, email);

            stmt.executeUpdate();
            statusLabel.setText("Registrasi berhasil!");
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Username sudah digunakan.");
        }
    }
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
