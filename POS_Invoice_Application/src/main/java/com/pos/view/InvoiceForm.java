package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.dao.InvoiceDAO;
import com.pos.dao.ItemDAO;
import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;
import com.pos.model.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceForm extends JFrame {
    private JComboBox<String> cmbBillingType;
    private JTextField txtDiscount;
    private JLabel lblSubtotal, lblTotal;
    private JTable tblItems;
    private DefaultTableModel tableModel;

    private List<Item> availableItems;

    public InvoiceForm() {
        setTitle("Create Invoice");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        // Top panel
        JPanel top = new JPanel(new FlowLayout());
        cmbBillingType = new JComboBox<>(new String[]{"Retail", "Wholesale"});
        txtDiscount = new JTextField(5);
        top.add(new JLabel("Billing Type:"));
        top.add(cmbBillingType);
        top.add(new JLabel("Discount:"));
        top.add(txtDiscount);
        add(top, BorderLayout.NORTH);

        // Center table
        tableModel = new DefaultTableModel(new Object[]{"Item ID","Item Name","Qty","Price","Total"}, 0);
        tblItems = new JTable(tableModel);
        add(new JScrollPane(tblItems), BorderLayout.CENTER);

        // Bottom panel
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblSubtotal = new JLabel("Subtotal: 0.00");
        lblTotal = new JLabel("Total: 0.00");
        JButton btnAddItem = new JButton("Add Item");
        JButton btnSave = new JButton("Save Invoice");
        bottom.add(lblSubtotal);
        bottom.add(lblTotal);
        bottom.add(btnAddItem);
        bottom.add(btnSave);
        add(bottom, BorderLayout.SOUTH);

        // Load items
        loadAvailableItems();

        // Button actions
        btnAddItem.addActionListener(e -> addItemToInvoice());
        btnSave.addActionListener(e -> saveInvoice());

        setVisible(true);
    }

    private void loadAvailableItems() {
        try {
            availableItems = new ItemDAO().getAllItems();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load items: " + e.getMessage());
        }
    }

    private void addItemToInvoice() {
        if (availableItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items available!");
            return;
        }

        String[] itemNames = availableItems.stream().map(Item::getItemName).toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(this, "Select Item:", "Add Item",
                JOptionPane.PLAIN_MESSAGE, null, itemNames, itemNames[0]);

        if (selected == null) return;

        Item item = availableItems.stream().filter(i -> i.getItemName().equals(selected)).findFirst().orElse(null);
        if (item == null) return;

        String qtyStr = JOptionPane.showInputDialog(this, "Enter Quantity:");
        int qty = Integer.parseInt(qtyStr);
        double price = (cmbBillingType.getSelectedItem().equals("Wholesale")) ? item.getWholesalePrice() : item.getRetailPrice();
        double total = qty * price;

        tableModel.addRow(new Object[]{item.getId(), item.getItemName(), qty, price, total});
        recalcTotals();
    }

    private void recalcTotals() {
        double subtotal = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            subtotal += (double) tableModel.getValueAt(i, 4);
        }
        lblSubtotal.setText(String.format("Subtotal: %.2f", subtotal));

        double discount = txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());
        double total = subtotal - discount;
        lblTotal.setText(String.format("Total: %.2f", total));
    }

    private void saveInvoice() {
        try {
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No items added!");
                return;
            }

            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber("INV-" + UUID.randomUUID().toString().substring(0, 8));
            invoice.setBillingType(cmbBillingType.getSelectedItem().toString());
            invoice.setDiscount(txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText()));

            double subtotal = 0;
            List<InvoiceItem> itemList = new ArrayList<>();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                InvoiceItem ii = new InvoiceItem();
                ii.setItemId((int) tableModel.getValueAt(i, 0));
                ii.setItemName((String) tableModel.getValueAt(i, 1));
                ii.setQuantity((int) tableModel.getValueAt(i, 2));
                ii.setPrice((double) tableModel.getValueAt(i, 3));
                ii.setTotal((double) tableModel.getValueAt(i, 4));
                subtotal += ii.getTotal();
                itemList.add(ii);
            }

            invoice.setSubtotal(subtotal);
            invoice.setTotal(subtotal - invoice.getDiscount());
            invoice.setStatus("Active");
            invoice.setItems(itemList);

            InvoiceDAO dao = new InvoiceDAO();
            int invoiceId = dao.saveInvoice(invoice);
            dao.saveInvoiceItems(invoiceId, itemList);

            JOptionPane.showMessageDialog(this, "Invoice saved successfully!");
            tableModel.setRowCount(0);
            recalcTotals();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving invoice: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new InvoiceForm();
    }
}
