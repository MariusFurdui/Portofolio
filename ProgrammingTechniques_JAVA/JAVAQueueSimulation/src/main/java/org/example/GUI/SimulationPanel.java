package org.example.GUI;

import org.example.Model.Server;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class SimulationPanel extends JPanel {
    private JTextArea logArea = new JTextArea();
    private JLabel lblStats = new JLabel(" ");
    private List<JTextArea> queueAreas = new ArrayList<>();
    private JPanel queuesPanel;

    public SimulationPanel() {
        setLayout(new BorderLayout(5, 5));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.add(new JLabel("  Simulation Log"), BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Log
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Event Log"));
        add(logScroll, BorderLayout.CENTER);

        // Stats
        lblStats.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblStats, BorderLayout.SOUTH);
    }

    public void initQueues(int n) {
        if(queuesPanel != null) {
            this.remove(queuesPanel);
        }

        queueAreas.clear();
        queuesPanel = new JPanel(new GridLayout(1, n, 10, 10));
        queuesPanel.setBorder(BorderFactory.createTitledBorder("Queues"));

        for(int i=1; i<=n;i++)
        {
            JPanel queueContainer = new JPanel(new BorderLayout());
            queueContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel title = new JLabel("Queue " + i, SwingConstants.CENTER);
            title.setFont(new Font("SansSerif", Font.BOLD, 12));

            JTextArea queueContent = new JTextArea(5, 20);
            queueContent.setEditable(false);
            queueContent.setFont(new Font("Monospaced", Font.PLAIN, 12));
            queueContent.setText("closed");
            queueAreas.add(queueContent);

            JScrollPane scroll = new JScrollPane(queueContent);

            queueContainer.add(title, BorderLayout.NORTH);
            queueContainer.add(scroll, BorderLayout.CENTER);
            queuesPanel.add(queueContainer);
        }
        add(queuesPanel, BorderLayout.NORTH);

        revalidate();
        repaint();
    }

    public void updateQueues(List<Server> servers) {
        SwingUtilities.invokeLater(()->{
            for(int i=0; i<servers.size() && i<queueAreas.size();i++){
                Server server = servers.get(i);
                String content = server.toString();
                queueAreas.get(i).setText(content);
            }
        });
    }

    public void appendLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.setText(text);
            //logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public void showStats(String stats) {
        SwingUtilities.invokeLater(() -> {
            lblStats.setText("<html><b>" + stats + "</b></html>");
        });
    }

}