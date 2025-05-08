package game;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class PedidoProcessor {
    private List<Pedido> pedidosEnCurso;
    private Calendar fechaActual;
    private List<Vehiculo> flota;
    private Jugador jugador;
    private int enviosExitosos;
    private int enviosTotales;
    private int beneficiosAcumulados;
    private int satisfaccionClientes;
    private Random random;
    private String dificultad;

    public PedidoProcessor(List<Pedido> pedidosEnCurso, Calendar fechaActual, List<Vehiculo> flota, Jugador jugador, int enviosExitosos, int enviosTotales, int beneficiosAcumulados, int satisfaccionClientes, Random random, String dificultad) {
        this.pedidosEnCurso = pedidosEnCurso;
        this.fechaActual = fechaActual;
        this.flota = flota;
        this.jugador = jugador;
        this.enviosExitosos = enviosExitosos;
        this.enviosTotales = enviosTotales;
        this.beneficiosAcumulados = beneficiosAcumulados;
        this.satisfaccionClientes = satisfaccionClientes;
        this.random = random;
        this.dificultad = dificultad;
    }

    public static void procesarPedidosEnCurso(JuegoLogistica juego) {
        Calendar fechaActual = juego.getFechaActual();
        List<Pedido> pedidosEnCurso = juego.getPedidosEnCurso();
        List<Pedido> pedidosCompletados = new ArrayList<>();
        Jugador jugador = juego.getJugador();
        List<Vehiculo> flota = juego.getFlota();

        for (Pedido pedido : pedidosEnCurso) {
            Calendar fechaEntrega = pedido.getFechaEntregaCalendar();

            // Verificar si el pedido está completado (fecha actual es igual o posterior a la fecha de entrega)
            if (!fechaActual.before(fechaEntrega)) {
                // Calcular retraso
                int diasRetraso = 0;
                if (fechaActual.after(fechaEntrega)) {
                    diasRetraso = (int) ((fechaActual.getTimeInMillis() - fechaEntrega.getTimeInMillis()) / (1000 * 60 * 60 * 24));
                }

                // Procesar pago y penalizaciones
                int pagoBase = pedido.getPago();
                int pagoFinal = pagoBase;

                if (diasRetraso > 0) {
                    int penalizacion = (int) (pagoBase * (diasRetraso * 0.1)); // 10% por día de retraso
                    pagoFinal -= penalizacion;
                    System.out.println("\n⚠️ Pedido entregado con " + diasRetraso + " días de retraso");
                    System.out.println("💸 Penalización aplicada: $" + penalizacion);
                } else {
                    int bonificacion = (int) (pagoBase * 0.1); // 10% de bonificación por entrega a tiempo
                    pagoFinal += bonificacion;
                    System.out.println("\n✅ Pedido entregado a tiempo");
                    System.out.println("💰 Bonificación aplicada: $" + bonificacion);
                }

                // Aplicar el pago
                jugador.recuperarBalance(pagoFinal);
                System.out.println("💵 Pago recibido: $" + pagoFinal);

                // Aplicar desgaste al vehículo
                Vehiculo vehiculo = flota.stream()
                    .filter(v -> v.getPedidoAsignado() != null && v.getPedidoAsignado().getId().equals(pedido.getId()))
                    .findFirst()
                    .orElse(null);

                if (vehiculo != null) {
                    vehiculo.aplicarDesgaste();
                    vehiculo.asignarPedido(null); // Liberar el vehículo
                    System.out.println("🚗 Vehículo " + vehiculo.getId() + " liberado y disponible");
                }

                // Incrementar envíos exitosos
                juego.incrementarEnviosExitosos();

                pedidosCompletados.add(pedido);
            }
        }

        // Remover pedidos completados
        pedidosEnCurso.removeAll(pedidosCompletados);
    }

    private void procesarPedidoCompletado(Pedido pedido, int pagoOriginal, int multa, int ganancia, boolean exito, String mensaje, JuegoLogistica juego) {
        // Actualizar estadísticas
        enviosTotales++;
        if (exito) {
            enviosExitosos++;
        } else {
            // Ensure 'Envíos fallidos' does not go below zero
            satisfaccionClientes = Math.max(0, satisfaccionClientes - 2);
        }

        // Mostrar mensaje de completado
        System.out.println("\n" + mensaje);
        if (multa > 0) {
            System.out.println("💸 Multa aplicada: $" + multa);
        }

        String idVehiculo = pedido.getTransporteAsignado().split(" ")[1];
        Vehiculo vehiculo = flota.stream()
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
                beneficiosAcumulados += ganancia;

                System.out.println("💰 Pago recibido: $" + ganancia);

                // Liberar el vehículo inmediatamente
                vehiculo.asignarPedido(null);
                System.out.println("🚗 Vehículo " + vehiculo.getId() + " liberado y disponible");
            }
        }
    }
}