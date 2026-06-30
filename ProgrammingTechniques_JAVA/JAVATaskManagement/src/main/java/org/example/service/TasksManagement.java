package org.example.service;

import org.example.model.Employee;
import org.example.model.Task;
import org.example.util.DataPersistence;

import java.util.*;

public class TasksManagement {
    private Map<Employee, List<Task>> map;
    private List<Task> unassignedTasks;

    public TasksManagement() {
        this.map = DataPersistence.loadMap();
        this.unassignedTasks = DataPersistence.loadTasks();
    }

    public void assignTaskToEmployee(Employee employee, Task task) {
        if (employee == null || task == null) {
            throw new IllegalArgumentException("Employee and task cannot be null");
        }
        List<Task> tasks = map.get(employee);
        if (tasks == null) {
            throw new IllegalArgumentException("Employee not found");
        }

        if(tasks.contains(task)){
            throw new IllegalArgumentException("Task already assigned");
        }

        tasks.add(task);
        //unassignedTasks.remove(task);
        DataPersistence.saveTasks(tasks);
        DataPersistence.saveMap(map);
    }

    public double calculateEmployeeWorkDuration(Employee employee) {
        if (employee == null) {
            return 0.0;
        }

        return getTasksForEmployee(employee).stream()
                .filter(task -> "Completed".equals(task.getStatus()))
                .mapToDouble(Task::estimateDuration)
                .sum();
    }

    public void modifyTaskStatus(Employee employee, Task task, String newStatus) {
        if (employee == null || task == null) {
            throw new IllegalArgumentException("Employee and task cannot be null");
        }

        if (!"Completed".equals(newStatus) && !"Uncompleted".equals(newStatus)) {
            throw new IllegalArgumentException("Status must be 'Completed' or 'Uncompleted'");
        }
        List<Task> tasks = map.get(employee);
        if (tasks != null && tasks.contains(task)) {
            task.setStatus(newStatus);
            DataPersistence.saveMap(map);
        } else {
            throw new IllegalArgumentException("Task does not belong to this employee");
        }
    }

    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }
        map.put(employee, new ArrayList<>());
        DataPersistence.saveMap(map);
    }

    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        boolean titleExists = unassignedTasks.stream().anyMatch(tasksManagement -> tasksManagement.getTitle().equals(task.getTitle()));
        if (titleExists) {
            throw new IllegalArgumentException("Task already exists");
        }
        unassignedTasks.add(task);
        DataPersistence.saveTasks(unassignedTasks);
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(unassignedTasks);
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(map.keySet());
    }

    public List<Task> getTasksForEmployee(Employee employee) {
        if (employee == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(map.getOrDefault(employee, new ArrayList<>()));
    }

    public void deleteEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Employee cannot be null");
        }

        map.remove(employee);

        DataPersistence.saveMap(map);
    }

    public void deleteTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }

        unassignedTasks.remove(task);

        for(List<Task> tasks: map.values()){
            tasks.remove(task);
        }
        DataPersistence.saveMap(map);
        DataPersistence.saveTasks(unassignedTasks);
    }
}
