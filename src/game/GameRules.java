package game;

public class GameRules {
    /**
     * Verifica si el jugador está derrotado según el modo de juego
     * @param modoJuego Modo de juego seleccionado
     * @param balance Balance actual del jugador
     * @return true si el jugador está derrotado, false si no
     */
    public static boolean jugadorDerrotado(String modoJuego, int balance) {
        if (modoJuego.equals("libre")) {
            return false; // En modo libre nunca se pierde
        }
        return balance < 0; // Cambiado de <= 0 a < 0 para que termine cuando sea negativo
    }
}