package strategy.implementations;

import strategy.core.EstrategiaEnvio;
import decorator.base.Envio;

/**
 * Implementación de la estrategia de envío rápido.
 * Prioriza la velocidad sobre el costo.
 */
public class EstrategiaRapida implements EstrategiaEnvio {
    private static final double MULTIPLICADOR_COSTO = 2.0;
    private static final int TIEMPO_BASE = 1;

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
        return "Estrategia de envío rápido (prioridad velocidad)";
    }
} 