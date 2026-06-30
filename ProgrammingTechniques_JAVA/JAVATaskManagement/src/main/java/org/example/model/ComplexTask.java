package org.example.model;

import java.util.ArrayList;
import java.util.List;

public final class ComplexTask extends Task {

    private static final long serialVersionUID = 1L;

    private List<Task> subTasks;

    public ComplexTask(String title) {
        super(title, null, null);
        this.subTasks = new ArrayList<>();
    }

    @Override
    public double estimateDuration() {
        return subTasks.stream()
                .mapToDouble(Task::estimateDuration)
                .sum();
    }

    public void addSubTask(Task task) {
        subTasks.add(task);
    }

    public void removeSubTask(Task task) {
        subTasks.remove(task);
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<Task> subTasks) {
        this.subTasks = subTasks;
    }

    @Override
    public String getStatus() {
        if (subTasks.isEmpty()) {
            return "Uncompleted";
        }

        boolean allCompleted = subTasks.stream()
                .allMatch(t -> "Completed".equals(t.getStatus()));

        return allCompleted ? "Completed" : "In Progress";
    }

    @Override
    public String toString(){
        if(subTasks.isEmpty()){
            return getTitle()+ " - " + getStatus();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getTitle()).append(" - ").append(getStatus());
        for(Task subTask : subTasks){
            sb.append("\n   ").append(subTask.toString().replace("\n", "\n        "));
        }
        return sb.toString();
    }
}