package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.controller.ItemController;
import com.pos.model.Item;
import com.pos.util.ThemeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ItemForm extends JFrame {
    private JTextField txtCode, txtName, txtCategory, txtCost, txtWholesale, txtRetail;
    private JButton btnSave, btnTheme, btnUpdate, btnDelete;
    private JTable tblItems;
    private DefaultTableModel tableModel;

    private final ItemController itemController;
    private int selectedItemId = -1;

    public ItemForm() {
        itemController = new ItemController();

        setTitle("Item Management");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnTheme = new JButton();
        btnTheme.setFocusPainted(false);
        btnTheme.setBorderPainted(false);
        btnTheme.setContentAreaFilled(false);
        btnTheme.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnTheme.setPreferredSize(new Dimension(32, 32));
        btnTheme.setToolTipText("Toggle Theme");

        java.net.URL iconURL = getClass().getResource("/icons/sun.png");
        if (iconURL != null) btnTheme.setIcon(new ImageIcon(iconURL));
        btnTheme.addActionListener(e -> ThemeManager.toggleTheme(this, btnTheme));

        top.add(btnTheme);
        add(top, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

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

        btnSave = new JButton("Add Item");
        btnSave.addActionListener(e -> saveItem());
        btnUpdate = new JButton("Update Item");
        btnUpdate.addActionListener(e -> updateItem());
        btnDelete = new JButton("Delete Item");
        btnDelete.addActionListener(e -> deleteItem());

        formPanel.add(btnSave);
        formPanel.add(btnUpdate);
        formPanel.add(new JLabel());
        formPanel.add(btnDelete);

        add(formPanel, BorderLayout.NORTH);

        // Table for item list
        tableModel = new DefaultTableModel(new Object[]{"ID", "Code", "Name", "Category", "Cost", "Wholesale", "Retail", "Status"}, 0);
        tblItems = new JTable(tableModel);
        add(new JScrollPane(tblItems), BorderLayout.CENTER);

        // When clicking on a row, fill data into the form for editing
        tblItems.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblItems.getSelectedRow() != -1) {
                selectedItemId = (int) tableModel.getValueAt(tblItems.getSelectedRow(), 0);
                txtCode.setText((String) tableModel.getValueAt(tblItems.getSelectedRow(), 1));
                txtName.setText((String) tableModel.getValueAt(tblItems.getSelectedRow(), 2));
                txtCategory.setText((String) tableModel.getValueAt(tblItems.getSelectedRow(), 3));
                txtCost.setText(String.valueOf(tableModel.getValueAt(tblItems.getSelectedRow(), 4)));
                txtWholesale.setText(String.valueOf(tableModel.getValueAt(tblItems.getSelectedRow(), 5)));
                txtRetail.setText(String.valueOf(tableModel.getValueAt(tblItems.getSelectedRow(), 6)));
            }
        });

        loadItems();
        setVisible(true);
    }

    private void loadItems() {
        try {
            tableModel.setRowCount(0);
            List<Item> items = itemController.getAllItems();
            for (Item item : items) {
                tableModel.addRow(new Object[]{
                        item.getId(), item.getItemCode(), item.getItemName(),
                        item.getCategory(), item.getCost(),
                        item.getWholesalePrice(), item.getRetailPrice(),
                        item.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load items: " + e.getMessage());
        }
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

            itemController.addItem(item);
            JOptionPane.showMessageDialog(this, "Item added successfully!");
            clearForm();
            loadItems();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void updateItem() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.");
            return;
        }

        try {
            Item item = new Item();
            item.setId(selectedItemId);
            item.setItemCode(txtCode.getText());
            item.setItemName(txtName.getText());
            item.setCategory(txtCategory.getText());
            item.setCost(Double.parseDouble(txtCost.getText()));
            item.setWholesalePrice(Double.parseDouble(txtWholesale.getText()));
            item.setRetailPrice(Double.parseDouble(txtRetail.getText()));
            item.setStatus("Active");

            itemController.updateItem(item);
            JOptionPane.showMessageDialog(this, "Item updated successfully!");
            clearForm();
            loadItems();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void deleteItem() {
        if (selectedItemId == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this item?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                itemController.deleteItem(selectedItemId);
                JOptionPane.showMessageDialog(this, "Item deleted successfully!");
                clearForm();
                loadItems();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error deleting item: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        txtCode.setText("");
        txtName.setText("");
        txtCategory.setText("");
        txtCost.setText("");
        txtWholesale.setText("");
        txtRetail.setText("");
        selectedItemId = -1;
        tblItems.clearSelection();
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new ItemForm();
    }
}
