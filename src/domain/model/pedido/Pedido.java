package domain.model.pedido;

import state.core.EstadoPedido;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Clase que representa un pedido en el sistema.
 * Implementa el patrón State para gestionar su ciclo de vida.
 */
public class Pedido {
    private final String id;
    private final String origen;
    private final String destino;
    private final double peso;
    private final String descripcion;
    private final LocalDateTime fechaCreacion;
    private EstadoPedido estado;
    private double costo;
    private LocalDateTime fechaEstimadaEntrega;
    private LocalDateTime fechaEntregaReal;

    /**
     * Constructor de la clase Pedido
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @param peso Peso del envío en kg
     * @param descripcion Descripción del contenido
     */
    public Pedido(String origen, String destino, double peso, String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * Obtiene el ID del pedido
     * @return String con el ID
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene la ciudad de origen
     * @return String con la ciudad de origen
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Obtiene la ciudad de destino
     * @return String con la ciudad de destino
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Obtiene el peso del envío
     * @return double con el peso en kg
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Obtiene la descripción del contenido
     * @return String con la descripción
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la fecha de creación
     * @return LocalDateTime con la fecha de creación
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Obtiene el estado actual
     * @return EstadoPedido actual
     */
    public EstadoPedido getEstado() {
        return estado;
    }

    /**
     * Establece el estado del pedido
     * @param estado Nuevo estado
     */
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el costo del envío
     * @return double con el costo
     */
    public double getCosto() {
        return costo;
    }

    /**
     * Establece el costo del envío
     * @param costo Nuevo costo
     */
    public void setCosto(double costo) {
        this.costo = costo;
    }

    /**
     * Obtiene la fecha estimada de entrega
     * @return LocalDateTime con la fecha estimada
     */
    public LocalDateTime getFechaEstimadaEntrega() {
        return fechaEstimadaEntrega;
    }

    /**
     * Establece la fecha estimada de entrega
     * @param fechaEstimadaEntrega Nueva fecha estimada
     */
    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) {
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }

    /**
     * Obtiene la fecha real de entrega
     * @return LocalDateTime con la fecha real
     */
    public LocalDateTime getFechaEntregaReal() {
        return fechaEntregaReal;
    }

    /**
     * Establece la fecha real de entrega
     * @param fechaEntregaReal Nueva fecha real
     */
    public void setFechaEntregaReal(LocalDateTime fechaEntregaReal) {
        this.fechaEntregaReal = fechaEntregaReal;
    }

    /**
     * Procesa el pedido según su estado actual
     */
    public void procesar() {
        estado.procesar(this);
    }

    /**
     * Marca el pedido como en tránsito
     */
    public void marcarEnTransito() {
        estado.marcarEnTransito(this);
    }

    /**
     * Marca el pedido como retrasado
     */
    public void marcarRetrasado() {
        estado.marcarRetrasado(this);
    }

    /**
     * Marca el pedido como entregado
     */
    public void marcarEntregado() {
        estado.marcarEntregado(this);
    }

    /**
     * Obtiene el nombre del estado actual
     * @return String con el nombre del estado
     */
    public String getNombreEstado() {
        return estado.getNombreEstado();
    }
} 