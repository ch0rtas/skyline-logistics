package game;

import java.util.*;
import decorator.IVehiculo;
import template.AbstractImpuestosProcessor;

public class GameUIHelper {
    public static void mostrarFlota(List<IVehiculo> flota, Scanner scanner, Calendar fechaActual, String almacenPrincipal, JuegoLogistica juego) {
        System.out.println("\n🚗 FLOTA DE VEHÍCULOS:");

        // Calcular anchos máximos para cada columna
        String[] encabezados = {"TIPO", "ID", "CAPACIDAD", "VELOCIDAD", "COSTE/KM", "ESTADO", "SALUD", "CARGAS PERMITIDAS"};
        int[] anchos = new int[encabezados.length];

        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }

        // Calcular anchos máximos basados en el contenido
        for (IVehiculo vehiculo : flota) {
            String estado;
            if (vehiculo.getPedidoAsignado() != null) {
                Calendar fechaEntrega = vehiculo.getPedidoAsignado().getFechaEntregaCalendar();
                estado = "Ocupado (" + vehiculo.getPedidoAsignado().getId() + ") hasta " + JuegoLogistica.formatoFecha.format(fechaEntrega.getTime());
            } else {
                estado = "Disponible";
            }

            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                estado,
                vehiculo.getSalud() + "%",
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };

            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println(JuegoLogistica.generarFilaTabla(encabezados, anchos));
        System.out.println(JuegoLogistica.generarLineaSeparadora(anchos));

        // Mostrar datos
        for (IVehiculo vehiculo : flota) {
            String estado;
            if (vehiculo.getPedidoAsignado() != null) {
                Calendar fechaEntrega = vehiculo.getPedidoAsignado().getFechaEntregaCalendar();
                estado = "Ocupado (" + vehiculo.getPedidoAsignado().getId() + ") hasta " + JuegoLogistica.formatoFecha.format(fechaEntrega.getTime());
            } else {
                estado = "Disponible";
            }

            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                estado,
                vehiculo.getSalud() + "%",
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            System.out.println(JuegoLogistica.generarFilaTabla(valores, anchos));
        }

        System.out.println("\n01. Volver al menú partida");
        System.out.println("02. Reparar vehículo");
        System.out.println("03. Ver mercado de vehículos");
        System.out.print("\nSeleccione una opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "01":
            case "1":
                return;
            case "02":
            case "2":
                ReparacionVehiculos.repararVehiculo(juego);
                break;
            case "03":
            case "3":
                MercadoVehiculos.mostrarMercadoVehiculos(juego.getVehiculosMercado(), juego.getJugador(), 
                    juego.getFlota(), scanner, juego.getDificultad(), juego.getImpuestosProcessor());
                break;
            default:
                System.out.println("\n❌ Opción no válida");
        }
    }
}