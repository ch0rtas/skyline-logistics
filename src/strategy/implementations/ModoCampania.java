package strategy.implementations;

import game.Jugador;
import strategy.core.ModoJuegoStrategy;

/**
 * Modo Campaña: el juego termina cuando se alcanza cierta ganancia.
 */
public class ModoCampania implements ModoJuegoStrategy {

    private final int objetivoGanancia = 100000; // Ganancia objetivo

    @Override
    public void iniciarJuego(Jugador jugador) {
        System.out.println("Modo Campaña iniciado: tu meta es alcanzar " + objetivoGanancia + "€.");
    }

    @Override
    public boolean verificarCondicionesFin(Jugador jugador, int diaActual) {
        // Termina cuando el jugador supera la meta de dinero
        return jugador.getPresupuesto() >= objetivoGanancia;
    }

    @Override
    public void mostrarObjetivos() {
        System.out.println("Objetivo del modo campaña: gana al menos " + objetivoGanancia + "€.");
    }
}
