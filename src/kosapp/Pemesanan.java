package kosapp;

public class Pemesanan {
    private int kosanId;
    private String namaKosan;
    private String namaClient;
    private String tanggalPesan;

    public Pemesanan(int kosanId, String namaKosan, String namaClient, String tanggalPesan) {
        this.kosanId = kosanId;
        this.namaKosan = namaKosan;
        this.namaClient = namaClient;
        this.tanggalPesan = tanggalPesan;
    }

    public int getKosanId() { return kosanId; }
    public String getNamaKosan() { return namaKosan; }
    public String getNamaClient() { return namaClient; }
    public String getTanggalPesan() { return tanggalPesan; }
}

