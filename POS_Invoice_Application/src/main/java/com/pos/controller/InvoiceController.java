package com.pos.controller;

import com.pos.dao.InvoiceDAO;
import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;
import com.pos.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class InvoiceController {
    private final InvoiceDAO invoiceDAO;

    public InvoiceController() {
        this.invoiceDAO = new InvoiceDAO();
    }

    public int saveInvoice(Invoice invoice) throws SQLException {
        if (invoice.getItems() == null || invoice.getItems().isEmpty()) {
            throw new IllegalArgumentException("Invoice must have at least one item!");
        }

        int invoiceId;
        try (Connection con = DBConnection.getConnection()) {
            try {
                con.setAutoCommit(false);

                invoiceId = invoiceDAO.saveInvoice(con, invoice);

                invoiceDAO.saveInvoiceItems(con, invoiceId, invoice.getItems());

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }

        return invoiceId;
    }

    public void saveInvoiceItems(int invoiceId, List<InvoiceItem> items) throws SQLException {
        if (invoiceId <= 0) throw new IllegalArgumentException("Invalid invoice ID!");
        invoiceDAO.saveInvoiceItems(invoiceId, items);
    }
}
