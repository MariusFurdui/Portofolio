package org.example.util;

import org.example.model.Employee;
import org.example.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPersistence {
    private static final String employeeFile = "employee.txt";
    private static final String taskFile = "task.txt";

    public static void saveEmployees(List<Employee> employees) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(employeeFile))) {
            oos.writeObject(employees);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Employee save error:" + e.getMessage());
        }
    }

    public static void saveTasks(List<Task> tasks) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(taskFile))) {
            oos.writeObject(tasks);
            oos.flush();
        } catch (IOException e) {
            System.err.println("Task Save error:" + e.getMessage());
        }
    }

    public static List<Task> loadTasks(){
        File file = new File(taskFile);
        if(!file.exists()){
            return new ArrayList<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            List<Task> tasks = (List<Task>) ois.readObject();
            tasks.stream()
                    .mapToInt(Task::getIdTask)
                    .max()
                    .ifPresent(max -> Task.setCounter(max + 1));
            return tasks;
        }catch (IOException | ClassNotFoundException e){
            System.err.println("Load tasks error:" + e.getMessage());
            return new ArrayList<>();
        }
    }

    private static final String mapFile = "map.txt";
    public static void saveMap(Map<Employee, List<Task>> map) {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(mapFile))){
            oos.writeObject(map);
            oos.flush();
        } catch(IOException e) {
            System.err.println("Map Save error:" + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<Employee, List<Task>> loadMap() {
        File file = new File(mapFile);
        if(!file.exists()){
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            Map<Employee, List<Task>> map = (Map<Employee, List<Task>>) ois.readObject();
            map.keySet().stream()
                    .mapToInt(Employee::getIdEmployee)
                    .max()
                    .ifPresent(max -> Employee.setCounter(max + 1));
            return map;
        }catch(IOException | ClassNotFoundException e){
            System.err.println("Map Load error:" + e.getMessage());
            return new HashMap<>();
        }
    }
}