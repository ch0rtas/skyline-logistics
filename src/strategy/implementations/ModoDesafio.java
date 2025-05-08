package strategy.implementations;

import game.Jugador;
import strategy.core.ModoJuegoStrategy;

/**
 * Modo Desafío: termina si pasas de X días o si te quedas sin presupuesto.
 */
public class ModoDesafio implements ModoJuegoStrategy {

    private final int limiteDias = 30; // Límite máximo de días

    @Override
    public void iniciarJuego(Jugador jugador) {
        System.out.println("Modo Desafío iniciado: tienes " + limiteDias + " días para triunfar.");
    }

    @Override
    public boolean verificarCondicionesFin(Jugador jugador, int diaActual) {
        // Termina si te quedas sin dinero o pasas el límite de días
        return diaActual >= limiteDias || jugador.getPresupuesto() < 0;
    }

    @Override
    public void mostrarObjetivos() {
        System.out.println("Objetivo del modo desafío: mantente a flote durante " + limiteDias + " días sin quebrar.");
    }
}
