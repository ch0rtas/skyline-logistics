package game;

import strategy.ProcesamientoPedidoStrategy;
import strategy.ProcesamientoNormalStrategy;
import strategy.ProcesamientoUrgenteStrategy;
import decorator.IVehiculo;
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
            if (pedido.getEstado().equals("EN_CURSO")) {
                estrategiaProcesamiento.procesarPedido(pedido, flota, fechaActual, 
                    juego.getAlmacenPrincipal(), jugador, estadisticas);
                
                if (pedido.getEstado().equals("ENTREGADO") || pedido.getEstado().equals("FALLIDO")) {
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

    private void procesarPedidoCompletado(Pedido pedido, int pagoOriginal, int multa, int ganancia, boolean exito, String mensaje, JuegoLogistica juego) {
        // Actualizar estadísticas
        estadisticas[1]++;
        if (exito) {
            estadisticas[0]++;
        } else {
            // Ensure 'Envíos fallidos' does not go below zero
            estadisticas[3] = Math.max(0, estadisticas[3] - 2);
        }

        // Mostrar mensaje de completado
        System.out.println("\n" + mensaje);
        if (multa > 0) {
            System.out.println("💸 Multa aplicada: $" + multa);
        }

        String idVehiculo = pedido.getTransporteAsignado().split(" ")[1];
        IVehiculo vehiculo = flota.stream()
                .filter(v -> v.getId().equals(idVehiculo))
                .findFirst()
                .orElse(null);

        if (vehiculo != null) {
            // Aplicar desgaste al vehículo
            vehiculo.aplicarDesgaste();

            // Descontar el costo del vehículo asignado del balance del jugador
            int costoTotal = vehiculo.getPrecio();
            jugador.gastar(costoTotal);
            System.out.println("💰 Se ha descontado el costo del vehículo: $" + costoTotal);

            // Procesar el pago solo cuando se alcanza la fecha de entrega
            Calendar fechaActual = juego.getFechaActual();
            Calendar fechaEntrega = pedido.getFechaEntregaCalendar();

            if (!fechaActual.before(fechaEntrega)) {
                if (multa > 0) {
                    jugador.gastar(multa);
                }
                jugador.recuperarBalance(ganancia);
                estadisticas[2] += ganancia;

                System.out.println("💰 Pago recibido: $" + ganancia);

                // Liberar el vehículo inmediatamente
                vehiculo.asignarPedido(null);
                System.out.println("🚗 Vehículo " + vehiculo.getId() + " liberado y disponible");
            }
        }
    }
}