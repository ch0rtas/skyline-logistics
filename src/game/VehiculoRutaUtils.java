package game;

import decorator.IVehiculo;
import java.util.Arrays;

public class VehiculoRutaUtils {
    /**
     * Verifica si un vehículo puede realizar una ruta específica
     * @param vehiculo Vehículo a verificar
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return true si el vehículo puede realizar la ruta, false si no
     */
    public static boolean vehiculoPuedeRealizarRuta(IVehiculo vehiculo, String origen, String destino) {
        switch (vehiculo.getTipo()) {
            case "Furgoneta":
            case "Camión":
            case "Camion":
                return RutaUtils.existeRutaTerrestre(origen, destino);
            case "Barco":
                return esRutaMaritima(origen, destino);
            case "Avión":
                return true; // Los aviones pueden ir a cualquier parte
            default:
                return false;
        }
    }

    /**
     * Verifica si una ruta es marítima entre dos ciudades
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return true si la ruta es marítima, false si no
     */
    private static boolean esRutaMaritima(String origen, String destino) {
        return Arrays.asList(CityConstants.CIUDADES_MARITIMAS).contains(origen) &&
               Arrays.asList(CityConstants.CIUDADES_MARITIMAS).contains(destino);
    }
}