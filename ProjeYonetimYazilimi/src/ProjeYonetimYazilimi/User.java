package ProjeYonetimYazilimi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class User {

    private String gmail;
    private String password;
    private String name;
    private String surname;

    // Kullanıcı nesnesini oluşturur
    public User(String gmail, String password, String name, String surname) {
        this.gmail = gmail;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    // Kullanıcıyı veritabanına ekler
    public String AddUser() throws Exception {
        try {
            PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement("INSERT INTO users VALUES(?,?,?,?)");
            psmt.setString(1, this.gmail);
            psmt.setString(2, this.password);
            psmt.setString(3, this.name);
            psmt.setString(4, this.surname);
            psmt.execute();
            psmt.close();
        } catch (Exception e) {
            return "Bu gmail kullanılıyor!";
        }
        return "İşlem Başarılı";
    }

    // Kullanıcı doğrulaması yapar
    public static String Authentication(String gmail, String password) {
        try {
            PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement("SELECT * FROM users WHERE gmail= ?");
            psmt.setString(1, gmail);
            ResultSet rs = psmt.executeQuery();
            rs.next();

            if (password.equals(rs.getString("password"))) {
                return "+"; // Doğru giriş
            }
        } catch (Exception e) {
            return "Giriş Yapılamadı";
        }
        return " "; // Yanlış giriş
    }
}