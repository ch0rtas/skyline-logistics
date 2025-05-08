package game;

import java.util.Calendar;

public class DiaManager {
    public static void pasarDia(JuegoLogistica juego) {
        if (!juego.getPedidosPendientes().isEmpty()) {
            System.out.println("\n❌ " + juego.getJugador().getNombre() + ", no puedes pasar al siguiente día con pedidos pendientes");
            juego.mostrarMenuPartida();
            juego.procesarOpcion(juego.getScanner().nextLine());
            return;
        }

        juego.incrementarDiaActual();
        juego.getFechaActual().add(Calendar.DAY_OF_MONTH, 1); // Añadir un día a la fecha actual

        System.out.println("\n==================================================");
        System.out.println("📅 DÍA " + juego.getDiaActual() + " (" + JuegoLogistica.formatoFecha.format(juego.getFechaActual().getTime()) + ") | ALMACÉN PRINCIPAL: " + juego.getAlmacenPrincipal());
        System.out.println("==================================================");

        // Procesar envíos
        juego.procesarPedidosEnCurso();

        // Procesar impuestos
        juego.procesarImpuestos();

        // Verificar objetivos de campaña
        if (juego.getModoJuego().equals("campaña")) {
            juego.verificarObjetivosCampaña();
        }

        // Generar nuevos vehículos en el mercado
        juego.generarVehiculosMercado();

        // Generar nuevos pedidos del día
        juego.generarPedidosDia();

        // Mostrar estadísticas
        juego.mostrarEstadisticas();
    }
}