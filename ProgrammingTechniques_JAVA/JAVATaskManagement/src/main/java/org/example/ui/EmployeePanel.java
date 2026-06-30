package org.example.ui;

import org.example.model.Employee;
import org.example.service.TasksManagement;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends JPanel {

    private final TasksManagement tasksManagement;
    private final DefaultListModel<Employee> employeeListModel;
    private final JList<Employee> employeeList;

    public EmployeePanel(TasksManagement tasksManagement) {
        this.tasksManagement = tasksManagement;
        this.employeeListModel = new DefaultListModel<>();
        this.employeeList = new JList<>(employeeListModel);

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Employees"));
        initComponents();
        refresh();
    }

    private void initComponents() {
        employeeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(employeeList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnAdd = new JButton("Add employee");
        JButton btnDelete = new JButton("Delete employee");
        buttons.add(btnAdd);
        buttons.add(btnDelete);
        add(buttons, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> onAdd());
        btnDelete.addActionListener(e -> onDelete());
    }

    private void onAdd() {
        JTextField nameField = new JTextField();

        Object[] message = {"Name: ", nameField};
        int option = JOptionPane.showConfirmDialog(this, message,
                "Add Employee", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Name cannot be null!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            tasksManagement.addEmployee(new Employee(name));
            refresh();
        }
    }

    private void onDelete() {
        Employee selected = employeeList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this,
                    "Select an employee!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm deletion of employee:\n" + selected.getName() + "?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            tasksManagement.deleteEmployee(selected);
            refresh();
        }
    }

    public void refresh() {
        employeeListModel.clear();
        tasksManagement.getAllEmployees().forEach(employeeListModel::addElement);
    }

    public DefaultListModel<Employee> getEmployeeListModel() {
        return employeeListModel;
    }
}