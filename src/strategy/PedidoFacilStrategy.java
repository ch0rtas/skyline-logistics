package strategy;

import game.Pedido;
import game.PedidoGenerator;
import game.PedidoUtils;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

public class PedidoFacilStrategy implements PedidoStrategy {
    private PedidoGenerator pedidoGenerator;

    public PedidoFacilStrategy(PedidoGenerator pedidoGenerator) {
        this.pedidoGenerator = pedidoGenerator;
    }

    @Override
    public void generarPedidos(String dificultad, int diaActual, List<Pedido> pedidosPendientes, 
                             Map<String, Pedido> pedidos, Calendar fechaActual) {
        int numPedidos = PedidoUtils.calcularCantidadPedidos(dificultad, diaActual);
        for (int i = 0; i < numPedidos; i++) {
            Pedido pedido = pedidoGenerator.generarPedidoAleatorio();
            pedidosPendientes.add(pedido);
            pedidos.put(pedido.getId(), pedido);
        }
    }
} 