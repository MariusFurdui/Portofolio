package org.example.ui;

import org.example.service.TasksManagement;
import org.example.service.Utility;
import org.example.util.DataPersistence;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final TasksManagement tasksManagement;
    private final Utility utility;

    private EmployeePanel employeePanel;
    private TaskPanel taskPanel;
    private StatisticsPanel statisticsPanel;

    public MainFrame() {
        this.tasksManagement = new TasksManagement();
        this.utility = new Utility(tasksManagement);

        setTitle("Task Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));

        employeePanel = new EmployeePanel(tasksManagement);
        taskPanel = new TaskPanel(tasksManagement, employeePanel.getEmployeeListModel());
        statisticsPanel = new StatisticsPanel(tasksManagement, utility);

        JPanel topPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        topPanel.add(employeePanel);
        topPanel.add(taskPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, statisticsPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(320);

        add(splitPane, BorderLayout.CENTER);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(e -> saveData());
        add(btnSave, BorderLayout.NORTH);
    }

    private void saveData() {
        try {
            DataPersistence.saveEmployees(tasksManagement.getAllEmployees());
            DataPersistence.saveTasks(tasksManagement.getAllTasks());
            JOptionPane.showMessageDialog(this, "Data saved successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Saving error: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}