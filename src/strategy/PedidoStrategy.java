package strategy;

import game.Pedido;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

public interface PedidoStrategy {
    void generarPedidos(String dificultad, int diaActual, List<Pedido> pedidosPendientes, 
                       Map<String, Pedido> pedidos, Calendar fechaActual);
} 