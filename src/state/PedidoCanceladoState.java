package state;

import game.Pedido;
import game.JuegoLogistica;

public class PedidoCanceladoState implements PedidoState {
    @Override
    public void procesarPedido(Pedido pedido, JuegoLogistica juego) {
        throw new IllegalStateException("No se puede procesar un pedido cancelado");
    }

    @Override
    public void cancelarPedido(Pedido pedido, JuegoLogistica juego) {
        throw new IllegalStateException("El pedido ya está cancelado");
    }

    @Override
    public void completarPedido(Pedido pedido, JuegoLogistica juego) {
        throw new IllegalStateException("No se puede completar un pedido cancelado");
    }

    @Override
    public String getEstado() {
        return "CANCELADO";
    }
} 