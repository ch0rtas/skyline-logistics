package strategy.implementations;

import game.Jugador;
import strategy.core.ModoJuegoStrategy;

/**
 * Estrategia para el modo campaña: hay objetivos a cumplir.
 */
public class ModoCampania implements ModoJuegoStrategy {

    @Override
    public void iniciarJuego(Jugador jugador) {
        System.out.println("🎯 Modo campaña activado: cumple los objetivos antes de que se acabe el tiempo.");
    }

    @Override
    public boolean verificarCondicionesFin(Jugador jugador, int diaActual) {
        // El modo campaña no se termina automáticamente desde aquí (lo maneja JuegoLogistica)
        return false;
    }

    @Override
    public void mostrarObjetivos() {
        System.out.println("\n🎯 OBJETIVOS DEL MODO CAMPAÑA (según dificultad):");
        System.out.println("• Fácil: 30 días, 100 envíos exitosos, 80% satisfacción, 100.000€ beneficios");
        System.out.println("• Medio: 60 días, 350 envíos exitosos, 90% satisfacción, 250.000€ beneficios");
        System.out.println("• Difícil: 100 días, 920 envíos exitosos, 95% satisfacción, 500.000€ beneficios");
    }
}
