package strategy;

import game.Pedido;
import game.Jugador;
import decorator.IVehiculo;
import java.util.List;
import java.util.Calendar;

public interface ProcesamientoPedidoStrategy {
    void procesarPedido(Pedido pedido, List<IVehiculo> flota, Calendar fechaActual, 
                       String almacenPrincipal, Jugador jugador, int[] estadisticas);
} 