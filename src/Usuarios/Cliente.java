package Usuarios;

import java.util.ArrayList;
import java.util.List;

public class Cliente 
{
    private String id;
    private String nombre;
    private String correo;
    private String fechaAlta;
    private int saldo;
    private int puntosAcumulados;
    private List<Movimiento> historial;

    public Cliente(String id, String nombre, String correo, String fechaAlta) 
    {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.fechaAlta = fechaAlta;
        this.saldo = 0;
        this.puntosAcumulados = 0;
        this.historial = new ArrayList<>();
    } 

    // Calcular el nivel del cliente basado en los puntos 
    public Nivel obtenerNivel() 
    {
        if (this.puntosAcumulados >= 5000) {
            return Nivel.ORO;
        } else if (this.puntosAcumulados >= 1000) {
            return Nivel.PLATA;
        } else {
            return Nivel.BRONCE;
        }
    }

    // Registrar la transaccion, los puntos y el saldo del cliente
    public void agregarMovimiento(Movimiento mov) 
    {
        this.historial.add(mov);
        this.saldo += mov.getPuntos(); 
        // Actualizar puntos acumulados si es una acumulación o devolución
        if (mov.getTipo() == TipoMovimiento.ACUMULACION) {
            this.puntosAcumulados += mov.getPuntos();
        } else if (mov.getTipo() == TipoMovimiento.DEVOLUCION) {
            this.puntosAcumulados += mov.getPuntos();
        }
    }

    // Getters y Setters para datos personales
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(String fechaAlta) { this.fechaAlta = fechaAlta; }

    // Getters de lectura
    public int getSaldo() { return saldo; }
    public int getPuntosAcumulados() { return puntosAcumulados; }
    public List<Movimiento> getHistorial() { return historial; }
}