package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class SplashScreen extends JWindow {
    private JLabel lblDots;
    private Timer dotTimer;
    private int dotCount = 0;

    public SplashScreen() {
        FlatLightLaf.setup();
        initUI();
        startLoadingAnimation();
        loadProgress();

        setVisible(true);
    }

    private void initUI() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel lblTitle = new JLabel("POS System", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(Color.BLACK);

        JLabel lblSub = new JLabel("Retail Management Solution", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSub.setForeground(new Color(33, 150, 243));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblDots = new JLabel("● ○ ○ ○", SwingConstants.CENTER);
        lblDots.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDots.setForeground(new Color(150, 150, 150));
        lblDots.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDots.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblTitle);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(lblSub);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        centerPanel.add(lblDots);
        centerPanel.add(Box.createVerticalGlue());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JLabel lblVersion = new JLabel("Version 1.0.0", SwingConstants.CENTER);
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblVersion.setForeground(new Color(100, 100, 100));
        lblVersion.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCopyright = new JLabel("© 2024 All rights reserved.", SwingConstants.CENTER);
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(new Color(160, 160, 160));
        lblCopyright.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(lblVersion);
        bottomPanel.add(lblCopyright);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        setContentPane(panel);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.width * 0.5);
        int height = (int) (screen.height * 0.5);
        setSize(width, height);
        setLocationRelativeTo(null);
    }

    private void startLoadingAnimation() {
        dotTimer = new Timer(300, e -> {
            dotCount = (dotCount + 1) % 4;
            StringBuilder dots = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                dots.append(i == dotCount ? "● " : "○ ");
            }
            lblDots.setText(dots.toString().trim());
        });
        dotTimer.start();
    }

    private void loadProgress() {
        new Thread(() -> {
            try {
                Thread.sleep(3500);
                SwingUtilities.invokeLater(() -> {
                    dotTimer.stop();
                    dispose();
                    new LoginForm().setVisible(true);
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
