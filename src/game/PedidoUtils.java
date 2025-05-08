package game;

import java.util.List;
import java.util.Map;

public class PedidoUtils {
    public static void mostrarPedidosPendientes(List<Pedido> pedidosPendientes) {
        if (pedidosPendientes.isEmpty()) {
            System.out.println("\n📭 No hay pedidos pendientes");
            return;
        }

        // Calcular anchos máximos para cada columna
        String[] encabezados = {"ID", "CLIENTE", "CARGA", "PRIORIDAD", "PESO", "DESTINO", "TIPO", "PAGO", "ENTREGA"};
        int[] anchos = new int[encabezados.length];

        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }

        // Calcular anchos máximos basados en el contenido
        for (Pedido pedido : pedidosPendientes) {
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega()
            };

            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println("\n📦 PEDIDOS PENDIENTES:");

        // Mostrar encabezados
        System.out.println(JuegoLogistica.generarFilaTabla(encabezados, anchos));
        System.out.println(JuegoLogistica.generarLineaSeparadora(anchos));

        // Mostrar datos
        for (Pedido pedido : pedidosPendientes) {
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega()
            };
            System.out.println(JuegoLogistica.generarFilaTabla(valores, anchos));
        }
    }
}