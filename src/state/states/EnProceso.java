package state.states;

import state.core.EstadoPedido;
import domain.model.pedido.Pedido;
import state.states.EnTransito;

import java.io.Serializable;

/**
 * Implementación del estado EnProceso para los pedidos.
 * Representa el estado inicial de un pedido cuando se crea.
 */
public class EnProceso implements EstadoPedido, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public void procesar(Pedido pedido) {
        System.out.println("Procesando pedido " + pedido.getId() + " en estado EnProceso");
        // Aquí se realizarían las validaciones iniciales
    }

    @Override
    public void marcarEnTransito(Pedido pedido) {
        System.out.println("Marcando pedido " + pedido.getId() + " como EnTránsito");
        pedido.setEstado(new EnTransito());
    }

    @Override
    public void marcarRetrasado(Pedido pedido) {
        System.out.println("No se puede marcar como retrasado un pedido en proceso");
    }

    @Override
    public void marcarEntregado(Pedido pedido) {
        System.out.println("No se puede marcar como entregado un pedido en proceso");
    }

    @Override
    public String getNombreEstado() {
        return "En Proceso";
    }
}