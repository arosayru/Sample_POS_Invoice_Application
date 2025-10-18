package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.dao.ItemDAO;
import com.pos.model.Item;
import com.pos.util.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ItemForm extends JFrame {
    private JTextField txtCode, txtName, txtCategory, txtCost, txtWholesale, txtRetail;
    private JButton btnSave, btnTheme;

    public ItemForm() {
        setTitle("Item Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // ======= TOP PANEL (Theme toggle button) =======
        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnTheme = new JButton();
        btnTheme.setFocusPainted(false);
        btnTheme.setBorderPainted(false);
        btnTheme.setContentAreaFilled(false);
        btnTheme.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTheme.setPreferredSize(new Dimension(32, 32));
        btnTheme.setToolTipText("Toggle Theme");

        // set initial icon (light mode)
        java.net.URL iconURL = getClass().getResource("/icons/sun.png");
        if (iconURL != null) {
            btnTheme.setIcon(new ImageIcon(iconURL));
        }

        btnTheme.addActionListener(e -> ThemeManager.toggleTheme(this, btnTheme));

        top.add(btnTheme);
        add(top, BorderLayout.NORTH);

        // ======= CENTER FORM PANEL =======
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        formPanel.add(new JLabel("Item Code:"));
        txtCode = new JTextField(); formPanel.add(txtCode);

        formPanel.add(new JLabel("Item Name:"));
        txtName = new JTextField(); formPanel.add(txtName);

        formPanel.add(new JLabel("Category:"));
        txtCategory = new JTextField(); formPanel.add(txtCategory);

        formPanel.add(new JLabel("Cost:"));
        txtCost = new JTextField(); formPanel.add(txtCost);

        formPanel.add(new JLabel("Wholesale Price:"));
        txtWholesale = new JTextField(); formPanel.add(txtWholesale);

        formPanel.add(new JLabel("Retail Price:"));
        txtRetail = new JTextField(); formPanel.add(txtRetail);

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
            Item item = new Item();
            item.setItemCode(txtCode.getText());
            item.setItemName(txtName.getText());
            item.setCategory(txtCategory.getText());
            item.setCost(Double.parseDouble(txtCost.getText()));
            item.setWholesalePrice(Double.parseDouble(txtWholesale.getText()));
            item.setRetailPrice(Double.parseDouble(txtRetail.getText()));
            item.setStatus("Active");

            new ItemDAO().addItem(item);
            JOptionPane.showMessageDialog(this, "Item saved successfully!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for prices!");
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new ItemForm();
    }
}
