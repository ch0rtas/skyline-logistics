package game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

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
    
    private static final Map<String, Map<String, int[]>> RANGOS_VEHICULOS = new HashMap<>();
    
    static {
        // Furgoneta: Ligera y rápida, pero poca capacidad
        Map<String, int[]> rangosFurgoneta = new HashMap<>();
        rangosFurgoneta.put("capacidad", new int[]{500, 1000});  // 500-1000 kg
        rangosFurgoneta.put("velocidad", new int[]{90, 120});    // 90-120 km/h
        rangosFurgoneta.put("costeKm", new int[]{2, 4});         // 2-4€/km
        RANGOS_VEHICULOS.put("Furgoneta", rangosFurgoneta);
        
        // Camión: Gran capacidad, velocidad media
        Map<String, int[]> rangosCamion = new HashMap<>();
        rangosCamion.put("capacidad", new int[]{3000, 5000});    // 3000-5000 kg
        rangosCamion.put("velocidad", new int[]{60, 80});        // 60-80 km/h
        rangosCamion.put("costeKm", new int[]{4, 6});            // 4-6€/km
        RANGOS_VEHICULOS.put("Camión", rangosCamion);
        
        // Barco: Mayor capacidad, muy lento
        Map<String, int[]> rangosBarco = new HashMap<>();
        rangosBarco.put("capacidad", new int[]{20000, 30000});   // 20000-30000 kg
        rangosBarco.put("velocidad", new int[]{15, 25});         // 15-25 km/h
        rangosBarco.put("costeKm", new int[]{3, 5});             // 3-5€/km
        RANGOS_VEHICULOS.put("Barco", rangosBarco);
        
        // Avión: Capacidad media, muy rápido
        Map<String, int[]> rangosAvion = new HashMap<>();
        rangosAvion.put("capacidad", new int[]{8000, 12000});    // 8000-12000 kg
        rangosAvion.put("velocidad", new int[]{500, 700});       // 500-700 km/h
        rangosAvion.put("costeKm", new int[]{10, 15});           // 10-15€/km
        RANGOS_VEHICULOS.put("Avión", rangosAvion);
    }

    /**
     * Constructor que genera valores aleatorios dentro de rangos predefinidos
     * @param tipo Tipo de vehículo
     * @param id Identificador único
     * @param tiposPaquetes Tipos de paquetes que puede transportar
     */
    public Vehiculo(String tipo, String id, String... tiposPaquetes) {
        this.tipo = tipo;
        this.id = id;
        this.tiposPaquetesPermitidos = new HashSet<>();
        this.tiposPaquetesPermitidos.add("NORMAL"); // Todos los vehículos pueden transportar carga normal
        
        // Añadir tipos específicos si se proporcionan
        for (String tipoPaquete : tiposPaquetes) {
            if (!tipoPaquete.equals("NORMAL")) {
                this.tiposPaquetesPermitidos.add(tipoPaquete);
            }
        }

        // Obtener rangos según el tipo de vehículo
        Map<String, int[]> rangos = RANGOS_VEHICULOS.get(tipo);
        if (rangos != null) {
            Random random = new Random();
            this.capacidad = rangos.get("capacidad")[0] + random.nextInt(rangos.get("capacidad")[1] - rangos.get("capacidad")[0]);
            this.velocidad = rangos.get("velocidad")[0] + random.nextInt(rangos.get("velocidad")[1] - rangos.get("velocidad")[0]);
            this.costePorKm = rangos.get("costeKm")[0] + random.nextInt(rangos.get("costeKm")[1] - rangos.get("costeKm")[0]);
        } else {
            // Valores por defecto si no se encuentra el tipo
            this.capacidad = 1000;
            this.velocidad = 60;
            this.costePorKm = 5;
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