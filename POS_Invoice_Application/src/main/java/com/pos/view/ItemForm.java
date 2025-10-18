package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.dao.ItemDAO;
import com.pos.model.Item;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ItemForm extends JFrame {
    private JTextField txtCode, txtName, txtCategory, txtCost, txtWholesale, txtRetail;
    private JButton btnSave;

    public ItemForm() {
        setTitle("Item Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new GridLayout(0, 2, 10, 10));
        setLocationRelativeTo(null);

        add(new JLabel("Item Code:"));
        txtCode = new JTextField(); add(txtCode);

        add(new JLabel("Item Name:"));
        txtName = new JTextField(); add(txtName);

        add(new JLabel("Category:"));
        txtCategory = new JTextField(); add(txtCategory);

        add(new JLabel("Cost:"));
        txtCost = new JTextField(); add(txtCost);

        add(new JLabel("Wholesale Price:"));
        txtWholesale = new JTextField(); add(txtWholesale);

        add(new JLabel("Retail Price:"));
        txtRetail = new JTextField(); add(txtRetail);

        btnSave = new JButton("Save Item");
        btnSave.addActionListener(e -> saveItem());
        add(new JLabel());
        add(btnSave);

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
