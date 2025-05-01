package state.states;

import state.core.EstadoPedido;
import domain.model.pedido.Pedido;
import state.states.Entregado;

/**
 * Implementación del estado Retrasado para los pedidos.
 * Representa el estado cuando un pedido ha sufrido un retraso en su entrega.
 */
public class Retrasado implements EstadoPedido {
    @Override
    public void procesar(Pedido pedido) {
        System.out.println("Procesando pedido " + pedido.getId() + " en estado Retrasado");
        // Aquí se implementaría la lógica para intentar recuperar el retraso
    }

    @Override
    public void marcarEnTransito(Pedido pedido) {
        System.out.println("No se puede marcar como en tránsito un pedido retrasado");
    }

    @Override
    public void marcarRetrasado(Pedido pedido) {
        System.out.println("El pedido ya está marcado como retrasado");
    }

    @Override
    public void marcarEntregado(Pedido pedido) {
        System.out.println("Marcando pedido " + pedido.getId() + " como Entregado");
        pedido.setEstado(new Entregado());
        pedido.setFechaEntregaReal(java.time.LocalDateTime.now());
    }

    @Override
    public String getNombreEstado() {
        return "Retrasado";
    }
} 