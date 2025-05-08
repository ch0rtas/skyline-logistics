package game;

public class EstadisticasHelper {
    /**
     * Muestra las estadísticas actuales del juego
     */
    public static void mostrarEstadisticas(Jugador jugador, int diaActual, int beneficiosAcumulados, int gastosAcumulados, int enviosTotales, int enviosExitosos, int satisfaccionClientes) {
        System.out.println("\n📊 ESTADÍSTICAS:");
        System.out.println("==================================================");
        System.out.println("💰 Balance actual: $" + jugador.getBalance());
        System.out.println("📅 Día actual: " + diaActual);
        System.out.println("✅ Envíos exitosos: " + enviosExitosos);
        System.out.println("❌ Envíos fallidos: " + (enviosTotales - enviosExitosos));
        System.out.println("💸 Gastos acumulados: $" + gastosAcumulados);
        System.out.println("😊 Satisfacción clientes: " + satisfaccionClientes + "%");
        System.out.println("==================================================");
    }
}