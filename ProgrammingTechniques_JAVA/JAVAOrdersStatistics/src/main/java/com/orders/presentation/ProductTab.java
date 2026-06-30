package com.orders.presentation;

import com.orders.businessLayer.ProductBLL;
import com.orders.model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The type Product tab.
 */
public class ProductTab extends JFrame {
    private ProductBLL productBLL = new ProductBLL();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, priceField, stockField;

    /**
     * Instantiates a new Product tab.
     */
    public ProductTab() {
        setTitle("Products");
        setSize(900, 500);
        setLocationRelativeTo(null);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        generateTableHeaders(Product.class);
        refreshTable();

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Name:", SwingConstants.RIGHT));
        nameField = new JTextField(20);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Price:", SwingConstants.RIGHT));
        priceField = new JTextField(20);
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stock:", SwingConstants.RIGHT));
        stockField = new JTextField(20);
        formPanel.add(stockField);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        addBtn.addActionListener(e -> addProduct());
        editBtn.addActionListener(e -> editProduct());
        deleteBtn.addActionListener(e -> deleteProduct());
        refreshBtn.addActionListener(e -> refreshTable());

        table.getSelectionModel().addListSelectionListener(e -> populateFields());

        add(new JScrollPane(table), BorderLayout.CENTER);
        formPanel.setPreferredSize(new Dimension(400, 100));
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.X_AXIS));
        wrapper.add(Box.createHorizontalGlue());
        wrapper.add(formPanel);
        wrapper.add(Box.createHorizontalGlue());
        add(wrapper, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void generateTableHeaders(Class<?> clazz) {
        tableModel.setColumnCount(0);
        for (Field field : clazz.getDeclaredFields()) {
            tableModel.addColumn(field.getName());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Product> products = productBLL.findAll();
        for (Product p : products) {
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getPrice(), p.getStock()});
        }
    }

    private void addProduct() {
        try {
            Product p = new Product(0, nameField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(stockField.getText()));
            productBLL.insert(p);
            refreshTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price or stock!");
        }
    }

    private void editProduct() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a product!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        try {
            Product p = new Product(id, nameField.getText(),
                    Double.parseDouble(priceField.getText()),
                    Integer.parseInt(stockField.getText()));
            productBLL.update(p);
            refreshTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid price or stock!");
        }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a product!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        productBLL.delete(id);
        refreshTable();
    }

    private void populateFields() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        nameField.setText((String) tableModel.getValueAt(row, 1));
        priceField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        stockField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }
}