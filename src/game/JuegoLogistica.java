package game;

import static game.CityConstants.*;
import static util.BalanceUtils.*;
import static util.GameSetupUtils.inicializarFlota;
import static game.CampanaObjetivos.OBJETIVOS_CAMPANA;
import static game.SatisfaccionClientesUtils.inicializarSatisfaccionClientes;
import static game.CiudadUtils.normalizarNombreCiudad;
import template.AbstractImpuestosProcessor;
import template.ImpuestosProcessorConcreto;

import java.util.Scanner;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import ui.MenuUI;
import ui.BienvenidaUI;
import ui.OpcionProcessor;
import game.EstadisticasHelper;
import game.GameRules;
import decorator.IVehiculo;

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
    private List<IVehiculo> flota;
    private int diaActual;
    private Calendar fechaActual;
    private String almacenPrincipal;
    private String ciudad;
    private String dificultad;
    private String modoJuego;
    private int satisfaccionClientes;
    private int enviosExitosos;
    private int enviosTotales;
    private int enviosFallidos;
    private int beneficiosAcumulados;
    private int gastosAcumulados = 0;
    private int[] beneficiosPorDia;
    private String fechaInicio;
    public static final double TASA_IMPUESTOS = 0.45;
    public static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");

    private List<IVehiculo> vehiculosMercado;
    private PedidoGenerator pedidoGenerator;
    private static final String[] TIPOS_CARGA = {"NORMAL", "REFRIGERADO", "CONGELADO", "PELIGROSO", "ESCOLTADO", "FRÁGIL", "PERECEDERO", "ALTO_VALOR", "SERES_VIVOS"};
    private IncidentHandler incidentHandler;
    private AbstractImpuestosProcessor impuestosProcessor;

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
        this.satisfaccionClientes = inicializarSatisfaccionClientes(modoJuego); // Inicialización por defecto
        this.enviosExitosos = 0;
        this.enviosTotales = 0;
        this.enviosFallidos = 0;
        this.beneficiosAcumulados = 0;
        this.beneficiosPorDia = new int[365]; // Máximo 365 días
        
        // Guardar fecha y hora de inicio
        SimpleDateFormat formatoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.fechaInicio = formatoFechaHora.format(new Date());
        
        // Inicializar flota de vehículos según la dificultad
        this.flota = inicializarFlota(dificultad);
        
        this.pedidoGenerator = new PedidoGenerator(this.fechaActual, this.flota, this.almacenPrincipal, this.dificultad);
        
        // Inicializar pedidoProcessor en el constructor
        int[] estadisticas = new int[]{this.enviosExitosos, this.enviosFallidos, this.beneficiosAcumulados, this.satisfaccionClientes};
        this.pedidoProcessor = new PedidoProcessor(this.pedidosEnCurso, this.fechaActual, this.flota, 
            this.jugador, estadisticas, this.random, this.dificultad, this);
        this.incidentHandler = new IncidentHandler(this.flota, this.random);
        this.impuestosProcessor = new ImpuestosProcessorConcreto();
    }
    
    /**
     * Calcula la multa por rechazar un pedido
     * @param pedido Pedido a rechazar
     * @return int con el monto de la multa
     */
    public int calcularMultaRechazo(Pedido pedido) {
        return PedidoUtils.calcularMultaRechazo(pedido);
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
        BienvenidaUI.mostrarBienvenida(almacenPrincipal, jugador);
    }

    /**
     * Muestra la flota de vehículos
     */
    public void mostrarFlota() {
        GameUIHelper.mostrarFlota(flota, scanner, fechaActual, almacenPrincipal, this);
    }

    /**
     * Muestra el menú de reparación de vehículos
     */
    public void repararVehiculo() {
        ReparacionVehiculos.repararVehiculo(this);
    }

    /**
     * Muestra el menú partida
     */
    public void mostrarMenuPartida() {
        MenuUI.mostrarMenuPartida(diaActual, fechaActual, almacenPrincipal, formatoFecha);
    }

    /**
     * Procesa la opción seleccionada
     * @param opcion Opción elegida por el usuario
     */
    public void procesarOpcion(String opcion) {
        ui.OpcionProcessor.procesarOpcion(opcion, this);
    }

    /**
     * Guarda las estadísticas del jugador en el archivo de histórico
     */
    public void guardarEstadisticas() {
        EstadisticasHandler.guardarEstadisticas(modoJuego, jugador, diaActual, beneficiosPorDia, enviosExitosos, satisfaccionClientes, fechaInicio, dificultad, ciudad);
    }

    /**
     * Genera los pedidos del día según la dificultad
     */
    public void generarPedidosDia() {
        pedidoGenerator.getPedidoStrategy().generarPedidos(dificultad, diaActual, pedidosPendientes, pedidos, fechaActual);
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
    public void mostrarPedidosEnCurso() {
        PedidosEnCursoHelper.mostrarPedidosEnCurso(pedidosEnCurso, fechaActual);
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
     * Muestra los vehículos disponibles para un pedido
     * @param pedido Pedido a transportar
     */
    public void mostrarVehiculosDisponibles(Pedido pedido) {
        VehiculoUtils.mostrarVehiculosDisponibles(pedido, flota, fechaActual, almacenPrincipal);
    }

    /**
     * Resuelve un incidente para un pedido
     * @param pedido Pedido afectado
     */
    public void resolverIncidente(Pedido pedido) {
        incidentHandler.resolverIncidente(pedido);
    }

    /**
     * Muestra las estadísticas actuales del juego
     */
    public void mostrarEstadisticas() {
        EstadisticasHelper.mostrarEstadisticas(jugador, diaActual, beneficiosAcumulados, 
            gastosAcumulados, enviosTotales, enviosExitosos, satisfaccionClientes, 
            dificultad, impuestosProcessor);
    }

    /**
     * Procesa el pago de impuestos
     */
    public void procesarImpuestos() {
        impuestosProcessor.procesarImpuestos(jugador, dificultad, diaActual);
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
    public void pasarDia() {
        DiaManager.pasarDia(this);
    }

    private PedidoProcessor pedidoProcessor;

    public void procesarPedidosEnCurso() {
        pedidoProcessor.procesarPedidos();
    }

    /**
     * Updates customer satisfaction based on the ratio of successful and total deliveries.
     */
    public void actualizarSatisfaccionClientes() {
        if (enviosTotales > 0) {
            double ratio = (double) enviosExitosos / enviosTotales;
            this.satisfaccionClientes = (int) (ratio * 100);
            
            // Ajustar la satisfacción basada en pedidos rechazados
            if (enviosFallidos > 0) {
                this.satisfaccionClientes = Math.max(0, this.satisfaccionClientes - (enviosFallidos * 10));
            }
        } else {
            this.satisfaccionClientes = 50; // Valor inicial por defecto
        }
    }

    /**
     * Incrementa el contador de envíos exitosos
     */
    public void incrementarEnviosExitosos() {
        this.enviosExitosos++;
        this.enviosTotales++;
        actualizarSatisfaccionClientes();
    }

    /**
     * Incrementa el contador de envíos fallidos
     */
    public void incrementarEnviosFallidos() {
        this.enviosFallidos++;
        this.enviosTotales++;
        actualizarSatisfaccionClientes();
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
        return GameRules.jugadorDerrotado(modoJuego, jugador.getBalance());
    }

    /**
     * Verifica si se han alcanzado los objetivos de la campaña
     * @return true si se han alcanzado los objetivos, false si no
     */
    public void verificarObjetivosCampaña() {
        CampanaUtils.verificarObjetivosCampaña(modoJuego, dificultad, diaActual, enviosExitosos, satisfaccionClientes, beneficiosAcumulados, OBJETIVOS_CAMPANA);
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

    public List<IVehiculo> getFlota() {
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

    public List<IVehiculo> getVehiculosMercado() {
        return vehiculosMercado;
    }

    /**
     * Muestra el mercado de vehículos
     */
    public void mostrarMercadoVehiculos() {
        MercadoVehiculos.mostrarMercadoVehiculos(vehiculosMercado, jugador, flota, scanner, 
            dificultad, impuestosProcessor);
    }

    public String getDificultad() {
        return dificultad;
    }

    public AbstractImpuestosProcessor getImpuestosProcessor() {
        return impuestosProcessor;
    }
}