package org.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Employee implements Serializable{
    private static final long serialVersionUID = 1L;
    private static int counter = 1;
    private int idEmployee;
    private String name;
    private List<Task> tasks;

    public Employee(String name){
        this.idEmployee = counter++;
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task){
        this.tasks.add(task);
    }

    public static void setCounter(int value){
        counter = value;
    }

    @Override
    public String toString() {
        return "ID: " + idEmployee + ", " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return idEmployee == employee.idEmployee;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idEmployee);
    }
}
