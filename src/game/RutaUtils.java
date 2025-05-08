package game;

public class RutaUtils {
    /**
     * Verifica si existe una ruta terrestre válida entre dos ciudades
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return true si existe una ruta terrestre válida, false si no
     */
    public static boolean existeRutaTerrestre(String origen, String destino) {
        // Normalizar nombres de ciudades
        String origenNormalizado = CiudadUtils.normalizarNombreCiudad(origen);
        String destinoNormalizado = CiudadUtils.normalizarNombreCiudad(destino);

        // Si alguna de las ciudades es una isla, no hay ruta terrestre
        if (JuegoLogistica.esIsla(origenNormalizado) || JuegoLogistica.esIsla(destinoNormalizado)) {
            return false;
        }

        // Obtener índices de las ciudades
        int indiceOrigen = -1;
        int indiceDestino = -1;

        for (int i = 0; i < CityConstants.CIUDADES.length; i++) {
            if (CityConstants.CIUDADES[i].equalsIgnoreCase(origenNormalizado)) {
                indiceOrigen = i;
            }
            if (CityConstants.CIUDADES[i].equalsIgnoreCase(destinoNormalizado)) {
                indiceDestino = i;
            }
        }

        if (indiceOrigen == -1 || indiceDestino == -1) {
            return false;
        }

        // Verificar si hay una distancia terrestre válida
        int distancia = CityConstants.DISTANCIAS[indiceOrigen][indiceDestino];
        return distancia > 0;
    }
}