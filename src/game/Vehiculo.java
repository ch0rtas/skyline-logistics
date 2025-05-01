package game;

import java.util.HashSet;
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

    /**
     * Constructor de la clase Vehiculo
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