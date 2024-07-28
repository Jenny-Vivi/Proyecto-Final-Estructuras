package controlador;

import modelo.Laberinto;

import java.util.*;

public class LaberintoControlador {
    private Laberinto laberinto;
    private int startX, startY;
    private int endX, endY;
    private Map<String, List<int[]>> cache = new HashMap<>();

    public LaberintoControlador(int ancho, int altura) {
        laberinto = new Laberinto(ancho, altura);
    }

    public void setInicio(int x, int y) {
        this.startX = x;
        this.startY = y;
    }

    public void setFin(int x, int y) {
        this.endX = x;
        this.endY = y;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getEndX() {
        return endX;
    }

    public int getEndY() {
        return endY;
    }

    public Laberinto getLaberinto() {
        return laberinto;
    }

    public void toggleCelda(int x, int y) {
        laberinto.toggleCelda(x, y);
    }

    public List<int[]> solveBFS() {
        boolean[][] visited = new boolean[laberinto.getAncho()][laberinto.getAltura()];
        int[][] parent = new int[laberinto.getAncho() * laberinto.getAltura()][2];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[] { startX, startY });
        visited[startX][startY] = true;

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            if (x == endX && y == endY) {
                return buildPath(parent, x, y);
            }

            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                if (isValid(newX, newY, visited)) {
                    visited[newX][newY] = true;
                    queue.add(new int[] { newX, newY });
                    parent[newY * laberinto.getAncho() + newX] = new int[] { x, y };
                }
            }
        }
        return new ArrayList<>(); // No se encontró camino
    }

    public List<int[]> solveDFS() {
        boolean[][] visited = new boolean[laberinto.getAncho()][laberinto.getAltura()];
        int[][] parent = new int[laberinto.getAncho() * laberinto.getAltura()][2];
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[] { startX, startY });
        visited[startX][startY] = true;

        int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int x = current[0];
            int y = current[1];

            if (x == endX && y == endY) {
                return buildPath(parent, x, y);
            }

            for (int[] direction : directions) {
                int newX = x + direction[0];
                int newY = y + direction[1];

                if (isValid(newX, newY, visited)) {
                    visited[newX][newY] = true;
                    stack.push(new int[] { newX, newY });
                    parent[newY * laberinto.getAncho() + newX] = new int[] { x, y };
                }
            }
        }
        return new ArrayList<>(); // No se encontró camino
    }

    public List<int[]> solveWithCache() {
        String key = startX + "," + startY + "->" + endX + "," + endY;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        List<int[]> path = solveBFS(); // O cualquier otro método de resolución que quieras usar
        cache.put(key, path);
        return path;
    }

    public List<int[]> solveNormal() {
        return solveBFS(); // O solveDFS() si prefieres
    }

    private boolean isValid(int x, int y, boolean[][] visited) {
        return x >= 0 && x < laberinto.getAncho() && y >= 0 && y < laberinto.getAltura() && !visited[x][y]
                && laberinto.getCelda(x, y).getEstado();
    }

    private List<int[]> buildPath(int[][] parent, int x, int y) {
        List<int[]> path = new ArrayList<>();
        while (x != startX || y != startY) {
            path.add(new int[] { x, y });
            int[] p = parent[y * laberinto.getAncho() + x];
            x = p[0];
            y = p[1];
        }
        path.add(new int[] { startX, startY });
        return path;
    }
}
