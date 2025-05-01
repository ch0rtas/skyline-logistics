package controller.facade;

import java.util.Properties;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Clase fachada que coordina la interacción entre el usuario y los subsistemas.
 * Implementa el patrón Facade para simplificar la interfaz del sistema.
 */
public class GestorCentralLogistica {
    private final Properties config;
    private final String dificultad;
    private final Scanner scanner;
    private int turnoActual;
    private LocalDate fechaActual;
    private boolean simulacionActiva;

    /**
     * Constructor de la clase GestorCentralLogistica
     * @param config Configuración del sistema
     * @param dificultad Nivel de dificultad de la simulación
     */
    public GestorCentralLogistica(Properties config, String dificultad) {
        this.config = config;
        this.dificultad = dificultad;
        this.scanner = new Scanner(System.in);
        this.turnoActual = 1;
        this.fechaActual = LocalDate.now();
        this.simulacionActiva = false;
    }

    /**
     * Muestra el menú principal y procesa la opción seleccionada
     */
    public void mostrarMenuPrincipal() {
        while (true) {
            System.out.println("\n=== 🚚 Skyline Logistics ===");
            System.out.println("1. Iniciar simulación");
            System.out.println("2. Configurar región");
            System.out.println("3. Ver ayuda");
            System.out.println("4. Salir");
            System.out.print("\nSeleccione una opción: ");

            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                
                switch (opcion) {
                    case 1:
                        iniciarSimulacion();
                        break;
                    case 2:
                        configurarRegion();
                        break;
                    case 3:
                        mostrarAyuda();
                        break;
                    case 4:
                        System.out.println("¡Hasta luego!");
                        return;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
    }

    /**
     * Inicia una nueva simulación
     */
    private void iniciarSimulacion() {
        simulacionActiva = true;
        System.out.println("\n=== Iniciando simulación ===");
        System.out.println("Dificultad: " + dificultad);
        System.out.println("Fecha inicial: " + fechaActual.format(DateTimeFormatter.ISO_LOCAL_DATE));
        
        while (simulacionActiva) {
            mostrarMenuTurno();
        }
    }

    /**
     * Muestra el menú de turno y procesa la opción seleccionada
     */
    private void mostrarMenuTurno() {
        System.out.println("\n=== Turno " + turnoActual + " - " + 
                         fechaActual.format(DateTimeFormatter.ISO_LOCAL_DATE) + " ===");
        System.out.println("1. Crear nuevo envío");
        System.out.println("2. Decorar envío existente");
        System.out.println("3. Resolver incidente");
        System.out.println("4. Rastrear pedido");
        System.out.println("5. Generar informe");
        System.out.println("6. Finalizar turno");
        System.out.println("7. Volver al menú principal");
        System.out.print("\nSeleccione una opción: ");

        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            
            switch (opcion) {
                case 1:
                    crearNuevoEnvio();
                    break;
                case 2:
                    decorarEnvio();
                    break;
                case 3:
                    resolverIncidente();
                    break;
                case 4:
                    rastrearPedido();
                    break;
                case 5:
                    generarInforme();
                    break;
                case 6:
                    finalizarTurno();
                    break;
                case 7:
                    simulacionActiva = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un número válido.");
        }
    }

    /**
     * Configura la región de operación
     */
    private void configurarRegion() {
        System.out.println("\n=== Configuración de Región ===");
        System.out.println("Regiones disponibles: " + config.getProperty("region.available"));
        System.out.print("Seleccione una región: ");
        String region = scanner.nextLine();
        System.out.println("Región configurada: " + region);
    }

    /**
     * Muestra la ayuda del sistema
     */
    private void mostrarAyuda() {
        System.out.println("\n=== Ayuda de Skyline Logistics ===");
        System.out.println("1. Iniciar simulación: Comienza una nueva simulación");
        System.out.println("2. Configurar región: Selecciona la región de operación");
        System.out.println("3. Ver ayuda: Muestra esta pantalla de ayuda");
        System.out.println("4. Salir: Termina la aplicación");
    }

    /**
     * Crea un nuevo envío
     */
    private void crearNuevoEnvio() {
        System.out.println("\n=== Crear Nuevo Envío ===");
        // Implementación pendiente
    }

    /**
     * Decora un envío existente con servicios adicionales
     */
    private void decorarEnvio() {
        System.out.println("\n=== Decorar Envío ===");
        // Implementación pendiente
    }

    /**
     * Resuelve un incidente
     */
    private void resolverIncidente() {
        System.out.println("\n=== Resolver Incidente ===");
        // Implementación pendiente
    }

    /**
     * Rastrea un pedido
     */
    private void rastrearPedido() {
        System.out.println("\n=== Rastrear Pedido ===");
        // Implementación pendiente
    }

    /**
     * Genera un informe del estado actual
     */
    private void generarInforme() {
        System.out.println("\n=== Generar Informe ===");
        // Implementación pendiente
    }

    /**
     * Finaliza el turno actual y avanza al siguiente
     */
    private void finalizarTurno() {
        System.out.println("\n=== Finalizando Turno " + turnoActual + " ===");
        // Procesar eventos aleatorios
        procesarEventosAleatorios();
        
        // Avanzar al siguiente turno
        turnoActual++;
        fechaActual = fechaActual.plusDays(1);
        
        System.out.println("Turno " + turnoActual + " iniciado.");
    }

    /**
     * Procesa eventos aleatorios al finalizar un turno
     */
    private void procesarEventosAleatorios() {
        // Implementación pendiente
    }
} 