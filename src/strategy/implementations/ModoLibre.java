package strategy.implementations;

import game.Jugador;
import strategy.core.ModoJuegoStrategy;

/**
 * Modo Libre: el juego nunca termina automáticamente.
 */
public class ModoLibre implements ModoJuegoStrategy {

    @Override
    public void iniciarJuego(Jugador jugador) {
        System.out.println("Modo Libre iniciado: sin límite de días ni dinero.");
    }

    @Override
    public boolean verificarCondicionesFin(Jugador jugador, int diaActual) {
        return false; // Nunca se cumple
    }

    @Override
    public void mostrarObjetivos() {
        System.out.println("Objetivo: Juega a tu ritmo. No hay condiciones de fin.");
    }
}
