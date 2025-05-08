
import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import strategy.implementations.ModoLibre;
import strategy.implementations.ModoDesafio;
import strategy.implementations.ModoCampania;

public class Main {
    private static final String[] CIUDADES = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza",
        "Málaga", "Murcia", "Palma de Mallorca", "Las Palmas", "Bilbao",
        "Alicante", "Córdoba", "Valladolid", "Vigo", "Gijón"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        while (!salir) {
            System.out.println("\n==============================================");
            System.out.println("🎮 MENÚ PRINCIPAL");
            System.out.println("==============================================");
            System.out.println("01. Nueva partida");
            System.out.println("02. Cargar partida");
            System.out.println("03. Ver histórico de jugadores");
            System.out.println("98. Créditos");
            System.out.println("99. Salir del juego");
            System.out.print("\nSeleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "01":
                case "1":
                    iniciarNuevaPartida();
                    break;
                case "02":
                case "2":
                    cargarPartida();
                    break;
                case "03":
                case "3":
                    mostrarHistoricoJugadores();
                    break;
                case "98":
                    mostrarCreditos();
                    break;
                case "99":
                    salir = true;
                    System.out.println("\n👋 ¡Gracias por jugar a Skyline Logistics!");
                    break;
                default:
                    System.out.println("\n❌ Opción no válida");
            }
        }

        scanner.close();
    }

    private static void iniciarNuevaPartida() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\n👤 Introduce tu nombre: ");
        String nombreJugador = scanner.nextLine();

        String ciudad = seleccionarCiudad(scanner);
        String dificultad = seleccionarDificultad(scanner);
        String modoJuego = seleccionarModoJuego(scanner);

        game.JuegoLogistica juego = new game.JuegoLogistica(ciudad, dificultad, nombreJugador);

        switch (modoJuego.toLowerCase()) {
            case "libre":
                juego.setModoJuego(new ModoLibre());
                break;
            case "campaña":
                juego.setModoJuego(new ModoCampania());
                break;
            case "desafio":
                juego.setModoJuego(new ModoDesafio());
                break;
            default:
                juego.setModoJuego(new ModoLibre());
        }

        juego.iniciar();
    }

    private static String seleccionarCiudad(Scanner scanner) {
        String opcion;
        do {
            System.out.println("\n🌍 SELECCIONA UNA CIUDAD:");
            int columnas = 3;
            int filas = (int) Math.ceil((double) CIUDADES.length / columnas);
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    int indice = i + j * filas;
                    if (indice < CIUDADES.length) {
                        System.out.printf("%02d. %-20s", indice + 1, CIUDADES[indice]);
                    }
                }
                System.out.println();
            }
            System.out.print("\nOpción: ");
            opcion = scanner.nextLine();

            try {
                int opcionNumerica = Integer.parseInt(opcion);
                if (opcionNumerica >= 1 && opcionNumerica <= CIUDADES.length) {
                    return CIUDADES[opcionNumerica - 1];
                }
            } catch (NumberFormatException e) {}
            System.out.println("❌ Opción no válida, por favor selecciona una opción válida.");
        } while (true);
    }

    private static String seleccionarDificultad(Scanner scanner) {
        String opcion;
        do {
            System.out.println("\n🎮 SELECCIONA LA DIFICULTAD:");
            System.out.println("01. Fácil");
            System.out.println("02. Medio");
            System.out.println("03. Difícil");
            System.out.print("\nOpción: ");
            opcion = scanner.nextLine();

            if (!opcion.equals("01") && !opcion.equals("1") && !opcion.equals("02") &&
                !opcion.equals("2") && !opcion.equals("03") && !opcion.equals("3")) {
                System.out.println("❌ Opción no válida. Por favor, selecciona una opción válida.");
            }
        } while (!opcion.equals("01") && !opcion.equals("1") &&
                 !opcion.equals("02") && !opcion.equals("2") &&
                 !opcion.equals("03") && !opcion.equals("3"));

        switch (opcion) {
            case "01": case "1": return "easy";
            case "02": case "2": return "medium";
            case "03": case "3": return "hard";
            default: return "medium";
        }
    }

    private static String seleccionarModoJuego(Scanner scanner) {
        String modoSeleccionado = null;

        while (modoSeleccionado == null) {
            System.out.println("\n🎮 SELECCIONA EL MODO DE JUEGO:");
            System.out.println("01. Modo Libre - Sin restricciones de tiempo ni recursos");
            System.out.println("02. Modo Desafío - Gestión de recursos limitados");
            System.out.println("03. Modo Campaña - Completa objetivos específicos");
            System.out.print("\nOpción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "01":
                case "1":
                    modoSeleccionado = "libre";
                    break;
                case "02":
                case "2":
                    modoSeleccionado = "desafio";
                    break;
                case "03":
                case "3":
                    modoSeleccionado = "campaña";
                    break;
                default:
                    System.out.println("❌ Opción no válida. Por favor, selecciona una opción válida.");
            }
        }
        return modoSeleccionado;
    }

    private static void mostrarHistoricoJugadores() {
        System.out.println("\n⚠️ Función de historial no implementada aún.");
    }

    private static void cargarPartida() {
        System.out.println("\n⚠️ Función de carga no implementada aún.");
    }

    private static void mostrarCreditos() {
        System.out.println("\n==============================================");
        System.out.println("📝 CRÉDITOS");
        System.out.println("==============================================");
        System.out.println("Juego desarrollado por alumnos de U-Tad para la asignatura Diseño de Software");
        System.out.println("• Manuel Martinez | GitHub: @ch0rtas");
        System.out.println("• Luis Marquina | GitHub: @Luiiss44");
        System.out.println("• Miguel Toran");
        System.out.println("\nRepositorio del proyecto:");
        System.out.println("https://github.com/Luiiss44/skyline-logistics");
    }
}
