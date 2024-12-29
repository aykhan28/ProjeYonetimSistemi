package Arayuz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

class Calisan implements Runnable {

    private Socket socket;
    private JFrame frame;
    private JTextArea responseArea;
    private JTextField fullNameField, gmailField, missionField;
    private JPanel workPanel, UpdatePanel;

    public Calisan(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        frame = new JFrame("Çalışan Girişi");
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.getContentPane().setBackground(new Color(60, 63, 65));

        workPanel = new JPanel();
        workPanel.setPreferredSize(new Dimension(300, 150));
        workPanel.setBackground(new Color(43, 43, 43));

        fullNameField = new JTextField(20);
        gmailField = new JTextField(20);
        missionField = new JTextField(20);
        responseArea = new JTextArea(5, 20);
        responseArea.setEditable(false);

        workPanel.add(new JLabel("İsim Soyisim:")).setForeground(Color.WHITE);
        workPanel.add(fullNameField);
        workPanel.add(new JLabel("Gmail:")).setForeground(Color.WHITE);
        workPanel.add(gmailField);

        JButton loginButton = new JButton("Giriş Yap");
        loginButton.setBackground(new Color(75, 110, 175));
        loginButton.setForeground(Color.WHITE);
        workPanel.add(loginButton);

        frame.add(workPanel);

        UpdatePanel = new JPanel();
        UpdatePanel.setPreferredSize(new Dimension(300, 150));
        UpdatePanel.setBackground(new Color(43, 43, 43));
        UpdatePanel.add(new JLabel("Görev:")).setForeground(Color.WHITE);
        UpdatePanel.add(missionField);

        JButton submitButton = new JButton("Gönder");
        submitButton.setBackground(new Color(75, 175, 75));
        submitButton.setForeground(Color.WHITE);
        UpdatePanel.add(submitButton);

        frame.add(UpdatePanel);
        frame.add(new JScrollPane(responseArea));

        loginButton.addActionListener((ActionEvent e) -> {
            login();
        });

        submitButton.addActionListener((ActionEvent e) -> {
            submitMission();
        });

        frame.setVisible(true);
    }

    private void login() {
        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ob.write("!" + " " + fullNameField.getText() + " " + gmailField.getText());
            ob.newLine();
            ob.flush();

            String response = in.readLine();
            responseArea.setText(response);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitMission() {
        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ob.write("+ " + missionField.getText());
            ob.newLine();
            ob.flush();

            String response = in.readLine();
            responseArea.setText(response);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}