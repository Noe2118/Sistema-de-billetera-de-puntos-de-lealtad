package Recompensas;

public class Recompensa 
{
    private String idRecompensa;
    private String nombre;
    private int costoPuntos;
    private int stockDisponible;
    private TipoRecompensa tipoRecompensa;

    // Constructor de las recompensas
    public Recompensa(String idRecompensa, String nombre, int costoPuntos, int stockDisponible, TipoRecompensa tipoRecompensa) 
    {
        this.idRecompensa = idRecompensa;
        this.nombre = nombre;
        this.costoPuntos = costoPuntos;
        this.stockDisponible = stockDisponible;
        this.tipoRecompensa = tipoRecompensa;
    }

    // Getters y Setters
    public String getIdRecompensa() 
    {
        return idRecompensa;
    }
    public void setIdRecompensa(String idRecompensa) 
    {
        this.idRecompensa = idRecompensa;
    }

    public String getNombre() 
    {
        return nombre;
    }
    public void setNombre(String nombre) 
    {
        this.nombre = nombre;
    }

    public int getCostoPuntos() 
    {
        return costoPuntos;
    }
    public void setCostoPuntos(int costoPuntos) 
    {
        this.costoPuntos = costoPuntos;
    }

    public int getStockDisponible() 
    {
        return stockDisponible;
    }
    public void setStockDisponible(int stockDisponible) 
    {
        this.stockDisponible = stockDisponible;
    }

    public TipoRecompensa getTipoRecompensa() 
    {
        return tipoRecompensa;
    }
    public void setTipoRecompensa(TipoRecompensa tipoRecompensa) 
    {
        this.tipoRecompensa = tipoRecompensa;
    }

    //Control de stock de las recompensas
    public void reducirStock() 
    {
        if (this.stockDisponible > 0) {
            this.stockDisponible -= 1;
        }
    }
}