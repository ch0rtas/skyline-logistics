package game;

public class SatisfaccionClientesUtils {
    /**
     * Ajusta la satisfacción del cliente al inicio del juego según el modo de juego.
     */
    public static int inicializarSatisfaccionClientes(String modoJuego) {
        if (modoJuego.equals("libre")) {
            return 100;
        } else {
            return 50;
        }
    }
}