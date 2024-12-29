package ProjeYonetimYazilimi;

import static ProjeYonetimYazilimi.ProjeYonetimYazilimi.con;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

final class Mission {

    private int idprojects;
    private int idWorkers;
    private String mission;
    private String missionstart;
    private String missionfinish;
    private int iddurum;

    // Görev oluşturucu: Proje ve çalışan bilgilerini, görev detaylarını alır
    public Mission(String projectname, String workername, String mission, String missionstart, String missionfinish, String ugmail) {
        this.idprojects = getIdforUser(projectname, "pid", ugmail); // Projenin ID'sini alır
        this.idWorkers = getIdforUser(workername, "wid", ugmail);  // Çalışanın ID'sini alır
        this.mission = mission;
        this.missionstart = missionstart;
        try {
            if (isValidDate(missionfinish)) { // Tarih formatı doğru mu kontrol edilir
                this.missionfinish = comprasion(getProjectFinishTime(this.idprojects), missionfinish); // Tarih karşılaştırması yapılır
                this.iddurum = 3; // Geçerli tarih için durum atanır
            } else {
                this.iddurum = 1; // Geçersiz tarih için farklı durum atanır
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Girilen tarih formatını kontrol eder
    private static boolean isValidDate(String input) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false); // Hatalı tarih girişlerine izin vermez

        try {
            Date date = dateFormat.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Görevi veritabanına ekler
    public String missionAdd() {
        try {
            PreparedStatement psmt = con.prepareStatement(
                "INSERT INTO missions (idprojects, idWorkers, mission, missionstart, missionfinish, iddurum) VALUES(?,?,?,?,?,?)"
            );
            psmt.setInt(1, this.idprojects); // Proje ID
            psmt.setInt(2, this.idWorkers); // Çalışan ID
            psmt.setString(3, this.mission); // Görev açıklaması
            psmt.setString(4, this.missionstart); // Görevin başlangıç tarihi
            psmt.setString(5, this.missionfinish); // Görevin bitiş tarihi
            psmt.setInt(6, this.iddurum); // Görev durumu
            psmt.execute();
            psmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "Işlem Başarısız!";
        }
        return "Görev Eklendi";
    }

    // Projenin bitiş tarihini getirir
    public String getProjectFinishTime(int id) throws Exception {
        PreparedStatement psmt = con.prepareStatement("SELECT * FROM projects WHERE idprojects=?");
        psmt.setInt(1, id); // Proje ID'sine göre sorgu yapılır
        ResultSet rs = psmt.executeQuery();
        rs.next(); // Sonuç setinden ilk satır alınır
        return rs.getString("finishTime");
    }

    // Görev bitiş tarihi ile proje bitiş tarihini karşılaştırır
    public String comprasion(String ProjectFinishTime, String missionfinishtime) throws Exception {
        String projecttime[] = ProjectFinishTime.split("\\.");
        String missiontime[] = missionfinishtime.split("\\.");
        int projtime = Integer.parseInt(projecttime[0]) + Integer.parseInt(projecttime[1]) * 30 + Integer.parseInt(projecttime[2]) * 365;
        int mistime = Integer.parseInt(missiontime[0]) + Integer.parseInt(missiontime[1]) * 30 + Integer.parseInt(missiontime[2]) * 365;

        if (projtime > mistime) { // Proje bitiş tarihi görev bitiş tarihinden büyükse
            return missionfinishtime;
        }
        return ProjectFinishTime; // Aksi halde proje bitiş tarihi döner
    }

    // Kullanıcıya bağlı proje veya çalışan ID'sini getirir
    public static int getIdforUser(String name, String opr, String gmail) {
        try {
            if (opr.equals("pid")) { // Proje ID'si için
                PreparedStatement psmt = con.prepareStatement(
                    "SELECT p.idprojects FROM projects p WHERE projectname=? AND gmail=?"
                );
                psmt.setString(1, name);
                psmt.setString(2, gmail);
                ResultSet rs = psmt.executeQuery();
                rs.next();
                return rs.getInt("idprojects");
            } else if (opr.equals("wid")) { // Çalışan ID'si için
                PreparedStatement psmt = con.prepareStatement(
                    "SELECT w.idWorkers FROM workers w WHERE workerName=? AND gmail=?"
                );
                psmt.setString(1, name);
                psmt.setString(2, gmail);
                ResultSet rs = psmt.executeQuery();
                rs.next();
                return rs.getInt("idWorkers");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Hata durumunda 0 döner
        }
        return 1; // Varsayılan döndürülen değer
    }

    // Görev adı ve çalışanın mailine göre ID'leri getirir
    public static String getIdforWorker(String missionname, String workergmail) {
        try {
            PreparedStatement psmt = con.prepareStatement(
                "SELECT m.idmissions, m.idWorkers FROM missions m JOIN workers w ON m.idWorkers = w.idWorkers " +
                "WHERE mission=? AND workergmail=?"
            );
            psmt.setString(1, missionname);
            psmt.setString(2, workergmail);
            ResultSet rs = psmt.executeQuery();
            rs.next();
            return rs.getString("idmissions") + " " + rs.getString("idWorkers");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Hata!";
    }

    // Görev durumunu günceller
    public static String UpdateCondition(String mission, String workergmail) {
        try {
            PreparedStatement psmt = con.prepareStatement(
                "UPDATE missions SET iddurum =? WHERE idworkers=? AND idmissions=?"
            );
            String ids = getIdforWorker(mission, workergmail); // Görev ve çalışan ID'lerini alır
            psmt.setInt(1, 2); // Durum 2 olarak güncellenir
            psmt.setString(2, ids.split(" ")[1]); // Çalışan ID'si
            psmt.setString(3, ids.split(" ")[0]); // Görev ID'si
            psmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return "Güncelleme yapılamadı";
        }
        return "Güncelleme yapıldı";
    }
}