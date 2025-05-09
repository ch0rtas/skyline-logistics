package strategy;

import game.Pedido;
import game.Jugador;
import game.JuegoLogistica;
import decorator.IVehiculo;
import state.PedidoCanceladoState;
import state.PedidoCompletadoState;
import state.PedidoEnProcesoState;
import java.util.List;
import java.util.Calendar;

public class ProcesamientoUrgenteStrategy implements ProcesamientoPedidoStrategy {
    private JuegoLogistica juego;

    public ProcesamientoUrgenteStrategy(JuegoLogistica juego) {
        this.juego = juego;
    }

    @Override
    public void procesarPedido(Pedido pedido, List<IVehiculo> flota, Calendar fechaActual, 
                              String almacenPrincipal, Jugador jugador, int[] estadisticas) {
        if (pedido.getEstado() instanceof PedidoEnProcesoState) {
            // Obtener el vehículo asignado al pedido
            IVehiculo vehiculo = flota.stream()
                .filter(v -> v.getPedidoAsignado() != null && v.getPedidoAsignado().getId().equals(pedido.getId()))
                .findFirst()
                .orElse(null);

            if (vehiculo != null) {
                // Obtener la fecha estimada de llegada del vehículo
                Calendar fechaEstimadaLlegada = vehiculo.getFechaEstimadaLlegada();
                
                // Verificar si el vehículo ha llegado a su destino
                if (fechaEstimadaLlegada != null && !fechaActual.before(fechaEstimadaLlegada)) {
                    // Calcular retraso
                    int diasRetraso = 0;
                    if (fechaActual.after(fechaEstimadaLlegada)) {
                        diasRetraso = (int) ((fechaActual.getTimeInMillis() - fechaEstimadaLlegada.getTimeInMillis()) / (1000 * 60 * 60 * 24));
                    }

                    // Procesar pago y penalizaciones
                    int pagoBase = pedido.getPago();
                    int pagoFinal = pagoBase;

                    if (diasRetraso > 0) {
                        int penalizacion = (int) (pagoBase * (diasRetraso * 0.2)); // 20% por día de retraso
                        pagoFinal -= penalizacion;
                        System.out.println("\n⚠️ Pedido urgente entregado con " + diasRetraso + " días de retraso");
                        System.out.println("💸 Penalización aplicada: $" + penalizacion);
                        pedido.setEstado(new PedidoCanceladoState());
                        juego.incrementarEnviosFallidos();
                    } else {
                        int bonificacion = (int) (pagoBase * 0.2); // 20% de bonificación por entrega a tiempo
                        pagoFinal += bonificacion;
                        System.out.println("\n✅ Pedido urgente entregado a tiempo");
                        System.out.println("💰 Bonificación aplicada: $" + bonificacion);
                        pedido.setEstado(new PedidoCompletadoState());
                        juego.incrementarEnviosExitosos();
                    }

                    // Aplicar el pago
                    jugador.recuperarBalance(pagoFinal);
                    System.out.println("💵 Pago recibido: $" + pagoFinal);

                    // Aplicar desgaste al vehículo
                    vehiculo.aplicarDesgaste();

                    // Calcular la fecha de disponibilidad (el mismo día de la entrega)
                    Calendar fechaDisponibilidad = (Calendar) fechaEstimadaLlegada.clone();
                    vehiculo.setFechaDisponibilidad(fechaDisponibilidad);
                    
                    // Liberar el vehículo
                    vehiculo.asignarPedido(null);
                    System.out.println("🚗 Vehículo " + vehiculo.getId() + " liberado y disponible a partir del " + 
                        JuegoLogistica.formatoFecha.format(fechaDisponibilidad.getTime()));
                }
            }
        }
    }
} 