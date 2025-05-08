package game;

import java.util.List;
import java.util.Map;

public class PedidoGeneratorHelper {
    public static void generarPedidosDia(String dificultad, int diaActual, List<Pedido> pedidosPendientes, Map<String, Pedido> pedidos, PedidoGenerator pedidoGenerator) {
        int cantidadPedidos = PedidoUtils.calcularCantidadPedidos(dificultad, diaActual);
        pedidosPendientes.clear();

        for (int i = 0; i < cantidadPedidos; i++) {
            Pedido pedido = pedidoGenerator.generarPedidoAleatorio();
            pedidosPendientes.add(pedido);
            pedidos.put(pedido.getId(), pedido);
        }

        System.out.println("\n📦 Han entrado " + cantidadPedidos + " paquetes nuevos!");
    }
}