package main;

import javax.swing.SwingUtilities;

import vista.MazeGenerator;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeGenerator::new);
    }
}