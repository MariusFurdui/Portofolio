package org.example.GUI;

import org.example.Logic.SimulationManager;
import org.example.Model.Server;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SimulationFrame extends JFrame {
    private SetupPanel setupPanel;
    private SimulationPanel simulationPanel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private SimulationManager currentManager;

    public SimulationFrame() {
        super("Queue Management Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 680);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        setupPanel = new SetupPanel();
        simulationPanel = new SimulationPanel();

        mainPanel.add(setupPanel, "setup");
        mainPanel.add(simulationPanel, "simulation");
        add(mainPanel);

        setupPanel.getBtnStart().addActionListener(e -> startSimulation());

        cardLayout.show(mainPanel, "setup");
    }

    private void startSimulation() {
        if (!setupPanel.validateInput()) return;

        int n      = setupPanel.getNumClients();
        int q      = setupPanel.getNumQueues();
        int maxSim = setupPanel.getMaxSimTime();
        int minA   = setupPanel.getMinArrival();
        int maxA   = setupPanel.getMaxArrival();
        int minS   = setupPanel.getMinService();
        int maxS   = setupPanel.getMaxService();

        if (currentManager != null) currentManager.stop();

        currentManager = new SimulationManager(n, q, maxSim, minA, maxA, minS, maxS);

        simulationPanel.initQueues(q);
        cardLayout.show(mainPanel, "simulation");

        currentManager.setOnLogUpdate(text -> simulationPanel.appendLog(text));
        currentManager.setOnQueueUpdate(servers -> simulationPanel.updateQueues(servers));
        currentManager.setOnFinished(stats -> {
            simulationPanel.appendLog("\n--- SIMULATION COMPLETE ---\n" + stats);
            simulationPanel.showStats(stats.replace("\n", " | "));
        });

        Thread t = new Thread(currentManager);
        t.setDaemon(true);
        t.start();
    }
}