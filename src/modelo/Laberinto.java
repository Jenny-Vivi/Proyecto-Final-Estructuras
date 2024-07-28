package modelo;

public class Laberinto {
    private int ancho;
    private int altura;
    private Celda[][] celdas;

    public Laberinto(int ancho, int altura) {
        this.ancho = ancho;
        this.altura = altura;
        celdas = new Celda[ancho][altura];
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < altura; j++) {
                celdas[i][j] = new Celda();
            }
        }
    }

    public int getAncho() {
        return ancho;
    }

    public int getAltura() {
        return altura;
    }

    public Celda getCelda(int x, int y) {
        return celdas[x][y];
    }

    public void toggleCelda(int x, int y) {
        celdas[x][y].setEstado(!celdas[x][y].getEstado());
    }

    public void resetLaberinto() {
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < altura; j++) {
                celdas[i][j].setEstado(true); //todas las celdas a camino
            }
        }
    }
}
