package state.core;

import domain.model.pedido.Pedido;

/**
 * Interfaz que define el contrato para los estados de un pedido.
 * Implementa el patrón State para gestionar el ciclo de vida de los pedidos.
 */
public interface EstadoPedido {
    /**
     * Procesa un nuevo pedido
     * @param pedido El pedido a procesar
     */
    void procesar(Pedido pedido);

    /**
     * Marca el pedido como en tránsito
     * @param pedido El pedido a marcar
     */
    void marcarEnTransito(Pedido pedido);

    /**
     * Marca el pedido como retrasado
     * @param pedido El pedido a marcar
     */
    void marcarRetrasado(Pedido pedido);

    /**
     * Marca el pedido como entregado
     * @param pedido El pedido a marcar
     */
    void marcarEntregado(Pedido pedido);

    /**
     * Obtiene el nombre del estado actual
     * @return String con el nombre del estado
     */
    String getNombreEstado();
} 