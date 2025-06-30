package kosapp;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;

public class ClientController {
    @FXML private VBox containerKosan;
    @FXML private Label labelNama;
    @FXML private Label labelEmail;
    @FXML private TextField tfCari;
    @FXML private TextField tfHargaMin;
    @FXML private TextField tfHargaMax;

    private KosanDAO dao;
    private ClientUser currentClient;

    @FXML
    public void initialize() {
        dao = new KosanDAO();
        // loadCards(); ← Ini HARUS DIHAPUS
    }


    private void loadCards() {
        containerKosan.getChildren().clear();

        for (Kosan kos : dao.getAllKosan()) {
            VBox card = buatCardKos(kos);
            containerKosan.getChildren().add(card);
        }
    }

    private void handleBeli(Kosan selected) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Pembelian");
        confirm.setHeaderText(null);
        confirm.setContentText("Yakin ingin membeli kosan: " + selected.getNama() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                new PemesananDAO().tambahPemesanan(new Pemesanan(
                        selected.getId(),
                        selected.getNama(),
                        currentClient.getNama(),
                        java.time.LocalDate.now().toString()
                ));

                dao.deleteKosan(selected.getId());
                loadCards();
                showAlert("Sukses", "Kosan berhasil dibeli dan dicatat di riwayat.");
            }
        });
    }



    public void setClientUser(ClientUser client) {
        this.currentClient = client;
        labelNama.setText(client.getNama());
        labelEmail.setText(client.getEmail());
        loadCards();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleFilter() {
        String keyword = tfCari.getText().toLowerCase();
        int minHarga = 0;
        int maxHarga = Integer.MAX_VALUE;

        try {
            if (!tfHargaMin.getText().isEmpty()) {
                minHarga = Integer.parseInt(tfHargaMin.getText());
            }
            if (!tfHargaMax.getText().isEmpty()) {
                maxHarga = Integer.parseInt(tfHargaMax.getText());
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Harga harus berupa angka.");
            return;
        }

        containerKosan.getChildren().clear();

        for (Kosan kos : dao.getAllKosan()) {
            boolean cocokKeyword = kos.getNama().toLowerCase().contains(keyword)
                    || kos.getAlamat().toLowerCase().contains(keyword);
            boolean cocokHarga = kos.getHarga() >= minHarga && kos.getHarga() <= maxHarga;

            if (cocokKeyword && cocokHarga) {
                VBox card = buatCardKos(kos);
                containerKosan.getChildren().add(card);
            }
        }
    }

    private VBox buatCardKos(Kosan kos) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-color: #cccccc; " +
                "-fx-border-radius: 10; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 2, 2);");
        card.setPrefWidth(600);
        VBox.setMargin(card, new Insets(10, 0, 10, 0));

        ImageView imgView = new ImageView();
        imgView.setFitWidth(200);
        imgView.setFitHeight(150);
        imgView.setPreserveRatio(true);
        try {
            Image img = new Image("file:" + kos.getImagePath(), 200, 150, true, true);
            imgView.setImage(img);
        } catch (Exception e) {
            imgView.setImage(new Image(getClass().getResource("/kosapp/assets/no-image.png").toExternalForm()));
        }

        Label lblNama = new Label("Nama: " + kos.getNama());
        lblNama.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Label lblAlamat = new Label("Alamat: " + kos.getAlamat());
        lblAlamat.setStyle("-fx-font-size: 13px;");

        Label lblHarga = new Label("Harga: Rp " + kos.getHarga());
        lblHarga.setStyle("-fx-font-size: 13px;");

        Label lblDeskripsi = new Label("Deskripsi: " + kos.getDeskripsi());
        lblDeskripsi.setWrapText(true);
        lblDeskripsi.setMaxWidth(550);
        lblDeskripsi.setStyle("-fx-font-size: 12px; -fx-text-fill: #555;");

        Button btnBeli = new Button("Pesan Kosan");
        btnBeli.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 6 12; -fx-background-radius: 8;");
        btnBeli.setOnAction(e -> handleBeli(kos));

        Button btnUlasi = new Button("Beri Ulasan");
        btnUlasi.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-padding: 6 12; -fx-background-radius: 8;");
        btnUlasi.setOnAction(e -> showUlasanForm(kos));

        HBox buttonBox = new HBox(10, btnBeli, btnUlasi);
        card.getChildren().addAll(imgView, lblNama, lblAlamat, lblHarga, lblDeskripsi, buttonBox);

        // Tambahkan daftar ulasan dari database
        UlasanDAO ulasanDAO = new UlasanDAO();
        for (Ulasan ul : ulasanDAO.getUlasanByKosan(kos.getId())) {
            String bintang = "★".repeat(ul.getRating()) + "☆".repeat(5 - ul.getRating());
            Label ulasanLabel = new Label(bintang + " - " + ul.getNamaClient() + ": " + ul.getKomentar());
            ulasanLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #444;");

            VBox ulasanBox = new VBox(ulasanLabel);

            // Jika ulasan milik client yang login, beri tombol edit dan hapus
            if (ul.getNamaClient().equals(currentClient.getNama())) {
                Button btnEdit = new Button("Edit");
                Button btnHapus = new Button("Hapus");

                btnEdit.setOnAction(e -> showEditUlasanDialog(ul));
                btnHapus.setOnAction(e -> {
                    ulasanDAO.deleteUlasan(ul);
                    showAlert("Ulasan dihapus", "Ulasan berhasil dihapus.");
                    loadCards();
                });

                HBox actionButtons = new HBox(5, btnEdit, btnHapus);
                ulasanBox.getChildren().add(actionButtons);
            }

            card.getChildren().add(ulasanBox);
        }


        return card;
    }

    private void showUlasanForm(Kosan kos) {
        Dialog<Ulasan> dialog = new Dialog<>();
        dialog.setTitle("Berikan Ulasan");
        dialog.setHeaderText("Ulasan untuk kosan: " + kos.getNama());

        Label lblRating = new Label("Rating (1-5):");
        Spinner<Integer> spinnerRating = new Spinner<>(1, 5, 5);
        TextArea tfUlasan = new TextArea();
        tfUlasan.setPromptText("Tulis ulasan di sini...");
        tfUlasan.setWrapText(true);

        VBox content = new VBox(10, lblRating, spinnerRating, tfUlasan);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        ButtonType submitButtonType = new ButtonType("Kirim", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == submitButtonType) {
                return new Ulasan(kos.getId(), currentClient.getNama(), spinnerRating.getValue(), tfUlasan.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(ulasan -> {
            UlasanDAO ulasanDAO = new UlasanDAO();
            ulasanDAO.insertUlasan(ulasan);
            showAlert("Terima kasih!", "Ulasan Anda telah dikirim.");
            loadCards();
        });
    }
    private void showEditUlasanDialog(Ulasan ulasan) {
        Dialog<Ulasan> dialog = new Dialog<>();
        dialog.setTitle("Edit Ulasan");
        dialog.setHeaderText("Edit ulasan Anda untuk kosan ID: " + ulasan.getKosanId());

        Label lblRating = new Label("Rating (1-5):");
        Spinner<Integer> spinnerRating = new Spinner<>(1, 5, ulasan.getRating());
        TextArea tfUlasan = new TextArea(ulasan.getKomentar());
        tfUlasan.setWrapText(true);

        VBox content = new VBox(10, lblRating, spinnerRating, tfUlasan);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        ButtonType submitButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == submitButtonType) {
                return new Ulasan(ulasan.getKosanId(), ulasan.getNamaClient(), spinnerRating.getValue(), tfUlasan.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updated -> {
            new UlasanDAO().updateUlasan(ulasan, updated);
            showAlert("Berhasil", "Ulasan berhasil diperbarui.");
            loadCards();
        });
    }

    @FXML
    private void handleLihatRiwayat() {
        List<Pemesanan> riwayat = new PemesananDAO().getRiwayatByClient(currentClient.getNama());

        StringBuilder msg = new StringBuilder("Riwayat Pemesanan Anda:\n\n");
        for (Pemesanan p : riwayat) {
            msg.append("Nama Kos: ").append(p.getNamaKosan())
                    .append("\nTanggal: ").append(p.getTanggalPesan())
                    .append("\n\n");

        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Riwayat Pemesanan");
        alert.setHeaderText(null);
        alert.setContentText(msg.toString());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }



}
