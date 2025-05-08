package game;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReparacionVehiculos {
    public static void repararVehiculo(JuegoLogistica juego) {
        List<Vehiculo> vehiculosReparables = juego.getFlota().stream()
            .filter(v -> v.getSalud() < 100 && v.getPedidoAsignado() == null)
            .collect(Collectors.toList());

        if (vehiculosReparables.isEmpty()) {
            System.out.println("\n❌ No tienes vehículos disponibles para reparar");
            return;
        }

        System.out.println("\n=== 🔧 REPARACIÓN DE VEHÍCULOS 🔧 ===");
        System.out.println("Balance actual: " + juego.getJugador().getBalance() + "€");

        for (int i = 0; i < vehiculosReparables.size(); i++) {
            Vehiculo v = vehiculosReparables.get(i);
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
            int indice = Integer.parseInt(opcion) - 1;
            if (indice >= 0 && indice < vehiculosReparables.size()) {
                Vehiculo vehiculoSeleccionado = vehiculosReparables.get(indice);
                if (juego.getModoJuego().equals("libre") || juego.getJugador().getBalance() >= vehiculoSeleccionado.getCosteReparacion()) {
                    if (!juego.getModoJuego().equals("libre")) {
                        juego.getJugador().gastar(vehiculoSeleccionado.getCosteReparacion());
                    }
                    vehiculoSeleccionado.reparar();
                    System.out.println("\n✅ Has reparado el " + vehiculoSeleccionado.getNombre());
                } else {
                    System.out.println("\n❌ No tienes suficiente dinero para reparar este vehículo");
                }
            } else {
                System.out.println("\n❌ Opción no válida");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Por favor, introduce un número válido");
        }

        repararVehiculo(juego);
    }
}