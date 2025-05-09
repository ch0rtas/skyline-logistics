package game;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import static game.CityConstants.*;
import static game.DistanciaUtils.obtenerDistancia;
import static game.VehiculoRutaUtils.vehiculoPuedeRealizarRuta;

public class VehiculoUtils {
    public static void mostrarVehiculosDisponibles(Pedido pedido, List<Vehiculo> flota, Calendar fechaActual, String almacenPrincipal) {
        System.out.println("\n🚗 VEHÍCULOS DISPONIBLES:");

        // Filtrar vehículos disponibles
        List<Vehiculo> vehiculosDisponibles = flota.stream()
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
        for (Vehiculo vehiculo : vehiculosDisponibles) {
            // Calcular tiempo de entrega basado en la velocidad y distancia
            int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());

            // Calcular horas de viaje basadas en la velocidad real del vehículo
            double horasViaje = (double) distancia / vehiculo.getVelocidad();

            // Ajustar horas según el tipo de vehículo
            switch (vehiculo.getTipo()) {
                case "Furgoneta":
                    horasViaje *= 1.2; // 20% más lento por paradas y tráfico
                    break;
                case "Camión":
                    horasViaje *= 1.3; // 30% más lento por paradas y restricciones
                    break;
                case "Barco":
                    horasViaje *= 1.5; // 50% más lento por condiciones marítimas
                    break;
                case "Avión":
                    horasViaje *= 1.1; // 10% más lento por procedimientos aeroportuarios
                    break;
            }

            // Convertir horas a días (considerando 8 horas de trabajo por día)
            int diasViaje = (int) Math.ceil(horasViaje / 8.0);

            // Asegurar un mínimo de 1 día de viaje
            diasViaje = Math.max(1, diasViaje);

            Calendar fechaEntrega = (Calendar) fechaActual.clone();
            // Ajustar para que la fecha de entrega sea consistente con la selección del jugador
            fechaEntrega.add(Calendar.DAY_OF_MONTH, diasViaje);

            // Calcular coste total del envío
            int costeTotal = calcularCosteEnvio(vehiculo, almacenPrincipal, pedido.getDestino());

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
        for (Vehiculo vehiculo : vehiculosDisponibles) {
            // Calcular tiempo de entrega basado en la velocidad y distancia
            int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());

            // Calcular horas de viaje basadas en la velocidad real del vehículo
            double horasViaje = (double) distancia / vehiculo.getVelocidad();

            // Ajustar horas según el tipo de vehículo
            switch (vehiculo.getTipo()) {
                case "Furgoneta":
                    horasViaje *= 1.2; // 20% más lento por paradas y tráfico
                    break;
                case "Camión":
                    horasViaje *= 1.3; // 30% más lento por paradas y restricciones
                    break;
                case "Barco":
                    horasViaje *= 1.5; // 50% más lento por condiciones marítimas
                    break;
                case "Avión":
                    horasViaje *= 1.1; // 10% más lento por procedimientos aeroportuarios
                    break;
            }

            // Convertir horas a días (considerando 8 horas de trabajo por día)
            int diasViaje = (int) Math.ceil(horasViaje / 8.0);

            // Asegurar un mínimo de 1 día de viaje
            diasViaje = Math.max(1, diasViaje);

            Calendar fechaEntrega = (Calendar) fechaActual.clone();
            // Ajustar para que la fecha de entrega sea consistente con la selección del jugador
            fechaEntrega.add(Calendar.DAY_OF_MONTH, diasViaje);

            // Calcular coste total del envío
            int costeTotal = calcularCosteEnvio(vehiculo, almacenPrincipal, pedido.getDestino());

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

    public static int calcularCosteEnvio(Vehiculo vehiculo, String origen, String destino) {
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