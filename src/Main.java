import java.util.Scanner;
import strategy.implementations.ModoCampania;
import strategy.implementations.ModoDesafio;
import strategy.implementations.ModoLibre;

/**
 * Clase principal que inicia el juego de logística
 */
public class Main {
    private static final String[] PROVINCIAS = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza",
        "Málaga", "Murcia", "Palma de Mallorca", "Las Palmas", "Bilbao",
        "Alicante", "Córdoba", "Valladolid", "Vigo", "Gijón"
    };

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Mostrar menú principal
        boolean salir = false;
        while (!salir) {
            System.out.println("\n==============================================");
            System.out.println("🎮 MENÚ PRINCIPAL");
            System.out.println("==============================================");
            System.out.println("01. Nueva partida");
            System.out.println("02. Cargar partida");
            System.out.println("03. Créditos");
            System.out.println("99. Salir del juego");
            System.out.print("\nSeleccione una opción: ");
            
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "01":
                case "1":
                    // Mostrar menú de bienvenida
                    System.out.println("\n==============================================");
                    System.out.println("🚚 BIENVENIDO A SKYLINE LOGISTICS");
                    System.out.println("==============================================");

                    // Instrucciones omitidas aquí por brevedad
                    // ...

                    // Solicitar nombre del jugador
                    System.out.print("\n👤 Por favor, introduce tu nombre: ");
                    String nombreJugador = scanner.nextLine();
                    
                    // Seleccionar provincia
                    String provincia = seleccionarProvincia(scanner);
                    
                    // Seleccionar dificultad
                    String dificultad = seleccionarDificultad(scanner);

                    // 🧠 Seleccionar modo de juego
                    System.out.println("\n🎮 SELECCIONA MODO DE JUEGO:");
                    System.out.println("01. Libre");
                    System.out.println("02. Campaña");
                    System.out.println("03. Desafío");
                    System.out.print("\nOpción: ");
                    String modoStr = scanner.nextLine();
                    int modoSeleccionado;
                    try {
                        modoSeleccionado = Integer.parseInt(modoStr);
                    } catch (NumberFormatException e) {
                        modoSeleccionado = 1; // Por defecto
                    }

                    // Crear instancia del juego
                    game.JuegoLogistica juego = new game.JuegoLogistica(provincia, dificultad, nombreJugador);

                    // Asignar el modo de juego (Strategy Pattern)
                    switch (modoSeleccionado) {
                        case 1:
                            juego.setModoJuego(new ModoLibre());
                            break;
                        case 2:
                            juego.setModoJuego(new ModoCampania());
                            break;
                        case 3:
                            juego.setModoJuego(new ModoDesafio());
                            break;
                        default:
                            System.out.println("❌ Opción no válida. Se usará modo libre por defecto.");
                            juego.setModoJuego(new ModoLibre());
                    }

                    // Iniciar el juego
                    juego.iniciar();
                    break;

                case "02":
                case "2":
                    System.out.println("\n⚠️ Función no implementada: Cargar partida");
                    break;

                case "03":
                case "3":
                    System.out.println("\n==============================================");
                    System.out.println("📝 CRÉDITOS");
                    System.out.println("==============================================");
                    System.out.println("Juego desarrollado en JAVA para la asignatura de");
                    System.out.println("Diseño de Software por alumnos de U-Tad");
                    System.out.println("\nDesarrolladores:");
                    System.out.println("• Manuel Martinez | GitHub: @ch0rtas");
                    System.out.println("• Luis Marquina | GitHub: @Luiiss44");
                    System.out.println("• Miguel Toran");
                    System.out.println("\nRepositorio del proyecto:");
                    System.out.println("https://github.com/Luiiss44/skyline-logistics");
                    break;

                case "99":
                    game.SalirJuego.ejecutar();
                    break;

                default:
                    System.out.println("\n❌ Opción no válida");
            }
        }

        scanner.close();
    }

    /**
     * Permite al usuario seleccionar la provincia
     */
    private static String seleccionarProvincia(Scanner scanner) {
        System.out.println("\n🌍 SELECCIONA UNA PROVINCIA:");

        int columnas = 3;
        int filas = (int) Math.ceil((double) PROVINCIAS.length / columnas);

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                int indice = i + j * filas;
                if (indice < PROVINCIAS.length) {
                    System.out.printf("%02d. %-20s", indice + 1, PROVINCIAS[indice]);
                }
            }
            System.out.println();
        }

        System.out.print("\nOpción: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion >= 1 && opcion <= PROVINCIAS.length) {
                return PROVINCIAS[opcion - 1];
            }
        } catch (NumberFormatException e) {
            // Ignorar, usamos valor por defecto abajo
        }

        System.out.println("❌ Opción no válida, seleccionando Madrid por defecto");
        return "Madrid";
    }

    /**
     * Permite al usuario seleccionar la dificultad
     */
    private static String seleccionarDificultad(Scanner scanner) {
        System.out.println("\n🎮 SELECCIONA LA DIFICULTAD:");
        System.out.println("01. Fácil");
        System.out.println("02. Medio");
        System.out.println("03. Difícil");
        System.out.print("\nOpción: ");

        String opcion = scanner.nextLine();
        switch (opcion) {
            case "01":
            case "1":
                return "easy";
            case "02":
            case "2":
                return "medium";
            case "03":
            case "3":
                return "hard";
            default:
                System.out.println("❌ Opción no válida, seleccionando Medio por defecto");
                return "medium";
        }
    }
}
