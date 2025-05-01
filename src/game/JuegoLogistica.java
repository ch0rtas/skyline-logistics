package game;

import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase principal que gestiona el juego de logística
 */
public class JuegoLogistica {
    private Jugador jugador;
    private Scanner scanner;
    private Random random;
    private Map<String, Integer> pedidos;
    private int diaActual;
    private String region;
    private String dificultad;
    private int satisfaccionClientes;
    private int enviosExitosos;
    private int enviosTotales;

    /**
     * Constructor del juego
     * @param region Región de operación
     * @param dificultad Nivel de dificultad
     */
    public JuegoLogistica(String region, String dificultad) {
        this.region = region.toUpperCase();
        this.dificultad = dificultad.toLowerCase();
        this.jugador = new Jugador("Jugador", 50000);
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.pedidos = new HashMap<>();
        this.diaActual = 1;
        this.satisfaccionClientes = 100;
        this.enviosExitosos = 0;
        this.enviosTotales = 0;
    }

    /**
     * Inicia el juego
     */
    public void iniciar() {
        mostrarBienvenida();
        inicializarFlota();
        
        while (!jugador.estaDerrotado()) {
            mostrarMenuPrincipal();
            procesarOpcion(scanner.nextLine());
        }
        
        mostrarGameOver();
    }

    /**
     * Muestra la pantalla de bienvenida
     */
    private void mostrarBienvenida() {
        System.out.println("\n✅ Sistema iniciado en región: " + region);
        System.out.println("💰 Presupuesto inicial: $" + jugador.getPresupuesto());
    }

    /**
     * Inicializa la flota de vehículos
     */
    private void inicializarFlota() {
        System.out.println("\n🛠 Flota creada:");
        System.out.println("   - 3 camiones (4x4)");
        System.out.println("   - 1 avión de carga");
        System.out.println("   - 2 almacenes (Lima, Cusco)");
    }

    /**
     * Muestra el menú principal
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n==============================================");
        System.out.println("📅 DÍA " + diaActual + " | ALMACÉN PRINCIPAL: LIMA");
        System.out.println("==============================================");
        System.out.println("\n1. Ver nuevo pedido");
        System.out.println("2. Planificar envío");
        System.out.println("3. Añadir servicios a envío");
        System.out.println("4. Resolver incidente");
        System.out.println("5. Rastrear envío");
        System.out.println("6. Ver estadísticas");
        System.out.println("7. Pasar al siguiente día");
        System.out.println("8. Salir");
        System.out.print("\nSeleccione una opción: ");
    }

    /**
     * Procesa la opción seleccionada
     * @param opcion Opción elegida por el usuario
     */
    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                mostrarNuevoPedido();
                break;
            case "2":
                planificarEnvio();
                break;
            case "3":
                anadirServicios();
                break;
            case "4":
                resolverIncidente();
                break;
            case "5":
                rastrearEnvio();
                break;
            case "6":
                mostrarEstadisticas();
                break;
            case "7":
                pasarDia();
                break;
            case "8":
                System.exit(0);
                break;
            default:
                System.out.println("❌ Opción no válida");
        }
    }

    /**
     * Muestra un nuevo pedido aleatorio
     */
    private void mostrarNuevoPedido() {
        String[] clientes = {"Hospital Regional Cusco", "Farmacia Central", "Laboratorio Médico"};
        String[] cargas = {"Vacunas", "Medicamentos", "Equipo médico"};
        String[] prioridades = {"URGENTE", "NORMAL", "BAJA"};

        String cliente = clientes[random.nextInt(clientes.length)];
        String carga = cargas[random.nextInt(cargas.length)];
        String prioridad = prioridades[random.nextInt(prioridades.length)];
        int pago = 5000 + random.nextInt(5000);
        int idPedido = 1000 + random.nextInt(9000);

        pedidos.put(String.valueOf(idPedido), pago);

        System.out.println("\n❗ Nuevo pedido entrante:");
        System.out.println("   - ID: #" + idPedido);
        System.out.println("   - Cliente: " + cliente);
        System.out.println("   - Carga: " + carga);
        System.out.println("   - Prioridad: " + prioridad);
        System.out.println("   - Pago ofrecido: $" + pago);
    }

    /**
     * Permite planificar un envío
     */
    private void planificarEnvio() {
        System.out.print("\nIngrese ID del pedido: ");
        String idPedido = scanner.nextLine();
        
        if (!pedidos.containsKey(idPedido)) {
            System.out.println("❌ Pedido no encontrado");
            return;
        }

        System.out.println("\n🔍 Analizando rutas disponibles...");
        System.out.println("1) Terrestre (camión): 18 horas - Coste $1,200");
        System.out.println("2) Aéreo (avión): 4 horas - Coste $4,500");
        
        if (random.nextBoolean()) {
            System.out.println("❌ Bloqueo parcial en carretera (Evento aleatorio)");
        }

        System.out.print("\nSeleccione ruta (1-2): ");
        String ruta = scanner.nextLine();

        if (ruta.equals("2")) {
            System.out.println("\n⏳ Aplicando patrón *Strategy*: Cambiando a estrategia rápida...");
            System.out.println("✈ Envío #" + idPedido + " asignado a AVIÓN (Costo total: $4,500).");
        } else {
            System.out.println("\n🚚 Envío #" + idPedido + " asignado a CAMIÓN (Costo total: $1,200).");
        }
    }

    /**
     * Permite añadir servicios a un envío
     */
    private void anadirServicios() {
        System.out.print("\nIngrese ID del pedido: ");
        String idPedido = scanner.nextLine();
        
        if (!pedidos.containsKey(idPedido)) {
            System.out.println("❌ Pedido no encontrado");
            return;
        }

        System.out.println("\n💡 Servicios disponibles:");
        System.out.println("1) Refrigeración (+$500)");
        System.out.println("2) Seguro (+$300)");
        System.out.println("3) Prioridad urgente (+$1000)");
        
        System.out.print("\nSeleccione servicios (separados por coma): ");
        String[] servicios = scanner.nextLine().split(",");

        int costoExtra = 0;
        for (String servicio : servicios) {
            switch (servicio.trim()) {
                case "1":
                    costoExtra += 500;
                    break;
                case "2":
                    costoExtra += 300;
                    break;
                case "3":
                    costoExtra += 1000;
                    break;
            }
        }

        System.out.println("\n💡 Servicios añadidos:");
        System.out.println("   - Costo adicional: $" + costoExtra);
    }

    /**
     * Permite resolver un incidente
     */
    private void resolverIncidente() {
        String[] incidentes = {
            "Tormenta eléctrica",
            "Bloqueo de carretera",
            "Problema mecánico",
            "Retraso en aduana"
        };

        String incidente = incidentes[random.nextInt(incidentes.length)];
        int idIncidente = 100 + random.nextInt(900);

        System.out.println("\n❗ ALERTA: Incidente #" + idIncidente + " - " + incidente);
        System.out.println("   - Riesgo: Retraso en entrega");
        System.out.println("   - Soluciones posibles:");
        System.out.println("     1) Esperar (50% de retraso)");
        System.out.println("     2) Desviar ruta (+$1,000, entrega en 6h)");
        
        System.out.print("\nSeleccione solución (1-2): ");
        String solucion = scanner.nextLine();

        System.out.println("\n🛠 Aplicando patrón *Template Method*:");
        System.out.println("   1. Identificando causa: " + incidente);
        System.out.println("   2. Asignando recursos...");
        
        if (solucion.equals("2")) {
            System.out.println("   3. Desviando ruta...");
            System.out.println("✅ Resuelto: Envío llegará con 6h de retraso.");
        } else {
            System.out.println("   3. Esperando condiciones...");
            System.out.println("✅ Resuelto: Envío llegará con 12h de retraso.");
        }
    }

    /**
     * Permite rastrear un envío
     */
    private void rastrearEnvio() {
        System.out.print("\nIngrese ID del pedido: ");
        String idPedido = scanner.nextLine();
        
        if (!pedidos.containsKey(idPedido)) {
            System.out.println("❌ Pedido no encontrado");
            return;
        }

        System.out.println("\n📌 Estado actual (patrón *State*):");
        System.out.println("   - 📦 Pedido #" + idPedido + ": \"EN TRÁNSITO\"");
        System.out.println("   - 📍 Ubicación: " + (random.nextBoolean() ? "Lima" : "Cusco"));
        System.out.println("   - ⏳ Llegada estimada: Día " + (diaActual + 1));
    }

    /**
     * Muestra las estadísticas del juego
     */
    private void mostrarEstadisticas() {
        System.out.println("\n📊 Métricas actuales:");
        System.out.println("   - 💰 Presupuesto: $" + jugador.getPresupuesto());
        System.out.println("   - 😊 Satisfacción clientes: " + satisfaccionClientes + "%");
        System.out.println("   - 🚚 Envíos exitosos: " + enviosExitosos + "/" + enviosTotales);
    }

    /**
     * Avanza al siguiente día
     */
    private void pasarDia() {
        diaActual++;
        System.out.println("\n==============================================");
        System.out.println("📅 DÍA " + diaActual + " | ENTREGA FINAL");
        System.out.println("==============================================");
        
        // Simular resultados de envíos
        for (Map.Entry<String, Integer> pedido : pedidos.entrySet()) {
            boolean exito = random.nextBoolean();
            if (exito) {
                enviosExitosos++;
                jugador.recuperarPresupuesto(pedido.getValue());
            } else {
                satisfaccionClientes -= 5;
            }
            enviosTotales++;
        }
        
        pedidos.clear();
        mostrarEstadisticas();
    }

    /**
     * Muestra la pantalla de fin de juego
     */
    private void mostrarGameOver() {
        System.out.println("\n==============================================");
        System.out.println("🎮 GAME OVER");
        System.out.println("==============================================");
        System.out.println("💰 Presupuesto final: $" + jugador.getPresupuesto());
        System.out.println("😊 Satisfacción final: " + satisfaccionClientes + "%");
        System.out.println("🚚 Envíos totales: " + enviosTotales);
        System.out.println("✅ Envíos exitosos: " + enviosExitosos);
    }
} 