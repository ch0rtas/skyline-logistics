package game;

import java.util.Arrays;

import static game.CityConstants.CIUDADES;
import static game.CityConstants.DISTANCIAS;

public class DistanciaUtils {
    /**
     * Obtiene la distancia entre dos ciudades
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return int con la distancia en km
     */
    public static int obtenerDistancia(String origen, String destino) {
        int indiceOrigen = Arrays.asList(CIUDADES).indexOf(origen);
        int indiceDestino = Arrays.asList(CIUDADES).indexOf(destino);
        
        // Validate indices before accessing the DISTANCIAS array
        if (indiceOrigen == -1 || indiceDestino == -1) {
            throw new IllegalArgumentException("Invalid city name(s): " + origen + ", " + destino);
        }
        
        return DISTANCIAS[indiceOrigen][indiceDestino];
    }
}