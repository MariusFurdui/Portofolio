package org.example.Model;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private LinkedBlockingQueue<Client> clients;
    private AtomicInteger waitingPeriod;
    private int id;
    private volatile boolean running = true;

    public Server(int id){
        this.clients = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger(0);
        this.id = id;
    }
    public void addClient(Client client) {
        client.setWaitingTime(waitingPeriod.get());
        clients.add(client);
        waitingPeriod.addAndGet(client.getServiceTime());
    }
    public int getId(){
        return id;
    }
    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }
    public boolean isEmpty() {
        return clients.isEmpty();
    }
    public LinkedBlockingQueue<Client> getClients() {
        return clients;
    }

    public void stop(){
        running = false;
    }

    @Override
    public void run() {
        while(running){
            Client current = clients.peek();
            if(current != null){
                current.setServiceTime(current.getServiceTime() - 1);
                waitingPeriod.decrementAndGet();
                if(current.getServiceTime() == 0){
                    clients.poll();
                }
            }
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public String toString() {
        if(clients.isEmpty()) return "closed";
        StringBuilder sb = new StringBuilder();
        for(Client client : clients){
            if(sb.length()>0) sb.append(" ");
            sb.append(client.toString());
        }
        return sb.toString();
    }
}
