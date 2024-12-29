package Arayuz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

class Yonetici implements Runnable {

    private Socket socket;
    private JFrame frame;
    private JTextField gmailField, passwordField, projectNameField, startDateField, endDateField;
    private JButton loginButton, createProjectButton;
    private JTextArea responseArea;
    private JTextField fullNameField, workerGmailField, positionField;
    private JButton addWorkerButton;
    private JTextField projectNameField1, workerNameField, missionField, missionStartField, missionEndField;
    private JButton addMissionButton;
    private JTextField workerIdField;
    private JButton deleteWorkerButton;
    private JButton getWorkerInfoButton;
    private JButton updateWorkerButton;
    private JTextField workernamefieldupdt, workergmailfield;
    private JButton getMissionInfButton;
    private JTextField nameMsnfield;
    private JButton refreshButton;
    private JPanel logPanel, CreatePPanel, CreateWPanel, AddMPanel, WorkerPanel, UpdateWorkerPanel, SitutationPanel;
    private JTable jtp, jtw;
    private JScrollPane spp, spw;

    public Yonetici(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        frame = new JFrame("Yönetici Paneli");
        frame.setSize(800, 900);
        frame.getContentPane().setBackground(new Color(60, 63, 65));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        gmailField = new JTextField(10);
        passwordField = new JTextField(10);
        loginButton = new JButton("Giriş");
        loginButton.setBackground(new Color(75, 110, 175));
        loginButton.setForeground(Color.WHITE);
        projectNameField1 = new JTextField(20);
        startDateField = new JTextField(10);
        endDateField = new JTextField(10);
        createProjectButton = new JButton("Proje Oluştur");
        createProjectButton.setBackground(new Color(75, 175, 75));
        createProjectButton.setForeground(Color.WHITE);
        logPanel = new JPanel();
        logPanel.setBackground(new Color(43, 43, 43));
        logPanel.add(new JLabel("Gmail:")).setForeground(Color.WHITE);
        logPanel.add(gmailField);
        logPanel.add(new JLabel("Şifre:")).setForeground(Color.WHITE);
        logPanel.add(passwordField);
        logPanel.add(loginButton);
        frame.add(logPanel);

        CreatePPanel = new JPanel();
        CreatePPanel.setBackground(new Color(43, 43, 43));
        CreatePPanel.add(new JLabel("Proje İsmi:")).setForeground(Color.WHITE);
        CreatePPanel.add(projectNameField1);
        CreatePPanel.add(new JLabel("Başlangıç Tarihi:")).setForeground(Color.WHITE);
        CreatePPanel.add(startDateField);
        CreatePPanel.add(new JLabel("Bitiş Tarihi:")).setForeground(Color.WHITE);
        CreatePPanel.add(endDateField);
        CreatePPanel.add(createProjectButton);
        frame.add(CreatePPanel);

        fullNameField = new JTextField(20);
        workerGmailField = new JTextField(20);
        positionField = new JTextField(20);
        CreateWPanel = new JPanel();
        CreateWPanel.setBackground(new Color(43, 43, 43));
        addWorkerButton = new JButton("Çalışan Ekle");
        addWorkerButton.setBackground(new Color(75, 175, 75));
        addWorkerButton.setForeground(Color.WHITE);
        CreateWPanel.add(new JLabel("Çalışan İsim:")).setForeground(Color.WHITE);
        CreateWPanel.add(fullNameField);
        CreateWPanel.add(new JLabel("Çalışan Gmail:")).setForeground(Color.WHITE);
        CreateWPanel.add(workerGmailField);
        CreateWPanel.add(new JLabel("Pozisyon:")).setForeground(Color.WHITE);
        CreateWPanel.add(positionField);
        CreateWPanel.add(addWorkerButton);
        CreateWPanel.setPreferredSize(new Dimension(300, 200));
        frame.add(CreateWPanel);

        AddMPanel = new JPanel();
        AddMPanel.setBackground(new Color(43, 43, 43));
        projectNameField = new JTextField(20);
        workerNameField = new JTextField(20);
        missionField = new JTextField(20);
        missionStartField = new JTextField(20);
        missionEndField = new JTextField(20);
        addMissionButton = new JButton("Görev Ekle");
        addMissionButton.setBackground(new Color(75, 175, 75));
        addMissionButton.setForeground(Color.WHITE);
        AddMPanel.add(new JLabel("Proje İsmi:")).setForeground(Color.WHITE);
        AddMPanel.add(projectNameField);
        AddMPanel.add(new JLabel("Çalışan İsmi:")).setForeground(Color.WHITE);
        AddMPanel.add(workerNameField);
        AddMPanel.add(new JLabel("Görev:")).setForeground(Color.WHITE);
        AddMPanel.add(missionField);
        AddMPanel.add(new JLabel("Başlangıç Tarihi:")).setForeground(Color.WHITE);
        AddMPanel.add(missionStartField);
        AddMPanel.add(new JLabel("Bitiş Tarihi:")).setForeground(Color.WHITE);
        AddMPanel.add(missionEndField);
        AddMPanel.add(addMissionButton);
        AddMPanel.setPreferredSize(new Dimension(300, 200));
        frame.add(AddMPanel);

        workerIdField = new JTextField(20);
        deleteWorkerButton = new JButton("Çalışan Sil");
        deleteWorkerButton.setBackground(new Color(175, 75, 75));
        deleteWorkerButton.setForeground(Color.WHITE);
        WorkerPanel = new JPanel();
        WorkerPanel.setBackground(new Color(43, 43, 43));
        WorkerPanel.add(new JLabel("Çalışan İsmi:")).setForeground(Color.WHITE);
        WorkerPanel.add(workerIdField);
        WorkerPanel.add(deleteWorkerButton);
        getWorkerInfoButton = new JButton("Çalışan Hakkında Bilgi Al");
        getWorkerInfoButton.setBackground(new Color(75, 110, 175));
        getWorkerInfoButton.setForeground(Color.WHITE);
        WorkerPanel.add(getWorkerInfoButton);
        frame.add(WorkerPanel);

        UpdateWorkerPanel = new JPanel();
        UpdateWorkerPanel.setBackground(new Color(43, 43, 43));
        updateWorkerButton = new JButton("Çalışanın Gmail'ini Güncelle");
        updateWorkerButton.setBackground(new Color(75, 110, 175));
        updateWorkerButton.setForeground(Color.WHITE);
        workergmailfield = new JTextField(20);
        workernamefieldupdt = new JTextField(20);
        UpdateWorkerPanel.add(new JLabel("Çalışan İsmi:")).setForeground(Color.WHITE);
        UpdateWorkerPanel.add(workernamefieldupdt);
        UpdateWorkerPanel.add(new JLabel("Çalışanın Yeni Gmail'i:")).setForeground(Color.WHITE);
        UpdateWorkerPanel.add(workergmailfield);
        UpdateWorkerPanel.add(updateWorkerButton);
        frame.add(UpdateWorkerPanel);

        SitutationPanel = new JPanel();
        SitutationPanel.setBackground(new Color(43, 43, 43));
        getMissionInfButton = new JButton("Görev Durumu Al");
        getMissionInfButton.setBackground(new Color(75, 110, 175));
        getMissionInfButton.setForeground(Color.WHITE);
        nameMsnfield = new JTextField(20);
        SitutationPanel.add(new JLabel("İsim")).setForeground(Color.WHITE);
        SitutationPanel.add(nameMsnfield);
        SitutationPanel.add(getMissionInfButton);
        frame.add(SitutationPanel);

        refreshButton = new JButton("Yenile");
        refreshButton.setBackground(new Color(75, 110, 175));
        refreshButton.setForeground(Color.WHITE);
        frame.add(refreshButton);

        responseArea = new JTextArea(5, 30);
        responseArea.setEditable(false);
        frame.add(new JScrollPane(responseArea));

        loginButton.addActionListener((ActionEvent e) -> {
            login();
        });

        getMissionInfButton.addActionListener((ActionEvent e) -> {
            getWorkerMissionInf();
        });
        updateWorkerButton.addActionListener((ActionEvent e) -> {
            uptadeWorker();
        });
        getWorkerInfoButton.addActionListener((ActionEvent e) -> {
            getWorkerInfo();
        });

        deleteWorkerButton.addActionListener((ActionEvent e) -> {
            deleteWorker();
        });

        createProjectButton.addActionListener((ActionEvent e) -> {
            createProject();
        });

        addWorkerButton.addActionListener((ActionEvent e) -> {
            addWorker();
        });

        addMissionButton.addActionListener((ActionEvent e) -> {
            addMission();
        });

        refreshButton.addActionListener((ActionEvent e) -> {
            refreshPrjctWrk();
        });

        frame.setVisible(true);
    }

    private void login() {

        String gmail = gmailField.getText();
        String password = passwordField.getText();
        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ob.write("$ " + gmail + " " + password);
            ob.newLine();
            ob.flush();
            String response = in.readLine();
            System.out.println(response);
            if (response.equals("+")) {
                responseArea.setText(GetInfosFromServer().toString());
                String[] pBaslik = {"PROJE ISMI", "BAŞLANGIÇ TARIHI", "BITIŞ TARIHI"};
                String[] wBaslik = {"ÇALIŞAN ISMI", "ÇALIŞAN GMAILI"};
                jtp = new JTable(ListToArray(GetInfosFromServer()), pBaslik);
                jtp.setBounds(30, 40, 200, 300);
                spp = new JScrollPane(jtp);
                frame.add(spp);
                jtw = new JTable(ListToArray(GetInfosFromServer()), wBaslik);
                jtw.setBounds(30, 40, 200, 300);
                spw = new JScrollPane(jtw);
                frame.add(spw);

                jtp.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                    if (!e.getValueIsAdjusting()) {
                        int selectedRow = jtp.getSelectedRow();
                        if (selectedRow != -1) {
                            projectNameField.setText(jtp.getValueAt(selectedRow, 0).toString());
                        }
                    }
                });

                jtw.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                    if (!e.getValueIsAdjusting()) {
                        int selectedRow = jtw.getSelectedRow();
                        if (selectedRow != -1) {
                            workerNameField.setText(jtw.getValueAt(selectedRow, 0).toString());
                        }
                    }
                });
                JOptionPane.showMessageDialog(frame, "Giriş yapıldı", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Giriş yapılamadı", "Hata", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
            responseArea.setText("Hata: " + e.getMessage());
        }

    }

    public ArrayList<String> GetInfosFromServer() {
        ArrayList<String> info = new ArrayList<>();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = "response";
            while (!response.equals("OK")) {
                response = in.readLine();
                if (!response.equals("OK")) {
                    info.add(response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    private void createProject() {
        String projectName = projectNameField1.getText();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println(projectName);
            ob.write("p&~78 " + projectName + " " + startDate + " " + endDate);
            ob.newLine();
            ob.flush();

            String response = in.readLine();
            responseArea.setText(response);
        } catch (IOException e) {
            e.printStackTrace();
            responseArea.setText("Hata: " + e.getMessage());
        }

    }

    private void addMission() {
        String projectName = projectNameField.getText();
        String workerName = workerNameField.getText();
        String mission = missionField.getText();
        String startTime = missionStartField.getText();
        String endTime = missionEndField.getText();

        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ob.write("m>,?[]" + " " + projectName + " " + workerName + " " + mission + " " + startTime + " " + endTime);
            ob.newLine();
            ob.flush();

            String response = in.readLine();
            responseArea.setText(response);
        } catch (IOException e) {
            e.printStackTrace();
            responseArea.setText("Hata: " + e.getMessage());
        }
    }

    private void addWorker() {
        String fullName = fullNameField.getText();
        String workerGmail = workerGmailField.getText();
        String position = positionField.getText();

        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            ob.write("w$#/8" + " " + fullName + " " + workerGmail + " " + position);
            ob.newLine();
            ob.flush();

            String response = in.readLine();
            responseArea.setText(response);
        } catch (IOException e) {
            e.printStackTrace();
            responseArea.setText("Hata: " + e.getMessage());
        }
    }

    private void deleteWorker() {
        String workerId = workerIdField.getText();

        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            ob.write("dm<>&89." + " " + workerId);
            ob.newLine();
            ob.flush();

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            responseArea.setText(response);
        } catch (IOException e) {
            e.printStackTrace();
            responseArea.setText("Hata: " + e.getMessage());
        }
    }

    private void uptadeWorker() {
        try {
            String workername = workernamefieldupdt.getText();
            String workergmail = workergmailfield.getText();
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ob.write("<.l:P" + " " + workername + " " + workergmail);
            ob.newLine();
            ob.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            responseArea.setText(response);

        } catch (Exception e) {

        }

    }

    private void getWorkerInfo() {
        String workerId = workerIdField.getText();
        try {

            ArrayList<String> info = new ArrayList<String>();
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ob.write("wp&*()3u8" + " " + workerId);
            ob.newLine();
            ob.flush();
            responseArea.setText(GetInfosFromServer().toString());
        } catch (Exception e) {
            e.printStackTrace();
            responseArea.setText("Hata: " + e.getMessage());
        }
    }

    private void getWorkerMissionInf() {
        try {
            String name = nameMsnfield.getText();
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ob.write("!@#$%" + " " + name);
            ob.newLine();
            ob.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = in.readLine();
            responseArea.setText(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void refreshPrjctWrk() {
        try {
            BufferedWriter ob = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            ob.write("@rf~s#*h");
            ob.newLine();
            ob.flush();
            String[] basp = {"PROJE ISMI", "BAŞLANGIÇ TARIHI", "BITIŞ TARIHI"};
            String[] basw = {"ÇALIŞAN ISMI", "ÇALIŞAN GMAILI"};
            jtp.removeAll();
            jtp.setModel(new DefaultTableModel(ListToArray(GetInfosFromServer()), basp));
            jtw.removeAll();
            jtw.setModel(new DefaultTableModel(ListToArray(GetInfosFromServer()), basw));
            frame.revalidate();
            frame.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[][] ListToArray(ArrayList<String> List) {
        String array[][] = new String[100][100];
        for (int i = 0; i < List.size(); i++) {
            String temp[] = List.get(i).split(" ");
            for (int j = 0; j < temp.length; j++) {
                array[i][j] = temp[j];
            }
        }

        return array;
    }

}