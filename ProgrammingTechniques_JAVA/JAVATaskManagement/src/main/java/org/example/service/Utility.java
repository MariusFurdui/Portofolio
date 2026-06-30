package org.example.service;

import org.example.model.Employee;
import org.example.model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class Utility {
    private final TasksManagement tasksManagement;

    public Utility(TasksManagement tasksManagement) {
        this.tasksManagement = tasksManagement;
    }

    public List<String> filterEmployeesWithWorkDurationGreaterThan40() {
        List<Employee> employees = tasksManagement.getAllEmployees();

        return employees.stream()
                .filter(emp -> tasksManagement.calculateEmployeeWorkDuration(emp) > 40)
                .sorted(Comparator.comparingDouble(
                        emp -> tasksManagement.calculateEmployeeWorkDuration(emp)))
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Map<String, Integer>> countTasksByStatusForEachEmployee() {
        List<Employee> employees = tasksManagement.getAllEmployees();
        Map<String, Map<String, Integer>> result = new HashMap<>();

        for (Employee emp : employees) {
            Map<String, Integer> statusCount = new HashMap<>();
            statusCount.put("Completed", 0);
            statusCount.put("Uncompleted", 0);

            for (Task task : tasksManagement.getTasksForEmployee(emp)) {
                String status = task.getStatus();
                if (statusCount.containsKey(status)) {
                    statusCount.put(status, statusCount.get(status) + 1);
                } else {
                    statusCount.put(status, 1);
                }
            }

            result.put(emp.getName(), statusCount);
        }

        return result;
    }
}