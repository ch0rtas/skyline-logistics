package state;

import game.Pedido;
import game.JuegoLogistica;

public class PedidoCompletadoState implements PedidoState {
    @Override
    public void procesarPedido(Pedido pedido, JuegoLogistica juego) {
        throw new IllegalStateException("No se puede procesar un pedido completado");
    }

    @Override
    public void cancelarPedido(Pedido pedido, JuegoLogistica juego) {
        throw new IllegalStateException("No se puede cancelar un pedido completado");
    }

    @Override
    public void completarPedido(Pedido pedido, JuegoLogistica juego) {
        throw new IllegalStateException("El pedido ya está completado");
    }

    @Override
    public String getEstado() {
        return "COMPLETADO";
    }
} 