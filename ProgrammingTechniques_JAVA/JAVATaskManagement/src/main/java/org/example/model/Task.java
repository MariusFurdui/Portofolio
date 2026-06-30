package org.example.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract sealed class Task implements Serializable
        permits SimpleTask, ComplexTask {
    private static int counter = 1;
    private static final long serialVersionUID = 1L;
    private int idTask;
    private String title;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Task(String title, LocalDateTime startTime, LocalDateTime endTime) {
        this.idTask = counter++;
        this.title = title;
        this.status = "Uncompleted";
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public abstract double estimateDuration();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public static void setCounter(int value) {
        counter = value;
    }

    @Override
    public String toString() {
        String type = (this instanceof SimpleTask) ? "Simple" : "Complex";
        return title + " - " + status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(!(o instanceof Task)) return false;
        Task task = (Task) o;
        return idTask == task.idTask;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idTask);
    }
}
