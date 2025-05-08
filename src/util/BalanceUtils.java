package util;

import game.Jugador;

/**
 * Utility class for handling balance-related calculations and operations.
 */
public class BalanceUtils {

    private static final double TASA_IMPUESTOS = 0.45;

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

    /**
     * Applies taxes to the player's balance based on the difficulty level.
     *
     * @param jugador The player whose balance will be taxed.
     * @param dificultad The difficulty level.
     * @param diaActual The current day in the game.
     */
    public static void aplicarImpuestos(Jugador jugador, String dificultad, int diaActual) {
        int diasImpuestos = PenaltyUtils.calcularDiasImpuestos(dificultad);
        if (diaActual % diasImpuestos == 0) {
            int impuestos = (int) (jugador.getBalance() * TASA_IMPUESTOS);
            jugador.gastar(impuestos);
            System.out.println("\n💰 Se han aplicado impuestos del " + (TASA_IMPUESTOS * 100) + "%: -" + impuestos + "€");
        }
    }
}