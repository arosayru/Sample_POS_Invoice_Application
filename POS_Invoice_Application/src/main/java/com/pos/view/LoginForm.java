package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.controller.UserController;
import com.pos.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private final UserController userController;
    private JLabel lblDateTime;

    public LoginForm() {
        userController = new UserController();

        FlatLightLaf.setup();
        initUI();

        new Timer(1000, e -> updateDateTime()).start();
        setVisible(true);
    }

    private void initUI() {
        setTitle("POS System Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("POS System Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.BLACK);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        txtUser = new JTextField();
        txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtUser.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.ipadx = 200;
        mainPanel.add(txtUser, gbc);

        txtPass = new JPasswordField();
        txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridy++;
        mainPanel.add(txtPass, gbc);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(33, 150, 243));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        gbc.gridy++;
        mainPanel.add(btnLogin, gbc);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblForgot = new JLabel("Forgot Password?");
        lblForgot.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblForgot.setForeground(new Color(100, 100, 100));
        bottomPanel.add(lblForgot, BorderLayout.WEST);

        lblDateTime = new JLabel("", SwingConstants.CENTER);
        lblDateTime.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDateTime.setForeground(new Color(120, 120, 120));
        bottomPanel.add(lblDateTime, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> handleLogin());

        KeyAdapter enterKey = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        };
        txtUser.addKeyListener(enterKey);
        txtPass.addKeyListener(enterKey);

        updateDateTime();
    }

    private void updateDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd yyyy hh:mm:ss a");
        lblDateTime.setText(sdf.format(new Date()));
    }

    private void handleLogin() {
        try {
            String username = txtUser.getText().trim();
            String password = new String(txtPass.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both username and password!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = userController.login(username, password);
            if (user != null) {
                showWelcomeAndClose(user.getUsername());
                new DashboardForm().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void showWelcomeAndClose(String username) {
        JDialog welcomeDialog = new JDialog(this, false);
        welcomeDialog.setUndecorated(true);
        welcomeDialog.setLayout(new BorderLayout());
        welcomeDialog.setBackground(new Color(255, 255, 255, 0));

        JLabel lblMsg = new JLabel("Welcome " + username + "!", SwingConstants.CENTER);
        lblMsg.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblMsg.setForeground(new Color(33, 150, 243));
        lblMsg.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        welcomeDialog.add(lblMsg, BorderLayout.CENTER);

        welcomeDialog.setSize(300, 100);
        welcomeDialog.setLocationRelativeTo(this);
        welcomeDialog.setVisible(true);

        new Timer(500, e -> {
            welcomeDialog.dispose();
            dispose();
        }).start();
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(LoginForm::new);
    }
}
