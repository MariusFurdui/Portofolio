package org.example.Logic;

import org.example.Model.Client;
import org.example.Model.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.io.FileWriter;
import java.util.function.Consumer;

public class SimulationManager implements Runnable{
    private List<Client> generatedClients = Collections.synchronizedList(new ArrayList<>());
    private List<Server> servers;
    private List<Client> allClients;
    private Scheduler scheduler;
    private int numClients;
    private int numQueues;
    private int maxSimulationTime;
    private int minArrival, maxArrival;
    private int minService, maxService;
    private PrintWriter logWriter;
    private Consumer<String> onLogUpdate;
    private Consumer<List<Server>> onQueueUpdate;
    private Consumer<String> onFinished;
    private List<Thread> serverThreads = new ArrayList<>();

    public void setOnQueueUpdate(Consumer<List<Server>> onQueueUpdate) {
        this.onQueueUpdate = onQueueUpdate;
    }

    public void setOnLogUpdate(Consumer<String> onLogUpdate) {
        this.onLogUpdate = onLogUpdate;
    }

    public Consumer<String> getOnFinished() {
        return onFinished;
    }

    public void setOnFinished(Consumer<String> onFinished) {
        this.onFinished = onFinished;
    }

    public SimulationManager(int numClients, int numQueues, int maxSimulationTime,
                             int minArrival, int maxArrival, int minService, int maxService) {
        this.numClients = numClients;
        this.numQueues = numQueues;
        this.maxSimulationTime = maxSimulationTime;
        this.minArrival = minArrival;
        this.maxArrival = maxArrival;
        this.minService = minService;
        this.maxService = maxService;
        this.servers = new ArrayList<>();
        for(int i = 1; i <= numQueues; i++){
            servers.add(new Server(i));
        }
        this.scheduler = new Scheduler(servers);
        this.generatedClients = generateClients(numClients, minArrival, maxArrival, minService, maxService);
        try {
            logWriter = new PrintWriter(new FileWriter("simulation_log.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.allClients = new ArrayList<>(generatedClients);
    }

    private List<Client> generateClients(int n, int minArrival,
                                                 int maxArrival, int minService, int maxService){
        List<Client> clients = new ArrayList<Client>();
        Random  rand = new Random();
        for(int i=1; i<=n; i++){
            int arrival = rand.nextInt(maxArrival - minArrival + 1) + minArrival;
            int service = rand.nextInt(maxService - minService + 1) + minService;
            clients.add(new Client(i, arrival, service));
        }
        clients.sort(Comparator.comparingInt(Client::getArrivalTime ));
        return clients;
    }

    public void run(){
        int peakHour = 0;
        int maxInSystem = 0;
        serverThreads.clear();
        for(Server s : servers){
            Thread t = new Thread(s);
            serverThreads.add(t);
            t.start();
        }
        for(int currentTime = 0; currentTime < maxSimulationTime && running; currentTime++) {

            Iterator<Client> it = generatedClients.iterator();
            while (it.hasNext()) {
                Client client = it.next();
                if (client.getArrivalTime() <= currentTime) {
                    scheduler.dispatchClient(client);
                    it.remove();
                }
            }

            int inSystem = 0;
            for(Server s : servers){
                inSystem += s.getClients().size();
            }
            if (inSystem > maxInSystem) {
                maxInSystem = inSystem;
                peakHour = currentTime;
            }
            logState(currentTime);
            if(generatedClients.isEmpty() && allQueuesEmpty())
                break;
            try{
                Thread.sleep(1000);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
                break;
            }
        }

        for(Server s : servers){
            s.stop();
        }
        for(Thread t : serverThreads){
            try{
                t.join(1000);
            }catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        double totalWaiting = 0;
        double totalService = 0;
        for(Client client:allClients){
            totalWaiting += client.getWaitingTime();
            totalService += client.getOriginalServiceTime();
        }

        double avgWaiting = totalWaiting/numClients;
        double avgService = totalService / numClients;

        logWriter.printf("Average waiting time: %.2f%n", avgWaiting);
        logWriter.printf("Average service time: %.2f%n", avgService);
        logWriter.printf("Peak hour: %d%n", peakHour);
        logWriter.flush();
        String stats = String.format("Avg waiting: %.2f | Avg service: %.2f | Peak Hour: %d", avgWaiting, avgService, peakHour);
        if(onFinished != null){
            onFinished.accept(stats);
        }
        logWriter.close();
    }

    private void logState(int currentTime){
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ").append(currentTime).append("\n");
        sb.append("Waiting clients:\n");

        if(generatedClients.isEmpty()){
            sb.append("-\n");
        }else{
            StringJoiner sj = new StringJoiner(", ");
            for(Client client : generatedClients){
                sj.add(client.toString());
            }
            sb.append(sj).append("\n");
        }
        for(Server server : servers){
            sb.append("Queue ").append(server.getId()).append(": ");
            sb.append(server.toString()).append("\n");
        }
        logWriter.print(sb);
        if(onLogUpdate != null){
            onLogUpdate.accept(sb.toString());
        }
        if(onQueueUpdate != null){
            onQueueUpdate.accept(servers);
        }
        logWriter.flush();
    }

    private boolean allQueuesEmpty(){
        for(Server server : servers){
            if(!server.isEmpty()) return false;
        }
        return true;
    }

    private volatile boolean running = true;

    public void stop() {
        running = false;
        for(Server s : servers){
            s.stop();
        }
        Thread.currentThread().interrupt();
    }

}
