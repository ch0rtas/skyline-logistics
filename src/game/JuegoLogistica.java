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
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

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
    private String ciudad;
    private String dificultad;
    private String modoJuego;
    private int satisfaccionClientes;
    private int enviosExitosos;
    private int enviosTotales;
    private int beneficiosAcumulados;
    private int gastosAcumulados = 0;
    private int[] beneficiosPorDia;
    private String fechaInicio;
    private static final double TASA_IMPUESTOS = 0.45;
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");
    private static final String[] CIUDADES = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza",
        "Málaga", "Murcia", "Palma de Mallorca", "Las Palmas", "Bilbao",
        "Alicante", "Córdoba", "Valladolid", "Vigo", "Gijón"
    };

    // Ciudades que son islas
    private static final String[] ISLAS = {
        "Palma de Mallorca", "Las Palmas"
    };

    // Ciudades con acceso marítimo (puertos)
    private static final String[] CIUDADES_MARITIMAS = {
        "Barcelona", "Valencia", "Málaga", "Bilbao", "Alicante", "Vigo", "Gijón",
        "Palma de Mallorca", "Las Palmas"
    };

    // Matriz de distancias entre ciudades (en km)
    private static final int[][] DISTANCIAS = {
        // Madrid, Barcelona, Valencia, Sevilla, Zaragoza, Málaga, Murcia, Palma, Las Palmas, Bilbao, Alicante, Córdoba, Valladolid, Vigo, Gijón
        {0, 621, 352, 538, 325, 530, 400, 800, 2100, 395, 420, 400, 193, 599, 450}, // Madrid
        {621, 0, 349, 1000, 296, 1000, 600, 200, 2200, 610, 500, 900, 800, 1000, 800}, // Barcelona
        {352, 349, 0, 650, 300, 600, 250, 300, 2000, 600, 166, 500, 500, 800, 700}, // Valencia
        {538, 1000, 650, 0, 800, 200, 400, 600, 1900, 800, 500, 140, 600, 900, 800}, // Sevilla
        {325, 296, 300, 800, 0, 700, 500, 500, 2100, 300, 400, 600, 300, 700, 600}, // Zaragoza
        {530, 1000, 600, 200, 700, 0, 300, 400, 1900, 800, 400, 200, 700, 1000, 900}, // Málaga
        {400, 600, 250, 400, 500, 300, 0, 300, 2000, 700, 100, 300, 600, 900, 800}, // Murcia
        {800, 200, 300, 600, 500, 400, 300, 0, 2100, 800, 300, 600, 800, 1000, 900}, // Palma de Mallorca
        {2100, 2200, 2000, 1900, 2100, 1900, 2000, 2100, 0, 2100, 2000, 1900, 2100, 2200, 2100}, // Las Palmas
        {395, 610, 600, 800, 300, 800, 700, 800, 2100, 0, 600, 700, 280, 400, 300}, // Bilbao
        {420, 500, 166, 500, 400, 400, 100, 300, 2000, 600, 0, 400, 500, 800, 700}, // Alicante
        {400, 900, 500, 140, 600, 200, 300, 600, 1900, 700, 400, 0, 500, 800, 700}, // Córdoba
        {193, 800, 500, 600, 300, 700, 600, 800, 2100, 280, 500, 500, 0, 400, 300}, // Valladolid
        {599, 1000, 800, 900, 700, 1000, 900, 1000, 2200, 400, 800, 800, 400, 0, 200}, // Vigo
        {450, 800, 700, 800, 600, 900, 800, 900, 2100, 300, 700, 700, 300, 200, 0}  // Gijón
    };

    private List<Vehiculo> vehiculosMercado;
    private static final String[] TIPOS_CARGA = {"NORMAL", "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL", "PERECEDERO", "ALTO_VALOR", "SERES_VIVOS"};

    private static final Map<String, Map<String, Integer>> OBJETIVOS_CAMPANA = new HashMap<>();
    
    static {
        // Objetivos nivel fácil
        Map<String, Integer> objetivosFacil = new HashMap<>();
        objetivosFacil.put("dias", 30);
        objetivosFacil.put("enviosExitosos", 100);
        objetivosFacil.put("satisfaccion", 80);
        objetivosFacil.put("beneficios", 100000);
        OBJETIVOS_CAMPANA.put("facil", objetivosFacil);
        
        // Objetivos nivel medio
        Map<String, Integer> objetivosMedio = new HashMap<>();
        objetivosMedio.put("dias", 60);
        objetivosMedio.put("enviosExitosos", 350);
        objetivosMedio.put("satisfaccion", 90);
        objetivosMedio.put("beneficios", 250000);
        OBJETIVOS_CAMPANA.put("medio", objetivosMedio);
        
        // Objetivos nivel difícil
        Map<String, Integer> objetivosDificil = new HashMap<>();
        objetivosDificil.put("dias", 100);
        objetivosDificil.put("enviosExitosos", 920);
        objetivosDificil.put("satisfaccion", 95);
        objetivosDificil.put("beneficios", 500000);
        OBJETIVOS_CAMPANA.put("dificil", objetivosDificil);
    }

    /**
     * Constructor del juego
     * @param ciudad Ciudad seleccionada como almacén principal
     * @param dificultad Nivel de dificultad
     * @param nombreJugador Nombre del jugador
     * @param modoJuego Modo de juego seleccionado
     */
    public JuegoLogistica(String ciudad, String dificultad, String nombreJugador, String modoJuego) {
        this.ciudad = ciudad;
        this.almacenPrincipal = normalizarNombreCiudad(ciudad);
        this.dificultad = dificultad.toLowerCase();
        this.modoJuego = modoJuego.toLowerCase();
        this.jugador = new Jugador(nombreJugador, calcularBalanceInicial());
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.pedidos = new HashMap<>();
        this.pedidosPendientes = new ArrayList<>();
        this.pedidosEnCurso = new ArrayList<>();
        this.diaActual = 1;
        this.fechaActual = Calendar.getInstance();
        this.satisfaccionClientes = 100; // Inicialización por defecto
        inicializarSatisfaccionClientes();
        this.enviosExitosos = 0;
        this.enviosTotales = 0;
        this.beneficiosAcumulados = 0;
        this.beneficiosPorDia = new int[365]; // Máximo 365 días
        
        // Guardar fecha y hora de inicio
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.fechaInicio = formatoFechaHora.format(new Date());
    }

    /**
     * Calcula el balance inicial según la dificultad y modo de juego
     * @return int con el balance inicial
     */
    private int calcularBalanceInicial() {
        if (modoJuego.equals("libre")) {
            return 999999;
        }
        
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
        int base = 1000; // Reducir la base de la multa
        int multa = base;

        // Aumentar según la dificultad
        switch (dificultad) {
            case "easy":
                multa *= 1;
                break;
            case "medium":
                multa *= 1.2; // Ajustar multiplicador
                break;
            case "hard":
                multa *= 1.5; // Ajustar multiplicador
                break;
        }

        // Aumentar según el día actual
        multa *= (1 + (diaActual * 0.05)); // Reducir el incremento por día

        // Aumentar según la prioridad
        if (pedido.getPrioridad().equals("URGENTE")) {
            multa *= 1.5; // Ajustar multiplicador
        } else if (pedido.getPrioridad().equals("NORMAL")) {
            multa *= 1.2; // Ajustar multiplicador
        }

        return (int) multa;
    }

    /**
     * Aplica impuestos al balance del jugador según la dificultad
     */
    private void aplicarImpuestos() {
        int diasImpuestos = calcularDiasImpuestos();
        if (diaActual % diasImpuestos == 0) {
            int impuestos = (int) (jugador.getBalance() * TASA_IMPUESTOS);
            jugador.gastar(impuestos);
            System.out.println("\n💰 Se han aplicado impuestos del " + (TASA_IMPUESTOS * 100) + "%: -" + impuestos + "€");
        }
    }

    /**
     * Calcula los días entre pagos de impuestos según la dificultad
     * @return int con los días entre pagos
     */
    private int calcularDiasImpuestos() {
        switch (dificultad.toLowerCase()) {
            case "hard":
                return 2;
            case "medium":
                return 4;
            case "easy":
                return 6;
            default:
                return 6; // Por defecto, se asume la dificultad más fácil
        }
    }

    /**
     * Inicia el juego
     */
    public void iniciar() {
        mostrarBienvenida();
        inicializarFlota();
        generarVehiculosMercado();
        generarPedidosDia();
        
        while (!jugadorDerrotado()) {
            mostrarMenuPartida();
            procesarOpcion(scanner.nextLine());
        }
        
        mostrarGameOver();
    }

    /**
     * Muestra la pantalla de bienvenida
     */
    private void mostrarBienvenida() {
        System.out.println("\n✅ Sistema iniciado en región: " + almacenPrincipal);
        System.out.println("💰 Balance inicial: $" + jugador.getBalance());
    }

    /**
     * Inicializa la flota de vehículos según la dificultad
     */
    private void inicializarFlota() {
        flota = new ArrayList<>();
        Random random = new Random();
        
        // Función auxiliar para generar tipos de carga aleatorios
        Function<Integer, String[]> generarTiposCarga = (numTipos) -> {
            List<String> tiposDisponibles = new ArrayList<>(Arrays.asList(TIPOS_CARGA));
            List<String> tiposSeleccionados = new ArrayList<>();
            tiposSeleccionados.add("NORMAL"); // Todos los vehículos pueden transportar carga normal
            
            for (int i = 1; i < numTipos; i++) {
                if (tiposDisponibles.isEmpty()) break;
                int index = random.nextInt(tiposDisponibles.size());
                tiposSeleccionados.add(tiposDisponibles.remove(index));
            }
            
            return tiposSeleccionados.toArray(new String[0]);
        };
        
        switch (dificultad) {
            case "easy":
                // 3 furgonetas, 2 camiones, 2 barcos, 2 aviones
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Barco", "B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Barco", "B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Avión", "A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Avión", "A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
            case "medium":
                // 2 furgonetas, 2 camiones, 1 barco, 1 avión
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Barco", "B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Avión", "A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
            case "hard":
                // 1 furgoneta, 1 camión, 1 barco, 1 avión
                flota.add(new Vehiculo("Furgoneta", "F" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Camión", "C" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Barco", "B" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                flota.add(new Vehiculo("Avión", "A" + String.format("%02d", random.nextInt(100)), generarTiposCarga.apply(3)));
                break;
        }

        System.out.println("\n🛠 Empresa creada:");
        System.out.println("   - Almacén: " + almacenPrincipal);
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
     * Muestra la flota de vehículos
     */
    private void mostrarFlota() {
        System.out.println("\n🚗 FLOTA DE VEHÍCULOS:");
        
        // Calcular anchos máximos para cada columna
        String[] encabezados = {"TIPO", "ID", "CAPACIDAD", "VELOCIDAD", "COSTE/KM", "ESTADO", "SALUD", "CARGAS PERMITIDAS"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Vehiculo vehiculo : flota) {
            String estado;
            if (vehiculo.getPedidoAsignado() != null) {
                Calendar fechaEntrega = Calendar.getInstance();
                fechaEntrega.setTime(fechaActual.getTime());
                fechaEntrega.add(Calendar.DAY_OF_MONTH, vehiculo.getPedidoAsignado().getDiasRestantes());
                estado = "Ocupado (" + vehiculo.getPedidoAsignado().getId() + ") hasta " + formatoFecha.format(fechaEntrega.getTime());
            } else {
                estado = "Disponible";
            }
            
            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                estado,
                vehiculo.getSalud() + "%",
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            
            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Vehiculo vehiculo : flota) {
            String estado;
            if (vehiculo.getPedidoAsignado() != null) {
                Calendar fechaEntrega = Calendar.getInstance();
                fechaEntrega.setTime(fechaActual.getTime());
                fechaEntrega.add(Calendar.DAY_OF_MONTH, vehiculo.getPedidoAsignado().getDiasRestantes());
                estado = "Ocupado (" + vehiculo.getPedidoAsignado().getId() + ") hasta " + formatoFecha.format(fechaEntrega.getTime());
            } else {
                estado = "Disponible";
            }
            
            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                estado,
                vehiculo.getSalud() + "%",
                String.join(", ", vehiculo.getTiposPaquetesPermitidos())
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }

        System.out.println("\n01. Volver al menú partida");
        System.out.println("02. Reparar vehículo");
        System.out.println("03. Ver mercado de vehículos");
        System.out.print("\nSeleccione una opción: ");
        String opcion = scanner.nextLine();

        switch (opcion) {
            case "01":
            case "1":
                return;
            case "02":
            case "2":
                repararVehiculo();
                break;
            case "03":
            case "3":
                mostrarMercadoVehiculos();
                break;
            default:
                System.out.println("\n❌ Opción no válida");
                mostrarFlota();
        }
    }

    /**
     * Muestra el menú de reparación de vehículos
     */
    private void repararVehiculo() {
        // Filtrar vehículos con menos del 100% de salud y que estén disponibles
        List<Vehiculo> vehiculosReparables = flota.stream()
            .filter(v -> v.getSalud() < 100 && v.getPedidoAsignado() == null)
            .collect(Collectors.toList());

        if (vehiculosReparables.isEmpty()) {
            System.out.println("\n❌ No tienes vehículos disponibles para reparar");
            return; // Evitar mostrar el menú partida dos veces
        }

        System.out.println("\n=== 🔧 REPARACIÓN DE VEHÍCULOS 🔧 ===");
        System.out.println("Balance actual: " + jugador.getBalance() + "€");

        for (int i = 0; i < vehiculosReparables.size(); i++) {
            Vehiculo v = vehiculosReparables.get(i);
            System.out.printf("\n%02d. %s\n", i + 1, v.getNombre());
            System.out.println("   Salud: " + v.getSalud() + "%");
            System.out.println("   Coste de reparación: " + v.getCosteReparacion() + "€");
        }

        System.out.println("\n0. Volver al menú principal");
        System.out.print("\nSeleccione un vehículo para reparar (0 para volver): ");
        String opcion = scanner.nextLine();

        if (opcion.equals("0")) {
            mostrarMenuPartida();
            return;
        }

        try {
            int indice = Integer.parseInt(opcion) - 1;
            if (indice >= 0 && indice < vehiculosReparables.size()) {
                Vehiculo vehiculoSeleccionado = vehiculosReparables.get(indice);
                if (modoJuego.equals("libre") || jugador.getBalance() >= vehiculoSeleccionado.getCosteReparacion()) {
                    if (!modoJuego.equals("libre")) {
                        jugador.gastar(vehiculoSeleccionado.getCosteReparacion());
                    }
                    vehiculoSeleccionado.reparar();
                    System.out.println("\n✅ Has reparado el " + vehiculoSeleccionado.getNombre());
                } else {
                    System.out.println("\n❌ No tienes suficiente dinero para reparar este vehículo");
                }
            } else {
                System.out.println("\n❌ Opción no válida");
            }
        } catch (NumberFormatException e) {
            System.out.println("\n❌ Por favor, introduce un número válido");
        }

        repararVehiculo();
    }

    /**
     * Muestra el mercado de vehículos
     */
    private void mostrarMercadoVehiculos() {
        while (true) {
            System.out.println("\n=== 🚗 MERCADO DE VEHÍCULOS 🚗 ===");
            System.out.println("Balance actual: " + jugador.getBalance() + "€\n");

            for (int i = 0; i < vehiculosMercado.size(); i++) {
                Vehiculo vehiculo = vehiculosMercado.get(i);
                String indice = String.format("%02d", i + 1);
                System.out.println(indice + ". " + vehiculo.getTipo() + " " + vehiculo.getId());
                System.out.println("   Capacidad: " + vehiculo.getCapacidad() + " kg");
                System.out.println("   Velocidad: " + vehiculo.getVelocidad() + " km/h");
                System.out.println("   Consumo: " + vehiculo.getConsumo() + " L/100km");
                System.out.println("   Precio: " + vehiculo.getPrecio() + "€\n");
            }

            System.out.println("0. Volver al menú principal\n");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("0") || opcion.equals("00")) {
                break; // Salir del mercado de vehículos
            }

            try {
                int indice = Integer.parseInt(opcion);
                if (indice < 1 || indice > vehiculosMercado.size()) {
                    System.out.println("❌ Opción no válida");
                    continue;
                }

                Vehiculo vehiculoSeleccionado = vehiculosMercado.get(indice - 1);
                if (jugador.getBalance() < vehiculoSeleccionado.getPrecio()) {
                    System.out.println("❌ No tienes suficiente balance para comprar este vehículo");
                    continue;
                }

                jugador.gastar(vehiculoSeleccionado.getPrecio());
                flota.add(vehiculoSeleccionado);
                vehiculosMercado.remove(vehiculoSeleccionado);

                System.out.println("✅ Has comprado el vehículo " + vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());
            } catch (NumberFormatException e) {
                System.out.println("❌ Opción no válida");
            }
        }
    }

    /**
     * Muestra el menú principal
     */
    private void mostrarMenuPartida() {
        System.out.println("\n==================================================");
        System.out.println("📅 DÍA " + diaActual + " (" + formatoFecha.format(fechaActual.getTime()) + ") | ALMACÉN PRINCIPAL: " + almacenPrincipal);
        System.out.println("==================================================");
        System.out.println("\n01. Ver pedidos pendientes");
        System.out.println("02. Ver pedidos en curso");
        System.out.println("03. Gestionar pedidos");
        System.out.println("04. Ver flota");
        System.out.println("05. Ver estadísticas");
        System.out.println("06. Pasar al siguiente día");
        System.out.println("99. Finalizar partida");
        System.out.print("\nSeleccione una opción: ");
    }

    /**
     * Procesa la opción seleccionada
     * @param opcion Opción elegida por el usuario
     */
    private void procesarOpcion(String opcion) {
        switch (opcion) {
            case "01":
            case "1":
                mostrarPedidosPendientes();
                break;
            case "02":
            case "2":
                mostrarPedidosEnCurso();
                break;
            case "03":
            case "3":
                gestionarPedido();
                break;
            case "04":
            case "4":
                mostrarFlota();
                break;
            case "05":
            case "5":
                mostrarEstadisticas();
                break;
            case "06":
            case "6":
                pasarDia();
                break;
            case "99":
                mostrarEstadisticas();
                guardarEstadisticas();
                System.exit(0);
                break;
            default:
                System.out.println("\n❌ Opción no válida");
                mostrarMenuPartida();
                procesarOpcion(scanner.nextLine());
        }
    }

    /**
     * Guarda las estadísticas del jugador en el archivo de histórico
     */
    private void guardarEstadisticas() {
        try {
            FileWriter fw = new FileWriter("historico_jugadores.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            
            // Calcular beneficios acumulados
            int beneficiosAcumulados = 0;
            for (int i = 0; i < diaActual; i++) {
                beneficiosAcumulados += beneficiosPorDia[i];
            }
            
            // Formato: modoJuego|nombreJugador|dias|dinero|enviosExitosos|satisfaccion|beneficios|fechaInicio|fechaFin|dificultad|ciudad
            String linea = String.format("%s|%s|%d|%d|%d|%d|%d|%s|%s|%s|%s",
                modoJuego,
                jugador.getNombre(),
                diaActual,
                jugador.getBalance(),
                enviosExitosos,
                satisfaccionClientes,
                beneficiosAcumulados,
                fechaInicio,
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                dificultad,
                ciudad);
            
            bw.write(linea);
            bw.newLine();
            bw.close();
            
        } catch (IOException e) {
            System.out.println("❌ Error al guardar estadísticas: " + e.getMessage());
        }
    }

    private void mostrarHistoricoJugadores() {
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
                if (datos.length == 10) {
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

                    JugadorHistorico jugador = new JugadorHistorico(nombre, dias, dinero, envios, satisfaccion, beneficios, fechaInicio, fechaFin, dificultad);
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

        public JugadorHistorico(String nombre, int dias, int dinero, int envios, int satisfaccion, int beneficios, String fechaInicio, String fechaFin, String dificultad) {
            this.nombre = nombre;
            this.dias = dias;
            this.dinero = dinero;
            this.envios = envios;
            this.satisfaccion = satisfaccion;
            this.beneficios = beneficios;
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
            this.dificultad = dificultad;
        }
    }

    private int obtenerPesoDificultad(String dificultad) {
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

    private String obtenerEmojiDificultad(String dificultad) {
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

    /**
     * Genera un pedido aleatorio
     * @return Pedido generado
     */
    private Pedido generarPedidoAleatorio() {
        String[] clientes = {
            "Hospital Regional", 
            "Farmacia Central", 
            "Laboratorio Médico",
            "Supermercado Local",
            "Restaurante VIP",
            "Tienda Electrónica",
            "Floristería Central",
            "Joyería Premium",
            "Galería de Arte",
            "Constructora",
            "Fábrica Industrial",
            "Aeropuerto Local",
            "Puerto Marítimo",
            "Centro Científico",
            "Universidad"
        };
        
        // Cargas específicas por tipo de paquete
        Map<String, String[]> cargasPorTipo = new HashMap<>();
        cargasPorTipo.put("NORMAL", new String[]{"Materiales Construcción", "Piezas Industriales", "Equipaje VIP", "Material Educativo"});
        cargasPorTipo.put("REFRIGERADO", new String[]{"Vacunas", "Medicamentos", "Alimentos Frescos", "Mariscos Frescos"});
        cargasPorTipo.put("CONGELADO", new String[]{"Productos Congelados", "Muestras Biológicas", "Alimentos Ultra Congelados"});
        cargasPorTipo.put("PELIGROSO", new String[]{"Productos Químicos", "Materiales Explosivos", "Residuos Tóxicos", "Combustibles"});
        cargasPorTipo.put("ESCOLTADO", new String[]{"Joyas Valiosas", "Obras de Arte", "Dinero", "Documentos Secretos"});
        cargasPorTipo.put("FRÁGIL", new String[]{"Equipos Médicos", "Electrónicos", "Flores Exóticas", "Instrumentos Musicales", "Antigüedades"});
        cargasPorTipo.put("PERECEDERO", new String[]{"Frutas Frescas", "Verduras", "Lácteos", "Carnes"});
        cargasPorTipo.put("ALTO_VALOR", new String[]{"Obras de Arte", "Metales Preciosos", "Electrónica de Alta Gama", "Documentos Confidenciales"});
        cargasPorTipo.put("SERES_VIVOS", new String[]{"Animales Domésticos", "Ganado", "Aves", "Peces"});
        
        String[] prioridades = {"URGENTE", "NORMAL", "BAJA"};
        String[] tiposPaquetes = {"NORMAL", "REFRIGERADO", "CONGELADO", "ESCOLTADO", "PELIGROSO", "FRÁGIL", "PERECEDERO", "ALTO_VALOR", "SERES_VIVOS"};

        // Seleccionar tipo de paquete
        String tipoPaquete = tiposPaquetes[random.nextInt(tiposPaquetes.length)];
        
        // Seleccionar carga según el tipo
        String[] cargasDisponibles = cargasPorTipo.get(tipoPaquete);
        String carga = cargasDisponibles[random.nextInt(cargasDisponibles.length)];
        
        // Seleccionar cliente
        String cliente = clientes[random.nextInt(clientes.length)];
        
        // Seleccionar prioridad
        String prioridad = prioridades[random.nextInt(prioridades.length)];
        
        // Generar ID y peso según el tipo de paquete
        String idPedido = "P" + (100 + random.nextInt(900));
        int peso;
        
        // Ajustar peso según el tipo de paquete
        switch (tipoPaquete) {
            case "NORMAL":
                peso = 1000 + random.nextInt(4000);
                break;
            case "REFRIGERADO":
                peso = 500 + random.nextInt(2000);
                break;
            case "CONGELADO":
                peso = 1000 + random.nextInt(3000);
                break;
            case "PELIGROSO":
                peso = 500 + random.nextInt(1500);
                break;
            case "ESCOLTADO":
                peso = 100 + random.nextInt(900);
                break;
            case "FRÁGIL":
                peso = 100 + random.nextInt(500);
                break;
            case "PERECEDERO":
                peso = 500 + random.nextInt(2000);
                break;
            case "ALTO_VALOR":
                peso = 100 + random.nextInt(400);
                break;
            case "SERES_VIVOS":
                peso = 100 + random.nextInt(900);
                break;
            default:
                peso = 1000 + random.nextInt(2000);
        }
        
        // Seleccionar origen y destino
        String origen = CIUDADES[random.nextInt(CIUDADES.length)];
        String destino;
        do {
            destino = CIUDADES[random.nextInt(CIUDADES.length)];
        } while (destino.equals(origen));
        
        // Calcular el coste mínimo basado en el vehículo más barato disponible
        int costeMinimo = Integer.MAX_VALUE;
        for (Vehiculo v : flota) {
            if (v.estaDisponible() && v.puedeTransportarTipo(tipoPaquete)) {
                int distancia = obtenerDistancia(origen, destino);
                int costeViaje = distancia * v.getCostePorKm();
                costeMinimo = Math.min(costeMinimo, costeViaje);
            }
        }
        
        // Si no hay vehículos disponibles, usar un coste base
        if (costeMinimo == Integer.MAX_VALUE) {
            costeMinimo = 1000;
        }
        
        // El pago base será al menos un 20% más que el coste mínimo
        int pagoBase = (int)(costeMinimo * 1.2);
        
        // Añadir un bonus aleatorio entre 0% y 50%
        pagoBase += (int)(pagoBase * random.nextDouble() * 0.5);
        
        // Ajustar pago según prioridad
        if (prioridad.equals("URGENTE")) {
            pagoBase *= 1.5;
        } else if (prioridad.equals("BAJA")) {
            pagoBase *= 0.8;
        }
        
        Calendar fechaEntrega = (Calendar) fechaActual.clone();
        int diasBase;
        
        // Días base según tipo de paquete
        switch (tipoPaquete) {
            case "NORMAL":
                diasBase = 3;
                break;
            case "REFRIGERADO":
                diasBase = 2;
                break;
            case "CONGELADO":
                diasBase = 4;
                break;
            case "PELIGROSO":
                diasBase = 5;
                break;
            case "ESCOLTADO":
                diasBase = 2;
                break;
            case "FRÁGIL":
                diasBase = 2;
                break;
            case "PERECEDERO":
                diasBase = 1;
                break;
            case "ALTO_VALOR":
                diasBase = 3;
                break;
            case "SERES_VIVOS":
                diasBase = 2;
                break;
            default:
                diasBase = 3;
        }
        
        // Ajustar días según prioridad
        if (prioridad.equals("URGENTE")) {
            diasBase = Math.max(1, diasBase - 2);
        } else if (prioridad.equals("BAJA")) {
            diasBase += 2;
        }
        
        fechaEntrega.add(Calendar.DAY_OF_MONTH, diasBase);

        // Determinar si es un pedido de varios días según la dificultad
        int diasEntrega = 1;
        if (random.nextDouble() < calcularProbabilidadMultiDia()) {
            diasEntrega = 2 + random.nextInt(3);
            pagoBase *= diasEntrega;
        }

        return new Pedido(idPedido, cliente, carga, prioridad, pagoBase, diasEntrega, destino, fechaEntrega, peso, tipoPaquete);
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
        
        System.out.println("\n📦 Han entrado " + cantidadPedidos + " paquetes nuevos!");
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
     * Calcula el ancho máximo necesario para una columna basado en su contenido
     * @param contenido Array de strings con el contenido de la columna
     * @param encabezado String con el encabezado de la columna
     * @return int con el ancho máximo necesario
     */
    private int calcularAnchoColumna(String[] contenido, String encabezado) {
        int anchoMaximo = encabezado.length();
        for (String item : contenido) {
            anchoMaximo = Math.max(anchoMaximo, item.length());
        }
        return anchoMaximo;
    }

    /**
     * Genera una línea separadora para una tabla
     * @param anchos Array con los anchos de cada columna
     * @return String con la línea separadora
     */
    private String generarLineaSeparadora(int[] anchos) {
        StringBuilder linea = new StringBuilder();
        for (int ancho : anchos) {
            linea.append("-".repeat(ancho + 2)).append("+");
        }
        return linea.toString();
    }

    /**
     * Genera una fila de tabla con el formato correcto
     * @param valores Array con los valores de cada columna
     * @param anchos Array con los anchos de cada columna
     * @return String con la fila formateada
     */
    private String generarFilaTabla(String[] valores, int[] anchos) {
        StringBuilder fila = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            fila.append(String.format(" %-" + anchos[i] + "s |", valores[i]));
        }
        return fila.toString();
    }

    /**
     * Muestra los pedidos pendientes
     */
    private void mostrarPedidosPendientes() {
        if (pedidosPendientes.isEmpty()) {
            System.out.println("\n📭 No hay pedidos pendientes");
            return;
        }

        // Calcular anchos máximos para cada columna
        String[] encabezados = {"ID", "CLIENTE", "CARGA", "PRIORIDAD", "PESO", "DESTINO", "TIPO", "PAGO", "ENTREGA"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Pedido pedido : pedidosPendientes) {
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega()
            };
            
            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println("\n📦 PEDIDOS PENDIENTES:");
        
        // Mostrar encabezados
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Pedido pedido : pedidosPendientes) {
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega()
            };
            System.out.println(generarFilaTabla(valores, anchos));
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

        // Calcular anchos máximos para cada columna
        String[] encabezados = {"ID", "CLIENTE", "CARGA", "PRIORIDAD", "PESO", "DESTINO", "TIPO", "PAGO", "ENTREGA MÁXIMA", "ENTREGA PREVISTA"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Pedido pedido : pedidosEnCurso) {
            Calendar fechaPrevia = Calendar.getInstance();
            fechaPrevia.setTime(fechaActual.getTime());
            fechaPrevia.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());
            
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega(),
                formatoFecha.format(fechaPrevia.getTime())
            };
            
            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println("\n📦 PEDIDOS EN CURSO:");
        
        // Mostrar encabezados
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Pedido pedido : pedidosEnCurso) {
            Calendar fechaPrevia = Calendar.getInstance();
            fechaPrevia.setTime(fechaActual.getTime());
            fechaPrevia.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());
            
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega(),
                formatoFecha.format(fechaPrevia.getTime())
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }
    }

    /**
     * Normaliza el nombre de una ciudad
     * @param ciudad Nombre de la ciudad a normalizar
     * @return String con el nombre normalizado
     */
    private String normalizarNombreCiudad(String ciudad) {
        String nombre = ciudad.replace("_", " ");
        String[] palabras = nombre.split(" ");
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < palabras.length; i++) {
            if (palabras[i].length() > 0) {
                resultado.append(Character.toUpperCase(palabras[i].charAt(0)));
                resultado.append(palabras[i].substring(1).toLowerCase());
                if (i < palabras.length - 1) {
                    resultado.append(" ");
                }
            }
        }
        return resultado.toString();
    }

    /**
     * Obtiene la distancia entre dos ciudades
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return int con la distancia en km
     */
    private int obtenerDistancia(String origen, String destino) {
        int indiceOrigen = Arrays.asList(CIUDADES).indexOf(origen);
        int indiceDestino = Arrays.asList(CIUDADES).indexOf(destino);
        return DISTANCIAS[indiceOrigen][indiceDestino];
    }

    /**
     * Verifica si una ciudad es una isla
     * @param ciudad Nombre de la ciudad
     * @return true si es una isla, false si no
     */
    private boolean esIsla(String ciudad) {
        return Arrays.asList(ISLAS).contains(ciudad);
    }

    /**
     * Verifica si una ruta es marítima entre dos ciudades
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return true si la ruta es marítima, false si no
     */
    private boolean esRutaMaritima(String origen, String destino) {
        return Arrays.asList(CIUDADES_MARITIMAS).contains(origen) && Arrays.asList(CIUDADES_MARITIMAS).contains(destino);
    }

    /**
     * Calcula el coste de envío para un vehículo
     * @param vehiculo Vehículo que realizará el envío
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return int con el coste total
     */
    private int calcularCosteEnvio(Vehiculo vehiculo, String origen, String destino) {
        int distancia = obtenerDistancia(origen, destino);
        int costeBase = vehiculo.getCostePorKm() * distancia;
        
        // Ajustes específicos para barcos
        if (vehiculo.getTipo().equals("Barco")) {
            // Coste adicional por ser ruta marítima
            costeBase *= 1.5; // 50% más caro que la ruta terrestre equivalente
            
            // Coste adicional por tipo de ruta marítima
            if (esIsla(origen) && esIsla(destino)) {
                costeBase *= 1.3; // 30% más caro entre islas
            } else if (esIsla(origen) || esIsla(destino)) {
                costeBase *= 1.2; // 20% más caro entre isla y costa
            }
        }
        
        // Ajustes específicos para aviones
        if (vehiculo.getTipo().equals("Avión")) {
            // Coste adicional por ser ruta aérea
            costeBase *= 2.0; // 100% más caro que la ruta terrestre equivalente
            
            // Coste adicional por tipo de ruta aérea
            if (esIsla(origen) && esIsla(destino)) {
                costeBase *= 1.5; // 50% más caro entre islas
            } else if (esIsla(origen) || esIsla(destino)) {
                costeBase *= 1.3; // 30% más caro entre isla y costa
            }
        }
        
        return costeBase;
    }

    /**
     * Verifica si existe una ruta terrestre válida entre dos ciudades
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return true si existe una ruta terrestre válida, false si no
     */
    private boolean existeRutaTerrestre(String origen, String destino) {
        // Normalizar nombres de ciudades
        String origenNormalizado = normalizarNombreCiudad(origen);
        String destinoNormalizado = normalizarNombreCiudad(destino);
        
        // Si alguna de las ciudades es una isla, no hay ruta terrestre
        if (esIsla(origenNormalizado) || esIsla(destinoNormalizado)) {
            return false;
        }
        
        // Obtener índices de las ciudades
        int indiceOrigen = -1;
        int indiceDestino = -1;
        
        for (int i = 0; i < CIUDADES.length; i++) {
            if (CIUDADES[i].equalsIgnoreCase(origenNormalizado)) {
                indiceOrigen = i;
            }
            if (CIUDADES[i].equalsIgnoreCase(destinoNormalizado)) {
                indiceDestino = i;
            }
        }
        
        if (indiceOrigen == -1 || indiceDestino == -1) {
            return false;
        }
        
        // Verificar si hay una distancia terrestre válida
        int distancia = DISTANCIAS[indiceOrigen][indiceDestino];
        return distancia > 0;
    }

    /**
     * Verifica si un vehículo puede realizar una ruta específica
     * @param vehiculo Vehículo a verificar
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @return true si el vehículo puede realizar la ruta, false si no
     */
    private boolean vehiculoPuedeRealizarRuta(Vehiculo vehiculo, String origen, String destino) {
        switch (vehiculo.getTipo()) {
            case "Furgoneta":
            case "Camión":
            case "Camion":
                return existeRutaTerrestre(origen, destino);
            case "Barco":
                return esRutaMaritima(origen, destino);
            case "Avión":
                return true; // Los aviones pueden ir a cualquier parte
            default:
                return false;
        }
    }

    /**
     * Muestra los vehículos disponibles para un pedido
     * @param pedido Pedido a transportar
     */
    private void mostrarVehiculosDisponibles(Pedido pedido) {
        System.out.println("\n🚗 VEHÍCULOS DISPONIBLES:");
        
        // Filtrar vehículos disponibles
        List<Vehiculo> vehiculosDisponibles = flota.stream()
            .filter(v -> v.estaDisponible() && v.puedeTransportarTipo(pedido.getTipoPaquete()) && v.getSalud() >= 10)
            .filter(v -> {
                // Si es un barco, verificar que tanto origen como destino sean marítimos
                if (v.getTipo().equals("Barco")) {
                    return Arrays.asList(CIUDADES_MARITIMAS).contains(almacenPrincipal) && 
                           Arrays.asList(CIUDADES_MARITIMAS).contains(pedido.getDestino());
                }
                return true;
            })
            .collect(Collectors.toList());
            
        if (vehiculosDisponibles.isEmpty()) {
            System.out.println("\n❌ No hay vehículos disponibles para este tipo de carga");
            pedidosPendientes.remove(pedido);
            return;
        }
        
        // Calcular anchos máximos para cada columna
        String[] encabezados = {"TIPO", "ID", "CAPACIDAD", "VELOCIDAD", "COSTE/KM", "SALUD", "DESGASTE", "CARGAS PERMITIDAS", "COSTE TOTAL", "FECHA ENTREGA"};
        int[] anchos = new int[encabezados.length];
        
        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }
        
        // Calcular anchos máximos basados en el contenido
        for (Vehiculo vehiculo : vehiculosDisponibles) {
            // Calcular tiempo de entrega basado en la velocidad y distancia
            int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
            
            // Calcular horas de viaje basadas en la velocidad real del vehículo
            double horasViaje = (double) distancia / vehiculo.getVelocidad();
            
            // Ajustar horas según el tipo de vehículo
            switch (vehiculo.getTipo()) {
                case "Furgoneta":
                    horasViaje *= 1.2; // 20% más lento por paradas y tráfico
                    break;
                case "Camión":
                    horasViaje *= 1.3; // 30% más lento por paradas y restricciones
                    break;
                case "Barco":
                    horasViaje *= 1.5; // 50% más lento por condiciones marítimas
                    break;
                case "Avión":
                    horasViaje *= 1.1; // 10% más lento por procedimientos aeroportuarios
                    break;
            }
            
            // Convertir horas a días (considerando 8 horas de trabajo por día)
            int diasViaje = (int) Math.ceil(horasViaje / 8.0);
            
            // Asegurar un mínimo de 1 día de viaje
            diasViaje = Math.max(1, diasViaje);
            
            Calendar fechaEntrega = (Calendar) fechaActual.clone();
            fechaEntrega.add(Calendar.DAY_OF_MONTH, diasViaje);

            // Calcular coste total del envío
            int costeTotal = calcularCosteEnvio(vehiculo, almacenPrincipal, pedido.getDestino());

            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                vehiculo.getSalud() + "%",
                vehiculo.getDesgastePorViaje() + "%",
                String.join(", ", vehiculo.getTiposPaquetesPermitidos()),
                "$" + costeTotal,
                formatoFecha.format(fechaEntrega.getTime())
            };

            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));
        
        // Mostrar datos
        for (Vehiculo vehiculo : vehiculosDisponibles) {
            // Calcular tiempo de entrega basado en la velocidad y distancia
            int distancia = obtenerDistancia(almacenPrincipal, pedido.getDestino());
            
            // Calcular horas de viaje basadas en la velocidad real del vehículo
            double horasViaje = (double) distancia / vehiculo.getVelocidad();
            
            // Ajustar horas según el tipo de vehículo
            switch (vehiculo.getTipo()) {
                case "Furgoneta":
                    horasViaje *= 1.2; // 20% más lento por paradas y tráfico
                    break;
                case "Camión":
                    horasViaje *= 1.3; // 30% más lento por paradas y restricciones
                    break;
                case "Barco":
                    horasViaje *= 1.5; // 50% más lento por condiciones marítimas
                    break;
                case "Avión":
                    horasViaje *= 1.1; // 10% más lento por procedimientos aeroportuarios
                    break;
            }
            
            // Convertir horas a días (considerando 8 horas de trabajo por día)
            int diasViaje = (int) Math.ceil(horasViaje / 8.0);
            
            // Asegurar un mínimo de 1 día de viaje
            diasViaje = Math.max(1, diasViaje);
            
            Calendar fechaEntrega = (Calendar) fechaActual.clone();
            fechaEntrega.add(Calendar.DAY_OF_MONTH, diasViaje);

            // Calcular coste total del envío
            int costeTotal = calcularCosteEnvio(vehiculo, almacenPrincipal, pedido.getDestino());

            String[] valores = {
                vehiculo.getTipo(),
                vehiculo.getId(),
                String.valueOf(vehiculo.getCapacidad()),
                String.valueOf(vehiculo.getVelocidad()),
                "$" + vehiculo.getCostePorKm(),
                vehiculo.getSalud() + "%",
                vehiculo.getDesgastePorViaje() + "%",
                String.join(", ", vehiculo.getTiposPaquetesPermitidos()),
                "$" + costeTotal,
                formatoFecha.format(fechaEntrega.getTime())
            };
            System.out.println(generarFilaTabla(valores, anchos));
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

        // Verificar si hay vehículos disponibles para esta ruta
        boolean hayVehiculosDisponibles = false;
        for (Vehiculo v : flota) {
            if (v.estaDisponible() && 
                v.puedeTransportarTipo(pedido.getTipoPaquete()) &&
                vehiculoPuedeRealizarRuta(v, almacenPrincipal, pedido.getDestino())) {
                hayVehiculosDisponibles = true;
                break;
            }
        }

        if (!hayVehiculosDisponibles) {
            System.out.println("\n❌ No hay vehículos disponibles para realizar esta ruta");
            System.out.println("   - Origen: " + almacenPrincipal);
            System.out.println("   - Destino: " + pedido.getDestino());
            System.out.println("   - Tipo de carga: " + pedido.getTipoPaquete());

            System.out.println("\n¿Qué desea hacer con el pedido #" + idPedido + "?");
            System.out.println("02. Rechazar (Multa: $" + calcularMultaRechazo(pedido) + ")");
            System.out.print("\nOpción: ");
            String opcion = scanner.nextLine();

            if (opcion.equals("02") || opcion.equals("2")) {
                String confirmacion;
                do {
                    System.out.print("¿Confirmar rechazo? (S/N): ");
                    confirmacion = scanner.nextLine().toUpperCase();
                } while (!confirmacion.equals("S") && !confirmacion.equals("N"));

                if (confirmacion.equals("S")) {
                    int multa = calcularMultaRechazo(pedido);
                    jugador.gastar(multa); // Restar la multa del balance del jugador
                    gastosAcumulados += multa; // Añadir la multa a los gastos acumulados
                    pedidosPendientes.remove(pedido);
                    System.out.println("❌ Pedido #" + idPedido + " rechazado");
                    System.out.println("💰 Multa aplicada: $" + multa);
                }
                return;
            } else {
                System.out.println("\n❌ Opción no válida");
                gestionarPedido();
                return;
            }
        }

        System.out.println("\n¿Qué desea hacer con el pedido #" + idPedido + "?");
        System.out.println("01. Enviar");
        System.out.println("02. Rechazar (Multa: $" + calcularMultaRechazo(pedido) + ")");
        System.out.print("\nOpción: ");
        String opcion = scanner.nextLine();

        if (opcion.equals("02") || opcion.equals("2")) {
            System.out.println("\n⚠️ ¿Está seguro de rechazar el pedido #" + idPedido + "?");
            System.out.println("   - Multa por rechazo: $" + calcularMultaRechazo(pedido));
            System.out.print("   - Confirmar (s/N): ");

            String confirmacion = scanner.nextLine().toUpperCase();
            if (confirmacion.equals("S")) {
                int multa = calcularMultaRechazo(pedido);
                jugador.gastar(multa); // Restar la multa del balance del jugador
                gastosAcumulados += multa; // Añadir la multa a los gastos acumulados
                pedidosPendientes.remove(pedido);
                System.out.println("❌ Pedido #" + idPedido + " rechazado");
                System.out.println("💰 Multa aplicada: $" + multa);
            }
            return;
        } else if (!opcion.equals("01") && !opcion.equals("1")) {
            System.out.println("\n❌ Opción no válida");
            gestionarPedido();
            return;
        }

        // Mostrar vehículos disponibles
        mostrarVehiculosDisponibles(pedido);
        if (pedidosPendientes.isEmpty()) {
            return;
        }

        System.out.print("\nIngrese ID del vehículo a utilizar: ");
        String idVehiculo = scanner.nextLine().toUpperCase();

        Vehiculo vehiculoSeleccionado = null;
        for (Vehiculo v : flota) {
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
        int costoTotal = calcularCosteEnvio(vehiculoSeleccionado, almacenPrincipal, pedido.getDestino());

        // Verificar balance
        if (jugador.getBalance() < costoTotal) {
            System.out.println("❌ Balance insuficiente para realizar el envío");
            return;
        }

        // Restar el costo del balance del jugador
        jugador.gastar(costoTotal);
        gastosAcumulados += costoTotal;

        // Asignar vehículo al pedido
        vehiculoSeleccionado.asignarPedido(pedido);
        pedido.setTransporteAsignado(vehiculoSeleccionado.getTipo() + " " + vehiculoSeleccionado.getId());

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
        String idVehiculo = pedido.getTransporteAsignado().split(" ")[1];
        
        // Encontrar el vehículo afectado
        Vehiculo vehiculoAfectado = flota.stream()
            .filter(v -> v.getId().equals(idVehiculo))
            .findFirst()
            .orElse(null);
            
        if (vehiculoAfectado == null) {
            return;
        }
        
        // Posibilidad de incidente adicional
        if (random.nextDouble() < 0.3) { // 30% de probabilidad de incidente
        String[] incidentesTerrestres = {
            "Caída de árbol en la carretera",
            "Accidente de tráfico",
            "Obras en la vía",
            "Protesta de agricultores",
            "Control policial",
            "Avería mecánica",
            "Desprendimiento de rocas",
            "Nieve en la carretera",
            "Niebla densa",
            "Pinchazo de neumático",
            "Fallo en el sistema de frenos",
            "Problemas con el motor",
            "Batería descargada",
            "Problemas con el sistema de refrigeración",
            "Fallo en el sistema eléctrico"
        };

        String[] incidentesAereos = {
            "Turbulencias severas",
            "Retraso en el despegue",
            "Problemas técnicos en el avión",
            "Mal tiempo en el aeropuerto",
            "Huelga de controladores",
            "Restricciones de espacio aéreo",
            "Problemas de navegación",
            "Viento fuerte en pista",
            "Fallo en el sistema de presurización",
            "Problemas con el tren de aterrizaje",
            "Avería en el sistema de combustible",
            "Problemas con el sistema de comunicación",
            "Fallo en el sistema de oxígeno",
            "Problemas con el sistema de navegación",
            "Avería en el sistema de climatización"
        };

        String[] incidentesMaritimos = {
            "Tormenta en el mar",
            "Niebla en la costa",
            "Problemas en el puerto",
            "Avería en el motor",
            "Oleaje fuerte",
            "Retraso en la descarga",
            "Problemas de navegación",
            "Control de aduanas",
            "Fallo en el sistema de propulsión",
            "Problemas con el sistema de carga",
            "Avería en el sistema de refrigeración",
            "Problemas con el sistema de navegación",
            "Fallo en el sistema de comunicación",
            "Problemas con el sistema de estabilización",
            "Avería en el sistema de lastre"
        };

        String incidente;
        int idIncidente = 100 + random.nextInt(900);
            int costeReparacion = 0;
            int diasRetraso = 0;

        // Seleccionar incidente según el tipo de transporte
        switch (tipoTransporte) {
            case "Furgoneta":
            case "Camión":
                incidente = incidentesTerrestres[random.nextInt(incidentesTerrestres.length)];
                    // Asignar costes y retrasos según el tipo de incidente
                    if (incidente.contains("Pinchazo")) {
                        costeReparacion = 500;
                        diasRetraso = 1;
                    } else if (incidente.contains("Avería") || incidente.contains("Fallo")) {
                        costeReparacion = 2000;
                        diasRetraso = 2;
                    } else if (incidente.contains("Accidente")) {
                        costeReparacion = 5000;
                        diasRetraso = 3;
                    } else {
                        costeReparacion = 1000;
                        diasRetraso = 1;
                    }
                break;
            case "Avión":
                incidente = incidentesAereos[random.nextInt(incidentesAereos.length)];
                    if (incidente.contains("Fallo") || incidente.contains("Avería")) {
                        costeReparacion = 10000;
                        diasRetraso = 2;
                    } else if (incidente.contains("Turbulencias")) {
                        costeReparacion = 0;
                        diasRetraso = 1;
                    } else {
                        costeReparacion = 5000;
                        diasRetraso = 1;
                    }
                break;
            case "Barco":
                incidente = incidentesMaritimos[random.nextInt(incidentesMaritimos.length)];
                    if (incidente.contains("Fallo") || incidente.contains("Avería")) {
                        costeReparacion = 8000;
                        diasRetraso = 2;
                    } else if (incidente.contains("Tormenta")) {
                        costeReparacion = 0;
                        diasRetraso = 2;
                    } else {
                        costeReparacion = 3000;
                        diasRetraso = 1;
                    }
                break;
            default:
                incidente = "Incidente desconocido";
                    costeReparacion = 1000;
                    diasRetraso = 1;
        }

        // Obtener la fecha límite de entrega del pedido
        Calendar fechaLimite = Calendar.getInstance();
        fechaLimite.setTime(fechaActual.getTime());
        fechaLimite.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());
        
        // Calcular fechas de llegada para cada opción
        Calendar fechaEspera = (Calendar) fechaActual.clone();
            fechaEspera.add(Calendar.DAY_OF_MONTH, diasRetraso + 2); // Días de retraso + 2 días de espera
        
        Calendar fechaDesvio = (Calendar) fechaActual.clone();
            fechaDesvio.add(Calendar.DAY_OF_MONTH, diasRetraso + 1); // Días de retraso + 1 día por desviar
            
            Calendar fechaReparacion = (Calendar) fechaActual.clone();
            fechaReparacion.add(Calendar.DAY_OF_MONTH, diasRetraso); // Solo los días de retraso por reparación

        // Calcular los días de retraso para cada opción
        int diasRetrasoEspera = calcularDiasRetraso(fechaEspera, fechaLimite);
        int diasRetrasoDesvio = calcularDiasRetraso(fechaDesvio, fechaLimite);
            int diasRetrasoReparacion = calcularDiasRetraso(fechaReparacion, fechaLimite);

        System.out.println("\n❗ ALERTA: Incidente #" + idIncidente + " - " + incidente);
        System.out.println("   - Riesgo: Retraso en entrega");
        System.out.println("   - Fecha límite de entrega: " + formatoFecha.format(fechaLimite.getTime()));
        System.out.println("   - Soluciones posibles:");
            
            // Opción 1: Esperar
        System.out.println("     01. Esperar");
        System.out.println("         • Nueva fecha de entrega: " + formatoFecha.format(fechaEspera.getTime()));
        if (diasRetrasoEspera > 0) {
            if (diasRetrasoEspera == 1) {
                System.out.println("         • Penalización: 50% del pago");
            } else if (diasRetrasoEspera == 2) {
                System.out.println("         • Penalización: 10% del pago");
            } else {
                System.out.println("         • Penalización: 65% de multa");
            }
        } else {
            System.out.println("         • Sin penalización");
        }
        
            // Opción 2: Desviar ruta

        System.out.println("     02. Desviar ruta (Coste adicional: $1,000)");
        System.out.println("         • Nueva fecha de entrega: " + formatoFecha.format(fechaDesvio.getTime()));
        if (diasRetrasoDesvio > 0) {
            if (diasRetrasoDesvio == 1) {
                System.out.println("         • Penalización: 50% del pago");
            } else if (diasRetrasoDesvio == 2) {
                System.out.println("         • Penalización: 10% del pago");
            } else {
                System.out.println("         • Penalización: 65% de multa");
            }
        } else {
            System.out.println("         • Sin penalización");
        }
        
            // Opción 3: Reparar (solo si hay coste de reparación)
            if (costeReparacion > 0) {
                System.out.println("     03. Reparar (Coste: $" + costeReparacion + ")");

                System.out.println("         • Nueva fecha de entrega: " + formatoFecha.format(fechaReparacion.getTime()));
                if (diasRetrasoReparacion > 0) {
                    if (diasRetrasoReparacion == 1) {
                        System.out.println("         • Penalización: 50% del pago");
                    } else if (diasRetrasoReparacion == 2) {
                        System.out.println("         • Penalización: 10% del pago");
                    } else {
                        System.out.println("         • Penalización: 65% de multa");
                    }
                } else {
                    System.out.println("         • Sin penalización");
                }
            }
            
            System.out.print("\nSeleccione solución (01-03): ");
        String solucion = scanner.nextLine();

        System.out.println("\n🛠 Aplicando patrón *Template Method*:");
       
        System.out.println("   1. Identificando causa: " + incidente);
        System.out.println("   2. Asignando recursos...");
        
            switch (solucion) {
                case "02":
                case "2":
            System.out.println("   3. Desviando ruta...");
            System.out.println("✅ Resuelto: Envío llegará el " + formatoFecha.format(fechaDesvio.getTime()));
            if (diasRetrasoDesvio > 0) {
                if (diasRetrasoDesvio == 1) {
                            pedido.setPago((int)(pedido.getPago() * 0.5));
                } else if (diasRetrasoDesvio == 2) {
                            pedido.setPago((int)(pedido.getPago() * 0.9));
                } else {
                            pedido.setPago((int)(pedido.getPago() * 0.35));
                        }
                    }
                    jugador.gastar(1000); // Coste adicional por desviar
                    vehiculoAfectado.aplicarDesgaste(); // Desgaste adicional por desviar
                    break;
                    
                case "03":
                case "3":
                    if (costeReparacion > 0) {
                        if (jugador.getBalance() >= costeReparacion) {
                            System.out.println("   3. Realizando reparación...");
                            System.out.println("✅ Resuelto: Envío llegará el " + formatoFecha.format(fechaReparacion.getTime()));
                            if (diasRetrasoReparacion > 0) {
                                if (diasRetrasoReparacion == 1) {
                                    pedido.setPago((int)(pedido.getPago() * 0.5));
                                } else if (diasRetrasoReparacion == 2) {
                                    pedido.setPago((int)(pedido.getPago() * 0.9));
                                } else {
                                    pedido.setPago((int)(pedido.getPago() * 0.35));
                                }
                            }
                            jugador.gastar(costeReparacion);
                            vehiculoAfectado.reparar(); // Reparar el vehículo
                        } else {
                            System.out.println("❌ No hay suficiente balance para la reparación");
                            System.out.println("   3. Esperando resolución...");
            System.out.println("✅ Resuelto: Envío llegará el " + formatoFecha.format(fechaEspera.getTime()));
            if (diasRetrasoEspera > 0) {
                if (diasRetrasoEspera == 1) {
                                    pedido.setPago((int)(pedido.getPago() * 0.5));
                } else if (diasRetrasoEspera == 2) {
                                    pedido.setPago((int)(pedido.getPago() * 0.9));
                } else {
                                    pedido.setPago((int)(pedido.getPago() * 0.35));
                }
            } else {
                                pedido.setPago((int)(pedido.getPago() * 0.35));
                            }
                            vehiculoAfectado.aplicarDesgaste(); // Desgaste adicional por esperar
                        }
        } else {
                        System.out.println("❌ No se puede reparar este tipo de incidente");
                        System.out.println("   3. Esperando resolución...");
            System.out.println("✅ Resuelto: Envío llegará el " + formatoFecha.format(fechaEspera.getTime()));
            if (diasRetrasoEspera > 0) {
                if (diasRetrasoEspera == 1) {
                                pedido.setPago((int)(pedido.getPago() * 0.5));
                } else if (diasRetrasoEspera == 2) {
                                pedido.setPago((int)(pedido.getPago() * 0.9));
                } else {
                                pedido.setPago((int)(pedido.getPago() * 0.35));
                            }
                        }
                        vehiculoAfectado.aplicarDesgaste(); // Desgaste adicional por esperar
                    }
                    break;
                    
                default:
                    System.out.println("   3. Esperando resolución...");
                    System.out.println("✅ Resuelto: Envío llegará el " + formatoFecha.format(fechaEspera.getTime()));
                    if (diasRetrasoEspera > 0) {
                        if (diasRetrasoEspera == 1) {
                            pedido.setPago((int)(pedido.getPago() * 0.5));
                        } else if (diasRetrasoEspera == 2) {
                            pedido.setPago((int)(pedido.getPago() * 0.9));
                        } else {
                            pedido.setPago((int)(pedido.getPago() * 0.35));
                        }
                    }
                    vehiculoAfectado.aplicarDesgaste(); // Desgaste adicional por esperar
            }
            
            pedido.setDiasRestantes(pedido.getDiasRestantes() + diasRetraso);
            satisfaccionClientes -= 5;
        }
    }
    
    private int calcularDiasRetraso(Calendar fechaLlegada, Calendar fechaLimite) {
        long diffMillis = fechaLlegada.getTimeInMillis() - fechaLimite.getTimeInMillis();
        int dias = (int) (diffMillis / (1000 * 60 * 60 * 24));
        return Math.max(0, dias); // Retornar 0 si la fecha es anterior o igual
    }

    /**
     * Muestra las estadísticas actuales del juego
     */
    private void mostrarEstadisticas() {
        System.out.println("\n📊 ESTADÍSTICAS DEL DÍA " + diaActual);
        System.out.println("==================================================");
        System.out.println("💰 Balance actual: $" + jugador.getBalance());
        System.out.println("💰 Beneficios acumulados: $" + (beneficiosAcumulados - gastosAcumulados));
        System.out.println("🚚 Envíos totales: " + enviosTotales);
        System.out.println("✅ Envíos exitosos: " + enviosExitosos);
        System.out.println("❌ Envíos fallidos: " + (enviosTotales - enviosExitosos));
        System.out.println("😊 Satisfacción clientes: " + satisfaccionClientes + "%");
        System.out.println("==================================================");
    }

    /**
     * Procesa el pago de impuestos
     */
    private void procesarImpuestos() {
        if (diaActual % calcularDiasImpuestos() == 0) {
            int impuestos = (int)(beneficiosAcumulados * TASA_IMPUESTOS);
            System.out.println("\n💰 " + jugador.getNombre() + ", es hora de pagar impuestos");
            System.out.println("   - Debes pagar el " + (TASA_IMPUESTOS * 100) + "% de tus beneficios");
            System.out.println("   - Beneficios acumulados: $" + beneficiosAcumulados);
            System.out.println("   - Impuestos a pagar: $" + impuestos);
            
            beneficiosAcumulados = 0;
        }
    }

    /**
     * Genera vehículos aleatorios para el mercado
     */
    private void generarVehiculosMercado() {
        vehiculosMercado = new ArrayList<>();
        Random random = new Random();
        Set<String> idsUsados = new HashSet<>();
        
        // Generar 3 vehículos diferentes
        while (vehiculosMercado.size() < 3) {
            String tipo = random.nextBoolean() ? "Furgoneta" : "Camión";
            // Generar ID con formato 1Letra2Numeros
            String id = tipo.charAt(0) + String.format("%02d", random.nextInt(100));
            
            // Verificar que el ID no esté repetido
            if (idsUsados.contains(id)) {
                continue;
            }
            idsUsados.add(id);
            
            // Generar tipos de carga permitidos aleatorios (mínimo 1, máximo 3)
            int numTipos = 1 + random.nextInt(3);
            List<String> tiposPermitidos = new ArrayList<>();
            
            for (int j = 0; j < numTipos; j++) {
                String tipoCarga;
                do {
                    tipoCarga = TIPOS_CARGA[random.nextInt(TIPOS_CARGA.length)];
                } while (tiposPermitidos.contains(tipoCarga));
                tiposPermitidos.add(tipoCarga);
            }
            
            vehiculosMercado.add(new Vehiculo(tipo, id, tiposPermitidos.toArray(new String[0])));
        }
    }

    /**
     * Avanza al siguiente día
     */
    private void pasarDia() {
        if (!pedidosPendientes.isEmpty()) {
            System.out.println("\n❌ " + jugador.getNombre() + ", no puedes pasar al siguiente día con pedidos pendientes");
            mostrarMenuPartida();
            procesarOpcion(scanner.nextLine());
            return;
        }

        diaActual++;
        fechaActual.add(Calendar.DAY_OF_MONTH, 1); // Añadir un día a la fecha actual

        System.out.println("\n==================================================");
        System.out.println("📅 DÍA " + diaActual + " (" + formatoFecha.format(fechaActual.getTime()) + ") | ALMACÉN PRINCIPAL: " + almacenPrincipal);
        System.out.println("==================================================");
        
        // Procesar envíos
        procesarPedidosEnCurso();
        
        // Procesar impuestos
        procesarImpuestos();
        
        // Verificar objetivos de campaña
        if (modoJuego.equals("campaña")) {
            verificarObjetivosCampaña();
        }
        
        // Generar nuevos vehículos en el mercado
        generarVehiculosMercado();
        
        // Generar nuevos pedidos del día
        generarPedidosDia();

        // Mostrar estadísticas
        mostrarEstadisticas();
    }

    private void procesarPedidosEnCurso() {
        List<Pedido> pedidosCompletados = new ArrayList<>();
        
        for (Pedido pedido : pedidosEnCurso) {
            pedido.reducirDiasRestantes();
            
            if (pedido.getDiasRestantes() <= 0) {
                int pagoOriginal = pedido.getPago();
                int multa = 0;
                int ganancia = pagoOriginal;
                boolean exito = true;
                String mensaje = "";
                
                // Verificar si hay retraso
                Calendar fechaLlegada = (Calendar) fechaActual.clone();
                Calendar fechaEntrega = pedido.getFechaEntregaCalendar();
                int diasRetraso = calcularDiasRetraso(fechaLlegada, fechaEntrega);
                
                if (diasRetraso > 0) {
                    multa = diasRetraso * pedido.getMultaPorDia();
                    ganancia = pagoOriginal - multa;
                    mensaje = "⚠️ Envío retrasado " + diasRetraso + " días";
                    exito = false;
                } else {
                    int diasAdelanto = pedido.getDiasEntrega() - pedido.getDiasRestantes();
                    if (diasAdelanto > 0) {
                        ganancia = pagoOriginal + (diasAdelanto * pedido.getBonificacionPorDia());
                        mensaje = "✅ Envío completado con " + diasAdelanto + " días de adelanto";
                    } else {
                        mensaje = "✅ Envío completado a tiempo";
                    }
                }
                
                if (exito) {
                    enviosExitosos++;
                    jugador.recuperarBalance(ganancia);
                    beneficiosAcumulados += ganancia;
                } else {
                    satisfaccionClientes -= 10;
                }

                System.out.println(mensaje);
                System.out.println("💰 Pago original: $" + pagoOriginal);
                System.out.println("💰 Multa por retraso: $" + multa);
                System.out.println("💰 Ganancia final: $" + ganancia);
                
                // Encontrar y liberar el vehículo
                String idVehiculo = pedido.getTransporteAsignado().split(" ")[1];
                Vehiculo vehiculo = flota.stream()
                    .filter(v -> v.getId().equals(idVehiculo))
                    .findFirst()
                    .orElse(null);
                    
                if (vehiculo != null) {
                    vehiculo.asignarPedido(null);
                    vehiculo.aplicarDesgaste(); // Aplicar desgaste por completar el viaje
                }
                
                enviosTotales++;
                pedidosCompletados.add(pedido);
            }
        }
        
        // Eliminar pedidos completados
        pedidosEnCurso.removeAll(pedidosCompletados);
        
        // Limpiar pedidos pendientes
        pedidos.clear();
    }

    /**
     * Muestra la pantalla de fin de juego
     */
    private void mostrarGameOver() {
        System.out.println("\n==============================================");
        System.out.println("🎮 GAME OVER");
        System.out.println("==============================================");
        System.out.println("💰 Balance final: $" + jugador.getBalance());
        System.out.println("😊 Satisfacción final: " + satisfaccionClientes + "%");
        System.out.println("🚚 Envíos totales: " + enviosTotales);
        System.out.println("✅ Envíos exitosos: " + enviosExitosos);
    }

    /**
     * Verifica si el jugador está derrotado según el modo de juego
     * @return true si el jugador está derrotado, false si no
     */
    private boolean jugadorDerrotado() {
        if (modoJuego.equals("libre")) {
            return false; // En modo libre nunca se pierde
        }
        return jugador.getBalance() < 0; // Cambiado de <= 0 a < 0 para que termine cuando sea negativo
    }

    /**
     * Gasta dinero según el modo de juego
     * @param cantidad Cantidad a gastar
     */
    private void gastarDinero(int cantidad) {
        if (!modoJuego.equals("libre")) {
            jugador.gastar(cantidad);
        }
    }

    /**
     * Recibe dinero según el modo de juego
     * @param cantidad Cantidad a recibir
     */
    private void recibirDinero(int cantidad) {
        if (!modoJuego.equals("libre")) {
            jugador.recuperarBalance(cantidad);
        }
    }

    /**
     * Verifica si se han alcanzado los objetivos de la campaña
     * @return true si se han alcanzado los objetivos, false si no
     */
    private boolean verificarObjetivosCampaña() {
        if (!modoJuego.equals("campaña")) {
            return false;
        }

        // Obtener objetivos según la dificultad
        Map<String, Integer> objetivos = OBJETIVOS_CAMPANA.get(dificultad);
        if (objetivos == null) {
            return false;
        }

        // Verificar si se ha alcanzado el día máximo
        if (diaActual >= objetivos.get("dias")) {
            // Verificar el resto de objetivos
            boolean objetivosAlcanzados = 
                enviosExitosos >= objetivos.get("enviosExitosos") &&
                satisfaccionClientes >= objetivos.get("satisfaccion") &&
                beneficiosAcumulados >= objetivos.get("beneficios");

            // Mostrar resultado final
            System.out.println("\n=== 🎯 RESULTADO DE LA CAMPAÑA 🎯 ===");
            System.out.println("Nivel: " + dificultad.toUpperCase());
            System.out.println("Días jugados: " + diaActual + "/" + objetivos.get("dias"));
            System.out.println("Envíos exitosos: " + enviosExitosos + "/" + objetivos.get("enviosExitosos"));
            System.out.println("Satisfacción: " + satisfaccionClientes + "%/" + objetivos.get("satisfaccion") + "%");
            System.out.println("Beneficios: " + beneficiosAcumulados + "€/" + objetivos.get("beneficios") + "€");
            System.out.println("\nResultado: " + (objetivosAlcanzados ? "✅ VICTORIA" : "❌ DERROTA"));

            // Guardar estadísticas
            guardarEstadisticas();
            
            // Terminar el juego
            System.exit(0);
        }

        return false;
    }

    /**
     * Verifica si el jugador ha perdido y guarda las estadísticas en el histórico si es así.
     */
    private void verificarDerrota() {
        if (jugador.getBalance() <= 0) {
            System.out.println("\n❌ Has perdido. Tu balance ha llegado a 0€.");
            guardarEstadisticas();
            System.out.println("📊 Tus estadísticas han sido guardadas en el histórico.");
            System.out.println("📊 Tus estadísticas han sido guardadas en el histórico.");
            System.exit(0);
        }
    }

    /**
     * Ajusta la satisfacción del cliente al inicio del juego según el modo de juego.
     */
    private void inicializarSatisfaccionClientes() {
        if (modoJuego.equals("libre")) {
            satisfaccionClientes = 100;
        } else {
            satisfaccionClientes = 50;
        }
    }

    /**
     * Ajusta la satisfacción del cliente al enviar un pedido exitosamente.
     */
    private void ajustarSatisfaccionEnvioExitoso() {
        switch (dificultad) {
            case "easy":
                satisfaccionClientes = Math.min(100, satisfaccionClientes + 15);
                break;
            case "medium":
                satisfaccionClientes = Math.min(100, satisfaccionClientes + 10);
                break;
            case "hard":
                satisfaccionClientes = Math.min(100, satisfaccionClientes + 5);
                break;
        }
    }

    /**
     * Ajusta la satisfacción del cliente al no entregar un pedido a tiempo o rechazarlo.
     */
    private void ajustarSatisfaccionEnvioFallido() {
        switch (dificultad) {
            case "easy":
                satisfaccionClientes = Math.max(0, satisfaccionClientes - 15);
                break;
            case "medium":
                satisfaccionClientes = Math.max(0, satisfaccionClientes - 10);
                break;
            case "hard":
                satisfaccionClientes = Math.max(0, satisfaccionClientes - 5);
                break;
        }
    }
}