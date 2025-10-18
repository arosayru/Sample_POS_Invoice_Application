package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.dao.UserDAO;
import com.pos.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;

    public LoginForm() {
        setTitle("Login - POS Application");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);

        add(new JLabel("Username:"));
        txtUser = new JTextField(); add(txtUser);

        add(new JLabel("Password:"));
        txtPass = new JPasswordField(); add(txtPass);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> handleLogin());
        add(new JLabel());
        add(btnLogin);

        setVisible(true);
    }

    private void handleLogin() {
        try {
            String username = txtUser.getText();
            String password = new String(txtPass.getPassword());
            User user = new UserDAO().login(username, password);

            if (user != null) {
                JOptionPane.showMessageDialog(this, "Welcome " + user.getUsername() + "!");
                dispose();
                new InvoiceForm(); // Open main POS window after login
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new LoginForm();
    }
}
