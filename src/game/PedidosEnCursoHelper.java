package game;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import static game.JuegoLogistica.formatoFecha;
import static game.JuegoLogistica.generarFilaTabla;
import static game.JuegoLogistica.generarLineaSeparadora;

public class PedidosEnCursoHelper {
    public static void mostrarPedidosEnCurso(List<Pedido> pedidosEnCurso, Calendar fechaActual) {
        if (pedidosEnCurso == null || pedidosEnCurso.isEmpty()) {
            System.out.println("\n📦 No hay pedidos en curso");
            return;
        }

        // Filtrar solo los pedidos que están en curso
        List<Pedido> pedidosActivos = pedidosEnCurso.stream()
            .filter(pedido -> pedido.getEstado().equals("EN CURSO"))
            .toList();

        if (pedidosActivos.isEmpty()) {
            System.out.println("\n📦 No hay pedidos en curso");
            return;
        }

        // Calcular anchos máximos para cada columna
        String[] encabezados = {"ID", "CLIENTE", "CARGA", "PRIORIDAD", "PESO", "DESTINO", "TIPO", "PAGO", "ENTREGA MÁXIMA", "ENTREGA PREVISTA"};
        int[] anchos = new int[encabezados.length];

        // Inicializar anchos con los encabezados
        for (int i = 0; i < encabezados.length; i++) {
            anchos[i] = encabezados[i].length();
        }

        // Calcular anchos máximos basados en el contenido
        for (Pedido pedido : pedidosActivos) {
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntregaOriginal(),
                pedido.getFechaEntrega()
            };

            for (int i = 0; i < valores.length; i++) {
                anchos[i] = Math.max(anchos[i], valores[i].length());
            }
        }

        // Mostrar tabla
        System.out.println("\n📦 PEDIDOS EN CURSO:");

        // Mostrar encabezados
        System.out.println(generarFilaTabla(encabezados, anchos));
        System.out.println(generarLineaSeparadora(anchos));

        // Mostrar datos
        for (Pedido pedido : pedidosActivos) {
            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntregaOriginal(),
                pedido.getFechaEntrega()
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }
    }
}