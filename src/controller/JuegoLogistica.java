package controller;

import incidente.tipos.*;
import incidente.core.IncidenteLogistico;
import decorator.base.Envio;
import decorator.implementations.EnvioBasico;
import decorator.decorators.*;
import strategy.core.EstrategiaEnvio;
import strategy.implementations.*;
import strategy.context.EnvioContext;
import java.util.Scanner;
import java.util.Random;

/**
 * Clase principal que orquesta el juego de logística.
 * Gestiona la interacción con el usuario y coordina los diferentes componentes.
 */
public class JuegoLogistica {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();
    private static int presupuesto = 100000;
    private static int turno = 1;
    private static boolean juegoActivo = true;

    public static void main(String[] args) {
        System.out.println("🚚 Bienvenido a Skyline Logistics 🚚");
        System.out.println("Tu presupuesto inicial es: $" + presupuesto);

        while (juegoActivo) {
            mostrarMenuPrincipal();
        }

        System.out.println("¡Gracias por jugar a Skyline Logistics!");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("\n=== Turno " + turno + " ===");
        System.out.println("Presupuesto actual: $" + presupuesto);
        System.out.println("1. Crear nuevo envío");
        System.out.println("2. Gestionar incidente");
        System.out.println("3. Ver estadísticas");
        System.out.println("4. Salir");
        System.out.print("Selecciona una opción: ");

        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        switch (opcion) {
            case 1:
                crearEnvio();
                break;
            case 2:
                gestionarIncidente();
                break;
            case 3:
                mostrarEstadisticas();
                break;
            case 4:
                juegoActivo = false;
                break;
            default:
                System.out.println("Opción no válida");
        }
    }

    private static void crearEnvio() {
        System.out.println("\n=== Crear Nuevo Envío ===");
        System.out.print("Origen: ");
        String origen = scanner.nextLine();
        System.out.print("Destino: ");
        String destino = scanner.nextLine();
        System.out.print("Peso (kg): ");
        double peso = scanner.nextDouble();
        scanner.nextLine();

        // Crear envío básico
        Envio envio = new EnvioBasico(origen, destino, peso);

        // Seleccionar estrategia
        System.out.println("\nSelecciona una estrategia de envío:");
        System.out.println("1. Rápida (2x costo, 1 día)");
        System.out.println("2. Económica (0.8x costo, 5 días)");
        System.out.println("3. Alternativa (1.5x costo, 3 días)");
        int estrategia = scanner.nextInt();
        scanner.nextLine();

        EstrategiaEnvio estrategiaSeleccionada;
        switch (estrategia) {
            case 1:
                estrategiaSeleccionada = new EstrategiaRapida();
                break;
            case 2:
                estrategiaSeleccionada = new EstrategiaEconomica();
                break;
            case 3:
                estrategiaSeleccionada = new EstrategiaAlternativa();
                break;
            default:
                System.out.println("Estrategia no válida, usando económica por defecto");
                estrategiaSeleccionada = new EstrategiaEconomica();
        }

        // Aplicar decoradores
        System.out.println("\nSelecciona servicios adicionales (0 para terminar):");
        System.out.println("1. Seguro (+10%)");
        System.out.println("2. Refrigeración (+20%, +1 día)");
        System.out.println("3. Urgente (+50%, -2 días)");

        int servicio;
        do {
            servicio = scanner.nextInt();
            scanner.nextLine();
            switch (servicio) {
                case 1:
                    envio = new SeguroDecorator(envio);
                    break;
                case 2:
                    envio = new RefrigeracionDecorator(envio);
                    break;
                case 3:
                    envio = new UrgenteDecorator(envio);
                    break;
            }
        } while (servicio != 0);

        // Calcular costos y tiempos
        EnvioContext context = new EnvioContext(estrategiaSeleccionada);
        double costo = context.calcularCosto(envio);
        int tiempo = context.calcularTiempoEstimado(envio);

        System.out.println("\nResumen del envío:");
        System.out.println("Descripción: " + envio.getDescripcion());
        System.out.println("Estrategia: " + context.getDescripcionEstrategia());
        System.out.println("Costo: $" + costo);
        System.out.println("Tiempo estimado: " + tiempo + " días");

        if (costo <= presupuesto) {
            presupuesto -= costo;
            System.out.println("Envío creado exitosamente!");
        } else {
            System.out.println("No tienes suficiente presupuesto para este envío");
        }
    }

    private static void gestionarIncidente() {
        System.out.println("\n=== Gestionar Incidente ===");
        System.out.println("Selecciona el tipo de incidente:");
        System.out.println("1. Avería de camión");
        System.out.println("2. Huelga de transporte");
        System.out.println("3. Condiciones climáticas adversas");
        int tipo = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Descripción del incidente: ");
        String descripcion = scanner.nextLine();

        IncidenteLogistico incidente;
        switch (tipo) {
            case 1:
                incidente = new AveriaCamion(descripcion);
                break;
            case 2:
                incidente = new HuelgaTransporte(descripcion);
                break;
            case 3:
                incidente = new ClimaAdverso(descripcion);
                break;
            default:
                System.out.println("Tipo de incidente no válido");
                return;
        }

        incidente.resolver();
        System.out.println("Incidente resuelto: " + incidente.getSolucion());
    }

    private static void mostrarEstadisticas() {
        System.out.println("\n=== Estadísticas ===");
        System.out.println("Turno actual: " + turno);
        System.out.println("Presupuesto restante: $" + presupuesto);
        System.out.println("Envíos realizados: " + (turno - 1));
    }

    private static void finalizarTurno() {
        turno++;
        // Simular eventos aleatorios
        if (random.nextDouble() < 0.3) { // 30% de probabilidad de incidente
            gestionarIncidente();
        }
    }
} 