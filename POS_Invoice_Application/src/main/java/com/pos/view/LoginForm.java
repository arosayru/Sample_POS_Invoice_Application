package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.controller.UserController;
import com.pos.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;
    private final UserController userController;

    public LoginForm() {
        userController = new UserController();

        setTitle("Login - POS Application");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(null);

        add(new JLabel("Username:"));
        txtUser = new JTextField();
        add(txtUser);

        add(new JLabel("Password:"));
        txtPass = new JPasswordField();
        add(txtPass);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> handleLogin());
        add(new JLabel()); // empty placeholder
        add(btnLogin);

        setVisible(true);
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
                JOptionPane.showMessageDialog(this, "Welcome " + user.getUsername() + "!");
                dispose();
                new InvoiceForm(); // open main POS window
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

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(LoginForm::new);
    }
}
