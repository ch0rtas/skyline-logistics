package state;

import game.Pedido;
import game.JuegoLogistica;

public class PedidoEnProcesoState implements PedidoState {
    @Override
    public void procesarPedido(Pedido pedido, JuegoLogistica juego) {
        // Ya está en proceso, no se puede procesar de nuevo
        throw new IllegalStateException("El pedido ya está en proceso");
    }

    @Override
    public void cancelarPedido(Pedido pedido, JuegoLogistica juego) {
        pedido.setEstado(new PedidoCanceladoState());
        juego.getPedidosEnCurso().remove(pedido);
        juego.incrementarEnviosFallidos();
    }

    @Override
    public void completarPedido(Pedido pedido, JuegoLogistica juego) {
        pedido.setEstado(new PedidoCompletadoState());
        juego.getPedidosEnCurso().remove(pedido);
        juego.incrementarEnviosExitosos();
        juego.getJugador().incrementarBalance(pedido.getPago());
    }

    @Override
    public String getEstado() {
        return "EN_PROCESO";
    }
} 