package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.dao.InvoiceHistoryDAO;
import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class InvoiceHistoryForm extends JFrame {
    private JTable tblInvoices;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnSearch, btnCancel, btnViewDetails;

    public InvoiceHistoryForm() {
        setTitle("Invoice History");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));
        setLocationRelativeTo(null);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtSearch = new JTextField(20);
        btnSearch = new JButton("Search");
        top.add(new JLabel("Invoice Number:"));
        top.add(txtSearch);
        top.add(btnSearch);
        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID","Invoice No","Billing Type","Subtotal","Discount","Total","Status"}, 0);
        tblInvoices = new JTable(tableModel);
        add(new JScrollPane(tblInvoices), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnViewDetails = new JButton("View Details");
        btnCancel = new JButton("Cancel Invoice");
        bottom.add(btnViewDetails);
        bottom.add(btnCancel);
        add(bottom, BorderLayout.SOUTH);

        loadInvoices();

        btnSearch.addActionListener(e -> searchInvoice());
        btnViewDetails.addActionListener(e -> viewDetails());
        btnCancel.addActionListener(e -> cancelInvoice());

        setVisible(true);
    }

    private void loadInvoices() {
        try {
            tableModel.setRowCount(0);
            List<Invoice> list = new InvoiceHistoryDAO().getAllInvoices();
            for (Invoice inv : list) {
                tableModel.addRow(new Object[]{
                        inv.getId(), inv.getInvoiceNumber(), inv.getBillingType(),
                        inv.getSubtotal(), inv.getDiscount(), inv.getTotal(), inv.getStatus()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load invoices: " + e.getMessage());
        }
    }

    private void searchInvoice() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadInvoices();
            return;
        }
        try {
            Invoice inv = new InvoiceHistoryDAO().getInvoiceByNumber(keyword);
            tableModel.setRowCount(0);
            if (inv != null) {
                tableModel.addRow(new Object[]{
                        inv.getId(), inv.getInvoiceNumber(), inv.getBillingType(),
                        inv.getSubtotal(), inv.getDiscount(), inv.getTotal(), inv.getStatus()
                });
            } else {
                JOptionPane.showMessageDialog(this, "No invoice found for: " + keyword);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void viewDetails() {
        int row = tblInvoices.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an invoice to view!");
            return;
        }

        int invoiceId = (int) tableModel.getValueAt(row, 0);
        String invoiceNo = (String) tableModel.getValueAt(row, 1);

        try {
            Invoice inv = new InvoiceHistoryDAO().getInvoiceByNumber(invoiceNo);
            if (inv == null) {
                JOptionPane.showMessageDialog(this, "Invoice not found!");
                return;
            }

            JTextArea area = new JTextArea(15, 40);
            area.setEditable(false);
            area.append("Invoice Number: " + inv.getInvoiceNumber() + "\n");
            area.append("Billing Type: " + inv.getBillingType() + "\n");
            area.append("Subtotal: " + inv.getSubtotal() + "\n");
            area.append("Discount: " + inv.getDiscount() + "\n");
            area.append("Total: " + inv.getTotal() + "\n");
            area.append("Status: " + inv.getStatus() + "\n\n");
            area.append("Items:\n-----------------------------------\n");

            for (InvoiceItem item : inv.getItems()) {
                area.append(item.getItemName() + " | Qty: " + item.getQuantity() +
                        " | Price: " + item.getPrice() +
                        " | Total: " + item.getTotal() + "\n");
            }

            JScrollPane scrollPane = new JScrollPane(area);
            JOptionPane.showMessageDialog(this, scrollPane, "Invoice Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading details: " + e.getMessage());
        }
    }

    private void cancelInvoice() {
        int row = tblInvoices.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an invoice to cancel!");
            return;
        }

        int invoiceId = (int) tableModel.getValueAt(row, 0);
        String status = (String) tableModel.getValueAt(row, 6);
        if (status.equals("Cancelled")) {
            JOptionPane.showMessageDialog(this, "This invoice is already cancelled!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this invoice?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new InvoiceHistoryDAO().cancelInvoice(invoiceId);
                JOptionPane.showMessageDialog(this, "Invoice cancelled successfully!");
                loadInvoices();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error cancelling invoice: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        new InvoiceHistoryForm();
    }
}
