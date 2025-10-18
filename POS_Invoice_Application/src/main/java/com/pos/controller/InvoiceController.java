package com.pos.controller;

import com.pos.dao.InvoiceDAO;
import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;

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
        return invoiceDAO.saveInvoice(invoice);
    }

    public void saveInvoiceItems(int invoiceId, List<InvoiceItem> items) throws SQLException {
        if (invoiceId <= 0) throw new IllegalArgumentException("Invalid invoice ID!");
        invoiceDAO.saveInvoiceItems(invoiceId, items);
    }
}
