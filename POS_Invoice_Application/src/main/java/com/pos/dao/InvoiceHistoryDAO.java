package com.pos.dao;

import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;
import com.pos.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceHistoryDAO {
    public List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices ORDER BY date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Invoice inv = new Invoice();
                inv.setId(rs.getInt("id"));
                inv.setInvoiceNumber(rs.getString("invoice_number"));
                inv.setBillingType(rs.getString("billing_type"));
                inv.setSubtotal(rs.getDouble("subtotal"));
                inv.setDiscount(rs.getDouble("discount"));
                inv.setTotal(rs.getDouble("total"));
                inv.setStatus(rs.getString("status"));
                list.add(inv);
            }
        }
        return list;
    }

    public Invoice getInvoiceByNumber(String invoiceNumber) throws SQLException {
        Invoice invoice = null;
        String sql = "SELECT * FROM invoices WHERE invoice_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, invoiceNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    invoice = new Invoice();
                    invoice.setId(rs.getInt("id"));
                    invoice.setInvoiceNumber(rs.getString("invoice_number"));
                    invoice.setBillingType(rs.getString("billing_type"));
                    invoice.setSubtotal(rs.getDouble("subtotal"));
                    invoice.setDiscount(rs.getDouble("discount"));
                    invoice.setTotal(rs.getDouble("total"));
                    invoice.setStatus(rs.getString("status"));
                }
            }
        }
        if (invoice != null) {
            invoice.setItems(getInvoiceItems(invoice.getId()));
        }
        return invoice;
    }

    public List<InvoiceItem> getInvoiceItems(int invoiceId) throws SQLException {
        List<InvoiceItem> list = new ArrayList<>();
        String sql = """
                SELECT ii.id, i.item_name, ii.quantity, ii.price, ii.total 
                FROM invoice_items ii 
                JOIN items i ON ii.item_id = i.id 
                WHERE ii.invoice_id = ?
                """;
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InvoiceItem item = new InvoiceItem();
                    item.setId(rs.getInt("id"));
                    item.setItemName(rs.getString("item_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setPrice(rs.getDouble("price"));
                    item.setTotal(rs.getDouble("total"));
                    list.add(item);
                }
            }
        }
        return list;
    }

    public void cancelInvoice(int id) throws SQLException {
        String sql = "UPDATE invoices SET status = 'Cancelled' WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // --- Filter invoices by date range ---
    public List<Invoice> getInvoicesByDateRange(java.time.LocalDate from, java.time.LocalDate to) throws SQLException {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE DATE(date) BETWEEN ? AND ? ORDER BY date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invoice inv = new Invoice();
                    inv.setId(rs.getInt("id"));
                    inv.setInvoiceNumber(rs.getString("invoice_number"));
                    inv.setBillingType(rs.getString("billing_type"));
                    inv.setSubtotal(rs.getDouble("subtotal"));
                    inv.setDiscount(rs.getDouble("discount"));
                    inv.setTotal(rs.getDouble("total"));
                    inv.setStatus(rs.getString("status"));
                    list.add(inv);
                }
            }
        }
        return list;
    }

}
