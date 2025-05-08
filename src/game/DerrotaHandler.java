package game;

import game.Jugador;

public class DerrotaHandler {
    public static void verificarDerrota(Jugador jugador, int balance, Runnable guardarEstadisticas) {
        if (balance <= 0) {
            System.out.println("\n❌ Has perdido. Tu balance ha llegado a 0€.");
            guardarEstadisticas.run();
            System.out.println("📊 Tus estadísticas han sido guardadas en el histórico.");
            System.exit(0);
        }
    }
}