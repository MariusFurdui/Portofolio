package com.orders.presentation;

import com.orders.businessLayer.ClientBLL;
import com.orders.model.Client;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

/**
 * The type Client tab.
 */
public class ClientTab extends JFrame {
    private ClientBLL clientBLL = new ClientBLL();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nameField, emailField, phoneField, addressField;

    /**
     * Instantiates a new Client tab.
     */
    public ClientTab() {
        setTitle("Clients");
        setSize(900, 500);
        setLocationRelativeTo(null);

        // Table
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        generateTableHeaders(Client.class);
        refreshTable();

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("Name:", SwingConstants.RIGHT));
        nameField = new JTextField(40);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Email:", SwingConstants.RIGHT));
        emailField = new JTextField(40);
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:", SwingConstants.RIGHT));
        phoneField = new JTextField(40);
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Address:", SwingConstants.RIGHT));
        addressField = new JTextField(40);
        formPanel.add(addressField);

        // Buttons
        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(refreshBtn);

        // Actions
        addBtn.addActionListener(e -> addClient());
        editBtn.addActionListener(e -> editClient());
        deleteBtn.addActionListener(e -> deleteClient());
        refreshBtn.addActionListener(e -> refreshTable());

        // Table click -> populate fields
        table.getSelectionModel().addListSelectionListener(e -> populateFields());

        // Layout
        add(new JScrollPane(table), BorderLayout.CENTER);
        formPanel.setPreferredSize(new Dimension(500, 130));
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.add(formPanel);
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
        List<Client> clients = clientBLL.findAll();
        for (Client c : clients) {
            tableModel.addRow(new Object[]{c.getIdClient(), c.getName(), c.getEmail(), c.getPhone(), c.getAddress()});
        }
    }

    private void addClient() {
        Client c = new Client(0, nameField.getText(), emailField.getText(),
                phoneField.getText(), addressField.getText());
        clientBLL.insert(c);
        refreshTable();
    }

    private void editClient() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a client!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Client c = new Client(id, nameField.getText(), emailField.getText(),
                phoneField.getText(), addressField.getText());
        clientBLL.update(c);
        refreshTable();
    }

    private void deleteClient() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a client!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        clientBLL.delete(id);
        refreshTable();
    }

    private void populateFields() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        nameField.setText((String) tableModel.getValueAt(row, 1));
        emailField.setText((String) tableModel.getValueAt(row, 2));
        phoneField.setText((String) tableModel.getValueAt(row, 3));
        addressField.setText((String) tableModel.getValueAt(row, 4));
    }
}