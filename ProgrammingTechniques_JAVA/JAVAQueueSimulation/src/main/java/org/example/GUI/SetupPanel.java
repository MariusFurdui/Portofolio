package org.example.GUI;

import javax.swing.*;
import java.awt.*;

public class SetupPanel extends JPanel {
    private JTextField tfNumClients = new JTextField("4", 10);
    private JTextField tfNumQueues  = new JTextField("2", 10);
    private JTextField tfMaxSimTime = new JTextField("60", 10);
    private JTextField tfMinArrival = new JTextField("2", 10);
    private JTextField tfMaxArrival = new JTextField("30", 10);
    private JTextField tfMinService = new JTextField("2", 10);
    private JTextField tfMaxService = new JTextField("4", 10);
    private JButton btnStart = new JButton("Start Simulation");

    public SetupPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("Queue Management Simulator");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 20, 10);
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 10);

        addRow(gbc, 1, "Number of clients (N):", tfNumClients);
        addRow(gbc, 2, "Number of queues (Q):", tfNumQueues);
        addRow(gbc, 3, "Max simulation time:", tfMaxSimTime);
        addRow(gbc, 4, "Min arrival time:", tfMinArrival);
        addRow(gbc, 5, "Max arrival time:", tfMaxArrival);
        addRow(gbc, 6, "Min service time:", tfMinService);
        addRow(gbc, 7, "Max service time:", tfMaxService);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 10, 10, 10);
        add(btnStart, gbc);
    }

    private void addRow(GridBagConstraints gbc, int row, String label, JTextField tf) {
        gbc.gridx = 0; gbc.gridy = row;
        add(new JLabel(label), gbc);
        gbc.gridx = 1;
        add(tf, gbc);
    }

    public JButton getBtnStart() { return btnStart; }

    public int getNumClients()  { return parse(tfNumClients, 4); }
    public int getNumQueues()   { return parse(tfNumQueues, 2); }
    public int getMaxSimTime()  { return parse(tfMaxSimTime, 60); }
    public int getMinArrival()  { return parse(tfMinArrival, 2); }
    public int getMaxArrival()  { return parse(tfMaxArrival, 30); }
    public int getMinService()  { return parse(tfMinService, 2); }
    public int getMaxService()  { return parse(tfMaxService, 4); }

    private int parse(JTextField tf, int def) {
        try { return Integer.parseInt(tf.getText().trim()); }
        catch (NumberFormatException e) { return def; }
    }

    public boolean validateInput() {
        try {
            if (getNumClients() <= 0 || getNumQueues() <= 0 || getMaxSimTime() <= 0) throw new Exception();
            if (getMinArrival() > getMaxArrival()) throw new Exception();
            if (getMinService() <= 0 || getMinService() > getMaxService()) throw new Exception();
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Check all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}