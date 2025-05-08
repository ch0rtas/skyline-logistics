package game;

import java.util.*;
import static game.VehiculoUtils.calcularCosteEnvio;
import static game.VehiculoRutaUtils.vehiculoPuedeRealizarRuta;

public class PedidoManager {
    public static void gestionarPedido(JuegoLogistica juego) {
        List<Pedido> pedidosPendientes = juego.getPedidosPendientes();
        Jugador jugador = juego.getJugador();
        Scanner scanner = juego.getScanner();
        String almacenPrincipal = juego.getAlmacenPrincipal();
        Random random = new Random();
        List<Vehiculo> flota = juego.getFlota();
        Map<String, Pedido> pedidos = juego.getPedidos();
        List<Pedido> pedidosEnCurso = juego.getPedidosEnCurso();

        if (pedidosPendientes.isEmpty()) {
            System.out.println("\n📭 No hay pedidos pendientes para gestionar");
            return;
        }

        juego.mostrarPedidosPendientes();
        System.out.print("\nIngrese ID del pedido a gestionar: ");
        String idPedido = scanner.nextLine();

        Pedido pedido = pedidos.get(idPedido);
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado");
            return;
        }

        // Verificar si hay vehículos disponibles para esta ruta
        boolean hayVehiculosDisponibles = false;
        for (Vehiculo v : flota) {
            if (v.estaDisponible() && 
                v.puedeTransportarTipo(pedido.getTipoPaquete()) &&
                vehiculoPuedeRealizarRuta(v, almacenPrincipal, pedido.getDestino())) {
                hayVehiculosDisponibles = true;
                break;
            }
        }

        if (!hayVehiculosDisponibles) {
            System.out.println("\n❌ No hay vehículos disponibles para realizar esta ruta");
            System.out.println("   - Origen: " + almacenPrincipal);
            System.out.println("   - Destino: " + pedido.getDestino());
            System.out.println("   - Tipo de carga: " + pedido.getTipoPaquete());

            System.out.println("\n¿Qué desea hacer con el pedido #" + idPedido + "?");
            System.out.println("02. Rechazar (Multa: $" + juego.calcularMultaRechazo(pedido) + ")");
            System.out.print("\nOpción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("02") || opcion.equals("2")) {
                String confirmacion;
                do {
                    System.out.print("¿Confirmar rechazo? (S/N): ");
                    confirmacion = scanner.nextLine().toUpperCase();
                } while (!confirmacion.equals("S") && !confirmacion.equals("N"));

                if (confirmacion.equals("S")) {
                    int multa = juego.calcularMultaRechazo(pedido);
                    jugador.gastar(multa); // Restar la multa del balance del jugador
                    juego.incrementarGastosAcumulados(multa); // Añadir la multa a los gastos acumulados
                    pedidosPendientes.remove(pedido);
                    System.out.println("❌ Pedido #" + idPedido + " rechazado");
                    System.out.println("💰 Multa aplicada: $" + multa);
                }
                return;
            } else {
                System.out.println("\n❌ Opción no válida");
                gestionarPedido(juego);
                return;
            }
        }

        System.out.println("\n¿Qué desea hacer con el pedido #" + idPedido + "?");
        System.out.println("01. Enviar");
        System.out.println("02. Rechazar (Multa: $" + juego.calcularMultaRechazo(pedido) + ")");
        System.out.print("\nOpción: ");
        String opcion = scanner.nextLine();

        if (opcion.equals("02") || opcion.equals("2")) {
            System.out.println("\n⚠️ ¿Está seguro de rechazar el pedido #" + idPedido + "?");
            System.out.println("   - Multa por rechazo: $" + juego.calcularMultaRechazo(pedido));
            System.out.print("   - Confirmar (s/N): ");

            String confirmacion = scanner.nextLine().toUpperCase();
            if (confirmacion.equals("S")) {
                int multa = juego.calcularMultaRechazo(pedido);
                jugador.gastar(multa); // Restar la multa del balance del jugador
                juego.incrementarGastosAcumulados(multa); // Añadir la multa a los gastos acumulados
                pedidosPendientes.remove(pedido);
                System.out.println("❌ Pedido #" + idPedido + " rechazado");
                System.out.println("💰 Multa aplicada: $" + multa);
            }
            return;
        } else if (!opcion.equals("01") && !opcion.equals("1")) {
            System.out.println("\n❌ Opción no válida");
            gestionarPedido(juego);
            return;
        }

        // Mostrar vehículos disponibles
        juego.mostrarVehiculosDisponibles(pedido);
        if (pedidosPendientes.isEmpty()) {
            return;
        }

        System.out.print("\nIngrese ID del vehículo a utilizar: ");
        String idVehiculo = scanner.nextLine().toUpperCase();

        Vehiculo vehiculoSeleccionado = null;
        for (Vehiculo v : flota) {
            if (v.getId().equals(idVehiculo)) {
                vehiculoSeleccionado = v;
                break;
            }
        }

        if (vehiculoSeleccionado == null) {
            System.out.println("❌ ID de vehículo no válido");
            return;
        }

        // Calcular costo total
        int costoTotal = calcularCosteEnvio(vehiculoSeleccionado, almacenPrincipal, pedido.getDestino());

        // Verificar balance
        if (jugador.getBalance() < costoTotal) {
            System.out.println("❌ Balance insuficiente para realizar el envío");
            return;
        }

        // Restar el costo del balance del jugador
        jugador.gastar(costoTotal);
        juego.incrementarGastosAcumulados(costoTotal);

        // Asignar vehículo al pedido
        vehiculoSeleccionado.asignarPedido(pedido);
        pedido.setTransporteAsignado(vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());

        // Resolver incidente si ocurre
        if (random.nextBoolean()) {
            juego.resolverIncidente(pedido);
        }

        pedidosPendientes.remove(pedido);
        pedidosEnCurso.add(pedido);
        System.out.println("\n✅ Pedido #" + idPedido + " gestionado exitosamente");
        System.out.println("   - Vehículo asignado: " + vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());
        System.out.println("   - Costo total: $" + costoTotal);
    }
}