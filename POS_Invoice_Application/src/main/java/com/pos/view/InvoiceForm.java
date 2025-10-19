package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.controller.InvoiceController;
import com.pos.controller.ItemController;
import com.pos.model.Invoice;
import com.pos.model.InvoiceItem;
import com.pos.model.Item;
import com.pos.util.ThemeManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InvoiceForm extends JFrame {
    private JComboBox<String> cmbBillingType;
    private JTextField txtDiscount;
    private JLabel lblSubtotal, lblTotal, lblInvoiceNo;
    private JTable tblItems;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnTheme, btnSave, btnPrint, btnNewInvoice, btnBack;

    private List<Item> availableItems;

    private final ItemController itemController;
    private final InvoiceController invoiceController;

    public InvoiceForm() {
        System.out.println("InvoiceForm constructor started");

        itemController = new ItemController();
        invoiceController = new InvoiceController();

        FlatLightLaf.setup();
        initUI();
        setVisible(true);
        loadAvailableItems();

        System.out.println("InvoiceForm constructor finished");
    }

    private void initUI() {
        setTitle("Invoice Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));

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

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        themePanel.setOpaque(false);

        JLabel lblThemeIcon = new JLabel("🌤️");
        lblThemeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));

        JLabel lblThemeText = new JLabel("Light/Dark Theme");
        lblThemeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JToggleButton toggleTheme = new JToggleButton();
        toggleTheme.setPreferredSize(new Dimension(50, 25));
        toggleTheme.setFocusable(false);
        toggleTheme.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggleTheme.addActionListener(e -> ThemeManager.toggleTheme(this, new JButton()));

        themePanel.add(lblThemeIcon);
        themePanel.add(lblThemeText);
        themePanel.add(toggleTheme);

        topPanel.add(themePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(themePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setOpaque(false);

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtSearch.setPreferredSize(new Dimension(0, 40));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.setToolTipText("Search by Item, Code, or Category");
        leftPanel.add(txtSearch, BorderLayout.NORTH);

        JPanel itemGrid = new JPanel(new GridLayout(0, 3, 15, 15));
        itemGrid.setBackground(new Color(245, 247, 250));

        JScrollPane scrollPane = new JScrollPane(itemGrid);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        infoPanel.setOpaque(false);

        lblInvoiceNo = new JLabel("INV-" + UUID.randomUUID().toString().substring(0, 8));
        lblInvoiceNo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(new JLabel("Invoice #:"));
        infoPanel.add(lblInvoiceNo);

        lblSubtotal = new JLabel("$0.00");
        infoPanel.add(new JLabel("Subtotal:"));
        infoPanel.add(lblSubtotal);

        txtDiscount = new JTextField("0");
        infoPanel.add(new JLabel("Discount:"));
        infoPanel.add(txtDiscount);

        rightPanel.add(infoPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Item ID", "Item Name", "Qty", "Price", "Total"}, 0);
        tblItems = new JTable(tableModel);
        tblItems.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblItems.setRowHeight(25);
        tblItems.removeColumn(tblItems.getColumnModel().getColumn(0));

        JScrollPane tableScroll = new JScrollPane(tblItems);
        rightPanel.add(tableScroll, BorderLayout.CENTER);

        JPanel bottomRight = new JPanel(new BorderLayout(10, 10));
        bottomRight.setOpaque(false);

        lblTotal = new JLabel("Grand Total: $0.00", SwingConstants.RIGHT);
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTotal.setForeground(new Color(33, 150, 243));
        bottomRight.add(lblTotal, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        btnPanel.setOpaque(false);

        btnSave = createActionButton("Save Invoice", new Color(33, 150, 243));
        btnNewInvoice = createActionButton("New Invoice", new Color(100, 181, 246));
        btnPrint = createActionButton("Print", new Color(129, 199, 132));

        btnSave.addActionListener(e -> saveInvoice());
        btnNewInvoice.addActionListener(e -> clearInvoice());
        btnPrint.addActionListener(e -> JOptionPane.showMessageDialog(this, "Printing..."));

        btnPanel.add(btnSave);
        btnPanel.add(btnNewInvoice);
        btnPanel.add(btnPrint);
        bottomRight.add(btnPanel, BorderLayout.SOUTH);

        rightPanel.add(bottomRight, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        mainPanel.add(leftPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.4;
        mainPanel.add(rightPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        txtSearch.addActionListener(e -> filterItems(itemGrid));
        loadItemsGrid(itemGrid);
    }

    private void loadAvailableItems() {
        try {
            availableItems = itemController.getAllItems();
            System.out.println("Items loaded: " + availableItems.size());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load items: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void filterItems(JPanel itemGrid) {
        String keyword = txtSearch.getText().trim().toLowerCase();
        itemGrid.removeAll();
        for (Item item : availableItems) {
            if (item.getItemName().toLowerCase().contains(keyword)
                    || item.getItemCode().toLowerCase().contains(keyword)
                    || item.getCategory().toLowerCase().contains(keyword)) {
                itemGrid.add(createItemCard(item));
            }
        }
        itemGrid.revalidate();
        itemGrid.repaint();
    }

    private void loadItemsGrid(JPanel itemGrid) {
        itemGrid.removeAll();
        if (availableItems != null) {
            for (Item item : availableItems) {
                itemGrid.add(createItemCard(item));
            }
        }
        itemGrid.revalidate();
        itemGrid.repaint();
    }

    private JPanel createItemCard(Item item) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel name = new JLabel(item.getItemName());
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel price = new JLabel("$" + item.getRetailPrice());
        price.setFont(new Font("Segoe UI", Font.BOLD, 16));
        price.setForeground(new Color(33, 150, 243));

        JLabel lblCategory = new JLabel(item.getCategory(), SwingConstants.LEFT);
        lblCategory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCategory.setForeground(Color.GRAY);

        card.add(name, BorderLayout.NORTH);
        card.add(price, BorderLayout.CENTER);
        card.add(lblCategory, BorderLayout.SOUTH);

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                addItemToInvoice(item);
            }
        });

        return card;
    }

    private JButton createActionButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void addItemToInvoice(Item item) {
        String qtyStr = JOptionPane.showInputDialog(this, "Enter Quantity:", "Add Item", JOptionPane.PLAIN_MESSAGE);
        if (qtyStr == null || qtyStr.trim().isEmpty()) return;
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) return;

            double price = item.getRetailPrice();
            double total = qty * price;

            tableModel.addRow(new Object[]{item.getId(), item.getItemName(), qty, price, total});
            recalcTotals();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!");
        }
    }

    private void recalcTotals() {
        double subtotal = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            subtotal += (double) tableModel.getValueAt(i, 4);
        }
        lblSubtotal.setText(String.format("$%.2f", subtotal));

        double discount = 0;
        try {
            discount = Double.parseDouble(txtDiscount.getText());
        } catch (NumberFormatException ignored) {
        }

        double total = subtotal - discount;
        lblTotal.setText(String.format("Grand Total: $%.2f", total));
    }

    private void saveInvoice() {
        try {
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No items added!");
                return;
            }

            Invoice invoice = new Invoice();
            invoice.setInvoiceNumber(lblInvoiceNo.getText());
            invoice.setBillingType(cmbBillingType == null ? "Retail" : cmbBillingType.getSelectedItem().toString());
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

            int invoiceId = invoiceController.saveInvoice(invoice);
            invoiceController.saveInvoiceItems(invoiceId, itemList);

            JOptionPane.showMessageDialog(this, "Invoice saved successfully!");
            clearInvoice();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearInvoice() {
        tableModel.setRowCount(0);
        txtDiscount.setText("0");
        lblSubtotal.setText("$0.00");
        lblTotal.setText("Grand Total: $0.00");
        lblInvoiceNo.setText("INV-" + UUID.randomUUID().toString().substring(0, 8));
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(() -> {
            try {
                new InvoiceForm();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
