package state.states;

import state.core.EstadoPedido;
import domain.model.pedido.Pedido;
import java.io.Serializable;

/**
 * Implementación del estado Entregado para los pedidos.
 * Representa el estado final cuando un pedido ha sido entregado.
 */
public class Entregado implements EstadoPedido, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void procesar(Pedido pedido) {
        System.out.println("El pedido " + pedido.getId() + " ya ha sido entregado");
    }

    @Override
    public void marcarEnTransito(Pedido pedido) {
        System.out.println("No se puede marcar como en tránsito un pedido entregado");
    }

    @Override
    public void marcarRetrasado(Pedido pedido) {
        System.out.println("No se puede marcar como retrasado un pedido entregado");
    }

    @Override
    public void marcarEntregado(Pedido pedido) {
        System.out.println("El pedido ya está marcado como entregado");
    }

    @Override
    public String getNombreEstado() {
        return "Entregado";
    }
}