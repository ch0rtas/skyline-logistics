package strategy.core;

import game.Jugador;

/**
 * Interfaz base del patrón Strategy para los modos de juego.
 */
public interface ModoJuegoStrategy {
    /**
     * Se ejecuta al iniciar el juego.
     */
    void iniciarJuego(Jugador jugador);

    /**
     * Verifica si deben cumplirse las condiciones para finalizar el juego.
     * @param jugador El jugador actual
     * @param diaActual Día actual del juego (lo recibe desde JuegoLogistica)
     */
    boolean verificarCondicionesFin(Jugador jugador, int diaActual);

    /**
     * Muestra los objetivos específicos del modo.
     */
    void mostrarObjetivos();
}
