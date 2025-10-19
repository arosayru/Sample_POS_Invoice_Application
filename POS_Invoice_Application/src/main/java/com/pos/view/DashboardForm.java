package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardForm extends JFrame {
    private JLabel lblDate, lblTime;
    private final Timer timer;

    public DashboardForm() {
        FlatLightLaf.setup();
        initUI();

        timer = new Timer(1000, e -> updateDateTime());
        timer.start();

        setVisible(true);
    }

    private void initUI() {
        setTitle("Dashboard - POS System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblThemeText = new JLabel("Light Theme");
        lblThemeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        themePanel.setOpaque(false);
        JLabel lblThemeSwitch = new JLabel("Light/Dark Theme");
        JToggleButton toggleTheme = new JToggleButton();
        toggleTheme.setPreferredSize(new Dimension(50, 25));
        toggleTheme.addActionListener(e -> ThemeManager.toggleTheme(this, new JButton()));
        themePanel.add(new JLabel("🌤️"));
        themePanel.add(lblThemeSwitch);
        themePanel.add(toggleTheme);

        topBar.add(lblThemeText, BorderLayout.WEST);
        topBar.add(themePanel, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        JButton btnNewInvoiceLeft = createSideButton("New Invoice");
        JButton btnItemManagerLeft = createSideButton("Item Manager");
        JButton btnHistoryLeft = createSideButton("Invoice History");

        btnNewInvoiceLeft.addActionListener(e -> openInvoice());
        btnItemManagerLeft.addActionListener(e -> openItems());
        btnHistoryLeft.addActionListener(e -> openHistory());

        leftPanel.add(btnNewInvoiceLeft);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(btnItemManagerLeft);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(btnHistoryLeft);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.3;
        contentPanel.add(leftPanel, gbc);

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setForeground(new Color(210, 210, 210));
        gbc.gridx = 1;
        gbc.weightx = 0.02;
        contentPanel.add(sep, gbc);

        JPanel rightPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        rightPanel.setOpaque(false);

        JLabel lblDashTitle = new JLabel("Dashboard", SwingConstants.CENTER);
        lblDashTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblDashTitle.setForeground(Color.BLACK);

        JPanel dashTitlePanel = new JPanel(new BorderLayout());
        dashTitlePanel.setOpaque(false);
        dashTitlePanel.add(lblDashTitle, BorderLayout.NORTH);

        JButton btnCreateInvoice = createMainButton("CREATE NEW INVOICE", new Color(100, 181, 246));
        JButton btnManageItems = createMainButton("MANAGE ITEMS", new Color(129, 199, 132));
        JButton btnViewHistory = createMainButton("VIEW HISTORY", new Color(255, 183, 77));

        btnCreateInvoice.addActionListener(e -> openInvoice());
        btnManageItems.addActionListener(e -> openItems());
        btnViewHistory.addActionListener(e -> openHistory());

        rightPanel.add(btnCreateInvoice);
        rightPanel.add(btnManageItems);
        rightPanel.add(btnViewHistory);

        JPanel rightContainer = new JPanel(new BorderLayout());
        rightContainer.setOpaque(false);
        rightContainer.add(dashTitlePanel, BorderLayout.NORTH);
        rightContainer.add(rightPanel, BorderLayout.CENTER);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.68;
        contentPanel.add(rightContainer, gbc);

        add(contentPanel, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomBar.setOpaque(false);

        lblDate = new JLabel();
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        lblTime = new JLabel();
        lblTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        bottomBar.add(new JLabel("Date:"));
        bottomBar.add(lblDate);
        bottomBar.add(new JLabel("Time:"));
        bottomBar.add(lblTime);

        add(bottomBar, BorderLayout.SOUTH);

        updateDateTime();
    }

    private JButton createSideButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createMainButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateDateTime() {
        Date now = new Date();
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(now));
        lblTime.setText(new SimpleDateFormat("HH:mm:ss").format(now));
    }

    private void openInvoice() {
        dispose();
        new InvoiceForm().setVisible(true);
    }

    private void openItems() {
        dispose();
        new ItemForm().setVisible(true);
    }

    private void openHistory() {
        dispose();
        new InvoiceHistoryForm().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardForm::new);
    }
}
