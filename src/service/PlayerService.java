package service;

import model.JugadorHistorico;
import java.io.*;
import java.util.*;

public class PlayerService {
    private static final String ARCHIVO_HISTORICO = "historico_jugadores.txt";

    public void mostrarHistoricoJugadores() {
        try {
            File archivo = new File(ARCHIVO_HISTORICO);
            if (!archivo.exists()) {
                System.out.println("\n📊 No hay registros de jugadores aún");
                System.out.println("\nEscribe 0 para volver al menú principal...");
                new Scanner(System.in).nextLine();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(archivo));
            Map<String, List<JugadorHistorico>> jugadoresPorModo = new HashMap<>();
            String linea;

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split("\\|");
                if (datos.length == 10) {
                    String modo = datos[0];
                    String nombre = datos[1];
                    int dias = Integer.parseInt(datos[2]);
                    int dinero = Integer.parseInt(datos[3]);
                    int envios = Integer.parseInt(datos[4]);
                    int satisfaccion = Integer.parseInt(datos[5]);
                    String fechaInicio = datos[6];
                    String fechaFin = datos[7];
                    String dificultad = datos[8];
                    String ciudad = datos[9];

                    JugadorHistorico jugador = new JugadorHistorico(nombre, dias, dinero, envios, satisfaccion, fechaInicio, fechaFin, dificultad, ciudad);
                    jugadoresPorModo.computeIfAbsent(modo, k -> new ArrayList<>()).add(jugador);
                }
            }
            br.close();

            mostrarTopJugadores(jugadoresPorModo);

            System.out.println("\nEscribe 0 para volver al menú principal...");
            new Scanner(System.in).nextLine();

        } catch (IOException e) {
            System.out.println("\n❌ Error al leer el histórico: " + e.getMessage());
            System.out.println("\nEscribe 0 para volver al menú principal...");
            new Scanner(System.in).nextLine();
        }
    }

    private void mostrarTopJugadores(Map<String, List<JugadorHistorico>> jugadoresPorModo) {
        for (Map.Entry<String, List<JugadorHistorico>> entry : jugadoresPorModo.entrySet()) {
            String modo = entry.getKey();
            List<JugadorHistorico> jugadores = entry.getValue();

            jugadores.sort((j1, j2) -> {
                int comparacionDificultad = obtenerPesoDificultad(j2.dificultad) - obtenerPesoDificultad(j1.dificultad);
                if (comparacionDificultad != 0) {
                    return comparacionDificultad;
                }
                return j2.dinero - j1.dinero;
            });

            System.out.println("\n" + "=".repeat(50));
            System.out.println("🏆 TOP 5 - MODO " + modo.toUpperCase() + " 🏆");
            System.out.println("=".repeat(50));
            
            for (int i = 0; i < Math.min(5, jugadores.size()); i++) {
                JugadorHistorico j = jugadores.get(i);
                System.out.println("\n🥇 POSICIÓN " + (i + 1));
                System.out.println("👤 Jugador: " + j.nombre);
                System.out.println("🌍 Ciudad: " + j.ciudad);
                System.out.println("🎮 Dificultad: " + obtenerEmojiDificultad(j.dificultad) + " " + j.dificultad.toUpperCase());
                System.out.println("📅 Días jugados: " + j.dias);
                System.out.println("💰 Balance final: " + j.dinero + "€");
                System.out.println("📦 Envíos exitosos: " + j.envios);
                System.out.println("😊 Satisfacción: " + j.satisfaccion + "%");
                System.out.println("⏰ Duración: " + j.fechaInicio + " → " + j.fechaFin);
                System.out.println("-".repeat(50));
            }
        }
    }

    private int obtenerPesoDificultad(String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "hard":
                return 3;
            case "medium":
                return 2;
            case "easy":
                return 1;
            default:
                return 0;
        }
    }

    private String obtenerEmojiDificultad(String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "hard":
                return "🔥";
            case "medium":
                return "⚡";
            case "easy":
                return "⭐";
            default:
                return "❓";
        }
    }
} 