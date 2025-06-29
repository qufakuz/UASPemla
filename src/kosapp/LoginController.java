package kosapp;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            if (username.equals("admin") && password.equals("admin123")) {
                Main.setRootScene("kosan.fxml");
            } else {
                ClientUser client = ClientUserDAO.login(username, password);
                if (client != null) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
                    Parent root = loader.load();

                    ClientController controller = loader.getController();
                    controller.setClientUser(client);

                    // Set fullscreen scene
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
                    scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

                    Main.primaryStage.setScene(scene);
                } else {
                    errorLabel.setText("Username atau password salah!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Terjadi kesalahan saat login.");
        }
    }
    @FXML
    private void handleGoToRegister() {
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
