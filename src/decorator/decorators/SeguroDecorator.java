package decorator.decorators;

import decorator.base.Envio;

/**
 * Decorador que añade seguro a un envío.
 * Implementa el patrón Decorator para añadir la funcionalidad de seguro.
 */
public class SeguroDecorator extends EnvioDecorator {
    private static final double PORCENTAJE_SEGURO = 0.1; // 10% del valor del envío

    /**
     * Constructor de la clase SeguroDecorator
     * @param envio Envío a decorar con seguro
     */
    public SeguroDecorator(Envio envio) {
        super(envio);
    }

    @Override
    public double getCosto() {
        return envio.getCosto() + (envio.getCosto() * PORCENTAJE_SEGURO);
    }

    @Override
    public String getDescripcion() {
        return envio.getDescripcion() + " con seguro";
    }
} 