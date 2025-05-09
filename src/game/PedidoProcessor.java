package game;

import strategy.ProcesamientoPedidoStrategy;
import strategy.ProcesamientoNormalStrategy;
import strategy.ProcesamientoUrgenteStrategy;
import decorator.IVehiculo;
import state.PedidoEnProcesoState;
import state.PedidoCompletadoState;
import state.PedidoCanceladoState;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class PedidoProcessor {
    private List<Pedido> pedidosEnCurso;
    private Calendar fechaActual;
    private List<IVehiculo> flota;
    private Jugador jugador;
    private int[] estadisticas;
    private Random random;
    private String dificultad;
    private JuegoLogistica juego;
    private ProcesamientoPedidoStrategy estrategiaProcesamiento;

    public PedidoProcessor(List<Pedido> pedidosEnCurso, Calendar fechaActual, List<IVehiculo> flota, 
                          Jugador jugador, int[] estadisticas, Random random, String dificultad, JuegoLogistica juego) {
        this.pedidosEnCurso = pedidosEnCurso;
        this.fechaActual = fechaActual;
        this.flota = flota;
        this.jugador = jugador;
        this.estadisticas = estadisticas;
        this.random = random;
        this.dificultad = dificultad;
        this.juego = juego;
        this.estrategiaProcesamiento = crearEstrategia();
    }

    private ProcesamientoPedidoStrategy crearEstrategia() {
        if (dificultad.equalsIgnoreCase("facil")) {
            return new ProcesamientoNormalStrategy(juego);
        } else {
            return new ProcesamientoUrgenteStrategy(juego);
        }
    }

    public void procesarPedidos() {
        List<Pedido> pedidosCompletados = new ArrayList<>();
        
        for (Pedido pedido : pedidosEnCurso) {
            if (pedido.getEstado() instanceof PedidoEnProcesoState) {
                estrategiaProcesamiento.procesarPedido(pedido, flota, fechaActual, 
                    juego.getAlmacenPrincipal(), jugador, estadisticas);
                
                if (pedido.getEstado() instanceof PedidoCompletadoState || 
                    pedido.getEstado() instanceof PedidoCanceladoState) {
                    pedidosCompletados.add(pedido);
                }
            }
        }
        
        // Eliminar pedidos completados
        pedidosEnCurso.removeAll(pedidosCompletados);
    }

    public void setProcesamientoStrategy(ProcesamientoPedidoStrategy strategy) {
        this.estrategiaProcesamiento = strategy;
    }
}