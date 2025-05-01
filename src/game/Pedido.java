package game;

import java.util.Calendar;
import java.text.SimpleDateFormat;

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
    private String tipoPaquete; // NORMAL, REFRIGERADO, CONGELADO, ESCOLTADO, PELIGROSO, FRÁGIL
    private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yy");

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
        this.tipoPaquete = tipoPaquete;
    }

    /**
     * Obtiene el ID del pedido
     * @return String con el ID
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el nombre del cliente
     * @return String con el nombre
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Obtiene el tipo de carga
     * @return String con la carga
     */
    public String getCarga() {
        return carga;
    }

    /**
     * Obtiene la prioridad del pedido
     * @return String con la prioridad
     */
    public String getPrioridad() {
        return prioridad;
    }

    /**
     * Obtiene el monto ofrecido
     * @return int con el monto
     */
    public int getPago() {
        return pago;
    }

    /**
     * Obtiene los días restantes para la entrega
     * @return int con los días restantes
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
     * Obtiene la ciudad de destino
     * @return String con el destino
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Obtiene la bonificación por día de adelanto
     * @return int con la bonificación
     */
    public int getBonificacionPorDia() {
        return bonificacionPorDia;
    }

    /**
     * Obtiene la multa por día de retraso
     * @return int con la multa
     */
    public int getMultaPorDia() {
        return multaPorDia;
    }

    /**
     * Obtiene el transporte asignado
     * @return String con el transporte
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
     * @return int con el pago final
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
     * @return int con los días de entrega
     */
    public int getDiasEntrega() {
        return diasEntrega;
    }

    /**
     * Obtiene la fecha de entrega
     * @return String con la fecha formateada
     */
    public String getFechaEntrega() {
        return formatoFecha.format(fechaEntrega.getTime());
    }

    /**
     * Verifica si el pedido está retrasado respecto a la fecha actual
     * @param fechaActual Fecha actual del juego
     * @return true si está retrasado, false si no
     */
    public boolean estaRetrasado(Calendar fechaActual) {
        return fechaActual.after(fechaEntrega);
    }

    /**
     * Calcula los días de retraso
     * @param fechaActual Fecha actual del juego
     * @return int con los días de retraso (0 si no hay retraso)
     */
    public int calcularDiasRetraso(Calendar fechaActual) {
        if (!estaRetrasado(fechaActual)) {
            return 0;
        }
        long diff = fechaActual.getTimeInMillis() - fechaEntrega.getTimeInMillis();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    /**
     * Obtiene el peso del pedido
     * @return int con el peso en kg
     */
    public int getPeso() {
        return peso;
    }

    /**
     * Obtiene el tipo de paquete
     * @return String con el tipo de paquete
     */
    public String getTipoPaquete() {
        return tipoPaquete;
    }

    /**
     * Devuelve una representación formateada del pedido
     * @return String con la representación formateada
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
} 