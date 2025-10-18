package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private JProgressBar progressBar;
    private JLabel lblStatus;

    public SplashScreen() {
        FlatLightLaf.setup();
        initUI();
        loadProgress();
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JLabel lblTitle = new JLabel("POS Invoice Application", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        panel.add(lblTitle, BorderLayout.NORTH);

        JLabel lblIcon = new JLabel();
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        java.net.URL iconURL = getClass().getResource("/icons/loading.gif");
        if (iconURL != null) {
            lblIcon.setIcon(new ImageIcon(iconURL));
        } else {
            lblIcon.setText("Loading...");
        }
        panel.add(lblIcon, BorderLayout.CENTER);

        // Progress Bar
        progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(33, 150, 243));
        progressBar.setBorderPainted(false);

        lblStatus = new JLabel("Initializing modules...", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(lblStatus, BorderLayout.NORTH);
        bottomPanel.add(progressBar, BorderLayout.SOUTH);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        panel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(panel);
        setSize(400, 250);
        setLocationRelativeTo(null);
    }

    private void loadProgress() {
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    final int progress = i;

                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(progress);

                        if (progress < 30) lblStatus.setText("Connecting to database...");
                        else if (progress < 60) lblStatus.setText("Loading modules...");
                        else if (progress < 90) lblStatus.setText("Starting application...");
                        else lblStatus.setText("Done!");
                    });

                    Thread.sleep(30);
                }

                SwingUtilities.invokeLater(() -> {
                    dispose();
                    new LoginForm();
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SplashScreen::new);
    }
}
