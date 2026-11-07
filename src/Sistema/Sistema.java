package Sistema;

import Usuarios.Cliente;
import Usuarios.Movimiento;
import Usuarios.Nivel;
import Usuarios.TipoMovimiento;
import Recompensas.Recompensa;
import Recompensas.TipoRecompensa;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoUnit;

public class Sistema 
{
    private List<Cliente> clientes;
    private List<Recompensa> catalogoRecompensas;
    //Contador para generar los id
    private int contadorCompras; 

    //Constructor de la clase SistemaLealtad
    public Sistema() 
    {
        this.clientes = new ArrayList<>();
        this.catalogoRecompensas = new ArrayList<>();
        this.contadorCompras = 1;
        
        inicializarCatalogo();
    }

    //Se inicializa el catálogo de recompensas
    private void inicializarCatalogo() 
    {
        catalogoRecompensas.add(new Recompensa("P01", "Café Americano", 100, 10, TipoRecompensa.PRODUCTO));
        catalogoRecompensas.add(new Recompensa("P02", "Cupón 10% Descuento", 250, 20, TipoRecompensa.CUPON));
        catalogoRecompensas.add(new Recompensa("P03", "Gift Card 50 u.m.", 800, 5, TipoRecompensa.DESCUENTO));
        catalogoRecompensas.add(new Recompensa("P04", "Audífonos Bluetooth", 1200, 3, TipoRecompensa.PRODUCTO));
        catalogoRecompensas.add(new Recompensa("P05", "Estancia en Hotel", 4000, 3, TipoRecompensa.PRODUCTO));
    }

    //Metodos de búsqueda
    public Cliente buscarClientePorId(String id) 
    {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }
        return null;
    }

    public Recompensa buscarRecompensaPorId(String id) 
    {   // Se busca la recompensa en el catálogo por su ID
        for (Recompensa recompensa : catalogoRecompensas) {
            if (recompensa.getIdRecompensa().equalsIgnoreCase(id)) {
                return recompensa;
            }
        }
        return null;
    }

    //Metodos de registro de clientes, compras y canjes
    public void registrarCliente(String id, String nombre, String correo, String fechaAlta) 
    {
        if (buscarClientePorId(id) != null) {
            throw new Exceptions("CLIENTE_DUPLICADO: El ID " + id + " ya está registrado.");
        }
        
        if (nombre == null || nombre.trim().isEmpty() || correo == null || !correo.contains("@")) {
            throw new Exceptions("DATOS_INVALIDOS: El nombre está vacío o el correo es inválido.");
        }
        
        clientes.add(new Cliente(id, nombre, correo, fechaAlta));
    }

    public String registrarCompra(String idCliente, double monto, String fecha) 
    {
        Cliente cliente = buscarClientePorId(idCliente);
        
        if (cliente == null) {
            throw new Exceptions("CLIENTE_NO_ENCONTRADO: El cliente " + idCliente + " no existe.");
        }
        if (!Double.isFinite(monto) || monto <= 0) {
            throw new Exceptions("MONTO_INVALIDO: El monto debe ser mayor a 0.");
        }

        // Determinar el multiplicador correspondiente
        Nivel nivelActual = cliente.obtenerNivel();
        double multiplicador = 1.0;
        
        if (nivelActual == Nivel.PLATA) {
            multiplicador = 1.25;
        } else if (nivelActual == Nivel.ORO) {
            multiplicador = 1.5;
        }
        //Calcular de puntos y regisatr el movimiento
        int puntosGanados = (int) Math.floor((monto / 10) * multiplicador);

        String referencia = "C-" + contadorCompras++;
        Movimiento mov = new Movimiento(TipoMovimiento.ACUMULACION, puntosGanados, fecha, referencia);
        cliente.agregarMovimiento(mov);
        return referencia;
    }

    public int devolverCompra(String idCliente, String idCompra, String fecha) 
    {
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            throw new Exceptions("CLIENTE_NO_ENCONTRADO: El cliente " + idCliente + " no existe.");
        }
        if (idCompra == null || idCompra.trim().isEmpty()) {
            throw new Exceptions("COMPRA_NO_ENCONTRADA: Debe ingresar el ID de la compra.");
        }
        idCompra = idCompra.trim();
        
        // Buscar la compra en el historial del cliente
        Movimiento compra = null;
        boolean yaDevuelta = false;
        for (Movimiento movimiento : cliente.getHistorial()) {
            if (idCompra.equals(movimiento.getReferencia())) {
                if (movimiento.getTipo() == TipoMovimiento.ACUMULACION) {
                    compra = movimiento;
                } else if (movimiento.getTipo() == TipoMovimiento.DEVOLUCION) {
                    yaDevuelta = true;
                }
            }
        }
        if (compra == null) {
            throw new Exceptions("COMPRA_NO_ENCONTRADA: La compra " + idCompra + " no existe para este cliente.");
        }
        if (yaDevuelta) {
            throw new Exceptions("COMPRA_YA_DEVUELTA: La compra " + idCompra + " ya fue devuelta.");
        }
        // Validar la fecha de devolución
        LocalDate fechaCompra = parsearFecha(compra.getFecha());
        LocalDate fechaDevolucion = parsearFecha(fecha);
        List<Movimiento> historial = cliente.getHistorial();
        LocalDate fechaUltimaOperacion = parsearFecha(historial.get(historial.size() - 1).getFecha());
        if (fechaDevolucion.isBefore(fechaUltimaOperacion)) {
            throw new Exceptions("FECHA_INVALIDA: La devolución no puede ser anterior a la última operación.");
        }
        long diasDesdeCompra = ChronoUnit.DAYS.between(fechaCompra, fechaDevolucion);
        if (diasDesdeCompra < 0) {
            throw new Exceptions("FECHA_INVALIDA: La devolución no puede ser anterior a la compra.");
        }
        if (diasDesdeCompra > 30) {
            throw new Exceptions("DEVOLUCION_FUERA_DE_PLAZO: Solo se puede devolver dentro de los 30 días siguientes a la compra.");
        }
        // Registrar la devolución y revertir los puntos
        int puntosRevertidos = compra.getPuntos();
        cliente.agregarMovimiento(new Movimiento(
                TipoMovimiento.DEVOLUCION, -puntosRevertidos, fecha, idCompra));
        return puntosRevertidos;
    }
    
    private LocalDate parsearFecha(String fecha) 
    {
        if (fecha == null) {
            throw new Exceptions("FECHA_INVALIDA: La fecha debe tener formato DD-MM-AAAA y ser válida.");
        }
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-uuuu")
                .withResolverStyle(ResolverStyle.STRICT);
        try {
            return LocalDate.parse(fecha, formato);
        } catch (DateTimeParseException e) {
            throw new Exceptions("FECHA_INVALIDA: La fecha debe tener formato DD-MM-AAAA y ser válida.");
        }
    }
    
    public void canjearPremio(String idCliente, String idPremio, String fecha) 
    {
        // Validar cliente y premio
        Cliente cliente = buscarClientePorId(idCliente);
        if (cliente == null) {
            throw new Exceptions("CLIENTE_NO_ENCONTRADO: El cliente " + idCliente + " no existe.");
        }

        Recompensa premio = buscarRecompensaPorId(idPremio);
        if (premio == null) {
            throw new Exceptions("PREMIO_NO_ENCONTRADO: El premio " + idPremio + " no existe.");
        }

        if (premio.getStockDisponible() <= 0) {
            throw new Exceptions("STOCK_AGOTADO: El premio " + premio.getNombre() + " no tiene existencias.");
        }

        if (cliente.getSaldo() < premio.getCostoPuntos()) {
            throw new Exceptions("SALDO_INSUFICIENTE: Necesitas " + premio.getCostoPuntos() + " puntos, pero tienes " + cliente.getSaldo() + ".");
        }

        // Se reduce el stock y se registra el movimiento 
        premio.reducirStock();
        Movimiento mov = new Movimiento(TipoMovimiento.CANJE, -premio.getCostoPuntos(), fecha, premio.getIdRecompensa());
        cliente.agregarMovimiento(mov);
    }
    
    // Getters para acceder a las listas 
    public List<Cliente> getClientes() 
    {
        return clientes;
    }
    
    public List<Recompensa> getCatalogoRecompensas() 
    {
        return catalogoRecompensas;
    }
}