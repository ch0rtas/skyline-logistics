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

    // Matriz de distancias entre provincias (en km)
    private static final int[][] DISTANCIAS = {
        // Madrid, Barcelona, Valencia, Sevilla, Zaragoza, Málaga, Murcia, Palma, Las Palmas, Bilbao, Alicante, Córdoba, Valladolid, Vigo, Gijón
        {0, 621, 352, 538, 325, 530, 400, 0, 0, 395, 420, 400, 193, 599, 450}, // Madrid
        {621, 0, 349, 1000, 296, 1000, 600, 0, 0, 610, 500, 900, 800, 1000, 800}, // Barcelona
        {352, 349, 0, 650, 300, 600, 250, 0, 0, 600, 166, 500, 500, 800, 700}, // Valencia
        {538, 1000, 650, 0, 800, 200, 400, 0, 0, 800, 500, 140, 600, 900, 800}, // Sevilla
        {325, 296, 300, 800, 0, 700, 500, 0, 0, 300, 400, 600, 300, 700, 600}, // Zaragoza
        {530, 1000, 600, 200, 700, 0, 300, 0, 0, 800, 400, 200, 700, 1000, 900}, // Málaga
        {400, 600, 250, 400, 500, 300, 0, 0, 0, 700, 100, 300, 600, 900, 800}, // Murcia
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Palma de Mallorca
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Las Palmas
        {395, 610, 600, 800, 300, 800, 700, 0, 0, 0, 600, 700, 280, 400, 300}, // Bilbao
        {420, 500, 166, 500, 400, 400, 100, 0, 0, 600, 0, 400, 500, 800, 700}, // Alicante
        {400, 900, 500, 140, 600, 200, 300, 0, 0, 700, 400, 0, 500, 800, 700}, // Córdoba
        {193, 800, 500, 600, 300, 700, 600, 0, 0, 280, 500, 500, 0, 400, 300}, // Valladolid
        {599, 1000, 800, 900, 700, 1000, 900, 0, 0, 400, 800, 800, 400, 0, 200}, // Vigo
        {450, 800, 700, 800, 600, 900, 800, 0, 0, 300, 700, 700, 300, 200, 0}  // Gijón
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
                flota.add(new Vehiculo("Furgoneta", "F1", 1000, 80, 2, "REFRIGERADO"));
                flota.add(new Vehiculo("Furgoneta", "F2", 1000, 80, 2, "FRÁGIL"));
                flota.add(new Vehiculo("Camión", "C1", 5000, 60, 3, "PELIGROSO", "ESCOLTADO"));
                flota.add(new Vehiculo("Barco", "B1", 20000, 30, 4, "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL"));
                flota.add(new Vehiculo("Avión", "A1", 10000, 500, 10, "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL"));
                break;
            case "medium":
                // 1 furgoneta, 1 camión, 1 barco
                flota.add(new Vehiculo("Furgoneta", "F1", 1000, 80, 2, "REFRIGERADO"));
                flota.add(new Vehiculo("Camión", "C1", 5000, 60, 3, "PELIGROSO"));
                flota.add(new Vehiculo("Barco", "B1", 20000, 30, 4, "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL"));
                break;
            case "hard":
                // 1 furgoneta, 1 barco
                flota.add(new Vehiculo("Furgoneta", "F1", 1000, 80, 2));
                flota.add(new Vehiculo("Barco", "B1", 20000, 30, 4, "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL"));
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
        System.out.println("TIPO     | ID      | CAPACIDAD | VELOCIDAD | COSTE/KM | ESTADO");
        System.out.println("---------|---------|-----------|-----------|----------|--------");
        for (Vehiculo vehiculo : flota) {
            String estado;
            if (vehiculo.getPedidoAsignado() != null) {
                Calendar fechaEntrega = (Calendar) fechaActual.clone();
                fechaEntrega.add(Calendar.DAY_OF_MONTH, vehiculo.getPedidoAsignado().getDiasRestantes());
                estado = "Ocupado (" + vehiculo.getPedidoAsignado().getId() + ") hasta " + formatoFecha.format(fechaEntrega.getTime());
            } else {
                estado = "Disponible";
            }
            System.out.printf("%-9s| %-8s| %-10d| %-10d| $%-8d| %s%n",
                vehiculo.getTipo(),
                vehiculo.getId(),
                vehiculo.getCapacidad(),
                vehiculo.getVelocidad(),
                vehiculo.getCostePorKm(),
                estado
            );
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
        String[] tiposPaquetes = {"NORMAL", "REFRIGERADO", "CONGELADO", "ESCOLTADO", "PELIGROSO", "FRÁGIL"};

        String cliente = clientes[random.nextInt(clientes.length)];
        String carga = cargas[random.nextInt(cargas.length)];
        String prioridad = prioridades[random.nextInt(prioridades.length)];
        String tipoPaquete = tiposPaquetes[random.nextInt(tiposPaquetes.length)];
        int pago = 5000 + random.nextInt(5000);
        String idPedido = "P" + (100 + random.nextInt(900)); // IDs de 3 dígitos
        int peso = 100 + random.nextInt(900); // Peso entre 100 y 1000 kg

        // Ajustar pago según tipo de paquete
        switch (tipoPaquete) {
            case "REFRIGERADO":
                pago *= 1.2;
                break;
            case "CONGELADO":
                pago *= 1.3;
                break;
            case "ESCOLTADO":
                pago *= 1.5;
                break;
            case "PELIGROSO":
                pago *= 1.4;
                break;
            case "FRÁGIL":
                pago *= 1.1;
                break;
        }

        // Seleccionar un destino aleatorio que no sea el almacén principal
        String destino;
        do {
            destino = PROVINCIAS[random.nextInt(PROVINCIAS.length)];
        } while (destino.equalsIgnoreCase(almacenPrincipal));

        // Generar fecha de entrega según la prioridad
        Calendar fechaEntrega = (Calendar) fechaActual.clone();
        if (prioridad.equals("URGENTE")) {
            fechaEntrega.add(Calendar.DAY_OF_MONTH, 1);
            pago *= 1.5;
        } else {
            int diasExtra = 2 + random.nextInt(4);
            fechaEntrega.add(Calendar.DAY_OF_MONTH, diasExtra);
        }

        // Determinar si es un pedido de varios días según la dificultad
        int diasEntrega = 1;
        if (random.nextDouble() < calcularProbabilidadMultiDia()) {
            diasEntrega = 2 + random.nextInt(3);
            pago *= diasEntrega;
        }

        return new Pedido(idPedido, cliente, carga, prioridad, pago, diasEntrega, destino, fechaEntrega, peso, tipoPaquete);
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
        System.out.println("ID      | CLIENTE              | CARGA           | PRIORIDAD  | PESO      | DESTINO         | TIPO        | PAGO        | ENTREGA");
        System.out.println("--------|----------------------|-----------------|------------|-----------|-----------------|-------------|-------------|----------");
        for (Pedido pedido : pedidosPendientes) {
            System.out.println(pedido.toStringFormateado());
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
        System.out.println("ID      | CLIENTE              | CARGA           | PRIORIDAD  | PESO      | DESTINO         | TIPO        | PAGO        | ENTREGA");
        System.out.println("--------|----------------------|-----------------|------------|-----------|-----------------|-------------|-------------|----------");
        for (Pedido pedido : pedidosEnCurso) {
            System.out.println(pedido.toStringFormateado());
        }
    }

    /**
     * Obtiene la distancia entre dos provincias
     * @param origen Provincia de origen
     * @param destino Provincia de destino
     * @return int con la distancia en km
     */
    private int obtenerDistancia(String origen, String destino) {
        int indiceOrigen = -1;
        int indiceDestino = -1;
        
        for (int i = 0; i < PROVINCIAS.length; i++) {
            if (PROVINCIAS[i].equalsIgnoreCase(origen)) {
                indiceOrigen = i;
            }
            if (PROVINCIAS[i].equalsIgnoreCase(destino)) {
                indiceDestino = i;
            }
        }
        
        if (indiceOrigen == -1 || indiceDestino == -1) {
            return 0;
        }
        
        return DISTANCIAS[indiceOrigen][indiceDestino];
    }

    /**
     * Verifica si una provincia es una isla
     * @param provincia Nombre de la provincia
     * @return true si es una isla, false si no
     */
    private boolean esIsla(String provincia) {
        return provincia.equalsIgnoreCase("Palma de Mallorca") || 
               provincia.equalsIgnoreCase("Las Palmas");
    }

    /**
     * Muestra los vehículos disponibles para un pedido
     * @param pedido Pedido a transportar
     * @return Lista de vehículos disponibles
     */
    private List<Vehiculo> mostrarVehiculosDisponibles(Pedido pedido) {
        List<Vehiculo> disponibles = new ArrayList<>();
        int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
        boolean origenEsIsla = esIsla(almacenPrincipal);
        boolean destinoEsIsla = esIsla(pedido.getDestino());
        boolean rutaMaritima = origenEsIsla || destinoEsIsla;
        
        System.out.println("\n🚗 VEHÍCULOS DISPONIBLES:");
        System.out.println("ID      | TIPO     | CAPACIDAD | VELOCIDAD | COSTE/KM | COSTE TOTAL | TIPOS PERMITIDOS");
        System.out.println("--------|----------|-----------|-----------|----------|-------------|-----------------");
        
        for (Vehiculo vehiculo : flota) {
            // Verificar restricciones de transporte
            boolean vehiculoPermitido = true;
            
            if (vehiculo.getPedidoAsignado() == null && 
                vehiculo.getCapacidad() >= pedido.getPeso() && 
                vehiculo.puedeTransportarTipo(pedido.getTipoPaquete())) {
                
                // Si el origen es una isla
                if (origenEsIsla) {
                    // Solo permitir furgoneta y camión para envíos en la misma provincia
                    if (!almacenPrincipal.equalsIgnoreCase(pedido.getDestino())) {
                        vehiculoPermitido = false;
                    } else if (!vehiculo.getTipo().equals("Furgoneta") && !vehiculo.getTipo().equals("Camión")) {
                        vehiculoPermitido = false;
                    }
                }
                
                // Si el destino es una isla
                if (destinoEsIsla) {
                    // Solo permitir barco o avión
                    if (!vehiculo.getTipo().equals("Barco") && !vehiculo.getTipo().equals("Avión")) {
                        vehiculoPermitido = false;
                    }
                }

                // Restricción para barcos
                if (vehiculo.getTipo().equals("Barco") && !rutaMaritima) {
                    vehiculoPermitido = false;
                }
                
                if (vehiculoPermitido) {
                    disponibles.add(vehiculo);
                    int costeTotal = vehiculo.getCostePorKm() * distancia;
                    System.out.printf("%-8s| %-9s| %-10d| %-10d| $%-8d| $%-11d| %s%n",
                        vehiculo.getId(),
                        vehiculo.getTipo(),
                        vehiculo.getCapacidad(),
                        vehiculo.getVelocidad(),
                        vehiculo.getCostePorKm(),
                        costeTotal,
                        String.join(", ", vehiculo.getTiposPaquetesPermitidos())
                    );
                }
            }
        }
        
        if (disponibles.isEmpty()) {
            System.out.println("❌ No hay vehículos disponibles para este pedido");
            System.out.println("   - Peso del pedido: " + pedido.getPeso() + " kg");
            System.out.println("   - Tipo de paquete: " + pedido.getTipoPaquete());
            if (origenEsIsla && !almacenPrincipal.equalsIgnoreCase(pedido.getDestino())) {
                System.out.println("   - ⚠️ No se pueden realizar envíos fuera de la isla desde " + almacenPrincipal);
            }
            if (destinoEsIsla) {
                System.out.println("   - ⚠️ Solo se pueden utilizar barcos o aviones para envíos a " + pedido.getDestino());
            }
            if (!rutaMaritima) {
                System.out.println("   - ⚠️ Los barcos solo están disponibles para rutas marítimas");
            }
        } else {
            System.out.println("\n   - Distancia a recorrer: " + distancia + " km");
            if (origenEsIsla) {
                System.out.println("   - ⚠️ Solo se permiten envíos dentro de " + almacenPrincipal);
            }
            if (destinoEsIsla) {
                System.out.println("   - ⚠️ Solo se permiten barcos o aviones para " + pedido.getDestino());
            }
            if (rutaMaritima) {
                System.out.println("   - 🌊 Ruta marítima disponible");
            }
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
                pedidosPendientes.remove(pedido);
                System.out.println("❌ Pedido #" + idPedido + " rechazado");
                System.out.println("💰 Multa aplicada: $" + calcularMultaRechazo(pedido));
                jugador.recibirDanio(calcularMultaRechazo(pedido));
            }
            return;
        }

        // Mostrar vehículos disponibles
        List<Vehiculo> disponibles = mostrarVehiculosDisponibles(pedido);
        if (disponibles.isEmpty()) {
            return;
        }

        System.out.print("\nIngrese ID del vehículo a utilizar: ");
        String idVehiculo = scanner.nextLine().toUpperCase();
        
        Vehiculo vehiculoSeleccionado = null;
        for (Vehiculo v : disponibles) {
            if (v.getId().equals(idVehiculo)) {
                vehiculoSeleccionado = v;
                break;
            }
        }
        
        if (vehiculoSeleccionado == null) {
            System.out.println("❌ ID de vehículo no válido");
            return;
        }

        // Calcular costo total
        int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
        int costoTotal = vehiculoSeleccionado.getCostePorKm() * distancia;
        
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
        String tipoTransporte = pedido.getTransporteAsignado().split(" ")[0];
        String[] incidentesTerrestres = {
            "Caída de árbol en la carretera",
            "Accidente de tráfico",
            "Obras en la vía",
            "Protesta de agricultores",
            "Control policial",
            "Avería mecánica",
            "Desprendimiento de rocas",
            "Nieve en la carretera",
            "Niebla densa"
        };

        String[] incidentesAereos = {
            "Turbulencias severas",
            "Retraso en el despegue",
            "Problemas técnicos en el avión",
            "Mal tiempo en el aeropuerto",
            "Huelga de controladores",
            "Restricciones de espacio aéreo",
            "Problemas de navegación",
            "Viento fuerte en pista"
        };

        String[] incidentesMaritimos = {
            "Tormenta en el mar",
            "Niebla en la costa",
            "Problemas en el puerto",
            "Avería en el motor",
            "Oleaje fuerte",
            "Retraso en la descarga",
            "Problemas de navegación",
            "Control de aduanas"
        };

        String incidente;
        int idIncidente = 100 + random.nextInt(900);

        // Seleccionar incidente según el tipo de transporte
        switch (tipoTransporte) {
            case "Furgoneta":
            case "Camión":
                incidente = incidentesTerrestres[random.nextInt(incidentesTerrestres.length)];
                break;
            case "Avión":
                incidente = incidentesAereos[random.nextInt(incidentesAereos.length)];
                break;
            case "Barco":
                incidente = incidentesMaritimos[random.nextInt(incidentesMaritimos.length)];
                break;
            default:
                incidente = "Incidente desconocido";
        }

        System.out.println("\n❗ ALERTA: Incidente #" + idIncidente + " - " + incidente);
        System.out.println("   - Riesgo: Retraso en entrega");
        System.out.println("   - Soluciones posibles:");
        System.out.println("     1) Esperar (50% de retraso)");
        System.out.println("     2) Desviar ruta (Coste adicional: $1,000, Entrega: +1 día)");
        
        System.out.print("\nSeleccione solución (1-2): ");
        String solucion = scanner.nextLine();

        System.out.println("\n🛠 Aplicando patrón *Template Method*:");
        System.out.println("   1. Identificando causa: " + incidente);
        System.out.println("   2. Asignando recursos...");
        
        if (solucion.equals("2")) {
            System.out.println("   3. Desviando ruta...");
            System.out.println("✅ Resuelto: Envío llegará con 1 día de retraso.");
            jugador.recibirDanio(1000);
            pedido.setDiasRestantes(pedido.getDiasRestantes() + 1);
        } else {
            System.out.println("   3. Esperando condiciones...");
            System.out.println("✅ Resuelto: Envío llegará con 2 días de retraso.");
            pedido.setDiasRestantes(pedido.getDiasRestantes() + 2);
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
            if (random.nextDouble() < 0.15) { // 15% de probabilidad de accidente
                String tipoTransporte = pedido.getTransporteAsignado().split(" ")[0];
                String incidente;
                int costeAdicional = 0;

                switch (tipoTransporte) {
                    case "Furgoneta":
                    case "Camión":
                        incidente = "Accidente en carretera";
                        costeAdicional = 2000;
                        break;
                    case "Avión":
                        incidente = "Turbulencias severas";
                        costeAdicional = 5000;
                        break;
                    case "Barco":
                        incidente = "Tormenta en el mar";
                        costeAdicional = 3000;
                        break;
                    default:
                        incidente = "Incidente desconocido";
                        costeAdicional = 1000;
                }

                System.out.println("\n⚠️ ¡INCIDENTE! El paquete #" + pedido.getId() + " ha sufrido " + incidente);
                System.out.println("   - Cliente: " + pedido.getCliente());
                System.out.println("   - Carga: " + pedido.getCarga());
                System.out.println("   - Coste adicional: $" + costeAdicional);
                
                jugador.recibirDanio(costeAdicional);
                pedido.setDiasRestantes(pedido.getDiasRestantes() + 1);
                satisfaccionClientes -= 5;
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