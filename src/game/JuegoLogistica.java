package game;

import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Clase principal que gestiona el juego de logística
 */
public class JuegoLogistica {
    private Jugador jugador;
    private Scanner scanner;
    private Random random;
    private Map<String, Pedido> pedidos;
    private List<Pedido> pedidosPendientes;
    private List<Pedido> pedidosEnCurso;
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
        this.jugador = new Jugador("Jugador", calcularBalanceInicial());
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.pedidos = new HashMap<>();
        this.pedidosPendientes = new ArrayList<>();
        this.pedidosEnCurso = new ArrayList<>();
        this.diaActual = 1;
        this.satisfaccionClientes = 100;
        this.enviosExitosos = 0;
        this.enviosTotales = 0;
    }

    /**
     * Calcula el balance inicial según la dificultad
     * @return int con el balance inicial
     */
    private int calcularBalanceInicial() {
        switch (dificultad) {
            case "easy":
                return 50000;
            case "medium":
                return 25000;
            case "hard":
                return 10000;
            default:
                return 25000;
        }
    }

    /**
     * Calcula la multa por rechazar un pedido según la dificultad
     * @return int con el monto de la multa
     */
    private int calcularMultaRechazo() {
        int base = 5000;
        switch (dificultad) {
            case "easy":
                return base;
            case "medium":
                return base * 2;
            case "hard":
                return base * 3;
            default:
                return base;
        }
    }

    /**
     * Inicia el juego
     */
    public void iniciar() {
        mostrarBienvenida();
        inicializarFlota();
        generarPedidosDia();
        
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
        System.out.println("💰 Balance inicial: $" + jugador.getPresupuesto());
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
        System.out.println("\n1. Ver pedidos pendientes");
        System.out.println("2. Ver pedidos en curso");
        System.out.println("3. Gestionar pedido");
        System.out.println("4. Ver estadísticas");
        System.out.println("5. Pasar al siguiente día");
        System.out.println("6. Salir");
        System.out.print("\nSeleccione una opción: ");
    }

    /**
     * Procesa la opción seleccionada
     * @param opcion Opción elegida por el usuario
     */
    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                mostrarPedidosPendientes();
                break;
            case "2":
                mostrarPedidosEnCurso();
                break;
            case "3":
                gestionarPedido();
                break;
            case "4":
                mostrarEstadisticas();
                break;
            case "5":
                pasarDia();
                break;
            case "6":
                System.exit(0);
                break;
            default:
                System.out.println("❌ Opción no válida");
        }
    }

    /**
     * Genera los pedidos del día según la dificultad
     */
    private void generarPedidosDia() {
        int cantidadPedidos = calcularCantidadPedidos();
        pedidosPendientes.clear();
        
        for (int i = 0; i < cantidadPedidos; i++) {
            Pedido pedido = generarPedidoAleatorio();
            pedidosPendientes.add(pedido);
            pedidos.put(pedido.getId(), pedido);
        }
        
        System.out.println("\n📦 Se han generado " + cantidadPedidos + " nuevos pedidos para el día " + diaActual);
    }

    /**
     * Calcula la cantidad de pedidos según la dificultad
     * @return int con la cantidad de pedidos
     */
    private int calcularCantidadPedidos() {
        int base = 2; // Pedidos base por día
        switch (dificultad) {
            case "easy":
                return base + (diaActual / 3); // Aumenta 1 cada 3 días
            case "medium":
                return base + (diaActual / 2); // Aumenta 1 cada 2 días
            case "hard":
                return base + diaActual; // Aumenta 1 cada día
            default:
                return base;
        }
    }

    /**
     * Genera un pedido aleatorio
     * @return Pedido generado
     */
    private Pedido generarPedidoAleatorio() {
        String[] clientes = {"Hospital Regional Cusco", "Farmacia Central", "Laboratorio Médico"};
        String[] cargas = {"Vacunas", "Medicamentos", "Equipo médico"};
        String[] prioridades = {"URGENTE", "NORMAL", "BAJA"};

        String cliente = clientes[random.nextInt(clientes.length)];
        String carga = cargas[random.nextInt(cargas.length)];
        String prioridad = prioridades[random.nextInt(prioridades.length)];
        int pago = 5000 + random.nextInt(5000);
        String idPedido = "P" + (1000 + random.nextInt(9000));

        return new Pedido(idPedido, cliente, carga, prioridad, pago);
    }

    /**
     * Muestra los pedidos pendientes
     */
    private void mostrarPedidosPendientes() {
        if (pedidosPendientes.isEmpty()) {
            System.out.println("\n📭 No hay pedidos pendientes");
            return;
        }

        System.out.println("\n📦 PEDIDOS PENDIENTES:");
        for (Pedido pedido : pedidosPendientes) {
            System.out.println("\n   ID: #" + pedido.getId());
            System.out.println("   Cliente: " + pedido.getCliente());
            System.out.println("   Carga: " + pedido.getCarga());
            System.out.println("   Prioridad: " + pedido.getPrioridad());
            System.out.println("   Pago ofrecido: $" + pedido.getPago());
        }
    }

    /**
     * Muestra los pedidos en curso
     */
    private void mostrarPedidosEnCurso() {
        if (pedidosEnCurso.isEmpty()) {
            System.out.println("\n📭 No hay pedidos en curso");
            return;
        }

        System.out.println("\n📦 PEDIDOS EN CURSO:");
        for (Pedido pedido : pedidosEnCurso) {
            System.out.println("\n   ID: #" + pedido.getId());
            System.out.println("   Cliente: " + pedido.getCliente());
            System.out.println("   Carga: " + pedido.getCarga());
            System.out.println("   Prioridad: " + pedido.getPrioridad());
            System.out.println("   Pago ofrecido: $" + pedido.getPago());
        }
    }

    /**
     * Permite gestionar un pedido
     */
    private void gestionarPedido() {
        if (pedidosPendientes.isEmpty()) {
            System.out.println("\n📭 No hay pedidos pendientes para gestionar");
            return;
        }

        mostrarPedidosPendientes();
        System.out.print("\nIngrese ID del pedido a gestionar: ");
        String idPedido = scanner.nextLine();

        Pedido pedido = pedidos.get(idPedido);
        if (pedido == null) {
            System.out.println("❌ Pedido no encontrado");
            return;
        }

        System.out.println("\n¿Qué desea hacer con el pedido #" + idPedido + "?");
        System.out.println("1. Enviar");
        System.out.println("2. Rechazar (Multa: $" + calcularMultaRechazo() + ")");
        System.out.print("\nOpción: ");
        String opcion = scanner.nextLine();

        if (opcion.equals("2")) {
            System.out.println("\n⚠️ ¿Está seguro de rechazar el pedido #" + idPedido + "?");
            System.out.println("   - Multa por rechazo: $" + calcularMultaRechazo());
            System.out.print("   - Confirmar (S/N): ");
            
            String confirmacion = scanner.nextLine().toUpperCase();
            if (confirmacion.equals("S")) {
                jugador.recibirDanio(calcularMultaRechazo());
                pedidosPendientes.remove(pedido);
                System.out.println("❌ Pedido #" + idPedido + " rechazado");
                System.out.println("💰 Multa aplicada: $" + calcularMultaRechazo());
            }
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

        int costoRuta = 0;
        if (ruta.equals("2")) {
            costoRuta = 4500;
            System.out.println("\n⏳ Aplicando patrón *Strategy*: Cambiando a estrategia rápida...");
            System.out.println("✈ Envío #" + idPedido + " asignado a AVIÓN (Costo total: $" + costoRuta + ").");
        } else {
            costoRuta = 1200;
            System.out.println("\n🚚 Envío #" + idPedido + " asignado a CAMIÓN (Costo total: $" + costoRuta + ").");
        }

        // Verificar si hay balance suficiente
        if (jugador.getPresupuesto() < costoRuta) {
            System.out.println("❌ Balance insuficiente para realizar el envío");
            return;
        }

        // Añadir servicios
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

        // Verificar balance total
        int costoTotal = costoRuta + costoExtra;
        if (jugador.getPresupuesto() < costoTotal) {
            System.out.println("❌ Balance insuficiente para los servicios seleccionados");
            return;
        }

        System.out.println("\n💡 Servicios añadidos:");
        System.out.println("   - Costo adicional: $" + costoExtra);
        System.out.println("   - Costo total: $" + costoTotal);

        // Descontar el costo del balance
        jugador.recibirDanio(costoTotal);

        // Resolver incidente si ocurre
        if (random.nextBoolean()) {
            resolverIncidente(pedido);
        }

        pedidosPendientes.remove(pedido);
        pedidosEnCurso.add(pedido);
        System.out.println("\n✅ Pedido #" + idPedido + " gestionado exitosamente");
    }

    /**
     * Resuelve un incidente para un pedido
     * @param pedido Pedido afectado
     */
    private void resolverIncidente(Pedido pedido) {
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
     * Muestra las estadísticas del juego
     */
    private void mostrarEstadisticas() {
        System.out.println("\n📊 Métricas actuales:");
        System.out.println("   - 💰 Balance: $" + jugador.getPresupuesto());
        System.out.println("   - 😊 Satisfacción clientes: " + satisfaccionClientes + "%");
        System.out.println("   - 🚚 Envíos exitosos: " + enviosExitosos + "/" + enviosTotales);
        System.out.println("   - 📦 Pedidos pendientes: " + pedidosPendientes.size());
        System.out.println("   - 📦 Pedidos en curso: " + pedidosEnCurso.size());
    }

    /**
     * Avanza al siguiente día
     */
    private void pasarDia() {
        if (!pedidosPendientes.isEmpty()) {
            System.out.println("\n❌ No puedes pasar al siguiente día con pedidos pendientes");
            return;
        }

        diaActual++;
        System.out.println("\n==============================================");
        System.out.println("📅 DÍA " + diaActual + " | ENTREGA FINAL");
        System.out.println("==============================================");
        
        // Simular resultados de envíos
        for (Pedido pedido : pedidosEnCurso) {
            boolean exito = random.nextBoolean();
            if (exito) {
                enviosExitosos++;
                jugador.recuperarPresupuesto(pedido.getPago());
                System.out.println("✅ Envío #" + pedido.getId() + " completado exitosamente");
                System.out.println("💰 Ganancia: $" + pedido.getPago());
            } else {
                satisfaccionClientes -= 5;
                System.out.println("❌ Envío #" + pedido.getId() + " falló");
            }
            enviosTotales++;
        }
        
        pedidosEnCurso.clear();
        pedidos.clear();
        generarPedidosDia();
        mostrarEstadisticas();
    }

    /**
     * Muestra la pantalla de fin de juego
     */
    private void mostrarGameOver() {
        System.out.println("\n==============================================");
        System.out.println("🎮 GAME OVER");
        System.out.println("==============================================");
        System.out.println("💰 Balance final: $" + jugador.getPresupuesto());
        System.out.println("😊 Satisfacción final: " + satisfaccionClientes + "%");
        System.out.println("🚚 Envíos totales: " + enviosTotales);
        System.out.println("✅ Envíos exitosos: " + enviosExitosos);
    }
} 