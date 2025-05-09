package strategy;

import game.Pedido;
import game.Vehiculo;
import game.Jugador;
import java.util.List;
import java.util.Calendar;

public interface ProcesamientoPedidoStrategy {
    void procesarPedido(Pedido pedido, List<Vehiculo> flota, Calendar fechaActual, 
                       String almacenPrincipal, Jugador jugador, int[] estadisticas);
} 