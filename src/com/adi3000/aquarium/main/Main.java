package com.adi3000.aquarium.main;

import javax.swing.*;

public class Main {
    
    public static void main(String[] args) {
        JFrame window = new JFrame("Aquarium Simulator");
        window.setContentPane(new Game());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        window.setIgnoreRepaint(true);
    }
    
}
