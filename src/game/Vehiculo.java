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
    private static final Random random = new Random();
    
    private static final Map<String, Map<String, int[]>> RANGOS_VEHICULOS = new HashMap<>();
    
    static {
        // Furgoneta: Ligera y rápida
        Map<String, int[]> rangosFurgoneta = new HashMap<>();
        rangosFurgoneta.put("capacidad", new int[]{500, 1000}); // 500-1000 kg
        rangosFurgoneta.put("velocidad", new int[]{120, 150}); // 120-150 km/h
        rangosFurgoneta.put("costePorKm", new int[]{3, 3}); // 3€/km fijo
        RANGOS_VEHICULOS.put("Furgoneta", rangosFurgoneta);
        
        // Camión: Alta capacidad, más lento
        Map<String, int[]> rangosCamion = new HashMap<>();
        rangosCamion.put("capacidad", new int[]{3000, 5000}); // 3000-5000 kg
        rangosCamion.put("velocidad", new int[]{40, 60}); // 40-60 km/h
        rangosCamion.put("costePorKm", new int[]{4, 6}); // 4-6€/km
        RANGOS_VEHICULOS.put("Camión", rangosCamion);
        
        // Barco: Muy alta capacidad, muy lento
        Map<String, int[]> rangosBarco = new HashMap<>();
        rangosBarco.put("capacidad", new int[]{10000, 15000}); // 10000-15000 kg
        rangosBarco.put("velocidad", new int[]{30, 40}); // 30-40 km/h
        rangosBarco.put("costePorKm", new int[]{8, 11}); // 8-11€/km
        RANGOS_VEHICULOS.put("Barco", rangosBarco);
        
        // Avión: Capacidad media, muy rápido
        Map<String, int[]> rangosAvion = new HashMap<>();
        rangosAvion.put("capacidad", new int[]{8000, 10000}); // 8000-10000 kg
        rangosAvion.put("velocidad", new int[]{500, 700}); // 500-700 km/h
        rangosAvion.put("costePorKm", new int[]{10, 15}); // 10-15€/km
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
            int[] rangoCapacidad = rangos.get("capacidad");
            int[] rangoVelocidad = rangos.get("velocidad");
            int[] rangoCoste = rangos.get("costePorKm");
            
            // Asegurar que el rango sea válido
            this.capacidad = rangoCapacidad[0] + (rangoCapacidad[1] > rangoCapacidad[0] ? 
                random.nextInt(rangoCapacidad[1] - rangoCapacidad[0]) : 0);
            
            this.velocidad = rangoVelocidad[0] + (rangoVelocidad[1] > rangoVelocidad[0] ? 
                random.nextInt(rangoVelocidad[1] - rangoVelocidad[0]) : 0);
            
            this.costePorKm = rangoCoste[0] + (rangoCoste[1] > rangoCoste[0] ? 
                random.nextInt(rangoCoste[1] - rangoCoste[0]) : 0);
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