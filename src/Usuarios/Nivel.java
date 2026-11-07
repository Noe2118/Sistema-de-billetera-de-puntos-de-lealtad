package Usuarios;

public enum Nivel {
    BRONCE(0),
    PLATA(1000),
    ORO(5000);

    private final int puntosMinimos;

    Nivel(int puntosMinimos) {
        this.puntosMinimos = puntosMinimos;
    }

    public int getPuntosMinimos() {
        return puntosMinimos;
    }
}
