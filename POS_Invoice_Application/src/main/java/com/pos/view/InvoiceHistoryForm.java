package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.dao.InvoiceHistoryDAO;
import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;
import com.pos.util.PDFGenerator;
import com.pos.util.ThemeManager;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InvoiceHistoryForm extends JFrame {
    private JTable tblInvoices;
    private DefaultTableModel tableModel;
    private JTextField txtInvoiceSearch;
    private JDateChooser dateFromChooser;
    private JDateChooser dateToChooser;
    private JButton btnSearch;
    private JButton btnBack;
    private JToggleButton toggleTheme;

    public InvoiceHistoryForm() {
        FlatLightLaf.setup();
        initUI();
        loadInvoices();
        setVisible(true);
    }

    private void initUI() {
        setTitle("Invoice History");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            dispose();
            new DashboardForm().setVisible(true);
        });
        topPanel.add(btnBack, BorderLayout.WEST);

        JLabel lblTitle = new JLabel("Invoice History", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        topPanel.add(lblTitle, BorderLayout.CENTER);

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        themePanel.setOpaque(false);
        JLabel lblThemeIcon = new JLabel("🌤️");
        lblThemeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        JLabel lblThemeText = new JLabel("Light/Dark Theme");
        lblThemeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        toggleTheme = new JToggleButton();
        toggleTheme.setPreferredSize(new Dimension(50, 25));
        toggleTheme.setFocusable(false);
        toggleTheme.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleTheme.addActionListener(e -> ThemeManager.toggleTheme(this, new JButton()));
        themePanel.add(lblThemeIcon);
        themePanel.add(lblThemeText);
        themePanel.add(toggleTheme);
        topPanel.add(themePanel, BorderLayout.EAST);

        JPanel filterPanel = new JPanel(new GridBagLayout());
        filterPanel.setOpaque(false);
        filterPanel.setBorder(new EmptyBorder(10, 24, 10, 24));
        GridBagConstraints fgb = new GridBagConstraints();
        fgb.insets = new Insets(6, 6, 6, 6);
        fgb.fill = GridBagConstraints.HORIZONTAL;

        fgb.gridx = 0;
        fgb.gridy = 0;
        fgb.weightx = 0;
        filterPanel.add(new JLabel("From Date:"), fgb);

        dateFromChooser = new JDateChooser();
        dateFromChooser.setDateFormatString("yyyy-MM-dd");
        dateFromChooser.setPreferredSize(new Dimension(160, 30));
        fgb.gridx = 1;
        fgb.weightx = 0.15;
        filterPanel.add(dateFromChooser, fgb);

        fgb.gridx = 2;
        fgb.weightx = 0;
        filterPanel.add(new JLabel("To Date:"), fgb);

        dateToChooser = new JDateChooser();
        dateToChooser.setDateFormatString("yyyy-MM-dd");
        dateToChooser.setPreferredSize(new Dimension(160, 30));
        fgb.gridx = 3;
        fgb.weightx = 0.15;
        filterPanel.add(dateToChooser, fgb);

        fgb.gridx = 4;
        fgb.weightx = 0;
        filterPanel.add(new JLabel("Invoice Number:"), fgb);

        txtInvoiceSearch = new JTextField();
        txtInvoiceSearch.setPreferredSize(new Dimension(220, 30));
        txtInvoiceSearch.setToolTipText("Enter invoice number and press Search");
        fgb.gridx = 5;
        fgb.weightx = 0.25;
        filterPanel.add(txtInvoiceSearch, fgb);

        btnSearch = new JButton("Search");
        btnSearch.setFocusPainted(false);
        btnSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSearch.addActionListener(e -> searchInvoice());
        fgb.gridx = 6;
        fgb.weightx = 0;
        filterPanel.add(btnSearch, fgb);

        JPanel headerContainer = new JPanel(new BorderLayout());
        headerContainer.setOpaque(false);
        headerContainer.add(topPanel, BorderLayout.NORTH);
        headerContainer.add(filterPanel, BorderLayout.SOUTH);
        add(headerContainer, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{
                "ID", "Invoice No", "Billing Type", "Subtotal", "Discount", "Total", "Status", "Actions"
        }, 0);

        tblInvoices = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };
        tblInvoices.setRowHeight(36);
        tblInvoices.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblInvoices.setSelectionBackground(new Color(200, 230, 255));
        tblInvoices.setGridColor(new Color(230, 230, 230));

        int actionsCol = tableModel.getColumnCount() - 1;
        tblInvoices.getColumnModel().getColumn(actionsCol).setCellRenderer(new ActionCellRenderer());
        tblInvoices.getColumnModel().getColumn(actionsCol).setCellEditor(new ActionCellEditor());

        JScrollPane sp = new JScrollPane(tblInvoices);
        sp.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        add(sp, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        bottom.setOpaque(false);

        JButton btnViewDetails = new JButton("View Details");
        btnViewDetails.setBackground(new Color(33, 150, 243));
        btnViewDetails.setForeground(Color.WHITE);
        btnViewDetails.setFocusPainted(false);
        btnViewDetails.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnViewDetails.addActionListener(e -> viewDetailsSelected());

        JButton btnCancel = new JButton("Cancel Invoice");
        btnCancel.setBackground(new Color(233, 30, 99));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> cancelSelected());

        bottom.add(btnViewDetails);
        bottom.add(btnCancel);
        add(bottom, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }

    private void loadInvoices() {
        try {
            tableModel.setRowCount(0);
            InvoiceHistoryDAO dao = new InvoiceHistoryDAO();
            List<Invoice> list = dao.getAllInvoices();
            for (Invoice inv : list) {
                tableModel.addRow(new Object[]{
                        inv.getId(),
                        inv.getInvoiceNumber(),
                        inv.getBillingType(),
                        inv.getSubtotal(),
                        inv.getDiscount(),
                        inv.getTotal(),
                        inv.getStatus(),
                        "Actions"
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load invoices: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchInvoice() {
        String keyword = txtInvoiceSearch.getText().trim();

        Date from = dateFromChooser.getDate();
        Date to = dateToChooser.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("Search clicked | From: " + (from != null ? sdf.format(from) : "none")
                + " | To: " + (to != null ? sdf.format(to) : "none"));

        if (keyword.isEmpty()) {
            loadInvoices();
            return;
        }

        try {
            Invoice inv = new InvoiceHistoryDAO().getInvoiceByNumber(keyword);
            tableModel.setRowCount(0);
            if (inv != null) {
                tableModel.addRow(new Object[]{
                        inv.getId(),
                        inv.getInvoiceNumber(),
                        inv.getBillingType(),
                        inv.getSubtotal(),
                        inv.getDiscount(),
                        inv.getTotal(),
                        inv.getStatus(),
                        "Actions"
                });
            } else {
                JOptionPane.showMessageDialog(this, "No invoice found for: " + keyword);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error searching invoice: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewDetailsSelected() {
        int row = tblInvoices.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an invoice to view!");
            return;
        }
        int modelRow = tblInvoices.convertRowIndexToModel(row);
        String invoiceNo = (String) tableModel.getValueAt(modelRow, 1);
        showInvoiceDetails(invoiceNo);
    }

    private void cancelSelected() {
        int row = tblInvoices.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an invoice to cancel!");
            return;
        }
        int modelRow = tblInvoices.convertRowIndexToModel(row);
        int invoiceId = (int) tableModel.getValueAt(modelRow, 0);
        String status = (String) tableModel.getValueAt(modelRow, 6);
        if ("Cancelled".equalsIgnoreCase(status)) {
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
                JOptionPane.showMessageDialog(this, "Error cancelling invoice: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showInvoiceDetails(String invoiceNo) {
        try {
            Invoice inv = new InvoiceHistoryDAO().getInvoiceByNumber(invoiceNo);
            if (inv == null) {
                JOptionPane.showMessageDialog(this, "Invoice not found!");
                return;
            }

            JTextArea area = new JTextArea(16, 48);
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

            int confirm = JOptionPane.showConfirmDialog(this, "Generate PDF for this invoice?", "Export", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                PDFGenerator.generateInvoicePDF(inv);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class ActionCellRenderer implements TableCellRenderer {
        private final JPanel panel;
        private final JButton btnView;
        private final JButton btnCancel;

        ActionCellRenderer() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
            panel.setOpaque(true);

            btnView = new JButton("View");
            btnView.setFocusable(false);
            btnView.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

            btnCancel = new JButton("Cancel");
            btnCancel.setFocusable(false);
            btnCancel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

            panel.add(btnView);
            panel.add(btnCancel);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? new Color(230, 245, 255) : Color.WHITE);
            return panel;
        }
    }

    private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
        private final JButton btnView = new JButton("View");
        private final JButton btnCancel = new JButton("Cancel");
        private int editingRow = -1;

        ActionCellEditor() {
            panel.setOpaque(true);

            btnView.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btnView.addActionListener(e -> {
                if (editingRow >= 0) {
                    int modelRow = tblInvoices.convertRowIndexToModel(editingRow);
                    String invoiceNo = (String) tableModel.getValueAt(modelRow, 1);
                    stopCellEditing();
                    showInvoiceDetails(invoiceNo);
                } else stopCellEditing();
            });

            btnCancel.addActionListener(e -> {
                if (editingRow >= 0) {
                    int modelRow = tblInvoices.convertRowIndexToModel(editingRow);
                    int invoiceId = (int) tableModel.getValueAt(modelRow, 0);
                    String status = (String) tableModel.getValueAt(modelRow, 6);
                    if ("Cancelled".equalsIgnoreCase(status)) {
                        JOptionPane.showMessageDialog(InvoiceHistoryForm.this, "This invoice is already cancelled!");
                        stopCellEditing();
                        return;
                    }
                    int confirm = JOptionPane.showConfirmDialog(InvoiceHistoryForm.this,
                            "Are you sure you want to cancel this invoice?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            new InvoiceHistoryDAO().cancelInvoice(invoiceId);
                            JOptionPane.showMessageDialog(InvoiceHistoryForm.this, "Invoice cancelled successfully!");
                            stopCellEditing();
                            loadInvoices();
                        } catch (SQLException ex) {
                            stopCellEditing();
                            JOptionPane.showMessageDialog(InvoiceHistoryForm.this, "Error cancelling: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else stopCellEditing();
                } else stopCellEditing();
            });

            panel.add(btnView);
            panel.add(btnCancel);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editingRow = row;
            panel.setBackground(new Color(230, 245, 255));
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "Actions";
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(InvoiceHistoryForm::new);
    }
}
