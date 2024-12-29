package ProjeYonetimYazilimi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.*;

public class ProjeYonetimYazilimi {

    // Static değişkenler, işçi ve görev ID'lerini depolamak için
    static long wid;
    static long mid;

    // Veritabanı bağlantısı
    protected static Connection con;

    public static void main(String[] args) {
        try {
            // Veritabanı bağlantısı oluşturuluyor
            createConnection();

            // Sunucu soketi belirli bir port üzerinde başlatılıyor
            ServerSocket serversoc = new ServerSocket(5252);

            // Gelen istemci bağlantılarını sürekli dinleyen döngü
            while (true) {
                // Her istemci bağlantısı için yeni bir iş parçacığı başlatılıyor
                Thread th = new Thread(new ClientHandlermng(serversoc.accept()));
                th.start();
            }
        } catch (Exception e) {
            // Hata durumunda ayrıntılar yazdırılır
            e.printStackTrace();
        }
    }

    // Veritabanı bağlantısını oluşturur
    public static void createConnection() throws Exception {
        // MySQL JDBC sürücüsü yükleniyor
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Veritabanına bağlanılıyor
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/vtys", "root", "root");
        System.out.println("Veri Tabanına Bağlanıldı!");
    }
}

// İstemciyi yöneten iş parçacığı sınıfı
class ClientHandlermng implements Runnable {

    // İstemci soketi
    Socket socket;

    // Constructor: İstemci soketi alınıyor
    public ClientHandlermng(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            // İstemci iletişimi devam ederken true olarak kalacak sinyal
            boolean signal = true;

            // İstemci ile iletişim için okuyucu ve yazıcı oluşturuluyor
            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // İlk istemci isteği alınıyor
            String opr = in.readLine();

            if (opr.contains("$")) { // Kullanıcı kimlik doğrulama komutu
                String rsp;
                String ugmail;
                String gp[] = opr.split(" ");
                ugmail = gp[1];

                // Kullanıcı kimlik doğrulaması yapılır
                rsp = User.Authentication(gp[1], gp[2]);
                System.out.println(rsp);

                // Kimlik doğrulama yanıtı istemciye gönderilir
                pw.write(rsp);
                pw.newLine();
                pw.flush();

                if (!rsp.equals("+")) { // Başarısızsa sinyal kapatılır
                    signal = false;
                }

                // Proje zaman kontrolleri alınıyor ve istemciye gönderiliyor
                ArrayList<String> timeContList = Project.projeZamanKont(ugmail);
                sendList(timeContList);

                // Proje bilgileri istemciye gönderiliyor
                ArrayList<String> proInf = Project.projects(ugmail);
                sendList(proInf);

                // İşçi bilgileri istemciye gönderiliyor
                ArrayList<String> workers = Worker.workers(ugmail);
                sendList(workers);

                // İstemciden gelen işlemleri sürekli dinleyen döngü
                while (signal) {
                    String sysmanage = in.readLine();

                    // Gelen komutlara göre işlem yapılır
                    if (sysmanage.contains("p&~78")) { // Proje ekleme komutu
                        String projparam[] = sysmanage.split(" ");
                        System.out.println(projparam[1]);
                        rsp = new Project(projparam[1], projparam[2], projparam[3], ugmail).projectAdd();
                        pw.write(rsp);
                        pw.newLine();
                        pw.flush();
                    } else if (sysmanage.contains("w$#/8")) { // İşçi ekleme komutu
                        String workparam[] = sysmanage.split(" ");
                        System.out.println(workparam[1]);
                        String response = new Worker(workparam[1], workparam[2], workparam[3]).workerAdd(ugmail);
                        pw.write(response);
                        pw.newLine();
                        pw.flush();
                    } else if (sysmanage.contains("m>,?[]")) { // Görev ekleme komutu
                        String missionparam[] = sysmanage.split(" ");
                        String response = new Mission(missionparam[1], missionparam[2], missionparam[3], missionparam[4], missionparam[5], ugmail).missionAdd();
                        pw.write(response);
                        pw.newLine();
                        pw.flush();
                    } else if (sysmanage.contains("dm<>&89.")) { // İşçi silme komutu
                        String di[] = sysmanage.split(" ");
                        String response = Worker.DeleteWorker(di[1], ugmail);
                        pw.write(response);
                        pw.newLine();
                        pw.flush();
                    } else if (sysmanage.contains("wp&*()3u8")) { // İşçi bilgisi alma komutu
                        String wp[] = sysmanage.split(" ");
                        ArrayList<String> list = Worker.workerinfo(wp[1], ugmail);
                        for (int i = 0; i < list.size(); i++) {
                            pw.write(list.get(i));
                            pw.newLine();
                            pw.flush();
                        }
                        pw.write("OK");
                        pw.newLine();
                        pw.flush();
                    } else if (sysmanage.contains("<.l:P")) { // İşçi durumu güncelleme komutu
                        String wp[] = sysmanage.split(" ");
                        pw.write(Worker.UpdateWorkerCondition(wp[1], wp[2], ugmail));
                        pw.newLine();
                        pw.flush();
                    } else if (sysmanage.contains("!@#$%")) { // Görev bilgisi isteme komutu
                        String nm[] = sysmanage.split(" ");
                        System.out.println(nm[1]);
                        pw.write(Worker.WorkerMissionInf(nm[1], ugmail));
                        pw.newLine();
                        pw.flush();
                    } else if (sysmanage.contains("@rf~s#*h")) { // Proje ve işçi bilgilerini güncelleme
                        proInf = Project.projects(ugmail);
                        sendList(proInf);
                        workers = Worker.workers(ugmail);
                        sendList(workers);
                    }
                }
            } else if (opr.contains("#")) { // Kullanıcı ekleme komutu
                String param[] = opr.split(" ");
                String rsp = new User(param[1], param[2], param[3], param[4]).AddUser();
                System.out.println(rsp);
                pw.write(rsp);
                pw.newLine();
                pw.flush();
            }

            if (opr.contains("!")) { // Otomatik görev güncelleme işlemleri
                String wrkprm[] = opr.split(" ");
                String response = Worker.AutoHenction(wrkprm[1], wrkprm[2]);
                pw.write(response);
                pw.newLine();
                pw.flush();

                if (response.contains("+")) {
                    while (true) {
                        String task = in.readLine();
                        System.out.println(task.split(" ")[1]);
                        response = Mission.UpdateCondition(task.split(" ")[1], wrkprm[2]);
                        pw.write(response);
                        pw.newLine();
                        pw.flush();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Liste halindeki verileri istemciye gönderir
    public void sendList(ArrayList<String> list) {
        try {
            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            for (int i = 0; i < list.size(); i++) {
                bf.write(list.get(i));
                bf.newLine();
                bf.flush();
            }
            bf.write("OK");
            bf.newLine();
            bf.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}