import java.util.Scanner;

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
                    
                    // Mostrar instrucciones
                    System.out.println("\n📖 INSTRUCCIONES DEL JUEGO:");
                    System.out.println("🚚 Skyline Logistics es un juego de gestión de pedidos donde tu objetivo es");
                    System.out.println("   administrar una empresa de logística en España. Cada día que pasa, el");
                    System.out.println("   volumen de pedidos aumenta, poniendo a prueba tu capacidad de gestión.");
                    System.out.println("\n🎯 CARACTERÍSTICAS PRINCIPALES:");
                    System.out.println("• 🚗 Gestiona una flota de vehículos limitada");
                    System.out.println("• 📦 Diferentes tipos de vehículos para diferentes tipos de carga");
                    System.out.println("• 🌍 Pedidos a diferentes provincias de España");
                    System.out.println("• 💰 Costes variables según la distancia");
                    System.out.println("• 🛒 Sistema de compra de vehículos");
                    System.out.println("• ⚠️ Gestión de incidentes y mantenimiento");
                    System.out.println("• 📝 Sistema de impuestos y multas");
                    System.out.println("\n❄️ TIPOS DE CARGA ESPECIAL:");
                    System.out.println("• 🧊 REFRIGERADO: Requiere vehículos con refrigeración");
                    System.out.println("• ❄️ CONGELADO: Necesita vehículos con congelación");
                    System.out.println("• ⚠️ PELIGROSO: Requiere vehículos especiales");
                    System.out.println("• 👮 ESCOLTADO: Necesita escolta de seguridad");
                    System.out.println("• 🎯 FRÁGIL: Requiere manejo especial");
                    System.out.println("\n🚗 TIPOS DE VEHÍCULOS:");
                    System.out.println("• 🚐 Furgoneta: Ideal para envíos locales y pequeños");
                    System.out.println("• 🚛 Camión: Para cargas medianas y largas distancias");
                    System.out.println("• 🚢 Barco: Para envíos a islas y provincias costeras");
                    System.out.println("• ✈️ Avión: Para envíos urgentes y largas distancias");
                    System.out.println("\n🎮 OBJETIVO DEL JUEGO:");
                    System.out.println("• 💰 Mantener un balance positivo");
                    System.out.println("• 😊 Mantener alta satisfacción de clientes");
                    System.out.println("• 📦 Gestionar eficientemente los pedidos");
                    System.out.println("• 🚗 Expandir tu flota de vehículos");
                    System.out.println("• 🌍 Conectar todas las provincias de España");
                    
                    // Solicitar nombre del jugador
                    System.out.print("\n👤 Por favor, introduce tu nombre: ");
                    String nombreJugador = scanner.nextLine();
                    
                    // Seleccionar provincia
                    String provincia = seleccionarProvincia(scanner);
                    
                    // Seleccionar dificultad
                    String dificultad = seleccionarDificultad(scanner);
                    
                    // Iniciar juego
                    game.JuegoLogistica juego = new game.JuegoLogistica(provincia, dificultad, nombreJugador);
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
                    salir = true;
                    System.out.println("\n👋 ¡Hasta luego!");
                    break;
                default:
                    System.out.println("\n❌ Opción no válida");
            }
        }
        
        scanner.close();
    }
    
    /**
     * Permite al usuario seleccionar la provincia
     * @param scanner Scanner para entrada de usuario
     * @return String con la provincia seleccionada
     */
    private static String seleccionarProvincia(Scanner scanner) {
        System.out.println("\n🌍 SELECCIONA UNA PROVINCIA:");
        
        // Mostrar provincias en 3 columnas
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
                return PROVINCIAS[opcion - 1].toLowerCase().replace(" ", "_");
            }
        } catch (NumberFormatException e) {
            // Si la entrada no es un número, continuamos con el valor por defecto
        }
        
        System.out.println("❌ Opción no válida, seleccionando Madrid por defecto");
        return "madrid";
    }
    
    /**
     * Permite al usuario seleccionar la dificultad
     * @param scanner Scanner para entrada de usuario
     * @return String con la dificultad seleccionada
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