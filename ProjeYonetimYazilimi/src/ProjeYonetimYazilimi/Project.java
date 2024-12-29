package ProjeYonetimYazilimi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Project {

    private String projectname;
    private String startTime;
    private String finishTime;
    private String gmail;

    // Proje nesnesi oluşturur
    public Project(String projectname, String startTime, String finishTime, String gmail) {
        this.projectname = projectname;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.gmail = gmail;
    }

    // Yeni bir projeyi veritabanına ekler
    public String projectAdd() {
        try {
            PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
                "INSERT INTO projects (projectname, startTime, finishTime, gmail) VALUES(?,?,?,?)"
            );
            psmt.setString(1, this.projectname);
            psmt.setString(2, this.startTime);
            psmt.setString(3, this.finishTime);
            psmt.setString(4, this.gmail);
            psmt.execute();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Hata!";
        }
        return "Proje Eklendi";
    }

    // Belirli bir kullanıcıya ait projeleri listeler
    public static ArrayList<String> projects(String gmail) throws Exception {
        ArrayList<String> projectList = new ArrayList<>();
        PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement("SELECT * FROM projects WHERE gmail=?");
        psmt.setString(1, gmail);
        ResultSet rs = psmt.executeQuery();
        while (rs.next()) {
            projectList.add(
                rs.getString("projectname") + " " + rs.getString("startTime") + " " + rs.getString("finishTime")
            );
        }
        psmt.close();
        return projectList;
    }

    // Projelerin bitiş tarihini kontrol eder, gerekiyorsa 1 yıl uzatır
    public static ArrayList<String> projeZamanKont(String gmail) throws Exception {
        ArrayList<String> List = new ArrayList<>();
        PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
            "SELECT p.idprojects,p.finishTime,p.projectname,p.startTime " +
            "FROM projects p JOIN users s ON p.gmail = s.gmail " +
            "WHERE p.gmail=? && STR_TO_DATE(finishTime, '%d.%m.%Y') < STR_TO_DATE(?, '%d.%m.%Y')"
        );
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String formattedDateTime = now.format(formatter);
        psmt.setString(1, gmail);
        psmt.setString(2, formattedDateTime);
        ResultSet rs = psmt.executeQuery();
        System.out.println(formattedDateTime); // Mevcut tarihi kontrol amaçlı yazdırır

        while (rs.next()) {
            System.out.println(rs.getString("idprojects")); // Proje ID'sini yazdırır

            // İlgili proje için görev durumlarını kontrol eder
            PreparedStatement psmt1 = ProjeYonetimYazilimi.con.prepareStatement(
                "SELECT iddurum FROM missions WHERE idprojects=?"
            );
            psmt1.setString(1, rs.getString("idprojects"));
            ResultSet rs1 = psmt1.executeQuery();
            while (rs1.next()) {
                if (rs1.getInt("iddurum") == 2) {
                    // Görev tamamlanmış, işlem yapmadan devam
                } else {
                    // Bitiş tarihini 1 yıl uzatır
                    PreparedStatement psmt2 = ProjeYonetimYazilimi.con.prepareStatement(
                        "UPDATE projects SET finishTime =? WHERE idprojects =? "
                    );
                    String paramfinish[] = rs.getString("finishTime").split("\\.");
                    String paramform[] = formattedDateTime.split("\\.");
                    int subtraction = Integer.parseInt(paramform[2]) - Integer.parseInt(paramfinish[2]);
                    paramfinish[2] = String.valueOf(Integer.parseInt(paramfinish[2]) + subtraction + 1);
                    String newtime = String.join(".", paramfinish);
                    psmt2.setString(1, newtime);
                    psmt2.setString(2, rs.getString("idprojects"));
                    psmt2.execute();
                    List.add(
                        "Gecikme: " + subtraction + " yıl, Proje: " + rs.getString("projectname") +
                        " - Bitiş süresi 1 yıl uzatıldı >==<"
                    );
                    break;
                }
            }
            psmt1.close();
        }
        psmt.close();
        return List;
    }
}