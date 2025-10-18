package com.pos.dao;

import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;
import com.pos.util.DBConnection;

import java.sql.*;
import java.util.List;

public class InvoiceDAO {
    public int saveInvoice(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (invoice_number, billing_type, subtotal, discount, total, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, invoice.getInvoiceNumber());
            ps.setString(2, invoice.getBillingType());
            ps.setDouble(3, invoice.getSubtotal());
            ps.setDouble(4, invoice.getDiscount());
            ps.setDouble(5, invoice.getTotal());
            ps.setString(6, invoice.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public void saveInvoiceItems(int invoiceId, List<InvoiceItem> items) throws SQLException {
        String sql = "INSERT INTO invoice_items (invoice_id, item_id, quantity, price, total) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (InvoiceItem i : items) {
                ps.setInt(1, invoiceId);
                ps.setInt(2, i.getItemId());
                ps.setInt(3, i.getQuantity());
                ps.setDouble(4, i.getPrice());
                ps.setDouble(5, i.getTotal());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
