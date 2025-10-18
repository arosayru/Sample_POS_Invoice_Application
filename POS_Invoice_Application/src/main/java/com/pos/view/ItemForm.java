package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.controller.ItemController;
import com.pos.model.Item;
import com.pos.util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ItemForm extends JFrame {
    private JTextField txtCode, txtName, txtCategory, txtCost, txtWholesale, txtRetail;
    private JButton btnSave, btnTheme;
    private final ItemController itemController;

    public ItemForm() {
        itemController = new ItemController();

        setTitle("Item Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // top panel
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnTheme = new JButton();
        btnTheme.setFocusPainted(false);
        btnTheme.setBorderPainted(false);
        btnTheme.setContentAreaFilled(false);
        btnTheme.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTheme.setPreferredSize(new Dimension(32, 32));
        btnTheme.setToolTipText("Toggle Theme");

        // light or dark mode icon
        java.net.URL iconURL = getClass().getResource("/icons/sun.png");
        if (iconURL != null) {
            btnTheme.setIcon(new ImageIcon(iconURL));
        }

        btnTheme.addActionListener(e -> ThemeManager.toggleTheme(this, btnTheme));

        top.add(btnTheme);
        add(top, BorderLayout.NORTH);

        // form panel
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        formPanel.add(new JLabel("Item Code:"));
        txtCode = new JTextField();
        formPanel.add(txtCode);

        formPanel.add(new JLabel("Item Name:"));
        txtName = new JTextField();
        formPanel.add(txtName);

        formPanel.add(new JLabel("Category:"));
        txtCategory = new JTextField();
        formPanel.add(txtCategory);

        formPanel.add(new JLabel("Cost:"));
        txtCost = new JTextField();
        formPanel.add(txtCost);

        formPanel.add(new JLabel("Wholesale Price:"));
        txtWholesale = new JTextField();
        formPanel.add(txtWholesale);

        formPanel.add(new JLabel("Retail Price:"));
        txtRetail = new JTextField();
        formPanel.add(txtRetail);

        // Save Button
        btnSave = new JButton("Save Item");
        btnSave.addActionListener(e -> saveItem());
        formPanel.add(new JLabel());
        formPanel.add(btnSave);

        add(formPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void saveItem() {
        try {
            if (txtCode.getText().trim().isEmpty() ||
                    txtName.getText().trim().isEmpty() ||
                    txtCost.getText().trim().isEmpty() ||
                    txtWholesale.getText().trim().isEmpty() ||
                    txtRetail.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Item item = new Item();
            item.setItemCode(txtCode.getText().trim());
            item.setItemName(txtName.getText().trim());
            item.setCategory(txtCategory.getText().trim());
            item.setCost(Double.parseDouble(txtCost.getText().trim()));
            item.setWholesalePrice(Double.parseDouble(txtWholesale.getText().trim()));
            item.setRetailPrice(Double.parseDouble(txtRetail.getText().trim()));
            item.setStatus("Active");

            itemController.addItem(item);

            JOptionPane.showMessageDialog(this, "Item saved successfully!");
            clearForm();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for prices!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearForm() {
        txtCode.setText("");
        txtName.setText("");
        txtCategory.setText("");
        txtCost.setText("");
        txtWholesale.setText("");
        txtRetail.setText("");
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new ItemForm();
    }
}
