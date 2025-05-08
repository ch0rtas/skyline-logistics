package game;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import static game.JuegoLogistica.formatoFecha;
import static game.JuegoLogistica.generarFilaTabla;
import static game.JuegoLogistica.generarLineaSeparadora;

public class PedidosEnCursoHelper {
    public static void mostrarPedidosEnCurso(List<Pedido> pedidosEnCurso, Calendar fechaActual) {
        if (pedidosEnCurso.isEmpty()) {
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
        for (Pedido pedido : pedidosEnCurso) {
            Calendar fechaPrevia = Calendar.getInstance();
            fechaPrevia.setTime(fechaActual.getTime());
            fechaPrevia.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());

            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega(),
                formatoFecha.format(fechaPrevia.getTime())
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
        for (Pedido pedido : pedidosEnCurso) {
            Calendar fechaPrevia = Calendar.getInstance();
            fechaPrevia.setTime(fechaActual.getTime());
            fechaPrevia.add(Calendar.DAY_OF_MONTH, pedido.getDiasRestantes());

            String[] valores = {
                pedido.getId(),
                pedido.getCliente(),
                pedido.getCarga(),
                pedido.getPrioridad(),
                String.valueOf(pedido.getPeso()),
                pedido.getDestino(),
                pedido.getTipoPaquete(),
                "$" + pedido.getPago(),
                pedido.getFechaEntrega(),
                formatoFecha.format(fechaPrevia.getTime())
            };
            System.out.println(generarFilaTabla(valores, anchos));
        }
    }
}