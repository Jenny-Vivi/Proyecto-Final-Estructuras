package modelo;

public class Celda {
    private boolean estado; // true para camino, false para pared

    public Celda() {
        this.estado = true; // Por defecto, las celdas son caminos
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
