package kosapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Ambil ukuran layar
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Muat file login.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        // Atur posisi dan ukuran stage
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());

        // Tambahkan CSS
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setTitle("Kos5tress App");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRootScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxml));
            Parent root = loader.load();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene newScene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());
            newScene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());

            primaryStage.setScene(newScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
