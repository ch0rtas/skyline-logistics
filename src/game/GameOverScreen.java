package game;

public class GameOverScreen {
    public static void mostrarGameOver(Jugador jugador, int satisfaccionClientes, int enviosTotales, int enviosExitosos) {
        System.out.println("\n==============================================");
        System.out.println("🎮 GAME OVER");
        System.out.println("==============================================");
        System.out.println("💰 Balance final: $" + jugador.getBalance());
        System.out.println("😊 Satisfacción final: " + satisfaccionClientes + "%");
        System.out.println("🚚 Envíos totales: " + enviosTotales);
        System.out.println("✅ Envíos exitosos: " + enviosExitosos);
    }
}