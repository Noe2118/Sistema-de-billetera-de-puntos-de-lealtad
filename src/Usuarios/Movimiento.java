package Usuarios;

public class Movimiento 
{
    private final TipoMovimiento tipo;
    private final int puntos;
    private final String fecha;
    private final String referencia; 

    public Movimiento(TipoMovimiento tipo, int puntos, String fecha, String referencia) 
    {
        this.tipo = tipo;
        this.puntos = puntos;
        this.fecha = fecha;
        this.referencia = referencia;
    }

    public TipoMovimiento getTipo() { return tipo; }
    public int getPuntos() { return puntos; }
    public String getFecha() { return fecha; }
    public String getReferencia() { return referencia; }
}