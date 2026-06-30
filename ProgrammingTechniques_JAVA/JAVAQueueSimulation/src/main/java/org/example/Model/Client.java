package org.example.Model;

public class Client {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int waitingTime;
    private final int originalServiceTime;
    private int finishTime;

    public Client(int id, int arrivalTime, int serviceTime) {
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.id = id;
        this.originalServiceTime = serviceTime;
        this.waitingTime = 0;
        this.finishTime = 0;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }
    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    public int getOriginalServiceTime() {
        return originalServiceTime;
    }
    public int getId() {
        return id;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    @Override
    public String toString() {
        return "(" + id + ", " + arrivalTime + ", " + serviceTime + ")";
    }

    public double getWaitingTime() {
        return waitingTime;
    }
    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

}
