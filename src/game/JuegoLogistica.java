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
    private List<Vehiculo> flota;
    private int diaActual;
    private String almacenPrincipal;
    private String dificultad;
    private int satisfaccionClientes;
    private int enviosExitosos;
    private int enviosTotales;
    private int beneficiosAcumulados;
    private static final double TASA_IMPUESTOS = 0.45;
    private static final String[] PROVINCIAS = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza",
        "Málaga", "Murcia", "Palma de Mallorca", "Las Palmas", "Bilbao",
        "Alicante", "Córdoba", "Valladolid", "Vigo", "Gijón"
    };

    /**
     * Constructor del juego
     * @param almacenPrincipal Provincia seleccionada como almacén principal
     * @param dificultad Nivel de dificultad
     */
    public JuegoLogistica(String almacenPrincipal, String dificultad) {
        this.almacenPrincipal = almacenPrincipal;
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
        this.beneficiosAcumulados = 0;
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
     * Calcula los días entre pagos de impuestos según la dificultad
     * @return int con los días entre pagos
     */
    private int calcularDiasImpuestos() {
        switch (dificultad) {
            case "easy":
                return 8;
            case "medium":
                return 5;
            case "hard":
                return 3;
            default:
                return 5;
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
        System.out.println("\n✅ Sistema iniciado en región: " + almacenPrincipal.toUpperCase());
        System.out.println("💰 Balance inicial: $" + jugador.getPresupuesto());
    }

    /**
     * Inicializa la flota de vehículos según la dificultad
     */
    private void inicializarFlota() {
        flota = new ArrayList<>();
        
        switch (dificultad) {
            case "easy":
                // 2 furgonetas, 1 camión, 1 barco, 1 avión
                flota.add(new Vehiculo("Furgoneta", "F1", 1000, 80, 2));
                flota.add(new Vehiculo("Furgoneta", "F2", 1000, 80, 2));
                flota.add(new Vehiculo("Camión", "C1", 5000, 60, 3));
                flota.add(new Vehiculo("Barco", "B1", 20000, 30, 4));
                flota.add(new Vehiculo("Avión", "A1", 10000, 500, 10));
                break;
            case "medium":
                // 1 furgoneta, 1 camión, 1 barco
                flota.add(new Vehiculo("Furgoneta", "F1", 1000, 80, 2));
                flota.add(new Vehiculo("Camión", "C1", 5000, 60, 3));
                flota.add(new Vehiculo("Barco", "B1", 20000, 30, 4));
                break;
            case "hard":
                // 1 furgoneta, 1 barco
                flota.add(new Vehiculo("Furgoneta", "F1", 1000, 80, 2));
                flota.add(new Vehiculo("Barco", "B1", 20000, 30, 4));
                break;
        }

        System.out.println("\n🛠 Empresa creada:");
        System.out.println("   - Almacén: " + almacenPrincipal.toUpperCase());
        System.out.print("   - Vehículos: ");
        for (int i = 0; i < flota.size(); i++) {
            Vehiculo v = flota.get(i);
            System.out.print(v.getTipo() + " " + v.getId());
            if (i < flota.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    /**
     * Muestra la flota de vehículos y sus pedidos asignados
     */
    private void mostrarFlota() {
        System.out.println("\n🚗 FLOTA DE VEHÍCULOS:");
        for (Vehiculo vehiculo : flota) {
            System.out.println("\n   " + vehiculo.getTipo() + " " + vehiculo.getId());
            System.out.println("   - Capacidad: " + vehiculo.getCapacidad() + " kg");
            System.out.println("   - Velocidad: " + vehiculo.getVelocidad() + " km/h");
            System.out.println("   - Coste/km: $" + vehiculo.getCostePorKm());
            if (vehiculo.getPedidoAsignado() != null) {
                Pedido pedido = vehiculo.getPedidoAsignado();
                System.out.println("   - Pedido asignado: " + pedido.getId());
                System.out.println("     Destino: " + pedido.getDestino());
                System.out.println("     Días restantes: " + pedido.getDiasRestantes());
            } else {
                System.out.println("   - Estado: Disponible");
            }
        }
    }

    /**
     * Muestra el menú principal
     */
    private void mostrarMenuPrincipal() {
        System.out.println("\n==============================================");
        System.out.println("📅 DÍA " + diaActual + " | ALMACÉN PRINCIPAL: " + almacenPrincipal.toUpperCase());
        System.out.println("==============================================");
        System.out.println("\n1. Ver pedidos pendientes");
        System.out.println("2. Ver pedidos en curso");
        System.out.println("3. Gestionar pedido");
        System.out.println("4. Ver flota");
        System.out.println("5. Ver estadísticas");
        System.out.println("6. Pasar al siguiente día");
        System.out.println("7. Salir");
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
                mostrarFlota();
                break;
            case "5":
                mostrarEstadisticas();
                break;
            case "6":
                pasarDia();
                break;
            case "7":
                System.exit(0);
                break;
            default:
                System.out.println("❌ Opción no válida");
        }
    }

    /**
     * Genera un pedido aleatorio
     * @return Pedido generado
     */
    private Pedido generarPedidoAleatorio() {
        String[] clientes = {"Hospital Regional", "Farmacia Central", "Laboratorio Médico"};
        String[] cargas = {"Vacunas", "Medicamentos", "Equipo médico"};
        String[] prioridades = {"URGENTE", "NORMAL", "BAJA"};

        String cliente = clientes[random.nextInt(clientes.length)];
        String carga = cargas[random.nextInt(cargas.length)];
        String prioridad = prioridades[random.nextInt(prioridades.length)];
        int pago = 5000 + random.nextInt(5000);
        String idPedido = "P" + (100 + random.nextInt(900)); // IDs de 3 dígitos

        // Seleccionar un destino aleatorio que no sea el almacén principal
        String destino;
        do {
            destino = PROVINCIAS[random.nextInt(PROVINCIAS.length)];
        } while (destino.equalsIgnoreCase(almacenPrincipal));

        // Determinar si es un pedido de varios días según la dificultad
        int diasEntrega = 1;
        if (random.nextDouble() < calcularProbabilidadMultiDia()) {
            diasEntrega = 2 + random.nextInt(3); // Entre 2 y 4 días
            pago *= diasEntrega; // Aumentar el pago proporcionalmente
        }

        return new Pedido(idPedido, cliente, carga, prioridad, pago, diasEntrega, destino);
    }

    /**
     * Calcula la probabilidad de que un pedido sea de varios días según la dificultad
     * @return double con la probabilidad
     */
    private double calcularProbabilidadMultiDia() {
        switch (dificultad) {
            case "easy":
                return 0.2; // 20% de probabilidad
            case "medium":
                return 0.4; // 40% de probabilidad
            case "hard":
                return 0.6; // 60% de probabilidad
            default:
                return 0.3;
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
        
        System.out.println("\n📦 Han entrado " + cantidadPedidos + " paquetes");
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
            System.out.println("\n📦 No hay pedidos en curso");
            return;
        }

        System.out.println("\n📦 PEDIDOS EN CURSO:");
        for (Pedido pedido : pedidosEnCurso) {
            System.out.println("\nID: " + pedido.getId());
            System.out.println("Cliente: " + pedido.getCliente());
            System.out.println("Carga: " + pedido.getCarga());
            System.out.println("Destino: " + pedido.getDestino());
            System.out.println("Transporte: " + pedido.getTransporteAsignado());
            System.out.println("Días restantes: " + pedido.getDiasRestantes() + "/" + pedido.getDiasEntrega());
            System.out.println("Pago base: $" + pedido.getPago());
            System.out.println("Bonificación por día: $" + pedido.getBonificacionPorDia());
            System.out.println("Multa por día: $" + pedido.getMultaPorDia());
            System.out.println("Pago estimado: $" + pedido.calcularPagoFinal());
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
     * Procesa los accidentes aleatorios
     */
    private void procesarAccidentes() {
        for (Pedido pedido : new ArrayList<>(pedidosEnCurso)) {
            if (random.nextDouble() < 0.1) { // 10% de probabilidad de accidente
                System.out.println("\n⚠️ ¡ACCIDENTE! El paquete #" + pedido.getId() + " se ha perdido");
                System.out.println("   - Cliente: " + pedido.getCliente());
                System.out.println("   - Carga: " + pedido.getCarga());
                System.out.println("   - Debes pagar: $" + pedido.getPago());
                
                jugador.recibirDanio(pedido.getPago());
                pedidosEnCurso.remove(pedido);
                satisfaccionClientes -= 10;
            }
        }
    }

    /**
     * Procesa el pago de impuestos
     */
    private void procesarImpuestos() {
        if (diaActual % calcularDiasImpuestos() == 0) {
            int impuestos = (int)(beneficiosAcumulados * TASA_IMPUESTOS);
            System.out.println("\n💰 HACIENDA: Debes pagar el " + (TASA_IMPUESTOS * 100) + "% de tus beneficios");
            System.out.println("   - Beneficios acumulados: $" + beneficiosAcumulados);
            System.out.println("   - Impuestos a pagar: $" + impuestos);
            
            jugador.recibirDanio(impuestos);
            beneficiosAcumulados = 0;
        }
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
        
        // Procesar accidentes
        procesarAccidentes();
        
        // Procesar envíos
        for (Pedido pedido : new ArrayList<>(pedidosEnCurso)) {
            pedido.reducirDiasRestantes();
            
            if (pedido.getDiasRestantes() <= 0) {
                boolean exito = random.nextBoolean();
                if (exito) {
                    enviosExitosos++;
                    int ganancia = pedido.getPago();
                    jugador.recuperarPresupuesto(ganancia);
                    beneficiosAcumulados += ganancia;
                    System.out.println("✅ Envío #" + pedido.getId() + " completado exitosamente");
                    System.out.println("💰 Ganancia: $" + ganancia);
                } else {
                    satisfaccionClientes -= 5;
                    System.out.println("❌ Envío #" + pedido.getId() + " falló");
                }
                enviosTotales++;
                pedidosEnCurso.remove(pedido);
            }
        }
        
        // Procesar impuestos
        procesarImpuestos();
        
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