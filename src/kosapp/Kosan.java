package kosapp;

import javafx.beans.property.*;

public class Kosan {
        private IntegerProperty id;
        private StringProperty nama;
        private StringProperty alamat;
        private IntegerProperty harga;
        private StringProperty imagePath;  // path file gambar
        private final StringProperty deskripsi;



    // getter setter dan constructor



    public Kosan() {
        this.id = new SimpleIntegerProperty();
        this.nama = new SimpleStringProperty();
        this.alamat = new SimpleStringProperty();
        this.harga = new SimpleIntegerProperty();
        this.imagePath = new SimpleStringProperty();
        this.deskripsi = new SimpleStringProperty();
    }

    public Kosan(int id, String nama, String alamat, int harga, String imagePath, String deskripsi) {
        this.id = new SimpleIntegerProperty(id);
        this.nama = new SimpleStringProperty(nama);
        this.alamat = new SimpleStringProperty(alamat);
        this.harga = new SimpleIntegerProperty(harga);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.deskripsi = new SimpleStringProperty(deskripsi);
    }

    // Getter & Setter
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNama() { return nama.get(); }
    public void setNama(String nama) { this.nama.set(nama); }
    public StringProperty namaProperty() { return nama; }

    public String getAlamat() { return alamat.get(); }
    public void setAlamat(String alamat) { this.alamat.set(alamat); }
    public StringProperty alamatProperty() { return alamat; }

    public int getHarga() { return harga.get(); }
    public void setHarga(int harga) { this.harga.set(harga); }
    public IntegerProperty hargaProperty() { return harga; }

    // getter dan setter imagePath
    public String getImagePath() {
        return imagePath.get();
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    public String getDeskripsi() { return deskripsi.get(); }
    public void setDeskripsi(String deskripsi) { this.deskripsi.set(deskripsi); }
    public StringProperty deskripsiProperty() { return deskripsi; }
}
