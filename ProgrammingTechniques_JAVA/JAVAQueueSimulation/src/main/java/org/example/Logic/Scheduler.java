package org.example.Logic;

import org.example.Model.Client;
import org.example.Model.Server;

import java.util.List;

public class Scheduler {
    private List<Server> servers;

    public Scheduler(List<Server> servers){
        this.servers = servers;
    }

    public void dispatchClient(Client client){
        Server best = servers.get(0);
        int minWait = servers.get(0).getWaitingPeriod();

        for(Server server : servers){
            if(server.getWaitingPeriod() < minWait)
            {
                minWait = server.getWaitingPeriod();
                best = server;
            }
        }
        best.addClient(client);
    }
}
