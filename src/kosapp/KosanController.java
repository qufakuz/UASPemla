package kosapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

public class KosanController {
    @FXML private TextField tfNama;
    @FXML private TextField tfAlamat;
    @FXML private TextField tfHarga;
    @FXML private TextField tfImagePath;
    @FXML private Button btnBrowseImage;
    @FXML private ImageView imageViewPreview;

    @FXML private TableView<Kosan> tableKosan;
    @FXML private TableColumn<Kosan, Number> colId;
    @FXML private TableColumn<Kosan, String> colNama;
    @FXML private TableColumn<Kosan, String> colAlamat;
    @FXML private TableColumn<Kosan, Number> colHarga;
    @FXML private TableColumn<Kosan, String> colGambar;
    @FXML private TextArea tfDeskripsi;
    @FXML private TableColumn<Kosan, String> colDeskripsi;

    private KosanDAO dao;
    private ObservableList<Kosan> data;

    @FXML
    public void initialize() {
        dao = new KosanDAO();

        // Set cell value factories
        colId.setCellValueFactory(cell -> cell.getValue().idProperty());
        colNama.setCellValueFactory(cell -> cell.getValue().namaProperty());
        colAlamat.setCellValueFactory(cell -> cell.getValue().alamatProperty());
        colHarga.setCellValueFactory(cell -> cell.getValue().hargaProperty());

        colGambar.setCellValueFactory(cell -> cell.getValue().imagePathProperty());
        colDeskripsi.setCellValueFactory(cell -> cell.getValue().deskripsiProperty());

        // Custom cell to show image thumbnails in the gambar column
        colGambar.setCellFactory(col -> new TableCell<Kosan, String>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(100);
                imageView.setFitHeight(75);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null || path.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image img = new Image("file:" + path, 100, 75, true, true);
                        imageView.setImage(img);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        loadData();
    }

    private void loadData() {
        data = FXCollections.observableArrayList(dao.getAllKosan());
        tableKosan.setItems(data);
    }

    @FXML
    private void handleTambah() {
        String nama = tfNama.getText();
        String alamat = tfAlamat.getText();
        String imagePath = tfImagePath.getText();
        int harga;
        String deskripsi = tfDeskripsi.getText();

        try {
            harga = Integer.parseInt(tfHarga.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Harga harus angka");
            return;
        }

        Kosan k = new Kosan(0, nama, alamat, harga, imagePath, deskripsi);
        dao.insertKosan(k);

        clearFields();   // ← Tambahkan ini
        loadData();      // ← Tambahkan ini
    }


    @FXML
    private void handleUpdate() {
        Kosan selected = tableKosan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Pilih data yang ingin diupdate");
            return;
        }

        String nama = tfNama.getText();
        String alamat = tfAlamat.getText();
        String imagePath = tfImagePath.getText();
        int harga;
        String deskripsi = tfDeskripsi.getText();


        try {
            harga = Integer.parseInt(tfHarga.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Harga harus angka");
            return;
        }

        selected.setNama(nama);
        selected.setAlamat(alamat);
        selected.setHarga(harga);
        selected.setImagePath(imagePath);
        selected.setDeskripsi(deskripsi);
        dao.updateKosan(selected);
        clearFields();
        loadData();
    }

    @FXML
    private void handleHapus() {
        Kosan selected = tableKosan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Pilih data yang ingin dihapus");
            return;
        }
        dao.deleteKosan(selected.getId());
        clearFields();
        loadData();
    }

    @FXML
    private void handleSelect() {
        Kosan selected = tableKosan.getSelectionModel().getSelectedItem();
        if (selected != null) {
            tfNama.setText(selected.getNama());
            tfAlamat.setText(selected.getAlamat());
            tfHarga.setText(String.valueOf(selected.getHarga()));
            tfImagePath.setText(selected.getImagePath());
            tfDeskripsi.setText(selected.getDeskripsi());


            if (selected.getImagePath() != null && !selected.getImagePath().isEmpty()) {
                try {
                    Image img = new Image("file:" + selected.getImagePath());
                    imageViewPreview.setImage(img);
                } catch (Exception e) {
                    imageViewPreview.setImage(null);
                }
            } else {
                imageViewPreview.setImage(null);
            }
        }
    }

    private void clearFields() {
        tfNama.clear();
        tfAlamat.clear();
        tfHarga.clear();
        tfImagePath.clear();
        imageViewPreview.setImage(null);
        tfDeskripsi.clear();

    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            tfImagePath.setText(file.getAbsolutePath());

            try {
                Image img = new Image("file:" + file.getAbsolutePath());
                imageViewPreview.setImage(img);
            } catch (Exception e) {
                imageViewPreview.setImage(null);
            }
        }
    }
}
