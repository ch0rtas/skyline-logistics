package util;

import game.Jugador;
import game.Pedido;

/**
 * Utility class for handling penalties, taxes, and related operations.
 */
public class PenaltyUtils {

    private static final double TASA_IMPUESTOS = 0.45;

    /**
     * Calculates the penalty for rejecting an order.
     *
     * @param pedido The order being rejected.
     * @param dificultad The difficulty level.
     * @param diaActual The current day in the game.
     * @return The penalty amount.
     */
    public static int calcularMultaRechazo(Pedido pedido, String dificultad, int diaActual) {
        int base = 1000; // Base penalty amount
        int multa = base;

        // Increase based on difficulty
        switch (dificultad.toLowerCase()) {
            case "easy":
                multa *= 1;
                break;
            case "medium":
                multa *= 1.2;
                break;
            case "hard":
                multa *= 1.5;
                break;
        }

        // Increase based on the current day
        multa *= (1 + (diaActual * 0.05));

        // Increase based on priority
        if (pedido.getPrioridad().equals("URGENTE")) {
            multa *= 1.5;
        } else if (pedido.getPrioridad().equals("NORMAL")) {
            multa *= 1.2;
        }

        return (int) multa;
    }

    /**
     * Applies taxes to the player's balance based on the difficulty level.
     *
     * @param jugador The player whose balance will be taxed.
     * @param dificultad The difficulty level.
     * @param diaActual The current day in the game.
     */
    public static void aplicarImpuestos(Jugador jugador, String dificultad, int diaActual) {
        int diasImpuestos = calcularDiasImpuestos(dificultad);
        if (diaActual % diasImpuestos == 0) {
            int impuestos = (int) (jugador.getBalance() * TASA_IMPUESTOS);
            jugador.gastar(impuestos);
            System.out.println("\n💰 Se han aplicado impuestos del " + (TASA_IMPUESTOS * 100) + "%: -" + impuestos + "€");
        }
    }

    /**
     * Calculates the number of days between tax payments based on difficulty.
     *
     * @param dificultad The difficulty level.
     * @return The number of days between tax payments.
     */
    private static int calcularDiasImpuestos(String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "hard":
                return 2;
            case "medium":
                return 4;
            case "easy":
                return 6;
            default:
                return 6; // Default to the easiest level
        }
    }
}