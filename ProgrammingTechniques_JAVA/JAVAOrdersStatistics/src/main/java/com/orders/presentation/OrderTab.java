package com.orders.presentation;

import com.orders.businessLayer.ClientBLL;
import com.orders.businessLayer.OrderBLL;
import com.orders.businessLayer.ProductBLL;
import com.orders.model.Client;
import com.orders.model.Orders;
import com.orders.model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * The type Order tab.
 */
public class OrderTab extends JFrame {
    private OrderBLL orderBLL = new OrderBLL();
    private ClientBLL clientBLL = new ClientBLL();
    private ProductBLL productBLL = new ProductBLL();
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> clientCombo;
    private JComboBox<String> productCombo;
    private JTextField quantityField;

    /**
     * Instantiates a new Order tab.
     */
    public OrderTab() {
        setTitle("Orders");
        setSize(800, 500);
        setLocationRelativeTo(null);

        String[] columns = {"ID", "Client", "Product", "Quantity", "Total Price", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        clientCombo = new JComboBox<>();
        productCombo = new JComboBox<>();
        quantityField = new JTextField();

        formPanel.add(new JLabel("Client:"));
        formPanel.add(clientCombo);
        formPanel.add(new JLabel("Product:"));
        formPanel.add(productCombo);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(quantityField);

        JButton addBtn = new JButton("Place Order");
        JButton deleteBtn = new JButton("Delete Order");
        JButton refreshBtn = new JButton("Refresh");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        addBtn.addActionListener(e -> placeOrder());
        deleteBtn.addActionListener(e -> deleteOrder());
        refreshBtn.addActionListener(e -> refreshTable());

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        loadCombos();
        refreshTable();
    }

    private void loadCombos() {
        clientCombo.removeAllItems();
        productCombo.removeAllItems();

        for (Client c : clientBLL.findAll()) {
            clientCombo.addItem(c.getIdClient() + " - " + c.getName());
        }

        for (Product p : productBLL.findAll()) {
            productCombo.addItem(p.getId() + " - " + p.getName() + " (stock: " + p.getStock() + ")");
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        List<Orders> orders = orderBLL.findAll();

        for (Orders order : orders) {
            Client client = clientBLL.findById(order.getClientID());
            Product product = productBLL.findById(order.getProductID());

            String clientName = (client != null) ? client.getName() : "Unknown";
            String productName = (product != null) ? product.getName() : "Unknown";
            double totalPrice = (product != null) ? product.getPrice() * order.getQuantity() : 0;

            tableModel.addRow(new Object[]{
                    order.getIdOrder(),
                    clientName,
                    productName,
                    order.getQuantity(),
                    totalPrice,
                    orderBLL.getOrderDate(order.getIdOrder())
            });
        }
    }

    private void placeOrder() {
        try {
            if (clientCombo.getSelectedIndex() == -1 || productCombo.getSelectedIndex() == -1) {
                JOptionPane.showMessageDialog(this, "Please select a client and a product!");
                return;
            }

            int clientId = clientBLL.findAll().get(clientCombo.getSelectedIndex()).getIdClient();
            int productId = productBLL.findAll().get(productCombo.getSelectedIndex()).getId();
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!");
                return;
            }

            Orders order = new Orders(0, clientId, productId, quantity);
            orderBLL.insert(order);

            refreshTable();
            loadCombos();
            quantityField.setText("");

            JOptionPane.showMessageDialog(this, "Order placed successfully!");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity! Please enter a number.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void deleteOrder() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete!");
            return;
        }

        int orderId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete order #" + orderId + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            orderBLL.delete(orderId);
            refreshTable();
            loadCombos();
            JOptionPane.showMessageDialog(this, "Order deleted!");
        }
    }
}