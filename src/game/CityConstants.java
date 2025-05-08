package game;

/**
 * Clase que contiene las constantes relacionadas con las ciudades y distancias.
 */
public class CityConstants {
    public static final String[] CIUDADES = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza",
        "Málaga", "Murcia", "Palma de Mallorca", "Las Palmas", "Bilbao",
        "Alicante", "Córdoba", "Valladolid", "Vigo", "Gijón"
    };

    // Ciudades que son islas
    public static final String[] ISLAS = {
        "Palma de Mallorca", "Las Palmas"
    };

    // Ciudades con acceso marítimo (puertos)
    public static final String[] CIUDADES_MARITIMAS = {
        "Barcelona", "Valencia", "Málaga", "Bilbao", "Alicante", "Vigo", "Gijón",
        "Palma de Mallorca", "Las Palmas"
    };

    // Matriz de distancias entre ciudades (en km)
    public static final int[][] DISTANCIAS = {
        // Madrid, Barcelona, Valencia, Sevilla, Zaragoza, Málaga, Murcia, Palma, Las Palmas, Bilbao, Alicante, Córdoba, Valladolid, Vigo, Gijón
        {0, 621, 352, 538, 325, 530, 400, 800, 2100, 395, 420, 400, 193, 599, 450}, // Madrid
        {621, 0, 349, 1000, 296, 1000, 600, 200, 2200, 610, 500, 900, 800, 1000, 800}, // Barcelona
        {352, 349, 0, 650, 300, 600, 250, 300, 2000, 600, 166, 500, 500, 800, 700}, // Valencia
        {538, 1000, 650, 0, 800, 200, 400, 600, 1900, 800, 500, 140, 600, 900, 800}, // Sevilla
        {325, 296, 300, 800, 0, 700, 500, 500, 2100, 300, 400, 600, 300, 700, 600}, // Zaragoza
        {530, 1000, 600, 200, 700, 0, 300, 400, 1900, 800, 400, 200, 700, 1000, 900}, // Málaga
        {400, 600, 250, 400, 500, 300, 0, 300, 2000, 700, 100, 300, 600, 900, 800}, // Murcia
        {800, 200, 300, 600, 500, 400, 300, 0, 2100, 800, 300, 600, 800, 1000, 900}, // Palma de Mallorca
        {2100, 2200, 2000, 1900, 2100, 1900, 2000, 2100, 0, 2100, 2000, 1900, 2100, 2200, 2100}, // Las Palmas
        {395, 610, 600, 800, 300, 800, 700, 800, 2100, 0, 600, 700, 280, 400, 300}, // Bilbao
        {420, 500, 166, 500, 400, 400, 100, 300, 2000, 600, 0, 400, 500, 800, 700}, // Alicante
        {400, 900, 500, 140, 600, 200, 300, 600, 1900, 700, 400, 0, 500, 800, 700}, // Córdoba
        {193, 800, 500, 600, 300, 700, 600, 800, 2100, 280, 500, 500, 0, 400, 300}, // Valladolid
        {599, 1000, 800, 900, 700, 1000, 900, 1000, 2200, 400, 800, 800, 400, 0, 200}, // Vigo
        {450, 800, 700, 800, 600, 900, 800, 900, 2100, 300, 700, 700, 300, 200, 0}  // Gijón
    };
}