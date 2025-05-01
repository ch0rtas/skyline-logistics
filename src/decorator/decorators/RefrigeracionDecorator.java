package decorator.decorators;

import decorator.base.Envio;

/**
 * Decorador que añade refrigeración a un envío.
 * Implementa el patrón Decorator para añadir la funcionalidad de refrigeración.
 */
public class RefrigeracionDecorator extends EnvioDecorator {
    private static final double PORCENTAJE_REFRIGERACION = 0.2; // 20% del valor del envío
    private static final int DIAS_EXTRA = 1; // Un día extra por refrigeración

    /**
     * Constructor de la clase RefrigeracionDecorator
     * @param envio Envío a decorar con refrigeración
     */
    public RefrigeracionDecorator(Envio envio) {
        super(envio);
    }

    @Override
    public double getCosto() {
        return envio.getCosto() + (envio.getCosto() * PORCENTAJE_REFRIGERACION);
    }

    @Override
    public int getTiempoEstimado() {
        return envio.getTiempoEstimado() + DIAS_EXTRA;
    }

    @Override
    public String getDescripcion() {
        return envio.getDescripcion() + " con refrigeración";
    }
} 