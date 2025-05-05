package game;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Clase que representa un vehículo en la flota
 */
public class Vehiculo {
    private String tipo;
    private String id;
    private Pedido pedidoAsignado;
    private int capacidad;
    private int velocidad; // km/h
    private int costePorKm;
    private Set<String> tiposPaquetesPermitidos;
    
    // Rangos preestablecidos para cada tipo de vehículo
    private static final int[][] RANGOS_FURGONETA = {
        {800, 1500},    // Capacidad
        {70, 100},      // Velocidad
        {1, 3}          // Coste por km
    };
    
    private static final int[][] RANGOS_CAMION = {
        {4000, 7000},   // Capacidad
        {50, 80},       // Velocidad
        {3, 6}          // Coste por km
    };
    
    private static final int[][] RANGOS_BARCO = {
        {15000, 25000}, // Capacidad
        {20, 40},       // Velocidad
        {3, 5}          // Coste por km
    };
    
    private static final int[][] RANGOS_AVION = {
        {8000, 12000},  // Capacidad
        {400, 600},     // Velocidad
        {8, 12}         // Coste por km
    };

    /**
     * Constructor de la clase Vehiculo con valores aleatorios
     * @param tipo Tipo de vehículo
     * @param id Identificador único
     * @param tiposPaquetes Tipos de paquetes que puede transportar el vehículo
     */
    public Vehiculo(String tipo, String id, String... tiposPaquetes) {
        this.tipo = tipo;
        this.id = id;
        this.pedidoAsignado = null;
        this.tiposPaquetesPermitidos = new HashSet<>();
        
        // Generar características aleatorias según el tipo
        int[][] rangos = obtenerRangosPorTipo(tipo);
        Random random = new Random();
        
        this.capacidad = rangos[0][0] + random.nextInt(rangos[0][1] - rangos[0][0]);
        this.velocidad = rangos[1][0] + random.nextInt(rangos[1][1] - rangos[1][0]);
        this.costePorKm = rangos[2][0] + random.nextInt(rangos[2][1] - rangos[2][0]);
        
        // Todos los vehículos pueden transportar paquetes normales
        this.tiposPaquetesPermitidos.add("NORMAL");
        
        // Añadir los tipos de paquetes específicos
        for (String tipoPaquete : tiposPaquetes) {
            this.tiposPaquetesPermitidos.add(tipoPaquete);
        }
    }

    /**
     * Constructor de la clase Vehiculo con valores específicos
     * @param tipo Tipo de vehículo
     * @param id Identificador único
     * @param capacidad Capacidad de carga en kg
     * @param velocidad Velocidad en km/h
     * @param costePorKm Coste por kilómetro
     * @param tiposPaquetes Tipos de paquetes que puede transportar el vehículo
     */
    public Vehiculo(String tipo, String id, int capacidad, int velocidad, int costePorKm, String... tiposPaquetes) {
        this.tipo = tipo;
        this.id = id;
        this.capacidad = capacidad;
        this.velocidad = velocidad;
        this.costePorKm = costePorKm;
        this.pedidoAsignado = null;
        this.tiposPaquetesPermitidos = new HashSet<>();
        
        // Todos los vehículos pueden transportar paquetes normales
        this.tiposPaquetesPermitidos.add("NORMAL");
        
        // Añadir los tipos de paquetes específicos
        for (String tipoPaquete : tiposPaquetes) {
            this.tiposPaquetesPermitidos.add(tipoPaquete);
        }
    }

    private int[][] obtenerRangosPorTipo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "furgoneta":
                return RANGOS_FURGONETA;
            case "camión":
            case "camion":
                return RANGOS_CAMION;
            case "barco":
                return RANGOS_BARCO;
            case "avión":
            case "avion":
                return RANGOS_AVION;
            default:
                return RANGOS_FURGONETA; // Por defecto retorna rangos de furgoneta
        }
    }

    /**
     * Obtiene el tipo de vehículo
     * @return String con el tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene el ID del vehículo
     * @return String con el ID
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el pedido asignado
     * @return Pedido asignado o null si no tiene
     */
    public Pedido getPedidoAsignado() {
        return pedidoAsignado;
    }

    /**
     * Asigna un pedido al vehículo
     * @param pedido Pedido a asignar
     */
    public void asignarPedido(Pedido pedido) {
        this.pedidoAsignado = pedido;
        if (pedido != null) {
            pedido.setTransporteAsignado(tipo + " " + id);
        }
    }

    /**
     * Verifica si el vehículo está disponible
     * @return true si está disponible, false si no
     */
    public boolean estaDisponible() {
        return pedidoAsignado == null;
    }

    /**
     * Obtiene la capacidad del vehículo
     * @return int con la capacidad
     */
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Obtiene la velocidad del vehículo
     * @return int con la velocidad
     */
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * Obtiene el coste por kilómetro
     * @return int con el coste
     */
    public int getCostePorKm() {
        return costePorKm;
    }

    /**
     * Calcula el tiempo estimado de entrega en horas
     * @param distancia Distancia en kilómetros
     * @return int con las horas estimadas
     */
    public int calcularTiempoEntrega(int distancia) {
        return (int) Math.ceil((double) distancia / velocidad);
    }

    /**
     * Verifica si el vehículo puede transportar un tipo de paquete específico
     * @param tipoPaquete Tipo de paquete a verificar
     * @return true si puede transportarlo, false si no
     */
    public boolean puedeTransportarTipo(String tipoPaquete) {
        return tiposPaquetesPermitidos.contains(tipoPaquete);
    }

    /**
     * Obtiene los tipos de paquetes que puede transportar el vehículo
     * @return Set con los tipos de paquetes permitidos
     */
    public Set<String> getTiposPaquetesPermitidos() {
        return tiposPaquetesPermitidos;
    }
} 