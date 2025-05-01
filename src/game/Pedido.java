package game;

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

    /**
     * Constructor de la clase Pedido
     * @param id Identificador del pedido
     * @param cliente Nombre del cliente
     * @param carga Tipo de carga
     * @param prioridad Nivel de prioridad
     * @param pago Monto ofrecido
     * @param diasEntrega Días necesarios para la entrega
     */
    public Pedido(String id, String cliente, String carga, String prioridad, int pago, int diasEntrega) {
        this.id = id;
        this.cliente = cliente;
        this.carga = carga;
        this.prioridad = prioridad;
        this.pago = pago;
        this.diasEntrega = diasEntrega;
        this.diasRestantes = diasEntrega;
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
} 