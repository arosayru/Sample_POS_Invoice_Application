package com.pos.view;

import com.formdev.flatlaf.FlatLightLaf;
import com.pos.controller.ItemController;
import com.pos.model.Item;
import com.pos.util.ThemeManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;

public class ItemForm extends JFrame {
    private JTextField txtSearch;
    private JButton btnAddItem, btnBack;
    private JTable tblItems;
    private DefaultTableModel tableModel;
    private List<Item> items;
    private final ItemController itemController;

    public ItemForm() {
        itemController = new ItemController();
        FlatLightLaf.setup();
        initUI();
        setVisible(true);
        loadItems();
    }

    private void initUI() {
        setTitle("Item Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().setBackground(new Color(245, 247, 250));
        setLayout(new BorderLayout(10, 10));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(15, 20, 10, 20));

        btnBack = new JButton("← Back");
        btnBack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnBack.setFocusPainted(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBack.addActionListener(e -> {
            dispose();
            new DashboardForm().setVisible(true);
        });
        topPanel.add(btnBack, BorderLayout.WEST);

        JPanel themePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        themePanel.setOpaque(false);
        JLabel lblThemeIcon = new JLabel("🌤️");
        lblThemeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        JLabel lblThemeText = new JLabel("Light / Dark Theme");
        lblThemeText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JToggleButton toggleTheme = new JToggleButton();
        toggleTheme.setPreferredSize(new Dimension(50, 25));
        toggleTheme.setFocusable(false);
        toggleTheme.addActionListener(e -> ThemeManager.toggleTheme(this, new JButton()));
        themePanel.add(lblThemeIcon);
        themePanel.add(lblThemeText);
        themePanel.add(toggleTheme);
        topPanel.add(themePanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(15, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(10, 60, 40, 60));

        JLabel lblTitle = new JLabel("Item Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        centerPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setOpaque(false);

        txtSearch = new JTextField("Search by Item Name, Code, or Category");
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSearch.setPreferredSize(new Dimension(350, 30));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(4, 10, 4, 10)
        ));
        txtSearch.setForeground(Color.GRAY);

        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().startsWith("Search")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().trim().isEmpty()) {
                    txtSearch.setText("Search by Item Name, Code, or Category");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = txtSearch.getText().trim().toLowerCase();
                if (query.isEmpty() || query.startsWith("search")) {
                    refreshTable(items);
                    return;
                }
                List<Item> filtered = items.stream()
                        .filter(item -> item.getItemName().toLowerCase().contains(query)
                                || item.getItemCode().toLowerCase().contains(query)
                                || (item.getCategory() != null && item.getCategory().toLowerCase().contains(query)))
                        .toList();
                refreshTable(filtered);
            }
        });

        btnAddItem = new JButton("+ Add New Item");
        btnAddItem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAddItem.setBackground(new Color(67, 160, 71));
        btnAddItem.setForeground(Color.WHITE);
        btnAddItem.setFocusPainted(false);
        btnAddItem.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        btnAddItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAddItem.addActionListener(e -> openAddOrEditDialog(null));

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnAddItem, BorderLayout.EAST);
        centerPanel.add(searchPanel, BorderLayout.CENTER);

        String[] columns = {"ID", "Code", "Name", "Category", "Cost", "Wholesale", "Retail", "Label", "Credit", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0);
        tblItems = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };
        tblItems.setRowHeight(36);
        tblItems.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblItems.setGridColor(new Color(230, 230, 230));
        tblItems.setSelectionBackground(new Color(200, 230, 255));
        tblItems.setFillsViewportHeight(true);

        int actionsColIndex = tableModel.getColumnCount() - 1;
        tblItems.getColumnModel().getColumn(actionsColIndex).setCellRenderer(new ActionCellRenderer());
        tblItems.getColumnModel().getColumn(actionsColIndex).setCellEditor(new ActionCellEditor());

        tblItems.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblItems.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblItems.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblItems.getColumnModel().getColumn(10).setPreferredWidth(160);

        JScrollPane scrollPane = new JScrollPane(tblItems);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setPreferredSize(new Dimension(0, 450));

        centerPanel.add(scrollPane, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    private void loadItems() {
        try {
            items = itemController.getAllItems();
            refreshTable(items);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load items: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTable(List<Item> list) {
        tableModel.setRowCount(0);
        if (list == null) return;
        for (Item item : list) {
            tableModel.addRow(new Object[]{
                    item.getId(),
                    item.getItemCode(),
                    item.getItemName(),
                    item.getCategory(),
                    item.getCost(),
                    item.getWholesalePrice(),
                    item.getRetailPrice(),
                    item.getLabelPrice(),
                    item.getCreditPrice(),
                    item.getStatus(),
                    "Actions"
            });
        }
    }

    private void openAddOrEditDialog(Item existingItem) {
        boolean isEdit = existingItem != null;
        JDialog dialog = new JDialog(this, isEdit ? "Edit Item" : "Add New Item", true);
        dialog.setSize(450, 550);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.getContentPane().setBackground(new Color(245, 247, 250));
        dialog.setLayout(new BorderLayout(15, 15));

        JLabel lblTitle = new JLabel(isEdit ? "Edit Item" : "Add New Item", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setBorder(new EmptyBorder(15, 10, 5, 10));
        dialog.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        formPanel.setOpaque(false);

        JTextField txtCode = createInputField();
        JTextField txtName = createInputField();
        JTextField txtCategory = createInputField();
        JTextField txtCost = createInputField();
        JTextField txtWholesale = createInputField();
        JTextField txtRetail = createInputField();
        JTextField txtLabel = createInputField();
        JTextField txtCredit = createInputField();
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Active", "Inactive"});

        if (isEdit) {
            txtCode.setText(existingItem.getItemCode());
            txtName.setText(existingItem.getItemName());
            txtCategory.setText(existingItem.getCategory());
            txtCost.setText(String.valueOf(existingItem.getCost()));
            txtWholesale.setText(String.valueOf(existingItem.getWholesalePrice()));
            txtRetail.setText(String.valueOf(existingItem.getRetailPrice()));
            txtLabel.setText(String.valueOf(existingItem.getLabelPrice()));
            txtCredit.setText(String.valueOf(existingItem.getCreditPrice()));
            cmbStatus.setSelectedItem(existingItem.getStatus());
        }

        formPanel.add(createLabel("Item Code:")); formPanel.add(txtCode);
        formPanel.add(createLabel("Item Name:")); formPanel.add(txtName);
        formPanel.add(createLabel("Category:")); formPanel.add(txtCategory);
        formPanel.add(createLabel("Cost:")); formPanel.add(txtCost);
        formPanel.add(createLabel("Wholesale Price:")); formPanel.add(txtWholesale);
        formPanel.add(createLabel("Retail Price:")); formPanel.add(txtRetail);
        formPanel.add(createLabel("Label Price:")); formPanel.add(txtLabel);
        formPanel.add(createLabel("Credit Price:")); formPanel.add(txtCredit);
        formPanel.add(createLabel("Status:")); formPanel.add(cmbStatus);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        JButton btnSave = new JButton(isEdit ? "Update Item" : "Save Item");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSave.setBackground(new Color(33, 150, 243));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        btnCancel.setBackground(new Color(189, 189, 189));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnCancel.addActionListener(e -> dialog.dispose());

        btnSave.addActionListener(e -> {
            try {
                if (txtCode.getText().trim().isEmpty() || txtName.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all required fields!");
                    return;
                }

                Item item = isEdit ? existingItem : new Item();
                item.setItemCode(txtCode.getText().trim());
                item.setItemName(txtName.getText().trim());
                item.setCategory(txtCategory.getText().trim());
                item.setCost(parseDoubleOrZero(txtCost.getText().trim()));
                item.setWholesalePrice(parseDoubleOrZero(txtWholesale.getText().trim()));
                item.setRetailPrice(parseDoubleOrZero(txtRetail.getText().trim()));
                item.setLabelPrice(parseDoubleOrZero(txtLabel.getText().trim()));
                item.setCreditPrice(parseDoubleOrZero(txtCredit.getText().trim()));
                item.setStatus((String) cmbStatus.getSelectedItem());

                if (isEdit) {
                    itemController.updateItem(item);
                    JOptionPane.showMessageDialog(this, "Item updated successfully!");
                } else {
                    itemController.addItem(item);
                    JOptionPane.showMessageDialog(this, "Item added successfully!");
                }

                loadItems();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.getRootPane().setDefaultButton(btnSave);
        dialog.setVisible(true);
    }

    private double parseDoubleOrZero(String s) {
        try {
            if (s == null || s.isEmpty()) return 0.0;
            return Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JTextField createInputField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private class ActionCellRenderer implements TableCellRenderer {
        private final JPanel panel;
        private final JButton editBtn;
        private final JButton deleteBtn;

        public ActionCellRenderer() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
            panel.setOpaque(true);
            editBtn = createStyledButton("Edit", new Color(33, 150, 243));
            deleteBtn = createStyledButton("Delete", new Color(244, 67, 54));
            panel.add(editBtn);
            panel.add(deleteBtn);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            panel.setBackground(isSelected ? new Color(230, 245, 255) : Color.WHITE);
            return panel;
        }
    }

    private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        private final JButton editBtn = createStyledButton("Edit", new Color(33, 150, 243));
        private final JButton deleteBtn = createStyledButton("Delete", new Color(244, 67, 54));
        private int editingRow = -1;

        public ActionCellEditor() {
            panel.setOpaque(true);
            panel.add(editBtn);
            panel.add(deleteBtn);

            editBtn.addActionListener(e -> {
                if (editingRow >= 0 && editingRow < tblItems.getRowCount()) {
                    int modelRow = tblItems.convertRowIndexToModel(editingRow);
                    Object idObj = tableModel.getValueAt(modelRow, 0);
                    if (idObj != null) {
                        int id = Integer.parseInt(idObj.toString());
                        Item found = items.stream().filter(it -> it.getId() == id).findFirst().orElse(null);
                        stopCellEditing();
                        if (found != null) {
                            openAddOrEditDialog(found);
                        } else {
                            JOptionPane.showMessageDialog(ItemForm.this, "Item not found for edit.");
                        }
                    }
                }
            });

            deleteBtn.addActionListener(e -> {
                if (editingRow >= 0 && editingRow < tblItems.getRowCount()) {
                    int modelRow = tblItems.convertRowIndexToModel(editingRow);
                    Object idObj = tableModel.getValueAt(modelRow, 0);
                    if (idObj != null) {
                        int id = Integer.parseInt(idObj.toString());
                        int confirm = JOptionPane.showConfirmDialog(ItemForm.this,
                                "Are you sure you want to delete this item?", "Confirm Delete",
                                JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            try {
                                itemController.deleteItem(id);
                                JOptionPane.showMessageDialog(ItemForm.this, "Item deleted successfully!");
                                loadItems();
                            } catch (SQLException ex) {
                                JOptionPane.showMessageDialog(ItemForm.this, "Error deleting item: " + ex.getMessage());
                            }
                        }
                        stopCellEditing();
                    }
                }
            });
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

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        SwingUtilities.invokeLater(ItemForm::new);
    }
}
