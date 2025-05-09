package game;

import java.util.*;
import static game.VehiculoRutaUtils.vehiculoPuedeRealizarRuta;
import static game.DistanciaUtils.obtenerDistancia;
import decorator.IVehiculo;
import state.PedidoEnProcesoState;

public class PedidoManager {
    public static void gestionarPedido(JuegoLogistica juego) {
        List<Pedido> pedidosPendientes = juego.getPedidosPendientes();
        Jugador jugador = juego.getJugador();
        Scanner scanner = juego.getScanner();
        String almacenPrincipal = juego.getAlmacenPrincipal();
        List<IVehiculo> flota = juego.getFlota();
        Map<String, Pedido> pedidos = juego.getPedidos();
        List<Pedido> pedidosEnCurso = juego.getPedidosEnCurso();
        Calendar fechaActual = juego.getFechaActual();

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
        for (IVehiculo v : flota) {
            if (v.estaDisponible() && 
                v.puedeTransportarTipo(pedido.getTipoPaquete()) &&
                vehiculoPuedeRealizarRuta(v, almacenPrincipal, pedido.getDestino())) {
                hayVehiculosDisponibles = true;
                break;
            }
        }

        if (!hayVehiculosDisponibles) {
            System.out.println("\n❌ No hay vehículos disponibles para esta ruta");
            System.out.println("\nOpciones:");
            System.out.println("1. Cancelar pedido (penalización del 50%)");
            System.out.println("2. Volver al menú anterior");
            System.out.print("\nSeleccione una opción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("1")) {
                System.out.println("\n⚠️ ¿Está seguro de que desea cancelar el pedido?");
                System.out.println("Se aplicará una penalización del 50% del valor del pedido.");
                System.out.print("Ingrese 'SI' para confirmar: ");
                String confirmacion = scanner.nextLine();

                if (confirmacion.equalsIgnoreCase("SI")) {
                    pedido.cancelarPedido(juego);
                }
            }
            return;
        }

        // Mostrar vehículos disponibles
        juego.mostrarVehiculosDisponibles(pedido);

        // Seleccionar vehículo
        System.out.print("\nIngrese ID del vehículo a asignar: ");
        String idVehiculo = scanner.nextLine();

        IVehiculo vehiculoSeleccionado = flota.stream()
                .filter(v -> v.getId().equals(idVehiculo))
                .findFirst()
                .orElse(null);

        if (vehiculoSeleccionado == null) {
            System.out.println("❌ Vehículo no encontrado");
            return;
        }

        if (!vehiculoSeleccionado.estaDisponible()) {
            System.out.println("❌ El vehículo no está disponible");
            return;
        }

        if (!vehiculoSeleccionado.puedeTransportarTipo(pedido.getTipoPaquete())) {
            System.out.println("❌ El vehículo no puede transportar este tipo de carga");
            return;
        }

        if (!vehiculoPuedeRealizarRuta(vehiculoSeleccionado, almacenPrincipal, pedido.getDestino())) {
            System.out.println("❌ El vehículo no puede realizar esta ruta");
            return;
        }

        // Recalculate the delivery date based on the selected vehicle's speed and distance
        int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
        double horasViaje = (double) distancia / vehiculoSeleccionado.getVelocidad();

        // Adjust travel time based on vehicle type
        switch (vehiculoSeleccionado.getTipo()) {
            case "Furgoneta":
                horasViaje *= 1.2;
                break;
            case "Camión":
                horasViaje *= 1.3;
                break;
            case "Barco":
                horasViaje *= 1.5;
                break;
            case "Avión":
                horasViaje *= 1.1;
                break;
        }

        int diasViaje = (int) Math.ceil(horasViaje / 8.0);
        diasViaje = Math.max(1, diasViaje);

        Calendar fechaEntrega = (Calendar) fechaActual.clone();
        fechaEntrega.add(Calendar.DAY_OF_MONTH, diasViaje);

        // Set the recalculated delivery date
        pedido.setFechaEntrega(fechaEntrega);

        // Calcular el costo total del vehículo basado en la distancia y el costo por kilómetro
        int costoTotalVehiculo = (int) (distancia * vehiculoSeleccionado.getCostePorKm());

        // Descontar el costo total del vehículo del balance del jugador
        jugador.gastar(costoTotalVehiculo);
        System.out.println("💰 Se ha descontado el costo del vehículo: $" + costoTotalVehiculo);

        // Asignar pedido al vehículo
        vehiculoSeleccionado.asignarPedido(pedido);
        pedido.setTransporteAsignado(vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());
        pedido.setFechaDisponible(fechaEntrega);

        // Mover el pedido a la lista de pedidos en curso antes de cambiar su estado
        pedidosPendientes.remove(pedido);
        pedidosEnCurso.add(pedido);

        // Cambiar el estado del pedido a EN_PROCESO
        pedido.setEstado(new PedidoEnProcesoState());

        System.out.println("\n✅ Pedido asignado correctamente");
        System.out.println("📅 Fecha estimada de entrega: " + JuegoLogistica.formatoFecha.format(fechaEntrega.getTime()));
    }
}