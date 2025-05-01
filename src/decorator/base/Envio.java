package decorator.base;

/**
 * Clase base para el patrón Decorator que representa un envío básico.
 * Define la interfaz común para todos los envíos y sus decoradores.
 */
public abstract class Envio {
    protected String origen;
    protected String destino;
    protected double peso;
    protected double costoBase;

    /**
     * Constructor de la clase Envio
     * @param origen Ciudad de origen
     * @param destino Ciudad de destino
     * @param peso Peso del envío en kg
     * @param costoBase Costo base del envío
     */
    public Envio(String origen, String destino, double peso, double costoBase) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.costoBase = costoBase;
    }

    /**
     * Obtiene el costo total del envío
     * @return double con el costo total
     */
    public abstract double getCosto();

    /**
     * Obtiene el tiempo estimado de entrega en días
     * @return int con el tiempo estimado
     */
    public abstract int getTiempoEstimado();

    /**
     * Obtiene la descripción del envío
     * @return String con la descripción
     */
    public abstract String getDescripcion();

    /**
     * Obtiene la ciudad de origen
     * @return String con la ciudad de origen
     */
    public String getOrigen() {
        return origen;
    }

    /**
     * Obtiene la ciudad de destino
     * @return String con la ciudad de destino
     */
    public String getDestino() {
        return destino;
    }

    /**
     * Obtiene el peso del envío
     * @return double con el peso en kg
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Obtiene el costo base del envío
     * @return double con el costo base
     */
    public double getCostoBase() {
        return costoBase;
    }
} 