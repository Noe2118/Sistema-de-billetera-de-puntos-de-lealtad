package Sistema;

import Usuarios.Cliente;
import Usuarios.Movimiento;
import Usuarios.Nivel;
import Recompensas.Recompensa;
import java.util.List;
import java.util.Scanner;

public class Main 
{
    // Se instancia el sistema de puntos para acceder desde el main
    public static Sistema sistema = new Sistema();

    public static void main(String[] args) 
    {
        Scanner lee = new Scanner(System.in);
        int opcion = 0;
        do {

        
            System.out.println("\n============= SISTEMA DE LEALTAD =============");
            System.out.println("1. Registrar nuevo cliente");
            System.out.println("2. Registrar compra");
            System.out.println("3. Ver catálogo de recompensas");
            System.out.println("4. Canjear recompensa");
            System.out.println("5. Consultar saldo y nivel de cliente");
            System.out.println("6. Consultar historial de movimientos");
            System.out.println("7. Devolver una compra");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            
            //se valida que la opción ingresada sea un número entero
            String entrada = lee.nextLine();
            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            if (opcion < 1 || opcion > 8) {
                System.out.println("Opción no válida. Intente de nuevo.");
            } else {
                switch (opcion) {
                case 1:
                    menuRegistrarCliente(lee);
                    break;
                case 2:
                    menuRegistrarCompra(lee);
                    break;
                case 3:
                    mostrarCatalogo();
                    break;
                case 4:
                    menuCanjearPremio(lee);
                    break;
                case 5:
                    consultarCliente(lee);
                    break;
                case 6:
                    consultarHistorial(lee);
                    break;
                case 7:
                    menuDevolverCompra(lee);
                    break;
                case 8:
                    System.out.println("Cerrando el sistema. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
                }
            }
        } while (opcion != 8);
        
        lee.close();
    }

    private static void menuRegistrarCliente(Scanner lee) 
    {
        System.out.print("\nIngrese ID del cliente: ");
        String id = lee.nextLine();
        System.out.print("Ingrese nombre completo: ");
        String nombre = lee.nextLine();
        System.out.print("Ingrese correo (debe contener @): ");
        String correo = lee.nextLine();
        System.out.print("Ingrese fecha de alta (DD-MM-AAAA): ");
        String fecha = lee.nextLine();

        try {
            sistema.registrarCliente(id, nombre, correo, fecha);
            System.out.println("Cliente registrado exitosamente.");
        } catch (Exceptions e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void menuRegistrarCompra(Scanner lee) 
    {
        System.out.print("\nIngrese ID del cliente: ");
        String idCliente = lee.nextLine();
        System.out.print("Ingrese el monto de la compra: $");
        //Se valida que el monto sea numérico y tenga como máximo dos decimales
        String entradaMonto = lee.nextLine();
        double monto;

        try {
            if (!entradaMonto.matches("[+-]?\\d+(\\.\\d{1,2})?")) {
                throw new NumberFormatException();
            }
            monto = Double.parseDouble(entradaMonto);
            if (!Double.isFinite(monto)) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            System.out.println("Monto no válido. Ingrese una cantidad numérica con máximo dos decimales.");
            return;
        }
        System.out.print("Ingrese fecha de la compra (DD-MM-AAAA): ");
        String fecha = lee.nextLine();
    
        try {
            Cliente clienteAntes = sistema.buscarClientePorId(idCliente);
            int saldoAnterior = clienteAntes == null ? 0 : clienteAntes.getSaldo();
            String idCompra = sistema.registrarCompra(idCliente, monto, fecha);
            Cliente c = sistema.buscarClientePorId(idCliente);
            System.out.println("Compra " + idCompra + " registrada. Puntos ganados: "
                    + (c.getSaldo() - saldoAnterior) + " | Saldo actual: "
                    + c.getSaldo() + " pts | Nivel: " + c.obtenerNivel());
        } catch (Exceptions e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void mostrarCatalogo() 
    {
        System.out.println("\n--- CATÁLOGO DE RECOMPENSAS ---");
        List<Recompensa> catalogo = sistema.getCatalogoRecompensas();
        for (Recompensa r : catalogo) {
            System.out.println("ID: " + r.getIdRecompensa() + 
                               " | " + r.getNombre() + 
                               " | Costo: " + r.getCostoPuntos() + " pts" + 
                               " | Stock: " + r.getStockDisponible());
        }
    }

    private static void menuCanjearPremio(Scanner lee) 
    {
        System.out.print("\nIngrese ID del cliente: ");
        String idCliente = lee.nextLine();
        System.out.print("Ingrese ID de la recompensa (ej. P01): ");
        String idPremio = lee.nextLine();
        System.out.print("Ingrese fecha del canje (DD-MM-AAAA): ");
        String fecha = lee.nextLine();

        try {
            sistema.canjearPremio(idCliente, idPremio, fecha);
            Cliente c = sistema.buscarClientePorId(idCliente);
            System.out.println("Canje exitoso. Tu nuevo saldo es de: " + c.getSaldo() + " pts.");
        } catch (Exceptions e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void menuDevolverCompra(Scanner lee) 
    {
        System.out.print("\nIngrese ID del cliente: ");
        String idCliente = lee.nextLine();
        System.out.print("Ingrese ID de la compra (ej. C-1): ");
        String idCompra = lee.nextLine();
        System.out.print("Ingrese fecha de la devolución (DD-MM-AAAA): ");
        String fecha = lee.nextLine();
        //Se realiza la devoluciom
        try {
            int puntosRevertidos = sistema.devolverCompra(idCliente, idCompra, fecha);
            Cliente cliente = sistema.buscarClientePorId(idCliente);
            System.out.println("Devolución registrada. Puntos revertidos: "
                    + puntosRevertidos + " | Saldo actual: " + cliente.getSaldo() + " pts.");
        } catch (Exceptions e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    private static void consultarCliente(Scanner lee) 
    {
        System.out.print("\nIngrese ID del cliente a consultar: ");
        String idCliente = lee.nextLine();
        Cliente cliente = sistema.buscarClientePorId(idCliente);
        // Se valida si el cliente existe antes de mostrar los datos
        if (cliente != null) {
            System.out.println("\n--- DATOS DEL CLIENTE ---");
            System.out.println("Nombre: " + cliente.getNombre());
            Nivel nivelActual = cliente.obtenerNivel();
            System.out.println("Nivel actual: " + nivelActual);
            System.out.println("Saldo disponible: " + cliente.getSaldo() + " pts");
            if (nivelActual == Nivel.ORO) {
                System.out.println("Nivel máximo alcanzado.");
            } else {
                Nivel nivelSiguiente = nivelActual == Nivel.BRONCE ? Nivel.PLATA : Nivel.ORO;
                int puntosFaltantes = nivelSiguiente.getPuntosMinimos()
                        - cliente.getPuntosAcumulados();
                System.out.println("Puntos faltantes para " + nivelSiguiente + ": "
                        + puntosFaltantes + " pts");
            }
        } else {
            System.out.println("ERROR: Cliente no encontrado.");
        }
    }

    private static void consultarHistorial(Scanner lee) 
    {
        System.out.print("\nIngrese ID del cliente: ");
        String idCliente = lee.nextLine();
        Cliente cliente = sistema.buscarClientePorId(idCliente);
        // Se valida si el cliente existe antes de mostrar el historial
        if (cliente != null) {
            System.out.println("\n--- HISTORIAL DE MOVIMIENTOS ---");
            List<Movimiento> historial = cliente.getHistorial();
            if (historial.isEmpty()) {
                System.out.println("El cliente no tiene movimientos registrados.");
            } else {
                for (Movimiento m : historial) {
                    System.out.println("[" + m.getFecha() + "] " + m.getTipo() + 
                                       " | Puntos: " + m.getPuntos() + 
                                       " | Ref: " + m.getReferencia());
                }
            }
        } else {
            System.out.println("ERROR: Cliente no encontrado.");
        }
    }
}