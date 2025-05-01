package decorator.decorators;

import decorator.base.Envio;

/**
 * Decorador que añade prioridad urgente a un envío.
 * Implementa el patrón Decorator para añadir la funcionalidad de envío urgente.
 */
public class UrgenteDecorator extends EnvioDecorator {
    private static final double PORCENTAJE_URGENTE = 0.5; // 50% del valor del envío
    private static final int DIAS_REDUCCION = 2; // Reduce 2 días del tiempo estimado

    /**
     * Constructor de la clase UrgenteDecorator
     * @param envio Envío a decorar con prioridad urgente
     */
    public UrgenteDecorator(Envio envio) {
        super(envio);
    }

    @Override
    public double getCosto() {
        return envio.getCosto() + (envio.getCosto() * PORCENTAJE_URGENTE);
    }

    @Override
    public int getTiempoEstimado() {
        int tiempoBase = envio.getTiempoEstimado();
        return Math.max(1, tiempoBase - DIAS_REDUCCION); // Mínimo 1 día
    }

    @Override
    public String getDescripcion() {
        return envio.getDescripcion() + " con prioridad urgente";
    }
} 