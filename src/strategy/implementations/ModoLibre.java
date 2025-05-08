package strategy.implementations;

import game.Jugador;
import strategy.core.ModoJuegoStrategy;

/**
 * Estrategia para el modo libre: sin condiciones de derrota ni límites.
 */
public class ModoLibre implements ModoJuegoStrategy {

    @Override
    public void iniciarJuego(Jugador jugador) {
        System.out.println("🔓 Modo libre activado: sin restricciones.");
    }

    @Override
    public boolean verificarCondicionesFin(Jugador jugador, int diaActual) {
        // En modo libre, el juego nunca termina automáticamente
        return false;
    }

    @Override
    public void mostrarObjetivos() {
        System.out.println("\n🎯 OBJETIVO (Modo Libre): Juega todo lo que quieras, sin presión.");
    }
}
