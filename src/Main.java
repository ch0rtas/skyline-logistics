import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

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
    
    /**
     * Permite al usuario seleccionar la provincia
     * @param scanner Scanner para entrada de usuario
     * @return String con la provincia seleccionada
     */
    private static String seleccionarProvincia(Scanner scanner) {
        System.out.println("\n🌍 SELECCIONA UNA CIUDAD:");
        
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
                return PROVINCIAS[opcion - 1];
            }
        } catch (NumberFormatException e) {
            // Si la entrada no es un número, continuamos con el valor por defecto
        }
        
        System.out.println("❌ Opción no válida, seleccionando Madrid por defecto");
        return "Madrid";
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

    /**
     * Permite al usuario seleccionar el modo de juego
     * @param scanner Scanner para entrada de usuario
     * @return String con el modo de juego seleccionado
     */
    private static String seleccionarModoJuego(Scanner scanner) {
        String modoSeleccionado = null;
        
        while (modoSeleccionado == null) {
            System.out.println("\n🎮 SELECCIONA EL MODO DE JUEGO:");
            System.out.println("01. Modo Libre - Sin restricciones de tiempo ni recursos");
            System.out.println("02. Modo Desafío - Gestión de recursos limitados");
            System.out.println("03. Modo Campaña - Completa objetivos específicos (99. Para ver objetivos)");
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
                case "99":
                    mostrarObjetivosCampaña();
                    System.out.println("\nPresiona Enter para volver a la selección de modo...");
                    scanner.nextLine();
                    break;
                default:
                    System.out.println("❌ Opción no válida. Por favor, selecciona una opción válida.");
                    break;
            }
        }
        
        if (modoSeleccionado.equals("campaña")) {
            mostrarObjetivosCampaña();
        }
        
        return modoSeleccionado;
    }

    /**
     * Muestra los objetivos del Modo Campaña según la dificultad
     */
    private static void mostrarObjetivosCampaña() {
        System.out.println("\n=== 🎯 OBJETIVOS DE LA CAMPAÑA 🎯 ===");
        System.out.println("Para completar la campaña, deberás alcanzar las siguientes estadísticas:");
        System.out.println("\n📊 OBJETIVOS MÍNIMOS:");
        System.out.println("• Días jugados: 30");
        System.out.println("• Envíos exitosos: 50");
        System.out.println("• Satisfacción de clientes: 80%");
        System.out.println("• Beneficios acumulados: 100,000€");
        
        System.out.println("\n🏆 OBJETIVOS AVANZADOS:");
        System.out.println("• Días jugados: 60");
        System.out.println("• Envíos exitosos: 100");
        System.out.println("• Satisfacción de clientes: 90%");
        System.out.println("• Beneficios acumulados: 250,000€");
        
        System.out.println("\n🌟 OBJETIVOS ÉLITE:");
        System.out.println("• Días jugados: 100");
        System.out.println("• Envíos exitosos: 200");
        System.out.println("• Satisfacción de clientes: 95%");
        System.out.println("• Beneficios acumulados: 500,000€");
        
        System.out.println("\n⚠️ NOTA: Los objetivos se ajustarán según la dificultad seleccionada.");
        System.out.println("   - Fácil: 80% de los objetivos");
        System.out.println("   - Medio: 100% de los objetivos");
        System.out.println("   - Difícil: 120% de los objetivos");
    }

    private static void mostrarHistoricoJugadores() {
        try {
            File archivo = new File("historico_jugadores.txt");
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
                if (datos.length == 11) {
                    String modo = datos[0];
                    String nombre = datos[1];
                    int dias = Integer.parseInt(datos[2]);
                    int dinero = Integer.parseInt(datos[3]);
                    int envios = Integer.parseInt(datos[4]);
                    int satisfaccion = Integer.parseInt(datos[5]);
                    int beneficios = Integer.parseInt(datos[6]);
                    String fechaInicio = datos[7];
                    String fechaFin = datos[8];
                    String dificultad = datos[9];
                    String ciudad = datos[10];

                    JugadorHistorico jugador = new JugadorHistorico(nombre, dias, dinero, envios, satisfaccion, beneficios, fechaInicio, fechaFin, dificultad, ciudad);
                    jugadoresPorModo.computeIfAbsent(modo, k -> new ArrayList<>()).add(jugador);
                }
            }
            br.close();

            // Mostrar Top 5 para cada modo
            for (Map.Entry<String, List<JugadorHistorico>> entry : jugadoresPorModo.entrySet()) {
                String modo = entry.getKey();
                List<JugadorHistorico> jugadores = entry.getValue();

                // Ordenar por dificultad (descendente) y luego por dinero (descendente)
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
                    System.out.println("💵 Beneficios: " + j.beneficios + "€");
                    System.out.println("⏰ Duración: " + j.fechaInicio + " → " + j.fechaFin);
                    System.out.println("-".repeat(50));
                }
            }

            System.out.println("\nEscribe 0 para volver al menú principal...");
            new Scanner(System.in).nextLine();

        } catch (IOException e) {
            System.out.println("\n❌ Error al leer el histórico: " + e.getMessage());
            System.out.println("\nEscribe 0 para volver al menú principal...");
            new Scanner(System.in).nextLine();
        }
    }

    private static class JugadorHistorico {
        String nombre;
        int dias;
        int dinero;
        int envios;
        int satisfaccion;
        int beneficios;
        String fechaInicio;
        String fechaFin;
        String dificultad;
        String ciudad;

        public JugadorHistorico(String nombre, int dias, int dinero, int envios, int satisfaccion, int beneficios, String fechaInicio, String fechaFin, String dificultad, String ciudad) {
            this.nombre = nombre;
            this.dias = dias;
            this.dinero = dinero;
            this.envios = envios;
            this.satisfaccion = satisfaccion;
            this.beneficios = beneficios;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.dificultad = dificultad;
            this.ciudad = ciudad;
        }
    }

    private static int obtenerPesoDificultad(String dificultad) {
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

    private static String obtenerEmojiDificultad(String dificultad) {
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

    private static void iniciarNuevaPartida() {
        Scanner scanner = new Scanner(System.in);
        
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
        
        // Seleccionar modo de juego
        String modoJuego = seleccionarModoJuego(scanner);
        
        // Iniciar juego
        game.JuegoLogistica juego = new game.JuegoLogistica(provincia, dificultad, nombreJugador, modoJuego);
        juego.iniciar();
    }

    private static void cargarPartida() {
        System.out.println("\n⚠️ Función no implementada: Cargar partida");
    }

    private static void mostrarCreditos() {
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
        
        System.out.println("\nEscribe 0 para volver al menú principal...");
        new Scanner(System.in).nextLine();
    }
} 