package game;

import java.util.List;

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

    /**
     * Calcula la multa por rechazar un pedido
     * @param pedido Pedido a rechazar
     * @return int con el monto de la multa
     */
    public static int calcularMultaRechazo(Pedido pedido) {
        int multaBase = (int) (pedido.getPago() * 0.5); // 50% del pago como multa base
        if (pedido.getPrioridad().equals("URGENTE")) {
            multaBase *= 2; // Doble multa para pedidos urgentes
        }
        return multaBase;
    }

    /**
     * Calcula la cantidad de pedidos según la dificultad
     * @param dificultad Nivel de dificultad
     * @param diaActual Día actual del juego
     * @return int con la cantidad de pedidos
     */
    public static int calcularCantidadPedidos(String dificultad, int diaActual) {
        int base;
        switch (dificultad) {
            case "easy":
                base = 2;
                return base + (diaActual / 5); // Aumenta 1 cada 5 días
            case "medium":
                base = 3;
                return base + (diaActual / 3); // Aumenta 1 cada 3 días
            case "hard":
                base = 4;
                return base + (diaActual / 2); // Aumenta 1 cada 2 días
            default:
                return 1;
        }
    }
}