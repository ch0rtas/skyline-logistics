package strategy.core;

import decorator.base.Envio;

/**
 * Interfaz que define el contrato para las estrategias de envío.
 * Implementa el patrón Strategy para permitir diferentes métodos de envío.
 */
public interface EstrategiaEnvio {
    /**
     * Calcula el costo del envío según la estrategia
     * @param envio Envío a calcular
     * @return double con el costo calculado
     */
    double calcularCosto(Envio envio);

    /**
     * Calcula el tiempo estimado de entrega según la estrategia
     * @param envio Envío a calcular
     * @return int con el tiempo estimado en días
     */
    int calcularTiempoEstimado(Envio envio);

    /**
     * Obtiene la descripción de la estrategia
     * @return String con la descripción
     */
    String getDescripcion();
} 