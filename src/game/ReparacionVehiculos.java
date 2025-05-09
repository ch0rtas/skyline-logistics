package game;

import java.util.List;
import java.util.stream.Collectors;
import decorator.IVehiculo;

public class ReparacionVehiculos {
    public static void repararVehiculo(JuegoLogistica juego) {
        List<IVehiculo> vehiculosReparables = juego.getFlota().stream()
            .filter(v -> v.getSalud() < 100 && v.getPedidoAsignado() == null)
            .collect(Collectors.toList());

        if (vehiculosReparables.isEmpty()) {
            System.out.println("\n❌ No tienes vehículos disponibles para reparar");
            return;
        }

        System.out.println("\n=== 🔧 REPARACIÓN DE VEHÍCULOS 🔧 ===");
        System.out.println("Balance actual: " + juego.getJugador().getBalance() + "€");

        for (int i = 0; i < vehiculosReparables.size(); i++) {
            IVehiculo v = vehiculosReparables.get(i);
            System.out.printf("\n%02d. %s\n", i + 1, v.getNombre());
            System.out.println("   Salud: " + v.getSalud() + "%");
            System.out.println("   Coste de reparación: " + v.getCosteReparacion() + "€");
        }

        System.out.println("\n0. Volver al menú principal");
        System.out.print("\nSeleccione un vehículo para reparar (0 para volver): ");
        String opcion = juego.getScanner().nextLine();

        if (opcion.equals("0")) {
            juego.mostrarMenuPartida();
            return;
        }

        try {
            int indice = Integer.parseInt(opcion);
            if (indice < 1 || indice > vehiculosReparables.size()) {
                System.out.println("❌ Opción no válida");
                return;
            }

            IVehiculo vehiculoSeleccionado = vehiculosReparables.get(indice - 1);
            int costeReparacion = vehiculoSeleccionado.getCosteReparacion();

            if (juego.getJugador().getBalance() < costeReparacion) {
                System.out.println("❌ No tienes suficiente balance para reparar este vehículo");
                return;
            }

            juego.getJugador().gastar(costeReparacion);
            vehiculoSeleccionado.reparar();

            System.out.println("✅ Has reparado el vehículo " + vehiculoSeleccionado.getNombre());
            System.out.println("💰 Balance actualizado: " + juego.getJugador().getBalance() + "€");
        } catch (NumberFormatException e) {
            System.out.println("❌ Opción no válida");
        }
    }
}