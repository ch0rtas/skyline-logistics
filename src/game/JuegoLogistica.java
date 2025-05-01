package game;

import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

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
    private Calendar fechaActual;
    private String almacenPrincipal;
    private String dificultad;
    private int satisfaccionClientes;
    private int enviosExitosos;
    private int enviosTotales;
    private int beneficiosAcumulados;
    private static final double TASA_IMPUESTOS = 0.45;
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");
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
        this.fechaActual = Calendar.getInstance(); // Fecha actual del sistema
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
     * Calcula la multa por rechazar un pedido
     * @param pedido Pedido a rechazar
     * @return int con el monto de la multa
     */
    private int calcularMultaRechazo(Pedido pedido) {
        int base = 5000;
        int multa = base;
        
        // Aumentar según la dificultad
        switch (dificultad) {
            case "easy":
                multa *= 1;
                break;
            case "medium":
                multa *= 1.5;
                break;
            case "hard":
                multa *= 2;
                break;
        }
        
        // Aumentar según el día actual
        multa *= (1 + (diaActual * 0.1)); // 10% más por cada día
        
        // Aumentar según la prioridad
        if (pedido.getPrioridad().equals("URGENTE")) {
            multa *= 2;
        } else if (pedido.getPrioridad().equals("NORMAL")) {
            multa *= 1.5;
        }
        
        return (int) multa;
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
        System.out.println("📅 DÍA " + diaActual + " (" + formatoFecha.format(fechaActual.getTime()) + ") | ALMACÉN PRINCIPAL: " + almacenPrincipal.toUpperCase());
        System.out.println("==============================================");
        System.out.println("\n1. Ver pedidos pendientes");
        System.out.println("2. Ver pedidos en curso");
        System.out.println("3. Gestionar pedidos");
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
        int peso = 100 + random.nextInt(900); // Peso entre 100 y 1000 kg

        // Seleccionar un destino aleatorio que no sea el almacén principal
        String destino;
        do {
            destino = PROVINCIAS[random.nextInt(PROVINCIAS.length)];
        } while (destino.equalsIgnoreCase(almacenPrincipal));

        // Generar fecha de entrega según la prioridad
        Calendar fechaEntrega = (Calendar) fechaActual.clone();
        if (prioridad.equals("URGENTE")) {
            // Pedidos urgentes deben entregarse al día siguiente
            fechaEntrega.add(Calendar.DAY_OF_MONTH, 1);
            pago *= 1.5; // Aumentar el pago en un 50% por urgencia
        } else {
            // Para pedidos normales y bajos, entre 2 y 5 días
            int diasExtra = 2 + random.nextInt(4); // 2 a 5 días
            fechaEntrega.add(Calendar.DAY_OF_MONTH, diasExtra);
        }

        // Determinar si es un pedido de varios días según la dificultad
        int diasEntrega = 1;
        if (random.nextDouble() < calcularProbabilidadMultiDia()) {
            diasEntrega = 2 + random.nextInt(3); // Entre 2 y 4 días
            pago *= diasEntrega; // Aumentar el pago proporcionalmente
        }

        return new Pedido(idPedido, cliente, carga, prioridad, pago, diasEntrega, destino, fechaEntrega, peso);
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
            System.out.println("   Peso: " + pedido.getPeso() + " kg");
            System.out.println("   Prioridad: " + pedido.getPrioridad());
            System.out.println("   Destino: " + pedido.getDestino());
            System.out.println("   Fecha entrega máxima: " + pedido.getFechaEntrega());
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
            System.out.println("Peso: " + pedido.getPeso() + " kg");
            System.out.println("Destino: " + pedido.getDestino());
            System.out.println("Transporte: " + pedido.getTransporteAsignado());
            System.out.println("Fecha entrega máxima: " + pedido.getFechaEntrega());
            System.out.println("Días restantes: " + pedido.getDiasRestantes() + "/" + pedido.getDiasEntrega());
            System.out.println("Pago base: $" + pedido.getPago());
            System.out.println("Bonificación por día: $" + pedido.getBonificacionPorDia());
            System.out.println("Multa por día: $" + pedido.getMultaPorDia());
            
            int diasRetraso = pedido.calcularDiasRetraso(fechaActual);
            if (diasRetraso > 0) {
                System.out.println("⚠️ Retraso: " + diasRetraso + " días");
            }
            
            System.out.println("Pago estimado: $" + pedido.calcularPagoFinal());
        }
    }

    /**
     * Muestra los vehículos disponibles para un pedido
     * @param pedido Pedido a transportar
     * @return Lista de vehículos disponibles
     */
    private List<Vehiculo> mostrarVehiculosDisponibles(Pedido pedido) {
        List<Vehiculo> disponibles = new ArrayList<>();
        System.out.println("\n🚗 VEHÍCULOS DISPONIBLES:");
        
        for (Vehiculo vehiculo : flota) {
            if (vehiculo.getPedidoAsignado() == null && vehiculo.getCapacidad() >= pedido.getPeso()) {
                disponibles.add(vehiculo);
                System.out.println("\n" + (disponibles.size()) + ". " + vehiculo.getTipo() + " " + vehiculo.getId());
                System.out.println("   - Capacidad: " + vehiculo.getCapacidad() + " kg");
                System.out.println("   - Velocidad: " + vehiculo.getVelocidad() + " km/h");
                System.out.println("   - Coste/km: $" + vehiculo.getCostePorKm());
            }
        }
        
        if (disponibles.isEmpty()) {
            System.out.println("❌ No hay vehículos disponibles para este pedido");
            System.out.println("   - Peso del pedido: " + pedido.getPeso() + " kg");
        }
        
        return disponibles;
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
        System.out.println("2. Rechazar (Multa: $" + calcularMultaRechazo(pedido) + ")");
        System.out.print("\nOpción: ");
        String opcion = scanner.nextLine();

        if (opcion.equals("2")) {
            System.out.println("\n⚠️ ¿Está seguro de rechazar el pedido #" + idPedido + "?");
            System.out.println("   - Multa por rechazo: $" + calcularMultaRechazo(pedido));
            System.out.print("   - Confirmar (S/N): ");
            
            String confirmacion = scanner.nextLine().toUpperCase();
            if (confirmacion.equals("S")) {
                jugador.recibirDanio(calcularMultaRechazo(pedido));
                pedidosPendientes.remove(pedido);
                System.out.println("❌ Pedido #" + idPedido + " rechazado");
                System.out.println("💰 Multa aplicada: $" + calcularMultaRechazo(pedido));
            }
            return;
        }

        // Mostrar vehículos disponibles
        List<Vehiculo> disponibles = mostrarVehiculosDisponibles(pedido);
        if (disponibles.isEmpty()) {
            return;
        }

        System.out.print("\nSeleccione vehículo (1-" + disponibles.size() + "): ");
        int opcionVehiculo = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (opcionVehiculo < 0 || opcionVehiculo >= disponibles.size()) {
            System.out.println("❌ Opción no válida");
            return;
        }

        Vehiculo vehiculoSeleccionado = disponibles.get(opcionVehiculo);
        
        // Añadir servicios
        System.out.println("\n💡 Servicios disponibles:");
        System.out.println("1) Refrigeración (+$500)");
        System.out.println("2) Seguro (+$300)");
        System.out.println("3) Prioridad urgente (+$1,000)");
        
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

        // Calcular costo total
        int costoTotal = vehiculoSeleccionado.getCostePorKm() * 100 + costoExtra; // Asumimos 100km de distancia promedio
        
        // Verificar balance
        if (jugador.getPresupuesto() < costoTotal) {
            System.out.println("❌ Balance insuficiente para realizar el envío");
            return;
        }

        // Asignar vehículo al pedido
        vehiculoSeleccionado.asignarPedido(pedido);
        pedido.setTransporteAsignado(vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());
        
        // Descontar el costo del balance
        jugador.recibirDanio(costoTotal);

        // Resolver incidente si ocurre
        if (random.nextBoolean()) {
            resolverIncidente(pedido);
        }

        pedidosPendientes.remove(pedido);
        pedidosEnCurso.add(pedido);
        System.out.println("\n✅ Pedido #" + idPedido + " gestionado exitosamente");
        System.out.println("   - Vehículo asignado: " + vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());
        System.out.println("   - Costo total: $" + costoTotal);
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
        fechaActual.add(Calendar.DAY_OF_MONTH, 1); // Añadir un día a la fecha actual
        
        System.out.println("\n==============================================");
        System.out.println("📅 DÍA " + diaActual + " (" + formatoFecha.format(fechaActual.getTime()) + ") | ENTREGA FINAL");
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