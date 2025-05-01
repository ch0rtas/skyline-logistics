package decorator.decorators;

import decorator.base.Envio;

/**
 * Clase base abstracta para los decoradores de envío.
 * Implementa el patrón Decorator para añadir funcionalidades adicionales a los envíos.
 */
public abstract class EnvioDecorator extends Envio {
    protected Envio envio;

    /**
     * Constructor de la clase EnvioDecorator
     * @param envio Envío a decorar
     */
    public EnvioDecorator(Envio envio) {
        super(envio.getOrigen(), envio.getDestino(), envio.getPeso(), envio.getCostoBase());
        this.envio = envio;
    }

    @Override
    public double getCosto() {
        return envio.getCosto();
    }

    @Override
    public int getTiempoEstimado() {
        return envio.getTiempoEstimado();
    }

    @Override
    public String getDescripcion() {
        return envio.getDescripcion();
    }
} 