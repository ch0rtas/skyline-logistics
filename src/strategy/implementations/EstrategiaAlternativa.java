package strategy.implementations;

import strategy.core.EstrategiaEnvio;
import decorator.base.Envio;

/**
 * Implementación de la estrategia de envío alternativo.
 * Se utiliza cuando hay bloqueos o problemas en la ruta principal.
 */
public class EstrategiaAlternativa implements EstrategiaEnvio {
    private static final double MULTIPLICADOR_COSTO = 1.5;
    private static final int TIEMPO_BASE = 3;

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
        return "Estrategia de envío alternativo (ruta secundaria)";
    }
} 