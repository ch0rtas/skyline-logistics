package game;

import static game.CityConstants.*;
import static util.BalanceUtils.*;
import static util.GameSetupUtils.inicializarFlota;
import static util.PenaltyUtils.*;
import static game.VehiculoGenerator.generarVehiculosMercado;
import static game.VehiculoUtils.mostrarVehiculosDisponibles;
import static game.ReparacionVehiculos.repararVehiculo;
import static game.MercadoVehiculos.mostrarMercadoVehiculos;
import static game.EstadisticasHandler.guardarEstadisticas;
import static game.PedidoUtils.mostrarPedidosPendientes;
import static game.CampanaObjetivos.OBJETIVOS_CAMPANA;

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
    public static final double TASA_IMPUESTOS = 0.45;
    public static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");

    private List<Vehiculo> vehiculosMercado;
    private PedidoGenerator pedidoGenerator;
    private static final String[] TIPOS_CARGA = {"NORMAL", "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL", "PERECEDERO", "ALTO_VALOR", "SERES_VIVOS"};

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
        this.jugador = new Jugador(nombreJugador, calcularBalanceInicial(dificultad, modoJuego));
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
        
        // Inicializar flota de vehículos según la dificultad
        this.flota = inicializarFlota(dificultad);
        
        this.pedidoGenerator = new PedidoGenerator(this.fechaActual, this.flota, this.almacenPrincipal, this.dificultad);
    }

    /**
     * Aplica impuestos al balance del jugador según la dificultad
     */
    private void aplicarImpuestos() {
        int balanceActual = jugador.getBalance();
        int impuestos = (int) (balanceActual * TASA_IMPUESTOS);
        jugador.gastar(impuestos);
        System.out.println("\n💰 Se han aplicado impuestos por valor de $" + impuestos);
    }

    /**
     * Calcula la multa por rechazar un pedido
     * @param pedido Pedido a rechazar
     * @return int con el monto de la multa
     */
    public int calcularMultaRechazo(Pedido pedido) {
        int multaBase = (int) (pedido.getPago() * 0.5); // 50% del pago como multa base
        if (pedido.getPrioridad().equals("URGENTE")) {
            multaBase *= 2; // Doble multa para pedidos urgentes
        }
        return multaBase;
    }

    /**
     * Inicia el juego
     */
    public void iniciar() {
        mostrarBienvenida();
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
     * Muestra la flota de vehículos
     */
    private void mostrarFlota() {
        GameUIHelper.mostrarFlota(flota, scanner, fechaActual, almacenPrincipal, this);
    }

    /**
     * Muestra el menú de reparación de vehículos
     */
    public void repararVehiculo() {
        ReparacionVehiculos.repararVehiculo(this);
    }

    /**
     * Muestra el mercado de vehículos
     */
    private void mostrarMercadoVehiculos() {
        MercadoVehiculos.mostrarMercadoVehiculos(vehiculosMercado, jugador, flota, scanner);
    }

    /**
     * Muestra el menú principal
     */
    public void mostrarMenuPartida() {
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
    public void procesarOpcion(String opcion) {
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
        EstadisticasHandler.guardarEstadisticas(modoJuego, jugador, diaActual, beneficiosPorDia, enviosExitosos, satisfaccionClientes, fechaInicio, dificultad, ciudad);
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
        return this.pedidoGenerator.generarPedidoAleatorio();
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
    public void generarPedidosDia() {
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
    public static String generarLineaSeparadora(int[] anchos) {
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
    public static String generarFilaTabla(String[] valores, int[] anchos) {
        StringBuilder fila = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            fila.append(String.format(" %-" + anchos[i] + "s |", valores[i]));
        }
        return fila.toString();
    }

    /**
     * Muestra los pedidos pendientes
     */
    public void mostrarPedidosPendientes() {
        PedidoUtils.mostrarPedidosPendientes(pedidosPendientes);
    }

    /**
     * Muestra los pedidos en curso
     */
    private void mostrarPedidosEnCurso() {
        PedidosEnCursoHelper.mostrarPedidosEnCurso(pedidosEnCurso, fechaActual);
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
    public static int obtenerDistancia(String origen, String destino) {
        int indiceOrigen = Arrays.asList(CIUDADES).indexOf(origen);
        int indiceDestino = Arrays.asList(CIUDADES).indexOf(destino);
        
        // Validate indices before accessing the DISTANCIAS array
        if (indiceOrigen == -1 || indiceDestino == -1) {
            throw new IllegalArgumentException("Invalid city name(s): " + origen + ", " + destino);
        }
        
        return DISTANCIAS[indiceOrigen][indiceDestino];
    }

    /**
     * Verifica si una ciudad es una isla
     * @param ciudad Nombre de la ciudad
     * @return true si es una isla, false si no
     */
    public static boolean esIsla(String ciudad) {
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
    public static int calcularCosteEnvio(Vehiculo vehiculo, String origen, String destino) {
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
    public boolean vehiculoPuedeRealizarRuta(Vehiculo vehiculo, String origen, String destino) {
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
    public void mostrarVehiculosDisponibles(Pedido pedido) {
        VehiculoUtils.mostrarVehiculosDisponibles(pedido, flota, fechaActual, almacenPrincipal);
    }

    /**
     * Permite gestionar un pedido
     */
    private void gestionarPedido() {
        PedidoManager.gestionarPedido(this);
    }

    /**
     * Resuelve un incidente para un pedido
     * @param pedido Pedido afectado
     */
    public void resolverIncidente(Pedido pedido) {
        IncidentResolver resolver = new IncidentResolver(flota, random);
        resolver.resolverIncidente(pedido);
    }

    /**
     * Muestra las estadísticas actuales del juego
     */
    public void mostrarEstadisticas() {
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
    public void procesarImpuestos() {
        int diasImpuestos = switch (dificultad.toLowerCase()) {
            case "hard" -> 2;
            case "medium" -> 4;
            case "easy" -> 6;
            default -> 6;
        };
        if (diaActual % diasImpuestos == 0) {
            int impuestos = (int) (jugador.getBalance() * 0.45);
            jugador.gastar(impuestos);
            System.out.println("\n💰 Se han aplicado impuestos del 45%: -" + impuestos + "€");
        }
    }

    /**
     * Genera vehículos aleatorios para el mercado
     */
    public void generarVehiculosMercado() {
        vehiculosMercado = VehiculoGenerator.generarVehiculosMercado(TIPOS_CARGA);
    }

    /**
     * Avanza al siguiente día
     */
    private void pasarDia() {
        DiaManager.pasarDia(this);
    }

    private PedidoProcessor pedidoProcessor;

    public void procesarPedidosEnCurso() {
        pedidoProcessor.procesarPedidosEnCurso();
    }

    /**
     * Muestra la pantalla de fin de juego
     */
    private void mostrarGameOver() {
        GameOverScreen.mostrarGameOver(jugador, satisfaccionClientes, enviosTotales, enviosExitosos);
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
    public void verificarObjetivosCampaña() {
        CampanaUtils.verificarObjetivosCampaña(modoJuego, dificultad, diaActual, enviosExitosos, satisfaccionClientes, beneficiosAcumulados, OBJETIVOS_CAMPANA);
    }

    /**
     * Verifica si el jugador ha perdido y guarda las estadísticas en el histórico si es así.
     */
    private void verificarDerrota() {
        DerrotaHandler.verificarDerrota(jugador, jugador.getBalance(), this::guardarEstadisticas);
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

    public List<Pedido> getPedidosPendientes() {
        return pedidosPendientes;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public Scanner getScanner() {
        return scanner;
    }

    public List<Vehiculo> getFlota() {
        return flota;
    }

    public Map<String, Pedido> getPedidos() {
        return pedidos;
    }

    public List<Pedido> getPedidosEnCurso() {
        return pedidosEnCurso;
    }

    public void incrementarDiaActual() {
        diaActual++;
    }

    public Calendar getFechaActual() {
        return fechaActual;
    }

    public int getDiaActual() {
        return diaActual;
    }

    public String getAlmacenPrincipal() {
        return almacenPrincipal;
    }

    public String getModoJuego() {
        return modoJuego;
    }

    public void incrementarGastosAcumulados(int cantidad) {
        gastosAcumulados += cantidad;
    }

    public List<Vehiculo> getVehiculosMercado() {
        return vehiculosMercado;
    }
}