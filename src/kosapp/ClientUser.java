package kosapp;

public class ClientUser {
    private int id;
    private String username;
    private String nama;
    private String email;

    public ClientUser(int id, String username, String nama, String email) {
        this.id = id;
        this.username = username;
        this.nama = nama;
        this.email = email;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getNama() { return nama; }
    public String getEmail() { return email; }
}
