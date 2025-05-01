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

    /**
     * Constructor de la clase Pedido
     * @param id Identificador del pedido
     * @param cliente Nombre del cliente
     * @param carga Tipo de carga
     * @param prioridad Nivel de prioridad
     * @param pago Monto ofrecido
     */
    public Pedido(String id, String cliente, String carga, String prioridad, int pago) {
        this.id = id;
        this.cliente = cliente;
        this.carga = carga;
        this.prioridad = prioridad;
        this.pago = pago;
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
} 