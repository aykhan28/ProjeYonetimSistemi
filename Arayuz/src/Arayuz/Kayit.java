package Arayuz;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

// Kayit sınıfı, kullanıcı kaydını sunucuya göndermek için GUI sağlar
class Kayit implements Runnable {

    private Socket socket;
    private JTextField gmailField, passwordField, nameField, surnameField;
    private JFrame frame;
    private JTextArea responseArea;

    // Constructor: Socket nesnesini alır
    public Kayit(Socket socket) {
        this.socket = socket;
    }

    // GUI elemanlarını oluşturur ve pencereyi başlatır
    public void run() {
        frame = new JFrame("Kullanıcı Girişi");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().setBackground(new Color(60, 63, 65));

        // Text alanlarını oluşturur
        gmailField = new JTextField(10);
        passwordField = new JTextField(10);
        nameField = new JTextField(10);
        surnameField = new JTextField(10);
        responseArea = new JTextArea(5, 20);
        responseArea.setEditable(false);

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        // Etiketleri ve text alanlarını ekler
        frame.add(new JLabel("Gmail:"));
        frame.add(gmailField);
        frame.add(new JLabel("Şifre:"));
        frame.add(passwordField);
        frame.add(new JLabel("İsim:"));
        frame.add(nameField);
        frame.add(new JLabel("Soyisim:"));
        frame.add(surnameField);

        // Giriş butonunu ekler
        JButton SignButton = new JButton("Giriş Yap");
        SignButton.setBackground(new Color(75, 110, 175));
        SignButton.setForeground(Color.WHITE);
        frame.add(SignButton);

        // Yanıtları gösterecek alanı ekler
        frame.add(new JScrollPane(responseArea));

        // Buton tıklama olayı
        SignButton.addActionListener((ActionEvent e) -> {
            Sign(); // Kullanıcı bilgilerini sunucuya gönderir
            frame.dispose(); // Pencereyi kapatır
        });

        frame.setVisible(true); // Pencereyi görünür yapar
    }

    // Kullanıcı bilgilerini sunucuya gönderir
    private void Sign() {
        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Kullanıcı bilgilerini sunucuya gönderir
            ob.write("# " + gmailField.getText() + " " + passwordField.getText() + " " + nameField.getText() + " " + surnameField.getText());
            ob.newLine();
            ob.flush();

            // Sunucudan gelen yanıtı gösterir
            String response = in.readLine();
            responseArea.setText(response);

            socket.close(); // Sunucuya bağlantıyı kapatır

        } catch (Exception e) {
            e.printStackTrace(); // Hata durumunda hata mesajını yazdırır
        }
    }
}