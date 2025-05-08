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
    private String dificultad; // Added dificultad field

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
        this.dificultad = dificultad; // Initialize dificultad
    }

    private void procesarPedidoCompletado(Pedido pedido, int pagoOriginal, int multa, int ganancia, boolean exito, String mensaje) {
        System.out.println(mensaje);
        System.out.println("💰 Pago original: $" + pagoOriginal);
        System.out.println("💰 Multa por retraso: $" + multa);
        System.out.println("💰 Ganancia final: $" + ganancia);

        if (exito) {
            enviosExitosos++;
            jugador.recuperarBalance(ganancia);
            beneficiosAcumulados += ganancia;
        } else {
            satisfaccionClientes -= 10;
        }

        String idVehiculo = pedido.getTransporteAsignado().split(" ")[1];
        Vehiculo vehiculo = flota.stream()
                .filter(v -> v.getId().equals(idVehiculo))
                .findFirst()
                .orElse(null);

        if (vehiculo != null) {
            vehiculo.asignarPedido(null);

            // Reduce health based on the wear percentage
            int desgaste = vehiculo.getDesgastePorViaje();
            vehiculo.reducirSalud(desgaste);

            // Set availability to one day after delivery
            Calendar fechaDisponible = (Calendar) pedido.getFechaEntregaCalendar().clone();
            fechaDisponible.add(Calendar.DAY_OF_MONTH, 1);
            pedido.setFechaDisponible(fechaDisponible);
        }

        enviosTotales++;
    }

    public void procesarPedidosEnCurso() {
        List<Pedido> pedidosCompletados = new ArrayList<>();

        for (Pedido pedido : pedidosEnCurso) {
            pedido.reducirDiasRestantes();

            if (pedido.getDiasRestantes() <= 0) {
                int pagoOriginal = pedido.getPago();
                int multa = 0;
                int ganancia = pagoOriginal;
                boolean exito = true;
                String mensaje = "";

                Calendar fechaLlegada = (Calendar) fechaActual.clone();
                Calendar fechaEntrega = pedido.getFechaEntregaCalendar();
                int diasRetraso = Pedido.calcularDiasRetraso(fechaLlegada, fechaEntrega);

                if (diasRetraso > 0) {
                    multa = diasRetraso * pedido.getMultaPorDia();
                    ganancia = pagoOriginal - multa;
                    mensaje = "⚠️ Envío retrasado " + diasRetraso + " días";
                    exito = false;
                } else {
                    int diasAdelanto = pedido.getDiasEntrega() - pedido.getDiasRestantes();
                    if (diasAdelanto > 0) {
                        ganancia = pagoOriginal + (int) (diasAdelanto * pedido.getBonificacionPorDia() * (dificultad.equals("hard") ? 1.5 : 1.2)); // Explicit cast to int
                        mensaje = "✅ Envío completado con " + diasAdelanto + " días de adelanto";
                    } else {
                        mensaje = "✅ Envío completado a tiempo";
                    }
                }

                procesarPedidoCompletado(pedido, pagoOriginal, multa, ganancia, exito, mensaje);
                pedidosCompletados.add(pedido);
            }
        }

        pedidosEnCurso.removeAll(pedidosCompletados);
    }
}