package util;

import game.Jugador;
import game.Pedido;

/**
 * Utility class for handling penalties and related operations.
 */
public class PenaltyUtils {

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
                multa *= 6; // Quadrupled multiplier for easy difficulty
                break;
            case "medium":
                multa *= 8; // Quadrupled multiplier for medium difficulty
                break;
            case "hard":
                multa *= 12; // Quadrupled multiplier for hard difficulty
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
}