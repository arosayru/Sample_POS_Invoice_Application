package com.pos.util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

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
            frame.pack();
            frame.setSize(400, 400);
            frame.setLocationRelativeTo(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setButtonIcon(JButton button, String resourcePath) {
        try {
            java.net.URL iconURL = ThemeManager.class.getResource(resourcePath);
            if (iconURL != null) {
                button.setIcon(new ImageIcon(iconURL));
            } else {
                System.err.println("Icon not found: " + resourcePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
