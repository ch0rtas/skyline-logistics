package strategy.implementations;

import strategy.core.EstrategiaEnvio;
import decorator.base.Envio;

/**
 * Implementación de la estrategia de envío económico.
 * Prioriza el costo sobre la velocidad.
 */
public class EstrategiaEconomica implements EstrategiaEnvio {
    private static final double MULTIPLICADOR_COSTO = 0.8;
    private static final int TIEMPO_BASE = 5;

    @Override
    public double calcularCosto(Envio envio) {
        return envio.getCosto() * MULTIPLICADOR_COSTO;
    }

    @Override
    public int calcularTiempoEstimado(Envio envio) {
        return TIEMPO_BASE;
    }

    @Override
    public String getDescripcion() {
        return "Estrategia de envío económico (prioridad costo)";
    }
} 