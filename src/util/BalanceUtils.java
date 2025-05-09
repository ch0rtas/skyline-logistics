package util;

import game.Jugador;

/**
 * Utility class for handling balance-related calculations and operations.
 */
public class BalanceUtils {

    /**
     * Calculates the initial balance based on difficulty and game mode.
     *
     * @param dificultad The difficulty level.
     * @param modoJuego The game mode.
     * @return The initial balance.
     */
    public static int calcularBalanceInicial(String dificultad, String modoJuego) {
        if (modoJuego.equals("libre")) {
            return 999999;
        }

        switch (dificultad.toLowerCase()) {
            case "easy":
                return 50000;
            case "medium":
                return 25000;
            case "hard":
                return 10000;
            default:
                return 25000;
        }
    }
}