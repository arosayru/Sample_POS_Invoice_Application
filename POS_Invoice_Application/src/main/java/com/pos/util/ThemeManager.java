package com.pos.util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {
    private static boolean darkMode = false;

    public static void toggleTheme(JFrame frame, JButton toggleButton) {
        try {
            if (darkMode) {
                FlatLightLaf.setup();
                setButtonIcon(toggleButton, "/icons/sun.png");
            } else {
                FlatDarkLaf.setup();
                setButtonIcon(toggleButton, "/icons/moon.png");
            }

            darkMode = !darkMode;

            SwingUtilities.updateComponentTreeUI(frame);
            frame.revalidate();
            frame.repaint();

            updateBackgroundColor(frame);
            updateTextColors(frame);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    private static void setButtonIcon(JButton button, String resourcePath) {
        try {
            java.net.URL iconURL = ThemeManager.class.getResource(resourcePath);
            if (iconURL != null) {
                button.setIcon(new ImageIcon(iconURL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateBackgroundColor(JFrame frame) {
        Color bgColor = darkMode ? new Color(45, 45, 45) : new Color(245, 247, 250);

        frame.getContentPane().setBackground(bgColor);

        for (Component comp : frame.getContentPane().getComponents()) {
            if (comp instanceof JPanel) {
                ((JPanel) comp).setBackground(bgColor);
            }
        }

        frame.repaint();
    }

    private static void updateTextColors(JFrame frame) {
        Color textColor = darkMode ? Color.WHITE : Color.BLACK;
        Color subTextColor = darkMode ? new Color(200, 200, 200) : new Color(80, 80, 80);

        updateComponentTextColor(frame.getContentPane(), textColor, subTextColor);
    }

    private static void updateComponentTextColor(Container container, Color textColor, Color subTextColor) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JLabel label) {
                if (label.getFont().isBold()) {
                    label.setForeground(textColor);
                } else {
                    label.setForeground(subTextColor);
                }
            } else if (comp instanceof JButton button) {
                button.setForeground(textColor);
            } else if (comp instanceof JPanel panel) {
                updateComponentTextColor(panel, textColor, subTextColor);
            } else if (comp instanceof JTextField textField) {
                textField.setForeground(textColor);
                textField.setBackground(darkMode ? new Color(60, 60, 60) : Color.WHITE);
                textField.setCaretColor(textColor);
            } else if (comp instanceof JPasswordField passwordField) {
                passwordField.setForeground(textColor);
                passwordField.setBackground(darkMode ? new Color(60, 60, 60) : Color.WHITE);
                passwordField.setCaretColor(textColor);
            } else if (comp instanceof JTable table) {
                table.setForeground(textColor);
                table.setBackground(darkMode ? new Color(60, 60, 60) : Color.WHITE);
                table.getTableHeader().setForeground(textColor);
                table.getTableHeader().setBackground(darkMode ? new Color(70, 70, 70) : new Color(240, 240, 240));
            }
        }
    }
}
