package game;

import java.util.List;
import java.util.Scanner;
import decorator.IVehiculo;
import template.AbstractImpuestosProcessor;

public class MercadoVehiculos {
    public static void mostrarMercadoVehiculos(List<IVehiculo> vehiculosMercado, Jugador jugador, 
            List<IVehiculo> flota, Scanner scanner, String dificultad, 
            AbstractImpuestosProcessor impuestosProcessor) {
        while (true) {
            System.out.println("\n=== 🚗 MERCADO DE VEHÍCULOS 🚗 ===");
            System.out.println("Balance actual: " + jugador.getBalance() + "€\n");

            for (int i = 0; i < vehiculosMercado.size(); i++) {
                IVehiculo vehiculo = vehiculosMercado.get(i);
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

                IVehiculo vehiculoSeleccionado = vehiculosMercado.get(indice - 1);
                if (jugador.getBalance() < vehiculoSeleccionado.getPrecio()) {
                    System.out.println("❌ No tienes suficiente balance para comprar este vehículo");
                    continue;
                }

                // Después de realizar la compra, actualizamos las estadísticas para reflejar el nuevo balance
                jugador.gastar(vehiculoSeleccionado.getPrecio());
                flota.add(vehiculoSeleccionado);
                vehiculosMercado.remove(vehiculoSeleccionado);

                System.out.println("✅ Has comprado el vehículo " + vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());
                System.out.println("💰 Balance actualizado: " + jugador.getBalance() + "€");

                // Llamada explícita para mostrar estadísticas actualizadas
                EstadisticasHelper.mostrarEstadisticas(jugador, 0, 0, vehiculoSeleccionado.getPrecio(), 0, 0, 0, 
                    dificultad, impuestosProcessor);
            } catch (NumberFormatException e) {
                System.out.println("❌ Opción no válida");
            }
        }
    }
}