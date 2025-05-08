import java.util.Scanner;
import strategy.core.ModoJuegoStrategy;
import strategy.implementations.ModoCampania;
import strategy.implementations.ModoDesafio;
import strategy.implementations.ModoLibre;

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
                    iniciarNuevaPartida(scanner);
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

    private static void iniciarNuevaPartida(Scanner scanner) {
        System.out.print("\n👤 Introduce tu nombre: ");
        String nombreJugador = scanner.nextLine();

        String ciudad = seleccionarCiudad(scanner);
        String dificultad = seleccionarDificultad(scanner);
        ModoJuegoStrategy modoJuego = seleccionarModoJuego(scanner);

        game.JuegoLogistica juego = new game.JuegoLogistica(ciudad, dificultad, nombreJugador, modoJuego);
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

            switch (opcion) {
                case "01": case "1": return "easy";
                case "02": case "2": return "medium";
                case "03": case "3": return "hard";
                default:
                    System.out.println("❌ Opción no válida. Por favor, selecciona una opción válida.");
            }
        } while (true);
    }

    private static ModoJuegoStrategy seleccionarModoJuego(Scanner scanner) {
        while (true) {
            System.out.println("\n🎮 SELECCIONA EL MODO DE JUEGO:");
            System.out.println("01. Modo Libre - Sin restricciones de tiempo ni recursos");
            System.out.println("02. Modo Desafío - Gestión de recursos limitados");
            System.out.println("03. Modo Campaña - Completa objetivos específicos");
            System.out.print("\nOpción: ");

            String opcion = scanner.nextLine();
            switch (opcion) {
                case "01": case "1": return new ModoLibre();
                case "02": case "2": return new ModoDesafio();
                case "03": case "3": return new ModoCampania();
                default:
                    System.out.println("❌ Opción no válida. Por favor, selecciona una opción válida.");
            }
        }
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

