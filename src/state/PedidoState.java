package state;

import game.Pedido;
import game.JuegoLogistica;

public interface PedidoState {
    void procesarPedido(Pedido pedido, JuegoLogistica juego);
    void cancelarPedido(Pedido pedido, JuegoLogistica juego);
    void completarPedido(Pedido pedido, JuegoLogistica juego);
    String getEstado();
} 