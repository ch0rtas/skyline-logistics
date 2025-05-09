package game;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import state.PedidoState;
import state.PedidoPendienteState;

/**
 * Clase que representa un pedido en el juego
 */
public class Pedido {
    private String id;
    private String cliente;
    private String carga;
    private String prioridad;
    private int pago;
    private int diasEntrega;
    private int diasRestantes;
    private String destino;
    private int peso; // en kg
    private int bonificacionPorDia;
    private int multaPorDia;
    private String transporteAsignado;
    private Calendar fechaEntrega;
    private Calendar fechaEntregaOriginal;
    private String tipoPaquete; // NORMAL, REFRIGERADO, CONGELADO, ESCOLTADO, PELIGROSO, FRÁGIL
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");
    private Calendar fechaDisponible;
    private PedidoState estado;
    private String origen;

    /**
     * Constructor de la clase Pedido
     * @param id Identificador del pedido
     * @param cliente Nombre del cliente
     * @param carga Tipo de carga
     * @param prioridad Nivel de prioridad
     * @param pago Monto ofrecido
     * @param diasEntrega Días necesarios para la entrega
     * @param destino Ciudad de destino
     * @param fechaEntrega Fecha límite de entrega
     * @param peso Peso del pedido en kg
     * @param tipoPaquete Tipo de paquete
     */
    public Pedido(String id, String cliente, String carga, String prioridad, int pago, int diasEntrega, String destino, Calendar fechaEntrega, int peso, String tipoPaquete) {
        this.id = id;
        this.cliente = cliente;
        this.carga = carga;
        this.prioridad = prioridad;
        this.pago = pago;
        this.diasEntrega = diasEntrega;
        this.diasRestantes = diasEntrega;
        this.destino = destino;
        this.peso = peso;
        this.bonificacionPorDia = pago / 10;
        this.multaPorDia = pago / 5;
        this.transporteAsignado = "No asignado";
        this.fechaEntrega = fechaEntrega;
        this.fechaEntregaOriginal = (Calendar) fechaEntrega.clone();
        this.tipoPaquete = tipoPaquete;
        this.estado = new PedidoPendienteState();
    }

    /**
     * Obtiene el ID del pedido
     * @return String with the ID
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del cliente
     * @return String with the name
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Obtiene el tipo de carga
     * @return String with the load type
     */
    public String getCarga() {
        return carga;
    }

    /**
     * Obtiene la prioridad del pedido
     * @return String with the priority
     */
    public String getPrioridad() {
        return prioridad;
    }

    /**
     * Obtiene el monto ofrecido
     * @return int with the amount
     */
    public int getPago() {
        return pago;
    }

    /**
     * Establece el monto del pago
     * @param pago Nuevo monto del pago
     */
    public void setPago(int pago) {
        this.pago = pago;
    }

    /**
     * Obtiene los días restantes para la entrega
     * @return int with the remaining days
     */
    public int getDiasRestantes() {
        return diasRestantes;
    }

    /**
     * Reduce en uno los días restantes para la entrega
     */
    public void reducirDiasRestantes() {
        if (diasRestantes > 0) {
            diasRestantes--;
        }
    }

    /**
     * Incrementa los días restantes para la entrega
     * @param dias Número de días a incrementar
     */
    public void incrementarDiasRestantes(int dias) {
        this.diasRestantes += dias;
    }

    /**
     * Establece los días restantes para la entrega
     * @param diasRestantes Nuevo número de días restantes
     */
    public void setDiasRestantes(int diasRestantes) {
        this.diasRestantes = diasRestantes;
    }

    /**
     * Obtiene la ciudad de destino
     * @return String with the destination
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Obtiene la bonificación por día de adelanto
     * @return int with the bonus
     */
    public int getBonificacionPorDia() {
        return bonificacionPorDia;
    }

    /**
     * Obtiene la multa por día de retraso
     * @return int with the penalty
     */
    public int getMultaPorDia() {
        return multaPorDia;
    }

    /**
     * Obtiene el transporte asignado
     * @return String with the transport
     */
    public String getTransporteAsignado() {
        return transporteAsignado;
    }

    /**
     * Asigna un transporte al pedido
     * @param transporte Nombre del transporte
     */
    public void setTransporteAsignado(String transporte) {
        this.transporteAsignado = transporte;
    }

    /**
     * Calcula el pago final considerando bonificaciones y multas
     * @return int with the final amount
     */
    public int calcularPagoFinal() {
        int diasAdelanto = diasEntrega - diasRestantes;
        if (diasAdelanto > 0) {
            return pago + (diasAdelanto * bonificacionPorDia);
        } else if (diasAdelanto < 0) {
            return pago - (Math.abs(diasAdelanto) * multaPorDia);
        }
        return pago;
    }

    /**
     * Obtiene los días de entrega asignados
     * @return int with the delivery days
     */
    public int getDiasEntrega() {
        return diasEntrega;
    }

    /**
     * Obtiene la fecha de entrega
     * @return String with the formatted date
     */
    public String getFechaEntrega() {
        return formatoFecha.format(fechaEntrega.getTime());
    }

    /**
     * Verifies if the order is delayed with respect to the current date
     * @param fechaActual Current date of the game
     * @return true if delayed, false if not
     */
    public boolean estaRetrasado(Calendar fechaActual) {
        return fechaActual.after(fechaEntrega);
    }

    /**
     * Calculates the delay days
     * @param fechaActual Current date of the game
     * @return int with the delay days (0 if no delay)
     */
    public int calcularDiasRetraso(Calendar fechaActual) {
        if (!estaRetrasado(fechaActual)) {
            return 0;
        }
        long diff = fechaActual.getTimeInMillis() - fechaEntrega.getTimeInMillis();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    // Overloaded method to calculate days of delay between two dates
    public static int calcularDiasRetraso(Calendar fechaInicio, Calendar fechaFin) {
        long diff = fechaFin.getTimeInMillis() - fechaInicio.getTimeInMillis();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    /**
     * Obtiene el peso del pedido
     * @return int with the weight in kg
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Obtiene el tipo de paquete
     * @return String with the package type
     */
    public String getTipoPaquete() {
        return tipoPaquete;
    }

    /**
     * Returns a formatted representation of the order
     * @return String with the formatted representation
     */
    public String toStringFormateado() {
        return String.format("%-8s | %-20s | %-15s | %-10s | %-10s | %-15s | %-12s | %-10s | %-10s",
            id,
            cliente,
            carga,
            prioridad,
            peso + " kg",
            destino,
            tipoPaquete,
            "Pago: $" + pago,
            "Entrega: " + getFechaEntrega()
        );
    }

    /**
     * Gets the delivery date as Calendar
     * @return Calendar with the delivery date
     */
    public Calendar getFechaEntregaCalendar() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Calendar fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public Calendar getFechaDisponible() {
        return fechaDisponible;
    }

    public void setFechaDisponible(Calendar fechaDisponible) {
        this.fechaDisponible = fechaDisponible;
    }

    /**
     * Obtiene el estado actual del pedido
     * @return PedidoState con el estado actual
     */
    public PedidoState getEstado() {
        return estado;
    }

    /**
     * Establece el estado del pedido
     * @param estado Nuevo estado
     */
    public void setEstado(PedidoState estado) {
        this.estado = estado;
    }

    /**
     * Procesa el pedido según su estado actual
     * @param juego Instancia del juego
     */
    public void procesarPedido(JuegoLogistica juego) {
        estado.procesarPedido(this, juego);
    }

    /**
     * Cancela el pedido según su estado actual
     * @param juego Instancia del juego
     */
    public void cancelarPedido(JuegoLogistica juego) {
        estado.cancelarPedido(this, juego);
    }

    /**
     * Completa el pedido según su estado actual
     * @param juego Instancia del juego
     */
    public void completarPedido(JuegoLogistica juego) {
        estado.completarPedido(this, juego);
    }

    public String getFechaEntregaOriginal() {
        return formatoFecha.format(fechaEntregaOriginal.getTime());
    }

    public Calendar getFechaEntregaOriginalCalendar() {
        return fechaEntregaOriginal;
    }

    public int getBeneficio() {
        return pago;
    }

    public String getOrigen() {
        return origen;
    }
}