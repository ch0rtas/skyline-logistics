package game;

import decorator.IVehiculo;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;

/**
 * Clase base que representa un vehículo en la flota
 */
public class Vehiculo implements IVehiculo {
    private String tipo;
    private String id;
    private Pedido pedidoAsignado;
    private int capacidad;
    private int velocidad; // km/h
    private int costePorKm;
    private List<String> tiposPaquetesPermitidos;
    private static final Random random = new Random();
    private int salud; // Porcentaje de salud del vehículo (0-100)
    private int desgastePorViaje; // Porcentaje de desgaste por viaje
    private int consumo;
    private int precio;
    private Calendar fechaEstimadaLlegada;
    private Calendar fechaDisponibilidad;
    
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
        this.pedidoAsignado = null;
        this.tiposPaquetesPermitidos = new ArrayList<>();
        this.salud = 100; // Inicialmente al 100%
        
        // Calcular desgaste por viaje según el tipo de vehículo
        switch (tipo.toLowerCase()) {
            case "furgoneta":
                this.desgastePorViaje = 5;
                break;
            case "camión":
                this.desgastePorViaje = 8;
                break;
            case "barco":
                this.desgastePorViaje = 3;
                break;
            case "avión":
                this.desgastePorViaje = 10;
                break;
            default:
                this.desgastePorViaje = 5;
        }
        
        // Todos los vehículos pueden transportar carga normal
        this.tiposPaquetesPermitidos.add("NORMAL");
        
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

        this.consumo = calcularConsumo();
        this.precio = calcularPrecio();
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
    public Vehiculo(String tipo, String id, int capacidad, int velocidad, int costePorKm, List<String> tiposPaquetesPermitidos) {
        this.tipo = tipo;
        this.id = id;
        this.capacidad = capacidad;
        this.velocidad = velocidad;
        this.costePorKm = costePorKm;
        this.pedidoAsignado = null;
        this.tiposPaquetesPermitidos = tiposPaquetesPermitidos;
        this.salud = 100;
        this.consumo = calcularConsumo();
        this.precio = calcularPrecio();
    }

    /**
     * Obtiene el tipo de vehículo
     * @return String with the type
     */
    @Override
    public String getTipo() {
        return tipo;
    }

    /**
     * Obtiene el ID del vehículo
     * @return String with the ID
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Obtiene el pedido asignado
     * @return Pedido assigned or null if none
     */
    @Override
    public Pedido getPedidoAsignado() {
        return pedidoAsignado;
    }

    /**
     * Assigns a pedido to the vehicle
     * @param pedido Pedido to assign
     */
    @Override
    public void asignarPedido(Pedido pedido) {
        this.pedidoAsignado = pedido;
        if (pedido != null) {
            pedido.setTransporteAsignado(tipo + " " + id);
            // Calculate and set the estimated arrival date
            this.fechaEstimadaLlegada = pedido.getFechaEntregaCalendar();
        } else {
            this.fechaEstimadaLlegada = null;
        }
    }

    /**
     * Verifies if the vehicle is available
     * @return true if it is available, false if not
     */
    @Override
    public boolean estaDisponible() {
        if (pedidoAsignado != null) {
            return false;
        }
        if (fechaDisponibilidad != null) {
            Calendar fechaActual = Calendar.getInstance();
            return !fechaActual.before(fechaDisponibilidad);
        }
        return true;
    }

    /**
     * Gets the vehicle's capacity
     * @return int with the capacity
     */
    @Override
    public int getCapacidad() {
        return capacidad;
    }

    /**
     * Gets the vehicle's speed
     * @return int with the speed
     */
    @Override
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * Gets the cost per kilometer
     * @return int with the cost
     */
    @Override
    public int getCostePorKm() {
        return costePorKm;
    }

    /**
     * Calculates the estimated delivery time in hours
     * @param distancia Distancia in kilometers
     * @return int with the estimated hours
     */
    @Override
    public int calcularTiempoEntrega(int distancia) {
        // Consider that vehicles cannot travel for 24 hours straight
        // We assume they travel for 8 hours a day
        double horasViaje = (double) distancia / velocidad;
        // Add a 20% extra time for stops, rest, etc.
        horasViaje *= 1.2;
        return (int) Math.ceil(horasViaje);
    }

    /**
     * Verifies if the vehicle can transport a specific package type
     * @param tipoPaquete Type of package to verify
     * @return true if it can transport it, false if not
     */
    @Override
    public boolean puedeTransportarTipo(String tipoPaquete) {
        return tiposPaquetesPermitidos.contains(tipoPaquete);
    }

    /**
     * Gets the types of packages that the vehicle can transport
     * @return Set with the allowed package types
     */
    @Override
    public List<String> getTiposPaquetesPermitidos() {
        return tiposPaquetesPermitidos;
    }

    /**
     * Gets the current health of the vehicle
     * @return int with the health percentage (0-100)
     */
    @Override
    public int getSalud() {
        return salud;
    }

    /**
     * Gets the health percentage (0-100)
     */
    @Override
    public int getDesgastePorViaje() {
        return desgastePorViaje;
    }

    /**
     * Applies the health percentage (0-100)
     */
    @Override
    public void aplicarDesgaste() {
        reducirSalud(this.desgastePorViaje);
    }

    /**
     * Calculates the repair cost of the vehicle
     * @return int with the repair cost
     */
    @Override
    public int calcularCosteReparacion() {
        int costeBase = 0;
        switch (tipo.toLowerCase()) {
            case "furgoneta":
                costeBase = 1000;
                break;
            case "camión":
                costeBase = 2000;
                break;
            case "barco":
                costeBase = 5000;
                break;
            case "avión":
                costeBase = 10000;
                break;
        }
        
        // The cost increases according to the damage the vehicle has
        int porcentajeDano = 100 - salud;
        return costeBase + (costeBase * porcentajeDano / 100);
    }

    /**
     * Repairs the vehicle
     * @return int with the repair cost
     */
    @Override
    public int reparar() {
        int coste = calcularCosteReparacion();
        salud = 100;
        return coste;
    }

    @Override
    public String getNombre() {
        return tipo + " " + id;
    }

    @Override
    public int getCosteReparacion() {
        return (100 - salud) * 100; // 100€ for each point of lost health
    }

    @Override
    public int getConsumo() {
        return consumo;
    }

    @Override
    public int getPrecio() {
        return precio;
    }

    private int calcularConsumo() {
        return (capacidad / 100) + (velocidad / 10); // Consumption based on capacity and speed
    }

    private int calcularPrecio() {
        return (capacidad * 2) + (velocidad * 10) + (costePorKm * 100) + (tiposPaquetesPermitidos.size() * 1000);
    }

    // Added a method to reduce the health of the vehicle
    @Override
    public void reducirSalud(int cantidad) {
        this.salud = Math.max(0, this.salud - cantidad);
        if (this.salud < 10) {
            System.out.println("⚠️ El vehículo " + this.id + " necesita reparación urgente");
        }
    }

    /**
     * Gets the estimated arrival date of the vehicle
     * @return Calendar with the estimated arrival date, or null if no pedido assigned
     */
    @Override
    public Calendar getFechaEstimadaLlegada() {
        return fechaEstimadaLlegada;
    }

    /**
     * Gets the availability date of the vehicle
     * @return Calendar with the availability date, or null if no date set
     */
    @Override
    public Calendar getFechaDisponibilidad() {
        return fechaDisponibilidad;
    }

    /**
     * Sets the availability date of the vehicle
     * @param fechaDisponibilidad Calendar with the availability date
     */
    @Override
    public void setFechaDisponibilidad(Calendar fechaDisponibilidad) {
        this.fechaDisponibilidad = fechaDisponibilidad;
    }
}