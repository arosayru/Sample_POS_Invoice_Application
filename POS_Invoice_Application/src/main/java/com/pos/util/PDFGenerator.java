package com.pos.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class PDFGenerator {
    public static void generateInvoicePDF(Invoice invoice) {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setSelectedFile(new File(invoice.getInvoiceNumber() + ".pdf"));

            int result = chooser.showSaveDialog(null);
            if (result != JFileChooser.APPROVE_OPTION) return;

            String filePath = chooser.getSelectedFile().getAbsolutePath();
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Invoice #" + invoice.getInvoiceNumber()).setBold());
            document.add(new Paragraph("Billing Type: " + invoice.getBillingType()));
            document.add(new Paragraph("Status: " + invoice.getStatus()));
            document.add(new Paragraph("\n"));

            float[] columnWidths = {200, 100, 100, 100};
            Table table = new Table(columnWidths);
            table.addCell("Item Name");
            table.addCell("Quantity");
            table.addCell("Price");
            table.addCell("Total");

            for (InvoiceItem item : invoice.getItems()) {
                table.addCell(item.getItemName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.format("%.2f", item.getPrice()));
                table.addCell(String.format("%.2f", item.getTotal()));
            }

            document.add(table);
            document.add(new Paragraph("\nSubtotal: " + invoice.getSubtotal()));
            document.add(new Paragraph("Discount: " + invoice.getDiscount()));
            document.add(new Paragraph("Total: " + invoice.getTotal()));

            document.close();
            JOptionPane.showMessageDialog(null, "PDF generated successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error generating PDF: " + e.getMessage());
        }
    }
}
