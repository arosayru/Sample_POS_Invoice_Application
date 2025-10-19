package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.controller.ItemController;
import com.pos.model.Item;
import com.pos.util.ThemeManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ItemForm extends JFrame {
    private JTextField txtSearch;
    private JButton btnAddItem, btnBack;
    private JTable tblItems;
    private DefaultTableModel tableModel;
    private List<Item> items;
    private final ItemController itemController;

    public ItemForm() {
        itemController = new ItemController();

        FlatLightLaf.setup();
        initUI();
        setVisible(true);
        loadItems();
    }

    private void initUI() {
        setTitle("Item Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(10, 10));

        // ===== TOP PANEL =====
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(15, 20, 10, 20));

        // Back Button
        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            dispose();
            new DashboardForm().setVisible(true);
        });
        topPanel.add(btnBack, BorderLayout.WEST);

        // Theme Toggle Panel
        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        themePanel.setOpaque(false);
        JLabel lblThemeIcon = new JLabel("🌤️");
        lblThemeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        JLabel lblThemeText = new JLabel("Light / Dark Theme");
        lblThemeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JToggleButton toggleTheme = new JToggleButton();
        toggleTheme.setPreferredSize(new Dimension(50, 25));
        toggleTheme.setFocusable(false);
        toggleTheme.addActionListener(e -> ThemeManager.toggleTheme(this, new JButton()));
        themePanel.add(lblThemeIcon);
        themePanel.add(lblThemeText);
        themePanel.add(toggleTheme);
        topPanel.add(themePanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER PANEL =====
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 60, 40, 60));

        JLabel lblTitle = new JLabel("Item Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        centerPanel.add(lblTitle, BorderLayout.NORTH);

        // ===== SEARCH + ADD PANEL =====
        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setOpaque(false);

        // Search Field
        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSearch.setPreferredSize(new Dimension(350, 35));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 12, 5, 12)
        ));
        txtSearch.setForeground(Color.GRAY);
        txtSearch.setText("🔍 Search by Item Name, Code, or Category");

        txtSearch.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().equals("🔍 Search by Item Name, Code, or Category")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText("🔍 Search by Item Name, Code, or Category");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String query = txtSearch.getText().trim().toLowerCase();
                if (query.isEmpty() || query.equals("🔍 search by item name, code, or category")) {
                    refreshTable(items);
                    return;
                }

                List<Item> filtered = items.stream()
                        .filter(item -> item.getItemName().toLowerCase().contains(query)
                                || item.getItemCode().toLowerCase().contains(query)
                                || (item.getCategory() != null && item.getCategory().toLowerCase().contains(query)))
                        .toList();
                refreshTable(filtered);
            }
        });

        // Add Item Button
        btnAddItem = new JButton("+ Add New Item");
        btnAddItem.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnAddItem.setBackground(new Color(67, 160, 71));
        btnAddItem.setForeground(Color.WHITE);
        btnAddItem.setFocusPainted(false);
        btnAddItem.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnAddItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAddItem.addActionListener(e -> openAddItemDialog());

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnAddItem, BorderLayout.EAST);
        centerPanel.add(searchPanel, BorderLayout.CENTER);

        // ===== TABLE =====
        String[] columns = {"ID", "Code", "Name", "Category", "Cost", "Wholesale", "Retail", "Label", "Credit", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        tblItems = new JTable(tableModel);
        tblItems.setRowHeight(28);
        tblItems.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblItems.setGridColor(new Color(230, 230, 230));
        tblItems.setSelectionBackground(new Color(200, 230, 255));
        tblItems.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(tblItems);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(0, 450));

        centerPanel.add(scrollPane, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadItems() {
        try {
            items = itemController.getAllItems();
            refreshTable(items);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load items: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable(List<Item> list) {
        tableModel.setRowCount(0);
        for (Item item : list) {
            tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getItemCode(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getCost(),
                    item.getWholesalePrice(),
                    item.getRetailPrice(),
                    item.getLabelPrice(),
                    item.getCreditPrice(),
                    item.getStatus()
            });
        }
    }

    private void openAddItemDialog() {
        JDialog dialog = new JDialog(this, "Add New Item", true);
        dialog.setSize(450, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(new Color(245, 247, 250));
        dialog.setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel("Add New Item", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(15, 10, 5, 10));
        dialog.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        formPanel.setOpaque(false);

        JTextField txtCode = createInputField();
        JTextField txtName = createInputField();
        JTextField txtCategory = createInputField();
        JTextField txtCost = createInputField();
        JTextField txtWholesale = createInputField();
        JTextField txtRetail = createInputField();
        JTextField txtLabel = createInputField();
        JTextField txtCredit = createInputField();

        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Active", "Inactive"});

        formPanel.add(createLabel("Item Code:"));
        formPanel.add(txtCode);
        formPanel.add(createLabel("Item Name:"));
        formPanel.add(txtName);
        formPanel.add(createLabel("Category:"));
        formPanel.add(txtCategory);
        formPanel.add(createLabel("Cost:"));
        formPanel.add(txtCost);
        formPanel.add(createLabel("Wholesale Price:"));
        formPanel.add(txtWholesale);
        formPanel.add(createLabel("Retail Price:"));
        formPanel.add(txtRetail);
        formPanel.add(createLabel("Label Price:"));
        formPanel.add(txtLabel);
        formPanel.add(createLabel("Credit Price:"));
        formPanel.add(txtCredit);
        formPanel.add(createLabel("Status:"));
        formPanel.add(cmbStatus);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton btnSave = new JButton("Save Item");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSave.setBackground(new Color(33, 150, 243));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnCancel.setBackground(new Color(189, 189, 189));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            try {
                if (txtCode.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all required fields!");
                    return;
                }

                Item newItem = new Item();
                newItem.setItemCode(txtCode.getText().trim());
                newItem.setItemName(txtName.getText().trim());
                newItem.setCategory(txtCategory.getText().trim());
                newItem.setCost(Double.parseDouble(txtCost.getText().trim()));
                newItem.setWholesalePrice(Double.parseDouble(txtWholesale.getText().trim()));
                newItem.setRetailPrice(Double.parseDouble(txtRetail.getText().trim()));
                newItem.setLabelPrice(Double.parseDouble(txtLabel.getText().trim()));
                newItem.setCreditPrice(Double.parseDouble(txtCredit.getText().trim()));
                newItem.setStatus((String) cmbStatus.getSelectedItem());

                itemController.addItem(newItem);
                JOptionPane.showMessageDialog(this, "Item added successfully!");
                loadItems();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.getRootPane().setDefaultButton(btnSave);
        dialog.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(ItemForm::new);
    }
}
