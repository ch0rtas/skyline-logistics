import java.util.Scanner;

/**
 * Clase principal que inicia el juego de logística
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Mostrar menú de bienvenida
        System.out.println("\n==============================================");
        System.out.println("🚚 BIENVENIDO A SKYLINE LOGISTICS");
        System.out.println("==============================================");
        
        // Seleccionar región
        String region = seleccionarRegion(scanner);
        
        // Seleccionar dificultad
        String dificultad = seleccionarDificultad(scanner);
        
        // Iniciar juego
        game.JuegoLogistica juego = new game.JuegoLogistica(region, dificultad);
        juego.iniciar();
        
        scanner.close();
    }
    
    /**
     * Permite al usuario seleccionar la región
     * @param scanner Scanner para entrada de usuario
     * @return String con la región seleccionada
     */
    private static String seleccionarRegion(Scanner scanner) {
        System.out.println("\n🌍 SELECCIONA UNA REGIÓN:");
        System.out.println("1. Sudamérica");
        System.out.println("2. Europa");
        System.out.println("3. Asia");
        System.out.print("\nOpción: ");
        
        String opcion = scanner.nextLine();
        switch (opcion) {
            case "1":
                return "sudamerica";
            case "2":
                return "europa";
            case "3":
                return "asia";
            default:
                System.out.println("❌ Opción no válida, seleccionando Sudamérica por defecto");
                return "sudamerica";
        }
    }
    
    /**
     * Permite al usuario seleccionar la dificultad
     * @param scanner Scanner para entrada de usuario
     * @return String con la dificultad seleccionada
     */
    private static String seleccionarDificultad(Scanner scanner) {
        System.out.println("\n🎮 SELECCIONA LA DIFICULTAD:");
        System.out.println("1. Fácil");
        System.out.println("2. Medio");
        System.out.println("3. Difícil");
        System.out.print("\nOpción: ");
        
        String opcion = scanner.nextLine();
        switch (opcion) {
            case "1":
                return "easy";
            case "2":
                return "medium";
            case "3":
                return "hard";
            default:
                System.out.println("❌ Opción no válida, seleccionando Medio por defecto");
                return "medium";
        }
    }
} 