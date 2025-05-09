package ui;

import java.util.Scanner;

public class UIManager {
    private static final String[] CIUDADES = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza",
        "Málaga", "Murcia", "Palma de Mallorca", "Las Palmas", "Bilbao",
        "Alicante", "Córdoba", "Valladolid", "Vigo", "Gijón"
    };

    public void mostrarMenuPrincipal() {
        System.out.println("\n==============================================");
        System.out.println("🎮 MENÚ PRINCIPAL");
        System.out.println("==============================================");
        System.out.println("01. Nueva partida");
        System.out.println("02. Ver histórico de jugadores");
        System.out.println("98. Créditos");
        System.out.println("99. Salir del juego");
        System.out.print("\nSeleccione una opción: ");
    }

    public void mostrarBienvenida() {
        System.out.println("\n==============================================");
        System.out.println("🚚 BIENVENIDO A SKYLINE LOGISTICS");
        System.out.println("==============================================");
        mostrarInstrucciones();
    }

    private void mostrarInstrucciones() {
        System.out.println("\n📖 INSTRUCCIONES DEL JUEGO:");
        System.out.println("🚚 Skyline Logistics es un juego de gestión de pedidos donde tu objetivo es");
        System.out.println("   administrar una empresa de logística en España. Cada día que pasa, el");
        System.out.println("   volumen de pedidos aumenta, poniendo a prueba tu capacidad de gestión.");
        System.out.println("\n🎯 CARACTERÍSTICAS PRINCIPALES:");
        System.out.println("• 🚗 Gestiona una flota de vehículos limitada");
        System.out.println("• 📦 Diferentes tipos de vehículos para diferentes tipos de carga");
        System.out.println("• 🌍 Pedidos a diferentes ciudades de España");
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
        System.out.println("• 🚢 Barco: Para envíos a islas y ciudades costeras");
        System.out.println("• ✈️ Avión: Para envíos urgentes y largas distancias");
        System.out.println("\n🎮 OBJETIVO DEL JUEGO:");
        System.out.println("• 💰 Mantener un balance positivo");
        System.out.println("• 😊 Mantener alta satisfacción de clientes");
        System.out.println("• 📦 Gestionar eficientemente los pedidos");
        System.out.println("• 🚗 Expandir tu flota de vehículos");
        System.out.println("• 🌍 Conectar todas las ciudades de España");
    }

    public String solicitarNombreJugador(Scanner scanner) {
        System.out.print("\n👤 Por favor, introduce tu nombre: ");
        return scanner.nextLine();
    }

    public String seleccionarCiudad(Scanner scanner) {
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
            } catch (NumberFormatException e) {
                // Si la entrada no es un número, continuamos con el bucle
            }

            mostrarError("Opción no válida, por favor selecciona una opción válida.");
        } while (true);
    }

    public String seleccionarDificultad(Scanner scanner) {
        String opcion;
        do {
            System.out.println("\n🎮 SELECCIONA LA DIFICULTAD:");
            System.out.println("01. Fácil");
            System.out.println("02. Medio");
            System.out.println("03. Difícil");
            System.out.print("\nOpción: ");
            opcion = scanner.nextLine();

            if (!opcion.equals("01") && !opcion.equals("1") && !opcion.equals("02") && !opcion.equals("2") && !opcion.equals("03") && !opcion.equals("3")) {
                mostrarError("Opción no válida. Por favor, selecciona una opción válida.");
            }
        } while (!opcion.equals("01") && !opcion.equals("1") && !opcion.equals("02") && !opcion.equals("2") && !opcion.equals("03") && !opcion.equals("3"));

        switch (opcion) {
            case "01", "1":
                return "easy";
            case "02", "2":
                return "medium";
            case "03", "3":
                return "hard";
            default:
                return "medium";
        }
    }

    public String seleccionarModoJuego(Scanner scanner) {
        String modoSeleccionado = null;
        
        while (modoSeleccionado == null) {
            System.out.println("\n🎮 SELECCIONA EL MODO DE JUEGO:");
            System.out.println("01. Modo Libre - Sin restricciones de tiempo ni recursos");
            System.out.println("02. Modo Desafío - Gestión de recursos limitados");
            System.out.println("03. Modo Campaña - Completa objetivos específicos (99. Para ver objetivos)");
            System.out.print("\nOpción: ");
            
            String opcion = scanner.nextLine();
            switch (opcion) {
                case "01", "1":
                    modoSeleccionado = "libre";
                    break;
                case "02", "2":
                    modoSeleccionado = "desafio";
                    break;
                case "03", "3":
                    modoSeleccionado = "campaña";
                    break;
                case "99":
                    mostrarObjetivosCampaña();
                    System.out.println("\nPresiona Enter para volver a la selección de modo...");
                    scanner.nextLine();
                    break;
                default:
                    mostrarError("Opción no válida. Por favor, selecciona una opción válida.");
                    break;
            }
        }
        
        if (modoSeleccionado.equals("campaña")) {
            mostrarObjetivosCampaña();
        }
        
        return modoSeleccionado;
    }

    public void mostrarObjetivosCampaña() {
        System.out.println("\n=== 🎯 OBJETIVOS DE LA CAMPAÑA 🎯 ===");
        System.out.println("Para completar la campaña, deberás alcanzar las siguientes estadísticas: en el tiempo señalado.");
        
        System.out.println("\n📊 NIVEL FÁCIL:");
        System.out.println("• Día final: 30");
        System.out.println("• Envíos exitosos: 100");
        System.out.println("• Satisfacción de clientes: 80%");
        System.out.println("• Beneficios acumulados: 100,000€");
        
        System.out.println("\n🏆 NIVEL MEDIO:");
        System.out.println("• Días jugados: 60");
        System.out.println("• Envíos exitosos: 350");
        System.out.println("• Satisfacción de clientes: 90%");
        System.out.println("• Beneficios acumulados: 250,000€");
        
        System.out.println("\n🌟 NIVEL DIFÍCIL:");
        System.out.println("• Días jugados: 100");
        System.out.println("• Envíos exitosos: 920");
        System.out.println("• Satisfacción de clientes: 95%");
        System.out.println("• Beneficios acumulados: 500,000€");
    }

    public void mostrarCreditos() {
        System.out.println("\n==============================================");
        System.out.println("📝 CRÉDITOS");
        System.out.println("==============================================");
        System.out.println("Juego desarrollado en JAVA para la asignatura de");
        System.out.println("Diseño de Software por alumnos de U-Tad");
        System.out.println("\nDesarrolladores:");
        System.out.println("• Luis Marquina | GitHub: @Luiiss44");
        System.out.println("• Manuel Martinez | GitHub: @ch0rtas");
        System.out.println("• Miguel Toran");
        System.out.println("==============================================");
        System.out.println("\nRepositorio del proyecto:");
        System.out.println("01. https://github.com/Luiiss44/skyline-logistics");
        System.out.println("02. https://github.com/ch0rtas/skyline-logistics [Fork]");

        System.out.println("\nEscribe 0 para volver al menú principal...");
        new Scanner(System.in).nextLine();
    }

    public void mostrarMensajeDespedida() {
        System.out.println("\n👋 ¡Gracias por jugar a Skyline Logistics!");
    }

    public void mostrarError(String mensaje) {
        System.out.println("\n❌ " + mensaje);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println("\n" + mensaje);
    }
} 