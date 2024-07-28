package vista;

import controlador.LaberintoControlador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

public class MazeGenerator {
    private JTextField widthField;
    private JTextField heightField;
    private JTextField startXField;
    private JTextField startYField;
    private JTextField endXField;
    private JTextField endYField;
    private JPanel gridPanel;
    private JButton generateButton;
    private JButton clearButton;
    private JButton bfsButton;
    private JButton dfsButton;
    private JButton cacheButton;
    private JButton normalButton;
    private LaberintoControlador controlador;
    private JTextArea resultArea;
    private JPanel mazePanel;

    public MazeGenerator() {
        JFrame frame = new JFrame("Generador de laberintos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600); // Aumentar el tamaño para incluir el área de resultados
        frame.setLayout(new BorderLayout());
        // Inicialización del laberinto y otros componentes
        mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Código para dibujar el laberinto
            }
        };

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setBackground(new Color(255, 255, 0));

        widthField = new JTextField(5);
        heightField = new JTextField(5);
        startXField = new JTextField(3);
        startYField = new JTextField(3);
        endXField = new JTextField(3);
        endYField = new JTextField(3);

        generateButton = new JButton("Generar laberinto");
        clearButton = new JButton("Limpiar");
        bfsButton = new JButton("Resolver con BFS");
        dfsButton = new JButton("Resolver con DFS");
        cacheButton = new JButton("Resolver con Cache");
        normalButton = new JButton("Resolver Normal");

        JButton[] buttons = { generateButton, clearButton, bfsButton, dfsButton, cacheButton, normalButton };
        for (JButton button : buttons) {
            button.setBackground(Color.BLACK); // Fondo negro
            button.setForeground(Color.WHITE); // Texto blanco
        }

        controlPanel.add(new JLabel("Ancho: "));
        controlPanel.add(widthField);
        controlPanel.add(new JLabel("Altura: "));
        controlPanel.add(heightField);
        controlPanel.add(new JLabel("Inicio X: "));
        controlPanel.add(startXField);
        controlPanel.add(new JLabel("Fin X: "));
        controlPanel.add(startYField);
        controlPanel.add(new JLabel("Inicio Y: "));
        controlPanel.add(endXField);
        controlPanel.add(new JLabel("Fin Y: "));
        controlPanel.add(endYField);
        controlPanel.add(generateButton);
        controlPanel.add(clearButton);

        frame.add(controlPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(new Color(0, 0, 205));

        bottomPanel.add(bfsButton);
        bottomPanel.add(dfsButton);
        bottomPanel.add(cacheButton);
        bottomPanel.add(normalButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        gridPanel = new JPanel();
        frame.add(gridPanel, BorderLayout.CENTER);

        // Crear el área de resultados
        resultArea = new JTextArea(10, 30);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        frame.add(scrollPane, BorderLayout.EAST);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMaze();
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });

        bfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveMaze("BFS");
            }
        });

        dfsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveMaze("DFS");
            }
        });

        cacheButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int startX = Integer.parseInt(startXField.getText());
                    int startY = Integer.parseInt(startYField.getText());
                    int endX = Integer.parseInt(endXField.getText());
                    int endY = Integer.parseInt(endYField.getText());

                    controlador.setInicio(startX, startY);
                    controlador.setFin(endX, endY);

                    solveMaze("CACHE");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                                                 "Por favor ingrese números enteros válidos.");
                }
            }
        });

        normalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int startX = Integer.parseInt(startXField.getText());
                    int startY = Integer.parseInt(startYField.getText());
                    int endX = Integer.parseInt(endXField.getText());
                    int endY = Integer.parseInt(endYField.getText());

                    controlador.setInicio(startX, startY);
                    controlador.setFin(endX, endY);

                    solveMaze("NORMAL");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, 
                                            "Por favor ingrese números enteros válidos.");
                }
            }
        });

        frame.setVisible(true);
    }

    private void generateMaze() {
        int width, height;
        int startX, startY, endX, endY;
        try {
            width = Integer.parseInt(widthField.getText());
            height = Integer.parseInt(heightField.getText());
            startX = Integer.parseInt(startXField.getText());
            startY = Integer.parseInt(startYField.getText());
            endX = Integer.parseInt(endXField.getText());
            endY = Integer.parseInt(endYField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, 
                                    "Por favor ingrese números enteros válidos.");
            return;
        }

        if (width <= 0 || height <= 0 || startX < 0 || startX >= width || startY < 0 ||
                startY >= height || endX < 0 || endX >= width || endY < 0 || endY >= height) {

            JOptionPane.showMessageDialog(null, 
                                        "Por favor introduzca dimensiones y puntos válidos.");
            return;
        }

        controlador = new LaberintoControlador(width, height);
        controlador.setInicio(startX, startY);
        controlador.setFin(endX, endY);

        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(height, width));

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final int row = i;
                final int col = j;
                JButton button = new JButton();
                button.setBackground(Color.WHITE);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controlador.toggleCelda(col, row); // Corregido el orden de las coordenadas
                        button.setBackground(
                                controlador.getLaberinto().getCelda(col, row).getEstado() ? 
                                Color.WHITE : Color.GRAY);
                    }
                });

                // Marca las celdas de inicio y fin
                if (i == startY && j == startX) {
                    button.setBackground(Color.GREEN); // Inicio en verde
                } else if (i == endY && j == endX) {
                    button.setBackground(Color.RED); // Fin en rojo
                }

                gridPanel.add(button);
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void clearFields() {
        widthField.setText("");
        heightField.setText("");
        startXField.setText("");
        startYField.setText("");
        endXField.setText("");
        endYField.setText("");
        gridPanel.removeAll();
        resultArea.setText(""); // Limpiar el área de resultados
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void solveMaze(String method) {
        if (controlador == null) {
            JOptionPane.showMessageDialog(null, 
                                        "Por favor genera un laberinto primero.");
            return;
        }

        List<int[]> path;
        long startTime = System.nanoTime(); // Captura el tiempo en nanosegundos antes de iniciar la resolución

        // Resolución del laberinto con el método seleccionado
        switch (method) {
            case "BFS":
                path = controlador.solveBFS();
                break;
            case "DFS":
                path = controlador.solveDFS();
                break;
            case "CACHE":
                path = controlador.solveWithCache();
                break;
            case "NORMAL":
                path = controlador.solveNormal();
                break;
            default:
                throw new IllegalArgumentException("Método no válido: " + method);
        }

        long endTime = System.nanoTime(); // Captura el tiempo en nanosegundos después de finalizar la resolución
        double duration = (endTime - startTime) / 1e9; // Calcula la duración en segundos

        // Formatear el tiempo en segundos
        String formattedDuration = String.format(Locale.US, "%.8f segundos", 
                                                duration).replace('.', ',');

        // Mostrar la duración en el área de resultados
        resultArea.append("Duración de " + method + ": " + formattedDuration + "\n");

        if (path == null || path.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontró un camino.");
            resultArea.append("No se encontró un camino.\n\n"); // Agregar al área de resultados con un salto de línea
                                                                // adicional
            return;
        }

        clearMazePath(); // Limpiar el laberinto visual antes de dibujar el nuevo camino
        drawPath(path, method); // Pasar el método para usar colores específicos
        resultArea.append("Camino encontrado: " + pathToString(path) + "\n"); // Mostrar el camino encontrado en el área
                                                                              // de resultados

        // Calcular y mostrar la información adicional sobre el camino
        int steps = path.size();
        resultArea.append("Número de pasos: " + steps + "\n");

        List<int[]> bfsPath = controlador.solveBFS();
        List<int[]> dfsPath = controlador.solveDFS();
        List<int[]> cachePath = controlador.solveWithCache();
        List<int[]> normalPath = controlador.solveNormal();

        int bfsSteps = bfsPath.size();
        int dfsSteps = dfsPath.size();
        int cacheSteps = cachePath.size();
        int normalSteps = normalPath.size();

        int betterRoutes = 0;
        int stepsDifference = 0;

        if (steps < bfsSteps) {
            betterRoutes++;
            stepsDifference += (bfsSteps - steps);
        }
        if (steps < dfsSteps) {
            betterRoutes++;
            stepsDifference += (dfsSteps - steps);
        }
        if (steps < cacheSteps) {
            betterRoutes++;
            stepsDifference += (cacheSteps - steps);
        }
        if (steps < normalSteps) {
            betterRoutes++;
            stepsDifference += (normalSteps - steps);
        }

        resultArea.append("Superó " + betterRoutes + " otras rutas por " + stepsDifference + " pasos.\n\n");
    }

    private void displayPath(List<int[]> path) {
        if (path == null || path.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontró un camino.");
            return;
        }

        clearMazePath(); // Limpiar el laberinto visual antes de dibujar el nuevo camino
        drawPath(path, ""); // Dibujar el camino sin especificar un método específico
    }

    // Método para convertir el camino en una cadena legible
    private String pathToString(List<int[]> path) {
        StringBuilder sb = new StringBuilder();
        for (int[] step : path) {
            sb.append("(").append(step[0]).append(", ").append(step[1]).append(") ");
        }
        return sb.toString();
    }

    private int[][] laberinto;

    private void clearMazePath() {
        // Verifica si la matriz laberinto está correctamente inicializada
        if (laberinto == null) {
            return;
        }

        // Limpia el camino visual del laberinto
        for (int i = 0; i < laberinto.length; i++) {
            for (int j = 0; j < laberinto[0].length; j++) {
                if (laberinto[i][j] == 2) { // Asumiendo que '2' representa el camino visual
                    laberinto[i][j] = 0; // Restaurar a su estado original
                }
            }
        }
        mazePanel.repaint();
    }

    private void drawPath(List<int[]> path, String method) {
        Component[] components = gridPanel.getComponents();
        int startX = controlador.getStartX();
        int startY = controlador.getStartY();
        int endX = controlador.getEndX();
        int endY = controlador.getEndY();
        Color pathColor;

        switch (method) {
            case "BFS":
                pathColor = Color.BLUE;
                break;
            case "DFS":
                pathColor = Color.ORANGE;
                break;
            case "CACHE":
                pathColor = Color.MAGENTA;
                break;
            case "NORMAL":
                pathColor = Color.CYAN;
                break;
            default:
                pathColor = Color.BLUE;
        }

        for (int[] step : path) {
            int index = step[1] * controlador.getLaberinto().getAncho() + step[0]; // Cambiado para usar correctamente
                                                                                   // las coordenadas
            if (index < components.length && !(step[0] == startX && step[1] == startY)
                    && !(step[0] == endX && step[1] == endY)) {
                components[index].setBackground(pathColor);
            }
        }
    }

}
