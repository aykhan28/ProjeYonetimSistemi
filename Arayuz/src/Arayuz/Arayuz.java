package Arayuz;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;

public class Arayuz extends JFrame {

    private Socket socket;

    public Arayuz() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Management");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(60, 63, 65));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel headerLabel = new JLabel("Proje Yönetim Yazılımı");
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setForeground(new Color(173, 216, 230));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(headerLabel, gbc);

        gbc.gridwidth = 1;

        JButton adminButton = createStyledButton("Yönetici", new Color(135, 206, 250));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(adminButton, gbc);

        JButton workerButton = createStyledButton("Çalışan", new Color(144, 238, 144));
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(workerButton, gbc);

        JButton signAdminButton = createStyledButton("Yönetici Kaydı Yaptır", new Color(255, 182, 193));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(signAdminButton, gbc);

        ImageIcon icon = new ImageIcon("C:\\Users\\user\\Downloads\\pngwing.com(3).png");
        Image image = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(image));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(iconLabel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        adminButton.addActionListener(e -> connectAndStartThread(1));
        workerButton.addActionListener(e -> connectAndStartThread(2));
        signAdminButton.addActionListener(e -> connectAndStartThread(3));

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        return button;
    }

    private void connectAndStartThread(int choice) {
        try {
            socket = new Socket("localhost", 5252);
            Thread sender;
            switch (choice) {
                case 1 ->
                    sender = new Thread(new Yonetici(socket));
                case 2 ->
                    sender = new Thread(new Calisan(socket));
                case 3 ->
                    sender = new Thread(new Kayit(socket));
                default ->
                    throw new IllegalStateException("Geçersiz Değer: " + choice);
            }
            sender.start();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Sunucuya bağlanamadı: " + e.getMessage(), "Bağlantı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Arayuz::new);
    }

}