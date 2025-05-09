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
        System.out.println("\n🚚 VEHÍCULOS DISPONIBLES:");
        System.out.println(" TIPO  | ID  | CAPACIDAD | VELOCIDAD | COSTE/KM | SALUD | DESGASTE | CARGAS PERMITIDAS               | COSTE TOTAL | FECHA ENTREGA |");
        System.out.println("-------+-----+-----------+-----------+----------+-------+----------+---------------------------------+-------------+---------------");

        for (IVehiculo vehiculo : flota) {
            if (vehiculo.estaDisponible() && 
                vehiculo.puedeTransportarTipo(pedido.getTipoPaquete()) &&
                vehiculoPuedeRealizarRuta(vehiculo, almacenPrincipal, pedido.getDestino())) {
                
                // Calcular la fecha de entrega
                int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
                double horasViaje = (double) distancia / vehiculo.getVelocidad();

                // Ajustar tiempo de viaje según el tipo de vehículo
                switch (vehiculo.getTipo()) {
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

                // Calcular el costo total
                int costoTotal = (int) (distancia * vehiculo.getCostePorKm());

                System.out.printf(" %-5s | %-3s | %-9d | %-9d | $%-7d | %-5s | %-8s | %-31s | $%-10d | %-12s |\n",
                    vehiculo.getTipo(),
                    vehiculo.getId(),
                    vehiculo.getCapacidad(),
                    vehiculo.getVelocidad(),
                    vehiculo.getCostePorKm(),
                    vehiculo.getSalud() + "%",
                    vehiculo.getDesgastePorViaje() + "%",
                    String.join(", ", vehiculo.getTiposPaquetesPermitidos()),
                    costoTotal,
                    JuegoLogistica.formatoFecha.format(fechaEntrega.getTime())
                );
            }
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