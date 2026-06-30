package com.orders.presentation;

import javax.swing.*;
import java.awt.*;

/**
 * The type Main tab.
 */
public class MainTab extends JFrame {

    /**
     * Instantiates a new Main tab.
     */
    public MainTab() {
        setTitle("Orders Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));

        JButton clientsBtn = new JButton("Clients");
        JButton productsBtn = new JButton("Products");
        JButton ordersBtn = new JButton("Orders");

        clientsBtn.addActionListener(e -> new ClientTab().setVisible(true));
        productsBtn.addActionListener(e -> new ProductTab().setVisible(true));
        ordersBtn.addActionListener(e -> new OrderTab().setVisible(true));

        panel.add(clientsBtn);
        panel.add(productsBtn);
        panel.add(ordersBtn);

        add(panel);
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainTab().setVisible(true));
    }
}