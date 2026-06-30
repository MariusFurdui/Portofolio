package org.example.ui;

import org.example.model.*;
import org.example.service.TasksManagement;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class TaskPanel extends JPanel {

    private final TasksManagement tasksManagement;
    private final DefaultListModel<Employee> employeeListModel;
    private final DefaultListModel<Task> taskListModel;
    private final JList<Task> taskList;

    public TaskPanel(TasksManagement tasksManagement, DefaultListModel<Employee> employeeListModel) {
        this.tasksManagement = tasksManagement;
        this.employeeListModel = employeeListModel;
        this.taskListModel = new DefaultListModel<>();
        this.taskList = new JList<>(taskListModel);

        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createTitledBorder("Tasks"));
        initComponents();
        refresh();
    }

    private void initComponents() {
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(taskList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout());
        JButton btnAddSimple = new JButton("Add Simple");
        JButton btnAddComplex = new JButton("Add Complex");
        JButton btnDelete = new JButton("Delete");
        JButton btnAssign = new JButton("Assign");
        JButton btnModStatus = new JButton("Modify Status");

        buttons.add(btnAddSimple);
        buttons.add(btnAddComplex);
        buttons.add(btnDelete);
        buttons.add(btnAssign);
        buttons.add(btnModStatus);
        add(buttons, BorderLayout.SOUTH);

        btnAddSimple.addActionListener(e -> onAddSimpleTask());
        btnAddComplex.addActionListener(e -> onAddComplexTask());
        btnDelete.addActionListener(e -> onDeleteTask());
        btnAssign.addActionListener(e -> onAssignTask());
        btnModStatus.addActionListener(e -> onModifyStatus());

        taskList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            JTextArea area = new JTextArea(value.toString());
            area.setOpaque(true);
            area.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            area.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            area.setFont(list.getFont());
            area.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            return area;
        });
    }

    private void onAddSimpleTask() {
        JTextField titleField = new JTextField();
        JTextField startHourField = new JTextField("8");
        JTextField endHourField = new JTextField("10");

        Object[] message = {
                "Title:", titleField,
                "Start hour (0-23):", startHourField,
                "End hour (0-23):", endHourField
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                "Add Simple Task", JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) return;

        try {
            String title = titleField.getText().trim();
            int startHour = Integer.parseInt(startHourField.getText().trim());
            int endHour = Integer.parseInt(endHourField.getText().trim());

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Title cannot be null!", "Erorr", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (startHour < 0 || endHour > 23 || endHour <= startHour) {
                JOptionPane.showMessageDialog(this, "Invalid hours! (0-23, end > start)", "Erorr", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDateTime start = LocalDateTime.now().withHour(startHour).withMinute(0).withSecond(0);
            LocalDateTime end = LocalDateTime.now().withHour(endHour).withMinute(0).withSecond(0);

            tasksManagement.addTask(new SimpleTask(title, start, end));
            refresh();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddComplexTask() {
        JTextField titleField = new JTextField();

        JList<Task> availableList = new JList<>(taskListModel);
        availableList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        Object[] message = {
                "Complex Task Title:", titleField,
                "Select subtasks (hold Ctrl for multiple):", new JScrollPane(availableList)
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                "Add Complex Task", JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) return;

        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Title cannot be null!", "Erorr", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ComplexTask complexTask = new ComplexTask(title);
        for (Task sub : availableList.getSelectedValuesList()) {
            complexTask.addSubTask(sub);
        }

        try {
            tasksManagement.addTask(complexTask);
            refresh();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onDeleteTask() {
        Task selected = taskList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a task!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Confirm deletion of task:\n" + selected.getTitle() + "?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            tasksManagement.deleteTask(selected);
            refresh();
        }
    }

    private void onAssignTask() {
        if (employeeListModel.isEmpty() || taskListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "At least an employee and a task must exist!", "Erorr", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JList<Employee> empList = new JList<>(employeeListModel);
        JList<Task> tList = new JList<>(taskListModel);
        empList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Object[] message = {
                "Select employee:", new JScrollPane(empList),
                "Select task:", new JScrollPane(tList)
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                "Assign Task", JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) return;

        Employee emp = empList.getSelectedValue();
        Task task = tList.getSelectedValue();

        if (emp == null || task == null) {
            JOptionPane.showMessageDialog(this, "Select an employee and a task", "Warning!", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            tasksManagement.assignTaskToEmployee(emp, task);
            refresh();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onModifyStatus() {
        if (employeeListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No employees!", "Erorr", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JList<Employee> empList = new JList<>(employeeListModel);
        empList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int result = JOptionPane.showConfirmDialog(this, new JScrollPane(empList),
                "Select employee", JOptionPane.OK_CANCEL_OPTION);

        if (result != JOptionPane.OK_OPTION) return;

        Employee emp = empList.getSelectedValue();
        if (emp == null) return;

        List<Task> tasks = tasksManagement.getTasksForEmployee(emp);
        if (tasks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Employee has no tasks assigned!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] taskTitles = tasks.stream()
                .map(t -> t.getTitle() + " [" + t.getStatus() + "] (" + String.format("%.2f", t.estimateDuration()) + "h)")
                .toArray(String[]::new);

        JList<String> taskList = new JList<>(taskTitles);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JRadioButton completedBtn = new JRadioButton("Completed");
        JRadioButton uncompletedBtn = new JRadioButton("Uncompleted");
        ButtonGroup group = new ButtonGroup();
        group.add(completedBtn);
        group.add(uncompletedBtn);
        uncompletedBtn.setSelected(true);

        JPanel statusPanel = new JPanel();
        statusPanel.add(completedBtn);
        statusPanel.add(uncompletedBtn);

        Object[] message = {
                "Tasks for " + emp.getName() + ":", new JScrollPane(taskList),
                "New status:", statusPanel
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                "Modify Task Status", JOptionPane.OK_CANCEL_OPTION);

        if (option != JOptionPane.OK_OPTION) return;

        int idx = taskList.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Select a task!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String newStatus = completedBtn.isSelected() ? "Completed" : "Uncompleted";
        tasksManagement.modifyTaskStatus(emp, tasks.get(idx), newStatus);
    }

    public void refresh() {
        taskListModel.clear();
        tasksManagement.getAllTasks().forEach(taskListModel::addElement);
    }
}