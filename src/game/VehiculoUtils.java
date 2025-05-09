package game;

import decorator.IVehiculo;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import static game.CityConstants.*;
import static game.DistanciaUtils.obtenerDistancia;
import static game.VehiculoRutaUtils.vehiculoPuedeRealizarRuta;

public class VehiculoUtils {
    public static void mostrarVehiculosDisponibles(Pedido pedido, List<IVehiculo> flota, Calendar fechaActual, String almacenPrincipal) {
        System.out.println("\n🚗 VEHÍCULOS DISPONIBLES:");

        // Filtrar vehículos disponibles
        List<IVehiculo> vehiculosDisponibles = flota.stream()
            .filter(v -> {
                // Verificar si el vehículo está disponible según su fecha de disponibilidad
                if (v.getFechaDisponibilidad() != null && fechaActual.before(v.getFechaDisponibilidad())) {
                    return false;
                }
                return v.estaDisponible() && v.puedeTransportarTipo(pedido.getTipoPaquete()) && v.getSalud() >= 10;
            })
            .filter(v -> vehiculoPuedeRealizarRuta(v, almacenPrincipal, pedido.getDestino()))
            .collect(Collectors.toList());

        if (vehiculosDisponibles.isEmpty()) {
            System.out.println("\n❌ No hay vehículos disponibles para esta ruta");
            return;
        }

        // Calcular anchos máximos para cada columna
        String[] encabezados = {"TIPO", "ID", "CAPACIDAD", "VELOCIDAD", "COSTE/KM", "SALUD", "DESGASTE", "CARGAS PERMITIDAS", "COSTE TOTAL", "FECHA ENTREGA"};
        int[] anchos = new int[encabezados.length];

        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }

        // Calcular anchos máximos basados en el contenido
        for (IVehiculo vehiculo : vehiculosDisponibles) {
            int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
            int costeTotal = calcularCosteEnvio(vehiculo, almacenPrincipal, pedido.getDestino());
            Calendar fechaEntrega = (Calendar) fechaActual.clone();
            fechaEntrega.add(Calendar.HOUR, vehiculo.calcularTiempoEntrega(distancia));

            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                vehiculo.getSalud() + "%",
                vehiculo.getDesgastePorViaje() + "%",
                String.join(", ", vehiculo.getTiposPaquetesPermitidos()),
                "$" + costeTotal,
                JuegoLogistica.formatoFecha.format(fechaEntrega.getTime())
            };

            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println(JuegoLogistica.generarFilaTabla(encabezados, anchos));
        System.out.println(JuegoLogistica.generarLineaSeparadora(anchos));

        // Mostrar datos
        for (IVehiculo vehiculo : vehiculosDisponibles) {
            int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
            int costeTotal = calcularCosteEnvio(vehiculo, almacenPrincipal, pedido.getDestino());
            Calendar fechaEntrega = (Calendar) fechaActual.clone();
            fechaEntrega.add(Calendar.HOUR, vehiculo.calcularTiempoEntrega(distancia));

            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                vehiculo.getSalud() + "%",
                vehiculo.getDesgastePorViaje() + "%",
                String.join(", ", vehiculo.getTiposPaquetesPermitidos()),
                "$" + costeTotal,
                JuegoLogistica.formatoFecha.format(fechaEntrega.getTime())
            };
            System.out.println(JuegoLogistica.generarFilaTabla(valores, anchos));
        }
    }

    public static int calcularCosteEnvio(IVehiculo vehiculo, String origen, String destino) {
        int distancia = obtenerDistancia(origen, destino);
        int costeBase = vehiculo.getCostePorKm() * distancia;

        // Ajustes específicos para barcos
        if (vehiculo.getTipo().equals("Barco")) {
            costeBase *= 1.5; // 50% más caro que la ruta terrestre equivalente

            if (esIsla(origen) && esIsla(destino)) {
                costeBase *= 1.3; // 30% más caro entre islas
            } else if (esIsla(origen) || esIsla(destino)) {
                costeBase *= 1.2; // 20% más caro entre isla y costa
            }
        }

        // Ajustes específicos para aviones
        if (vehiculo.getTipo().equals("Avión")) {
            costeBase *= 2.0; // 100% más caro que la ruta terrestre equivalente

            if (esIsla(origen) && esIsla(destino)) {
                costeBase *= 1.5; // 50% más caro entre islas
            } else if (esIsla(origen) || esIsla(destino)) {
                costeBase *= 1.3; // 30% más caro entre isla y costa
            }
        }

        return costeBase;
    }

    public static boolean esIsla(String ciudad) {
        return Arrays.asList(ISLAS).contains(ciudad);
    }
}