package game;

public class CiudadUtils {
    /**
     * Normaliza el nombre de una ciudad
     * @param ciudad Nombre de la ciudad a normalizar
     * @return String con el nombre normalizado
     */
    public static String normalizarNombreCiudad(String ciudad) {
        String nombre = ciudad.replace("_", " ");
        String[] palabras = nombre.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length() > 0) {
                resultado.append(Character.toUpperCase(palabras[i].charAt(0)));
                resultado.append(palabras[i].substring(1).toLowerCase());
                if (i < palabras.length - 1) {
                    resultado.append(" ");
                }
            }
        }
        return resultado.toString();
    }
}