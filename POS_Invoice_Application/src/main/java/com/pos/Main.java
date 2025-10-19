package com.pos;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.view.SplashScreen;
import com.pos.view.DashboardForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            new SplashScreen();
            //new DashboardForm();
        });
    }
}