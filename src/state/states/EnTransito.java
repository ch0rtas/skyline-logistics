package state.states;

import state.core.EstadoPedido;
import domain.model.pedido.Pedido;
import state.states.Entregado;
import state.states.Retrasado;

/**
 * Implementación del estado EnTransito para los pedidos.
 * Representa el estado cuando un pedido está siendo transportado.
 */
public class EnTransito implements EstadoPedido {
    @Override
    public void procesar(Pedido pedido) {
        System.out.println("Procesando pedido " + pedido.getId() + " en estado EnTransito");
        // Aquí se realizaría el seguimiento del envío
    }

    @Override
    public void marcarEnTransito(Pedido pedido) {
        System.out.println("El pedido ya está en tránsito");
    }

    @Override
    public void marcarRetrasado(Pedido pedido) {
        System.out.println("Marcando pedido " + pedido.getId() + " como Retrasado");
        pedido.setEstado(new Retrasado());
    }

    @Override
    public void marcarEntregado(Pedido pedido) {
        System.out.println("Marcando pedido " + pedido.getId() + " como Entregado");
        pedido.setEstado(new Entregado());
        pedido.setFechaEntregaReal(java.time.LocalDateTime.now());
    }

    @Override
    public String getNombreEstado() {
        return "En Tránsito";
    }
} 