package ProjeYonetimYazilimi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class Worker {

    private final String fullName;
    private final String workergmail;
    private final String position;

    // Constructor: Çalışan nesnesini oluşturur ve pozisyon bilgisini ekrana yazdırır.
    public Worker(String fullName, String workergmail, String position) {
        this.fullName = fullName;
        this.workergmail = workergmail;
        this.position = position;
        System.out.println("pozisyon:" + position);
    }

    // workerAdd: Çalışanı veritabanına ekler. Pozisyon bilgisine göre farklı id değerleri atanır.
    public String workerAdd(String ugmail) {
        try {
            try (PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
                    "INSERT INTO workers (gmail, workerName, workergmail, idpozisyon) VALUES (?, ?, ?, ?)")) {
                psmt.setString(1, ugmail);
                psmt.setString(2, this.fullName);
                psmt.setString(3, this.workergmail);
                switch (this.position) {
                    case "Freshman" -> psmt.setInt(4, 1);
                    case "Sophomore" -> psmt.setInt(4, 2);
                    case "Junior" -> psmt.setInt(4, 3);
                    case "Senior" -> psmt.setInt(4, 4);
                    default -> psmt.setInt(4, 0);
                }
                psmt.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Çalışan Eklenemedi!";
        }
        return "Çalışan Başarıyla Eklendi";
    }

    // workers: Belirtilen kullanıcı mailine ait çalışanların listesini döndürür.
    public static ArrayList workers(String ugmail) throws Exception {
        ArrayList<String> workers = new ArrayList<>();
        PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
                "SELECT * FROM users s JOIN workers w ON s.gmail=w.gmail WHERE s.gmail=?");
        psmt.setString(1, ugmail);
        ResultSet rs = psmt.executeQuery();
        while (rs.next()) {
            workers.add(rs.getString("WorkerName") + " " + rs.getString("workergmail"));
        }
        return workers;
    }

    // workerinfo: Belirtilen çalışan hakkında detaylı bilgi döndürür.
    public static ArrayList<String> workerinfo(String name, String gmail) throws Exception {
        int wid = Mission.getIdforUser(name, "wid", gmail);
        System.out.println(wid);
        ArrayList<String> workerinf = new ArrayList<>();
        try (PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
                "SELECT * FROM pozisyon p JOIN workers w ON p.idpozisyon = w.idpozisyon "
                        + "JOIN missions m ON m.idWorkers = w.idWorkers "
                        + "JOIN projects pr ON pr.idprojects = m.idprojects "
                        + "JOIN durum d ON m.iddurum=d.iddurum WHERE w.idWorkers =? AND w.gmail=?")) {
            psmt.setInt(1, wid);
            psmt.setString(2, gmail);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                workerinf.add(rs.getString("projectname") + " " + rs.getString("pozisyoncol") + " " 
                            + rs.getString("mission") + " " + rs.getString("missionstart") + " " 
                            + rs.getString("missionfinish") + " " + rs.getString("durumcol") + ">--<");
            }
        }
        return workerinf;
    }

    // DeleteWorker: Belirtilen çalışanın veritabanından silinmesini sağlar.
    public static String DeleteWorker(String name, String gmail) {
        try {
            int wid = Mission.getIdforUser(name, "wid", gmail);
            try (PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
                    "DELETE FROM workers WHERE idWorkers=? AND gmail=?")) {
                psmt.setInt(1, wid);
                psmt.setString(2, gmail);
                psmt.execute();
            }

            try (PreparedStatement psmt1 = ProjeYonetimYazilimi.con.prepareStatement(
                    "UPDATE missions SET idWorkers = ? WHERE idWorkers = ?")) {
                psmt1.setNull(1, java.sql.Types.VARCHAR);
                psmt1.setInt(2, wid);
                psmt1.execute();
            }
        } catch (Exception e) {
            return "Hata";
        }
        return "Başarıyla Silindi";
    }

    // AutoHenction: Belirtilen bilgilerin doğruluğunu kontrol ederek giriş işlemi yapar.
    public static String AutoHenction(String name, String workergmail) {
        try {
            PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
                    "SELECT * FROM workers WHERE workergmail=?");
            psmt.setString(1, workergmail);
            ResultSet rs = psmt.executeQuery();
            rs.next();
            if (rs.getString("WorkerName").equals(name)) {
                return "Logged+";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Giriş Yapılamadı";
        }
        return "Giriş Yapıldı";
    }

    // UpdateWorkerCondition: Çalışanın mail adresini günceller.
    public static String UpdateWorkerCondition(String workername, String workergmail, String ugmail) {
        try {
            PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
                    "UPDATE workers SET workergmail=? WHERE idWorkers=?");
            psmt.setString(1, workergmail);
            psmt.setInt(2, Mission.getIdforUser(workername, "wid", ugmail));
            psmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Gmail Güncellendi";
    }

    // WorkerMissionInf: Çalışanın tamamlanan ve tamamlanmayan görev sayılarını döndürür.
    public static String WorkerMissionInf(String name, String ugmail) {
        int tamamlandi = 0, tamamlanmadi = 0;
        try {
            PreparedStatement psmt = ProjeYonetimYazilimi.con.prepareStatement(
                    "SELECT * FROM workers w JOIN missions m ON w.idWorkers=m.idWorkers "
                            + "WHERE (STR_TO_DATE(missionfinish, '%d.%m.%Y') < STR_TO_DATE(?, '%d.%m.%Y') "
                            + "OR m.iddurum=?) AND w.gmail=? AND w.workerName=?");
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formattedDateTime = now.format(formatter);
            psmt.setString(1, formattedDateTime);
            psmt.setInt(2, 2);
            psmt.setString(3, ugmail);
            psmt.setString(4, name);
            ResultSet rs = psmt.executeQuery();

            while (rs.next()) {
                int durum = rs.getInt("iddurum");
                if (durum == 2) {
                    tamamlandi++;
                } else if (durum == 3) {
                    tamamlanmadi++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ("Tamamlanmis gorev sayisi :" + tamamlandi + " Tamamlanmamis gorev sayisi : " + tamamlanmadi);
    }
}