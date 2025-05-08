package strategy.implementations;

import game.Jugador;
import strategy.core.ModoJuegoStrategy;

/**
 * Estrategia para el modo desafío: el juego termina si pasas un límite de días o te quedas sin dinero.
 */
public class ModoDesafio implements ModoJuegoStrategy {

    private final int limiteDias = 50;

    @Override
    public void iniciarJuego(Jugador jugador) {
        System.out.println("⚠️ Modo desafío activado: sobrevive 50 días sin quedarte sin dinero.");
    }

    @Override
    public boolean verificarCondicionesFin(Jugador jugador, int diaActual) {
        // Termina si pasa el límite de días o te quedas sin presupuesto
        return diaActual >= limiteDias || jugador.estaDerrotado();
    }

    @Override
    public void mostrarObjetivos() {
        System.out.println("\n⏱ OBJETIVO DEL MODO DESAFÍO:");
        System.out.println("• Dura hasta 50 días");
        System.out.println("• Si tu balance cae a 0, pierdes.");
    }
}
