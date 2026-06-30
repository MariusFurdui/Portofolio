package org.example.ui;

import org.example.model.ComplexTask;
import org.example.model.Employee;
import org.example.model.Task;
import org.example.service.TasksManagement;
import org.example.service.Utility;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class StatisticsPanel extends JPanel {

    private final TasksManagement tasksManagement;
    private final Utility utility;
    private final JTextArea displayArea;

    public StatisticsPanel(TasksManagement tasksManagement, Utility utility) {
        this.tasksManagement = tasksManagement;
        this.utility = utility;
        this.displayArea = new JTextArea();

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("View"));
        initComponents();
    }

    private void initComponents() {
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnViewAll = new JButton("View All");
        JButton btnStatistics = new JButton("Statistics");
        buttons.add(btnViewAll);
        buttons.add(btnStatistics);
        add(buttons, BorderLayout.SOUTH);

        btnViewAll.addActionListener(e -> showAllEmployees());
        btnStatistics.addActionListener(e -> showStatistics());
    }

    private void showAllEmployees() {
        StringBuilder sb = new StringBuilder();
        sb.append("Employees and Tasks:\n");

        for (Employee emp : tasksManagement.getAllEmployees()) {
            sb.append("\n");
            sb.append(emp.getName()).append(":\n");
            double duration = tasksManagement.calculateEmployeeWorkDuration(emp);
            sb.append("Total time:").append(String.format("%.2f", duration)).append("h\n");

            for (Task task : tasksManagement.getTasksForEmployee(emp)) {
                appendTask(sb, task, "  ");
            }
        }

        displayArea.setText(sb.toString());
    }

    private void showStatistics() {
        StringBuilder sb = new StringBuilder();
        sb.append("Statistics:\n\n");

        sb.append("Employees with more than 40 hours of work:\n");
        List<String> over40 = utility.filterEmployeesWithWorkDurationGreaterThan40();
        if (over40.isEmpty()) {
            sb.append("  - No employees\n");
        } else {
            for (String name : over40) {
                sb.append("  - ").append(name).append("\n");
            }
        }

        sb.append("\nNumber of tasks after status:\n");
        Map<String, Map<String, Integer>> taskCounts = utility.countTasksByStatusForEachEmployee();
        if (taskCounts.isEmpty()) {
            sb.append("  - No employees\n");
        } else {
            taskCounts.forEach((name, map) -> {
                sb.append("  \n").append(name).append(":\n");
                sb.append("    Completed: ").append(map.getOrDefault("Completed", 0)).append("\n");
                sb.append("    Uncompleted: ").append(map.getOrDefault("Uncompleted", 0)).append("\n");
            });
        }

        displayArea.setText(sb.toString());
    }

    private void appendTask(StringBuilder sb, Task task, String indent){
        sb.append(indent).append("  - ").append(task.getTitle())
                .append(" [").append(task.getStatus()).append("]")
                .append(" (").append(String.format("%.2f", task.estimateDuration())).append("h)\n");
        if (task instanceof ComplexTask) {
            for(Task sub : ((ComplexTask) task).getSubTasks()) {
                appendTask(sb, sub, indent + "      ");
            }
        }
    }
    public void append(String message) {
        displayArea.append(message + "\n");
    }
}