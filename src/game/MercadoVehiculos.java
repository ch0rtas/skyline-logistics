package game;

import java.util.List;
import java.util.Scanner;

public class MercadoVehiculos {
    public static void mostrarMercadoVehiculos(List<Vehiculo> vehiculosMercado, Jugador jugador, List<Vehiculo> flota, Scanner scanner) {
        while (true) {
            System.out.println("\n=== 🚗 MERCADO DE VEHÍCULOS 🚗 ===");
            System.out.println("Balance actual: " + jugador.getBalance() + "€\n");

            for (int i = 0; i < vehiculosMercado.size(); i++) {
                Vehiculo vehiculo = vehiculosMercado.get(i);
                String indice = String.format("%02d", i + 1);
                System.out.println(indice + ". " + vehiculo.getTipo() + " " + vehiculo.getId());
                System.out.println("   Capacidad: " + vehiculo.getCapacidad() + " kg");
                System.out.println("   Velocidad: " + vehiculo.getVelocidad() + " km/h");
                System.out.println("   Consumo: " + vehiculo.getConsumo() + " L/100km");
                System.out.println("   Precio: " + vehiculo.getPrecio() + "€\n");
            }

            System.out.println("0. Volver al menú principal\n");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("0") || opcion.equals("00")) {
                break; // Salir del mercado de vehículos
            }

            try {
                int indice = Integer.parseInt(opcion);
                if (indice < 1 || indice > vehiculosMercado.size()) {
                    System.out.println("❌ Opción no válida");
                    continue;
                }

                Vehiculo vehiculoSeleccionado = vehiculosMercado.get(indice - 1);
                if (jugador.getBalance() < vehiculoSeleccionado.getPrecio()) {
                    System.out.println("❌ No tienes suficiente balance para comprar este vehículo");
                    continue;
                }

                jugador.gastar(vehiculoSeleccionado.getPrecio());
                flota.add(vehiculoSeleccionado);
                vehiculosMercado.remove(vehiculoSeleccionado);

                System.out.println("✅ Has comprado el vehículo " + vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());
            } catch (NumberFormatException e) {
                System.out.println("❌ Opción no válida");
            }
        }
    }
}