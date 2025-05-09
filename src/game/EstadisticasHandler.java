package game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EstadisticasHandler {

    public static void guardarEstadisticas(String modoJuego, Jugador jugador, int diaActual, int[] beneficiosPorDia, int enviosExitosos, int satisfaccionClientes, String fechaInicio, String dificultad, String ciudad) {
        try {
            FileWriter fw = new FileWriter("historico_jugadores.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Formato: modoJuego|nombreJugador|dias|dinero|enviosExitosos|satisfaccion|fechaInicio|fechaFin|dificultad|ciudad
            String linea = String.format("%s|%s|%d|%d|%d|%d|%s|%s|%s|%s",
                modoJuego,
                jugador.getNombre(),
                diaActual,
                jugador.getBalance(),
                enviosExitosos,
                satisfaccionClientes,
                fechaInicio,
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                dificultad,
                ciudad);

            bw.write(linea);
            bw.newLine();
            bw.close();

        } catch (IOException e) {
            System.out.println("❌ Error al guardar estadísticas: " + e.getMessage());
        }
    }
}