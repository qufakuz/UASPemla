package kosapp;

public class Ulasan {
    private int kosanId;
    private String namaClient;
    private int rating;
    private String komentar; // ganti nama field

    public Ulasan(int kosanId, String namaClient, int rating, String komentar) {
        this.kosanId = kosanId;
        this.namaClient = namaClient;
        this.rating = rating;
        this.komentar = komentar;
    }

    public int getKosanId() { return kosanId; }
    public String getNamaClient() { return namaClient; }
    public int getRating() { return rating; }
    public String getKomentar() { return komentar; } // ubah juga getter-nya
}
