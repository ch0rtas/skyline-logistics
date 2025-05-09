package state;

import game.Pedido;
import game.JuegoLogistica;
import game.PedidoUtils;

public class PedidoPendienteState implements PedidoState {
    @Override
    public void procesarPedido(Pedido pedido, JuegoLogistica juego) {
        pedido.setEstado(new PedidoEnProcesoState());
        juego.getPedidosEnCurso().add(pedido);
        juego.getPedidosPendientes().remove(pedido);
    }

    @Override
    public void cancelarPedido(Pedido pedido, JuegoLogistica juego) {
        int multa = PedidoUtils.calcularMultaRechazo(pedido);
        juego.getJugador().gastar(multa);
        juego.incrementarGastosAcumulados(multa);
        juego.getPedidosPendientes().remove(pedido);
    }

    @Override
    public void completarPedido(Pedido pedido, JuegoLogistica juego) {
        // No se puede completar un pedido pendiente
        throw new IllegalStateException("No se puede completar un pedido pendiente");
    }

    @Override
    public String getEstado() {
        return "PENDIENTE";
    }
} 